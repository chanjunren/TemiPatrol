package com.robosolutions.temipatrol.views;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.robosolutions.temipatrol.R;
import com.robosolutions.temipatrol.viewmodel.GlobalViewModel;

import java.util.ArrayList;
import java.util.List;


public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.LocationViewHolder>{
    private final String TAG = "RouteAdapter";
    private List<String> route;

    public RouteAdapter(ArrayList<String> route) {
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
        Log.i(TAG, "setText called at " + position + " | value: " + route.get(position));
    }

    @Override
    public int getItemCount() {
        return route.size();
    }

    public class LocationViewHolder extends RecyclerView.ViewHolder {
        TextView locationTv;
        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            locationTv = itemView.findViewById(R.id.locationTv);
        }

        void setText(int position) {
            locationTv.setText(route.get(position));
        }
    }
}
