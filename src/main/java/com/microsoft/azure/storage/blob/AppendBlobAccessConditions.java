package com.microsoft.azure.storage.blob;

public final class AppendBlobAccessConditions {
    private final Long ifAppendPositionEquals;

    private final Long ifMaxSizeLessThanOrEqual;

    public AppendBlobAccessConditions(Long ifAppendPositionEquals, Long ifMaxSizeLessThanOrEqual) {
        this.ifAppendPositionEquals = ifAppendPositionEquals;
        this.ifMaxSizeLessThanOrEqual = ifMaxSizeLessThanOrEqual;
    }

    public Long getIfAppendPositionEquals() {
        return ifAppendPositionEquals;
    }

    public Long getIfMaxSizeLessThanOrEqual() {
        return ifMaxSizeLessThanOrEqual;
    }
}
