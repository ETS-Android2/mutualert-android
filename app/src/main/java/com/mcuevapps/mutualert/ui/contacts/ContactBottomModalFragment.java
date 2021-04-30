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

public class ContactBottomModalFragment extends BottomSheetDialogFragment {

    private static final String TAG = "HomeBottomModalFragment";

    private ContactViewModel contactViewModel;
    private int idContact;

    private View view;
    private NavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            boolean success = false;

            switch (item.getItemId()) {
                case R.id.action_edit_Contact:
                    //edit();
                    success = true;
                    break;
                case R.id.action_delete_Contact:
                    contactViewModel.deleteContact(idContact);
                    success = true;
                    break;
            }

            if(success){
                getDialog().dismiss();
            }

            return success;
        }
    };

    public static ContactBottomModalFragment newInstance(int idContact) {
        ContactBottomModalFragment fragment = new ContactBottomModalFragment();
        Bundle args = new Bundle();
        args.putInt(Constantes.ARG_CONTACT_ID, idContact);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null) {
            idContact = getArguments().getInt(Constantes.ARG_CONTACT_ID);
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