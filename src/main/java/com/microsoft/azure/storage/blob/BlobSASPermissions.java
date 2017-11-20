package com.microsoft.azure.storage.blob;

/**
 * Specifies the set of possible permissions for a shared access policy.
 */
public enum BlobSASPermissions {
    /**
     * Specifies Read access granted.
     */
    READ,

    /**
     * Specifies Add access granted.
     */
    ADD,

    /**
     * Specifies Create access granted.
     */
    CREATE,

    /**
     * Specifies Write access granted.
     */
    WRITE,

    /**
     * Specifies Delete access granted.
     */
    DELETE;
}
