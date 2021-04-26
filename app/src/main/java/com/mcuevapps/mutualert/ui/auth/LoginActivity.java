package com.mcuevapps.mutualert.ui.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.mcuevapps.mutualert.R;
import com.mcuevapps.mutualert.common.Constantes;
import com.mcuevapps.mutualert.common.DesignService;
import com.mcuevapps.mutualert.common.MyApp;
import com.mcuevapps.mutualert.common.SharedPreferencesManager;
import com.mcuevapps.mutualert.common.ToastService;
import com.mcuevapps.mutualert.retrofit.MutuAlertClient;
import com.mcuevapps.mutualert.retrofit.MutuAlertService;
import com.mcuevapps.mutualert.retrofit.request.RequestUserAuthLogin;
import com.mcuevapps.mutualert.retrofit.response.ResponseUserAuthSuccess;
import com.mcuevapps.mutualert.ui.HomeActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private static final String TAG = "LoginActivity";

    private TextInputEditText editTextPhone;
    private TextInputEditText editTextPassword;
    private Button buttonLogin;
    private Button buttonCreateAccount;
    private Button buttonForgotPassword;

    private DesignService designService;
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
        designService = new DesignService(getApplicationContext());


        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(this);
        designService.ButtonSecondaryDisable(buttonLogin);

        buttonCreateAccount = (Button) findViewById(R.id.buttonCreateAccount);
        buttonCreateAccount.setOnClickListener(this);

        buttonForgotPassword = (Button) findViewById(R.id.buttonForgotPassword);
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
                goToSignUp(true);
                break;
            case R.id.buttonForgotPassword:
                goToSignUp(false);
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
                    SharedPreferencesManager.setSomeStringValue(Constantes.PREF_TOKEN, response.body().getData().getToken());
                    SharedPreferencesManager.setSomeStringValue(Constantes.PREF_APELLIDOPAT, response.body().getData().getProfile().getApepat());
                    SharedPreferencesManager.setSomeStringValue(Constantes.PREF_APELLIDOMAT, response.body().getData().getProfile().getApemat());
                    SharedPreferencesManager.setSomeStringValue(Constantes.PREF_NOMBRES, response.body().getData().getProfile().getNombres());
                    SharedPreferencesManager.setSomeStringValue(Constantes.PREF_EMAIL, response.body().getData().getProfile().getEmail());
                    SharedPreferencesManager.setSomeStringValue(Constantes.PREF_AVATAR, response.body().getData().getProfile().getAvatar());
                    goToDashboard();
                } else {
                    ToastService.showErrorResponse(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ResponseUserAuthSuccess> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), getString(R.string.error_network), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goToDashboard() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void goToSignUp(boolean isNewUser) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        intent.putExtra("isNewUser", isNewUser);
        startActivity(intent);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if( formValid() ){
            designService.ButtonSecondaryEnable(buttonLogin);
        } else {
            designService.ButtonSecondaryDisable(buttonLogin);
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