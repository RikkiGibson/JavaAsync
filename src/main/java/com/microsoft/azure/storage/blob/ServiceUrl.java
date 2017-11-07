package com.microsoft.azure.storage.blob;

import com.microsoft.azure.storage.StorageClient;
import com.microsoft.azure.storage.implementation.StorageClientImpl;
import com.microsoft.azure.storage.models.ContainerEnumerationResults;
import com.microsoft.rest.v2.RestClient;
import rx.Single;

public final class ServiceUrl {

    private final StorageClient storageClient;

    private final String url;

    public ServiceUrl(String url, RestClient restClient) {
        this.url = url;
        this.storageClient = new StorageClientImpl(restClient);
    }


    public ContainerUrl createContainerUrl(String containerName) {
        return new ContainerUrl(url + "/" + containerName, this.storageClient.restClient());
    }

    public Single<ContainerEnumerationResults> listConatinersAsync() {
        return this.storageClient.services().listContainersAsync();
    }
}
