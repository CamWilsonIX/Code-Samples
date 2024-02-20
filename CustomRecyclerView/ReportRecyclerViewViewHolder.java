package com.irrelevxnce.jblgroundscare.CustomRecyclerView;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.irrelevxnce.jblgroundscare.Model.Report;
import com.irrelevxnce.jblgroundscare.R;

public class ReportRecyclerViewViewHolder extends RecyclerView.ViewHolder {
    private TextView jobTypeText;
    private TextView clientText;
    private TextView dateText;
    private TextView workerText;

    public ReportRecyclerViewViewHolder(View itemView) {
        super(itemView);
        jobTypeText = itemView.findViewById(R.id.jobTypeText);
        clientText = itemView.findViewById(R.id.clientText);
        dateText = itemView.findViewById(R.id.dateText);
        workerText = itemView.findViewById(R.id.workerText);
    }


    public void bindData(Report report) {
        jobTypeText.setText(report.getFormattedJobType());
        clientText.setText(report.getClient().getClientName());
        dateText.setText(report.getDate());
        workerText.setText(report.getWorker().getWorkerName());
    }
}
