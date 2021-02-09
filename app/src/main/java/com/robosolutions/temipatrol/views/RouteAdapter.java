package com.robosolutions.temipatrol.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.robosolutions.temipatrol.R;
import com.robosolutions.temipatrol.model.TemiRoute;

import java.util.ArrayList;
import java.util.List;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RouteViewHolder> {
    private final String TAG = "DestinationAdapter";
    private List<TemiRoute> routes;
    private OnRouteClickListener onRouteClickListener;

    public RouteAdapter(ArrayList<TemiRoute> routes, OnRouteClickListener onRouteClickListener) {
        this.routes = routes;
        this.onRouteClickListener = onRouteClickListener;
    }

    @NonNull
    @Override
    public RouteAdapter.RouteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.route_card_item, parent, false);
        return new RouteAdapter.RouteViewHolder(v, onRouteClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteAdapter.RouteViewHolder holder, int position) {
        holder.setText(position);

    }

    @Override
    public int getItemCount() {
        return routes.size();
    }

    public class RouteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView routeTv;
        OnRouteClickListener onRouteClickListener;
        public RouteViewHolder(@NonNull View itemView, OnRouteClickListener onRouteClickListener) {
            super(itemView);
            routeTv = itemView.findViewById(R.id.routeTv);
            this.onRouteClickListener = onRouteClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onRouteClickListener.onRouteClick(getAdapterPosition());
        }

        void setText(int position) {
            routeTv.setText(routes.get(position).getRouteTitle());
        }
    }

    public interface OnRouteClickListener {
        void onRouteClick(int position);
    }
}
