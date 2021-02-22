package com.robosolutions.temipatrol.views;

import android.Manifest;
import android.hardware.Camera;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.robosolutions.temipatrol.R;
import com.robosolutions.temipatrol.camera.CameraPreview;
import com.robosolutions.temipatrol.client.JsonPostman;
import com.robosolutions.temipatrol.client.JsonRequestUtils;
import com.robosolutions.temipatrol.viewmodel.GlobalViewModel;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import static androidx.core.content.PermissionChecker.checkSelfPermission;
import static com.robosolutions.temipatrol.camera.CameraUtils.MEDIA_TYPE_IMAGE;
import static com.robosolutions.temipatrol.camera.CameraUtils.getOutputMediaFile;

// Page shown when Temi is patrolling
public class PatrolFragment extends Fragment {
    private class TakePictureTask extends TimerTask {
        @Override
        public void run() {
            mCamera.takePicture(null, null, mPictureCallback);

        }
    }
    private static final String TAG = "PatrolFragment";
    private Camera mCamera;
    private CameraPreview mPreview;
    private GlobalViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCamera = getCameraInstance();
        mCamera.setDisplayOrientation(180);
        mPreview = new CameraPreview(getContext(), mCamera);
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
        FrameLayout previewLayout = view.findViewById(R.id.camera_preview);
        previewLayout.addView(mPreview);

        Timer timer = new Timer();
        timer.schedule(new TakePictureTask(),0, 1000);

        // Just for testing
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                releaseCamera();
                Log.i(TAG, "Camera released");
            }
        }, 100000);
    }

    public Camera getCameraInstance() {
        int cameraCount = 0;
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        if (checkSelfPermission(this.getContext() ,Manifest.permission.CAMERA)
                != PermissionChecker.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission not granted");
        }
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    cam = Camera.open(camIdx);
                } catch (RuntimeException e) {
                    Log.e(TAG, "Camera failed to open: " + e.getLocalizedMessage());
                }
            }
        }

        return cam;
    }

    private Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.i(TAG, "OnPictureTaken executed...");
            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null){
                Log.d(TAG, "Error creating media file, check storage permissions");
                return;
            }

            camera.startPreview();

            viewModel.getExecutorService().execute(() -> {
                try {
                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    fos.write(data);
                    fos.close();
                    JSONObject requestMessage = JsonRequestUtils.generateJsonMessage(pictureFile);
                    sendImageToServer(requestMessage);
                } catch (FileNotFoundException e) {
                    Log.d(TAG, "File not found: " + e.getMessage());
                } catch (IOException e) {
                    Log.d(TAG, "Error accessing file: " + e.getMessage());
                }
            });
        }
    };

    private void releaseCamera() {
        mCamera.stopPreview();
        mCamera.setPreviewCallback(null);

        mCamera.release();
        mCamera = null;
    }

    private void sendImageToServer(JSONObject requestJson) {
        // for testing
        viewModel.getExecutorService().execute(() -> {
            JsonPostman postman = new JsonPostman(getActivity());
            postman.postRequest(requestJson);
        });

    }
}