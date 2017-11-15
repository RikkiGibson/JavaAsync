package com.microsoft.azure.storage.blob;

import com.microsoft.rest.v2.http.HttpRequest;
import com.microsoft.rest.v2.http.HttpResponse;
import com.microsoft.rest.v2.policy.RequestPolicy;
import rx.Single;

import java.util.UUID;

final class RequestIDFactory implements RequestPolicy.Factory {

    private final class RequestIDPolicy implements RequestPolicy {
        final RequestPolicy nextPolicy;

        public RequestIDPolicy(RequestPolicy nextPolicy) {
            this.nextPolicy = nextPolicy;
        }

        public Single<HttpResponse> sendAsync(HttpRequest request) {
            request.headers().set(Constants.HeaderConstants.CLIENT_REQUEST_ID_HEADER, UUID.randomUUID().toString());
            return nextPolicy.sendAsync(request);
        }
    }
    @Override
    public RequestPolicy create(RequestPolicy nextPolicy) {
        return new RequestIDPolicy(nextPolicy);
    }
}
