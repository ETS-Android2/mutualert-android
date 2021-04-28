package com.mcuevapps.mutualert.retrofit;

import com.mcuevapps.mutualert.retrofit.response.ResponseUserAuthSuccess;

import retrofit2.Call;
import retrofit2.http.GET;

public interface AuthMutuAlertService {
    /** User Auth **/
    @GET("user/auth/token")
    Call<ResponseUserAuthSuccess> token();
}
