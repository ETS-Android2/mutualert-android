package com.mcuevapps.mutualert.retrofit.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserAuthSuccess {

    @SerializedName("userId")
    @Expose
    private int userId;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("alert")
    @Expose
    private boolean alert;
    @SerializedName("profile")
    @Expose
    private UserAuthSuccessProfile profile;

    /**
     * No args constructor for use in serialization
     *
     */
    public UserAuthSuccess() {
    }

    /**
     *
     * @param userId
     * @param alert
     * @param profile
     * @param token
     */
    public UserAuthSuccess(int userId, String token, boolean alert,UserAuthSuccessProfile profile) {
        super();
        this.userId = userId;
        this.token = token;
        this.alert = alert;
        this.profile = profile;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean getAlert() {
        return alert;
    }

    public void setAlert(boolean alert) {
        this.alert = alert;
    }

    public UserAuthSuccessProfile getProfile() {
        return profile;
    }

    public void setProfile(UserAuthSuccessProfile profile) {
        this.profile = profile;
    }

}