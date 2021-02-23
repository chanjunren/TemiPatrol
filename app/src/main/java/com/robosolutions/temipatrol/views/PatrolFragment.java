package com.robosolutions.temipatrol.views;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
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
import com.robosolutions.temipatrol.model.TemiRoute;
import com.robosolutions.temipatrol.viewmodel.GlobalViewModel;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;


// Page shown when Temi is patrolling
public class PatrolFragment extends Fragment {

    private class PatrolCallbackTask implements Runnable {
        private final Runnable patrolTask;

        void navigateToHomePage() {
            navController.navigate(R.id.action_patrolFragment_to_homeFragment);
        }
        PatrolCallbackTask(Runnable patrolTask) {
            this.patrolTask = patrolTask;
        }
        @Override
        public void run() {
            patrolTask.run();
            navigateToHomePage();
        }

    }


    private class CameraTask extends TimerTask {
        @Override
        public void run() {
            camera.takePicture();
        }
    }
    private static final String TAG = "PatrolFragment";
    private GlobalViewModel viewModel;
    private CameraView camera;
    private ExecutorService executorService;
    private NavController navController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);
        executorService = viewModel.getExecutorService();
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

        startCamera();
        PatrolCallbackTask patrolTask = new PatrolCallbackTask(() -> {
            TemiRoute selectedRoute = viewModel.getSelectedRoute();
            viewModel.getTemiController().patrolRoute(selectedRoute);
        });

        executorService.execute(patrolTask);

    }

    private void startCamera() {
        Timer timer = new Timer();
        timer.schedule(new CameraTask(), 0, 3000);
    }

    private void configureCamera(CameraView camera) {
        camera.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(@NonNull PictureResult result) {
                super.onPictureTaken(result);
                byte[] image = result.getData();
                JSONObject requestMessage = JsonRequestUtils.generateJsonMessage(image);
                sendImageToServer(requestMessage);
            }
        });
    }

    private void sendImageToServer(JSONObject requestJson) {
        // for testing
        executorService.execute(() -> {
            JsonPostman postman = new JsonPostman(getActivity());
            postman.postRequest(requestJson);
        });

    }
}