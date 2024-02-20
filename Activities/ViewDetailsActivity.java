package com.irrelevxnce.jblgroundscare.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.irrelevxnce.jblgroundscare.Model.Client;
import com.irrelevxnce.jblgroundscare.Model.Job;
import com.irrelevxnce.jblgroundscare.Model.Report;
import com.irrelevxnce.jblgroundscare.Model.Worker;
import com.irrelevxnce.jblgroundscare.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ViewDetailsActivity extends AppCompatActivity {

    private Client client;
    private String date, comment, reference, imageUri;
    private Worker worker;
    private ArrayList<Job> jobs;
    private TextView detailsClient, detailsWorker, detailsDate, detailsJobs, detailsComments;
    private FloatingActionButton fab;
    private ImageView uploadedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report_details);

        client = (Client) getIntent().getSerializableExtra("client");
        date = (String) getIntent().getStringExtra("date");
        worker = (Worker) getIntent().getSerializableExtra("worker");
        jobs = (ArrayList<Job>) getIntent().getSerializableExtra("jobs");
        comment = (String) getIntent().getStringExtra("comment");
        reference = (String) getIntent().getStringExtra("reference");
        imageUri = (String) getIntent().getStringExtra("imageURI");

        detailsClient = findViewById(R.id.details_client);
        detailsWorker = findViewById(R.id.details_worker);
        detailsDate = findViewById(R.id.details_date);
        detailsJobs = findViewById(R.id.details_jobs);
        detailsComments = findViewById(R.id.details_comments);
        uploadedImage = findViewById(R.id.reportImage);
        fab = findViewById(R.id.deleteButtonFAB);
        fab.setOnClickListener(this :: deleteEntry);

        StringBuilder jobsString = new StringBuilder();
        for (int i = 0; i < jobs.size(); i++) {
            jobsString.append(jobs.get(i).getJobType());
            if (i < jobs.size() - 1) {
                jobsString.append("\n");
            }
        }

        detailsClient.setText(client.getClientName());
        detailsWorker.setText(worker.getWorkerName());
        detailsDate.setText(date);
        detailsJobs.setText(jobsString);
        if(!comment.equals("")) {
            detailsComments.setText(comment);
        }
        if(!imageUri.equals("")) {
            Picasso.get().load(imageUri).resize(300, 300)
                    .centerCrop()
                    .into(uploadedImage);
        } else {
            uploadedImage.setVisibility(View.GONE);
        }
    }

    private void deleteEntry(View view) {
        FirebaseDatabase.getInstance().getReference().child("Journal").child(reference).removeValue()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Report deleted", Toast.LENGTH_LONG).show();
                    Intent backToLandingPage = new Intent(this, LandingPageActivity.class);
                    startActivity(backToLandingPage);
                })
                .addOnFailureListener(e -> {
                });
    }
}
