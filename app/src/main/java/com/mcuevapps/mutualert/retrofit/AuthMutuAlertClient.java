package com.mcuevapps.mutualert.retrofit;

import com.mcuevapps.mutualert.BuildConfig;
import com.mcuevapps.mutualert.common.Constantes;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthMutuAlertClient {
    private static AuthMutuAlertClient instance = null;
    private AuthMutuAlertService authMutuAlertService;
    private Retrofit retrofit;

    public AuthMutuAlertClient() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.addInterceptor(new AuthInterceptor());
        OkHttpClient cliente = okHttpClientBuilder.build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(cliente)
                .build();

        authMutuAlertService = retrofit.create(AuthMutuAlertService.class);
    }

    public static AuthMutuAlertClient getInstance() {
        if(instance == null) {
            instance = new AuthMutuAlertClient();
        }
        return instance;
    }

    public AuthMutuAlertService getAuthMutuAlertService() {
        return authMutuAlertService;
    }

}
