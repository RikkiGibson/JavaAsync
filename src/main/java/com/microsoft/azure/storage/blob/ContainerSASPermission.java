package com.microsoft.azure.storage.blob;

import java.util.EnumSet;

/**
 * Specifies the set of possible permissions for a container shared access policy.
 */
public enum ContainerSASPermission {
    /**
     * Specifies Read access granted.
     */
    READ('r'),

    /**
     * Specifies Add access granted.
     */
    ADD('a'),

    /**
     * Specifies Create access granted.
     */
    CREATE('c'),

    /**
     * Specifies Write access granted.
     */
    WRITE('w'),

    /**
     * Specifies Delete access granted.
     */
    DELETE('d'),

    /**
     * Specifies List access granted.
     */
    LIST('l');

    final private char value;

    /**
     * Create a <code>ContainerSASPermission</code>.
     *
     * @param c
     *            The <code>char</code> which represents this permission.
     */
    private ContainerSASPermission(char c) {
        this.value = c;
    }

    /**
     * Converts the given permissions to a <code>String</code>.
     *
     * @param permissions
     *            The permissions to convert to a <code>String</code>.
     *
     * @return A <code>String</code> which represents the <code>ContainerSASPermission</code>.
     */
    static String permissionsToString(EnumSet<ContainerSASPermission> permissions) {
        if (permissions == null) {
            return Constants.EMPTY_STRING;
        }

        // The service supports a fixed order => racwdl
        final StringBuilder builder = new StringBuilder();

        if (permissions.contains(ContainerSASPermission.READ)) {
            builder.append("r");
        }

        if (permissions.contains(ContainerSASPermission.ADD)) {
            builder.append("a");
        }

        if (permissions.contains(ContainerSASPermission.CREATE)) {
            builder.append("c");
        }

        if (permissions.contains(ContainerSASPermission.WRITE)) {
            builder.append("w");
        }

        if (permissions.contains(ContainerSASPermission.DELETE)) {
            builder.append("d");
        }

        if (permissions.contains(ContainerSASPermission.LIST)) {
            builder.append("l");
        }

        return builder.toString();
    }

    /**
     * Creates an {@link EnumSet<ContainerSASPermission>} from the specified permissions string.
     *
     * @param permString
     *            A <code>String</code> which represents the <code>ContainerSASPermission</code>.
     * @return A {@link EnumSet<ContainerSASPermission>} generated from the given <code>String</code>.
     */
    static EnumSet<ContainerSASPermission> permissionsFromString(String permString) {
        EnumSet<ContainerSASPermission> permissions = EnumSet.noneOf(ContainerSASPermission.class);

        for (final char c : permString.toLowerCase().toCharArray()) {
            boolean invalidCharacter = true;

            for (ContainerSASPermission perm : ContainerSASPermission.values()) {
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