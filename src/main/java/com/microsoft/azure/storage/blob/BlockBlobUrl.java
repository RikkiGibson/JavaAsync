package com.microsoft.azure.storage.blob;

import com.microsoft.azure.storage.StorageClient;
import com.microsoft.azure.storage.implementation.StorageClientImpl;
import com.microsoft.azure.storage.models.BlobType;
import com.microsoft.rest.v2.RestClient;
import rx.Single;

import java.io.InputStream;

public final class BlockBlobUrl {

    private final String blockBlobUrl;

    private final StorageClient storageClient;

    private String containerName;

    public BlockBlobUrl(String containerName, String blockblobUrl, RestClient restClient) {
        this.storageClient = new StorageClientImpl(restClient);
        this.blockBlobUrl = blockblobUrl;
        this.containerName = containerName;
    }

    public Single<Void> putBlobAsync(byte[] data) {
        return this.storageClient.blobs().putAsync(this.containerName, this.blockBlobUrl, BlobType.BLOCK_BLOB, data);
    }

    public Single<InputStream> getBlobAsync() {
        return this.storageClient.blobs().getAsync(this.containerName, this.blockBlobUrl);
    }
}
