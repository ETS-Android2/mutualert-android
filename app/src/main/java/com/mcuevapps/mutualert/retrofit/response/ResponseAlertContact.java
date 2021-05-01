package com.mcuevapps.mutualert.retrofit.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseAlertContact {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private AlertContact data;

    /**
     * No args constructor for use in serialization
     *
     */
    public ResponseAlertContact() {
    }

    /**
     *
     * @param data
     * @param success
     */
    public ResponseAlertContact(Boolean success, AlertContact data) {
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

    public AlertContact getData() {
        return data;
    }

    public void setData(AlertContact data) {
        this.data = data;
    }

}