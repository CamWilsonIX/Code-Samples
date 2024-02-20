package com.irrelevxnce.jblgroundscare.Model;

import java.io.Serializable;

public class Job implements Serializable {
    private String jobType;

    public Job(){

    }

    public Job(String jobType) {
        this.jobType = jobType;
    }

    public void setJobName(String jobType) {
        this.jobType = jobType;
    }

    @Override
    public String toString() {
        return jobType;
    }

    public String getJobType() {
        return jobType;
    }
}
