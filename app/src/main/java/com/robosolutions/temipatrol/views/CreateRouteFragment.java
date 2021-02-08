package com.robosolutions.temipatrol.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.robosolutions.temipatrol.R;
import com.robosolutions.temipatrol.temi.TemiController;
import com.robosolutions.temipatrol.viewmodel.GlobalViewModel;

import java.util.ArrayList;


public class CreateRouteFragment extends Fragment {
    private final String TAG = "CreateRouteFragment";
    private RouteAdapter routeAdapter;
    private Button addLocationBtn, saveRouteBtn;
    private RecyclerView routeRv;
    private TemiController temiController;
    private GlobalViewModel viewModel;
    private ArrayList<String> route;

    private Spinner locationSpinner;
    private ArrayAdapter<String> spinnerAdapter;


    public CreateRouteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);
        temiController = viewModel.getTemiController();
        route = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.create_route_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        addLocationBtn = view.findViewById(R.id.addLocationBtn);
        addLocationBtn.setOnClickListener(v -> {
            addDestination();
        });

        saveRouteBtn = view.findViewById(R.id.saveRouteBtn);
        saveRouteBtn.setOnClickListener(v -> {
            saveCurrentRoute();
        });

        routeRv = view.findViewById(R.id.routeRv);
        buildRecyclerView();

        locationSpinner = view.findViewById(R.id.locationSpinner);
        spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item,
                temiController.getLocationsFromTemi());
        locationSpinner.setAdapter(spinnerAdapter);
    }

    private void buildRecyclerView() {
        routeAdapter =  new RouteAdapter(route);
        routeRv.setAdapter(routeAdapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        routeRv.setLayoutManager(mLayoutManager);
        routeRv.setAdapter(routeAdapter);
    }

    private void addDestination() {
        route.add(locationSpinner.getSelectedItem().toString());
        Log.i(TAG, "route: " + route.toString());
        routeAdapter.notifyItemInserted(route.size());
    }

    private void saveCurrentRoute() {
        Log.i(TAG, route.toString());
    }
}