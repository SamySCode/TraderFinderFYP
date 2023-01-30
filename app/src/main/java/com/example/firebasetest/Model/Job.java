package com.example.firebasetest.Model;

public class Job {

    private String jobTitle;
    private String jobDescription;
    private String jobStartDate;
    private String jobEndDate;
    private String jobLocation;
    private String trade;
    private String JobsImageUrl;
    private String accountType;

    public Job() {

    }

    public Job(String jobTitle, String jobDescription, String jobStartDate, String jobEndDate, String jobLocation, String trade, String jobsImageUrl, String accountType) {
        this.jobTitle = jobTitle;
        this.jobDescription = jobDescription;
        this.jobStartDate = jobStartDate;
        this.jobEndDate = jobEndDate;
        this.jobLocation = jobLocation;
        this.trade = trade;
        JobsImageUrl = jobsImageUrl;
        this.accountType = accountType;
    }




    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getJobStartDate() {
        return jobStartDate;
    }

    public void setJobStartDate(String jobStartDate) {
        this.jobStartDate = jobStartDate;
    }

    public String getJobEndDate() {
        return jobEndDate;
    }

    public void setJobEndDate(String jobEndDate) {
        this.jobEndDate = jobEndDate;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }

    public String getTrade() {
        return trade;
    }

    public void setTrade(String trade) {
        this.trade = trade;
    }

    public String getJobsImageUrl() {
        return JobsImageUrl;
    }

    public void setJobsImageUrl(String jobsImageUrl) {
        this.JobsImageUrl = JobsImageUrl;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
