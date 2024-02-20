package com.irrelevxnce.jblgroundscare.Utilities;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.irrelevxnce.jblgroundscare.Model.Client;
import com.irrelevxnce.jblgroundscare.Model.Job;
import com.irrelevxnce.jblgroundscare.Model.Worker;
import com.irrelevxnce.jblgroundscare.Enums.ModelTypes;

import java.util.List;

public class ReceiverFirebaseValueEventListener implements ValueEventListener {

    final private ModelTypes modelTypes;
    final private List<Object> itemList;

    public ReceiverFirebaseValueEventListener(ModelTypes modelTypes, List<Object> itemList) {
        this.modelTypes = modelTypes;
        this.itemList = itemList;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        for (DataSnapshot entrySnapshot : dataSnapshot.getChildren()) {
            itemList.add(getObjectType(entrySnapshot));
        }
    }

    private Object getObjectType (DataSnapshot dataSnapshot) {
        Object object = new Object();
        switch (modelTypes) {
            case Client:
                object = new Client(dataSnapshot.getKey());
                break;
            case Job:
                object = new Job(dataSnapshot.getKey());
                break;
            case Worker:
                object = new Worker(dataSnapshot.getKey());
                break;
        }
        return object;
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}
