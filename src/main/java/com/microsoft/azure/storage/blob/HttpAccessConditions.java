package com.microsoft.azure.storage.blob;

import java.util.Date;

public final class HttpAccessConditions {

    private final Date ifModifiedSince;
    private final Date ifUnmodifiedSince;
    private final ETag ifMatch;
    private final ETag ifNoneMatch;

    public HttpAccessConditions(Date ifModifiedSince, Date ifUnmodifiedSince, ETag ifMatch, ETag ifNoneMatch) {
        this.ifModifiedSince = ifModifiedSince;
        this.ifUnmodifiedSince = ifUnmodifiedSince;
        this.ifMatch = ifMatch;
        this.ifNoneMatch = ifNoneMatch;
    }

    public Date getIfModifiedSince() {
        return ifModifiedSince;
    }

    public Date getIfUnmodifiedSince() {
        return ifUnmodifiedSince;
    }

    public ETag getIfMatch() {
        return ifMatch;
    }

    public ETag getIfNoneMatch() {
        return ifNoneMatch;
    }
}
