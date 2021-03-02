package com.robosolutions.temipatrol.views;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.robosolutions.temipatrol.R;
import com.robosolutions.temipatrol.model.TemiRoute;

import java.util.ArrayList;
import java.util.List;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RouteViewHolder> {
    private final String TAG = "CreateRouteAdapter";
    private List<TemiRoute> routes;
    private OnRouteClickListener onRouteClickListener;
    private boolean[] expandedArrHelper;

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
        holder.dropDownBtn.setOnClickListener(v -> {
            boolean show = toggleLayout(!expandedArrHelper[position], v);
            expandedArrHelper[position] = show;
        });
    }

    @Override
    public int getItemCount() {
        return routes.size();
    }

    public class RouteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView routeTitleTv, routePathTv;
        ImageView dropDownBtn;
        OnRouteClickListener onRouteClickListener;
        public RouteViewHolder(@NonNull View itemView, OnRouteClickListener onRouteClickListener) {
            super(itemView);
            routeTitleTv = itemView.findViewById(R.id.routeTitleTv);
            routePathTv = itemView.findViewById(R.id.routePathTv);
            dropDownBtn = itemView.findViewById(R.id.dropDownBtn);
            this.onRouteClickListener = onRouteClickListener;
//            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onRouteClickListener.onRouteClick(getAdapterPosition());
        }

        void setText(int position) {
            routeTitleTv.setText(routes.get(position).getRouteTitle());
            routePathTv.setText(getRouteString(routes.get(position)));
        }
    }

    public interface OnRouteClickListener {
        void onRouteClick(int position);
    }

    private String getRouteString(TemiRoute temiRoute) {
        if (temiRoute.getDestinations().size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < temiRoute.getDestinations().size() - 1; i++) {
            sb.append(temiRoute.getDestinations().get(i));
            sb.append(" --- ");
        }
        sb.append(temiRoute.getDestinations().get(temiRoute.getDestinations().size() - 1));
        return sb.toString();
    }

    private boolean toggleLayout(boolean isExpanded, View v) {
        toggleArrow(v, isExpanded);
        return isExpanded;
    }

    public static boolean toggleArrow(View view, boolean isExpanded) {
        if (isExpanded) {
            view.animate().setDuration(200).rotation(180);
            return true;
        } else {
            view.animate().setDuration(200).rotation(0);
            return false;
        }
    }

    public void updateHelper(List<TemiRoute> temiRoutes) {
        Log.i(TAG, "routes.size() = " + temiRoutes.size());
        expandedArrHelper = new boolean[temiRoutes.size()];
    }
}
