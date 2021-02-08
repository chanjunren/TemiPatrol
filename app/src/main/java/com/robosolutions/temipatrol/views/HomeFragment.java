package com.robosolutions.temipatrol.views;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.robosolutions.temipatrol.R;
import com.robosolutions.temipatrol.model.TemiRoute;
import com.robosolutions.temipatrol.viewmodel.GlobalViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {
    private final String TAG = "HomeFragment";
    private GlobalViewModel viewModel;
    private NavController navController;
    private Button addRouteBtn, startPatrolBtn;
    private Spinner routeSpinner;
    private ArrayAdapter spinnerAdapter;
    private HashMap<String, TemiRoute> routeMap;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);
        routeMap = new HashMap<>();
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

        routeSpinner = view.findViewById(R.id.routeSpinner);
        buildSpinner();

        startPatrolBtn = view.findViewById(R.id.startPatrolBtn);
        startPatrolBtn.setOnClickListener(v -> {
            patrol();
        });
    }

    private void buildSpinner() {
        final Observer<List<TemiRoute>> routeListObserver = new Observer<List<TemiRoute>>() {
            @Override
            public void onChanged(List<TemiRoute> temiRoutes) {
                routeMap.clear();
                for (TemiRoute route: temiRoutes) {
                    routeMap.put(route.getRouteTitle(), route);
                }
                updateRouteSpinner((ArrayList<TemiRoute>) temiRoutes);
            }
        };
        viewModel.getRouteLiveDataFromRepo().observe(getActivity(), routeListObserver);
    }

    private void updateRouteSpinner(ArrayList<TemiRoute> temiRoutes) {
        ArrayList<String> routeTitles = new ArrayList<>();
        for (TemiRoute route: temiRoutes) {
            routeTitles.add(route.getRouteTitle());
        }
        Log.i(TAG, "Route titles set: " + routeTitles);
        spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item,
                routeTitles);
        routeSpinner.setAdapter(spinnerAdapter);
    }

    private void patrol() {
        TemiRoute selectedRoute = routeMap.get(routeSpinner.getSelectedItem());
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(() -> {
            viewModel.getTemiController().patrolRoute(selectedRoute);
        });
        executorService.execute(() -> {
            navController.navigate(R.id.action_homeFragment_to_patrolFragment);
        });
    }
}