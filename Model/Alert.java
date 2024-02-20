package com.irrelevxnce.jblgroundscare.Model;

import java.io.Serializable;

public class Alert implements Serializable {
    String client;
    int daysSinceLastJob;
    boolean dismissed;

    public Alert() {}

    @Override
    public String toString() {
        return "Alert{" +
                "client=" + client +
                ", daysSinceLastJob=" + daysSinceLastJob +
                ", dismissed=" + dismissed +
                '}';
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public int getDaysSinceLastJob() {
        return daysSinceLastJob;
    }

    public void setDaysSinceLastJob(int daysSinceLastJob) {
        this.daysSinceLastJob = daysSinceLastJob;
    }

    public boolean isDismissed() {
        return dismissed;
    }

    public void setDismissed(boolean dismissed) {
        this.dismissed = dismissed;
    }

    public Alert(String client, int daysSinceLastJob, boolean dismissed) {
        this.client = client;
        this.daysSinceLastJob = daysSinceLastJob;
        this.dismissed = dismissed;
    }
}
