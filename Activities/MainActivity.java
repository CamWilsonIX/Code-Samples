package com.irrelevxnce.jblgroundscare.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.irrelevxnce.jblgroundscare.JBLFunctions.JBLFunctions;
import com.irrelevxnce.jblgroundscare.Model.Alert;
import com.irrelevxnce.jblgroundscare.Model.Client;
import com.irrelevxnce.jblgroundscare.Model.Worker;
import com.irrelevxnce.jblgroundscare.R;
import com.irrelevxnce.jblgroundscare.CustomSpinner.SpinnerAdapter;
import com.irrelevxnce.jblgroundscare.Utilities.ExportToFirebase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends JBLFunctions{

    private Spinner workerSpinner;
    private SpinnerAdapter workerAdapter;
    private Button startButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        workerSpinner = findViewById(R.id.spinnerWorkers);
        startButton = findViewById(R.id.startButton);

        workerAdapter = new SpinnerAdapter(MainActivity.this, getItemList(true));
        workerSpinner.setAdapter(workerAdapter);

        startButton.setOnClickListener(this :: startButtonOnClick);
    }

    private void startButtonOnClick(View view) {
        Intent intentLandingPage = new Intent(this, LandingPageActivity.class);
        Worker selectedWorker = (Worker) workerSpinner.getSelectedItem();
        String username = selectedWorker.getWorkerName();
        intentLandingPage.putExtra("username", username);
        startActivity(intentLandingPage);
    }


}