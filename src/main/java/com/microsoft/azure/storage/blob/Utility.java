package com.microsoft.azure.storage.blob;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


public class Utility {

    /**
     * Thread local for storing GMT date format.
    */
    private static ThreadLocal<DateFormat>
            RFC1123_GMT_DATE_TIME_FORMATTER = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            final DateFormat formatter = new SimpleDateFormat(RFC1123_PATTERN, LOCALE_US);
            formatter.setTimeZone(GMT_ZONE);
            return formatter;
        }
    };

    /**
     * Stores a reference to the RFC1123 date/time pattern.
     */
    private static final String RFC1123_PATTERN = "EEE, dd MMM yyyy HH:mm:ss z";

    /**
     * Stores a reference to the GMT time zone.
     */
    public static final TimeZone GMT_ZONE = TimeZone.getTimeZone("GMT");

    /**
     * Stores a reference to the US locale.
     */
    public static final Locale LOCALE_US = Locale.US;

    /**
     * Returns the current GMT date/time String using the RFC1123 pattern.
     *
     * @return A <code>String</code> that represents the current GMT date/time using the RFC1123 pattern.
     */
    public static String getGMTTime() {
        return getGMTTime(new Date());
    }

    /**
     * Returns the GTM date/time String for the specified value using the RFC1123 pattern.
     *
     * @param date
     *            A <code>Date</code> object that represents the date to convert to GMT date/time in the RFC1123
     *            pattern.
     *
     * @return A <code>String</code> that represents the GMT date/time for the specified value using the RFC1123
     *         pattern.
     */
    public static String getGMTTime(final Date date) {
        return RFC1123_GMT_DATE_TIME_FORMATTER.get().format(date);
    }

    /**
     * Parses a query string into a one to many hashmap.
     *
     * @param parseString
     *            the string to parse
     * @return a HashMap<String, String[]> of the key values.
     * @throws UnsupportedEncodingException
     */
    public static TreeMap<String, String[]> parseQueryString(String parseString, boolean lowerCaseKey) throws UnsupportedEncodingException {
        //Comparator<String> c = new Comparator.<String>naturalOrder();
        final TreeMap<String, String[]> retVals = new TreeMap<String, String[]>( new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareTo(s2);
            }
        });
        if (Utility.isNullOrEmpty(parseString)) {
            return retVals;
        }

        // split name value pairs by splitting on the 'c&' character
        final String[] valuePairs = parseString.split("&");

        // for each field value pair parse into appropriate map entries
        for (int m = 0; m < valuePairs.length; m++) {
            // Getting key and value for a single query parameter
            final int equalDex = valuePairs[m].indexOf("=");
            String key = Utility.safeDecode(valuePairs[m].substring(0, equalDex));
            if (lowerCaseKey) {
                key = key.toLowerCase(Utility.LOCALE_US);
            }

            String value = Utility.safeDecode(valuePairs[m].substring(equalDex + 1));

            // add to map
            String[] keyValues = retVals.get(key);

            // check if map already contains key
            if (keyValues == null) {
                // map does not contain this key
                keyValues = new String[] { value };
                retVals.put(key, keyValues);
            }
            else {
                // TODO: Need to see if we can remove
                throw new RuntimeException("Jeff made me do this!!! :((((");
//                // map contains this key already so append
//                final String[] newValues = new String[keyValues.length + 1];
//                for (int j = 0; j < keyValues.length; j++) {
//                    newValues[j] = keyValues[j];
//                }
//
//                newValues[newValues.length] = value;
            }
        }

        return retVals;
    }

    /**
     * Returns a value that indicates whether the specified string is <code>null</code> or empty.
     *
     * @param value
     *            A <code>String</code> being examined for <code>null</code> or empty.
     *
     * @return <code>true</code> if the specified value is <code>null</code> or empty; otherwise, <code>false</code>
     */
    public static boolean isNullOrEmpty(final String value) {
        return value == null || value.length() == 0;
    }

    /**
     * Returns a value that indicates whether the specified string is <code>null</code>, empty, or whitespace.
     *
     * @param value
     *            A <code>String</code> being examined for <code>null</code>, empty, or whitespace.
     *
     * @return <code>true</code> if the specified value is <code>null</code>, empty, or whitespace; otherwise,
     *         <code>false</code>
     */
    public static boolean isNullOrEmptyOrWhitespace(final String value) {
        return value == null || value.trim().length() == 0;
    }

    /**
     * Performs safe decoding of the specified string, taking care to preserve each <code>+</code> character, rather
     * than replacing it with a space character.
     *
     * @param stringToDecode
     *            A <code>String</code> that represents the string to decode.
     *
     * @return A <code>String</code> that represents the decoded string.
     *
     * @throws UnsupportedEncodingException
     *             If a storage service error occurred.
     */
    public static String safeDecode(final String stringToDecode) throws UnsupportedEncodingException {
        if (stringToDecode.length() == 0) {
            return Constants.EMPTY_STRING;
        }

        // '+' are decoded as ' ' so preserve before decoding
        if (stringToDecode.contains("+")) {
            final StringBuilder outBuilder = new StringBuilder();

            int startDex = 0;
            for (int m = 0; m < stringToDecode.length(); m++) {
                if (stringToDecode.charAt(m) == '+') {
                    if (m > startDex) {
                        outBuilder.append(URLDecoder.decode(stringToDecode.substring(startDex, m),
                                Constants.UTF8_CHARSET));
                    }

                    outBuilder.append("+");
                    startDex = m + 1;
                }
            }

            if (startDex != stringToDecode.length()) {
                outBuilder.append(URLDecoder.decode(stringToDecode.substring(startDex, stringToDecode.length()),
                        Constants.UTF8_CHARSET));
            }

            return outBuilder.toString();
        }
        else {
            return URLDecoder.decode(stringToDecode, Constants.UTF8_CHARSET);
        }
    }
}
