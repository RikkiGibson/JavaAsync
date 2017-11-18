package com.microsoft.azure.storage.blob;

import com.microsoft.azure.storage.StorageClient;
import com.microsoft.azure.storage.implementation.StorageClientImpl;
import com.microsoft.azure.storage.models.BlobType;
import com.microsoft.azure.storage.pipeline.Pipeline;
import com.microsoft.rest.v2.RestClient;
import rx.Single;

import java.io.InputStream;

public final class BlockBlobUrl extends BlobUrl {

    public BlockBlobUrl(String url, Pipeline pipeline) {
        super(url, pipeline);
    }

    public Single<Void> putBlobAsync(byte[] data) {
        return this.storageClient.blobs().putAsync(this.url, BlobType.BLOCK_BLOB, data);
    }

    public BlockBlobUrl withPipeline(Pipeline pipeline) {
        return new BlockBlobUrl(this.url, pipeline);
    }
}
