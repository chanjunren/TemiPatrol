package com.robosolutions.temipatrol.views;

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

import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.PictureResult;
import com.robosolutions.temipatrol.R;
import com.robosolutions.temipatrol.client.JsonPostman;
import com.robosolutions.temipatrol.client.JsonRequestUtils;
import com.robosolutions.temipatrol.google.MediaHelper;
import com.robosolutions.temipatrol.model.ConfigurationEnum;
import com.robosolutions.temipatrol.model.TemiConfiguration;
import com.robosolutions.temipatrol.model.TemiRoute;
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

    private boolean isStationaryPatrol;

    private ArrayList<TemiConfiguration> temiConfigurations;
    private String maskDetectionCmd;
    private String humanDistanceCmd;
    private String serverIp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);
        globalExecutorService = viewModel.getExecutorService();
        postmanExecutorService = Executors.newFixedThreadPool(20);
        temiNavigator = new TemiNavigator(viewModel.getTemiRobot(), this);
        temiSpeaker = new TemiSpeaker(viewModel.getTemiRobot());
        mediaHelper = new MediaHelper(getContext(), viewModel);
        temiConfigurations = new ArrayList<>();
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

        initConfigurations();

        configureCamera(camera);
        startCamera();

        // No patrol for stationary patrol
        if (viewModel.getSelectedRoute().getDestinations().size() != 0) {
            isStationaryPatrol = false;
            startPatrol();
        } else {
            isStationaryPatrol = true;
        }
    }


    private void startCamera() {
        Timer timer = new Timer();
        timer.schedule(new CameraTask(), 0, 5000);
    }

    private void configureCamera(CameraView camera) {
        camera.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(@NonNull PictureResult result) {
                super.onPictureTaken(result);
                byte[] image = result.getData();
//                JSONObject maskReqMsg = JsonRequestUtils.generateJsonMessageForMaskDetection(image);
//                boolean isWearingMask = sendImageToServerAndGetMaskDetectionResult(maskReqMsg);
//                Log.i(TAG, "Wearing mask value: " + isWearingMask);
                JSONObject clusterReqMsg = JsonRequestUtils.generateJsonMessageForHumanDistance(image);
                boolean clusterDetected = sendImageToServerAndGetClusterDetectionResult(clusterReqMsg);
                Log.i(TAG, "Cluster detected value: " + clusterDetected);
//                if (!isWearingMask) {
//                    mediaHelper.uploadImage(image, NOT_WEARING_MASK_DETECTED);
//                    pauseAndMakeMaskAnnouncement();
//                }
                if (clusterDetected) {
                    mediaHelper.uploadImage(image, CLUSTER_DETECTED);
                    pauseAndMakeClusterAnnouncement();
                }
            }
        });
    }

    private void pauseAndMakeMaskAnnouncement() {
        temiNavigator.pausePatrol();
        TemiConfiguration voiceCmd = temiConfigurations.get(0);
        temiSpeaker.temiSpeak(voiceCmd.getValue());
    }

    private void pauseAndMakeClusterAnnouncement() {
        temiNavigator.pausePatrol();
        TemiConfiguration voiceCmd = temiConfigurations.get(1);
        temiSpeaker.temiSpeak(voiceCmd.getValue());
    }

    private void startPatrol() {
        globalExecutorService.execute(() -> {
            TemiRoute selectedRoute = viewModel.getSelectedRoute();
            temiNavigator.patrolRoute(selectedRoute);
        });
    }

    private void initConfigurations() {
        final Observer<List<TemiConfiguration>> voiceCmdListener = configs -> {
            temiConfigurations.clear();
            for (TemiConfiguration config: configs) {
                if (config.getKey() == ConfigurationEnum.MASK_DETECTION_MSG) {
                    maskDetectionCmd = config.getValue();
                }
                else if (config.getKey() == ConfigurationEnum.HUMAN_DIST_MSG) {
                    humanDistanceCmd = config.getValue();
                } else if (config.getKey() == ConfigurationEnum.SERVER_IP_ADD) {
                    serverIp = config.getValue();
                    jsonPostman = new JsonPostman(serverIp);
                }
            }
            Log.i(TAG, "Mask msg: " + maskDetectionCmd);
            Log.i(TAG, "Human Distance msg: " + humanDistanceCmd);
            Log.i(TAG, "Server IP: " + serverIp);
        };

        viewModel.getConfigurationLiveDataFromRepo().observe(getViewLifecycleOwner(), voiceCmdListener);
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

            if (!isStationaryPatrol) {
                temiNavigator.resumePatrol();
            }
        }
    }

    public void navigateToHomePage() {
        navController.navigate(R.id.action_patrolFragment_to_homeFragment);
    }
}