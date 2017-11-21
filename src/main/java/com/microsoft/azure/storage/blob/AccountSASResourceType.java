package com.microsoft.azure.storage.blob;

import java.util.EnumSet;

/**
 * Specifies the set of possible resource types for an account shared access account policy.
 */
public enum AccountSASResourceType {
    /**
     * Permission to access service level APIs granted.
     */
    SERVICE('s'),

    /**
     * Permission to access container level APIs (Blob Containers, Tables, Queues, File Shares) granted.
     */
    CONTAINER('c'),

    /**
     * Permission to access object level APIs (Blobs, Table Entities, Queue Messages, Files) granted.
     */
    OBJECT('o');

    char value;

    /**
     * Create a <code>AccountSASResourceType</code>.
     *
     * @param c
     *            The <code>char</code> which represents this resource type.
     */
    private AccountSASResourceType(char c) {
        this.value = c;
    }

    /**
     * Converts the given resource types to a <code>String</code>.
     *
     * @param types
     *            The resource types to convert to a <code>String</code>.
     *
     * @return A <code>String</code> which represents the <code>AccountSASResourceTypes</code>.
     */
    static String resourceTypesToString(EnumSet<AccountSASResourceType> types) {
        if (types == null) {
            return Constants.EMPTY_STRING;
        }

        StringBuilder builder = new StringBuilder();

        if (types.contains(AccountSASResourceType.SERVICE)) {
            builder.append("s");
        }

        if (types.contains(AccountSASResourceType.CONTAINER)) {
            builder.append("c");
        }

        if (types.contains(AccountSASResourceType.OBJECT)) {
            builder.append("o");
        }

        return builder.toString();
    }

    /**
     * Creates an {@link EnumSet<AccountSASResourceType>} from the specified resource types string.
     *
     * @param resourceTypesString
     *            A <code>String</code> which represents the <code>AccountSASResourceTypes</code>.
     * @return A {@link EnumSet<AccountSASResourceType>} generated from the given <code>String</code>.
     */
    static EnumSet<AccountSASResourceType> resourceTypesFromString(String resourceTypesString) {
        EnumSet<AccountSASResourceType> resources = EnumSet.noneOf(AccountSASResourceType.class);

        for (final char c : resourceTypesString.toLowerCase().toCharArray()) {
            boolean invalidCharacter = true;

            for (AccountSASResourceType rsrc : AccountSASResourceType.values()) {
                if (c == rsrc.value) {
                    resources.add(rsrc);
                    invalidCharacter = false;
                    break;
                }
            }

            if (invalidCharacter) {
                throw new IllegalArgumentException(
                        String.format(SR.ENUM_COULD_NOT_BE_PARSED, "Resource Types", resourceTypesString));
            }
        }

        return resources;
    }
}