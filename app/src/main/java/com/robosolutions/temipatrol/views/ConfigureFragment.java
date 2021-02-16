package com.robosolutions.temipatrol.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.robosolutions.temipatrol.R;
import com.robosolutions.temipatrol.model.TemiVoiceCommand;
import com.robosolutions.temipatrol.viewmodel.GlobalViewModel;

public class ConfigureFragment extends Fragment implements View.OnClickListener{
    private Button previewBtn1, previewBtn2, previewBtn3, previewBtn4,
        updateBtn1, updateBtn2, updateBtn3, updateBtn4, exitBtn;
    private TextView commandTv1, commandTv2, commandTv3, commandTv4;
    private EditText commandEt1, commandEt2, commandEt3, commandEt4;
    private NavController navController;
    private GlobalViewModel viewModel;


    public ConfigureFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);
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

        } else if (v.getId() == R.id.playBtn2) {

        } else if (v.getId() == R.id.playBtn3) {

        } else if (v.getId() == R.id.playBtn4) {

        } else if (v.getId() == R.id.updateBtn1) {
            String command = commandEt1.getText().toString();
            TemiVoiceCommand voiceCmd = new TemiVoiceCommand(command, 1);
            viewModel.insertVoiceCmdIntoRepo(voiceCmd);
        } else if (v.getId() == R.id.updateBtn2) {
            String command = commandEt1.getText().toString();
            TemiVoiceCommand voiceCmd = new TemiVoiceCommand(command, 2);
            viewModel.insertVoiceCmdIntoRepo(voiceCmd);
        } else if (v.getId() == R.id.updateBtn3) {
            String command = commandEt1.getText().toString();
            TemiVoiceCommand voiceCmd = new TemiVoiceCommand(command, 3);
            viewModel.insertVoiceCmdIntoRepo(voiceCmd);
        } else if (v.getId() == R.id.updateBtn4) {
            String command = commandEt1.getText().toString();
            TemiVoiceCommand voiceCmd = new TemiVoiceCommand(command, 4);
            viewModel.insertVoiceCmdIntoRepo(voiceCmd);
        }
    }
}