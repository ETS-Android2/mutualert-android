package com.mcuevapps.mutualert.retrofit;

import com.google.gson.Gson;
import com.mcuevapps.mutualert.R;
import com.mcuevapps.mutualert.Service.UIService;
import com.mcuevapps.mutualert.common.Constantes;
import com.mcuevapps.mutualert.common.MyApp;
import com.mcuevapps.mutualert.retrofit.response.ResponseError;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MutuAlertClient {
    private static MutuAlertClient instance = null;
    private MutuAlertService mutuAlertService;
    private Retrofit retrofit;

    public MutuAlertClient() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Response response = chain.proceed(request);
                try {
                    if ( !response.isSuccessful() && response.code()<Constantes.HTTP_SERVER_ERROR ) {
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
        });
        OkHttpClient cliente = okHttpClientBuilder.build();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constantes.API_MUTUALERT_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(cliente)
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