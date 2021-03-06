package com.robosolutions.temipatrol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.robosolutions.temipatrol.viewmodel.GlobalViewModel;


public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int WRITE_PERMISSION_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalViewModel viewModel = new ViewModelProvider(this).get(GlobalViewModel.class);

        viewModel.initialize();

        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_CODE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            if (requestCode == CAMERA_PERMISSION_CODE) {
                boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (!cameraAccepted) {
                    showErrorMsg("Please grant TemiPatrol the Camera Permission for it to function properly");
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                            CAMERA_PERMISSION_CODE);
                }
            } else if (requestCode == WRITE_PERMISSION_CODE) {
                boolean writeAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (!writeAccepted) {
                    showErrorMsg("Please grant TemiPatrol the Write Permission for it to function properly");
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            WRITE_PERMISSION_CODE);
                }
            } else {
                Log.e(TAG, "Unhandled permission code");
            }
        }
    }

    private void showErrorMsg(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
