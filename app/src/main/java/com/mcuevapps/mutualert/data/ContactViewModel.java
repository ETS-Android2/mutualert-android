package com.mcuevapps.mutualert.data;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mcuevapps.mutualert.retrofit.response.AlertContact;
import com.mcuevapps.mutualert.ui.contacts.ContactBottomModalFragment;

import java.util.List;

public class ContactViewModel extends AndroidViewModel {
    private ContactRepository contactRepository;
    private LiveData<List<AlertContact>> contacts;

    public ContactViewModel(@NonNull Application application) {
        super(application);
        contactRepository = new ContactRepository();
        contacts = contactRepository.getAllContacts();
    }

    public LiveData<List<AlertContact>> getContacts() { return contacts; }

    public LiveData<List<AlertContact>> getNewContacts() {
        contacts = contactRepository.getAllContacts();
        return contacts;
    }

    public void openDialogContactMenu(Context ctx, int idContact) {
        ContactBottomModalFragment dialogContact = ContactBottomModalFragment.newInstance(idContact);
        dialogContact.show(((AppCompatActivity)ctx).getSupportFragmentManager(), "ContactBottomModalFragment");
    }

    public void deleteContact(int idContact) {
        contactRepository.deleteContact(idContact);
    }
}
