package com.microsoft.azure.storage.pipeline;

public final class PipelineOptions {
    public final IRequestPolicyFactory httpSender;

    public final ILogRequest logRequest;

    PipelineOptions(IRequestPolicyFactory httpSender, ILogRequest logRequestInterface) {
        this.httpSender = httpSender;
        this.logRequest = logRequestInterface;
    }
}
