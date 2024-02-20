package com.irrelevxnce.jblgroundscare.CustomRecyclerView;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.irrelevxnce.jblgroundscare.Model.Alert;
import com.irrelevxnce.jblgroundscare.Model.Report;
import com.irrelevxnce.jblgroundscare.R;

public class AlertRecyclerViewViewHolder extends RecyclerView.ViewHolder {
    private TextView clientName;
    private TextView numberOfDays;

    public AlertRecyclerViewViewHolder(View itemView) {
        super(itemView);
        clientName = itemView.findViewById(R.id.clientNameTextView);
        numberOfDays = itemView.findViewById(R.id.numberOfDaysTextView);
    }


    public void bindData(Alert alert) {
        clientName.setText(alert.getClient());
        int alertDays = alert.getDaysSinceLastJob();
        numberOfDays.setText(String.valueOf(alertDays));
    }
}
