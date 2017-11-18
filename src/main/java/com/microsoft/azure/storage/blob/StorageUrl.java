package com.microsoft.azure.storage.blob;

import com.microsoft.azure.storage.StorageClient;
import com.microsoft.azure.storage.implementation.StorageClientImpl;
import com.microsoft.azure.storage.pipeline.Pipeline;

public abstract class StorageUrl {

    protected final String url;

    protected final StorageClient storageClient;

    protected StorageUrl(String url, Pipeline pipeline) {
        if (url == null) {
            throw new IllegalArgumentException("url cannot be null.");
        }
        if (pipeline == null) {
            throw new IllegalArgumentException("pipeline cannot be null.");
        }

        this.url = url;
        this.storageClient = new StorageClientImpl(pipeline);
    }

    @Override
    public String toString() {
        return this.url;
    }
}
