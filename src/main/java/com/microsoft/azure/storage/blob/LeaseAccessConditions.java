package com.microsoft.azure.storage.blob;

public final class LeaseAccessConditions {
    private final String leaseId;

    public LeaseAccessConditions(String leaseId) {
        this.leaseId = leaseId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this.leaseId == null) {
            return obj == null;
        }

        return this.leaseId.equals(obj);
    }

    @Override
    public String toString() {
        return this.leaseId;
    }
}
