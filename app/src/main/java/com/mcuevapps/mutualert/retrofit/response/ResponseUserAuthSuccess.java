package com.mcuevapps.mutualert.retrofit.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseUserAuthSuccess {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private UserAuthSuccess data;

    /**
     * No args constructor for use in serialization
     *
     */
    public ResponseUserAuthSuccess() {
    }

    /**
     *
     * @param data
     * @param success
     */
    public ResponseUserAuthSuccess(Boolean success, UserAuthSuccess data) {
        super();
        this.success = success;
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public UserAuthSuccess getData() {
        return data;
    }

    public void setData(UserAuthSuccess data) {
        this.data = data;
    }

}