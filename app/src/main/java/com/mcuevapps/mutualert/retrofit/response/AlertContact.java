package com.mcuevapps.mutualert.retrofit.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AlertContact {
    @SerializedName("id")
    @Expose
    private Integer id;
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
    public AlertContact() {
    }

    public AlertContact(AlertContact newContact) {
        this.id = newContact.getId();
        this.alias = newContact.getAlias();
        this.phone = newContact.getPhone();
    }

    /**
     *
     * @param phone
     * @param alias
     * @param id
     */
    public AlertContact(Integer id, String alias, String phone) {
        super();
        this.id = id;
        this.alias = alias;
        this.phone = phone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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