package com.mcuevapps.mutualert.retrofit.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AlertContact implements Parcelable {
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
     */
    public AlertContact(String alias, String phone) {
        super();
        this.alias = alias;
        this.phone = phone;
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

    protected AlertContact(Parcel in) {
        id = in.readInt();
        alias = in.readString();
        phone = in.readString();
    }

    public static final Creator<AlertContact> CREATOR = new Creator<AlertContact>() {
        @Override
        public AlertContact createFromParcel(Parcel in) {
            return new AlertContact(in);
        }

        @Override
        public AlertContact[] newArray(int size) {
            return new AlertContact[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeString(alias);
        out.writeString(phone);
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