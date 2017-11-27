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

import com.microsoft.azure.storage.pipeline.IRequestPolicyFactory;
import com.microsoft.azure.storage.pipeline.Pipeline;
import com.microsoft.azure.storage.pipeline.RequestPolicyFactoryInterface;
import com.microsoft.azure.storage.pipeline.RequestPolicyNode;
import com.microsoft.rest.v2.http.HttpRequest;
import com.microsoft.rest.v2.http.HttpResponse;
import com.microsoft.rest.v2.policy.RequestPolicy;
import rx.Single;

public final class TelemetryFactory implements IRequestPolicyFactory {

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
        final RequestPolicyNode requestPolicyNode;

        TelemetryPolicy(RequestPolicyNode requestPolicyNode) {
            this.requestPolicyNode = requestPolicyNode;
        }

        public Single<HttpResponse> sendAsync(HttpRequest request) {
            request.headers().set(Constants.HeaderConstants.USER_AGENT, userAgent);
            return this.requestPolicyNode.sendAsync(request);
        }
    }

    @Override
    public RequestPolicy create(RequestPolicyNode requestPolicyNode) {
        return new TelemetryPolicy(requestPolicyNode);
    }
}
