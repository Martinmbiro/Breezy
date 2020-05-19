package com.ahrefs.blizzard.model.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface WeatherDao {
    //Select the Weather Object from the Database
    @Query("SELECT * FROM weather_table")
    LiveData<Weather> getWeather();

    //To insert a Weather object into the Database
    @Insert
    void insertWeather(Weather weather);

    //Delete Weather Object from the Database
    @Query("DELETE FROM weather_table")
    void deleteOldWeather();

}
