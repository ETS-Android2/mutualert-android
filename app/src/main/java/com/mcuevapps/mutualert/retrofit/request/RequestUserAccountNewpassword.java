package com.mcuevapps.mutualert.retrofit.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestUserAccountNewpassword {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;

    /**
     * No args constructor for use in serialization
     *
     */
    public RequestUserAccountNewpassword() {
    }

    /**
     *
     * @param password
     * @param code
     * @param username
     */
    public RequestUserAccountNewpassword(String code, String username, String password) {
        super();
        this.code = code;
        this.username = username;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}