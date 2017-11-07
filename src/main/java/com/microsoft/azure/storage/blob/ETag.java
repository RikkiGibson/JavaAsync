package com.microsoft.azure.storage.blob;

public final class ETag {

    private final String eTagString;

    public static final ETag NONE = new ETag(Constants.EMPTY_STRING);

    public static final ETag ANY = new ETag("*");

    public ETag(String eTagString) {
        this.eTagString = eTagString;
    }

    @Override
    public boolean equals(Object obj) {
        if (this.eTagString == null) {
            return obj == null;
        }

        return this.eTagString.equals(obj);
    }

    @Override
    public String toString() {
        return this.eTagString;
    }
}
