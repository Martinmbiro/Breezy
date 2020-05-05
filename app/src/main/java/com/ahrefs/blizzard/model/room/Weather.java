package com.ahrefs.blizzard.model.room;

import androidx.lifecycle.LiveData;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/*A single Weather Object, as well as a Weather table in the Database*/
@Entity(tableName = "weather_table")
public class Weather {
    @PrimaryKey(autoGenerate = true)
    private int mId;

    private Long mTime;
    private String mSummary;
    private String mIcon;
    private String mTemperature;
    private String mHumidity;
    private double mUvIndex;

    /*Getters*/
    public int getId() {
        return mId;
    }

    public Long getTime() {
        return mTime;
    }

    public String getSummary() {
        return mSummary;
    }

    public String getIcon() {
        return mIcon;
    }

    public String getTemperature() {
        return mTemperature;
    }

    public String getHumidity() {
        return mHumidity;
    }

    public double getUvIndex() {
        return mUvIndex;
    }

    /*Constructor*/
    public Weather(Long time, String summary, String icon, String temperature, String humidity, double uvIndex) {
        mTime = time;
        mSummary = summary;
        mIcon = icon;
        mTemperature = temperature;
        mHumidity = humidity;
        mUvIndex = uvIndex;
    }

    /*Setter for the Id*/
    public void setId(int id) {
        mId = id;
    }
}
