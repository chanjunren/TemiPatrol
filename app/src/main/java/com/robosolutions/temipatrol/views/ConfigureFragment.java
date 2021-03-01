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

import com.robosolutions.temipatrol.R;
import com.robosolutions.temipatrol.model.TemiConfiguration;
import com.robosolutions.temipatrol.temi.TemiSpeaker;
import com.robosolutions.temipatrol.viewmodel.GlobalViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ConfigureFragment extends Fragment implements View.OnClickListener{
    private final String TAG = "ConfigureFragment";
    private Button previewBtn1, previewBtn2, previewBtn3, previewBtn4,
        updateBtn1, updateBtn2, updateBtn3, updateBtn4, exitBtn;
    private TextView commandTv1, commandTv2, commandTv3, commandTv4;
    private EditText commandEt1, commandEt2, commandEt3, commandEt4;
    private HashMap<Integer, TextView> tvMap;
    private NavController navController;
    private GlobalViewModel viewModel;
    private TemiSpeaker temiSpeaker;
    private ArrayList<TemiConfiguration> temiConfigurations;

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
        attachSpeechCmdsLiveData();
    }

    private void attachSpeechCmdsLiveData() {
//        final Observer<List<TemiConfiguration>> voiceCmdListObserver = liveDataCmds -> {
//            Log.i(TAG, "onChanged called");
//
//            for (TemiConfiguration voiceCommand: liveDataCmds) {
//                temiConfigurations.remove(voiceCommand.getKey());
//                temiConfigurations.add(voiceCommand.getKey(), voiceCommand);
//                tvMap.get(voiceCommand.getKey()).setText(voiceCommand.getValue());
//            }
//        };
//        viewModel.getConfigurationLiveDataFromRepo().observe(getActivity(), voiceCmdListObserver);
    }

    private void findAndSetViews(View view) {
        exitBtn = view.findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(this);
        previewBtn1 = view.findViewById(R.id.playBtn1);
        previewBtn1.setOnClickListener(this);
        previewBtn2 = view.findViewById(R.id.playBtn2);
        previewBtn2.setOnClickListener(this);
        previewBtn3 = view.findViewById(R.id.playBtn3);
        previewBtn3.setOnClickListener(this);
        previewBtn4 = view.findViewById(R.id.playBtn4);
        previewBtn4.setOnClickListener(this);

        updateBtn1 = view.findViewById(R.id.updateBtn1);
        updateBtn1.setOnClickListener(this);
        updateBtn2 = view.findViewById(R.id.updateBtn2);
        updateBtn2.setOnClickListener(this);
        updateBtn3 = view.findViewById(R.id.updateBtn3);
        updateBtn3.setOnClickListener(this);
        updateBtn4 = view.findViewById(R.id.updateBtn4);
        updateBtn4.setOnClickListener(this);

        commandTv1 = view.findViewById(R.id.voiceCmdTv1);
        commandTv2 = view.findViewById(R.id.voiceCmdTv2);
        commandTv3 = view.findViewById(R.id.voiceCmdTv3);
        commandTv4 = view.findViewById(R.id.voiceCmdTv4);
        tvMap.put(0, commandTv1);
        tvMap.put(1, commandTv2);
        tvMap.put(2, commandTv3);
        tvMap.put(3, commandTv4);

        Log.i(TAG, "Map updated: " + tvMap.toString());

        commandEt1 = view.findViewById(R.id.voiceCmdEt1);
        commandEt2 = view.findViewById(R.id.voiceCmdEt2);
        commandEt3 = view.findViewById(R.id.voiceCmdEt3);
        commandEt4 = view.findViewById(R.id.voiceCmdEt4);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.exitBtn) {
            navController.navigate(R.id.action_configureFragment_to_homeFragment);
        }
        else if (v.getId() == R.id.playBtn1) {
            temiSpeaker.temiSpeak(temiConfigurations.get(0).getValue());
        } else if (v.getId() == R.id.playBtn2) {
            temiSpeaker.temiSpeak(temiConfigurations.get(1).getValue());
        } else if (v.getId() == R.id.playBtn3) {
            temiSpeaker.temiSpeak(temiConfigurations.get(2).getValue());
        } else if (v.getId() == R.id.playBtn4) {
            temiSpeaker.temiSpeak(temiConfigurations.get(3).getValue());
        } else if (v.getId() == R.id.updateBtn1) {
            String command = commandEt1.getText().toString();
            updateVoiceCmd(command, 0);
            hideKeyboard();
        } else if (v.getId() == R.id.updateBtn2) {
            String command = commandEt2.getText().toString();
            updateVoiceCmd(command, 1);
            hideKeyboard();
        } else if (v.getId() == R.id.updateBtn3) {
            String command = commandEt3.getText().toString();
            updateVoiceCmd(command, 2);
            hideKeyboard();
        } else if (v.getId() == R.id.updateBtn4) {
            String command = commandEt4.getText().toString();
            updateVoiceCmd(command, 3);
            hideKeyboard();
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