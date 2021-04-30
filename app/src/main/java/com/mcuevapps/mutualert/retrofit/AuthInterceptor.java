package com.mcuevapps.mutualert.retrofit;

import android.widget.Toast;

import com.google.gson.Gson;
import com.mcuevapps.mutualert.R;
import com.mcuevapps.mutualert.Service.Utils;
import com.mcuevapps.mutualert.common.Constantes;
import com.mcuevapps.mutualert.common.MyApp;
import com.mcuevapps.mutualert.common.SharedPreferencesManager;
import com.mcuevapps.mutualert.retrofit.response.ResponseError;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor{
    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = SharedPreferencesManager.getSomeStringValue(Constantes.PREF_TOKEN);
        Request request = chain.request().newBuilder().addHeader("token", token).build();
        Response response = chain.proceed(request);
        if (response.code()==Constantes.HTTP_UNAUTHORIZED || response.code()==Constantes.HTTP_FORBIDDEN ) {
            Utils.removeDataLogin();
            Utils.goToLogin();
        } else if ( !response.isSuccessful() && response.code()<Constantes.HTTP_SERVER_ERROR ) {
            ResponseError error = new Gson().fromJson(response.body().string(), ResponseError.class);
            Toast.makeText(MyApp.getContext(), error.getMsg(), Toast.LENGTH_SHORT).show();
        } else if ( response.code()>=Constantes.HTTP_SERVER_ERROR ){
            Toast.makeText(MyApp.getContext(), MyApp.getInstance().getString(R.string.error_response), Toast.LENGTH_SHORT).show();
        }
        return response;
    }
}
