package com.robosolutions.temipatrol.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.robosolutions.temipatrol.db.TemiRouteDao;
import com.robosolutions.temipatrol.db.TemiRouteRoomDatabase;
import com.robosolutions.temipatrol.model.TemiRoute;

import java.util.List;

public class RouteRepository {
    private final String TAG = "RouteRepository";
    private TemiRouteDao mTemiRouteDao;
    private LiveData<List<TemiRoute>> routes;

    // Note that in order to unit test the Repository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public RouteRepository(Application application) {
        TemiRouteRoomDatabase db = TemiRouteRoomDatabase.getDatabase(application);
        // Only need Dao to access DB, no need exposure to DB
        mTemiRouteDao = db.routeDao();
        routes = mTemiRouteDao.getRoutesFromDb();
    }

    public LiveData<List<TemiRoute>> getAllRoutesFromDb() {
        if (routes.getValue() == null) {
            routes = mTemiRouteDao.getRoutesFromDb();
        }
        Log.i(TAG, "returning " + routes.getValue());
        return routes;
    }

    public void insertTemiRouteIntoDb(TemiRoute temiRoute) {
        TemiRouteRoomDatabase.getDbWriterExecutor().execute(() -> {
            Log.i(TAG, "Inserting route...");
            mTemiRouteDao.insertRouteIntoDb(temiRoute);
        });
    }
}
