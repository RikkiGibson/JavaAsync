package com.microsoft.azure.storage.blob;

import java.security.InvalidKeyException;
import java.util.Date;

public final class AccountSAS extends BaseSAS {

    private final String services;

    private final String resourceTypes;

    public AccountSAS(String version, String protocol, Date startTime, Date expiryTime, String permissions,
                      IPRange ipRange, String services, String resourceTypes) {
        super(version, protocol, startTime, expiryTime, permissions, ipRange);
        this.services = services;
        this.resourceTypes = resourceTypes;
    }

    @Override
    public SASQueryParameters GenerateSASQueryParameters(SharedKeyCredentials sharedKeyCredentials) throws InvalidKeyException {
        if (sharedKeyCredentials == null) {
            throw new IllegalArgumentException("SharedKeyCredentials cannot be null.");
        }

    }
}
