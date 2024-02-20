package com.irrelevxnce.jblgroundscare.CustomSpinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.irrelevxnce.jblgroundscare.Model.Client;
import com.irrelevxnce.jblgroundscare.Model.Job;
import com.irrelevxnce.jblgroundscare.Model.Worker;
import com.irrelevxnce.jblgroundscare.R;


import java.util.ArrayList;

public class SpinnerAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Object> itemsToDisplay;

    public SpinnerAdapter(Context context, ArrayList<Object>itemsToDisplay) {
        this.context = context;
        this.itemsToDisplay = itemsToDisplay;
    }

    @Override
    public int getCount() {
        return itemsToDisplay.size();
    }

    @Override
    public Object getItem(int i) {
        return itemsToDisplay.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.spinner_element, viewGroup, false);
        TextView itemText = rootView.findViewById(R.id.itemText);

        String item = itemsToDisplay.get(i).getClass().getSimpleName();
        switch (item) {
            case "Worker":
                itemText.setText(((Worker) itemsToDisplay.get(i)).getWorkerName());
            break;
            case "Client":
                itemText.setText(((Client) itemsToDisplay.get(i)).getClientName());
            break;
            case "Job":
                itemText.setText(((Job) itemsToDisplay.get(i)).getJobType());
            break;
        }
        return rootView;
    }
}
