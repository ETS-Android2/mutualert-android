package com.mcuevapps.mutualert.retrofit.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserAuthSuccess {

    @SerializedName("token")
    @Expose
    private String token;
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
     * @param profile
     * @param token
     */
    public UserAuthSuccess(String token, UserAuthSuccessProfile profile) {
        super();
        this.token = token;
        this.profile = profile;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserAuthSuccessProfile getProfile() {
        return profile;
    }

    public void setProfile(UserAuthSuccessProfile profile) {
        this.profile = profile;
    }

}