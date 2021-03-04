package com.robosolutions.temipatrol.views;

import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.flatdialoglibrary.dialog.FlatDialog;
import com.robosolutions.temipatrol.R;
import com.robosolutions.temipatrol.model.ConfigurationEnum;
import com.robosolutions.temipatrol.model.TemiConfiguration;
import com.robosolutions.temipatrol.viewmodel.GlobalViewModel;

import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private final String TAG = "HomeFragment";

    private NavController navController;
    private GlobalViewModel viewModel;
    private String password;
    private FlatDialog passwordDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);
        updatePassword();

        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        Animation topAnim = AnimationUtils.loadAnimation(getContext(), R.anim.top_animation);
        CardView pageLogo = view.findViewById(R.id.homePageLogo);
        pageLogo.setAnimation(topAnim);

        CardView addRouteBtn = view.findViewById(R.id.addRouteBtn);
        CardView patrolPageBtn = view.findViewById(R.id.patrolPageBtn);
        CardView configureBtn = view.findViewById(R.id.configurePageBtn);

        addRouteBtn.setOnClickListener(this);
        patrolPageBtn.setOnClickListener(this);
        configureBtn.setOnClickListener(this);

        passwordDialog = buildPasswordDialog();
    }

    private void updatePassword() {
        final Observer<List<TemiConfiguration>> configListObserver = liveDataConfigurations -> {
            for (TemiConfiguration configuration: liveDataConfigurations) {
                if (configuration.getKey() == ConfigurationEnum.ADMIN_PW) {
                    this.password = configuration.getValue();
                    Log.i(TAG, "Password set to: " + password);
                }
            }
        };
        viewModel.getConfigurationLiveDataFromRepo().observe(getActivity(), configListObserver);
    }

    @SuppressLint("ResourceType")
    private FlatDialog buildPasswordDialog() {
        Resources resources = getResources();
        FlatDialog flatDialog = new FlatDialog(getContext());
        flatDialog
                .setSubtitle("Please enter the password")
                .setFirstTextFieldHint("Password")
                .setFirstTextFieldInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD)
                .setFirstButtonText("Enter")
                .setSecondButtonText("Cancel")
                .withFirstButtonListner(view -> {
                    if (flatDialog.getFirstTextField().equals(password)) {
                        navController.navigate(R.id.action_homeFragment_to_configureFragment);
                    } else {
                        Toast.makeText(getContext(), "Incorrect password!", Toast.LENGTH_SHORT).show();
                    }
                    flatDialog.dismiss();
                })
                .withSecondButtonListner(view -> flatDialog.dismiss())
                .setBackgroundColor(Color.parseColor(resources.getString(R.color.white)))
                .setFirstButtonColor(Color.parseColor(resources.getString(R.color.temi_teal)))
                .setSecondButtonColor(Color.parseColor(resources.getString(R.color.slider_color)))
                .setFirstTextFieldTextColor(Color.parseColor(resources.getString(R.color.black)))
                .setFirstTextFieldHintColor(Color.parseColor(resources.getString(R.color.gray)))
                .setSubtitleColor(Color.parseColor(resources.getString(R.color.black)))
                .setIcon(R.drawable.ic_configure_icon);
        return flatDialog;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.addRouteBtn) {
            GlobalViewModel viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);
            viewModel.initializeForRouteCreation();
            navController.navigate(R.id.action_homeFragment_to_createRouteFragment);
        } else if (v.getId() == R.id.patrolPageBtn) {
            navController.navigate(R.id.action_homeFragment_to_routeExecutionFragment);
        } else if (v.getId() == R.id.configurePageBtn) {
            passwordDialog.show();
        }
    }
}