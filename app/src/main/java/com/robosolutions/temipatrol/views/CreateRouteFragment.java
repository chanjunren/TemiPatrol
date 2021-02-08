package com.robosolutions.temipatrol.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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
    private RouteAdapter routeAdapter;
    private Button saveRouteBtn;
    private TemiController temiController;
    private GlobalViewModel viewModel;
    private ArrayList<String> locations;

    private Spinner locationSpinner;
    private ArrayAdapter<String> spinnerAdapter;


    public CreateRouteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(GlobalViewModel.class);
        temiController = viewModel.getTemiController();
        routeAdapter =  new RouteAdapter();

        locations = new ArrayList<>();
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

        saveRouteBtn = view.findViewById(R.id.saveRouteBtn);

        locationSpinner = view.findViewById(R.id.locationSpinner);
        spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item,
                temiController.getLocationsFromTemi());
        locationSpinner.setAdapter(spinnerAdapter);

    }
}