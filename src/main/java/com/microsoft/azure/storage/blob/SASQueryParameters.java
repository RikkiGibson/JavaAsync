package com.microsoft.azure.storage.blob;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public final class SASQueryParameters {

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

    private String encoding;

    public SASQueryParameters(Map<String, String[]> queryParamsMap, boolean removeSASParams) {
        String[] queryValue = queryParamsMap.get("sv");
        if (queryValue != null) {
            if (removeSASParams) {
                queryParamsMap.remove("sv");
            }
            this.version = queryValue[0];
        }
        else {
            this.version = null;
        }

        queryValue = queryParamsMap.get("ss");
        if (queryValue != null) {
            if (removeSASParams) {
                queryParamsMap.remove("ss");
            }
            this.services = queryValue[0];
        }
        else {
            this.services = null;
        }

        queryValue = queryParamsMap.get("srt");
        if (queryValue != null) {
            if (removeSASParams) {
                queryParamsMap.remove("srt");
            }
            this.resourceTypes = queryValue[0];
        }
        else {
            this.resourceTypes = null;
        }

        queryValue = queryParamsMap.get("spr");
        if (queryValue != null) {
            if (removeSASParams) {
                queryParamsMap.remove("spr");
            }
            this.protocol = queryValue[0];
        }
        else {
            this.protocol = null;
        }

        queryValue = queryParamsMap.get("st");
        if (queryValue != null) {
            if (removeSASParams) {
                queryParamsMap.remove("st");
            }
            this.startTime = Utility.parseDate(queryValue[0]);
        }
        else {
            this.startTime = null;
        }

        queryValue = queryParamsMap.get("se");
        if (queryValue != null) {
            if (removeSASParams) {
                queryParamsMap.remove("se");
            }
            this.expiryTime = Utility.parseDate(queryValue[0]);
        }
        else {
            this.expiryTime = null;
        }

        queryValue = queryParamsMap.get("sip");
        if (queryValue != null) {
            if (removeSASParams) {
                queryParamsMap.remove("sip");
            }
            this.ipRange = new IPRange(queryValue[0]);
        }
        else {
            this.ipRange = null;
        }

        queryValue = queryParamsMap.get("si");
        if (queryValue != null) {
            if (removeSASParams) {
                queryParamsMap.remove("si");
            }
            this.identifier = queryValue[0];
        }
        else {
            this.identifier = null;
        }

        queryValue = queryParamsMap.get("sr");
        if (queryValue != null) {
            if (removeSASParams) {
                queryParamsMap.remove("sr");
            }
            this.resource = queryValue[0];
        }
        else {
            this.resource = null;
        }

        queryValue = queryParamsMap.get("sp");
        if (queryValue != null) {
            if (removeSASParams) {
                queryParamsMap.remove("sp");
            }
            this.permissions = queryValue[0];
        }
        else {
            this.permissions = null;
        }

        queryValue = queryParamsMap.get("sig");
        if (queryValue != null) {
            if (removeSASParams) {
                queryParamsMap.remove("sig");
            }
            this.signature = queryValue[0];
        }
        else {
            this.signature = null;
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

    public String encode() {
        return StringUtils.join(
                new String[]{
                        "sv=" + this.version,
                        "ss=" + this.services,
                        "srt" + this.resourceTypes,
                        "spr" + this.protocol,
                        "st=" + Utility.getUTCTimeOrEmpty(this.startTime),
                        "se=" + Utility.getUTCTimeOrEmpty(this.expiryTime),
                        "sip=" + this.ipRange.toString(),
                        "si=" + this.identifier,
                        "sr=" + this.resource,
                        "sp=" + this.permissions,
                        "sig=" + this.signature
                },
                '&'
        );
    }
}
