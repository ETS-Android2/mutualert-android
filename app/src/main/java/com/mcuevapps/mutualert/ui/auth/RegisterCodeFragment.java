package com.mcuevapps.mutualert.ui.auth;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mcuevapps.mutualert.R;

public class RegisterCodeFragment extends Fragment implements View.OnClickListener{

    private View view;

    private boolean isNewUser = true;

    private Button buttonContinue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register_code, container, false);

        Bundle arguments = getArguments();
        if(arguments!=null) {
            isNewUser = arguments.getBoolean("isNewUser");
        }
        initUI();
        return view;
    }

    private void initUI() {
        buttonContinue = (Button) view.findViewById(R.id.buttonContinue);
        buttonContinue.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.buttonContinue:
                goToInfo();
                break;
        }
    }

    private void goToInfo() {
        Bundle args = new Bundle();
        args.putBoolean("isNewUser", isNewUser);

        Fragment fragment = new RegisterInfoFragment();
        fragment.setArguments(args);

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayoutRegister, fragment)
                //.addToBackStack(null)
                .commit();
    }
}