package com.microsoft.azure.storage.blob;

import com.microsoft.azure.storage.pipeline.Pipeline;
import com.microsoft.azure.storage.pipeline.RequestPolicyNode;
import com.microsoft.rest.v2.http.HttpRequest;
import com.microsoft.rest.v2.http.HttpResponse;
import com.microsoft.rest.v2.policy.RequestPolicy;
import rx.Single;

public final class AnonymousCredentials implements CredentialsInterface {

    private final class AnonymousCredentialsPolicy implements RequestPolicy {
        final RequestPolicyNode requestPolicyNode;

        AnonymousCredentialsPolicy(RequestPolicyNode requestPolicyNode) {
            this.requestPolicyNode = requestPolicyNode;
        }

        // For anonymous credentials, this is effectively a no-op
        public Single<HttpResponse> sendAsync(HttpRequest request) { return requestPolicyNode.sendAsync(request); }
    }

    // New creates a credential policy object.
    @Override
    public RequestPolicy create(RequestPolicyNode requestPolicyNode) {
        return new AnonymousCredentialsPolicy(requestPolicyNode);
    }
}

