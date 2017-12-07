/**
 * Copyright Microsoft Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.microsoft.azure.storage.blob;

import com.microsoft.rest.v2.http.*;
import com.microsoft.rest.v2.policy.RequestPolicy;
import com.microsoft.rest.v2.policy.RetryPolicy;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.QueryStringDecoder;
import org.apache.commons.lang3.StringUtils;
import rx.Single;
import rx.functions.Action1;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static com.microsoft.azure.storage.blob.Utility.getGMTTime;

public final class SharedKeyCredentials implements ICredentials {

    private final String accountName;

    private final byte[] key;

    /**
     * Initialized a new instance of SharedKeyCredentials contains an account's name and its primary or secondary key.
     * @param accountName
     *      The account name associated with the request.
     * @param key
     *      A string that represent the account access key.
     */
    public SharedKeyCredentials(String accountName, String key) {
        this.accountName = accountName;
        this.key = key.getBytes();
    }

    /**
     * Gets the account name associated with the request.
     * @return
     *      The account name.
     */
    public String getAccountName() {
        return accountName;
    }

    private final class SharedKeyCredentialsPolicy implements RequestPolicy {

        private final RequestPolicy requestPolicy;

        private final RequestPolicy.Options options;

        private final SharedKeyCredentials factory;

        SharedKeyCredentialsPolicy(RequestPolicy requestPolicy, RequestPolicy.Options options, SharedKeyCredentials factory) {
            this.requestPolicy = requestPolicy;
            this.options = options;
            this.factory = factory;
        }

        /**
         * Sign the request
         * @param request
         *      the request to sign
         * @return
         *      A {@link Single} representing the HTTP response that will arrive asynchronously.
         */
        @Override
        public Single<HttpResponse> sendAsync(final HttpRequest request) {
            if (request.headers().value(Constants.HeaderConstants.DATE) == null) {
                request.headers().set(Constants.HeaderConstants.DATE, getGMTTime(new Date()));
            }

            final AtomicReference<String> stringToSign = new AtomicReference<>();
            try {
                stringToSign.set(this.factory.buildStringToSign(request));
                final String computedBase64Signature = this.factory.computeHmac256(stringToSign.get());
                request.headers().set(Constants.HeaderConstants.AUTHORIZATION, "SharedKey " + this.factory.accountName + ":"  + computedBase64Signature);
            } catch (Exception e) {
                return Single.error(e);
            }

            Single<HttpResponse> response = requestPolicy.sendAsync(request);
            return response.doOnSuccess(new Action1<HttpResponse>() {
                @Override
                public void call(HttpResponse response) {
                    if (response.statusCode() == HttpResponseStatus.FORBIDDEN.code()) {
                        //if (options.logger().shouldLogRequest(LogLevel.ERROR)) {
                            options.logger().log(HttpPipeline.LogLevel.ERROR, "===== HTTP Forbidden status, String-to-Sign:%n'%s'%n===============================%n", stringToSign.get());
                        //}
                    }
                }
            });
        }
    }

    @Override
    public RequestPolicy create(RequestPolicy nextRequestPolicy, RequestPolicy.Options options) {
        return new SharedKeyCredentialsPolicy(nextRequestPolicy, options, this);
    }

    /**
     * Constructs a canonicalized string for signing a request.
     *
     * @param request
     *  the request to canonicalize
     * @return a canonicalized string.
     */
    private String buildStringToSign(final HttpRequest request) throws Exception {
        final HttpHeaders httpHeaders = request.headers();
        String contentLength = getStandardHeaderValue(httpHeaders, Constants.HeaderConstants.CONTENT_LENGTH);
        contentLength = contentLength.equals("0") ? Constants.EMPTY_STRING : contentLength;

        // TODO: Change to String.join when Java 7 support is removed
        return StringUtils.join(
                new String[]{
                        request.httpMethod(),
                        getStandardHeaderValue(httpHeaders, Constants.HeaderConstants.CONTENT_ENCODING),
                        getStandardHeaderValue(httpHeaders, Constants.HeaderConstants.CONTENT_LANGUAGE),
                        contentLength,
                        getStandardHeaderValue(httpHeaders, Constants.HeaderConstants.CONTENT_MD5),
                        getStandardHeaderValue(httpHeaders, Constants.HeaderConstants.CONTENT_TYPE),
                        // x-ms-date header exists, so don't sign date header
                        Constants.EMPTY_STRING,
                        getStandardHeaderValue(httpHeaders, Constants.HeaderConstants.IF_MODIFIED_SINCE),
                        getStandardHeaderValue(httpHeaders, Constants.HeaderConstants.IF_MATCH),
                        getStandardHeaderValue(httpHeaders, Constants.HeaderConstants.IF_NONE_MATCH),
                        getStandardHeaderValue(httpHeaders, Constants.HeaderConstants.IF_UNMODIFIED_SINCE),
                        getStandardHeaderValue(httpHeaders, Constants.HeaderConstants.RANGE),
                        getAdditionalXmsHeaders(httpHeaders),
                        getCanonicalizedResource(request.url())
                },
                '\n'
        );
    }

    private void appendCanonicalizedElement(final StringBuilder builder, final String element) {
        builder.append("\n");
        builder.append(element);
    }

    private String getAdditionalXmsHeaders(final HttpHeaders headers) {
        // Add only headers that begin with 'x-ms-'
        final ArrayList<String> xmsHeaderNameArray = new ArrayList<String>();
        for (HttpHeader header : headers) {
            String lowerCaseHeader = header.name().toLowerCase(Utility.LOCALE_US);
            if (lowerCaseHeader.startsWith(Constants.PREFIX_FOR_STORAGE_HEADER)) {
                xmsHeaderNameArray.add(lowerCaseHeader);
            }
        }

        if (xmsHeaderNameArray.isEmpty()) {
            return Constants.EMPTY_STRING;
        }

        Collections.sort(xmsHeaderNameArray);

        final StringBuilder canonicalizedHeaders = new StringBuilder();
        for (final String key : xmsHeaderNameArray) {
            if (canonicalizedHeaders.length() > 0) {
                canonicalizedHeaders.append('\n');
            }

            canonicalizedHeaders.append(key);
            canonicalizedHeaders.append(':');
            canonicalizedHeaders.append(headers.value(key));
        }

        return canonicalizedHeaders.toString();
    }

    /**
     * Canonicalized the resource to sign.
     * @param requestURL
     *      A string that represents the request URL.
     * @return
     *      The canonicalized resource to sign.
     * @throws MalformedURLException
     * @throws UnsupportedEncodingException
     */
    private String getCanonicalizedResource(String requestURL) throws MalformedURLException, UnsupportedEncodingException {
        requestURL = Utility.safeDecode(requestURL);
        // Resource path
        final StringBuilder canonicalizedResource = new StringBuilder("/");
        canonicalizedResource.append(this.accountName);

        // Note that AbsolutePath starts with a '/'.
        QueryStringDecoder urlDecoder = new QueryStringDecoder(requestURL);
        if (urlDecoder.path().length() > 0) {
            String path = urlDecoder.path();

            // There are two slashes after the protocol and another slash after the account portion of the path
            path = path.substring(StringUtils.ordinalIndexOf(path, "/", 2) + 1);
            canonicalizedResource.append(path);
        }
        else {
            canonicalizedResource.append('/');
        }

        // check for no query params and return
        Map<String, List<String>> queryParams = urlDecoder.parameters();
        if (queryParams.size() == 0) {
            canonicalizedResource.append('\n');
            return canonicalizedResource.toString();
        }

        ArrayList<String> queryParamNames = new ArrayList<String>(queryParams.keySet());
        Collections.sort(queryParamNames);

        for (int i = 0; i < queryParamNames.size(); i++) {
            final String queryParamName = queryParamNames.get(i);
            final List<String> queryParamValues = queryParams.get(queryParamName);
            Collections.sort(queryParamValues);

            // concatenation of the query param name + colon + join of query param values which are commas separated
            canonicalizedResource.append("\n" + queryParamName.toLowerCase(Locale.US) + ":" + StringUtils.join(queryParamValues, ','));
        }

        // append to main string builder the join of completed params with new line
        return canonicalizedResource.toString();
    }

    /**
     * Returns the standard header value from the specified connection request, or an empty string if no header value
     * has been specified for the request.
     *
     * @param httpHeaders
     *      A <code>HttpHeaders</code> object that represents the headers for the request.
     * @param headerName
     *      A {@code String} that represents the name of the header being requested.
     *
     * @return A {@code String} that represents the header value, or <code>null</code> if there is no corresponding
     *      header value for <code>headerName</code>.
     */
    private String getStandardHeaderValue(final HttpHeaders httpHeaders, final String headerName) {
        final String headerValue = httpHeaders.value(headerName);

        return headerValue == null ? Constants.EMPTY_STRING : headerValue;
    }

    /**
     * Computes a signature for the specified string using the HMAC-SHA256 algorithm.
     *
     * @param stringToSign
     *      The UTF-8-encoded string to sign.
     *
     * @return
     *      A {@code String} that contains the HMAC-SHA256-encoded signature.
     *
     * @throws InvalidKeyException
     *      If the key is not a valid Base64-encoded string.
     */
    synchronized String computeHmac256(final String stringToSign) throws InvalidKeyException {
        byte[] utf8Bytes = null;
        try {
            utf8Bytes = stringToSign.getBytes(Constants.UTF8_CHARSET);
        }
        catch (final UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }

        // Initializes the HMAC-SHA256 Mac and SecretKey.
        Mac hmacSha256;
        try {
            hmacSha256 = Mac.getInstance("HmacSHA256");
        }
        catch (final NoSuchAlgorithmException e) {
            throw new IllegalArgumentException();
        }

        hmacSha256.init(new SecretKeySpec(this.key, "HmacSHA256"));
        return Base64.encode(hmacSha256.doFinal(utf8Bytes));
    }
}

