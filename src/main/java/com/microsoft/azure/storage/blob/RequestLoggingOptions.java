package com.microsoft.azure.storage.blob;

import java.util.logging.Level;

public final class RequestLoggingOptions {

    private final Long minDurationToLogSlowRequestsInMs;

    private final Level loggingLevel;

    public RequestLoggingOptions() {
        this(Level.SEVERE, 3000L);
    }

    public RequestLoggingOptions(Level loggingLevel) {
        this(loggingLevel, 3000L);
    }

    public RequestLoggingOptions(Level loggingLevel, Long minDurationToLogSlowRequestsInMs) {
        this.loggingLevel = loggingLevel;
        this.minDurationToLogSlowRequestsInMs = minDurationToLogSlowRequestsInMs;
    }

    public Level getLoggingLevel() {
        return loggingLevel;
    }

    public Long getMinDurationToLogSlowRequestsInMs() {
        return minDurationToLogSlowRequestsInMs;
    }


}
