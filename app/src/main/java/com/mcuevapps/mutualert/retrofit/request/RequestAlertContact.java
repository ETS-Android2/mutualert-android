package com.mcuevapps.mutualert.retrofit.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestAlertContact {

    @SerializedName("alias")
    @Expose
    private String alias;
    @SerializedName("phone")
    @Expose
    private String phone;

    /**
     * No args constructor for use in serialization
     *
     */
    public RequestAlertContact() {
    }

    /**
     *
     * @param phone
     * @param alias
     */
    public RequestAlertContact(String alias, String phone) {
        super();
        this.alias = alias;
        this.phone = phone;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}