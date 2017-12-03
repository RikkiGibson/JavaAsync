package com.microsoft.azure.storage.pipeline;

import com.microsoft.rest.v2.http.HttpClient;
import com.microsoft.rest.v2.http.HttpRequest;
import com.microsoft.rest.v2.http.HttpResponse;
import com.microsoft.rest.v2.http.NettyClient;
import com.microsoft.rest.v2.policy.RequestPolicy;
import rx.Single;

//final class NetworkRequestFactory implements IRequestPolicyFactory {
//
//    private static HttpClient.Factory httpClientFactory = new NettyClient.Factory();
//
//    private final HttpClient httpClient;
//
//    NetworkRequestFactory(HttpClient.Configuration configuration) {
//        this.httpClient = httpClientFactory.create(configuration);
//    }
//
//    private final class NetworkRequestPolicy implements RequestPolicy {
//
//        private final HttpClient httpClient;
//
//        public NetworkRequestPolicy(HttpClient httpClient) {
//            this.httpClient = httpClient;
//        }
//
//        @Override
//        public Single<HttpResponse> sendAsync(HttpRequest request) {
//            return httpClient.sendRequestAsync(request);
//        }
//    }
//
//    @Override
//    public RequestPolicy create(RequestPolicyNode requestPolicyNode) {
//        return new NetworkRequestPolicy(this.httpClient);
//    }
//}
