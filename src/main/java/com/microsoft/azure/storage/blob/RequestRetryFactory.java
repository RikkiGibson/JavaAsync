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

import com.microsoft.rest.v2.http.HttpRequest;
import com.microsoft.rest.v2.http.HttpResponse;
import com.microsoft.rest.v2.policy.RequestPolicy;
import rx.Single;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Factory for retrying requests
 */
public final class RequestRetryFactory implements RequestPolicy.Factory {

    private final RequestRetryOptions requestRetryOptions;

    private int tryCount;

    private long operationStartTime;

    public RequestRetryFactory(RequestRetryOptions requestRetryOptions) {
        this.requestRetryOptions = requestRetryOptions;
    }

    private final class RequestRetryPolicy implements RequestPolicy {

        private final RequestPolicy requestPolicy;

        final private RequestRetryFactory factory;

        final private RequestPolicy.Options options;

        RequestRetryPolicy(RequestPolicy requestPolicy, RequestPolicy.Options options, RequestRetryFactory factory) {
            this.requestPolicy = requestPolicy;
            this.options = options;
            this.factory = factory;
        }

        @Override
        public Single<HttpResponse> sendAsync(HttpRequest httpRequest) {
            return null;
        }
    }

    @Override
    public RequestPolicy create(RequestPolicy next, RequestPolicy.Options options) {
        return new RequestRetryPolicy(next, options, this);
    }
}
