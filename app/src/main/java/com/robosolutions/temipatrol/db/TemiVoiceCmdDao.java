package com.robosolutions.temipatrol.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.robosolutions.temipatrol.model.TemiRoute;
import com.robosolutions.temipatrol.model.TemiVoiceCommand;

import java.util.List;

@Dao
public interface TemiVoiceCmdDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertVoiceCmdIntoDb(TemiVoiceCommand temiVoiceCmd);

    @Query("DELETE from voiceCmdTable")
    void deleteAll();

    @Delete
    void deleteVoiceCmd(TemiVoiceCommand temiVoiceCmd);

    @Update
    void updateRoute(TemiVoiceCommand temiVoiceCmd);

    // Todo Deleting only one entry

    @Query("SELECT * FROM voiceCmdTable ORDER BY keyValue ASC")
    LiveData<List<TemiVoiceCommand>> getVoiceCmdsFromDb();
}
