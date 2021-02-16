package com.robosolutions.temipatrol.google;

import android.util.Log;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

public class DriveServiceHelper {
    private final String TAG = "DrievServiceHelper";
    private final Drive mDriveService;

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
        Log.i(TAG, "in uploadFile() | File ID: " + googleFile.getId());
    }

    // Checks if folder exists, if not it creates the folder and uploads
    public String getFolderIdFromDrive(String date) throws IOException {
        String pageToken = null;
        String folderName = "TemiPatrol"+ " | " + date;

        do {
            FileList result = mDriveService.files().list()
                    .setQ("mimeType = 'application/vnd.google-apps.folder'")
                    .setSpaces("drive")
                    .setFields("nextPageToken, files(id, name)")
                    .setPageToken(pageToken)
                    .execute();
            for (File file : result.getFiles()) {
                if (file.getName().equals(folderName)) {
                    Log.i(TAG, "getFolderId() file found: " + file.getName());
                    return file.getId();
                }
            }
            pageToken = result.getNextPageToken();
        } while (pageToken != null);

        return createGoogleFolderAndGetFolderId(folderName);
    }

    public String createGoogleFolderAndGetFolderId(String folderName) throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName(folderName);
        fileMetadata.setMimeType("application/vnd.google-apps.folder");

        File file = mDriveService.files().create(fileMetadata)
                .setFields("id")
                .execute();
        Log.i(TAG, "File created | Folder name: " + folderName + " | Id: " + file.getId());
        return file.getId();

    }
}