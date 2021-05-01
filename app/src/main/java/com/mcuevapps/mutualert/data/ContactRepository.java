package com.mcuevapps.mutualert.data;

import androidx.lifecycle.MutableLiveData;

import com.mcuevapps.mutualert.R;
import com.mcuevapps.mutualert.Service.UIService;
import com.mcuevapps.mutualert.common.MyApp;
import com.mcuevapps.mutualert.retrofit.AuthMutuAlertClient;
import com.mcuevapps.mutualert.retrofit.AuthMutuAlertService;
import com.mcuevapps.mutualert.retrofit.response.AlertContact;
import com.mcuevapps.mutualert.retrofit.response.ResponseAlertContactList;
import com.mcuevapps.mutualert.retrofit.response.ResponseSuccess;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactRepository {
    AuthMutuAlertService authMutuAlertService;
    AuthMutuAlertClient authMutuAlertClient;
    MutableLiveData<List<AlertContact>> allContacts;

    ContactRepository() {
        authMutuAlertClient = AuthMutuAlertClient.getInstance();
        authMutuAlertService = authMutuAlertClient.getAuthMutuAlertService();
        allContacts = getAllContacts();
    }

    public MutableLiveData<List<AlertContact>> getAllContacts() {
        if(allContacts == null) {
            allContacts = new MutableLiveData<>();
        }

        Call<ResponseAlertContactList> call = authMutuAlertService.getAllContacts();
        call.enqueue(new Callback<ResponseAlertContactList>() {
            @Override
            public void onResponse(Call<ResponseAlertContactList> call, Response<ResponseAlertContactList> response) {
                if(response.isSuccessful()) {
                    allContacts.setValue(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<ResponseAlertContactList> call, Throwable t) {
                UIService.showEventToast(UIService.TOAST_ERROR, MyApp.getInstance().getString(R.string.error_network));
            }
        });

        return allContacts;
    }

    public void deleteContact(final int idContact) {
        Call<ResponseSuccess> call = authMutuAlertService.deleteContact(idContact);

        call.enqueue(new Callback<ResponseSuccess>() {
            @Override
            public void onResponse(Call<ResponseSuccess> call, Response<ResponseSuccess> response) {
                if(response.isSuccessful()) {
                    List<AlertContact> clonedContacts = new ArrayList<>();
                    for(int i=0; i < allContacts.getValue().size(); i++) {
                        if(allContacts.getValue().get(i).getId() != idContact) {
                            clonedContacts.add(new AlertContact(allContacts.getValue().get(i)));
                        }
                    }

                    allContacts.setValue(clonedContacts);
                    //getAllContacts();
                    UIService.showEventToast(UIService.TOAST_SUCCESS, MyApp.getInstance().getString(R.string.success_delete_contact));
                }
            }

            @Override
            public void onFailure(Call<ResponseSuccess> call, Throwable t) {
                UIService.showEventToast(UIService.TOAST_ERROR, MyApp.getInstance().getString(R.string.error_network));
            }
        });
    }
}
