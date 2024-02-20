package com.irrelevxnce.jblgroundscare.Model;

import android.net.Uri;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class Report{
    private ArrayList<Job> jobs;
    private Client client;
    private String date;
    private Worker worker;
    private String comment;
    private String reference;
    private String imageURI;

    public Report(ArrayList<Job> jobTypes, Client client, String date, Worker worker, String comment, String reference, String imageURI) {
        this.jobs = jobTypes;
        this.client = client;
        this.date = date;
        this.worker = worker;
        this.comment = comment;
        this.reference = reference;
        this.imageURI = imageURI;
    }

    public Report(){

    }

    public ArrayList<Job> getJobType() {
        return jobs;
    }

    public Client getClient() {
        return client;
    }

    public String getDate() {
        return date;
    }

    public Worker getWorker() {
        return worker;
    }
    public String getComment() { return comment; }
    public String getReference() { return reference; }
    public String getImageURI() { return imageURI; }

    @Override
    public String toString() {
        return "Report{" +
                "jobType=" + jobs +
                ", clientName=" + client +
                ", date='" + date + '\'' +
                ", workerName=" + worker +
                ", comment=" + comment + ", reference=" + reference + ", imageURI=" + imageURI + '}';
    }

    public String getFormattedJobType() {
        if (jobs.size() > 1) {
            return "Multiple Jobs";
        } else {
            return jobs.get(0).getJobType();
        }
    }
}
