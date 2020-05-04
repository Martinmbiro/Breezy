package com.ahrefs.blizzard.model.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Weather.class, version = 1)
public abstract class WeatherDb extends RoomDatabase {
    //Ensure the class is a Singleton
    private static WeatherDb mInstance;

    //Give Database access to the WeatherDao
    public abstract WeatherDao weatherDao();

    public static synchronized WeatherDb getInstance(Context context){
        if(mInstance == null){
            mInstance = Room.databaseBuilder(context.getApplicationContext(),WeatherDb.class,"weather_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return mInstance;
    }
}
