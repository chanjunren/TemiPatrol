package com.ongbengchia.temipatrol.views;

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

import com.ongbengchia.temipatrol.R;
import com.ongbengchia.temipatrol.viewmodel.GlobalViewModel;

public class HomeFragment extends Fragment {

    private GlobalViewModel mViewModel;
    private NavController navController;
    private Button addRouteBtn;

    public HomeFragment () {
        // Err required? not sure
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(GlobalViewModel.class);
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        addRouteBtn = view.findViewById(R.id.addRouteBtn);

        addRouteBtn.setOnClickListener(v -> {
            navController.navigate(R.id.action_homeFragment_to_createRouteFragment);
        });
    }
}