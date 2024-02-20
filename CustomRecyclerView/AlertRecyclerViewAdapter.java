package com.irrelevxnce.jblgroundscare.CustomRecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.irrelevxnce.jblgroundscare.Interfaces.OnItemClickListener;
import com.irrelevxnce.jblgroundscare.Model.Alert;
import com.irrelevxnce.jblgroundscare.Model.Report;
import com.irrelevxnce.jblgroundscare.R;

import java.util.List;

public class AlertRecyclerViewAdapter extends RecyclerView.Adapter<AlertRecyclerViewViewHolder> {
    private final List<Alert> itemList;
    private OnItemClickListener onItemClickListener;

    public AlertRecyclerViewAdapter(List<Alert> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public AlertRecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.alert_element, parent, false);
        return new AlertRecyclerViewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AlertRecyclerViewViewHolder holder, int position) {
        Alert alert = itemList.get(position);
        holder.bindData(alert);

        holder.itemView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                Alert alertHolder = getItem(holder.getAdapterPosition());
                onItemClickListener.onItemClick(holder.getAdapterPosition(), alertHolder);
            }
        });
    }

    public Alert getItem(int position) {
        return itemList.get(position);
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
