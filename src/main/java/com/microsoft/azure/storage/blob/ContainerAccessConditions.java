package com.microsoft.azure.storage.blob;

public final class ContainerAccessConditions {
    private final HttpAccessConditions httpAccessConditions;

    private final LeaseAccessConditions leaseID;

    public ContainerAccessConditions(HttpAccessConditions httpAccessConditions, LeaseAccessConditions leaseID) {
        this.httpAccessConditions = httpAccessConditions;
        this.leaseID = leaseID;
    }

    public HttpAccessConditions getHttpAccessConditions() {
        return httpAccessConditions;
    }

    public LeaseAccessConditions getLeaseID() {
        return leaseID;
    }
}
