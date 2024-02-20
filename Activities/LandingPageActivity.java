package com.irrelevxnce.jblgroundscare.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.irrelevxnce.jblgroundscare.Interfaces.DataReadyCallBack;
import com.irrelevxnce.jblgroundscare.JBLFunctions.JBLFunctions;
import com.irrelevxnce.jblgroundscare.Model.Alert;
import com.irrelevxnce.jblgroundscare.R;
import com.irrelevxnce.jblgroundscare.Utilities.ExportToFirebase;
import com.irrelevxnce.jblgroundscare.Utilities.ManageViewEvents;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class LandingPageActivity extends JBLFunctions implements DataReadyCallBack {

    private String username;
    private TextView usernameTextView, upcomingTasksTextView;
    private Button addReportButton;
    private ImageButton manageItems;
    private Button overview;
    private Button alertsButton;
    private HashMap<String, Integer> alerts = new HashMap<String, Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        getLatestEntryForClient(this);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            username = null;
        } else {
            username = extras.getString("username");
        }

        usernameTextView = findViewById(R.id.usernameTextView);
        upcomingTasksTextView = findViewById(R.id.upcomingTasks);
        addReportButton = findViewById(R.id.addReport);
        manageItems = findViewById(R.id.manageItems);
        overview = findViewById(R.id.Overview);
        alertsButton = findViewById(R.id.Alerts);

        usernameTextView.setText(username);
        addReportButton.setOnClickListener(view -> ManageViewEvents.manageSetOnClickListener(view, this, AddReportActivity.class, "username", username));
        manageItems.setOnClickListener(view -> ManageViewEvents.manageSetOnClickListener(view, this, ManageItemsActivity.class));
        overview.setOnClickListener(view -> ManageViewEvents.manageSetOnClickListener(view, this, OverviewActivity.class));
        alertsButton.setOnClickListener(view -> ManageViewEvents.manageSetOnClickListener(view, this, AlertActivity.class));
    }

    @Override
    public void onDataReady(HashMap<String, Long> clientLatestEntry) {
        ExportToFirebase exportToFirebase = new ExportToFirebase("Alerts");
        Calendar calendar = Calendar.getInstance();
        long currentTimestamp = calendar.getTimeInMillis();
        for (Map.Entry<String, Long> entry : clientLatestEntry.entrySet()) {
            Long mostRecentEntry = entry.getValue();
            String clientName = entry.getKey();
            long differenceInMillis = currentTimestamp - mostRecentEntry;
            int daysSinceLastEntry = (int) (differenceInMillis / 86400000);
            alerts.put(clientName, daysSinceLastEntry);

            if (alerts.containsKey(entry.getKey()) && daysSinceLastEntry < 14) {
                alerts.remove(clientName);
                exportToFirebase.removeChild("Alert_" + clientName);
            }
        }
        for (Map.Entry<String, Integer> entry : alerts.entrySet()) {
            String referenceTitle = "Alert_" + entry.getKey();
            System.out.println(entry.getKey() + " has not been updated for " + entry.getValue() + " days.");
            Alert createAlert = new Alert(entry.getKey(), entry.getValue(), false);
            exportToFirebase.addChildValue(referenceTitle, "client", createAlert.getClient());
            exportToFirebase.addChildValue(referenceTitle, "daysSinceLastJob", createAlert.getDaysSinceLastJob());
            exportToFirebase.addChildValue(referenceTitle, "dismissed", createAlert.isDismissed());
        }
        upcomingTasksTextView.setText("There are " + alerts.size() + " alerts.");
    }
}
