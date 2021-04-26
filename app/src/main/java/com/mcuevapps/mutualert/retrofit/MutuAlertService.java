package com.mcuevapps.mutualert.retrofit;

import com.mcuevapps.mutualert.retrofit.request.RequestUserAccountExist;
import com.mcuevapps.mutualert.retrofit.request.RequestUserAuthCheckcode;
import com.mcuevapps.mutualert.retrofit.request.RequestUserAuthLogin;
import com.mcuevapps.mutualert.retrofit.request.RequestUserAuthNewpassword;
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

    @POST("user/auth/checkcode")
    Call<ResponseSuccess> checkCode(@Body RequestUserAuthCheckcode requestUserAuthCheckcode);

    @POST("user/auth/npwd")
    Call<ResponseSuccess> newPassword(@Body RequestUserAuthNewpassword requestUserAuthNewpassword);

    /** User Account **/
    @POST("user/account/exist")
    Call<ResponseSuccessBoolean> exist(@Body RequestUserAccountExist requestUserAccountExist);
}