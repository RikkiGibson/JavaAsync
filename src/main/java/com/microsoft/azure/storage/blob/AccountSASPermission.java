package com.microsoft.azure.storage.blob;

import java.util.EnumSet;

public enum AccountSASPermission {
    /**
     * Permission to read resources and list queues and tables granted.
     */
    READ('r'),

    /**
     * Permission to add messages, table entities, and append to blobs granted.
     */
    ADD('a'),

    /**
     * Permission to create blobs and files granted.
     */
    CREATE('c'),

    /**
     * Permission to write resources granted.
     */
    WRITE('w'),

    /**
     * Permission to delete resources granted.
     */
    DELETE('d'),

    /**
     * Permission to list blob containers, blobs, shares, directories, and files granted.
     */
    LIST('l'),

    /**
     * Permissions to update messages and table entities granted.
     */
    UPDATE('u'),

    /**
     * Permission to get and delete messages granted.
     */
    PROCESS_MESSAGES('p');

    final private char value;

    /**
     * Create a <code>SharedAccessAccountPermissions</code>.
     *
     * @param c
     *            The <code>char</code> which represents this permission.
     */
    private AccountSASPermission(char c) {
        this.value = c;
    }

    /**
     * Converts the given permissions to a <code>String</code>.
     *
     * @param permissions
     *            The permissions to convert to a <code>String</code>.
     *
     * @return A <code>String</code> which represents the <code>SharedAccessAccountPermissions</code>.
     */
    static String permissionsToString(EnumSet<AccountSASPermission> permissions) {
        if (permissions == null) {
            return Constants.EMPTY_STRING;
        }

        final StringBuilder builder = new StringBuilder();

        if (permissions.contains(AccountSASPermission.READ)) {
            builder.append("r");
        }

        if (permissions.contains(AccountSASPermission.ADD)) {
            builder.append("a");
        }

        if (permissions.contains(AccountSASPermission.CREATE)) {
            builder.append("c");
        }

        if (permissions.contains(AccountSASPermission.WRITE)) {
            builder.append("w");
        }

        if (permissions.contains(AccountSASPermission.DELETE)) {
            builder.append("d");
        }

        if (permissions.contains(AccountSASPermission.LIST)) {
            builder.append("l");
        }

        if (permissions.contains(AccountSASPermission.UPDATE)) {
            builder.append("u");
        }

        if (permissions.contains(AccountSASPermission.PROCESS_MESSAGES)) {
            builder.append("p");
        }

        return builder.toString();
    }

    /**
     * Creates an {@link EnumSet<AccountSASPermission>} from the specified permissions string.
     *
     * @param permString
     *            A <code>String</code> which represents the <code>SharedAccessAccountPermissions</code>.
     * @return A {@link EnumSet<AccountSASPermission>} generated from the given <code>String</code>.
     */
    static EnumSet<AccountSASPermission> permissionsFromString(String permString) {
        EnumSet<AccountSASPermission> permissions = EnumSet.noneOf(AccountSASPermission.class);

        for (final char c : permString.toLowerCase().toCharArray()) {
            boolean invalidCharacter = true;

            for (AccountSASPermission perm : AccountSASPermission.values()) {
                if (c == perm.value) {
                    permissions.add(perm);
                    invalidCharacter = false;
                    break;
                }

            }

            if (invalidCharacter) {
                throw new IllegalArgumentException(
                        String.format(SR.ENUM_COULD_NOT_BE_PARSED, "Permissions", permString));
            }
        }

        return permissions;
    }
}
