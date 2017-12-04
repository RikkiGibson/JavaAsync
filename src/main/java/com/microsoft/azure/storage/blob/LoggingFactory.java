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
            this.requestStartTime = System.currentTimeMillis();
            if (this.tryCount == 1) {
                this.operationStartTime = requestStartTime;
            }

            //if (this.options.logger().shouldLog(LogLevel.INFO)) {
                this.options.logger().log(/*LogLevel.INFO,*/
                        String.format("'%s'==> OUTGOING REQUEST (Try number='%d')%n", request.url(), this.tryCount));
            //}

            // TODO: Need to change logic slightly when support for writing to event log/sys log support is added
            return requestPolicy.sendAsync(request)
                    .doOnError(new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            //if (options.logger().shouldLog(LogLevel.ERROR)) {
                                options.logger().log(/*LogLevel.ERROR,*/
                                        String.format("Unexpected failure attempting to make request.%nError message:'%s'%n",
                                        throwable.getMessage()));
                            //}
                        }
                    })
                    .doOnSuccess(new Action1<HttpResponse>() {
                        @Override
                        public void call(HttpResponse response) {
                            long requestCompletionTime = System.currentTimeMillis() - requestStartTime;
                            // check if error should be logged since there is nothing of higher priority
//                            if (!options.logger().shouldLog(LogLevel.ERROR)) {
//                                return;
//                            }

                            //if (options.logger().shouldLog(LogLevel.INFO)) {
                                // assume success and default to informational logging
                                options.logger().log(/*LogLevel.INFO,*/ "Successfully Received Response\n");
                            //}

                            // if the response took too long, we'll upgrade to warning.
                            if (requestCompletionTime >=
                                    factory.requestLoggingOptions.getMinDurationToLogSlowRequestsInMs()) {
                                // log a warning if the try duration exceeded the specified threshold
                                //if (options.logger().shouldLog(LogLevel.WARNING)) {
                                    options.logger().log(/*LogLevel.WARNING,*/
                                            String.format("Slow Operation. Try duration of %l ms", requestCompletionTime));
                                //}
                            }

                            // TODO: Find symoblic reference nad look at updated Go code and change to just one log statement
                            if (response.statusCode() >= 500 ||
                                    (response.statusCode() >= 400 && response.statusCode() != 404 &&
                                     response.statusCode() != 409 && response.statusCode() != 412 &&
                                     response.statusCode() != 416)) {
                                options.logger().log(/*LogLevel.ERROR,*/
                                        String.format("HTTP request failed with status code:'%d'%n",
                                                response.statusCode()));
                            }

                            //if ( options.logger().shouldLog(LogLevel.INFO)) {
                                long requestEndTime = System.nanoTime();
                                long requestDuration = requestEndTime - requestStartTime;
                                long operationDuration = requestEndTime - operationStartTime;
                                options.logger().log(/*LogLevel.INFO,*/ String.format(
                                        "Request try:'%d', request duration:'%d' ms, operation duration:'%d' ms",
                                        tryCount, requestDuration, operationDuration));
                            //}
                        }
                    });
        }
    }

    @Override
    public RequestPolicy create(RequestPolicy next, RequestPolicy.Options options) {
        return new RequestLoggingPolicy(next, options, this);
    }
}
