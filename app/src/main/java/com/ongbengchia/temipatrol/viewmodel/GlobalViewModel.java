package com.ongbengchia.temipatrol.viewmodel;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.ongbengchia.temipatrol.google.DriveServiceHelper;

import java.util.Collections;

public class GlobalViewModel extends ViewModel {
    private DriveServiceHelper mDriveServiceHelper;

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

}
