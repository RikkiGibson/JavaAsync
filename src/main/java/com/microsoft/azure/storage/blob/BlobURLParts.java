package com.microsoft.azure.storage.blob;

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
}
