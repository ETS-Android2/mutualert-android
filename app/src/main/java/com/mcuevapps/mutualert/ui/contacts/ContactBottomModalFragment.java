package com.mcuevapps.mutualert.ui.contacts;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;

import com.mcuevapps.mutualert.R;
import com.mcuevapps.mutualert.common.Constantes;
import com.mcuevapps.mutualert.data.ContactViewModel;
import com.mcuevapps.mutualert.retrofit.response.AlertContact;

public class ContactBottomModalFragment extends BottomSheetDialogFragment {

    private static final String TAG = ContactBottomModalFragment.class.getSimpleName();

    private ContactViewModel contactViewModel;
    private AlertContact contact;

    private View view;
    private NavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            boolean success = false;

            switch (item.getItemId()) {
                case R.id.action_edit_Contact:
                    contactViewModel.editContact(getContext(), contact);
                    success = true;
                    break;
                case R.id.action_delete_Contact:
                    contactViewModel.deleteContact(getContext(), contact);
                    success = true;
                    break;
            }

            if(success){
                getDialog().dismiss();
            }

            return success;
        }
    };

    public static ContactBottomModalFragment newInstance(AlertContact contact) {
        ContactBottomModalFragment fragment = new ContactBottomModalFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constantes.ARG_ALERT_CONTACT, contact);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            contact = getArguments().getParcelable(Constantes.ARG_ALERT_CONTACT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_contact_bottom_modal, container, false);

        initUI();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        contactViewModel = new ViewModelProvider(getActivity()).get(ContactViewModel.class);
    }

    private void initUI() {
        final NavigationView nav = view.findViewById(R.id.navigationViewContact);
        nav.setNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
}