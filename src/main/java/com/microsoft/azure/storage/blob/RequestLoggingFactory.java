package com.microsoft.azure.storage.blob;

import com.microsoft.azure.storage.pipeline.PipelineLogger;
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

public final class RequestLoggingFactory implements RequestPolicy.Factory {

    private final RequestLoggingOptions requestLoggingOptions;

    private int tryCount;

    private long operationStartTime;

    public RequestLoggingFactory(RequestLoggingOptions requestLoggingOptions) {
        this.requestLoggingOptions = requestLoggingOptions;
        PipelineLogger.initialize(this.requestLoggingOptions.getLoggingLevel());
    }

    private final class RequestLoggingPolicy implements RequestPolicy {

        final private RequestPolicy nextPolicy;

        final private AtomicReference<RequestLoggingFactory> factory;

        private AtomicLong requestStartTime;

        public RequestLoggingPolicy(RequestPolicy nextPolicy, RequestLoggingFactory factory) {
            this.nextPolicy = nextPolicy;
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

            if (PipelineLogger.shouldLog(Level.INFO)) {
                //PipelineLogger.info("'%s'==> OUTGOING REQUEST (Try number='%d')%n", request.url(), this.factory.get().tryCount);
            }

            Single<HttpResponse> httpResponse = nextPolicy.sendAsync(request);
            return httpResponse
                    .doOnError(new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            if (PipelineLogger.shouldLog(Level.SEVERE)) {
                                //PipelineLogger.error("Unexpected failure attempting to make request.%nError message:'%s'%n", throwable.getMessage());
                            }
                        }
                    })
                    .doOnSuccess(new Action1<HttpResponse>() {
                        @Override
                        public void call(HttpResponse response) {
                            if (!PipelineLogger.shouldLog(Level.SEVERE)) {
                                return;
                            }

                            if (response.statusCode() >= 500 || (response.statusCode() >= 400 && response.statusCode() != 409 && response.statusCode() != 412)) {
                                if (PipelineLogger.shouldLog(Level.SEVERE)) {
                                    //PipelineLogger.error("HTTP request failed with status code:'%d'%n", response.statusCode());
                                }
                            }
                            if (PipelineLogger.shouldLog(Level.INFO)) {
                                RequestLoggingFactory loggingFactory = factory.get();
                                long requestEndTime = System.currentTimeMillis();
                                long requestDuration = requestEndTime - requestStartTime.get();
                                long operationDuration = requestEndTime - factory.get().operationStartTime;
                                //PipelineLogger.info("Request try:'%d', request duration:'%d' ms, operation duration:'%d'", loggingFactory.tryCount, requestDuration, operationDuration);
                            }
                        }
                    });
        }
    }

    @Override
    public RequestPolicy create(RequestPolicy next) {
        return new RequestLoggingPolicy(next, this);
    }
}
