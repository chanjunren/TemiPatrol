package com.robosolutions.temipatrol.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.robosolutions.temipatrol.R;
import com.robosolutions.temipatrol.viewmodel.GlobalViewModel;

import java.util.List;


public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.LocationViewHolder>{
    private List<String> locations;
    private LocationViewHolder holder;

    public RouteAdapter() {
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
        this.holder = holder;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class LocationViewHolder extends RecyclerView.ViewHolder {
        private TextView locationTv;
        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            locationTv = itemView.findViewById(R.id.locationTv);
            locationTv.setText(locations.get(getAdapterPosition()));
        }

    }

}
