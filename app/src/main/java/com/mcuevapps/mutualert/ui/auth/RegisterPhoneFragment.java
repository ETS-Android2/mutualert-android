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
import android.widget.Toast;

import com.mcuevapps.mutualert.R;
import com.mcuevapps.mutualert.common.Constantes;
import com.mcuevapps.mutualert.common.DesignService;
import com.mcuevapps.mutualert.common.InputFilterMinMax;
import com.mcuevapps.mutualert.common.MyApp;
import com.mcuevapps.mutualert.common.ToastService;
import com.mcuevapps.mutualert.retrofit.MutuAlertClient;
import com.mcuevapps.mutualert.retrofit.MutuAlertService;
import com.mcuevapps.mutualert.retrofit.request.RequestUserAccountExist;
import com.mcuevapps.mutualert.retrofit.response.ResponseSuccessBoolean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterPhoneFragment extends Fragment implements View.OnClickListener, TextWatcher {

    private static final String TAG = "RegisterPhoneFragment";

    private View view;

    private boolean isNewUser = true;

    private EditText editTextPhone;
    private Button buttonContinue;

    private DesignService designService;
    private MutuAlertClient mutuAlertClient;
    private MutuAlertService mutuAlertService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register_phone, container, false);

        Bundle arguments = getArguments();
        if(arguments!=null) {
            isNewUser = arguments.getBoolean("isNewUser");
        }

        retrofitInit();
        initUI();
        return view;
    }

    private void retrofitInit() {
        mutuAlertClient = MutuAlertClient.getInstance();
        mutuAlertService = mutuAlertClient.getMutuAlertService();
    }

    private void initUI() {
        designService = new DesignService(MyApp.getContext());

        editTextPhone = view.findViewById(R.id.editTextPhone);
        editTextPhone.setFilters(new InputFilter[]{ new InputFilterMinMax("9", "999999999")});
        editTextPhone.addTextChangedListener(this);

        buttonContinue = view.findViewById(R.id.buttonContinue);
        designService.ButtonPrimaryDisable(buttonContinue);
        buttonContinue.setOnClickListener(this);
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
                        Toast.makeText(MyApp.getContext(), (isNewUser ? getString(R.string.phone_exist) : getString(R.string.phone_not_exist)), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    ToastService.showErrorResponse(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ResponseSuccessBoolean> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), getString(R.string.error_network), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goToCode() {
        Bundle args = new Bundle();
        args.putString("phone", editTextPhone.getText().toString());
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
        if( charSequence.length() != Constantes.PHONE_LENGTH ) {
            designService.ButtonPrimaryDisable(buttonContinue);
        } else {
            designService.ButtonPrimaryEnable(buttonContinue);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) { }
}