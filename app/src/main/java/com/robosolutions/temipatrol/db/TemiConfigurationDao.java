package com.robosolutions.temipatrol.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.robosolutions.temipatrol.model.TemiConfiguration;

import java.util.List;

@Dao
public interface TemiConfigurationDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertVoiceCmdIntoDb(TemiConfiguration temiConfiguration);

    @Query("DELETE from configurationTable")
    void deleteAll();

    @Delete
    void deleteConfiguration(TemiConfiguration temiConfiguration);

    @Update
    void updateConfiguration(TemiConfiguration temiConfiguration);

    // Todo Deleting only one entry

    @Query("SELECT * FROM configurationTable ORDER BY keyValue ASC")
    LiveData<List<TemiConfiguration>> getTemiConfigurationsFromDb();
}
