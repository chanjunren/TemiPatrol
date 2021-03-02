package com.robosolutions.temipatrol.views;

import androidx.cardview.widget.CardView;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.robosolutions.temipatrol.R;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private final String TAG = "HomeFragment";

    private NavController navController;

    private CardView addRouteBtn, patrolPageBtn, configureBtn;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        addRouteBtn = view.findViewById(R.id.addRouteBtn);
        patrolPageBtn = view.findViewById(R.id.patrolPageBtn);
        configureBtn = view.findViewById(R.id.configurePageBtn);

        addRouteBtn.setOnClickListener(this);
        patrolPageBtn.setOnClickListener(this);
        configureBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.addRouteBtn) {
            navController.navigate(R.id.action_homeFragment_to_createRouteFragment);
        } else if (v.getId() == R.id.patrolPageBtn) {
            navController.navigate(R.id.action_homeFragment_to_routeExecutionFragment);
        } else if (v.getId() == R.id.configurePageBtn) {
            navController.navigate(R.id.action_homeFragment_to_configureFragment);
        }
    }
}