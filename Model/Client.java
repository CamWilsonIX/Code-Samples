package com.irrelevxnce.jblgroundscare.Model;

import java.io.Serializable;

public class Client implements Serializable {
    private String clientName;

    public Client() {

    }

    public Client(String clientName) {
        this.clientName = clientName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    @Override
    public String toString() {
        return clientName;
    }
}