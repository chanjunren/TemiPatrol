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
    private NavController navController;
    private GlobalViewModel viewModel;
    private TemiSpeaker temiSpeaker;
    private ArrayList<TemiConfiguration> temiConfigurations;
    private TextView maskMsgTv, humanDistTv, serverIpTv, adminPwTv;
    private HashMap<Integer, TextView> tvMap;

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
        humanDistTv = view.findViewById(R.id.humanDistanceMsgTv);
        serverIpTv = view.findViewById(R.id.serverIpTv);
        adminPwTv = view.findViewById(R.id.adminPwTv);

        tvMap.put(1, maskMsgTv);
        tvMap.put(2, humanDistTv);
        tvMap.put(3, serverIpTv);
        tvMap.put(4, adminPwTv);
    }

    @Override
    public void onClick(View v) {
//        if (v.getId() == R.id.exitBtn) {
//            navController.navigate(R.id.action_configureFragment_to_homeFragment);
//        }
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