package com.mcuevapps.mutualert.common;

public class Constantes {

    public static final String API_MUTUALERT_BASE_URL = "http://192.168.1.5/mutualert/public/index.php/";

    // Preferences
    public static final String PREF_TOKEN = "PREF_TOKEN";
    public static final String PREF_USERNAME = "PREF_USERNAME";
    public static final String PREF_APELLIDOPAT = "PREF_APELLIDOPAT";
    public static final String PREF_APELLIDOMAT = "PREF_APELLIDOMAT";
    public static final String PREF_NOMBRES = "PREF_NOMBRES";
    public static final String PREF_EMAIL = "PREF_EMAIL";
    public static final String PREF_AVATAR = "PREF_AVATAR";

    // Arguments
    public static final int PHONE_LENGTH = 9;
    public static final int CODE_LENGTH = 6;
    public static final int CODE_RESEND_TIME = 60;
    public static final int PERSON_NAME_LENGTH = 4;
    public static final int PASSWORD_LENGTH = 6;

    // Http Code
    public static final int HTTP_UNAUTHORIZED = 401;
    public static final int HTTP_FORBIDDEN = 403;
    public static final int HTTP_SERVER_ERROR = 500;

    // Arguments
    public static final String ARG_CONTACT_ID = "CONTACT_ID";
}
