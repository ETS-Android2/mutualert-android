package com.mcuevapps.mutualert.retrofit.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestUserStateLocation {
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("accuracy")
    @Expose
    private String accuracy;

    /**
     * No args constructor for use in serialization
     *
     */
    public RequestUserStateLocation() {
    }

    /**
     *
     * @param latitude
     * @param accuracy
     * @param longitude
     */
    public RequestUserStateLocation(String latitude, String longitude, String accuracy) {
        super();
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

}