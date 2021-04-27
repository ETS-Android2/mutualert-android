package com.mcuevapps.mutualert.retrofit;

import com.mcuevapps.mutualert.retrofit.request.RequestUserAccountExist;
import com.mcuevapps.mutualert.retrofit.request.RequestUserAccountCheckcode;
import com.mcuevapps.mutualert.retrofit.request.RequestUserAuthLogin;
import com.mcuevapps.mutualert.retrofit.request.RequestUserAccountNewpassword;
import com.mcuevapps.mutualert.retrofit.request.RequestUserAuthSignup;
import com.mcuevapps.mutualert.retrofit.response.ResponseSuccess;
import com.mcuevapps.mutualert.retrofit.response.ResponseSuccessBoolean;
import com.mcuevapps.mutualert.retrofit.response.ResponseUserAuthSuccess;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MutuAlertService {
    /** User Auth **/
    @POST("user/auth/login")
    Call<ResponseUserAuthSuccess> login(@Body RequestUserAuthLogin requestUserAuthLogin);

    @POST("user/auth/signup")
    Call<ResponseUserAuthSuccess> signUp(@Body RequestUserAuthSignup requestUserAuthSignup);

    /** User Account **/
    @POST("user/account/exist")
    Call<ResponseSuccessBoolean> exist(@Body RequestUserAccountExist requestUserAccountExist);

    @POST("user/account/checkcode")
    Call<ResponseSuccess> checkCode(@Body RequestUserAccountCheckcode requestUserAccountCheckcode);

    @POST("user/account/npwd")
    Call<ResponseSuccess> newPassword(@Body RequestUserAccountNewpassword requestUserAccountNewpassword);
}