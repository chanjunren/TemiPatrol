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


public class CreateRouteAdapter extends RecyclerView.Adapter<CreateRouteAdapter.CreateRouteViewHolder> {
    private final String TAG = "CreateRouteAdapter";
    private List<String> route;

    public CreateRouteAdapter(ArrayList<String> route) {
        this.route = route;
    }

    @NonNull
    @Override
    public CreateRouteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.create_route_card_item, parent, false);
        return new CreateRouteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CreateRouteViewHolder holder, int position) {
        holder.setText(position);
    }

    @Override
    public int getItemCount() {
        return route.size();
    }

    public class CreateRouteViewHolder extends RecyclerView.ViewHolder {
        TextView locationTv;
        public CreateRouteViewHolder(@NonNull View itemView) {
            super(itemView);
            locationTv = itemView.findViewById(R.id.destinationTv);
        }

        void setText(int position) {
            locationTv.setText(route.get(position));
        }
    }
}
