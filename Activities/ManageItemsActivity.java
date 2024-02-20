package com.irrelevxnce.jblgroundscare.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.irrelevxnce.jblgroundscare.R;
import com.irrelevxnce.jblgroundscare.Utilities.ExportToFirebase;

public class ManageItemsActivity extends AppCompatActivity {

    private EditText itemName;
    private RadioButton newWorker, newClient, newJobType;
    private Button saveButton;
    private TextView darken;
    private ImageView tick;
    private ExportToFirebase exportToFirebase;
    private int selectedType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_items);

        itemName = findViewById(R.id.itemName);
        newWorker = findViewById(R.id.newWorker);
        newClient = findViewById(R.id.newClient);
        newJobType = findViewById(R.id.newJobType);
        saveButton = findViewById(R.id.saveButton);
        darken = findViewById(R.id.darken2);
        tick = findViewById(R.id.tick2);

        saveButton.setOnClickListener(this :: addNewItems);
        newWorker.setOnClickListener(this :: setNewWorkerSelected);
        newClient.setOnClickListener(this :: setNewClientSelected);
        newJobType.setOnClickListener(this :: setNewJobTypeSelected);
    }

    private void setNewWorkerSelected(View view) {
        selectedType = 0;
    }

    private void setNewClientSelected(View view) {
        selectedType = 1;
    }

    private void setNewJobTypeSelected(View view) {
        selectedType = 2;
    }

    private void addNewItems(View view) {
        if (!String.valueOf(itemName.getText()).equals("")) {
            String item = String.valueOf(itemName.getText());

            switch (selectedType) {
                case 0:
                    exportToFirebase = new ExportToFirebase("ExtraWorkers");
                    exportToFirebase.makeChildReferences(item);
                break;
                case 1:
                    exportToFirebase = new ExportToFirebase("ExtraClients");
                    exportToFirebase.makeChildReferences(item);
                break;
                case 2:
                    exportToFirebase = new ExportToFirebase("ExtraJobTypes");
                    exportToFirebase.makeChildReferences(item);
                break;
            }
            darken.setVisibility(View.VISIBLE);
            tick.setVisibility(View.VISIBLE);
            new Handler().postDelayed(this :: finish, 1000);
        }
    }
}
