package com.microsoft.azure.storage.blob;

import java.security.InvalidKeyException;
import java.util.Date;

abstract class BaseSAS {
    final String version;

    final SASProtocol protocol;

    final Date startTime;

    final Date expiryTime;

    final String permissions;

    final IPRange ipRange;

    BaseSAS(String version, SASProtocol protocol, Date startTime, Date expiryTime, String permissions, IPRange ipRange) {
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

    String getIPRangeAsString() {
        String ipRangeString = Constants.EMPTY_STRING;
        if (this.ipRange != null) {
            ipRangeString = this.ipRange.toString();
        }

        return ipRangeString;
    }

    public abstract SASQueryParameters GenerateSASQueryParameters(SharedKeyCredentials sharedKeyCredentials) throws InvalidKeyException;
}
