package com.mcuevapps.mutualert.retrofit.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestUserSessionFcm {

    @SerializedName("registrationId")
    @Expose
    private String registrationId;

    /**
     * No args constructor for use in serialization
     *
     */
    public RequestUserSessionFcm() {
    }

    /**
     *
     * @param registrationId
     */
    public RequestUserSessionFcm(String registrationId) {
        super();
        this.registrationId = registrationId;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

}
