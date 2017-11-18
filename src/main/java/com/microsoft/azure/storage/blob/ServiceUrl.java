package com.microsoft.azure.storage.blob;

import com.microsoft.azure.storage.models.ContainerEnumerationResults;
import com.microsoft.azure.storage.pipeline.Pipeline;
import rx.Single;

public final class ServiceUrl extends StorageUrl {

    public ServiceUrl(String url, Pipeline pipeline) {
        super(url, pipeline);
    }

    public ContainerUrl createContainerUrl(String containerName) {
        return new ContainerUrl(this.url + "/" + containerName, this.storageClient);
    }

    public Single<ContainerEnumerationResults> listConatinersAsync() {
        return this.storageClient.services().listContainersAsync(this.url);
    }

    public ServiceUrl withPipeline(Pipeline pipeline) {
        return new ServiceUrl(this.url, pipeline);
    }
}
