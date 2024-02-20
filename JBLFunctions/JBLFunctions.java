package com.irrelevxnce.jblgroundscare.JBLFunctions;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.irrelevxnce.jblgroundscare.Model.Client;
import com.irrelevxnce.jblgroundscare.Model.Job;
import com.irrelevxnce.jblgroundscare.Model.Worker;
import com.irrelevxnce.jblgroundscare.R;
import com.irrelevxnce.jblgroundscare.Utilities.CompareClientEntryValueEventListener;
import com.irrelevxnce.jblgroundscare.Utilities.ReceiverFirebaseValueEventListener;
import com.irrelevxnce.jblgroundscare.Enums.ModelTypes;
import com.irrelevxnce.jblgroundscare.Interfaces.DataReadyCallBack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class JBLFunctions extends AppCompatActivity {

    private ArrayList<Object> itemList;
    private String[] itemNames;

    public ArrayList<Object> getItemList (Boolean isWorker) {
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        itemNames = (isWorker)? getResources().getStringArray(R.array.workers): getResources().getStringArray(R.array.clients);
        itemList = new ArrayList<>();

        if (isWorker) {
            DatabaseReference dataRef = root.child("ExtraWorkers");
            dataRef.addListenerForSingleValueEvent(new ReceiverFirebaseValueEventListener(ModelTypes.Worker, itemList));
            for (String name : itemNames) {
                itemList.add(new Worker(name));
            }
        } else {
            DatabaseReference dataRef = root.child("ExtraClients");
            dataRef.addListenerForSingleValueEvent(new ReceiverFirebaseValueEventListener(ModelTypes.Client, itemList));
            for (String name : itemNames) {
                itemList.add(new Client(name));
            }
            sortClientList(itemList);
        }
        return itemList;
    }

    public ArrayList<Object> getJobList(){
        itemList = new ArrayList<>();
        itemNames = getResources().getStringArray(R.array.jobs);
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference dataRef = root.child("ExtraJobTypes");
        dataRef.addListenerForSingleValueEvent(new ReceiverFirebaseValueEventListener(ModelTypes.Job, itemList));
        for (String name : itemNames) {
            itemList.add(new Job(name));
        }
        sortJobList(itemList);
        return itemList;
    }

    public void getLatestEntryForClient(DataReadyCallBack callback) {
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference dataRef = root.child("Journal");
        dataRef.addListenerForSingleValueEvent(new CompareClientEntryValueEventListener(callback));
    }

    public void sortClientList(ArrayList<Object> clientList) {
        ArrayList<Client> updatedClientList = new ArrayList<>();
        for (Object obj : clientList) {
            if (obj instanceof Client) {
                updatedClientList.add((Client) obj);
            }
        }

        Collections.sort(updatedClientList, Comparator.comparing(Client::getClientName));
        updatedClientList.removeIf(client -> client.getClientName().equals("Select Client"));
        updatedClientList.add(0, new Client("Select Client"));

        clientList.clear();
        clientList.addAll(updatedClientList);
    }

    public void sortJobList(ArrayList<Object> jobList) {
        ArrayList<Job> updatedJobList = new ArrayList<>();
        for (Object obj : jobList) {
            if (obj instanceof Job) {
                updatedJobList.add((Job) obj);
            }
        }

        Collections.sort(updatedJobList, Comparator.comparing(Job::getJobType));
        updatedJobList.removeIf(job -> job.getJobType().equals("Select Job Type"));
        updatedJobList.add(0, new Job("Select Job Type"));

        jobList.clear();
        jobList.addAll(updatedJobList);
    }
}
