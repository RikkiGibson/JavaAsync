package com.microsoft.azure.storage.blob;

import com.microsoft.azure.storage.pipeline.Pipeline;
import com.microsoft.azure.storage.pipeline.RequestPolicyFactoryInterface;
import com.microsoft.rest.v2.policy.RequestPolicy;

public final class RequestRetryFactory implements RequestPolicyFactoryInterface{
    @Override
    public RequestPolicy create(Pipeline pipeline, RequestPolicy nextPolicy) {
        return null;
    }

    @Override
    public RequestPolicy create(RequestPolicy nextPolicy) {
        return null;
    }
}
