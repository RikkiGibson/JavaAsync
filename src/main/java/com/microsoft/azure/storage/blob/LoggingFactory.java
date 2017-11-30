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

import com.microsoft.azure.storage.pipeline.*;
import com.microsoft.rest.v2.http.HttpRequest;
import com.microsoft.rest.v2.http.HttpResponse;
import com.microsoft.rest.v2.policy.RequestPolicy;
import io.netty.handler.codec.http.HttpResponseStatus;
//import org.apache.log4j.Level;
import rx.Single;
import rx.functions.Action1;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;

/**
 * Factory for logging requests and responses
 */
public final class LoggingFactory implements IRequestPolicyFactory {

    private final RequestLoggingOptions requestLoggingOptions;

    private int tryCount;

    private long operationStartTime;

    public LoggingFactory(RequestLoggingOptions requestLoggingOptions) {
        this.requestLoggingOptions = requestLoggingOptions;
    }

    private final class RequestLoggingPolicy implements RequestPolicy {

        private final AtomicReference<RequestPolicyNode> requestPolicyNode;

        final private AtomicReference<LoggingFactory> factory;

        private AtomicLong requestStartTime;

        RequestLoggingPolicy(RequestPolicyNode requestPolicyNode, LoggingFactory factory) {
            this.requestPolicyNode = new AtomicReference<>(requestPolicyNode);
            this.factory = new AtomicReference<>(factory);
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
            this.factory.get().tryCount++;
            this.requestStartTime.set(System.currentTimeMillis());
            if (this.factory.get().tryCount == 1) {
                this.factory.get().operationStartTime = requestStartTime.get();
            }

            if (requestPolicyNode.get().shouldLog(LogLevel.INFO)) {
                this.requestPolicyNode.get().log(LogLevel.INFO, "'%s'==> OUTGOING REQUEST (Try number='%d')%n",
                        request.url(), this.factory.get().tryCount);
            }

            // TODO: Need to change logic slightly when support for writing to event log/ sys log support is added
            return requestPolicyNode.get().sendAsync(request)
                    .doOnError(new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            if (requestPolicyNode.get().shouldLog(LogLevel.ERROR)) {
                                requestPolicyNode.get().log(LogLevel.ERROR,
                                    "Unexpected failure attempting to make request.%nError message:'%s'%n",
                                        throwable.getMessage());
                            }
                        }
                    })
                    .doOnSuccess(new Action1<HttpResponse>() {
                        @Override
                        public void call(HttpResponse response) {
                            // check if error should be logged since there is nothing of higher priority
                            if (!requestPolicyNode.get().shouldLog(LogLevel.ERROR)) {
                                return;
                            }

                            if (requestPolicyNode.get().shouldLog(LogLevel.INFO)) {
                                // assume success and default to informational logging
                                requestPolicyNode.get().log(LogLevel.INFO, "Successful Operation\n");
                            }

                            // if the response took too long, we'll upgrade to warning.
                            long requestCompletionTime = System.currentTimeMillis() - requestStartTime.get();
                            if (requestCompletionTime >=
                                    factory.get().requestLoggingOptions.getMinDurationToLogSlowRequestsInMs()) {
                                // log a warning if the try duration exceeded the specified threshold
                                if (requestPolicyNode.get().shouldLog(LogLevel.WARNING)) {
                                    requestPolicyNode.get().log(LogLevel.WARNING,
                                    "Slow Operation. Try duration of %l ms", requestCompletionTime);
                                }
                            }

                            if (response.statusCode() >= 500 ||
                                    (response.statusCode() >= 400 && response.statusCode() != 404 &&
                                     response.statusCode() != 409 && response.statusCode() != 412 &&
                                     response.statusCode() != 416)) {
                                requestPolicyNode.get().log(LogLevel.ERROR,
                                        "HTTP request failed with status code:'%d'%n", response.statusCode());
                            }

                            if (requestPolicyNode.get().shouldLog(LogLevel.INFO)) {
                                LoggingFactory loggingFactory = factory.get();
                                long requestEndTime = System.nanoTime();
                                long requestDuration = requestEndTime - requestStartTime.get();
                                long operationDuration = requestEndTime - factory.get().operationStartTime;
                                requestPolicyNode.get().log(LogLevel.INFO,
                                        "Request try:'%d', request duration:'%d' ms, operation duration:'%d'",
                                        loggingFactory.tryCount, requestDuration, operationDuration);
                            }
                        }
                    });
        }
    }

    @Override
    public RequestPolicy create(RequestPolicyNode requestPolicyNode) {
        return new RequestLoggingPolicy(requestPolicyNode, this);
    }
}
