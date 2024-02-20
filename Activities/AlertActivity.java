package com.irrelevxnce.jblgroundscare.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.irrelevxnce.jblgroundscare.CustomRecyclerView.AlertRecyclerViewAdapter;
import com.irrelevxnce.jblgroundscare.CustomRecyclerView.ReportRecyclerViewAdapter;
import com.irrelevxnce.jblgroundscare.JBLFunctions.JBLFunctions;
import com.irrelevxnce.jblgroundscare.Model.Alert;
import com.irrelevxnce.jblgroundscare.Model.Client;
import com.irrelevxnce.jblgroundscare.Model.Job;
import com.irrelevxnce.jblgroundscare.Model.Report;
import com.irrelevxnce.jblgroundscare.Model.Worker;
import com.irrelevxnce.jblgroundscare.R;
import com.irrelevxnce.jblgroundscare.Utilities.ExportToFirebase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AlertActivity extends JBLFunctions {

    private ArrayList<Alert> alertList = new ArrayList<Alert>();;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerts);
        recyclerView = findViewById(R.id.AlertRecyclerView);
        getItemsFromFirebase();
    }

    private void getItemsFromFirebase() {
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference dataRef = root.child("Alerts");

        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot entrySnapshot : dataSnapshot.getChildren()) {
                    String clientName = entrySnapshot.child("client").getValue(String.class);
                    int numberOfDays = entrySnapshot.child("daysSinceLastJob").getValue(Integer.class);
                    Boolean dismissed = entrySnapshot.child("dismissed").getValue(Boolean.class);

                    Alert alert = new Alert(clientName, numberOfDays, dismissed);
                    alertList.add(alert);
                }
                setupRecyclerView();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AlertActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupRecyclerView() {
        AlertRecyclerViewAdapter adapter = new AlertRecyclerViewAdapter(alertList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.notifyDataSetChanged();
    }
}
