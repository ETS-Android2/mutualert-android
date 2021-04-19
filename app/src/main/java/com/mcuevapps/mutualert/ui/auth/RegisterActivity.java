package com.mcuevapps.mutualert.ui.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.mcuevapps.mutualert.R;

public class RegisterActivity extends AppCompatActivity {

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

            Bundle args = new Bundle();
            args.putBoolean("isNewUser", isNewUser);

            Fragment fragment = new RegisterPhoneFragment();
            fragment.setArguments(args);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayoutRegister, fragment)
                    .commit();
        }
    }
}