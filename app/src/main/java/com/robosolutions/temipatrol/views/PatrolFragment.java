package com.robosolutions.temipatrol.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.robosolutions.temipatrol.model.TemiRoute;
import com.robosolutions.temipatrol.model.TemiVoiceCommand;
import com.robosolutions.temipatrol.temi.TemiController;
import com.robosolutions.temipatrol.viewmodel.GlobalViewModel;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


// Page shown when Temi is patrolling
public class PatrolFragment extends Fragment {
    private static final String TAG = "PatrolFragment";
    private static final int NOT_WEARING_MASK_DETECTED = 0;
    private static final int CLUSTER_DETECTED = 1;

    private class AnnouncementCallbackRunnable implements Runnable {
        private Runnable announcementTask;

        void resumeNavigation() {
            patrolCallbackRunnable.resume();
        }

        public AnnouncementCallbackRunnable(Runnable announcementTask) {
            this.announcementTask = announcementTask;
        }

        @Override
        public void run() {
            // Your code here
            announcementTask.run();
            resumeNavigation();

        }
    }

    private class PatrolCallbackRunnable implements Runnable  {
        private final Runnable patrolTask;
        private final Object pauseLock;
        private boolean running, paused;

        void navigateToHomePage() {
            navController.navigate(R.id.action_patrolFragment_to_homeFragment);
        }
        PatrolCallbackRunnable(Runnable patrolTask) {
            this.patrolTask = patrolTask;
            pauseLock = new Object();
            running = true;
            paused = false;
        }

        @Override
        public void run() {
            while (running) {
                synchronized (pauseLock) {
                    if (!running) { // may have changed while waiting to
                        // synchronize on pauseLock
                        break;
                    }
                    if (paused) {
                        try {
                            synchronized (pauseLock) {
                                pauseLock.wait(); // will cause this Thread to block until
                                // another thread calls pauseLock.notifyAll()
                                // Note that calling wait() will
                                // relinquish the synchronized lock that this
                                // thread holds on pauseLock so another thread
                                // can acquire the lock to call notifyAll()
                                // (link with explanation below this code)
                            }
                        } catch (InterruptedException ex) {
                            break;
                        }
                        if (!running) { // running might have changed since we paused
                            break;
                        }
                    }
                }
                // Your code here
                patrolTask.run();
                navigateToHomePage();
            }
        }

        public void stop() {
            running = false;
            // you might also want to interrupt() the Thread that is
            // running this Runnable, too, or perhaps call:
            resume();
            // to unblock
        }

        public void pause() {
            // you may want to throw an IllegalStateException if !running
            Log.i(TAG, "Thread Pause called");
            paused = true;
        }

        public void resume() {
            Log.i(TAG, "Thread Resume called");
            synchronized (pauseLock) {
                paused = false;
                pauseLock.notifyAll(); // Unblocks thread
            }
        }
    }


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
    private ArrayList<TemiVoiceCommand> commands;
    private NavController navController;
    private JsonPostman jsonPostman;
    private TemiController temiController;

    private PatrolCallbackRunnable patrolCallbackRunnable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);
        globalExecutorService = viewModel.getExecutorService();
        postmanExecutorService = Executors.newFixedThreadPool(20);
        jsonPostman = new JsonPostman(getActivity());
        commands = (ArrayList<TemiVoiceCommand>) viewModel.getCommandLiveDataFromRepo().getValue();

        temiController = viewModel.getTemiController();
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

        configureCamera(camera);

//        takePicturesContinuously();
        startPatrol();

        Handler handler = new Handler();
        handler.postDelayed(() -> pauseAndMakeMaskAnnouncement(), 10000);
        handler.postDelayed(() -> pauseAndMakeClusterAnnouncement(), 15000);
        handler.postDelayed(() -> pauseAndMakeMaskAnnouncement(), 20000);
    }

    private void takePicturesContinuously() {
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
                boolean personNotWearingMask = postMaskDetectionReq(maskReqMsg);
                JSONObject clusterReqMsg = JsonRequestUtils.generateJsonMessageForHumanDistance(image);
                boolean clusterDetected = postHumanDistanceReq(clusterReqMsg);

                if (personNotWearingMask) {
                    uploadImage(image, NOT_WEARING_MASK_DETECTED);
                    pauseAndMakeMaskAnnouncement();
                }
                if (clusterDetected) {
                    uploadImage(image, CLUSTER_DETECTED);
                    pauseAndMakeClusterAnnouncement();
                }
            }
        });
    }

    private void pauseAndMakeMaskAnnouncement() {
        Log.i(TAG, "pauseAndMakeMaskAnnouncement called!");
        patrolCallbackRunnable.pause();
        AnnouncementCallbackRunnable announcementCallbackRunnable = new AnnouncementCallbackRunnable(() -> {
            TemiVoiceCommand voiceCommand = new TemiVoiceCommand("Hi Im the mask announcement",
                    0);
            temiController.temiSpeak(voiceCommand.getCommand());
        });
        globalExecutorService.execute(announcementCallbackRunnable);

    }

    private void pauseAndMakeClusterAnnouncement() {
        Log.i(TAG, "pauseAndMakeClusterAnnouncement called!");
        patrolCallbackRunnable.pause();
        AnnouncementCallbackRunnable announcementCallbackRunnable = new AnnouncementCallbackRunnable(() -> {
            TemiVoiceCommand voiceCommand = new TemiVoiceCommand("Hi Im the cluster announcement", 0);
            temiController.temiSpeak(voiceCommand.getCommand());
        });
        globalExecutorService.execute(announcementCallbackRunnable);

    }

    private void startPatrol() {
        patrolCallbackRunnable = new PatrolCallbackRunnable(() -> {
            TemiRoute selectedRoute = viewModel.getSelectedRoute();
            viewModel.getTemiController().patrolRoute(selectedRoute);
        });
        globalExecutorService.execute(patrolCallbackRunnable);
    }

    private void uploadImage(byte[] image, int type) {
        globalExecutorService.execute(() -> {
            try {
                Log.i(TAG, "UPLOADING FILE...");
                File pictureFile = getOutputMediaFile(type);
                if (pictureFile == null){
                    Log.d(TAG, "Error creating media file, check storage permissions");
                    return;
                }
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(image);
                fos.close();
                viewModel.uploadFileToViewModel(pictureFile);
            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            }
        });
    }

    private Boolean postMaskDetectionReq(JSONObject requestJson) {
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

    private Boolean postHumanDistanceReq(JSONObject requestJson) {
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

    /** Create a File for saving an image or video */
    private File getOutputMediaFile(int type) throws IOException{
        File outputDir = getContext().getCacheDir(); // context being the Activity pointer
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        if (type == NOT_WEARING_MASK_DETECTED) {
            return new File(outputDir.getPath() + File.separator +
                    "IMG_NO_MASK_"+ timeStamp + ".jpg");
        } else if (type == CLUSTER_DETECTED) {
            return new File(outputDir.getPath() + File.separator +
                    "IMG_CLUSTER_"+ timeStamp + ".jpg");
        } else {
            return null;
        }
    }
}