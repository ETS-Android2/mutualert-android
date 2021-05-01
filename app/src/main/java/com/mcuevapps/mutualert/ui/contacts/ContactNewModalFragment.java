package com.mcuevapps.mutualert.ui.contacts;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mcuevapps.mutualert.R;

public class ContactNewModalFragment extends DialogFragment implements View.OnClickListener{

    private static final String TAG = "ContactNewModalFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_new_modal, container, false);

        return view;
    }

    @Override
    public void onClick(View v) {

    }
}