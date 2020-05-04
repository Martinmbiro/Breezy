package com.ahrefs.blizzard.model.retrofit;

/*A single response from the HTTP Call*/
public class BreezyResponse {
    private double latitude;
    private double longitude;
    private String timezone;
    private Currently currently;
    private int offset;

    /*Getters for the class*/
    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getTimezone() {
        return timezone;
    }

    public Currently getCurrently() {
        return currently;
    }

    public int getOffset() {
        return offset;
    }

}
