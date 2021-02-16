package com.robosolutions.temipatrol.views;

import android.Manifest;
import android.content.pm.PackageManager;
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
import com.robosolutions.temipatrol.viewmodel.GlobalViewModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static androidx.core.content.PermissionChecker.checkSelfPermission;
import static com.robosolutions.temipatrol.camera.CameraUtils.MEDIA_TYPE_IMAGE;
import static com.robosolutions.temipatrol.camera.CameraUtils.getOutputMediaFile;

// Page shown when Temi is patrolling
public class PatrolFragment extends Fragment {
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
        return inflater.inflate(R.layout.fragment_patrol, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FrameLayout previewLayout = view.findViewById(R.id.camera_preview);
        previewLayout.addView(mPreview);

        // Just for testing
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mCamera.takePicture(null, null, mPictureCallback);
                Log.i(TAG, "Camera released");
            }
        }, 10000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                releaseCamera();
                Log.i(TAG, "Camera released");
            }
        }, 20000);
    }

    public Camera getCameraInstance() {
        int cameraCount = 0;
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        Log.i(TAG, "CAmera count: " + cameraCount);

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

            if (checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                    != PermissionChecker.PERMISSION_GRANTED) {
                Log.i(TAG, "Permission NOT granted!");
            } else {
                Log.i(TAG, "Permission granted!");
            }

            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);


            if (pictureFile == null){
                Log.d(TAG, "Error creating media file, check storage permissions");
                return;
            }

            viewModel.getExecutorService().execute(() -> {
                try {

                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    fos.write(data);
                    fos.close();
                    viewModel.getmDriveServiceHelper().uploadFile(pictureFile);
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
}