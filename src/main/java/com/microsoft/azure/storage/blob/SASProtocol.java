package com.microsoft.azure.storage.blob;

/**
 * Specifies the set of possible permissions for a shared access signature protocol.
 */
public enum SASProtocol {
    /**
     * Permission to use SAS only through https granted.
     */
    HTTPS_ONLY(Constants.HTTPS),

    /**
     * Permission to use SAS only through https or http granted.
     */
    HTTPS_HTTP(Constants.HTTPS_HTTP);

    private final String protocols;

    private SASProtocol(String p) {
        this.protocols = p;
    }

    @Override
    public String toString() {
        return this.protocols;
    }
}
