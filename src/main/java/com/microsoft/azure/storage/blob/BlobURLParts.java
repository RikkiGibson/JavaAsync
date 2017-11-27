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

// A BlobURLParts object represents the components that make up an Azure Storage Container/Blob URL. You parse an
// existing URL into its parts by calling NewBlobURLParts(). You construct a URL from parts by calling URL().
// NOTE: Changing any SAS-related field requires computing a new SAS signature.
// TODO consider changing to data types
public final class BlobURLParts {
    private final String scheme;

    private final String host;

    private final String containerName;

    private final String blobName;

    private final Date snapshot;

    private final SASQueryParameters sasQueryParameters;

    private final Map<String, String[]> unparsedParameters;

    public BlobURLParts(String scheme, String host, String containerName, String blobName, Date snapshot, SASQueryParameters sasQueryParameters, Map<String, String[]> unparsedParameters) {
        this.scheme = scheme;
        this.host = host;
        this.containerName = containerName;
        this.blobName = blobName;
        this.snapshot = snapshot;
        this.sasQueryParameters = sasQueryParameters;
        this.unparsedParameters = unparsedParameters;
    }

    public String getScheme() {
        return scheme;
    }

    public String getHost() {
        return host;
    }

    public String getContainerName() {
        return containerName;
    }

    public String getBlobName() {
        return blobName;
    }

    public Date getSnapshot() {
        return snapshot;
    }

    public SASQueryParameters getSasQueryParameters() {
        return sasQueryParameters;
    }

    public Map<String, String[]> getUnparsedParameters() {
        return unparsedParameters;
    }

    public String toURL() {
        StringBuilder urlBuilder = new StringBuilder();

        if (this.containerName != null) {
            urlBuilder.append('/' + this.containerName);
            if (this.blobName != null) {
                urlBuilder.append('/' + this.blobName);
            }
        }

        boolean isFirst = true;

        for(Map.Entry<String, String[]> entry : this.unparsedParameters.entrySet()) {
            if (isFirst) {
                isFirst = false;
            }
            else {
                urlBuilder.append('&');
            }

            urlBuilder.append(entry.getKey() + '=' + StringUtils.join(entry.getValue(), ','));
        }

        if (this.snapshot != null) {
            if (isFirst) {
                isFirst = false;
            }
            else {
                urlBuilder.append('&');
            }

            urlBuilder.append("snapshot=" + Utility.getGMTTime(this.snapshot));
        }

        String sasEncoding = this.sasQueryParameters.encode();
        if (!Utility.isNullOrEmpty(sasEncoding)) {
            if (isFirst) {
                isFirst = false;
            }
            else {
                urlBuilder.append('&');
            }

            urlBuilder.append(sasEncoding);
        }

        return urlBuilder.toString();
    }
}
