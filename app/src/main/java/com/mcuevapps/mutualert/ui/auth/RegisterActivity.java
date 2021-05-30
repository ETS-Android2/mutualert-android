package com.mcuevapps.mutualert.ui.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.mcuevapps.mutualert.R;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = RegisterActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (savedInstanceState == null) {
            Bundle bundle = getIntent().getExtras();

            boolean isNewUser=false;
            if(bundle!=null) {
                isNewUser = bundle.getBoolean("isNewUser");
            }

            RegisterPhoneFragment fragment = RegisterPhoneFragment.newInstance(isNewUser);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayoutRegister, fragment)
                    .commit();
        }
    }
}