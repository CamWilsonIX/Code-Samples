package com.irrelevxnce.jblgroundscare.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.irrelevxnce.jblgroundscare.CustomSpinner.SpinnerAdapter;
import com.irrelevxnce.jblgroundscare.JBLFunctions.JBLFunctions;
import com.irrelevxnce.jblgroundscare.Model.Client;
import com.irrelevxnce.jblgroundscare.Model.Job;
import com.irrelevxnce.jblgroundscare.Model.Worker;
import com.irrelevxnce.jblgroundscare.R;
import com.irrelevxnce.jblgroundscare.Utilities.DatePicker;
import com.irrelevxnce.jblgroundscare.Utilities.ExportToFirebase;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class AddReportActivity extends JBLFunctions {
    private static final int REQUEST_GALLERY = 89593;
    private Spinner clientSpinner, jobSpinner;
    private SpinnerAdapter clientAdapter, jobAdapter;
    private Calendar calendar;
    private EditText editTextDate, editTextComment;
    private DatePickerDialog datePickerDialog;
    private Button saveReportButton;
    private String username;
    private ExportToFirebase exportToFirebase;
    private TextView darken;
    private ImageView tick, uploadedImage;
    private LinearLayout containerLayout;
    private HashMap<Integer, Job> filterableJobList;
    private List<Job> finalJobList;
    private int positionInJobList = 0;
    private ImageButton cameraButton;
    private Uri imageURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_report);

        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        jobSpinner = findViewById(R.id.spinnerJobs);
        clientSpinner = findViewById(R.id.spinnerClients);
        editTextDate = findViewById(R.id.editTextDate);
        saveReportButton = findViewById(R.id.saveReportButton);
        containerLayout = findViewById(R.id.containerLayout);
        darken = findViewById(R.id.darken);
        tick = findViewById(R.id.tick);
        editTextComment = findViewById(R.id.commentBox);
        cameraButton = findViewById(R.id.addPhotoButton);
        uploadedImage = findViewById(R.id.uploadedImage);

        filterableJobList = new HashMap<>();
        finalJobList = new ArrayList<>();

        clientAdapter = new SpinnerAdapter(AddReportActivity.this, getItemList(false));
        jobAdapter = new SpinnerAdapter(AddReportActivity.this, getJobList());

        clientSpinner.setAdapter(clientAdapter);
        jobSpinner.setAdapter(jobAdapter);

        editTextDate.setOnClickListener(this :: showCalendar);
        saveReportButton.setOnClickListener(this :: saveReport);
        cameraButton.setOnClickListener(this :: openGallery);
        jobSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                addJobElement(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        datePickerDialog = DatePicker.createDialog(this, editTextDate, calendar, year, month, dayOfMonth);
        Bundle extras = getIntent().getExtras();
        username = extras.getString("username");
    }

    private void openGallery(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Runnable runnable = () -> {
            if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK && data != null) {
                imageURI = data.getData();
                Picasso.get().load(imageURI).resize(300, 300)
                        .centerCrop()
                        .into(uploadedImage);
            }
        };
        uploadedImage.setVisibility(View.VISIBLE);
        runnable.run();
    }

    private void saveReport(View view) {
        if (!String.valueOf(editTextDate.getText()).equals("")) {
            exportToFirebase = new ExportToFirebase("Journal");
            Client exportClient = new Client(clientSpinner.getSelectedItem().toString());
            Worker exportWorker = new Worker(username);
            String referenceTitle;

            for (Map.Entry<Integer, Job> entry : filterableJobList.entrySet()) {
                Job job = entry.getValue();
                finalJobList.add(job);
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            long milliseconds = 0;

            if (finalJobList.size() == 1) {
                try {
                    String dateString = editTextDate.getText().toString();
                    Date date = dateFormat.parse(dateString);
                    milliseconds = date.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                referenceTitle = String.valueOf(milliseconds) + exportClient;
            } else {
                try {
                    String dateString = editTextDate.getText().toString();
                    Date date = dateFormat.parse(dateString);
                    milliseconds = date.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                referenceTitle = milliseconds + "Multi" + exportClient;
            }

            if (exportClient.getClientName().equalsIgnoreCase("Select client") || finalJobList.size() == 0) {
                if (exportClient.getClientName().equalsIgnoreCase("Select client")) {
                    Toast.makeText(this, "Please select a client!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Please select at least 1 job type!", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Report saved successfully!", Toast.LENGTH_LONG).show();
                exportToFirebase.addChildValue(referenceTitle, "jobs", finalJobList);
                exportToFirebase.addChildValue(referenceTitle, "client", exportClient);
                exportToFirebase.addChildValue(referenceTitle, "date", String.valueOf(editTextDate.getText()));
                exportToFirebase.addChildValue(referenceTitle, "worker", exportWorker);
                exportToFirebase.addChildValue(referenceTitle, "comment", String.valueOf(editTextComment.getText()));
                exportToFirebase.addChildValue(referenceTitle, "reference", referenceTitle);

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();
                if (imageURI != null) {
                    StorageReference imageRef = storageRef.child("images/" + referenceTitle);
                    UploadTask uploadTask = imageRef.putFile(imageURI);

                    uploadTask.addOnSuccessListener(taskSnapshot -> {
                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String downloadUrl = uri.toString();
                            exportToFirebase.addChildValue(referenceTitle, "imageURI", downloadUrl);
                        });
                    }).addOnFailureListener(exception -> {
                        Toast.makeText(this, "Check your internet connection", Toast.LENGTH_LONG).show();
                    });
                } else {
                    String defaultUrl = "";
                    exportToFirebase.addChildValue(referenceTitle, "imageURI", defaultUrl);
                }
                darken.setVisibility(View.VISIBLE);
                tick.setVisibility(View.VISIBLE);
                new Handler().postDelayed(this :: backToLandingPage, 1000);
            }


        } else {
            Toast.makeText(this, "Please enter the date!", Toast.LENGTH_LONG).show();
        }
    }

    private void backToLandingPage() {
        Intent backToLandingPage = new Intent(this, LandingPageActivity.class);
        startActivity(backToLandingPage);
    }

    private void showCalendar(View view) {
        datePickerDialog.show();
    }

    private void addJobElement(String text) {
        View jobElement = LayoutInflater.from(this).inflate(R.layout.job_element, containerLayout, false);
        TextView textView = jobElement.findViewById(R.id.jobName);
        textView.setText(text);

        Button deleteButton = jobElement.findViewById(R.id.deleteButton);
        deleteButton.setTag(positionInJobList);
        deleteButton.setOnClickListener(view -> {
            int position = (int) view.getTag();
            filterableJobList.remove(position);
            containerLayout.removeView(jobElement);
            System.out.println(filterableJobList);
        });

        if (!text.equals("Select Job Type")) {
            Job job = new Job(text);
            filterableJobList.put(positionInJobList, job);
            positionInJobList++;
            containerLayout.addView(jobElement);
        }

        System.out.println(filterableJobList);
    }
}
