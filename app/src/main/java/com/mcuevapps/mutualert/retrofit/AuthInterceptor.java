package com.mcuevapps.mutualert.retrofit;

import com.google.gson.Gson;
import com.mcuevapps.mutualert.R;
import com.mcuevapps.mutualert.Service.UIService;
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
        try {
            if (response.code()==Constantes.HTTP_UNAUTHORIZED || response.code()==Constantes.HTTP_FORBIDDEN ) {
                Utils.removeDataLogin();
                Utils.goToLogin();
            } else if ( !response.isSuccessful() && response.code()<Constantes.HTTP_SERVER_ERROR ) {
                ResponseError error = new Gson().fromJson(response.body().string(), ResponseError.class);
                UIService.showEventToast(UIService.TOAST_ERROR, error.getMsg());
            } else if ( response.code()>=Constantes.HTTP_SERVER_ERROR ){
                UIService.showEventToast(UIService.TOAST_ERROR, MyApp.getInstance().getString(R.string.error_response));
            }
        } catch (Exception e) {
            UIService.showEventToast(UIService.TOAST_ERROR, MyApp.getInstance().getString(R.string.error_network));
        }
        return response;
    }
}
