package com.mcuevapps.mutualert.retrofit;

import android.content.res.Resources;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mcuevapps.mutualert.R;
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
                if ( !response.isSuccessful() && response.code()<Constantes.HTTP_SERVER_ERROR ) {
                    ResponseError error = new Gson().fromJson(response.body().string(), ResponseError.class);
                    Toast.makeText(MyApp.getContext(), error.getMsg(), Toast.LENGTH_SHORT).show();
                } else if ( response.code()>=Constantes.HTTP_SERVER_ERROR ){
                    Toast.makeText(MyApp.getContext(), Resources.getSystem().getString(R.string.error_response), Toast.LENGTH_SHORT).show();
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