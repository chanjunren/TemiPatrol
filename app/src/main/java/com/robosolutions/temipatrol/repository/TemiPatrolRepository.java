package com.robosolutions.temipatrol.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.robosolutions.temipatrol.db.TemiRouteDao;
import com.robosolutions.temipatrol.db.TemiPatrolRoomDatabase;
import com.robosolutions.temipatrol.db.TemiConfigurationDao;
import com.robosolutions.temipatrol.model.TemiConfiguration;
import com.robosolutions.temipatrol.model.TemiRoute;

import java.util.ArrayList;
import java.util.List;

import static com.robosolutions.temipatrol.model.ConfigurationEnum.ADMIN_PW;
import static com.robosolutions.temipatrol.model.ConfigurationEnum.HUMAN_DIST_MSG;
import static com.robosolutions.temipatrol.model.ConfigurationEnum.MASK_DETECTION_MSG;
import static com.robosolutions.temipatrol.model.ConfigurationEnum.SERVER_IP_ADD;

public class TemiPatrolRepository {
    private final String TAG = "TemiPatrolRepository";
    private TemiRouteDao mTemiRouteDao;
    private TemiConfigurationDao mTemiConfigurationDao;
    private LiveData<List<TemiRoute>> routes;
    private LiveData<List<TemiConfiguration>> commands;

    // Note that in order to unit test the Repository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public TemiPatrolRepository(Application application) {
        TemiPatrolRoomDatabase db = TemiPatrolRoomDatabase.getDatabase(application);
        // Only need Dao to access DB, no need exposure to DB
        mTemiRouteDao = db.routeDao();
        mTemiConfigurationDao = db.voiceCmdDao();
        routes = mTemiRouteDao.getRoutesFromDb();
        commands = mTemiConfigurationDao.getTemiConfigurationsFromDb();
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

    public LiveData<List<TemiConfiguration>> getAllCmdsFromDb() {
        if (commands.getValue() == null) {
            commands = mTemiConfigurationDao.getTemiConfigurationsFromDb();
            if (commands.getValue() == null) {
                commands = getDefaultCmds(generateDefaultConfiguration());
            }
        }
        Log.i(TAG, "returning " + commands.getValue());
        return commands;
    }

    public void insertConfigurationIntoDb(TemiConfiguration temiConfiguration) {
        TemiPatrolRoomDatabase.getDbWriterExecutor().execute(() -> {
            Log.i(TAG, "Inserting command...");
            mTemiConfigurationDao.insertVoiceCmdIntoDb(temiConfiguration);
        });
    }

    public void deleteConfigurationFromDb(TemiConfiguration temiConfiguration) {
        TemiPatrolRoomDatabase.getDbWriterExecutor().execute(() -> {
            Log.i(TAG, "Deleting command...");
            mTemiConfigurationDao.deleteConfiguration(temiConfiguration);
        });
    }

    private List<TemiConfiguration> generateDefaultConfiguration() {
        TemiConfiguration voiceCommand1 = new TemiConfiguration("MASK_DETECTION_MSG", MASK_DETECTION_MSG);
        TemiConfiguration voiceCommand2 = new TemiConfiguration("HUMAN_DIST_MSG", HUMAN_DIST_MSG);
        TemiConfiguration voiceCommand3 = new TemiConfiguration("SERVER_IP_ADD", SERVER_IP_ADD);
        TemiConfiguration voiceCommand4 = new TemiConfiguration("ADMIN_PW", ADMIN_PW);
        List<TemiConfiguration> cmds = new ArrayList<>();
        cmds.add(voiceCommand1);
        cmds.add(voiceCommand2);
        cmds.add(voiceCommand3);
        cmds.add(voiceCommand4);
        return cmds;
    }

    private LiveData<List<TemiConfiguration>> getDefaultCmds(List<TemiConfiguration> cmds) {
        for (TemiConfiguration cmd: cmds) {
            insertConfigurationIntoDb(cmd);
        }
        return mTemiConfigurationDao.getTemiConfigurationsFromDb();

    }
}
