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

import com.microsoft.rest.v2.http.HttpPipeline;
import rx.Single;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * Represents a URL to an Azure Storage blob; the blob may be a block blob, append blob, or page blob.
 */
public class BlobURL extends StorageUrl {

    /**
     * Creates a new {@link BlobURL} object
     * @param url
     *      A {@code String} representing a URL
     * @param pipeline
     *      A {@link Pipeline} representing a pipeline for requests
     */
    public BlobURL(String url, HttpPipeline pipeline) {
        super(url, pipeline);
    }

    /**
     * Creates a new {@link BlobURL} with the given pipeline.
     * @param pipeline
     *      A {@link HttpPipeline} object to set.
     * @return
     *      A {@link BlobURL} object with the given pipeline.
     */
    public BlobURL withPipeline(HttpPipeline pipeline) {
        return new BlobURL(super.url, pipeline);
    }

    /**
     * Creates a new {@link BlobURL} with the given snapshot.
     * @param snapshot
     *      A <code>java.util.Date</code> to set.
     * @return
     *      A {@link BlobURL} object with the given pipeline.
     */
    public BlobURL withSnapshot(Date snapshot) throws MalformedURLException, UnsupportedEncodingException {
        BlobURLParts blobURLParts = URLParser.ParseURL(super.url);
        blobURLParts.setSnapshot(snapshot);
        return new BlobURL(blobURLParts.toURL(), super.storageClient.httpPipeline());
    }

    /**
     * Converts this BlobURL to a {@link BlockBlobURL} object.
     * @return
     *      A {@link BlockBlobURL} object.
     */
    public BlockBlobURL toBlockBlobURL() {
        return new BlockBlobURL(super.url, super.storageClient.httpPipeline());
    }

    /**
     * Converts this BlobURL to a {@link AppendBlobURL} object.
     * @return
     *      A {@link AppendBlobURL} object.
     */
    public AppendBlobURL toAppendBlobURL() {
        return new AppendBlobURL(super.url, super.storageClient.httpPipeline());
    }

    /**
     * Converts this BlobURL to a {@link PageBlobURL} object.
     * @return
     *      A {@link PageBlobURL} object.
     */
    public PageBlobURL toPageBlobURL() {
        return new PageBlobURL(super.url, super.storageClient.httpPipeline());
    }

    /**
     * StartCopy copies the data at the source URL to a blob.
     * For more information, see https://docs.microsoft.com/rest/api/storageservices/copy-blob.
     * @param sourceURL
     *      A {@code String} representing the source URL to copy from.
     *      URLs outside of Azure may only be copied to block blobs.
     * @param metadata
     *      {@link Metadata} representing the metadata to set on the blob
     * @param sourceAccessConditions
     *      {@link BlobAccessConditions} object to check against the source
     * @param destAccessConditions
     *      {@link BlobAccessConditions} object to check against the destination
     * @return
     *      A {@link Single<Void>} object if successful.
     */
    public Single<Void> startCopyAsync(String sourceURL, Metadata metadata, BlobAccessConditions sourceAccessConditions, BlobAccessConditions destAccessConditions) {
        //return this.storageClient.blobs().copyAsync(sourceURL);
        return null;
    }

    /**
     * AbortCopy stops a pending copy that was previously started
     * and leaves a destination blob with 0 length and metadata.
     * For more information, see https://docs.microsoft.com/rest/api/storageservices/abort-copy-blob.
     * @param copyID
     *      A {@code String} representing the copy identifierprovided in the x-ms-copy-id header of
     *      the original Copy Blob operation.
     * @param leaseAccessConditions
     *      {@link LeaseAccessConditions} object representing lease access conditions
     * @return
     *      A {@link Single<Void>} object if successful.
     */
    public Single<Void> abortCopyAsync(String copyID, LeaseAccessConditions leaseAccessConditions) {
        return null;
    }

    /**
     * GetBlob reads a range of bytes from a blob. The response also includes the blob's properties and metadata.
     * For more information, see https://docs.microsoft.com/rest/api/storageservices/get-blob.
     * @param offset
     *      A {@code Long} which represents the offset to use as the starting point for the source.
     * @param range
     *      A {@code Long} which represents the number of bytes to read or <code>null</code>.
     * @param blobAccessConditions
     *      A {@link BlobAccessConditions} object that represents the access conditions for the blob.
     * @return
     *       {@link Single<InputStream>} object represetning the stream the blob is dowloaded to.
     */
    public Single<InputStream> getBlobAsync(Long offset, Long range, BlobAccessConditions blobAccessConditions) {
        //return this.storageClient.blobs().getAsync();//super.url);
        return null;
    }

    /**
     * Deletes the specified blob or snapshot.
     * Note that deleting a blob also deletes all its snapshots.
     * For more information, see https://docs.microsoft.com/rest/api/storageservices/delete-blob.
     * @param deleteBlobSnapshotOptions
     *      A {@link DeleteBlobSnapshotOptions} which represents delete snapshot options.
     * @param blobAccessConditions
     *      A {@link BlobAccessConditions} object that represents the access conditions for the blob.
     * @return
     *      A {@link Single<Void>} object if successful.
     */
    public Single<Void> deleteAsync(DeleteBlobSnapshotOptions deleteBlobSnapshotOptions, BlobAccessConditions blobAccessConditions) {
        return null;
    }

    /**
     * GetPropertiesAndMetadata returns the blob's metadata and properties.
     * For more information, see https://docs.microsoft.com/rest/api/storageservices/get-blob-properties.
     * @param blobAccessConditions
     *      A {@link BlobAccessConditions} object that represents the access conditions for the blob.
     * @return
     *      A {@link Single<Void>} object if successful.
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
     *      A {@link Single<Void>} object if successful.
     */
    public Single<Void> setPropertiesAsync(BlobHttpHeaders blobHttpHeaders, BlobAccessConditions blobAccessConditions) {
        return null;
    }

    /**
     * SetMetadata changes a blob's metadata.
     * https://docs.microsoft.com/rest/api/storageservices/set-blob-metadata.
     * @param metadata
     * @param blobAccessConditions
     *      A {@link BlobAccessConditions} object that represents the access conditions for the blob.
     * @return
     *      A {@link Single<Void>} object if successful.
     */
    public Single<Void> setMetadaAsync(Metadata metadata, BlobAccessConditions blobAccessConditions) {
        return null;
    }

    /**
     * CreateSnapshot creates a read-only snapshot of a blob.
     * For more information, see https://docs.microsoft.com/rest/api/storageservices/snapshot-blob.
     * @param metadata
     * @param blobAccessConditions
     *      A {@link BlobAccessConditions} object that represents the access conditions for the blob.
     * @return
     *      A {@link Single<Void>} object if successful.
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
     *      A {@link HttpAccessConditions} object that represents HTTP access conditions.
     * @return
     *      A {@link Single<Void>} object if successful.
     */
    public Single<Void> acquireLeaseAsync(String proposedID, long duration, HttpAccessConditions httpAccessConditions) {
        return null;
    }

    /**
     * RenewLease renews the blob's previously-acquired lease.
     * For more information, see https://docs.microsoft.com/rest/api/storageservices/lease-blob.
     * @param leaseID
     * @param httpAccessConditions
     *      A {@link HttpAccessConditions} object that represents HTTP access conditions.
     * @return
     */
    public Single<Void> renewLeaseAsync(String leaseID, HttpAccessConditions httpAccessConditions) {
        return null;
    }

    /**
     * BreakLease breaks the blob's previously-acquired lease (if it exists). Pass the LeaseBreakDefault (-1) constant
     * to break a fixed-duration lease when it expires or an infinite lease immediately.
     * For more information, see https://docs.microsoft.com/rest/api/storageservices/lease-blob.
     * @param leaseID
     *      A {@code String} representing the lease ID to break
     * @param breakPeriodInSeconds
     *      An optional {@code Integer} representing the proposed duration of seconds that the lease should continue
     *      before it is broken, between 0 and 60 seconds. This break period is only used if it is shorter than the time
     *      remaining on the lease. If longer, the time remaining on the lease is used. A new lease will not be
     *      available before the break period has expired, but the lease may be held for longer than the break period
     * @param httpAccessConditions
     *      A {@link HttpAccessConditions} object that represents HTTP access conditions.
     * @return
     *      A {@link Single<Void>} object if successful.
     */
    public Single<Void> breakLeaseAsync(String leaseID, Integer breakPeriodInSeconds, HttpAccessConditions httpAccessConditions) {
        return null;
    }

    /**
     * ChangeLease changes the blob's lease ID.
     * For more information, see https://docs.microsoft.com/rest/api/storageservices/lease-blob.
     * @param proposedID
     *      A {@code String} representing the proposed lease ID, in a GUID String format.
     * @param httpAccessConditions
     *      A {@link HttpAccessConditions} object that represents HTTP access conditions.
     * @return
     *      A {@link Single<Void>} object if successful.
     */
    public Single<Void> changeLeaseAsync(String proposedID, HttpAccessConditions httpAccessConditions) {
        return null;
    }
}
