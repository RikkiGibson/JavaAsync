package com.microsoft.azure.storage.blob;

import com.microsoft.azure.storage.StorageClient;
import com.microsoft.azure.storage.implementation.StorageClientImpl;
import com.microsoft.rest.v2.RestClient;
import rx.Single;

public final class ContainerUrl {

    private final StorageClient storageClient;

    private final String containerUrl;

    public ContainerUrl(String containerUrl, RestClient restClient) {
        this.storageClient = new StorageClientImpl(restClient);
        this.containerUrl = containerUrl;
    }

    public Single<Void> createContainerAsync() {
        return this.storageClient.containers().createAsync(this.containerUrl);
    }

    public Single<Void> headContainerAsync() {
        return this.storageClient.containers().getPropertiesAsync(this.containerUrl);
    }

    public Single<Void> deleteContainerAsync() {
        return this.storageClient.containers().deleteAsync(this.containerUrl);
    }

    public BlockBlobUrl newBlockBlobUrl(String blobName) {
        return new BlockBlobUrl(this.containerUrl, blobName, this.storageClient.restClient());
    }
}
