package com.robosolutions.temipatrol.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.PictureResult;
import com.robosolutions.temipatrol.R;
import com.robosolutions.temipatrol.client.JsonPostman;
import com.robosolutions.temipatrol.client.JsonRequestUtils;
import com.robosolutions.temipatrol.viewmodel.GlobalViewModel;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

// Page shown when Temi is patrolling
public class PatrolFragment extends Fragment {
    private class CameraTask extends TimerTask {
        @Override
        public void run() {
            camera.takePicture();
        }
    }
    private static final String TAG = "PatrolFragment";
    private GlobalViewModel viewModel;
    private CameraView camera;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);
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

        camera = view.findViewById(R.id.camera);
        camera.setLifecycleOwner(getViewLifecycleOwner());

        configureCamera(camera);

        startCamera();
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
                JSONObject requestMessage = JsonRequestUtils.generateJsonMessage(image);
                sendImageToServer(requestMessage);
            }
        });
    }

    private void sendImageToServer(JSONObject requestJson) {
        // for testing
        viewModel.getExecutorService().execute(() -> {
            JsonPostman postman = new JsonPostman(getActivity());
            postman.postRequest(requestJson);
        });

    }
}