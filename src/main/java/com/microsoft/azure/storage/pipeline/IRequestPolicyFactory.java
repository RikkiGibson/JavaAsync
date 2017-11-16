package com.microsoft.azure.storage.pipeline;

import com.microsoft.rest.v2.policy.RequestPolicy;

public interface IRequestPolicyFactory {

    RequestPolicy create(RequestPolicyNode requestPolicyNode);
}
