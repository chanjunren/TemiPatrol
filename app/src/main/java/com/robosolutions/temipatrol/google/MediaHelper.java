package com.robosolutions.temipatrol.google;

import android.content.Context;
import android.util.Log;

import com.robosolutions.temipatrol.viewmodel.GlobalViewModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;

public class MediaHelper {
    private final String TAG = "MediaHelper";
    public static final int NOT_WEARING_MASK_DETECTED = 0;
    public static final int CLUSTER_DETECTED = 1;
    public static final int NO_MASK_AND_CLUSTER = 2;

    private Context context;
    private GlobalViewModel viewModel;
    private ExecutorService globalExecutorService;

    public MediaHelper(Context context, GlobalViewModel viewModel) {
        this.context = context;
        this.viewModel = viewModel;
        this.globalExecutorService = viewModel.getExecutorService();
    }

    public void uploadImage(byte[] image, int type) {
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

    /** Create a File for saving an image or video */
    public File getOutputMediaFile(int type) throws IOException {
        File outputDir = context.getCacheDir(); // context being the Activity pointer
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        if (type == NOT_WEARING_MASK_DETECTED) {
            return new File(outputDir.getPath() + File.separator +
                    "IMG_NO_MASK_"+ timeStamp + ".jpg");
        } else if (type == CLUSTER_DETECTED) {
            return new File(outputDir.getPath() + File.separator +
                    "IMG_CLUSTER_"+ timeStamp + ".jpg");
        } else if (type == NO_MASK_AND_CLUSTER) {
            return new File(outputDir.getPath() + File.separator +
                    "IMG_NO_MASK_AND_CLUSTER_"+ timeStamp + ".jpg");
        } else {
            return null;
        }
    }
}
