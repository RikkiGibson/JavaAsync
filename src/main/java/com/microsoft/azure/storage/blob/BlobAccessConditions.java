package com.microsoft.azure.storage.blob;

/**
 * Optional access conditions which are specific to blobs.
 */
public final class BlobAccessConditions {

    // Optional standard HTTP access conditions which are optionally set
    private final HttpAccessConditions httpAccessConditions;

    // Optional access conditions for a lease on a container or blob
    private final LeaseAccessConditions leaseAccessConditions;

    // Optional access conditions which are specific to append blobs
    private final AppendBlobAccessConditions appendBlobAccessConditions;

    // Optional access conditions which are specific to page blobs
    private final PageBlobAccessConditions pageBlobAccessConditions;

    /**
     * Access conditions which are specific to blobs.
     * @param httpAccessConditions
     *      Optional standard HTTP access conditions which are optionally set
     * @param leaseAccessConditions
     *      Optional access conditions for a lease on a container or blob
     * @param appendBlobAccessConditions
     *      Optional access conditions which are specific to append blobs
     * @param pageBlobAccessConditions
     *      Optional access conditions which are specific to page blobs
     */
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
