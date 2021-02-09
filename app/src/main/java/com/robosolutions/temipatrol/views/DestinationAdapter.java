package com.robosolutions.temipatrol.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.robosolutions.temipatrol.R;

import java.util.ArrayList;
import java.util.List;


public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.LocationViewHolder> {
    private final String TAG = "DestinationAdapter";
    private List<String> route;

    public DestinationAdapter(ArrayList<String> route) {
        this.route = route;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.location_card_item, parent, false);
        return new LocationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        holder.setText(position);
    }

    @Override
    public int getItemCount() {
        return route.size();
    }

    public class LocationViewHolder extends RecyclerView.ViewHolder {
        TextView locationTv;
        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            locationTv = itemView.findViewById(R.id.destinationTv);
        }

        void setText(int position) {
            locationTv.setText(route.get(position));
        }
    }
}
