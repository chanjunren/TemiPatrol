package com.robosolutions.temipatrol.google;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.IOException;
import java.text.SimpleDateFormat;
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

    public void uploadFile(java.io.File file) throws IOException {
        String pattern = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        String folderId = getFolderId(sdf.format(new Date()));

        File fileMetadata = new File();
        fileMetadata.setName(file.getName());
        FileContent mediaContent = new FileContent("image/jpeg", file);
        fileMetadata.setParents(Collections.singletonList(folderId));
        File googleFile = mDriveService.files().create(fileMetadata, mediaContent)
                .setFields("id, parents")
                .execute();
        System.out.println("File ID: " + googleFile.getId());
    }

    public String getFolderId(String date) {
        return "lol";
    }

    public String createGoogleFolderAndGetFolderId(String date) throws IOException {
        String pageToken = null;
        do {
            FileList result = mDriveService.files().list()
                    .setQ("mimeType = 'application/vnd.google-apps.folder'" +
                            " and name = 'Pictures'")
                    .setSpaces("drive")
                    .setFields("nextPageToken, files(id, name)")
                    .setPageToken(pageToken)
                    .execute();
            for (File file : result.getFiles()) {
                String fileString = String.format("Found file: %s (%s)\n",
                        file.getName(), file.getId());
                Log.i(TAG, fileString);
            }
            pageToken = result.getNextPageToken();
        } while (pageToken != null);
        return "lol";
    }

    // Google folder mime type: application/vnd.google-apps.folder
    // Creates folder and returns the folder ID
    public String createDateFolder(String date) throws IOException {
        File fileMetadata = new File();
//        fileMetadata.setName(date);
        fileMetadata.setMimeType("application/vnd.google-apps.folder");

        File file = mDriveService.files().create(fileMetadata)
                .setFields("id")
                .execute();
        return file.getId();
    }

    public String createGlobalFolder(String folderName) throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName(folderName);
        fileMetadata.setMimeType("application/vnd.google-apps.folder");

        File file = mDriveService.files().create(fileMetadata)
                .setFields("id")
                .execute();
        return file.getId();
    }

}