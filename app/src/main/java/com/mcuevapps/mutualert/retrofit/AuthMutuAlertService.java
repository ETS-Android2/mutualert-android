package com.mcuevapps.mutualert.retrofit;

import com.mcuevapps.mutualert.retrofit.response.ResponseAlertContactList;
import com.mcuevapps.mutualert.retrofit.response.ResponseSuccess;
import com.mcuevapps.mutualert.retrofit.response.ResponseUserAuthSuccess;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AuthMutuAlertService {
    /** User Auth **/
    @GET("user/auth/token")
    Call<ResponseUserAuthSuccess> token();

    /** Alert Contact **/
    @GET("alert/contact/l")
    Call<ResponseAlertContactList> getAllContacts();

    @DELETE("alert/contact/d/{idContact}")
    Call<ResponseSuccess> deleteContact(@Path("idContact") int idContact);
}
