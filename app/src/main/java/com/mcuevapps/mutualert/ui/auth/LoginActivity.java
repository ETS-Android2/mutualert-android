package com.mcuevapps.mutualert.ui.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.mcuevapps.mutualert.R;
import com.mcuevapps.mutualert.Service.UIService;
import com.mcuevapps.mutualert.Service.Utils;
import com.mcuevapps.mutualert.common.Constantes;
import com.mcuevapps.mutualert.common.SharedPreferencesManager;
import com.mcuevapps.mutualert.retrofit.MutuAlertClient;
import com.mcuevapps.mutualert.retrofit.MutuAlertService;
import com.mcuevapps.mutualert.retrofit.request.RequestUserAuthLogin;
import com.mcuevapps.mutualert.retrofit.response.ResponseSuccess;
import com.mcuevapps.mutualert.retrofit.response.ResponseUserAuthSuccess;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private TextInputEditText editTextPhone;
    private TextInputEditText editTextPassword;
    private Button buttonLogin;
    private Button buttonCreateAccount;
    private Button buttonForgotPassword;

    private MutuAlertClient mutuAlertClient;
    private MutuAlertService mutuAlertService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (savedInstanceState == null) {
            retrofitInit();
            initUI();
        }
    }

    private void retrofitInit() {
        mutuAlertClient = MutuAlertClient.getInstance();
        mutuAlertService = mutuAlertClient.getMutuAlertService();
    }

    private void initUI() {
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(this);
        UIService.ButtonDisable(UIService.BUTTON_SECONDARY, buttonLogin);

        buttonCreateAccount = findViewById(R.id.buttonCreateAccount);
        buttonCreateAccount.setOnClickListener(this);

        buttonForgotPassword = findViewById(R.id.buttonForgotPassword);
        buttonForgotPassword.setOnClickListener(this);

        editTextPhone = findViewById(R.id.editTextPhone);
        editTextPhone.addTextChangedListener(this);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPassword.addTextChangedListener(this);

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
            case R.id.buttonLogin:
                login();
                break;
            case R.id.buttonCreateAccount:
                goToRegister(true);
                break;
            case R.id.buttonForgotPassword:
                goToRegister(false);
                break;
        }
    }

    private void login(){
        RequestUserAuthLogin requestUserAuthLogin = new RequestUserAuthLogin(editTextPhone.getText().toString(), editTextPassword.getText().toString());
        Call<ResponseUserAuthSuccess> call = mutuAlertService.login(requestUserAuthLogin);
        call.enqueue(new Callback<ResponseUserAuthSuccess>() {
            @Override
            public void onResponse(Call<ResponseUserAuthSuccess> call, Response<ResponseUserAuthSuccess> response) {
                if( response.isSuccessful() ){
                    SharedPreferencesManager.setSomeStringValue(Constantes.PREF_USERNAME, editTextPhone.getText().toString());
                    if(response.body().getData().getAlert()) {
                        SharedPreferencesManager.setSomeBooleanValue(Constantes.PREF_ALERT_APP, true);
                    }
                    Utils.saveDataLogin(response.body().getData());
                    Utils.goToHome();
                    LoginActivity.this.finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseUserAuthSuccess> call, Throwable t) { }
        });
    }

    private void goToRegister(boolean isNewUser) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        intent.putExtra("isNewUser", isNewUser);
        startActivity(intent);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if( formValid() ){
            UIService.ButtonEnable(UIService.BUTTON_SECONDARY, buttonLogin);
        } else {
            UIService.ButtonDisable(UIService.BUTTON_SECONDARY, buttonLogin);
        }
    }

    @Override
    public void afterTextChanged(Editable s) { }

    public boolean formValid(){
        if( editTextPhone.getText().toString().length() < Constantes.PHONE_LENGTH)
            return false;

        if( editTextPassword.getText().toString().length() < Constantes.PASSWORD_LENGTH )
            return false;

        return true;
    }
}