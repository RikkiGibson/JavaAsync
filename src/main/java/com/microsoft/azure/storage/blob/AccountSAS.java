package com.microsoft.azure.storage.blob;

import org.apache.commons.lang3.StringUtils;

import java.security.InvalidKeyException;
import java.util.Date;
import java.util.EnumSet;

/**
 * AccountSAS is used to generate a Shared Access Signature (SAS) for an Azure Storage account.
 */
public final class AccountSAS extends BaseSAS {

    private final EnumSet<AccountSASService> services;

    private final EnumSet<AccountSASResourceType> resourceTypes;

    /**
     * AccountSAS is used to generate a Shared Access Signature (SAS) for an Azure Storage account.
     * @param version
     *      If null or empty, this defaults to <code>Constants.HeaderConstants.TARGET_STORAGE_VERSION</code>
     * @param protocol
     * @param startTime
     * @param expiryTime
     * @param permissions
     * @param ipRange
     * @param services
     * @param resourceTypes
     */
    public AccountSAS(String version, SASProtocol protocol, Date startTime, Date expiryTime,
                      EnumSet<AccountSASPermission> permissions, IPRange ipRange, EnumSet<AccountSASService> services,
                      EnumSet<AccountSASResourceType> resourceTypes) {
        super(version, protocol, startTime, expiryTime, AccountSASPermission.permissionsToString(permissions), ipRange);
        this.services = services;
        this.resourceTypes = resourceTypes;
    }

    @Override
    public SASQueryParameters GenerateSASQueryParameters(SharedKeyCredentials sharedKeyCredentials) throws InvalidKeyException {
        if (sharedKeyCredentials == null) {
            throw new IllegalArgumentException("SharedKeyCredentials cannot be null.");
        }

        String servicesString = AccountSASService.servicesToString(this.services);
        String resourceTypesString = AccountSASResourceType.resourceTypesToString(this.resourceTypes);
        String stringToSign = StringUtils.join(
                new String[]{
                        sharedKeyCredentials.getAccountName(),
                        super.permissions,
                        servicesString,
                        resourceTypesString,
                        Utility.getUTCTimeOrEmpty(super.startTime),
                        Utility.getUTCTimeOrEmpty(super.expiryTime),
                        super.ipRange.toString(),
                        super.protocol.toString(),
                        super.version,
                        Constants.EMPTY_STRING // Account SAS requires an additional newline character
                },
                '\n'
        );

        String signature = sharedKeyCredentials.computeHmac256(stringToSign);

        return new SASQueryParameters(super.version, servicesString, resourceTypesString, super.protocol.toString(), super.startTime,
                super.expiryTime, super.ipRange, null, null, super.permissions, signature);
    }
}
