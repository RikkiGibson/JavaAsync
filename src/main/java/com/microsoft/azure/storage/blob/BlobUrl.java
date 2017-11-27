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

import com.microsoft.azure.storage.pipeline.Pipeline;
import rx.Single;

import java.io.InputStream;
import java.util.Date;

/**
 * Represents a URL to an Azure Storage blob; the blob may be a block blob, append blob, or page blob.
 */
public class BlobUrl extends StorageUrl {

    public BlobUrl(String url, Pipeline pipeline) {
        super(url, pipeline);
    }

    /**
     * Creates a new {@link BlobUrl} with the given pipeline.
     * @param pipeline
     *      A {@link Pipeline} object to set.
     * @return
     *      A {@link BlobUrl} object with the given pipeline.
     */
    public BlobUrl withPipeline(Pipeline pipeline) {
        return new BlobUrl(super.url, pipeline);
    }

    /**
     * Creates a new {@link BlobUrl} with the given snapshot.
     * @param snapshot
     *      A <code>java.util.Date</code> to set.
     * @return
     *      A {@link BlobUrl} object with the given pipeline.
     */
    public BlobUrl withSnapshot(Date snapshot) {
        //return new BlobUrl(, this.pipeline)
        return null;
    }

    /**
     * Converts this BlobUrl to a {@link BlockBlobUrl} object.
     * @return
     *      A {@link BlockBlobUrl} object.
     */
    public BlockBlobUrl toBlockBlobUrl() {
        return new BlockBlobUrl(super.url, super.storageClient.pipeline());
    }

//    public AppendBlobUrl toAppendBlobUrl() {
//        return new AppendBlobUrl(super.url, super.storageClient.pipeline());
//    }
//
//    public PageBlobUrl toPageBlobUrl() {
//        return new PageBlobUrl(super.url, super.storageClient.pipeline());
//    }


    // StartCopy copies the data at the source URL to a blob.
// For more information, see https://docs.microsoft.com/rest/api/storageservices/copy-blob.

    /**
     * StartCopy copies the data at the source URL to a blob.
     * For more information, see https://docs.microsoft.com/rest/api/storageservices/copy-blob.
     * @param url
     * @param metadata
     * @param sourceAccessConditions
     * @param destAccessConditions
     * @return
     */
    public Single<Void> startCopyAsync(String url, Metadata metadata, BlobAccessConditions sourceAccessConditions, BlobAccessConditions destAccessConditions) {
        //return this.storageClient.blobs().copyAsync()
        return null;
    }

    /**
     * AbortCopy stops a pending copy that was previously started and leaves a destination blob with 0 length and metadata.
     * For more information, see https://docs.microsoft.com/rest/api/storageservices/abort-copy-blob.
     * @param copyID
     * @param leaseAccessConditions
     * @return
     */
    public Single<Void> abortCopyAsync(String copyID, LeaseAccessConditions leaseAccessConditions) {
        return null;
    }

    // if range == 0 download to end
    /**
     * GetBlob reads a range of bytes from a blob. The response also includes the blob's properties and metadata.
     * For more information, see https://docs.microsoft.com/rest/api/storageservices/get-blob.
     * @param offset
     * @param range
     * @param blobAccessConditions
     * @param requestContentMD5
     * @return
     */
    public Single<InputStream> getBlobAsync(long offset, long range, BlobAccessConditions blobAccessConditions, boolean requestContentMD5) {
        return this.storageClient.blobs().getAsync(super.url);
    }

    // Delete marks the specified blob or snapshot for deletion. The blob is later deleted during garbage collection.
    // Note that deleting a blob also deletes all its snapshots.
    // For more information, see https://docs.microsoft.com/rest/api/storageservices/delete-blob.

    /**
     * Deletes the specified blob or snapshot.
     * Note that deleting a blob also deletes all its snapshots.
     * For more information, see https://docs.microsoft.com/rest/api/storageservices/delete-blob.
     * @param deleteBlobSnapshotOptions
     * @param blobAccessConditions
     * @return
     */
    public Single<Void> deleteAsync(DeleteBlobSnapshotOptions deleteBlobSnapshotOptions, BlobAccessConditions blobAccessConditions) {
        return null;
    }

    /**
     * GetPropertiesAndMetadata returns the blob's metadata and properties.
     * For more information, see https://docs.microsoft.com/rest/api/storageservices/get-blob-properties.
     * @param blobAccessConditions
     * @return
     */
    public Single<Void> getPropertiesAndMetadataAsync(BlobAccessConditions blobAccessConditions) {
        return null;
    }

    /**
     * SetProperties changes a blob's HTTP header properties.
     * For more information, see https://docs.microsoft.com/rest/api/storageservices/set-blob-properties.
     * @param blobHttpHeaders
     * @param blobAccessConditions
     * @return
     */
    public Single<Void> setPropertiesAsync(BlobHttpHeaders blobHttpHeaders, BlobAccessConditions blobAccessConditions) {
        return null;
    }

    /**
     * SetMetadata changes a blob's metadata.
     * https://docs.microsoft.com/rest/api/storageservices/set-blob-metadata.
     * @param metadata
     * @param blobAccessConditions
     * @return
     */
    public Single<Void> setMetadaAsync(Metadata metadata, BlobAccessConditions blobAccessConditions) {
        return null;
    }

    /**
     * CreateSnapshot creates a read-only snapshot of a blob.
     * For more information, see https://docs.microsoft.com/rest/api/storageservices/snapshot-blob.
     * @param metadata
     * @param blobAccessConditions
     * @return
     */
    public Single<Void> createSnapshotAsync(Metadata metadata, BlobAccessConditions blobAccessConditions) {
        // CreateSnapshot does NOT panic if the user tries to create a snapshot using a URL that already has a snapshot query parameter
        // because checking this would be a performance hit for a VERY unusual path and I don't think the common case should suffer this
        // performance hit.
        return null;
    }

    /**
     * AcquireLease acquires a lease on the blob for write and delete operations. The lease duration must be between
     * 15 to 60 seconds, or infinite (-1).
     * For more information, see https://docs.microsoft.com/rest/api/storageservices/lease-blob.
     * @param proposedID
     * @param duration
     * @param httpAccessConditions
     * @return
     */
    public Single<Void> acquireLeaseAsync(String proposedID, long duration, HttpAccessConditions httpAccessConditions) {
        return null;
    }

    /**
     * RenewLease renews the blob's previously-acquired lease.
     * For more information, see https://docs.microsoft.com/rest/api/storageservices/lease-blob.
     * @param leaseID
     * @param httpAccessConditions
     * @return
     */
    public Single<Void> renewLeaseAsync(String leaseID, HttpAccessConditions httpAccessConditions) {
        return null;
    }

    /**
     * BreakLease breaks the blob's previously-acquired lease (if it exists). Pass the LeaseBreakDefault (-1) constant to break
     * a fixed-duration lease when it expires or an infinite lease immediately.
     * For more information, see https://docs.microsoft.com/rest/api/storageservices/lease-blob.
     * @param leaseID
     * @param breakPeriodInSeconds
     * @param httpAccessConditions
     * @return
     */
    public Single<Void> breakLeaseAsync(String leaseID, Integer breakPeriodInSeconds, HttpAccessConditions httpAccessConditions) {
        return null;
    }

    /**
     * ChangeLease changes the blob's lease ID.
     * For more information, see https://docs.microsoft.com/rest/api/storageservices/lease-blob.
     * @param proposedID
     * @param httpAccessConditions
     * @return
     */
    public Single<Void> changeLeaseAsync(String proposedID, HttpAccessConditions httpAccessConditions) {
        return null;
    }
}
