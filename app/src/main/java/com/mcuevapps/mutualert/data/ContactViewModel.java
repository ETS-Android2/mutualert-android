package com.mcuevapps.mutualert.data;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mcuevapps.mutualert.R;
import com.mcuevapps.mutualert.Service.UIService;
import com.mcuevapps.mutualert.Service.Utils;
import com.mcuevapps.mutualert.common.Constantes;
import com.mcuevapps.mutualert.common.MyApp;
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

    public void openDialogContactMenu(Context ctx, AlertContact contact) {
        ContactBottomModalFragment dialogContact = ContactBottomModalFragment.newInstance(contact);
        dialogContact.show(((AppCompatActivity)ctx).getSupportFragmentManager(), "ContactBottomModalFragment");
    }

    public void createContact(Context ctx) {
        showDialogContact(ctx, true, null);
    }

    public void editContact(Context ctx, AlertContact contact) {
        showDialogContact(ctx, false, contact);
    }

    private void showDialogContact(Context ctx, boolean isCreate, @Nullable AlertContact contact){
        final Dialog dialog = new Dialog(ctx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.ui_dialog_contact_cu);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        if (dialog.getWindow() != null) {
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

            EditText editTextAlias = dialog.findViewById(R.id.editTextAlias);
            EditText editTextPhone = dialog.findViewById(R.id.editTextPhone);
            if(!isCreate){
                editTextAlias.setText(contact.getAlias());
                editTextPhone.setText(contact.getPhone());
            }

            Button positiveButton = dialog.findViewById(R.id.ButtonPositive);
            positiveButton.setOnClickListener((View v) -> {
                String alias = editTextAlias.getText().toString();
                String phone = editTextPhone.getText().toString();

                if (Utils.contactValid(alias, phone)) {
                    if(isCreate){
                        contactRepository.createContact(new AlertContact(alias, phone));
                    } else {
                        contactRepository.editContact(new AlertContact(contact.getId(), alias, phone));
                    }
                    dialog.cancel();
                } else {
                    UIService.showEventToast(UIService.TOAST_WARNING, MyApp.getInstance().getString(R.string.form_invalid));
                }
            });
            dialog.show();
            dialog.getWindow().setAttributes(lp);
        }
    }

    public void deleteContact(Context ctx, AlertContact contact) {
        UIService.showDialogConfirm(success -> {
            if(success){
                contactRepository.deleteContact(contact.getId());
            }
        }, ctx, MyApp.getInstance().getString(R.string.contact_delete_title), MyApp.getInstance().getString(R.string.contact_delete_message).replace(Constantes.KEY_CONTACT, contact.getAlias()));
    }
}
