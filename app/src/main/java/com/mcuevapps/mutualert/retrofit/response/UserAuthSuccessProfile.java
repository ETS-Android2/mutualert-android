package com.mcuevapps.mutualert.retrofit.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserAuthSuccessProfile {

    @SerializedName("apepat")
    @Expose
    private String apepat;
    @SerializedName("apemat")
    @Expose
    private String apemat;
    @SerializedName("nombres")
    @Expose
    private String nombres;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("avatar")
    @Expose
    private String avatar;

    /**
     * No args constructor for use in serialization
     *
     */
    public UserAuthSuccessProfile() {
    }

    /**
     *
     * @param apepat
     * @param apemat
     * @param avatar
     * @param email
     * @param nombres
     */
    public UserAuthSuccessProfile(String apepat, String apemat, String nombres, String email, String avatar) {
        super();
        this.apepat = apepat;
        this.apemat = apemat;
        this.nombres = nombres;
        this.email = email;
        this.avatar = avatar;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

}