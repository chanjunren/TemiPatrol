package com.robosolutions.temipatrol.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.robosolutions.temipatrol.google.DriveServiceHelper;
import com.robosolutions.temipatrol.model.TemiRoute;
import com.robosolutions.temipatrol.repository.RouteRepository;
import com.robosolutions.temipatrol.temi.TemiController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GlobalViewModel extends AndroidViewModel {
    private final String TAG = "GlobalViewModel";
    private DriveServiceHelper mDriveServiceHelper;
    private TemiController temiController;
    private RouteRepository mRouteRepo;
    private LiveData<List<TemiRoute>> routeLiveData;

    public GlobalViewModel(Application application) {
        super(application);
        mRouteRepo = new RouteRepository(application);
        routeLiveData = mRouteRepo.getAllRoutesFromDb();
    }


    public void initializeTemiRobot() {
        temiController = new TemiController();
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

    public TemiController getTemiController() {
        return temiController;
    }

    public void insertRouteIntoRepo(TemiRoute temiRoute) {
        mRouteRepo.insertTemiRouteIntoDb(temiRoute);
    }

    public LiveData<List<TemiRoute>> getRouteLiveDataFromRepo() {
        return mRouteRepo.getAllRoutesFromDb();
    }
}
