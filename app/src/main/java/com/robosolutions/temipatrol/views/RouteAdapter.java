package com.robosolutions.temipatrol.views;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.robosolutions.temipatrol.R;
import com.robosolutions.temipatrol.model.TemiRoute;

import java.util.ArrayList;
import java.util.List;

import static com.robosolutions.temipatrol.views.CustomAnimation.collapse;
import static com.robosolutions.temipatrol.views.CustomAnimation.expand;
import static com.robosolutions.temipatrol.views.CustomAnimation.toggleArrow;

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
            boolean show = toggleLayout(!expandedArrHelper[position], v, holder.expandLayout);
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
        CardView deleteRouteBtn, editRouteBtn, executeRouteBtn;
        ConstraintLayout expandLayout;
        OnRouteClickListener onRouteClickListener;
        public RouteViewHolder(@NonNull View itemView, OnRouteClickListener onRouteClickListener) {
            super(itemView);
            routeTitleTv = itemView.findViewById(R.id.routeTitleTv);
            routePathTv = itemView.findViewById(R.id.routePathTv);
            dropDownBtn = itemView.findViewById(R.id.dropDownBtn);
            expandLayout = itemView.findViewById(R.id.expandLayout);
            deleteRouteBtn = itemView.findViewById(R.id.deleteRouteBtn);
            editRouteBtn = itemView.findViewById(R.id.editRouteBtn);
            executeRouteBtn = itemView.findViewById(R.id.execRouteBtn);
            this.onRouteClickListener = onRouteClickListener;

            deleteRouteBtn.setOnClickListener(this);
            editRouteBtn.setOnClickListener(this);
            executeRouteBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.deleteRouteBtn) {
                onRouteClickListener.onRouteDeleteClick(getAdapterPosition());
            } else if (v.getId() == R.id.editRouteBtn) {
                onRouteClickListener.onRouteEditClick(getAdapterPosition());
            } else if (v.getId() == R.id.execRouteBtn) {
                onRouteClickListener.onRouteExecuteClick(getAdapterPosition());
            }
        }

        void setText(int position) {
            routeTitleTv.setText(routes.get(position).getRouteTitle());
            routePathTv.setText(getRouteString(routes.get(position)));
        }
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

    private boolean toggleLayout(boolean isExpanded, View v, ConstraintLayout layoutExpand) {
        toggleArrow(v, isExpanded);
        if (isExpanded) {
            expand(layoutExpand);
        } else {
            collapse(layoutExpand);
        }
        return isExpanded;

    }

    public void updateHelper(List<TemiRoute> temiRoutes) {
        Log.i(TAG, "routes.size() = " + temiRoutes.size());
        expandedArrHelper = new boolean[temiRoutes.size()];
    }

    public interface OnRouteClickListener {
        void onRouteExecuteClick(int position);
        void onRouteDeleteClick(int position);
        void onRouteEditClick(int position);
    }
}
