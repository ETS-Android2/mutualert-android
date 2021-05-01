package com.mcuevapps.mutualert.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcuevapps.mutualert.R;
import com.mcuevapps.mutualert.Service.Utils;
import com.mcuevapps.mutualert.common.Constantes;
import com.mcuevapps.mutualert.common.SharedPreferencesManager;
import com.mcuevapps.mutualert.retrofit.AuthMutuAlertClient;
import com.mcuevapps.mutualert.retrofit.AuthMutuAlertService;
import com.mcuevapps.mutualert.retrofit.response.ResponseUserAuthSuccess;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreenActivity extends AppCompatActivity {

    private static final String TAG = "SplashScreenActivity";

    private ImageView iconImageView;
    private TextView nameTextView;
    private Boolean isRunning = false;

    private AuthMutuAlertClient authMutuAlertClient;
    private AuthMutuAlertService authMutuAlertService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (savedInstanceState == null) {
            retrofitInit();
            initUI();
        }
    }

    private void retrofitInit() {
        authMutuAlertClient = AuthMutuAlertClient.getInstance();
        authMutuAlertService = authMutuAlertClient.getAuthMutuAlertService();
    }

    private void initUI() {
        iconImageView = findViewById(R.id.iconImageView);
        nameTextView = findViewById(R.id.nameTextView);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if(!isRunning) {
            isRunning = true;
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            int height = Resources.getSystem().getDisplayMetrics().heightPixels;

            int halfWidth = width / 2;
            int halfHeight = height / 2;

            int px50 = Utils.dpToPx(this, 50);
            int px20 = Utils.dpToPx(this, 20);
            int px90 = Utils.dpToPx(this, 90);

            int iconToX = halfWidth - px50;
            int iconToY = halfHeight - px90;

            iconImageView.animate().scaleX(4).scaleY(4).alpha(0).setDuration(0).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {

                    iconImageView.animate().scaleX(1).scaleY(1).alpha(1).setDuration(1500).setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) { }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            nameTextView.animate().alpha(1).setDuration(800).setListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animator) { }

                                @Override
                                public void onAnimationEnd(Animator animator) {
                                    verifyToken();
                                }

                                @Override
                                public void onAnimationCancel(Animator animator) { }

                                @Override
                                public void onAnimationRepeat(Animator animator) { }
                            }).start();

                        }

                        @Override
                        public void onAnimationCancel(Animator animator) { }

                        @Override
                        public void onAnimationRepeat(Animator animator) { }
                    }).start();

                }

                @Override
                public void onAnimationCancel(Animator animator) { }

                @Override
                public void onAnimationRepeat(Animator animator) { }
            }).start();
        }
    }

    private void verifyToken(){
        String token = SharedPreferencesManager.getSomeStringValue(Constantes.PREF_TOKEN);
        if ( TextUtils.isEmpty(token) ) {
            Utils.goToLogin();
            this.finish();
        }else{
            authToken();
        }
    }

    private void authToken(){
        Call<ResponseUserAuthSuccess> call = authMutuAlertService.token();
        call.enqueue(new Callback<ResponseUserAuthSuccess>() {
            @Override
            public void onResponse(Call<ResponseUserAuthSuccess> call, Response<ResponseUserAuthSuccess> response) {
                if( response.isSuccessful() ){
                    Utils.saveDataLogin(response.body().getData());
                    Utils.goToHome();
                    SplashScreenActivity.this.finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseUserAuthSuccess> call, Throwable t) { }
        });
    }
}