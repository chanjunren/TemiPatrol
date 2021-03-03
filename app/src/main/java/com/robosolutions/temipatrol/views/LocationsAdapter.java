package com.robosolutions.temipatrol.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.robosolutions.temipatrol.R;
import com.robosolutions.temipatrol.viewmodel.GlobalViewModel;

import java.util.ArrayList;
import java.util.List;


public class LocationsAdapter extends RecyclerView.Adapter<LocationsAdapter.LocationViewHolder> {
    private final String TAG = "LocationsAdapter";
    private List<String> locations;
    private GlobalViewModel viewModel;
    private CreateRouteAdapter routeAdapter;
    public LocationsAdapter(ArrayList<String> locations, GlobalViewModel viewModel,
                            CreateRouteAdapter routeAdapter) {
        this.locations = locations;
        this.viewModel = viewModel;
        this.routeAdapter = routeAdapter;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.saved_location_card_item, parent, false);
        return new LocationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        holder.setText(position);
        holder.locationTv.setOnClickListener(v -> {
            viewModel.insertLocationIntoHelper(locations.get(position));
            routeAdapter.notifyItemInserted(viewModel.getCreateRouteHelperList().size());
        });
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public class LocationViewHolder extends RecyclerView.ViewHolder {
        TextView locationTv;
        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            locationTv = itemView.findViewById(R.id.destinationTv);
        }

        void setText(int position) {
            locationTv.setText(locations.get(position));
        }
    }
}
