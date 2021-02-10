package com.robosolutions.temipatrol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.robosolutions.temipatrol.viewmodel.GlobalViewModel;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static androidx.core.content.PermissionChecker.checkSelfPermission;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    private static final int CAMERA_PERMISSION_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalViewModel viewModel = new ViewModelProvider(this).get(GlobalViewModel.class);
        viewModel.initializeTemiRobot();
        setContentView(R.layout.activity_main);
        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission not granted");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            Log.i(TAG, "grantResults: " + grantResults);
            Log.i(TAG, "PERMISSION GRANTED CODE: " + PackageManager.PERMISSION_GRANTED);
            Log.i(TAG, "PERMISSION DENIED CODE: " + PackageManager.PERMISSION_DENIED);
            for (int result: grantResults) {
                Log.i(TAG, "Result: " + result);
            }
//            boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//            boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
//
//            if (locationAccepted && cameraAccepted)
//                Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access location data and camera", Toast.LENGTH_LONG).show();
//            else {
//                Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access location data and camera", Toast.LENGTH_LONG).show();
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    if (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
//                        showMessageOKCancel("You need to allow access to access to the camera",
//                                new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                            requestPermissions(new String[]{Manifest.permission.CAMERA},
//                                                    CAMERA_PERMISSION_CODE);
//                                        }
//                                    }
//                                });
//                        return;
//                    }
//                }

//            }
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getApplicationContext())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}
