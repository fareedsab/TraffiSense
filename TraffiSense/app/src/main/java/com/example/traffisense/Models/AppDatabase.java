package com.example.traffisense.Models;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {AlertModel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase{

    public abstract Alert_Interface task_interface();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "alert_database").allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}