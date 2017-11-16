package com.microsoft.azure.storage.pipeline;

import com.microsoft.rest.v2.http.HttpRequest;
import com.microsoft.rest.v2.http.HttpResponse;
import com.microsoft.rest.v2.policy.RequestPolicy;
//import org.apache.log4j.Level;
//import org.apache.log4j.Logger;
import rx.Single;

import java.util.logging.Level;

public final class RequestPolicyNode {
        // A Node represents a node in a linked-list of Policy objects. A Node is passed
        // to the Factory's New method which passes to the Policy object it creates. The Policy object
        // uses the Node to forward the Context and HTTP request to the next Policy object in the pipeline.
        private final RequestPolicy nextRequestPolicy;
        private final ILogRequest logRequest;

        RequestPolicyNode(RequestPolicy nextRequestPolicy, ILogRequest logRequest) {
            this.nextRequestPolicy = nextRequestPolicy;
            this.logRequest = logRequest;
        }
    /**
     * Sends an HTTP request as an asynchronous operation.
     *
     * @param request The HTTP request message to send.
     * @return The rx.Single instance representing the asynchronous operation.
     */
    public Single<HttpResponse> sendAsync(HttpRequest request) {
        return nextRequestPolicy.sendAsync(request);
    }

    public boolean shouldLogRequest(LogLevel logLevel) {
        if (logLevel == LogLevel.OFF) {
            return false;
        }

        LogLevel minLevelToLog = this.logRequest.minimumLevelToLog();
        return logLevel.getValue() <= minLevelToLog.getValue();
    }

    public void log(LogLevel logLevel, String format, Object... args) {
        if (shouldLogRequest(logLevel)) {
            this.logRequest.logRequest(logLevel, formatLogEntry(format, args));
        }
    }

    private static String formatLogEntry(String format, Object... args) {
        return String.format(format, args);//.replace('\n', '.');
    }
}
