package com.microsoft.azure.storage.blob;

import java.security.InvalidKeyException;
import java.util.Date;

abstract class BaseSAS {
    final String version;

    final String protocol;

    final Date startTime;

    final Date expiryTime;

    final String permissions;

    final IPRange ipRange;

    BaseSAS(String version, String protocol, Date startTime, Date expiryTime, String permissions, IPRange ipRange) {
        if (Utility.isNullOrEmpty(version)) {
            this.version = Constants.HeaderConstants.TARGET_STORAGE_VERSION;
        }
        else {
            this.version = version;
        }

        this.protocol = protocol;
        this.startTime = startTime;
        this.expiryTime = expiryTime;
        this.permissions = permissions;
        this.ipRange = ipRange;
    }

    public abstract SASQueryParameters GenerateSASQueryParameters(SharedKeyCredentials sharedKeyCredentials) throws InvalidKeyException;
}
