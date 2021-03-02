package com.robosolutions.temipatrol.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.robosolutions.temipatrol.R;
import com.robosolutions.temipatrol.model.TemiRoute;
import com.robosolutions.temipatrol.viewmodel.GlobalViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RouteExecutionFragment extends Fragment implements RouteAdapter.OnRouteClickListener {
    private final String TAG = "RouteExecutionFragment";
    private NavController navController;
    private RecyclerView routeRv;
    private RouteAdapter routeAdapter;
    private HashMap<String, TemiRoute> routeMap;
    private ArrayList<TemiRoute> routes;
    private GlobalViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);
        routeMap = new HashMap<>();
        routes = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.route_execution_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        routeRv = view.findViewById(R.id.routeRv);
        initializeRecylerView();
        attachLiveDataToRecyclerView();
    }

    private void initializeRecylerView() {
        Log.i(TAG, "buildRecyclerView called");
        routeAdapter =  new RouteAdapter(routes, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false);
        routeRv.setAdapter(routeAdapter);
        routeRv.setLayoutManager(mLayoutManager);
    }

    private void attachLiveDataToRecyclerView() {
        final Observer<List<TemiRoute>> routeListObserver = temiRoutes -> {
            Log.i(TAG, "onChanged called");
            routeMap.clear();
            for (TemiRoute route: temiRoutes) {
                routeMap.put(route.getRouteTitle(), route);
            }
            routes.clear();
            routes.addAll(temiRoutes);
            routeAdapter.notifyDataSetChanged();
        };
        viewModel.getRouteLiveDataFromRepo().observe(getActivity(), routeListObserver);
    }

    @Override
    public void onRouteClick(int position) {
        TemiRoute selectedRoute = routes.get(position);
        Log.i(TAG, "Selected Route: " + selectedRoute.toString());
        viewModel.setSelectedRoute(selectedRoute);
        navController.navigate(R.id.action_homeFragment_to_patrolFragment);
    }

    @Override
    public void onRouteLongClick(int position) {
        Toast.makeText(getContext(), "Hi im a long click", Toast.LENGTH_SHORT).show();
    }

}