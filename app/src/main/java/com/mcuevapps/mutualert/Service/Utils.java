package com.mcuevapps.mutualert.Service;

import android.content.Intent;
import android.text.TextUtils;

import com.mcuevapps.mutualert.common.Constantes;
import com.mcuevapps.mutualert.common.MyApp;
import com.mcuevapps.mutualert.common.SharedPreferencesManager;
import com.mcuevapps.mutualert.retrofit.response.UserAuthSuccess;
import com.mcuevapps.mutualert.ui.HomeActivity;
import com.mcuevapps.mutualert.ui.auth.LoginActivity;

public class Utils {
    public static void saveDataLogin(UserAuthSuccess data){
        SharedPreferencesManager.setSomeStringValue(Constantes.PREF_TOKEN, data.getToken());
        SharedPreferencesManager.setSomeStringValue(Constantes.PREF_APELLIDOPAT, data.getProfile().getApepat());
        SharedPreferencesManager.setSomeStringValue(Constantes.PREF_APELLIDOMAT, data.getProfile().getApemat());
        SharedPreferencesManager.setSomeStringValue(Constantes.PREF_NOMBRES, data.getProfile().getNombres());
        SharedPreferencesManager.setSomeStringValue(Constantes.PREF_EMAIL, data.getProfile().getEmail());
        SharedPreferencesManager.setSomeStringValue(Constantes.PREF_AVATAR, data.getProfile().getAvatar());
    }

    public static void removeDataLogin(){
        String username = SharedPreferencesManager.getSomeStringValue(Constantes.PREF_USERNAME);
        SharedPreferencesManager.deleteAllValues();
        if ( !TextUtils.isEmpty(username) ) {
            SharedPreferencesManager.setSomeStringValue(Constantes.PREF_USERNAME, username);
        }
    }

    public static void goToLogin(){
        Intent intent = new Intent(MyApp.getContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        MyApp.getContext().startActivity(intent);
    }

    public static void goToHome(){
        Intent intent = new Intent(MyApp.getContext(), HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        MyApp.getContext().startActivity(intent);
    }

    public static boolean contactValid(String alias, String phone){
        String phoneRegex = "9{1,1}[0-9]{8,8}";

        if( alias.length() < Constantes.PERSON_NAME_LENGTH )
            return false;

        if( !phone.matches(phoneRegex) )
            return false;

        return true;
    }
}
