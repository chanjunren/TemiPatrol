package com.robosolutions.temipatrol.viewmodel;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.robosolutions.temipatrol.google.DriveServiceHelper;
import com.robosolutions.temipatrol.model.TemiConfiguration;
import com.robosolutions.temipatrol.model.TemiRoute;
import com.robosolutions.temipatrol.repository.TemiPatrolRepository;
import com.robotemi.sdk.Robot;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GlobalViewModel extends AndroidViewModel {
    private final String TAG = "GlobalViewModel";
    private static int MAX_THREAD_COUNT = 5;
    private DriveServiceHelper mDriveServiceHelper;
    private TemiPatrolRepository temiPatrolRepo;
    private LiveData<List<TemiRoute>> routeLiveData;
    private ExecutorService executorService;
    private HashMap<String, String> fileIdMap;
    private Robot temiRobot;

    private TemiRoute selectedRoute;

    public GlobalViewModel(Application application) {
        super(application);
        temiPatrolRepo = new TemiPatrolRepository(application);
        routeLiveData = temiPatrolRepo.getAllRoutesFromDb();
    }

    public void initialize() {
        initializeTemiRobot();
        initializeExecutorService();
        fileIdMap = new HashMap<>();
    }

    private void initializeExecutorService() {
        executorService = Executors.newFixedThreadPool(MAX_THREAD_COUNT);
    }

    public void initializeTemiRobot() {
        temiRobot = Robot.getInstance();
    }

    public void initializeGoogleServices(GoogleSignInAccount googleSignInAccount, Context context) {
        GoogleAccountCredential credential =
                GoogleAccountCredential.usingOAuth2(
                        context, Collections.singleton(DriveScopes.DRIVE_FILE));

        credential.setSelectedAccount(googleSignInAccount.getAccount());
        Drive googleDriveService = new Drive.Builder(
                AndroidHttp.newCompatibleTransport(),
                new GsonFactory(),
                credential)
                .build();
        mDriveServiceHelper = new DriveServiceHelper(googleDriveService);
    }

    public DriveServiceHelper getmDriveServiceHelper() {
        return mDriveServiceHelper;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void uploadFileToViewModel(java.io.File file) {
        String pattern = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String folderName = sdf.format(new Date());
        try {
            if (fileIdMap.containsKey(folderName)) {
                mDriveServiceHelper.uploadFile(file, fileIdMap.get(folderName));
            } else {
                String folderId = mDriveServiceHelper.getFolderIdFromDrive(folderName);
                fileIdMap.put(folderName, folderId);
                mDriveServiceHelper.uploadFile(file, fileIdMap.get(folderName));
            }
        } catch(Exception e) {
            Log.e(TAG, "Error while uploading file to viewModel: " + e.toString());
        }
    }

    public void insertRouteIntoRepo(TemiRoute temiRoute) {
        temiPatrolRepo.insertTemiRouteIntoDb(temiRoute);
    }

    public void deleteRouteFromRepo(TemiRoute temiRoute) {
        temiPatrolRepo.deleteTemiRouteFromDb(temiRoute);
    }

    public void insertConfigurationIntoRepo(TemiConfiguration temiConfiguration) {
        Log.i(TAG, "insertVoiceCmdIntoRepo: " + temiConfiguration);
        temiPatrolRepo.insertConfigurationIntoDb(temiConfiguration);

    }

    public void deleteConfigurationFromRepo(TemiConfiguration temiConfiguration) {
        temiPatrolRepo.deleteConfigurationFromDb(temiConfiguration);
    }

    public LiveData<List<TemiConfiguration>> getConfigurationLiveDataFromRepo() {
        return temiPatrolRepo.getAllCmdsFromDb();
    }

    public LiveData<List<TemiRoute>> getRouteLiveDataFromRepo() {
        return temiPatrolRepo.getAllRoutesFromDb();
    }

    public TemiRoute getSelectedRoute() {
        return selectedRoute;
    }

    public void setSelectedRoute(TemiRoute selectedRoute) {
        this.selectedRoute = selectedRoute;
    }

    public Robot getTemiRobot() {
        return temiRobot;
    }
}
