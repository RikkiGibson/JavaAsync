package com.microsoft.azure.storage.blob;

import java.util.EnumSet;

public enum AccountSASService {
    /**
     * Permission to access blob resources granted.
     */
    BLOB('b'),

    /**
     * Permission to access file resources granted.
     */
    FILE('f'),

    /**
     * Permission to access queue resources granted.
     */
    QUEUE('q'),

    /**
     * Permission to access table resources granted.
     */
    TABLE('t');

    char value;

    /**
     * Create a <code>SharedAccessAccountService</code>.
     *
     * @param c
     *            The <code>char</code> which represents this service.
     */
    private AccountSASService(char c) {
        this.value = c;
    }

    /**
     * Converts the given services to a <code>String</code>.
     *
     * @param services
     *            The services to convert to a <code>String</code>.
     *
     * @return A <code>String</code> which represents the <code>SharedAccessAccountServices</code>.
     */
    static String servicesToString(EnumSet<AccountSASService> services) {
        if (services == null) {
            return Constants.EMPTY_STRING;
        }

        StringBuilder value = new StringBuilder();

        for (AccountSASService service : services) {
            value.append(service.value);
        }

        return value.toString();
    }

    /**
     * Creates an {@link EnumSet<AccountSASService>} from the specified services string.
     *
     * @param servicesString
     *            A <code>String</code> which represents the <code>SharedAccessAccountServices</code>.
     * @return A {@link EnumSet<AccountSASService>} generated from the given <code>String</code>.
     */
    static EnumSet<AccountSASService> servicesFromString(String servicesString) {
        EnumSet<AccountSASService> services = EnumSet.noneOf(AccountSASService.class);

        for (final char c : servicesString.toLowerCase().toCharArray()) {
            boolean invalidCharacter = true;

            for (AccountSASService service : AccountSASService.values()) {
                if (c == service.value) {
                    services.add(service);
                    invalidCharacter = false;
                    break;
                }
            }

            if (invalidCharacter) {
                throw new IllegalArgumentException(
                        String.format(SR.ENUM_COULD_NOT_BE_PARSED, "Services", servicesString));
            }
        }

        return services;
    }
}
