package com.microsoft.azure.storage.pipeline;

import com.microsoft.rest.v2.LogLevel;
import com.microsoft.rest.v2.RestClient;
import com.microsoft.rest.v2.http.HttpClient;
import com.microsoft.rest.v2.policy.RequestPolicy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;

public final class Pipeline {

    private final PipelineOptions pipelineOptions;
    private final HttpClient.Configuration configuration;
    //private final IRequestPolicyFactory n

    private List<IRequestPolicyFactory> customRequestPolicyFactories = new ArrayList<>();

    public Pipeline(HttpClient.Configuration configuration, PipelineOptions pipelineOptions) {
        this.pipelineOptions = pipelineOptions;
        this.configuration = configuration;
    }

    /**
     * Adds a custom RequestPolicy.Factory to the request pipeline in addition to the standard policies.
     *
     * @param factory The Factory producing a custom user-defined RequestPolicy.
     * @return the builder itself for chaining
     */
    public Pipeline addRequestPolicy(RequestPolicyFactoryInterface factory) {
        customRequestPolicyFactories.add(factory);
        return this;
    }

    public RestClient build() {
        return null;
    }

    public RequestPolicy Invoke() {
        NetworkRequestFactory networkRequestFactory = new NetworkRequestFactory(this.configuration);
        RequestPolicy nextRequestPolicy = networkRequestFactory.create(null);
        for (int i = this.customRequestPolicyFactories.size() - 1; i >= 0; i--) {
            nextRequestPolicy = this.customRequestPolicyFactories.get(i).create(this, nextRequestPolicy);
        }

        return nextRequestPolicy;
    }
}
