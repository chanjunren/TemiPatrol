package com.robosolutions.temipatrol.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.robosolutions.temipatrol.model.TemiRoute;

import java.util.List;

@Dao
public interface TemiRouteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertRouteIntoDb(TemiRoute route);

    @Query("DELETE from routeTable")
    void deleteAll();

    // Todo Deleting only one entry

    @Query("SELECT * FROM routeTable ORDER BY routeIdx ASC")
    LiveData<List<TemiRoute>> getRoutes();
}
