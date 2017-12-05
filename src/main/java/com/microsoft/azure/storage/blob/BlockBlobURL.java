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

import com.microsoft.azure.storage.models.BlobType;
import com.microsoft.azure.storage.models.BlockList;
import com.microsoft.azure.storage.models.BlockListType;
import com.microsoft.azure.storage.pipeline.Pipeline;
import com.microsoft.rest.v2.http.HttpPipeline;
import rx.Single;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Date;

/**
 * Represents a URL to a block blob.
 */
public final class BlockBlobURL extends BlobURL {

    /**
     * Creates a new {@link BlockBlobURL} object.
     * @param url
     *      A {@code String} representing a URL to a block blob.
     * @param pipeline
     *      A {@link Pipeline} object representing the pipeline for requests.
     */
    public BlockBlobURL(String url, HttpPipeline pipeline) {
        super(url, pipeline);
    }

    /**
     * Creates a new {@link BlockBlobURL} with the given pipeline.
     * @param pipeline
     *      A {@link HttpPipeline} object to set.
     * @return
     *      A {@link BlockBlobURL} object with the given pipeline.
     */
    public BlockBlobURL withPipeline(HttpPipeline pipeline) {
        return new BlockBlobURL(this.url, pipeline);
    }

    /**
     * Creates a new {@link BlockBlobURL} with the given snapshot.
     * @param snapshot
     *      A <code>java.util.Date</code> to set.
     * @return
     *      A {@link BlockBlobURL} object with the given pipeline.
     */
    public BlockBlobURL withSnapshot(Date snapshot) throws MalformedURLException, UnsupportedEncodingException {
        BlobURLParts blobURLParts = URLParser.ParseURL(super.url);
        blobURLParts.setSnapshot(snapshot);
        return new BlockBlobURL(blobURLParts.toURL(), super.storageClient.httpPipeline());
    }

    /**
     * PutBlob creates a new block blob, or updates the content of an existing block blob.
     * Updating an existing block blob overwrites any existing metadata on the blob. Partial updates are not
     * supported with PutBlob; the content of the existing blob is overwritten with the new content. To
     * perform a partial update of a block blob's, use PutBlock and PutBlockList.
     * For more information, see https://docs.microsoft.com/rest/api/storageservices/put-blob.
     * @param data
     * @return
     */
    public Single<Void> putBlobAsync(byte[] data) {
        //return this.storageClient.blobs().putAsync(BlobType.BLOCK_BLOB, data, null, null, null, null, null, null, null, null, null, null, null, null , null);//super.url, BlobType.BLOCK_BLOB, data);
        return null;
    }

    /**
     * GetBlockList returns the list of blocks that have been uploaded as part of a block blob using the specified block list filter.
     * For more information, see https://docs.microsoft.com/rest/api/storageservices/get-block-list.
     * @param listType
     * @param leaseAccessConditions
     * @return
     */
    public Single<BlockList> GetBlockListAsync(BlockListType listType, LeaseAccessConditions leaseAccessConditions) {
        //return this.storageClient.blockBlobs().getBlockListAsync(listType);
        return null;
    }
}
