package com.robosolutions.temipatrol.db;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.robosolutions.temipatrol.model.TemiRoute;
import com.robosolutions.temipatrol.model.TemiVoiceCommand;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// https://medium.com/google-developers/understanding-migrations-with-room-f01e04b07929
@Database(entities = {TemiRoute.class, TemiVoiceCommand.class}, version = 1, exportSchema = false)
public abstract class TemiPatrolRoomDatabase extends RoomDatabase {
    public abstract TemiRouteDao routeDao();
    public abstract TemiVoiceCmdDao voiceCmdDao();

    //Volatile keyword is used to modify the value of a variable by different threads.
    // It means that multiple threads can use a method and instance of the classes at the same time
    // without any problem
    private static volatile TemiPatrolRoomDatabase INSTANCE;
    private static final int THREAD_COUNT = 4;
    // For running DB operations asynchronously
    static final ExecutorService dbWriterExecutor = Executors.newFixedThreadPool(THREAD_COUNT);

    public static TemiPatrolRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            // Can append addCallback(sRoomDatabaseCallback) for future development if needed
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    TemiPatrolRoomDatabase.class, "TemiPatrolDatabase").build();
        }
        return INSTANCE;
    }

    public static ExecutorService getDbWriterExecutor() {
        return dbWriterExecutor;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

//            dbWriterExecutor.execute(() -> {
//                TemiTaskDao dao = INSTANCE.sequenceDao();
//                dao.deleteAll();
//
//                // Can add some default words
//                Word word = new Word("Hello");
//                dao.insert(word);
//                word = new Word("World");
//                dao.insert(word);
//            });
        }
    };
}
