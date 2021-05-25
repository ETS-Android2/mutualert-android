package com.mcuevapps.mutualert.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.animation.Animator;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.mcuevapps.mutualert.R;
import com.mcuevapps.mutualert.Service.UIService;
import com.mcuevapps.mutualert.Service.Utils;
import com.mcuevapps.mutualert.common.Constantes;
import com.mcuevapps.mutualert.common.SharedPreferencesManager;
import com.mcuevapps.mutualert.retrofit.AuthMutuAlertClient;
import com.mcuevapps.mutualert.retrofit.AuthMutuAlertService;
import com.mcuevapps.mutualert.retrofit.response.ResponseUserAuthSuccess;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreenActivity extends AppCompatActivity implements PermissionListener {

    private static final String TAG = "SplashScreenActivity";

    private ImageView iconImageView;
    private TextView nameTextView;
    private boolean isRunning = false;
    private boolean inReqPermission = true;
    private boolean permission;
    private boolean existToken;
    private boolean inRequest;

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

    @Override
    protected void onResume() {
        super.onResume();
        if(!permission && !inReqPermission){
            checkPermission();
        }
    }

    private void retrofitInit() {
        authMutuAlertClient = AuthMutuAlertClient.getInstance();
        authMutuAlertService = authMutuAlertClient.getAuthMutuAlertService();
    }

    private void initUI() {
        iconImageView = findViewById(R.id.iconImageView);
        nameTextView = findViewById(R.id.nameTextView);
        verifyToken();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if(!isRunning) {
            isRunning = true;

            iconImageView.animate().scaleX(4).scaleY(4).alpha(0).setDuration(0).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) { }

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
                                    checkPermission();
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

    private void checkPermission(){
        permission = false;
        Dexter.withContext(this)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(this)
            .check();
    }

    private void verifyToken(){
        String token = SharedPreferencesManager.getSomeStringValue(Constantes.PREF_TOKEN);
        if ( TextUtils.isEmpty(token) ) {
            existToken = false;
            inRequest = false;
        }else{
            authToken();
        }
    }
    private void authToken(){
        inRequest = true;
        Call<ResponseUserAuthSuccess> call = authMutuAlertService.token();
        call.enqueue(new Callback<ResponseUserAuthSuccess>() {
            @Override
            public void onResponse(Call<ResponseUserAuthSuccess> call, Response<ResponseUserAuthSuccess> response) {
                if( response.isSuccessful() ){
                    inRequest = false;
                    Utils.saveDataLogin(response.body().getData());
                    existToken = true;
                    changeActivity();
                }
            }

            @Override
            public void onFailure(Call<ResponseUserAuthSuccess> call, Throwable t) { }
        });
    }

    private void changeActivity(){
        if(!permission || inRequest){
            return;
        }

        if(existToken){
            Utils.goToHome();
        } else {
            Utils.goToLogin();
        }
        SplashScreenActivity.this.finish();
    }

    @Override
    public void onPermissionGranted(PermissionGrantedResponse response) {
        permission = true;
        changeActivity();
    }

    @Override
    public void onPermissionDenied(PermissionDeniedResponse response) {
        inReqPermission = false;
        moveTaskToBack(true);
    }

    @Override
    public void onPermissionRationaleShouldBeShown(PermissionRequest request, PermissionToken token) {
        UIService.showDialogConfirm(success -> {
            if(success){
                token.continuePermissionRequest();
            } else {
                token.cancelPermissionRequest();
            }
        }, this, UIService.DIALOG_DEFAULT, getString(R.string.permission_location), "",  getString(R.string.ok), getString(R.string.deny));
    }
}