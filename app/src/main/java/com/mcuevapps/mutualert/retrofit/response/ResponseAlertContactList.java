package com.mcuevapps.mutualert.retrofit.response;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseAlertContactList {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private List<AlertContact> data = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public ResponseAlertContactList() {
    }

    /**
     *
     * @param data
     * @param success
     */
    public ResponseAlertContactList(Boolean success, List<AlertContact> data) {
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

    public List<AlertContact> getData() {
        return data;
    }

    public void setData(List<AlertContact> data) {
        this.data = data;
    }

}