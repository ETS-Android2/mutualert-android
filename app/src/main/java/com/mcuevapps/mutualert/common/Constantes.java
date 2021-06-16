package com.mcuevapps.mutualert.common;

public class Constantes {

    // Preferences
    public static final String PREF_TOKEN = "PREF_TOKEN";
    public static final String PREF_ALERT_API = "PREF_ALERT_API";
    public static final String PREF_USERNAME = "PREF_USERNAME";
    public static final String PREF_APELLIDOPAT = "PREF_APELLIDOPAT";
    public static final String PREF_APELLIDOMAT = "PREF_APELLIDOMAT";
    public static final String PREF_NOMBRES = "PREF_NOMBRES";
    public static final String PREF_EMAIL = "PREF_EMAIL";
    public static final String PREF_AVATAR = "PREF_AVATAR";
    public static final String PREF_ALERT_APP = "PREF_ALERT_APP";

    // CONFIG
    public static final int PHONE_LENGTH = 9;
    public static final int CODE_LENGTH = 6;
    public static final int CODE_RESEND_TIME = 60;
    public static final int PERSON_NAME_LENGTH = 4;
    public static final int PASSWORD_LENGTH = 6;
    public static final int CONTACT_LENGTH = 4;

    public static final int LOCATION_UPDATE_INTERVAL = 60 * 1000;
    public static final int LOCATION_FASTEST_UPDATE_INTERVAL = LOCATION_UPDATE_INTERVAL / 2;
    public static final int LOCATION_MAX_WAIT_TIME = LOCATION_UPDATE_INTERVAL * 5;;

    // Http Code
    public static final int HTTP_UNAUTHORIZED = 401;
    public static final int HTTP_FORBIDDEN = 403;
    public static final int HTTP_SERVER_ERROR = 500;

    // Arguments
    public static final String ARG_NEW_USER = "NEW_USER";
    public static final String ARG_PHONE = "PHONE";
    public static final String ARG_CODE = "CODE";
    public static final String ARG_ALERT_CONTACT = "ALERT_CONTACT";

    // Key
    public static final String KEY_CONTACT = "$CONTACT$";

    // Socket
    public static final String SOCKET_EVENT = "event";
    public static final String EVENT_EMERGENCY_INIT = "EMERGENCY_INIT";
    public static final String EVENT_EMERGENCY_UPDATE= "EMERGENCY_UPDATE";
    public static final String EVENT_EMERGENCY_END = "EMERGENCY_END";
}
