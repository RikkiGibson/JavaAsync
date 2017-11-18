package com.microsoft.azure.storage.blob;

public enum DeleteBlobSnapshotOptions {

    NONE(""),

    INCLUDE("include"),

    ONLY("only");

    private final String id;
    DeleteBlobSnapshotOptions(String id) { this.id = id; }
    String getValue() { return id; }
}
