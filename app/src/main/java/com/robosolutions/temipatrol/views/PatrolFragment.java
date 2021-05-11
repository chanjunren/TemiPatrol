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
import android.widget.ImageView;
import android.widget.Toast;

import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.PictureResult;
import com.robosolutions.temipatrol.R;
import com.robosolutions.temipatrol.client.JsonPostman;
import com.robosolutions.temipatrol.client.JsonRequestHelper;
import com.robosolutions.temipatrol.google.MediaHelper;
import com.robosolutions.temipatrol.model.ConfigurationEnum;
import com.robosolutions.temipatrol.model.TemiConfiguration;
import com.robosolutions.temipatrol.model.TemiRoute;
import com.robosolutions.temipatrol.temi.TemiPatroller;
import com.robosolutions.temipatrol.temi.TemiSpeaker;
import com.robosolutions.temipatrol.viewmodel.GlobalViewModel;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;
import com.tomer.fadingtextview.FadingTextView;

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
import java.util.concurrent.TimeUnit;

import static com.robosolutions.temipatrol.google.MediaHelper.CLUSTER_DETECTED;
import static com.robosolutions.temipatrol.google.MediaHelper.NOT_WEARING_MASK_DETECTED;
import static com.robosolutions.temipatrol.google.MediaHelper.NO_MASK_AND_CLUSTER;


// Page shown when Temi is patrolling
public class PatrolFragment extends Fragment {
    private static final String TAG = "PatrolFragment";
    private static final int NUMBER_OF_CLICKS_TO_STOP = 6;
    private static final int CAMERA_INTERVAL = 7000;

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
    private TemiPatroller temiPatroller;
    private TemiSpeaker temiSpeaker;
    private MediaHelper mediaHelper;
    private FadingTextView fadingTv;

    private boolean isStationaryPatrol;

    private ArrayList<TemiConfiguration> temiConfigurations;
    private String maskDetectionCmd, humanDistanceCmd, combinedCmd, serverIp;

    private int clickCounter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Patrol Fragment created");
        viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);
        globalExecutorService = viewModel.getExecutorService();
        postmanExecutorService = Executors.newFixedThreadPool(20);
        temiSpeaker = new TemiSpeaker(viewModel.getTemiRobot());
        mediaHelper = new MediaHelper(getContext(), viewModel);
        temiConfigurations = new ArrayList<>();

        clickCounter = 0;
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

        ImageView robosolutionLogo = view.findViewById(R.id.robosolutionLogo);
        robosolutionLogo.setOnClickListener(v -> {
            Log.i(TAG, "Click counter: " + clickCounter);
            if (++clickCounter == NUMBER_OF_CLICKS_TO_STOP) {
                Log.i(TAG, "Stopping...");
                navigateToHomePage();
                if (!isStationaryPatrol) {
                    temiPatroller.getTemiRobot().stopMovement();
                }
            }
        });

        fadingTv = view.findViewById(R.id.fadingTv);
        fadingTv.setTimeout(10, TimeUnit.SECONDS);

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
        timer.schedule(new CameraTask(), 0, CAMERA_INTERVAL);
    }

    private void configureCamera(CameraView camera) {
        camera.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(@NonNull PictureResult result) {
                super.onPictureTaken(result);
                byte[] image = result.getData();
                postmanExecutorService.execute(() -> {
                    try {
                        JSONObject imageJson = JsonRequestHelper.getJSONImageObj(image);
                        boolean isWearingMask = sendImageToServerAndGetMaskDetectionResult(imageJson);
                        Log.i(TAG, "Wearing mask value: " + isWearingMask);
                        boolean clusterDetected = sendImageToServerAndGetClusterDetectionResult(imageJson);
                        Log.i(TAG, "Cluster detected value: " + clusterDetected);
//                            boolean isHuman = sendImageToServerAndGetHumanDetectionResult(imageJson);
                        if (!isWearingMask && clusterDetected) {
                            mediaHelper.uploadImage(image, NO_MASK_AND_CLUSTER);
                            pauseAndMakeCombinedAnnouncement();
                        } else {
                            if (!isWearingMask) {
                                mediaHelper.uploadImage(image, NOT_WEARING_MASK_DETECTED);
                                pauseAndMakeMaskAnnouncement();
                            }
                            if (clusterDetected) {
                                mediaHelper.uploadImage(image, CLUSTER_DETECTED);
                                pauseAndMakeClusterAnnouncement();
                            }
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error in onPictureTaken: " + e.toString());
                        Toast.makeText(getActivity(), "Error while patrolling: " + e.toString(),
                                Toast.LENGTH_LONG).show();
                        navigateToHomePage();
                    }
            });
            }
        });
    }

    private void pauseAndMakeCombinedAnnouncement() {
        if (!isStationaryPatrol) {
            temiPatroller.pausePatrol();
        }
        temiSpeaker.temiSpeak(combinedCmd);
    }

    private void pauseAndMakeMaskAnnouncement() {
        if (!isStationaryPatrol) {
            temiPatroller.pausePatrol();
        }
        temiSpeaker.temiSpeak(maskDetectionCmd);
    }

    private void pauseAndMakeClusterAnnouncement() {
        if (!isStationaryPatrol) {
            temiPatroller.pausePatrol();
        }
        temiSpeaker.temiSpeak(humanDistanceCmd);
    }

    private void startPatrol() {
        temiPatroller = TemiPatroller.getInstance(this, viewModel.getTemiRobot(), isStationaryPatrol);

        viewModel.getTemiRobot().addOnGoToLocationStatusChangedListener(temiPatroller);
        viewModel.getTemiRobot().addTtsListener(temiPatroller);

        TemiRoute selectedRoute = viewModel.getSelectedRoute();
        temiPatroller.patrolRoute(selectedRoute);

    }

    private void initConfigurations() {
        final Observer<List<TemiConfiguration>> configListener = configs -> {
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

            combinedCmd = maskDetectionCmd + " and " + humanDistanceCmd;
            Log.i(TAG, "Mask msg: " + maskDetectionCmd);
            Log.i(TAG, "Human Distance msg: " + humanDistanceCmd);
            Log.i(TAG, "Server IP: " + serverIp);
        };

        viewModel.getConfigurationLiveDataFromRepo().observe(getViewLifecycleOwner(), configListener);
    }



    private Boolean sendImageToServerAndGetMaskDetectionResult(JSONObject imageJson) {
        JSONObject requestJson = JsonRequestHelper.generateJsonMessageForMaskDetection(imageJson);
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

    private Boolean sendImageToServerAndGetClusterDetectionResult(JSONObject imageJson) {
        JSONObject requestJson = JsonRequestHelper.generateJsonMessageForHumanDistance(imageJson);
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

    private Boolean sendImageToServerAndGetHumanDetectionResult(JSONObject imageJson) {
        JSONObject requestJson = JsonRequestHelper.generateJsonMessageForHumanDetection(imageJson);
        // for testing
        Future<Boolean> result = postmanExecutorService.submit(() ->
                jsonPostman.postHumanDetectionRequestAndGetResult(requestJson));
        try {
            return result.get();
        } catch (ExecutionException | InterruptedException e) {
            Log.e(TAG, "sendImageToServerAndGetHumanDetectionResult error: " + e.toString());
            return null;
        }
    }

    public void navigateToHomePage() {
        if (!isStationaryPatrol) {
            viewModel.getTemiRobot().removeTtsListener(temiPatroller);
            viewModel.getTemiRobot().removeOnGoToLocationStatusChangedListener(temiPatroller);
        }
        if (navController.getCurrentDestination().getId() != R.id.homeFragment) {
            navController.navigate(R.id.action_patrolFragment_to_homeFragment);
        }
    }
}