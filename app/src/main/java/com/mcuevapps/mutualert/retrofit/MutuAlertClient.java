package com.mcuevapps.mutualert.retrofit;

import com.mcuevapps.mutualert.common.Constantes;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MutuAlertClient {
    private static MutuAlertClient instance = null;
    private MutuAlertService mutuAlertService;
    private Retrofit retrofit;

    public MutuAlertClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Constantes.API_MUTUALERT_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mutuAlertService = retrofit.create(MutuAlertService.class);
    }

    public static MutuAlertClient getInstance() {
        if(instance == null) {
            instance = new MutuAlertClient();
        }
        return instance;
    }

    public MutuAlertService getMutuAlertService() {
        return mutuAlertService;
    }

}