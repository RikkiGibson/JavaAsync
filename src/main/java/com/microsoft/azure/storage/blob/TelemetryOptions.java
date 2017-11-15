package com.microsoft.azure.storage.blob;

public final class TelemetryOptions {
    private final String userAgentPrefix;


    public TelemetryOptions() { this(Constants.EMPTY_STRING); }

    public TelemetryOptions(String userAgentPrefix) {
        this.userAgentPrefix = userAgentPrefix;
    }

    @Override
    public boolean equals(Object obj) {
        if (this.userAgentPrefix == null) {
            return obj == null;
        }

        return this.userAgentPrefix.equals(obj);
    }

    @Override
    public String toString() {
        return this.userAgentPrefix;
    }

    public String UserAgentPrefix() {
        return this.userAgentPrefix;
    }
}
