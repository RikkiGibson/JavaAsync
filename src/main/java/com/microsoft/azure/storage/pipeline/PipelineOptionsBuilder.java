package com.microsoft.azure.storage.pipeline;

import com.microsoft.rest.v2.http.HttpClient;

public final class PipelineOptionsBuilder {

    private IRequestPolicyFactory httpSender;

    private ILogRequest logRequest;

    public PipelineOptionsBuilder(){
    }

    public PipelineOptionsBuilder withNewHttpSender(IRequestPolicyFactory httpSender) {
        this.httpSender = httpSender;
        return this;
    }

    public PipelineOptionsBuilder withNewLogRequestInterface(ILogRequest logRequest) {
        this.logRequest = logRequest;
        return this;
    }

    public PipelineOptions build() {
        return new PipelineOptions(this.httpSender, this.logRequest);
    }
}
