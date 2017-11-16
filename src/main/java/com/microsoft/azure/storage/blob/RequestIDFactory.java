package com.microsoft.azure.storage.blob;

import com.microsoft.azure.storage.pipeline.IRequestPolicyFactory;
import com.microsoft.azure.storage.pipeline.Pipeline;
import com.microsoft.azure.storage.pipeline.RequestPolicyNode;
import com.microsoft.rest.v2.http.HttpRequest;
import com.microsoft.rest.v2.http.HttpResponse;
import com.microsoft.rest.v2.policy.RequestPolicy;
import rx.Single;

import java.util.UUID;

final class RequestIDFactory implements IRequestPolicyFactory {

    private final class RequestIDPolicy implements RequestPolicy {
        final RequestPolicyNode requestPolicyNode;

        public RequestIDPolicy(RequestPolicyNode requestPolicyNode) {
            this.requestPolicyNode = requestPolicyNode;
        }

        public Single<HttpResponse> sendAsync(HttpRequest request) {
            request.headers().set(Constants.HeaderConstants.CLIENT_REQUEST_ID_HEADER, UUID.randomUUID().toString());
            return requestPolicyNode.sendAsync(request);
        }
    }

    @Override
    public RequestPolicy create(RequestPolicyNode requestPolicyNode) {
        return new RequestIDPolicy(requestPolicyNode);
    }
}
