package com.robosolutions.temipatrol.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.flatdialoglibrary.dialog.FlatDialog;
import com.robosolutions.temipatrol.R;
import com.robosolutions.temipatrol.model.ConfigurationEnum;
import com.robosolutions.temipatrol.model.TemiConfiguration;
import com.robosolutions.temipatrol.temi.TemiSpeaker;
import com.robosolutions.temipatrol.viewmodel.GlobalViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ConfigureFragment extends Fragment implements View.OnClickListener{
    private final String TAG = "ConfigureFragment";
    private NavController navController;
    private GlobalViewModel viewModel;
    private TemiSpeaker temiSpeaker;
    private ArrayList<TemiConfiguration> temiConfigurations;
    private HashMap<Integer, TextView> tvMap;
    private FlatDialog maskDetectionDialog, humanDistanceDialog, serverIpDialog, adminPwDialog;


    public ConfigureFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);
        temiSpeaker = new TemiSpeaker(viewModel.getTemiRobot());
        TemiConfiguration[] tempArr = {null, null, null, null};
        temiConfigurations = new ArrayList<>(Arrays.asList(tempArr));
        tvMap = new HashMap<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.configure_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findAndSetViews(view);
        navController = Navigation.findNavController(view);
        attachConfigurationLiveData();
    }

    private void attachConfigurationLiveData() {
        final Observer<List<TemiConfiguration>> configListObserver = liveDataConfigurations -> {
            Log.i(TAG, "onChanged called");

            for (TemiConfiguration configuration: liveDataConfigurations) {
                temiConfigurations.remove(configuration.getKey().getValue() - 1);
                temiConfigurations.add(configuration.getKey().getValue() - 1, configuration);
                tvMap.get(configuration.getKey().getValue()).setText(configuration.getValue());
            }
        };
        viewModel.getConfigurationLiveDataFromRepo().observe(getActivity(), configListObserver);
    }

    private void findAndSetViews(View view) {
        TextView maskMsgTv = view.findViewById(R.id.maskDetectionMsgTv);
        TextView humanDistTv = view.findViewById(R.id.humanDistanceMsgTv);
        TextView serverIpTv = view.findViewById(R.id.serverIpTv);
        TextView adminPwTv = view.findViewById(R.id.adminPwTv);

        maskMsgTv.setMovementMethod(new ScrollingMovementMethod());
        humanDistTv.setMovementMethod(new ScrollingMovementMethod());
        serverIpTv.setMovementMethod(new ScrollingMovementMethod());
        adminPwTv.setMovementMethod(new ScrollingMovementMethod());

        tvMap.put(1, maskMsgTv);
        tvMap.put(2, humanDistTv);
        tvMap.put(3, serverIpTv);
        tvMap.put(4, adminPwTv);

        // Buttons to open dialog
        ImageView maskDialogBtn = view.findViewById(R.id.updateMaskMsgBtn);
        ImageView humanDistDialogBtn = view.findViewById(R.id.updateClusterMsgBtn);
        ImageView serverIpDialogBtn = view.findViewById(R.id.updateServerBtn);
        ImageView adminPwDialogBtn = view.findViewById(R.id.updatePwBtn);
        ConstraintLayout exitBtn = view.findViewById(R.id.exitBtn);

        maskDialogBtn.setOnClickListener(this);
        humanDistDialogBtn.setOnClickListener(this);
        serverIpDialogBtn.setOnClickListener(this);
        adminPwDialogBtn.setOnClickListener(this);
        exitBtn.setOnClickListener(this);

        ImageView testMaskMsgBtn = view.findViewById(R.id.playMaskMsgBtn);
        ImageView testClusterMsgBtn = view.findViewById(R.id.playClusterMsgBtn);
        ImageView testConnectionBtn = view.findViewById(R.id.testConnectionBtn);

        testMaskMsgBtn.setOnClickListener(this);
        testClusterMsgBtn.setOnClickListener(this);
        testConnectionBtn.setOnClickListener(this);

        maskDetectionDialog = buildMaskDetectionDialog();
        humanDistanceDialog = buildHumanDistanceDialog();
        serverIpDialog = buildServerIpDialog();
        adminPwDialog = buildAdminPwDialog();
    }

    private FlatDialog buildMaskDetectionDialog() {
        FlatDialog flatDialog = new FlatDialog(getContext());
        flatDialog
                .setSubtitle("Message broadcasted when a person not wearing mask is detected")
                .setFirstTextFieldHint("Message")
                .setFirstButtonText("Update")
                .setSecondButtonText("Cancel")
                .withFirstButtonListner(view -> {
                    String newCommand = flatDialog.getFirstTextField();
                    TemiConfiguration newConfiguration = new TemiConfiguration(newCommand,
                            ConfigurationEnum.MASK_DETECTION_MSG);
                    viewModel.deleteConfigurationFromRepo(temiConfigurations.get(0));
                    viewModel.insertConfigurationIntoRepo(newConfiguration);
                    flatDialog.dismiss();
                })
                .withSecondButtonListner(view -> {
                    flatDialog.dismiss();
                })
                .setBackgroundColor(R.color.white)
                .setFirstButtonColor(R.color.temi_teal)
                .setSecondButtonColor(R.color.slider_color)
                .setIcon(R.drawable.ic_configure_icon);
        return flatDialog;
    }

    private FlatDialog buildHumanDistanceDialog() {
        FlatDialog flatDialog = new FlatDialog(getContext());
        flatDialog
                .setSubtitle("Message broadcasted when a group of more than 8 people is detected")
                .setFirstTextFieldHint("Message")
                .setFirstButtonText("Update")
                .setSecondButtonText("Cancel")
                .withFirstButtonListner(view -> {
                    String newCommand = flatDialog.getFirstTextField();
                    TemiConfiguration newConfiguration = new TemiConfiguration(newCommand,
                            ConfigurationEnum.HUMAN_DIST_MSG);
                    viewModel.deleteConfigurationFromRepo(temiConfigurations.get(1));
                    viewModel.insertConfigurationIntoRepo(newConfiguration);
                    flatDialog.dismiss();
                })
                .withSecondButtonListner(view -> flatDialog.dismiss())
                .setBackgroundColor(R.color.white)
                .setFirstButtonColor(R.color.temi_teal)
                .setSecondButtonColor(R.color.slider_color)
                .setIcon(R.drawable.ic_configure_icon);
        return flatDialog;
    }


    private FlatDialog buildServerIpDialog() {
        FlatDialog flatDialog = new FlatDialog(getContext());
        flatDialog
                .setSubtitle("Address of where the JSON Requests are sent")
                .setFirstTextFieldHint("IP Address")
                .setFirstButtonText("Update")
                .setSecondButtonText("Cancel")
                .withFirstButtonListner(view -> {
                    String newIpAdd = flatDialog.getFirstTextField();
                    TemiConfiguration newConfiguration = new TemiConfiguration(newIpAdd,
                            ConfigurationEnum.SERVER_IP_ADD);
                    viewModel.deleteConfigurationFromRepo(temiConfigurations.get(2));
                    viewModel.insertConfigurationIntoRepo(newConfiguration);
                    flatDialog.dismiss();
                })
                .withSecondButtonListner(view -> {
                    flatDialog.dismiss();
                })
                .setBackgroundColor(R.color.white)
                .setFirstButtonColor(R.color.temi_teal)
                .setSecondButtonColor(R.color.slider_color)
                .setIcon(R.drawable.ic_configure_icon);
        return flatDialog;
    }


    private FlatDialog buildAdminPwDialog() {
        FlatDialog flatDialog = new FlatDialog(getContext());
        flatDialog
                .setSubtitle("Used to change any configurations")
                .setFirstTextFieldHint("New Password")
                .setFirstButtonText("Update")
                .setSecondButtonText("Cancel")
                .withFirstButtonListner(view -> {
                    String newPassword = flatDialog.getFirstTextField();
                    TemiConfiguration newConfiguration = new TemiConfiguration(newPassword,
                            ConfigurationEnum.ADMIN_PW);
                    viewModel.deleteConfigurationFromRepo(temiConfigurations.get(3));
                    viewModel.insertConfigurationIntoRepo(newConfiguration);
                    flatDialog.dismiss();
                })
                .withSecondButtonListner(view -> flatDialog.dismiss())
                .setBackgroundColor(R.color.white)
                .setFirstButtonColor(R.color.temi_teal)
                .setSecondButtonColor(R.color.slider_color)
                .setIcon(R.drawable.ic_configure_icon);
        return flatDialog;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.exitBtn) {
            navController.navigate(R.id.action_configureFragment_to_homeFragment);
        } else if (v.getId() == R.id.updateMaskMsgBtn) {
            maskDetectionDialog.show();
        } else if (v.getId() == R.id.updateClusterMsgBtn) {
            humanDistanceDialog.show();
        } else if (v.getId() == R.id.updateServerBtn) {
            serverIpDialog.show();
        } else if (v.getId() == R.id.updatePwBtn) {
            adminPwDialog.show();
        } else if (v.getId() == R.id.playMaskMsgBtn) {
            String maskCmd = temiConfigurations.get(0).getValue();
            temiSpeaker.temiSpeak(maskCmd);
        } else if (v.getId() == R.id.playClusterMsgBtn) {
            String clusterCmd = temiConfigurations.get(1).getValue();
            temiSpeaker.temiSpeak(clusterCmd);
        } else if (v.getId() == R.id.testConnectionBtn) {

        }
    }
}