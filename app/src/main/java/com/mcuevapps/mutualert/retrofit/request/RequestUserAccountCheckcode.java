package com.mcuevapps.mutualert.retrofit.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestUserAccountCheckcode {

    @SerializedName("sessionInfo")
    @Expose
    private String sessionInfo;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("username")
    @Expose
    private String username;

    /**
     * No args constructor for use in serialization
     *
     */
    public RequestUserAccountCheckcode() {
    }

    /**
     *
     * @param sessionInfo
     * @param code
     * @param username
     */
    public RequestUserAccountCheckcode(String sessionInfo, String code, String username) {
        super();
        this.sessionInfo = sessionInfo;
        this.code = code;
        this.username = username;
    }

    public String getSessionInfo() {
        return sessionInfo;
    }

    public void setSessionInfo(String sessionInfo) {
        this.sessionInfo = sessionInfo;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}