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

import com.microsoft.azure.storage.models.ContainerEnumerationResults;
import com.microsoft.azure.storage.pipeline.Pipeline;
import rx.Single;

public final class ServiceUrl extends StorageUrl {

    public ServiceUrl(String url, Pipeline pipeline) {
        super(url, pipeline);
    }

    public ContainerUrl createContainerUrl(String containerName) {
        return new ContainerUrl(this.url + "/" + containerName, this.storageClient.pipeline());
    }

    public Single<ContainerEnumerationResults> listConatinersAsync() {
        return this.storageClient.services().listContainersAsync(this.url);
    }

    public ServiceUrl withPipeline(Pipeline pipeline) {
        return new ServiceUrl(this.url, pipeline);
    }
}
