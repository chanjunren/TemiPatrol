package com.robosolutions.temipatrol.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.PictureResult;
import com.robosolutions.temipatrol.R;
import com.robosolutions.temipatrol.client.JsonPostman;
import com.robosolutions.temipatrol.client.JsonRequestUtils;
import com.robosolutions.temipatrol.google.MediaHelper;
import com.robosolutions.temipatrol.model.TemiRoute;
import com.robosolutions.temipatrol.model.TemiVoiceCommand;
import com.robosolutions.temipatrol.temi.TemiNavigator;
import com.robosolutions.temipatrol.temi.TemiSpeaker;
import com.robosolutions.temipatrol.viewmodel.GlobalViewModel;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.robosolutions.temipatrol.google.MediaHelper.CLUSTER_DETECTED;
import static com.robosolutions.temipatrol.google.MediaHelper.NOT_WEARING_MASK_DETECTED;


// Page shown when Temi is patrolling
public class PatrolFragment extends Fragment implements Robot.TtsListener {
    private static final String TAG = "PatrolFragment";

    private class CameraTask extends TimerTask {
        @Override
        public void run() {
            camera.takePictureSnapshot();
        }
    }

    private GlobalViewModel viewModel;
    private CameraView camera;
    private ExecutorService globalExecutorService;
    private ExecutorService postmanExecutorService;
    private NavController navController;
    private JsonPostman jsonPostman;
    private TemiNavigator temiNavigator;
    private TemiSpeaker temiSpeaker;
    private MediaHelper mediaHelper;
    private ArrayList<TemiVoiceCommand> temiVoiceCommands;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);
        globalExecutorService = viewModel.getExecutorService();
        postmanExecutorService = Executors.newFixedThreadPool(20);
        jsonPostman = new JsonPostman();
        temiNavigator = new TemiNavigator(viewModel.getTemiRobot(), this);
        temiSpeaker = new TemiSpeaker(viewModel.getTemiRobot());
        mediaHelper = new MediaHelper(getContext(), viewModel);
        temiVoiceCommands = new ArrayList<>();
        viewModel.getTemiRobot().addTtsListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.patrol_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        camera = view.findViewById(R.id.camera);
        camera.setLifecycleOwner(getViewLifecycleOwner());
        initializeVoiceCmds();
        configureCamera(camera);
        startCamera();
        startPatrol();
    }


    private void startCamera() {
        Timer timer = new Timer();
        timer.schedule(new CameraTask(), 0, 1000);
    }

    private void configureCamera(CameraView camera) {
        camera.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(@NonNull PictureResult result) {
                super.onPictureTaken(result);
                byte[] image = result.getData();
                JSONObject maskReqMsg = JsonRequestUtils.generateJsonMessageForMaskDetection(image);
                boolean personNotWearingMask = sendImageToServerAndGetMaskDetectionResult(maskReqMsg);
                JSONObject clusterReqMsg = JsonRequestUtils.generateJsonMessageForHumanDistance(image);
                boolean clusterDetected = sendImageToServerAndGetClusterDetectionResult(clusterReqMsg);

                if (personNotWearingMask) {
                    mediaHelper.uploadImage(image, NOT_WEARING_MASK_DETECTED);
                    pauseAndMakeMaskAnnouncement();
                }
                if (clusterDetected) {
                    mediaHelper.uploadImage(image, CLUSTER_DETECTED);
                    pauseAndMakeClusterAnnouncement();
                }
            }
        });
    }

    private void pauseAndMakeMaskAnnouncement() {
        temiNavigator.pausePatrol();
        TemiVoiceCommand voiceCmd = temiVoiceCommands.get(0);
        temiSpeaker.temiSpeak(voiceCmd.getCommand());
    }

    private void pauseAndMakeClusterAnnouncement() {
        temiNavigator.pausePatrol();
        TemiVoiceCommand voiceCmd = temiVoiceCommands.get(1);
        temiSpeaker.temiSpeak(voiceCmd.getCommand());
    }

    private void startPatrol() {
        globalExecutorService.execute(() -> {
            TemiRoute selectedRoute = viewModel.getSelectedRoute();
            temiNavigator.patrolRoute(selectedRoute);
        });
    }

    private void initializeVoiceCmds() {
        final Observer<List<TemiVoiceCommand>> voiceCmdListener = voiceCmds -> {
            temiVoiceCommands.clear();
            for (TemiVoiceCommand voiceCmd: voiceCmds) {
                this.temiVoiceCommands.add(voiceCmd);
            }
            Log.i(TAG, "Voice Cmds: " + temiVoiceCommands.toString());
        };

        viewModel.getCommandLiveDataFromRepo().observe(getViewLifecycleOwner(), voiceCmdListener);
    }



    private Boolean sendImageToServerAndGetMaskDetectionResult(JSONObject requestJson) {
        // for testing
        Future<Boolean> result = postmanExecutorService.submit(() ->
                jsonPostman.postMaskDetectionRequestAndGetResult(requestJson));
        try {
            return result.get();
        } catch (ExecutionException | InterruptedException e) {
            Log.e(TAG, "sendImageToServerAndGetMaskDetectionResult error: " + e.toString());
            return null;
        }
    }

    private Boolean sendImageToServerAndGetClusterDetectionResult(JSONObject requestJson) {
        // for testing
        Future<Boolean> result = postmanExecutorService.submit(() ->
                jsonPostman.postHumanDistanceRequestAndGetResult(requestJson));
        try {
            return result.get();
        } catch (ExecutionException | InterruptedException e) {
            Log.e(TAG, "sendImageToServerAndGetClusterDetectionResult error: " + e.toString());
            return null;
        }
    }

    @Override
    public void onTtsStatusChanged(@NotNull TtsRequest ttsRequest) {
        if (ttsRequest.getStatus() == TtsRequest.Status.COMPLETED) {
            Log.i(TAG, "SPEECH COMPLETED");
            temiNavigator.resumePatrol();
        }
    }

    public void navigateToHomePage() {
        navController.navigate(R.id.action_patrolFragment_to_homeFragment);
    }
}