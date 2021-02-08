package com.robosolutions.temipatrol.views;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.robosolutions.temipatrol.R;
import com.robosolutions.temipatrol.viewmodel.GlobalViewModel;

public class HomeFragment extends Fragment {

    private GlobalViewModel viewModel;
    private NavController navController;
    private Button addRouteBtn;

    public HomeFragment () {
        // Err required? not sure
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(GlobalViewModel.class);
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
    }
}