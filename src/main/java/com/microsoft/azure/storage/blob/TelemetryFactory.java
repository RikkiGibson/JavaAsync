package com.microsoft.azure.storage.blob;

import com.microsoft.rest.v2.http.HttpRequest;
import com.microsoft.rest.v2.http.HttpResponse;
import com.microsoft.rest.v2.policy.RequestPolicy;
import rx.Single;

public final class TelemetryFactory implements RequestPolicy.Factory {

    final private String userAgent;

    public TelemetryFactory(TelemetryOptions telemetryOptions) {
        String userAgentPrefix = telemetryOptions.UserAgentPrefix() == null ? Constants.EMPTY_STRING : telemetryOptions.UserAgentPrefix();
        userAgent = userAgentPrefix + ' ' +
                Constants.HeaderConstants.USER_AGENT_PREFIX + '/' + Constants.HeaderConstants.USER_AGENT_VERSION +
                String.format(Utility.LOCALE_US, "(JavaJRE %s; %s %s)",
                    System.getProperty("java.version"),
                    System.getProperty("os.name").replaceAll(" ", ""),
                    System.getProperty("os.version"));
    }

    private final class TelemetryPolicy implements RequestPolicy {
        final RequestPolicy nextPolicy;

        public TelemetryPolicy(RequestPolicy nextPolicy) {
            this.nextPolicy = nextPolicy;
        }

        public Single<HttpResponse> sendAsync(HttpRequest request) {
            request.headers().set(Constants.HeaderConstants.USER_AGENT, userAgent);
            return nextPolicy.sendAsync(request);
        }
    }

    @Override
    public RequestPolicy create(RequestPolicy nextPolicy) {
        return new TelemetryFactory.TelemetryPolicy(nextPolicy);
    }
}
