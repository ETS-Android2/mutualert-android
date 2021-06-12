package com.mcuevapps.mutualert.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DateObj implements Parcelable {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("timezone_type")
    @Expose
    private Integer timezoneType;
    @SerializedName("timezone")
    @Expose
    private String timezone;

    /**
     * No args constructor for use in serialization
     *
     */
    public DateObj() {
    }

    /**
     *
     * @param date
     * @param timezoneType
     * @param timezone
     */
    public DateObj(String date, Integer timezoneType, String timezone) {
        super();
        this.date = date;
        this.timezoneType = timezoneType;
        this.timezone = timezone;
    }

    protected DateObj(Parcel in) {
        this.date = in.readString();
        this.timezoneType = in.readInt();
        this.timezone = in.readString();
    }

    public static final Creator<DateObj> CREATOR = new Creator<DateObj>() {
        @Override
        public DateObj createFromParcel(Parcel in) {
            return new DateObj(in);
        }

        @Override
        public DateObj[] newArray(int size) {
            return new DateObj[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeInt(timezoneType);
        dest.writeString(timezone);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getTimezoneType() {
        return timezoneType;
    }

    public void setTimezoneType(Integer timezoneType) {
        this.timezoneType = timezoneType;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

}