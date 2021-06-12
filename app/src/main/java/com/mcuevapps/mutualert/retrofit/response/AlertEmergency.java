package com.mcuevapps.mutualert.retrofit.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mcuevapps.mutualert.model.DateObj;
import com.mcuevapps.mutualert.model.Point;

public class AlertEmergency implements Parcelable {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("apepat")
    @Expose
    private String apepat;
    @SerializedName("apemat")
    @Expose
    private String apemat;
    @SerializedName("nombres")
    @Expose
    private String nombres;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("startedAt")
    @Expose
    private DateObj startedAt;
    @SerializedName("location")
    @Expose
    private Point location;

    /**
     * No args constructor for use in serialization
     *
     */
    public AlertEmergency() {
    }

    /**
     *
     * @param apepat
     * @param apemat
     * @param phone
     * @param startedAt
     * @param location
     * @param id
     * @param avatar
     * @param nombres
     */
    public AlertEmergency(Long id, String apepat, String apemat, String nombres, String avatar, String phone, DateObj startedAt, Point location) {
        super();
        this.id = id;
        this.apepat = apepat;
        this.apemat = apemat;
        this.nombres = nombres;
        this.avatar = avatar;
        this.phone = phone;
        this.startedAt = startedAt;
        this.location = location;
    }

    protected AlertEmergency(Parcel in) {
        this.id = in.readLong();
        this.apepat = in.readString();
        this.apemat = in.readString();
        this.nombres = in.readString();
        this.avatar = in.readString();
        this.phone = in.readString();
        this.startedAt = in.readParcelable(DateObj.class.getClassLoader());
        this.location = in.readParcelable(Point.class.getClassLoader());
    }

    public static final Creator<AlertEmergency> CREATOR = new Creator<AlertEmergency>() {
        @Override
        public AlertEmergency createFromParcel(Parcel in) {
            return new AlertEmergency(in);
        }

        @Override
        public AlertEmergency[] newArray(int size) {
            return new AlertEmergency[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(apepat);
        dest.writeString(apemat);
        dest.writeString(nombres);
        dest.writeString(avatar);
        dest.writeParcelable(startedAt, flags);
        dest.writeParcelable(location, flags);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public DateObj getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(DateObj startedAt) {
        this.startedAt = startedAt;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

}