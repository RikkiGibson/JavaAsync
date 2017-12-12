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

import com.microsoft.azure.storage.implementation.StorageClientImpl;
import com.microsoft.azure.storage.models.ContainerCreateHeaders;
import com.microsoft.azure.storage.models.ContainerDeleteHeaders;
import com.microsoft.azure.storage.models.ContainerGetPropertiesHeaders;
import com.microsoft.azure.storage.models.PublicAccessType;
import com.microsoft.rest.v2.RestResponse;
import com.microsoft.rest.v2.http.HttpPipeline;
import org.joda.time.DateTime;
import io.reactivex.Single;

/**
 * Represents a URL to the Azure Storage container allowing you to manipulate its blobs.
 */
public final class ContainerURL extends StorageURL {

    public ContainerURL( String url, HttpPipeline pipeline) {
        super(url, pipeline);
    }

    /**
     * Create creates a new container within a storage account.
     * If a container with the same name already exists, the operation fails.
     * For more information, see https://docs.microsoft.com/rest/api/storageservices/create-container.
     * @param containerAccessConditions
     * @return
     */
    public Single<RestResponse<ContainerCreateHeaders, Void>> createAsync(
            Integer timeout, String metadata, PublicAccessType access) {
        return this.storageClient.containers().createWithRestResponseAsync(super.url, timeout, metadata, null, null);
    }

    /**
     * Delete marks the specified container for deletion. The container and any blobs contained within it are later deleted during garbage collection.
     * For more information, see https://docs.microsoft.com/rest/api/storageservices/delete-container.
     * @return
     */
    public Single<RestResponse<ContainerDeleteHeaders, Void>> deleteAsync(
            Integer timeout, ContainerAccessConditions containerAccessConditions) {
        return this.storageClient.containers().deleteWithRestResponseAsync(super.url, timeout,
                containerAccessConditions.getLeaseID().toString(),
                containerAccessConditions.getHttpAccessConditions().getIfModifiedSince(),
                containerAccessConditions.getHttpAccessConditions().getIfUnmodifiedSince(),
                containerAccessConditions.getHttpAccessConditions().getIfMatch().toString(),
                containerAccessConditions.getHttpAccessConditions().getIfNoneMatch().toString(),
                null);
    }

    /**
     * GetPropertiesAndMetadata returns the container's metadata and system properties.
     * For more information, see https://docs.microsoft.com/rest/api/storageservices/get-container-metadata.
     * @return
     */
    public Single<RestResponse<ContainerGetPropertiesHeaders, Void>> getPropertiesAndMetadataAsync(Integer timeout, LeaseAccessConditions leaseAccessConditions) {
        return this.storageClient.containers().getPropertiesWithRestResponseAsync(super.url, timeout, leaseAccessConditions.toString(), null);
    }

    public void setMetadataAsync() {
        //return this.storageClient.containers().setMetadataWithRestResponseAsync();
        return;
    }

    /**
     * Creates a new {@link BlockBlobURL} object by concatenating the blobName to the end of
     * ContainerURL's URL. The new BlockBlobUrl uses the same request policy pipeline as the ContainerURL.
     * To change the pipeline, create the BlockBlobUrl and then call its WithPipeline method passing in the
     * desired pipeline object. Or, call this package's NewBlockBlobUrl instead of calling this object's
     * NewBlockBlobUrl method.
     * @param blobName
     * @return
     */
    public BlockBlobURL createBlockBlobURL(String blobName) {
        return new BlockBlobURL(this.url + '/' + blobName, this.storageClient.httpPipeline());
    }

    /**
     * Creates a new {@link ContainerURL} with the given pipeline.
     * @param pipeline
     *      A {@link HttpPipeline} object to set.
     * @return
     *      A {@link ContainerURL} object with the given pipeline.
     */
    public ContainerURL withPipeline(HttpPipeline pipeline) {
        return new ContainerURL(this.url, pipeline);
    }
}
