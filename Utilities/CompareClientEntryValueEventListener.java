package com.irrelevxnce.jblgroundscare.Utilities;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.irrelevxnce.jblgroundscare.Interfaces.DataReadyCallBack;
import com.irrelevxnce.jblgroundscare.Model.Client;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class CompareClientEntryValueEventListener implements ValueEventListener {

    final private DataReadyCallBack callback;
    final private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    public CompareClientEntryValueEventListener(DataReadyCallBack callback) {
        this.callback = callback;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        HashMap<String, Long> clientLatestEntry = new HashMap<>();
        for (DataSnapshot entrySnapshot : snapshot.getChildren()) {
            Date date;
            Long longTimestamp = 0L;
            Client client = entrySnapshot.child("client").getValue(Client.class);
            String timestamp = entrySnapshot.child("date").getValue(String.class);

            try {
                date = dateFormat.parse(timestamp);
                longTimestamp = date.getTime();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            if (clientLatestEntry.containsKey(client.getClientName())) {
                if (longTimestamp > clientLatestEntry.get(client.getClientName())) {
                    clientLatestEntry.put(client.getClientName(), longTimestamp);
                }
            } else {
                clientLatestEntry.put(client.getClientName(), longTimestamp);
            }
        }
        callback.onDataReady(clientLatestEntry);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}
