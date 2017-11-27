/**
 * Copyright Microsoft Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.microsoft.azure.storage.blob;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.Map;

/**
 * Represents the components that make up an Azure Storage SAS' query parameters.
 * <p>NOTE: Changing any field requires computing a new SAS signature using a XxxSASSignatureValues type.</p>
 */
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

    /**
     * Creates a new {@link SASQueryParameters} object
     * @param queryParamsMap
     *      A {@code java.util.Map} representing all query parameters for the request as key-value pairs
     * @param removeSASParams
     *      When {@code true}, the SAS query parameters will be removed from queryParamsMap
     */
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

    /**
     * Creates a new {@link SASQueryParameters} object
     * @param version
     *      A {@code String} representing the storage version
     * @param services
     *      A {@code String} representing the storage services being accessed (only for Account SAS)
     * @param resourceTypes
     *      A {@code String} representing the storage resource types being accessed (only for Account SAS)
     * @param protocol
     *      A {@code String} representing the allowed HTTP protocol(s) or {@code null}
     * @param startTime
     *      A {@code java.util.Date} representing the start time for this SAS token or {@code null}
     * @param expiryTime
     *      A {@code java.util.Date} representing the expiry time for this SAS token
     * @param ipRange
     *      A {@link IPRange} representing the range of valid IP addresses for this SAS token or {@code null}
     * @param identifier
     *      A {@code String} representing the signed identifier (only for Service SAS) or {@code null}
     * @param resource
     *      A {@code String} representing the storage container or blob (only for Service SAS)
     * @param permissions
     *      A {@code String} representing the storage permissions or {@code null}
     * @param signature
     *      A {@code String} representing the signature for the SAS token
     */
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

    /**
     * @return
     *      A {@code String} representing the storage version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @return
     *      A {@code String} representing the storage services being accessed (only for Account SAS)
     */
    public String getServices() {
        return services;
    }

    /**
     * @return
     *      A {@code String} representing the storage resource types being accessed (only for Account SAS)
     */
    public String getResourceTypes() {
        return resourceTypes;
    }

    /**
     * @return
     *      A {@code String} representing the allowed HTTP protocol(s) or {@code null}
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * @return
     *      A {@code java.util.Date} representing the start time for this SAS token or {@code null}
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * @return
     *      A {@code java.util.Date} representing the expiry time for this SAS token
     */
    public Date getExpiryTime() {
        return expiryTime;
    }

    /**
     * @return
     *      A {@link IPRange} representing the range of valid IP addresses for this SAS token or {@code null}
     */
    public IPRange getIpRange() {
        return ipRange;
    }

    /**
     * @return
     *      A {@code String} representing the signed identifier (only for Service SAS) or {@code null}
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * @return
     *      A {@code String} representing the storage container or blob (only for Service SAS)
     */
    public String getResource() {
        return resource;
    }

    /**
     * @return
     *      A {@code String} representing the storage permissions or {@code null}
     */
    public String getPermissions() {
        return permissions;
    }

    /**
     * @return
     *      A {@code String} representing the signature for the SAS token
     */
    public String getSignature() {
        return signature;
    }

    /**
     * Encodes all 
     * @return
     */
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
