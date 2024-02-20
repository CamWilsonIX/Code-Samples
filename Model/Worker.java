package com.irrelevxnce.jblgroundscare.Model;

import java.io.Serializable;

public class Worker implements Serializable {
    private String workerName;

    public Worker(){

    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public Worker(String workerName) {
        this.workerName = workerName;
    }

    public String getWorkerName() {
        return workerName;
    }

    @Override
    public String toString() {
        return workerName;
    }
}
