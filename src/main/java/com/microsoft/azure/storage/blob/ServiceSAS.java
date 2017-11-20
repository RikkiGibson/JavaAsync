package com.microsoft.azure.storage.blob;

import org.apache.commons.lang3.StringUtils;

import java.security.InvalidKeyException;
import java.util.Date;
import java.util.EnumSet;

public final class ServiceSAS extends BaseSAS {

    private final String containerName;

    private final String blobName;

    private final String identifier;

    private final String cacheControl;

    private final String contentDisposition;

    private final String contentEncoding;

    private final String contentLanguage;

    private final String contentType;

    public ServiceSAS(String version, String protocol, Date startTime, Date expiryTime, String permissions,
                      IPRange ipRange, String containerName, String blobName, String identifier, String cacheControl,
                      String contentDisposition, String contentEncoding, String contentLanguage, String contentType) {
        super(version, protocol, startTime, expiryTime, permissions, ipRange);
        this.containerName = containerName;
        this.blobName = blobName;
        this.identifier = identifier;
        this.cacheControl = cacheControl;
        this.contentDisposition = contentDisposition;
        this.contentEncoding = contentEncoding;
        this.contentLanguage = contentLanguage;
        this.contentType = contentType;
    }

    @Override
    public SASQueryParameters GenerateSASQueryParameters(SharedKeyCredentials sharedKeyCredentials) throws InvalidKeyException {
        if (sharedKeyCredentials == null) {
            throw new IllegalArgumentException("SharedKeyCredentials cannot be null.");
        }

        String resource = "c";
        if (!Utility.isNullOrEmpty(this.blobName)) {
            resource = "b";
        }

        String startTimeString = Constants.EMPTY_STRING;
        if (super.startTime != null) {
            startTimeString = Utility.getGMTTime(super.startTime);
        }

        String expiryTimeString = Constants.EMPTY_STRING;
        if (super.expiryTime != null) {
            expiryTimeString = Utility.getGMTTime(super.expiryTime);
        }

        String ipRangeString = Constants.EMPTY_STRING;
        if (super.ipRange != null) {
            ipRangeString = super.ipRange.toString();
        }

        String stringToSign = StringUtils.join(
                new String[]{
                        super.permissions,
                        startTimeString,
                        expiryTimeString,
                        getCanonicalName(sharedKeyCredentials.getAccountName()),
                        this.identifier,
                        ipRangeString,
                        super.protocol,
                        super.version,
                        this.cacheControl,
                        this.contentDisposition,
                        this.contentEncoding,
                        this.contentLanguage,
                        this.contentType
                },
                '\n'
        );

        String signature = sharedKeyCredentials.computeHmac256(stringToSign);

        return new SASQueryParameters(super.version, null, null, super.protocol, super.startTime,
                super.expiryTime, super.ipRange, this.identifier, resource, super.permissions, signature);
    }

    public String blobSasPermissionsToString(EnumSet<BlobSASPermissions> blobSASPermissions) {
        if (blobSASPermissions == null) {
            return Constants.EMPTY_STRING;
        }

        // The service supports a fixed order => racwdl
        final StringBuilder builder = new StringBuilder();

        if (blobSASPermissions.contains(BlobSASPermissions.READ)) {
            builder.append("r");
        }

        if (blobSASPermissions.contains(BlobSASPermissions.ADD)) {
            builder.append("a");
        }

        if (blobSASPermissions.contains(BlobSASPermissions.CREATE)) {
            builder.append("c");
        }

        if (blobSASPermissions.contains(BlobSASPermissions.WRITE)) {
            builder.append("w");
        }

        if (blobSASPermissions.contains(BlobSASPermissions.DELETE)) {
            builder.append("d");
        }

        return builder.toString();
    }

    /**
     * Converts this policy's permissions to a string.
     *
     * @return A <code>String</code> that represents the shared access permissions in the "racwdl" format,
     *         which is described at {@link #setPermissionsFromString(String)}.
     */
    public String containerSASPermissionsToString(EnumSet<ContainerSASPermissions> containerSASPermissions) {
        if (containerSASPermissions == null) {
            return Constants.EMPTY_STRING;
        }

        // The service supports a fixed order => racwdl
        final StringBuilder builder = new StringBuilder();

        if (containerSASPermissions.contains(ContainerSASPermissions.READ)) {
            builder.append("r");
        }

        if (containerSASPermissions.contains(ContainerSASPermissions.ADD)) {
            builder.append("a");
        }

        if (containerSASPermissions.contains(ContainerSASPermissions.CREATE)) {
            builder.append("c");
        }

        if (containerSASPermissions.contains(ContainerSASPermissions.WRITE)) {
            builder.append("w");
        }

        if (containerSASPermissions.contains(ContainerSASPermissions.DELETE)) {
            builder.append("d");
        }

        if (containerSASPermissions.contains(ContainerSASPermissions.LIST)) {
            builder.append("d");
        }

        return builder.toString();
    }

    private String getCanonicalName(String accountName) {
        // Container: "/blob/account/containername"
        // Blob:      "/blob/account/containername/blobname"
        String canoncialName = StringUtils.join(
                new String[]{
                        "/blob",
                        accountName,
                        this.containerName
                },
                '/');
        if (!Utility.isNullOrEmpty(this.blobName)) {
            canoncialName += this.blobName.replace("\\", "/");
        }

        return canoncialName;
    }
}
