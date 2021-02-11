package com.robosolutions.temipatrol.google;

import android.content.Context;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DriveServiceHelper {
    private final String TAG = "DrievServiceHelper";
    private final Executor mExecutor = Executors.newSingleThreadExecutor();
    private final Drive mDriveService;
    private Context context;

    public DriveServiceHelper(Drive mDriveService) {
        this.mDriveService = mDriveService;
    }

    public void uploadFile(java.io.File file, String folderId) throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName(file.getName());
        FileContent mediaContent = new FileContent("image/jpeg", file);
        fileMetadata.setParents(Collections.singletonList(folderId));
        File googleFile = mDriveService.files().create(fileMetadata, mediaContent)
                .setFields("id, parents")
                .execute();
        System.out.println("File ID: " + googleFile.getId());
    }

    // Google folder mime type: application/vnd.google-apps.folder
    // Creates folder and returns the folder ID
    public String createFolder(String date) throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName(date);
        fileMetadata.setMimeType("application/vnd.google-apps.folder");

        File file = mDriveService.files().create(fileMetadata)
                .setFields("id")
                .execute();
        return file.getId();
    }
}