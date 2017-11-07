package com.microsoft.azure.storage.blob;

public final class BlobAccessConditions {

    private final HttpAccessConditions httpAccessConditions;

    private final LeaseAccessConditions leaseAccessConditions;

    private final AppendBlobAccessConditions appendBlobAccessConditions;

    private final PageBlobAccessConditions pageBlobAccessConditions;

    public BlobAccessConditions(
            HttpAccessConditions httpAccessConditions,
            LeaseAccessConditions leaseAccessConditions,
            AppendBlobAccessConditions appendBlobAccessConditions,
            PageBlobAccessConditions pageBlobAccessConditions) {
        this.httpAccessConditions = httpAccessConditions;
        this.leaseAccessConditions = leaseAccessConditions;
        this.appendBlobAccessConditions = appendBlobAccessConditions;
        this.pageBlobAccessConditions = pageBlobAccessConditions;
    }

    public HttpAccessConditions getHttpAccessConditions() {
        return httpAccessConditions;
    }

    public LeaseAccessConditions getLeaseAccessConditions() {
        return leaseAccessConditions;
    }

    public AppendBlobAccessConditions getAppendBlobAccessConditions() {
        return appendBlobAccessConditions;
    }

    public PageBlobAccessConditions getPageBlobAccessConditions() {
        return pageBlobAccessConditions;
    }
}
