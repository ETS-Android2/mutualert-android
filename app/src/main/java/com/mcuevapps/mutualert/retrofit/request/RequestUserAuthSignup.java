package com.mcuevapps.mutualert.retrofit.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestUserAuthSignup {

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("apepat")
    @Expose
    private String apepat;
    @SerializedName("apemat")
    @Expose
    private String apemat;
    @SerializedName("nombres")
    @Expose
    private String nombres;

    /**
     * No args constructor for use in serialization
     *
     */
    public RequestUserAuthSignup() {
    }

    /**
     *
     * @param password
     * @param apepat
     * @param code
     * @param apemat
     * @param username
     * @param nombres
     */
    public RequestUserAuthSignup(String username, String code, String password, String apepat, String apemat, String nombres) {
        super();
        this.username = username;
        this.code = code;
        this.password = password;
        this.apepat = apepat;
        this.apemat = apemat;
        this.nombres = nombres;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

}