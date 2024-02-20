package com.irrelevxnce.jblgroundscare.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.irrelevxnce.jblgroundscare.CustomRecyclerView.ReportRecyclerViewAdapter;
import com.irrelevxnce.jblgroundscare.CustomSpinner.SpinnerAdapter;
import com.irrelevxnce.jblgroundscare.JBLFunctions.JBLFunctions;
import com.irrelevxnce.jblgroundscare.Model.Client;
import com.irrelevxnce.jblgroundscare.Model.Job;
import com.irrelevxnce.jblgroundscare.Model.Report;
import com.irrelevxnce.jblgroundscare.Model.Worker;
import com.irrelevxnce.jblgroundscare.R;
import com.irrelevxnce.jblgroundscare.Utilities.ManageViewEvents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OverviewActivity extends JBLFunctions {

    private RecyclerView recyclerView;
    private List<Report> reportList;
    private ConstraintLayout menuLayout;
    private ImageButton menuButton;
    private ImageButton closeButton;
    private Spinner filterClient;
    private Button applyButton;
    private SpinnerAdapter adapter;
    private String selectedFilter;
    private List<Report> filteredList;
    private TextView darken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        adapter = new SpinnerAdapter(this, getItemList(false));
        filteredList = new ArrayList<>();
        selectedFilter = "";

        recyclerView = findViewById(R.id.recyclerView);
        menuLayout = findViewById(R.id.menuLayout);
        menuButton = findViewById(R.id.menuButton);
        closeButton = findViewById(R.id.closeButton);
        filterClient = findViewById(R.id.filterClient);
        applyButton = findViewById(R.id.applyButton);
        darken = findViewById(R.id.darken3);

        menuButton.setOnClickListener(this :: openMenu);
        closeButton.setOnClickListener(this :: closeMenu);
        applyButton.setOnClickListener(this :: applyFilter);
        filterClient.setAdapter(adapter);
        filterClient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedFilter = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        getItemsFromFirebase();
    }

    private void applyFilter(View view) {
        filterReports(selectedFilter);
        closeButton.callOnClick();
    }

    private void filterReports(String selectedFilter) {
        filteredList.clear();
        ReportRecyclerViewAdapter recyclerViewAdapter;
        if (selectedFilter.equalsIgnoreCase("Select Client")) {
            recyclerViewAdapter = new ReportRecyclerViewAdapter(reportList);
            recyclerView.setAdapter(recyclerViewAdapter);
        } else {
            for (Report report : reportList) {
                Client client = report.getClient();
                if (client.getClientName().equalsIgnoreCase(selectedFilter)) {
                    filteredList.add(report);
                }
            }
            recyclerViewAdapter = new ReportRecyclerViewAdapter(filteredList);
            recyclerView.setAdapter(recyclerViewAdapter);
        }
        recyclerViewAdapter.setOnItemClickListener((position, report) -> {
            ArrayList<Job> jobs = new ArrayList<>(report.getJobType());
            Intent viewReportDetails = new Intent(OverviewActivity.this, ViewDetailsActivity.class);
            viewReportDetails.putExtra("client", report.getClient());
            viewReportDetails.putExtra("worker", report.getWorker());
            viewReportDetails.putExtra("date", report.getDate());
            viewReportDetails.putExtra("jobs", jobs);
            viewReportDetails.putExtra("comment", report.getComment());
            viewReportDetails.putExtra("reference", report.getReference());
            viewReportDetails.putExtra("imageURI", report.getImageURI());
            startActivity(viewReportDetails);
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.notifyDataSetChanged();
    }
    private void closeMenu(View view) {
        menuLayout.setVisibility(View.INVISIBLE);
        darken.setVisibility(View.INVISIBLE);
    }

    private void openMenu(View view) {
        menuLayout.setVisibility(View.VISIBLE);
        darken.setVisibility(View.VISIBLE);
    }

    private void getItemsFromFirebase() {
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference dataRef = root.child("Journal");

        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reportList = new ArrayList<>();
                for (DataSnapshot entrySnapshot : dataSnapshot.getChildren()) {
                    String date = entrySnapshot.child("date").getValue(String.class);
                    Client client = entrySnapshot.child("client").getValue(Client.class);
                    String comment = entrySnapshot.child("comment").getValue(String.class);
                    String reference = entrySnapshot.child("reference").getValue(String.class);
                    String imageURI = entrySnapshot.child("imageURI").getValue(String.class);

                    ArrayList<Job> jobList = new ArrayList<>();
                    DataSnapshot jobsSnapshot = entrySnapshot.child("jobs");
                    for (DataSnapshot jobSnapshot : jobsSnapshot.getChildren()) {
                        String jobType = jobSnapshot.child("jobType").getValue(String.class);
                        Job job = new Job(jobType);
                        jobList.add(job);
                    }
                    ArrayList<Job> jobs = new ArrayList<>(jobList);

                    Worker worker = entrySnapshot.child("worker").getValue(Worker.class);

                    Report report = new Report(jobs, client, date, worker, comment, reference, imageURI);
                    reportList.add(report);
                }
                setupRecyclerView();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OverviewActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupRecyclerView() {
        Collections.reverse(reportList);
        ReportRecyclerViewAdapter adapter = new ReportRecyclerViewAdapter(reportList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((position, report) -> {
            ArrayList<Job> jobs = new ArrayList<>(report.getJobType());
            Intent viewReportDetails = new Intent(OverviewActivity.this, ViewDetailsActivity.class);
            viewReportDetails.putExtra("client", report.getClient());
            viewReportDetails.putExtra("worker", report.getWorker());
            viewReportDetails.putExtra("date", report.getDate());
            viewReportDetails.putExtra("jobs", jobs);
            viewReportDetails.putExtra("comment", report.getComment());
            viewReportDetails.putExtra("reference", report.getReference());
            viewReportDetails.putExtra("imageURI", report.getImageURI());
            startActivity(viewReportDetails);
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.notifyDataSetChanged();
    }
}
