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
import com.microsoft.azure.storage.pipeline.Pipeline;
import rx.Single;

/**
 * Represents a URL to a block blob.
 */
public final class BlockBlobUrl extends BlobUrl {

    /**
     * Creates a new {@link BlockBlobUrl} object
     * @param url
     * @param pipeline
     */
    public BlockBlobUrl(String url, Pipeline pipeline) {
        super(url, pipeline);
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
        return this.storageClient.blobs().putAsync(this.url, BlobType.BLOCK_BLOB, data);
    }

    /**
     * Creates a new {@link BlockBlobUrl} with the given pipeline.
     * @param pipeline
     *      A {@link Pipeline} object to set.
     * @return
     *      A {@link BlockBlobUrl} object with the given pipeline.
     */
    public BlockBlobUrl withPipeline(Pipeline pipeline) {
        return new BlockBlobUrl(this.url, pipeline);
    }
}
