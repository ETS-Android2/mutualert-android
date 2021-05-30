package com.mcuevapps.mutualert.data;

import androidx.lifecycle.MutableLiveData;

import com.mcuevapps.mutualert.R;
import com.mcuevapps.mutualert.Service.UIService;
import com.mcuevapps.mutualert.common.MyApp;
import com.mcuevapps.mutualert.retrofit.AuthMutuAlertClient;
import com.mcuevapps.mutualert.retrofit.AuthMutuAlertService;
import com.mcuevapps.mutualert.retrofit.request.RequestAlertContact;
import com.mcuevapps.mutualert.retrofit.response.AlertContact;
import com.mcuevapps.mutualert.retrofit.response.ResponseAlertContact;
import com.mcuevapps.mutualert.retrofit.response.ResponseAlertContactList;
import com.mcuevapps.mutualert.retrofit.response.ResponseSuccess;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactRepository {

    private static final String TAG = ContactRepository.class.getSimpleName();

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
            public void onFailure(Call<ResponseAlertContactList> call, Throwable t) { }
        });

        return allContacts;
    }

    public void createContact(final AlertContact contact) {
        RequestAlertContact requestAlertContact = new RequestAlertContact(contact.getAlias(), contact.getPhone());
        Call<ResponseAlertContact> call = authMutuAlertService.createContact(requestAlertContact);

        call.enqueue(new Callback<ResponseAlertContact>() {
            @Override
            public void onResponse(Call<ResponseAlertContact> call, Response<ResponseAlertContact> response) {
                if(response.isSuccessful()) {
                    List<AlertContact> clonedContacts = new ArrayList<>();
                    for (int i = 0; i < allContacts.getValue().size(); i++) {
                        clonedContacts.add(new AlertContact(allContacts.getValue().get(i)));
                    }
                    clonedContacts.add(response.body().getData());
                    allContacts.setValue(clonedContacts);
                    //getAllContacts();
                    UIService.showEventToast(UIService.TOAST_SUCCESS, MyApp.getInstance().getString(R.string.success_create_contact));
                }
            }

            @Override
            public void onFailure(Call<ResponseAlertContact> call, Throwable t) { }
        });
    }

    public void editContact(final AlertContact contact) {
        RequestAlertContact requestAlertContact = new RequestAlertContact(contact.getAlias(), contact.getPhone());
        Call<ResponseSuccess> call = authMutuAlertService.updateContact(contact.getId(), requestAlertContact);

        call.enqueue(new Callback<ResponseSuccess>() {
            @Override
            public void onResponse(Call<ResponseSuccess> call, Response<ResponseSuccess> response) {
                if(response.isSuccessful()) {
                    List<AlertContact> clonedContacts = new ArrayList<>();
                    for(int i=0; i < allContacts.getValue().size(); i++) {
                        if(allContacts.getValue().get(i).getId() != contact.getId()) {
                            clonedContacts.add(new AlertContact(allContacts.getValue().get(i)));
                        } else {
                            clonedContacts.add(new AlertContact(contact));
                        }
                    }
                    allContacts.setValue(clonedContacts);
                    UIService.showEventToast(UIService.TOAST_SUCCESS, MyApp.getInstance().getString(R.string.success_update_contact));
                }
            }

            @Override
            public void onFailure(Call<ResponseSuccess> call, Throwable t) { }
        });
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
                    UIService.showEventToast(UIService.TOAST_SUCCESS, MyApp.getInstance().getString(R.string.success_delete_contact));
                }
            }

            @Override
            public void onFailure(Call<ResponseSuccess> call, Throwable t) { }
        });
    }
}
