package com.microsoft.azure.storage.blob;

import java.net.Inet4Address;

/**
 * A continuous range of IP addresses.
 */
public final class IPRange {
    private String ipMin;
    private String ipMax;

    /**
     * Creates an IP Range using the specified single IP address. The IP address must be IPv4.
     *
     * @param ip
     *      the single IP address
     */
    public IPRange(String ip) {
        Utility.assertNotNull("ip", ip);
        IPRange.validateIPAddress(ip);

        this.ipMin = ip;
        this.ipMax = ip;
    }

    /**
     * Creates an IP Range using the specified minimum and maximum IP addresses. The IP addresses must be IPv4.
     *
     * @param minimumIP
     *      the minimum IP address of the range
     * @param maximumIP
     *      the maximum IP address of the range
     */
    public IPRange(String minimumIP, String maximumIP) {
        Utility.assertNotNull("minimumIP", minimumIP);
        Utility.assertNotNull("maximumIP", maximumIP);

        IPRange.validateIPAddress(minimumIP);
        IPRange.validateIPAddress(maximumIP);

        this.ipMin = minimumIP;
        this.ipMax = maximumIP;
    }

    /**
     * The minimum IP address for the range, inclusive.
     * Will match {@link #getIpMax()} if this <code>IPRange</code> represents a single IP address.
     *
     * @return The minimum IP address
     */
    public String getIpMin() {
        return this.ipMin;
    }

    /**
     * The maximum IP address for the range, inclusive.
     * Will match {@link #getIpMin()} if this <code>IPRange</code> represents a single IP address.
     *
     * @return The maximum IP address
     */
    public String getIpMax() {
        return this.ipMax;
    }

    /**
     * Output the single IP address or range of IP addresses.
     *
     * @return the single IP address or range of IP addresses formatted as a <code>String</code>
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder(this.ipMin);
        if (!this.ipMin.equals(this.ipMax)) {
            str.append("-");
            str.append(this.ipMax);
        }

        return str.toString();
    }

    /**
     * Validate that the IP address is IPv4.
     *
     * @param ipAddress
     *              the IP address to validate
     */
    private static void validateIPAddress(String ipAddress) {
        try {
            @SuppressWarnings("unused")
            Inet4Address address = (Inet4Address) Inet4Address.getByName(ipAddress);
        }
        catch (Exception ex) {
            throw new IllegalArgumentException(String.format(SR.INVALID_IP_ADDRESS, ipAddress), ex);
        }
    }
}