package com.mcuevapps.mutualert.ui.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mcuevapps.mutualert.R;
import com.mcuevapps.mutualert.ui.HomeActivity;
import com.mcuevapps.mutualert.ui.auth.LoginActivity;

public class RegisterInfoFragment extends Fragment implements View.OnClickListener{

    private View view;

    private boolean isNewUser = true;

    private Button buttonRegister;
    private Button buttonLogin;

    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutApellidoPaterno;
    private TextInputLayout textInputLayoutApellidoMaterno;
    private TextInputLayout textInputLayoutNombres;

    private TextInputEditText editTextPassword;
    private TextInputEditText editTextApellidoPaterno;
    private TextInputEditText editTextApellidoMaterno;
    private TextInputEditText editTextNombres;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register_info, container, false);

        Bundle arguments = getArguments();
        if(arguments!=null){
            isNewUser = arguments.getBoolean("isNewUser");
        }
        initUI();
        return view;
    }

    private void initUI() {
        buttonRegister = (Button) view.findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(this);

        buttonLogin = (Button) view.findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(this);

        editTextPassword = (TextInputEditText) view.findViewById(R.id.editTextPassword);
        editTextApellidoPaterno = (TextInputEditText) view.findViewById(R.id.editTextApellidoPaterno);
        editTextApellidoMaterno = (TextInputEditText) view.findViewById(R.id.editTextApellidoMaterno);
        editTextNombres = (TextInputEditText) view.findViewById(R.id.editTextNombres);

        if(!isNewUser){
            textInputLayoutPassword = (TextInputLayout) view.findViewById(R.id.textInputLayoutPassword);
            textInputLayoutPassword.setHint(getString(R.string.new_password));

            textInputLayoutApellidoPaterno = (TextInputLayout) view.findViewById(R.id.textInputLayoutApellidoPaterno);
            textInputLayoutApellidoPaterno.setVisibility(View.GONE);

            textInputLayoutApellidoMaterno = (TextInputLayout) view.findViewById(R.id.textInputLayoutApellidoMaterno);
            textInputLayoutApellidoMaterno.setVisibility(View.GONE);

            textInputLayoutNombres = (TextInputLayout) view.findViewById(R.id.textInputLayoutNombres);
            textInputLayoutNombres.setVisibility(View.GONE);

            buttonRegister.setText(getString(R.string.confirm));
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.buttonRegister:
                goToDashboard();
                break;
            case R.id.buttonLogin:
                goToLogin();
                break;
        }
    }

    private void goToDashboard() {
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }

    private void goToLogin() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }
}