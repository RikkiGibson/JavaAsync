package com.microsoft.azure.storage.blob;

import com.microsoft.azure.storage.pipeline.RequestPolicyNode;
import com.microsoft.rest.v2.http.HttpRequest;
import com.microsoft.rest.v2.http.HttpResponse;
import com.microsoft.rest.v2.policy.RequestPolicy;
import rx.Single;

/**
 * Anonymous credentials are to be used with with HTTP(S) requests
 * that read blobs from public containers or requests that use a
 * Shared Access Signature (SAS).
 */
public final class AnonymousCredentials implements CredentialsInterface {

    /**
     * Anonymous credentials are to be used with with HTTP(S) requests
     * that read blobs from public containers or requests that use a
     * Shared Access Signature (SAS).
     */
    private final class AnonymousCredentialsPolicy implements RequestPolicy {
        final RequestPolicyNode requestPolicyNode;

        AnonymousCredentialsPolicy(RequestPolicyNode requestPolicyNode) {
            this.requestPolicyNode = requestPolicyNode;
        }

        // For anonymous credentials, this is effectively a no-op
        public Single<HttpResponse> sendAsync(HttpRequest request) { return requestPolicyNode.sendAsync(request); }
    }

    /**
     * Creates a new <code>AnonymousCredentialsPolicy</code>
     * @param requestPolicyNode
     * @return
     */
    @Override
    public RequestPolicy create(RequestPolicyNode requestPolicyNode) {
        return new AnonymousCredentialsPolicy(requestPolicyNode);
    }
}