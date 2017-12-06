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

import com.microsoft.rest.v2.http.HttpPipeline;
import com.microsoft.rest.v2.http.HttpRequest;
import com.microsoft.rest.v2.http.HttpResponse;
import com.microsoft.rest.v2.policy.RequestPolicy;
import io.netty.handler.codec.http.HttpResponseStatus;
//import org.apache.log4j.Level;
import io.netty.handler.codec.http.HttpStatusClass;
import rx.Single;
import rx.functions.Action1;

import java.net.HttpURLConnection;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;

/**
 * Factory for logging requests and responses
 */
public final class LoggingFactory implements RequestPolicy.Factory {

    private final RequestLoggingOptions requestLoggingOptions;

    public LoggingFactory(RequestLoggingOptions requestLoggingOptions) {
        this.requestLoggingOptions = requestLoggingOptions;
    }

    private final class RequestLoggingPolicy implements RequestPolicy {

        private int tryCount;

        private long operationStartTime;

        private RequestPolicy.Options options;

        private final RequestPolicy requestPolicy;

        final private LoggingFactory factory;

        private Long requestStartTime;

        RequestLoggingPolicy(RequestPolicy requestPolicy, RequestPolicy.Options options, LoggingFactory factory) {
            this.requestPolicy = requestPolicy;
            this.options = options;
            this.factory = factory;
        }

        /**
         * Signed the request
         * @param request
         *      the request to sign
         * @return
         *      A {@link Single} representing the HTTP response that will arrive asynchronously.
         */
        @Override
        public Single<HttpResponse> sendAsync(final HttpRequest request) {
            this.tryCount++;
            this.requestStartTime = System.nanoTime();
            if (this.tryCount == 1) {
                this.operationStartTime = requestStartTime;
            }

            //if (this.options.logger().shouldLog(LogLevel.INFO)) {
                this.options.logger().log(HttpPipeline.LogLevel.INFO,
                        "'%s'==> OUTGOING REQUEST (Try number='%d')%n", request.url(), this.tryCount);
            //}

            // TODO: Need to change logic slightly when support for writing to event log/sys log support is added
            return requestPolicy.sendAsync(request)
                    .doOnError(new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            //if (options.logger().shouldLog(LogLevel.ERROR)) {
                                options.logger().log(HttpPipeline.LogLevel.ERROR,
                                        "Unexpected failure attempting to make request.%nError message:'%s'%n",
                                        throwable.getMessage());
                            //}
                        }
                    })
                    .doOnSuccess(new Action1<HttpResponse>() {
                        @Override
                        public void call(HttpResponse response) {
                            long requestCompletionTime = System.nanoTime() - requestStartTime;
                            HttpPipeline.LogLevel currentLevel = HttpPipeline.LogLevel.INFO;
                            // check if error should be logged since there is nothing of higher priority
//                            if (!options.logger().shouldLog(LogLevel.ERROR)) {
//                                return;
//                            }

                            String logMessage = Constants.EMPTY_STRING;
                            //if (options.logger().shouldLog(LogLevel.INFO)) {
                                // assume success and default to informational logging
                                logMessage = "Successfully Received Response" + System.lineSeparator();
                            //}

                            // if the response took too long, we'll upgrade to warning.
                            if (requestCompletionTime >=
                                    factory.requestLoggingOptions.getMinDurationToLogSlowRequestsInMs()) {
                                // log a warning if the try duration exceeded the specified threshold
                                //if (options.logger().shouldLog(LogLevel.WARNING)) {
                                    currentLevel = HttpPipeline.LogLevel.WARNING;
                                    logMessage = String.format("SLOW OPERATION. Duration > %d ms.%n", factory.requestLoggingOptions.getMinDurationToLogSlowRequestsInMs());
                                //}
                            }

                            if (response.statusCode() >= HttpURLConnection.HTTP_INTERNAL_ERROR ||
                                    (response.statusCode() >= HttpURLConnection.HTTP_BAD_REQUEST && response.statusCode() != HttpURLConnection.HTTP_NOT_FOUND &&
                                     response.statusCode() != HttpURLConnection.HTTP_CONFLICT && response.statusCode() != HttpURLConnection.HTTP_PRECON_FAILED &&
                                     response.statusCode() != 416 /* 416 is missing from the Enum but it is Range Not Satisfiable */)) {
                                String errorString = String.format("REQUEST ERROR%nHTTP request failed with status code:'%d'%n", response.statusCode());
                                if (currentLevel == HttpPipeline.LogLevel.WARNING) {
                                    logMessage += errorString;
                                }
                                else {
                                    logMessage = errorString;
                                }

                                currentLevel = HttpPipeline.LogLevel.ERROR;
                                // TODO: LOG THIS TO WINDOWS EVENT LOG/SYS LOG
                            }

                            //if (shouldlog(currentLevel) {
                                long requestEndTime = System.nanoTime();
                                long requestDuration = requestEndTime - requestStartTime;
                                long operationDuration = requestEndTime - operationStartTime;
                                String messageInfo = String.format(
                                        "Request try:'%d', request duration:'%d' ms, operation duration:'%d' ms%n",
                                        tryCount, requestDuration, operationDuration);
                                options.logger().log(HttpPipeline.LogLevel.INFO, logMessage + messageInfo);
                            // }
                        }
                    });
        }
    }

    @Override
    public RequestPolicy create(RequestPolicy next, RequestPolicy.Options options) {
        return new RequestLoggingPolicy(next, options, this);
    }
}
