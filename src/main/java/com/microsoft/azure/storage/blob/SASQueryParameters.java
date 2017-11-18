package com.microsoft.azure.storage.blob;

import java.util.Date;
import java.util.Map;

public class SASQueryParameters {

    private final String version;

    private final String services;

    private final String resourceTypes;

    private final String protocol;

    private final Date startTime;

    private final Date expiryTime;

    private final IPRange ipRange;

    private final String identifier;

    private final String resource;

    private final String permissions;

    private final String signature;

    public SASQueryParameters(Map<String, String[]> queryParamsMap, boolean removeSASParams) {
        String[] queryValue = queryParamsMap.get("sv");
        if (queryValue != null) {
            if (removeSASParams) {
                queryParamsMap.remove("sv");
            }
            this.version = queryValue[0];
        }

        queryValue = queryParamsMap.get("ss");
        if (queryValue != null) {
            if (removeSASParams) {
                queryParamsMap.remove("ss");
            }
            this.services = queryValue[0];
        }

        queryValue = queryParamsMap.get("srt");
        if (queryValue != null) {
            if (removeSASParams) {
                queryParamsMap.remove("srt");
            }
            this.resourceTypes = queryValue[0];
        }

        queryValue = queryParamsMap.get("spr");
        if (queryValue != null) {
            if (removeSASParams) {
                queryParamsMap.remove("spr");
            }
            this.protocol = queryValue[0];
        }

        queryValue = queryParamsMap.get("st");
        if (queryValue != null) {
            if (removeSASParams) {
                queryParamsMap.remove("st");
            }
            this.startTime = Utility.parseDate(queryValue[0]);
        }

        queryValue = queryParamsMap.get("se");
        if (queryValue != null) {
            if (removeSASParams) {
                queryParamsMap.remove("se");
            }
            this.expiryTime = Utility.parseDate(queryValue[0]);
        }

        queryValue = queryParamsMap.get("sip");
        if (queryValue != null) {
            if (removeSASParams) {
                queryParamsMap.remove("sip");
            }
            this.ipRange = new IPRange(queryValue[0]);
        }

        queryValue = queryParamsMap.get("si");
        if (queryValue != null) {
            if (removeSASParams) {
                queryParamsMap.remove("si");
            }
            this.identifier = queryValue[0];
        }

        queryValue = queryParamsMap.get("sr");
        if (queryValue != null) {
            if (removeSASParams) {
                queryParamsMap.remove("sr");
            }
            this.resource = queryValue[0];
        }

        queryValue = queryParamsMap.get("sp");
        if (queryValue != null) {
            if (removeSASParams) {
                queryParamsMap.remove("sp");
            }
            this.permissions = queryValue[0];
        }

        queryValue = queryParamsMap.get("sig");
        if (queryValue != null) {
            if (removeSASParams) {
                queryParamsMap.remove("sig");
            }
            this.signature = queryValue[0];
        }
    }

    public SASQueryParameters(
            String version,
            String services,
            String resourceTypes,
            String protocol,
            Date startTime,
            Date expiryTime,
            IPRange ipRange,
            String identifier,
            String resource,
            String permissions,
            String signature) {
        this.version = version;
        this.services = services;
        this.resourceTypes = resourceTypes;
        this.protocol = protocol;
        this.startTime = startTime;
        this.expiryTime = expiryTime;
        this.ipRange = ipRange;
        this.identifier = identifier;
        this.resource = resource;
        this.permissions = permissions;
        this.signature = signature;
    }

    public String getVersion() {
        return version;
    }

    public String getServices() {
        return services;
    }

    public String getResourceTypes() {
        return resourceTypes;
    }

    public String getProtocol() {
        return protocol;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getExpiryTime() {
        return expiryTime;
    }

    public IPRange getIpRange() {
        return ipRange;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getResource() {
        return resource;
    }

    public String getPermissions() {
        return permissions;
    }

    public String getSignature() {
        return signature;
    }
}
