package com.robosolutions.temipatrol.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.robosolutions.temipatrol.db.TemiRouteDao;
import com.robosolutions.temipatrol.db.TemiPatrolRoomDatabase;
import com.robosolutions.temipatrol.db.TemiVoiceCmdDao;
import com.robosolutions.temipatrol.model.TemiRoute;
import com.robosolutions.temipatrol.model.TemiVoiceCommand;

import java.util.List;

public class TemiPatrolRepository {
    private final String TAG = "TemiPatrolRepository";
    private TemiRouteDao mTemiRouteDao;
    private TemiVoiceCmdDao mTemiVoiceCmdDao;
    private LiveData<List<TemiRoute>> routes;
    private LiveData<List<TemiVoiceCommand>> commands;

    // Note that in order to unit test the Repository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public TemiPatrolRepository(Application application) {
        TemiPatrolRoomDatabase db = TemiPatrolRoomDatabase.getDatabase(application);
        // Only need Dao to access DB, no need exposure to DB
        mTemiRouteDao = db.routeDao();
        mTemiVoiceCmdDao = db.voiceCmdDao();
        routes = mTemiRouteDao.getRoutesFromDb();
        commands = mTemiVoiceCmdDao.getVoiceCmdsFromDb();
    }

    public LiveData<List<TemiRoute>> getAllRoutesFromDb() {
        if (routes.getValue() == null) {
            routes = mTemiRouteDao.getRoutesFromDb();
        }
        Log.i(TAG, "returning " + routes.getValue());
        return routes;
    }

    public void insertTemiRouteIntoDb(TemiRoute temiRoute) {
        TemiPatrolRoomDatabase.getDbWriterExecutor().execute(() -> {
            Log.i(TAG, "Inserting route...");
            mTemiRouteDao.insertRouteIntoDb(temiRoute);
        });
    }

    public void deleteTemiRouteFromDb(TemiRoute temiRoute) {
        TemiPatrolRoomDatabase.getDbWriterExecutor().execute(() -> {
            Log.i(TAG, "Deleting route...");
            mTemiRouteDao.deleteRoute(temiRoute);
        });
    }

    public LiveData<List<TemiVoiceCommand>> getAllCmdsFromDb() {
        if (commands.getValue() == null) {
            commands = mTemiVoiceCmdDao.getVoiceCmdsFromDb();
        }
        Log.i(TAG, "returning " + commands.getValue());
        return commands;
    }

    public void insertTemiVoiceCmdIntoDb(TemiVoiceCommand temiVoiceCommand) {
        TemiPatrolRoomDatabase.getDbWriterExecutor().execute(() -> {
            Log.i(TAG, "Inserting command...");
            mTemiVoiceCmdDao.insertVoiceCmdIntoDb(temiVoiceCommand);
        });
    }

    public void deleteTemiVoiceCmdFromDb(TemiVoiceCommand temiVoiceCommand) {
        TemiPatrolRoomDatabase.getDbWriterExecutor().execute(() -> {
            Log.i(TAG, "Deleting command...");
            mTemiVoiceCmdDao.deleteVoiceCmd(temiVoiceCommand);
        });
    }
}
