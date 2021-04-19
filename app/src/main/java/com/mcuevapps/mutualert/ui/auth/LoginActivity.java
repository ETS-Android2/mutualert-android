package com.mcuevapps.mutualert.ui.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mcuevapps.mutualert.R;
import com.mcuevapps.mutualert.ui.HomeActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonLogin;
    private Button buttonCreateAccount;
    private Button buttonForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (savedInstanceState == null) {
            initUI();
        }
    }

    private void initUI() {
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(this);
        buttonCreateAccount = (Button) findViewById(R.id.buttonCreateAccount);
        buttonCreateAccount.setOnClickListener(this);
        buttonForgotPassword = (Button) findViewById(R.id.buttonForgotPassword);
        buttonForgotPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.buttonLogin:
                goToDashboard();
                break;
            case R.id.buttonCreateAccount:
                goToSignUp(true);
                break;
            case R.id.buttonForgotPassword:
                goToSignUp(false);
                break;
        }
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
}