package com.mcuevapps.mutualert.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.mcuevapps.mutualert.R;
import com.mcuevapps.mutualert.common.Constantes;
import com.mcuevapps.mutualert.common.MyApp;
import com.mcuevapps.mutualert.common.SharedPreferencesManager;
import com.mcuevapps.mutualert.retrofit.AuthMutuAlertClient;
import com.mcuevapps.mutualert.retrofit.AuthMutuAlertService;
import com.mcuevapps.mutualert.retrofit.request.RequestUserStateLocation;
import com.mcuevapps.mutualert.retrofit.response.ResponseSuccess;
import com.mcuevapps.mutualert.ui.home.HomeActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationService extends Service {

    private static final String TAG = LocationService.class.getSimpleName();

    private static final String PACKAGE_NAME = "com.mcuevapps.mutualert.Service";
    public static final String EXTRA_STARTED_FROM_BOOT = PACKAGE_NAME +".started_from_boot";
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;


    private static final String CHANNEL_ID = "channel_01";
    private static final String CHANNEL_NAME = "LocationService Channel 01";
    private static final int NOTIFICATION_ID = 12345678;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private Location mLocation;
    private LocationRequest mLocationRequest;
    private Handler mServiceHandler;
    private NotificationManager mNotificationManager;


    private final IBinder mBinder = new LocalBinder();
    private boolean mChangingConfiguration = false;

    public LocationService() { }

    @Override
    public void onCreate() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                onNewLocation(locationResult.getLastLocation());
            }
        };

        createLocationRequest();

        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        mServiceHandler = new Handler(handlerThread.getLooper());
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service onStartCommand");

        boolean startedFromBoot = intent.getBooleanExtra(EXTRA_STARTED_FROM_BOOT,
                false);
        if (startedFromBoot && SharedPreferencesManager.getSomeBooleanValue(Constantes.PREF_ALERT_APP, false)) {
            createNotificationManager();
            startForeground(NOTIFICATION_ID, getNotification());
        }
        return START_NOT_STICKY;
    }

    private void onNewLocation(Location location) {
        if(!SharedPreferencesManager.getSomeBooleanValue(Constantes.PREF_ALERT_APP, false)){
            return;
        }
        mLocation = location;
        AuthMutuAlertClient authMutuAlertClient = AuthMutuAlertClient.getInstance();;
        AuthMutuAlertService authMutuAlertService = authMutuAlertClient.getAuthMutuAlertService();
        RequestUserStateLocation requestUserStateLocation = new RequestUserStateLocation(String.valueOf(mLocation.getLatitude()), String.valueOf(mLocation.getLongitude()), String.valueOf(mLocation.getAccuracy()));

        if( SharedPreferencesManager.getSomeBooleanValue(Constantes.PREF_ALERT_API, false) ){
            Call<ResponseSuccess> call = authMutuAlertService.sendLocation(requestUserStateLocation);
            call.enqueue(new Callback<ResponseSuccess>() {
                @Override
                public void onResponse(Call<ResponseSuccess> call, Response<ResponseSuccess> response) { }

                @Override
                public void onFailure(Call<ResponseSuccess> call, Throwable t) { }
            });
        }else{
            Call<ResponseSuccess> call = authMutuAlertService.startEmergency(requestUserStateLocation);
            call.enqueue(new Callback<ResponseSuccess>() {
                @Override
                public void onResponse(Call<ResponseSuccess> call, Response<ResponseSuccess> response) {
                    if(response.isSuccessful() && response.body().getSuccess())
                        SharedPreferencesManager.setSomeBooleanValue(Constantes.PREF_ALERT_API, true);
                }

                @Override
                public void onFailure(Call<ResponseSuccess> call, Throwable t) { }
            });
        }
        String strMLocation = "( " + mLocation.getLatitude() + " , " + mLocation.getLongitude() + " , " + mLocation.getAccuracy() + " )";
        Log.i(TAG, "Service NewLocation: "+strMLocation);
    }

    private void createLocationRequest() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void createNotificationManager(){
        NotificationManager  mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel( CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
            new NotificationCompat.Builder(this, CHANNEL_ID);
        }
    }

    private Notification getNotification() {
        Intent notificationIntent = new Intent(this, HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(MyApp.getInstance().getString(R.string.emergency_notification_title))
                .setContentText(MyApp.getInstance().getString(R.string.emergency_notification_message))
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setSmallIcon(R.drawable.icon_mutualert_notification)
                .setTicker(MyApp.getInstance().getString(R.string.emergency_notification_message))
                .build();

        return notification;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mChangingConfiguration = true;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "Service in onBind()");
        if(SharedPreferencesManager.getSomeBooleanValue(Constantes.PREF_ALERT_APP, false)){
            requestLocationUpdates();
        }
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.i(TAG, "Service in onRebind()");
        stopForeground(true);
        mChangingConfiguration = false;
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "Service last client unbound from service");
        if (!mChangingConfiguration && SharedPreferencesManager.getSomeBooleanValue(Constantes.PREF_ALERT_APP, false)) {
            Log.i(TAG, "Service starting foreground service");
            createNotificationManager();
            startForeground(NOTIFICATION_ID, getNotification());
        }
        return true;
    }

    public void requestLocationUpdates() {
        Log.i(TAG, "Service requesting location updates");
        SharedPreferencesManager.setSomeBooleanValue(Constantes.PREF_ALERT_APP, true);
        startService(new Intent(getApplicationContext(), LocationService.class));
        try {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback, Looper.myLooper());
        } catch (SecurityException unlikely) {
            Log.e(TAG, "Service lost location permission. Could not request updates. " + unlikely);
        }
    }

    public void removeLocationUpdates() {
        Log.i(TAG, "Service removing location updates");
        try {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            SharedPreferencesManager.setSomeBooleanValue(Constantes.PREF_ALERT_APP, false);
            stopSelf();
            AuthMutuAlertClient authMutuAlertClient = AuthMutuAlertClient.getInstance();;
            AuthMutuAlertService authMutuAlertService = authMutuAlertClient.getAuthMutuAlertService();
            Call<ResponseSuccess> call = authMutuAlertService.stopEmergency();
            call.enqueue(new Callback<ResponseSuccess>() {
                @Override
                public void onResponse(Call<ResponseSuccess> call, Response<ResponseSuccess> response) {
                    if(response.isSuccessful() && response.body().getSuccess())
                    SharedPreferencesManager.setSomeBooleanValue(Constantes.PREF_ALERT_API, false);
                }

                @Override
                public void onFailure(Call<ResponseSuccess> call, Throwable t) { }
            });
        } catch (SecurityException unlikely) {
            Log.e(TAG, "Service lost location permission. Could not remove updates. " + unlikely);
        }
    }

    @Override
    public void onDestroy() {
        mServiceHandler.removeCallbacksAndMessages(null);
    }

    public class LocalBinder extends Binder {
        public LocationService getService() {
            return LocationService.this;
        }
    }
}