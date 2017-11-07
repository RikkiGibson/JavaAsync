package com.microsoft.azure.storage.blob;

import com.microsoft.rest.v2.http.HttpRequest;
import com.microsoft.rest.v2.http.HttpResponse;
import com.microsoft.rest.v2.policy.RequestPolicy;
import rx.Single;

public final class AnonymousCredentials implements CredentialsInterface {

    private final class AnonymousCredentialsPolicy implements RequestPolicy {
        final RequestPolicy nextPolicy;

        public AnonymousCredentialsPolicy(RequestPolicy nextPolicy) {
            this.nextPolicy = nextPolicy;
        }

        // For anonymous credentials, this is effectively a no-op
        public Single<HttpResponse> sendAsync(HttpRequest request) { return nextPolicy.sendAsync(request); }
    }

    // New creates a credential policy object.
    @Override
    public RequestPolicy create(RequestPolicy nextPolicy) {
        return new AnonymousCredentialsPolicy(nextPolicy);
    }

}

