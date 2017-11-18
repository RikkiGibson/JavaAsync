package com.microsoft.azure.storage.blob;

import com.microsoft.azure.storage.StorageClient;
import com.microsoft.azure.storage.implementation.StorageClientImpl;
import com.microsoft.azure.storage.models.ContainerGetPropertiesHeaders;
import com.microsoft.azure.storage.models.ServiceSetPropertiesHeaders;
import com.microsoft.azure.storage.pipeline.Pipeline;
import com.microsoft.rest.v2.RestClient;
import com.microsoft.rest.v2.RestResponse;
import rx.Single;
import rx.functions.Func1;

import java.nio.channels.Pipe;

public final class ContainerUrl extends StorageUrl {

    public ContainerUrl(String url, Pipeline pipeline) {
        super(url, pipeline);
    }

    public Single<Void> createAsync(ContainerAccessConditions containerAccessConditions) {
        return this.storageClient.containers().createAsync(this.url, containerAccessConditions);
    }

    public Single<ContainerGetPropertiesHeaders> getPropertiesAndMetadataAsync() {
        Single<RestResponse<ContainerGetPropertiesHeaders, Void>> restResponse = this.storageClient.containers().getPropertiesWithRestResponseAsync(this.url);

        return restResponse.map(new Func1<RestResponse<ContainerGetPropertiesHeaders, Void>, ContainerGetPropertiesHeaders>() {
            public ContainerGetPropertiesHeaders call(RestResponse<ContainerGetPropertiesHeaders, Void> restResponse) {
                return restResponse.headers();
            }
        });
    }

    public Single<Void> deleteAsync() {
        return this.storageClient.containers().deleteAsync(this.url);
    }

    public BlockBlobUrl createBlockBlobUrl(String blobName) {
        return new BlockBlobUrl(this.url, blobName, this.storageClient.restClient());
    }

    public ContainerUrl withPipeline(Pipeline pipeline) {
        return new ContainerUrl(this.url, pipeline);
    }
}
