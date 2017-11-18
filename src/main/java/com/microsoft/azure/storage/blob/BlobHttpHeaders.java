package com.microsoft.azure.storage.blob;

public final class BlobHttpHeaders {

    /**
     * Represents the cache-control value stored for the blob.
     */
    private final String cacheControl;

    /**
     * Represents the content-disposition value stored for the blob. If this field has not been set for the blob, the
     * field returns <code>null</code>.
     */
    private final String contentDisposition;

    /**
     * Represents the content-encoding value stored for the blob. If this field has not been set for the blob, the field
     * returns <code>null</code>.
     */
    private final String contentEncoding;

    /**
     * Represents the content-language value stored for the blob. If this field has not been set for the blob, the field
     * returns <code>null</code>.
     */
    private final String contentLanguage;

    /**
     * Represents the content MD5 value stored for the blob.
     */
    private final String contentMD5;

    /**
     * Represents the content type value stored for the blob. If this field has not been set for the blob, the field
     * returns <code>null</code>.
     */
    private String contentType;

    public BlobHttpHeaders(String cacheControl, String contentDisposition, String contentEncoding, String contentLanguage, String contentMD5, String contentType) {
        this.cacheControl = cacheControl;
        this.contentDisposition = contentDisposition;
        this.contentEncoding = contentEncoding;
        this.contentLanguage = contentLanguage;
        this.contentMD5 = contentMD5;
        this.contentType = contentType;
    }

    public String getCacheControl() {
        return cacheControl;
    }

    public String getContentDisposition() {
        return contentDisposition;
    }

    public String getContentEncoding() {
        return contentEncoding;
    }

    public String getContentLanguage() {
        return contentLanguage;
    }

    public String getContentMD5() {
        return contentMD5;
    }

    public String getContentType() {
        return contentType;
    }
}
