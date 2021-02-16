package com.robosolutions.temipatrol.views;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.robosolutions.temipatrol.R;
import com.robosolutions.temipatrol.model.TemiRoute;
import com.robosolutions.temipatrol.viewmodel.GlobalViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class HomeFragment extends Fragment implements RouteAdapter.OnRouteClickListener {
    private final String TAG = "HomeFragment";
    private GlobalViewModel viewModel;
    private NavController navController;
    private Button addRouteBtn, configureBtn;
    private RecyclerView routeRv;
    private RouteAdapter routeAdapter;
    private HashMap<String, TemiRoute> routeMap;
    private ArrayList<TemiRoute> routes;

    private ExecutorService executorService;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);
        routeMap = new HashMap<>();
        routes = new ArrayList<>();
        executorService = viewModel.getExecutorService();

        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        addRouteBtn = view.findViewById(R.id.saveRouteBtn);
        addRouteBtn.setOnClickListener(v -> {
            navController.navigate(R.id.action_homeFragment_to_createRouteFragment);
        });

        configureBtn = view.findViewById(R.id.configureBtn);
        configureBtn.setOnClickListener(v -> {
            navController.navigate(R.id.action_homeFragment_to_configureFragment);
        });

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
        final Observer<List<TemiRoute>> routeListObserver = new Observer<List<TemiRoute>>() {
            @Override
            public void onChanged(List<TemiRoute> temiRoutes) {
                Log.i(TAG, "onChanged called");
                routeMap.clear();
                for (TemiRoute route: temiRoutes) {
                    routeMap.put(route.getRouteTitle(), route);
                }
                routes.clear();
                routes.addAll(temiRoutes);
                routeAdapter.notifyDataSetChanged();
            }
        };
        viewModel.getRouteLiveDataFromRepo().observe(getActivity(), routeListObserver);
    }

    @Override
    public void onRouteClick(int position) {
        TemiRoute selectedRoute = routes.get(position);

//        executorService.execute(() -> {
//            viewModel.getTemiController().patrolRoute(selectedRoute);
//        });
        executorService.execute(() -> {
            navController.navigate(R.id.action_homeFragment_to_patrolFragment);
        });

        Toast.makeText(getContext(), "Patrolling: " + selectedRoute.getRouteTitle(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRouteLongClick(int position) {
        Toast.makeText(getContext(), "Hi im a long click", Toast.LENGTH_SHORT).show();
    }
}