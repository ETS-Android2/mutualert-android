package com.mcuevapps.mutualert.retrofit.response;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseAlertEmergencyList {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private List<AlertEmergency> data = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public ResponseAlertEmergencyList() {
    }

    /**
     *
     * @param data
     * @param success
     */
    public ResponseAlertEmergencyList(Boolean success, List<AlertEmergency> data) {
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

    public List<AlertEmergency> getData() {
        return data;
    }

    public void setData(List<AlertEmergency> data) {
        this.data = data;
    }

}
