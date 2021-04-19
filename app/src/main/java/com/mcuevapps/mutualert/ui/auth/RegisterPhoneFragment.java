package com.mcuevapps.mutualert.ui.auth;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.mcuevapps.mutualert.R;
import com.mcuevapps.mutualert.common.DesignService;
import com.mcuevapps.mutualert.common.InputFilterMinMax;
import com.mcuevapps.mutualert.common.MyApp;

public class RegisterPhoneFragment extends Fragment implements View.OnClickListener, TextWatcher {

    private static final String TAG = "RegisterPhoneFragment";

    private View view;

    private boolean isNewUser = true;

    private EditText editTextPhone;
    private Button buttonContinue;

    private DesignService designService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register_phone, container, false);
        Bundle arguments = getArguments();
        if(arguments!=null) {
            isNewUser = arguments.getBoolean("isNewUser");
        }

        initUI();
        return view;
    }

    private void initUI() {
        designService = new DesignService(MyApp.getContext());

        editTextPhone = view.findViewById(R.id.editTextPhone);
        editTextPhone.setFilters(new InputFilter[]{ new InputFilterMinMax("9", "999999999")});
        editTextPhone.addTextChangedListener(this);

        buttonContinue = view.findViewById(R.id.buttonContinue);
        designService.ButtonDefaultDisable(buttonContinue);
        buttonContinue.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.buttonContinue:
                goToCode();
                break;
        }
    }

    private void goToCode() {
        String phone = editTextPhone.getText().toString();

        Bundle args = new Bundle();
        args.putString("phone", phone);
        args.putBoolean("isNewUser", isNewUser);

        Fragment fragment = new RegisterCodeFragment();
        fragment.setArguments(args);

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayoutRegister, fragment)
                //.addToBackStack(null)
                .commit();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) { }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        if( charSequence.length()==9 ) {
            designService.ButtonDefaultEnable(buttonContinue);
        }
        else {
            designService.ButtonDefaultDisable(buttonContinue);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) { }
}