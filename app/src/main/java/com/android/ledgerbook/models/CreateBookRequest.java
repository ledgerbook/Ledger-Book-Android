package com.android.ledgerbook.models;

import com.google.gson.annotations.SerializedName;

public class CreateBookRequest {
    @SerializedName("owner_name")
    private String ownerName;

    @SerializedName("business_name")
    private String businessName;

    @SerializedName("business_address")
    private String businessAddress;

    @SerializedName("email")
    private String email;

    public CreateBookRequest(String ownerName, String businessName, String businessAddress, String email) {
        this.ownerName = ownerName;
        this.businessName = businessName;
        this.businessAddress = businessAddress;
        this.email = email;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
