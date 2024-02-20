package com.irrelevxnce.jblgroundscare.CustomRecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.irrelevxnce.jblgroundscare.Model.Report;
import com.irrelevxnce.jblgroundscare.R;

import java.util.List;

public class ReportRecyclerViewAdapter extends RecyclerView.Adapter<ReportRecyclerViewViewHolder> {
    private final List<Report> itemList;
    private OnItemClickListener onItemClickListener;

    public ReportRecyclerViewAdapter(List<Report> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ReportRecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_element, parent, false);
        return new ReportRecyclerViewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportRecyclerViewViewHolder holder, int position) {
        Report report = itemList.get(position);
        holder.bindData(report);

        holder.itemView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                Report reportHolder = getItem(holder.getAdapterPosition());
                onItemClickListener.onItemClick(holder.getAdapterPosition(), reportHolder);
            }
        });
    }

    public Report getItem(int position) {
        return itemList.get(position);
    }

    public interface OnItemClickListener {
        void onItemClick(int position, Report report);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        try {
            return itemList.size();
        } catch (Exception e) {
            return 0;
        }
    }
}
