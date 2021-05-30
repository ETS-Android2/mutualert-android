package com.mcuevapps.mutualert.ui.auth;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.mcuevapps.mutualert.R;
import com.mcuevapps.mutualert.Service.UIService;
import com.mcuevapps.mutualert.common.Constantes;
import com.mcuevapps.mutualert.common.InputFilterMinMax;
import com.mcuevapps.mutualert.common.SharedPreferencesManager;
import com.mcuevapps.mutualert.retrofit.MutuAlertClient;
import com.mcuevapps.mutualert.retrofit.MutuAlertService;
import com.mcuevapps.mutualert.retrofit.request.RequestUserAccountExist;
import com.mcuevapps.mutualert.retrofit.response.ResponseSuccessBoolean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterPhoneFragment extends Fragment implements View.OnClickListener, TextWatcher {

    private static final String TAG = RegisterPhoneFragment.class.getSimpleName();

    private View view;

    private boolean isNewUser;

    private EditText editTextPhone;
    private Button buttonContinue;

    private MutuAlertClient mutuAlertClient;
    private MutuAlertService mutuAlertService;

    public static RegisterPhoneFragment newInstance(Boolean isNewUser) {
        RegisterPhoneFragment fragment = new RegisterPhoneFragment();
        Bundle args = new Bundle();
        args.putBoolean(Constantes.ARG_NEW_USER, isNewUser);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isNewUser = getArguments().getBoolean(Constantes.ARG_NEW_USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register_phone, container, false);

        retrofitInit();
        initUI();
        return view;
    }

    private void retrofitInit() {
        mutuAlertClient = MutuAlertClient.getInstance();
        mutuAlertService = mutuAlertClient.getMutuAlertService();
    }

    private void initUI() {
        editTextPhone = view.findViewById(R.id.editTextPhone);
        editTextPhone.setFilters(new InputFilter[]{ new InputFilterMinMax("9", "999999999")});
        editTextPhone.addTextChangedListener(this);

        buttonContinue = view.findViewById(R.id.buttonContinue);
        UIService.ButtonDisable(UIService.BUTTON_PRIMARY, buttonContinue);
        buttonContinue.setOnClickListener(this);

        setCredentialsIfExist();
    }

    private void setCredentialsIfExist() {
        String username = SharedPreferencesManager.getSomeStringValue(Constantes.PREF_USERNAME);
        if ( !TextUtils.isEmpty(username) ) {
            editTextPhone.setText(username);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.buttonContinue:
                existUser();
                break;
        }
    }

    private void existUser() {
        RequestUserAccountExist requestUserAccountExist = new RequestUserAccountExist(editTextPhone.getText().toString());
        Call<ResponseSuccessBoolean> call = mutuAlertService.exist(requestUserAccountExist);
        call.enqueue(new Callback<ResponseSuccessBoolean>() {
            @Override
            public void onResponse(Call<ResponseSuccessBoolean> call, Response<ResponseSuccessBoolean> response) {
                if( response.isSuccessful() ){
                    if( response.body().getData()!=isNewUser ) {
                        goToCode();
                    } else {
                        UIService.showEventToast(UIService.TOAST_WARNING, (isNewUser ? getString(R.string.phone_exist) : getString(R.string.phone_not_exist)));
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseSuccessBoolean> call, Throwable t) { }
        });
    }

    private void goToCode() {
        RegisterCodeFragment fragment = RegisterCodeFragment.newInstance(isNewUser, editTextPhone.getText().toString());
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
        if( charSequence.length() != Constantes.PHONE_LENGTH ) {
            UIService.ButtonDisable(UIService.BUTTON_PRIMARY, buttonContinue);
        } else {
            UIService.ButtonEnable(UIService.BUTTON_PRIMARY, buttonContinue);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) { }
}