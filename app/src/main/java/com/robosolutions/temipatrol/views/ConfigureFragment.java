package com.robosolutions.temipatrol.views;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    private TextView maskMsgTv;
    private TextView serverIpTv;
    private TextView adminPwTv;
    private HashMap<Integer, TextView> tvMap;
    // Buttons to open dialog
    private Button maskDialogBtn;
    private Button humanDistDialogBtn;
    private Button adminPwDialogBtn;
    private Button exitBtn;
    private FlatDialog maskDetectionDialog, humanDistanceDialog, serverIpDialog, adminPwDialog;

    private View createdView;


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
        this.createdView = view;
        findAndSetViews(view);
        navController = Navigation.findNavController(view);
        attachConfigurationLiveData();
    }

    private void attachConfigurationLiveData() {
        final Observer<List<TemiConfiguration>> voiceCmdListObserver = liveDataConfigurations -> {
            Log.i(TAG, "onChanged called");

            for (TemiConfiguration configuration: liveDataConfigurations) {
                temiConfigurations.remove(configuration.getKey().getValue() - 1);
                temiConfigurations.add(configuration.getKey().getValue() - 1, configuration);
                tvMap.get(configuration.getKey().getValue()).setText(configuration.getValue());
            }
        };
        viewModel.getConfigurationLiveDataFromRepo().observe(getActivity(), voiceCmdListObserver);
    }

    private void findAndSetViews(View view) {
        maskMsgTv = view.findViewById(R.id.maskDetectionMsgTv);
        TextView humanDistTv = view.findViewById(R.id.humanDistanceMsgTv);
        serverIpTv = view.findViewById(R.id.serverIpTv);
        adminPwTv = view.findViewById(R.id.adminPwTv);

        tvMap.put(1, maskMsgTv);
        tvMap.put(2, humanDistTv);
        tvMap.put(3, serverIpTv);
        tvMap.put(4, adminPwTv);

        maskDialogBtn = view.findViewById(R.id.button);
        humanDistDialogBtn = view.findViewById(R.id.button2);
        Button serverIpDialogBtn = view.findViewById(R.id.button3);
        adminPwDialogBtn = view.findViewById(R.id.button4);
        exitBtn = view.findViewById(R.id.exitBtn);

        maskDialogBtn.setOnClickListener(this);
        humanDistDialogBtn.setOnClickListener(this);
        serverIpDialogBtn.setOnClickListener(this);
        adminPwDialogBtn.setOnClickListener(this);
        exitBtn.setOnClickListener(this);

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
        } else if (v.getId() == R.id.button) {
            maskDetectionDialog.show();
        } else if (v.getId() == R.id.button2) {
            humanDistanceDialog.show();
        } else if (v.getId() == R.id.button3) {
            serverIpDialog.show();
        } else if (v.getId() == R.id.button4) {
            adminPwDialog.show();
        }
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        InputMethodManager imm =
                (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(createdView.getWindowToken(), 0);
    }

    private void updateVoiceCmd(String command, int index) {
//        if (temiConfigurations.get(index) != null) {
//            viewModel.deleteConfigurationFromRepo(temiConfigurations.get(index));
//        }
//        TemiConfiguration temiConfiguration = new TemiConfiguration(command, index);
//        viewModel.insertConfigurationIntoRepo(voiceCmd);
    }
}