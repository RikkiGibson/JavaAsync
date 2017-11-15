package com.microsoft.azure.storage.blob;

import com.microsoft.azure.storage.StorageClient;
import com.microsoft.azure.storage.implementation.StorageClientImpl;
import com.microsoft.azure.storage.models.ContainerGetPropertiesHeaders;
import com.microsoft.azure.storage.models.ServiceSetPropertiesHeaders;
import com.microsoft.rest.v2.RestClient;
import com.microsoft.rest.v2.RestResponse;
import rx.Single;
import rx.functions.Func1;

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

    public Single<ContainerGetPropertiesHeaders> headContainerAsync() {
        Single<RestResponse<ContainerGetPropertiesHeaders, Void>> restResponse = this.storageClient.containers().getPropertiesWithRestResponseAsync(this.containerUrl);

        return restResponse.map(new Func1<RestResponse<ContainerGetPropertiesHeaders, Void>, ContainerGetPropertiesHeaders>() {
            public ContainerGetPropertiesHeaders call(RestResponse<ContainerGetPropertiesHeaders, Void> restResponse) {
                return restResponse.headers();
            }
        });
    }

    public Single<Void> deleteContainerAsync() {
        return this.storageClient.containers().deleteAsync(this.containerUrl);
    }

    public BlockBlobUrl newBlockBlobUrl(String blobName) {
        return new BlockBlobUrl(this.containerUrl, blobName, this.storageClient.restClient());
    }
}
