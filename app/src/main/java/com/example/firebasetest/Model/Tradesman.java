package com.example.firebasetest.Model;

public class Tradesman {
    private String firstName;
    private String lastName;
    private String contactNumber;
    private String region;
    private String pastJobsImageUrl;
    private String certificationsImageUrl;
    private String accountType;

    public Tradesman(String firstName, String lastName, String contactNumber, String region, String pastJobsImageUrl, String certificationsImageUrl, String accountType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactNumber = contactNumber;
        this.region = region;
        this.pastJobsImageUrl = pastJobsImageUrl;
        this.certificationsImageUrl = certificationsImageUrl;
        this.accountType = accountType;
    }

    // Getters and setters for the instance variables
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPastJobsImageUrl() {
        return pastJobsImageUrl;
    }

    public void setPastJobsImageUrl(String pastJobsImageUrl) {
        this.pastJobsImageUrl = pastJobsImageUrl;
    }

    public String getCertificationsImageUrl() {
        return certificationsImageUrl;
    }

    public void setCertificationsImageUrl(String certificationsImageUrl) {
        this.certificationsImageUrl = certificationsImageUrl;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
