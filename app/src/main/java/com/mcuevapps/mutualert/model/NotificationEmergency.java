package com.mcuevapps.mutualert.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationEmergency {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("nombres")
    @Expose
    private String nombres;
    @SerializedName("apepat")
    @Expose
    private String apepat;
    @SerializedName("apemat")
    @Expose
    private String apemat;

    /**
     * No args constructor for use in serialization
     *
     */
    public NotificationEmergency() {
    }

    /**
     *
     * @param apepat
     * @param apemat
     * @param id
     * @param nombres
     */
    public NotificationEmergency(Integer id, String nombres, String apepat, String apemat) {
        super();
        this.id = id;
        this.nombres = nombres;
        this.apepat = apepat;
        this.apemat = apemat;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApepat() {
        return apepat;
    }

    public void setApepat(String apepat) {
        this.apepat = apepat;
    }

    public String getApemat() {
        return apemat;
    }

    public void setApemat(String apemat) {
        this.apemat = apemat;
    }

}