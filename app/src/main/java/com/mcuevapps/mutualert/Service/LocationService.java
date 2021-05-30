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

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mcuevapps.mutualert.R;
import com.mcuevapps.mutualert.common.Constantes;
import com.mcuevapps.mutualert.common.MyApp;
import com.mcuevapps.mutualert.common.SharedPreferencesManager;
import com.mcuevapps.mutualert.ui.HomeActivity;

public class LocationService extends Service {

    private static final String TAG = LocationService.class.getSimpleName();

    private static final String PACKAGE_NAME = "com.mcuevapps.mutualert.Service";
    //private static final String EXTRA_STARTED_FROM_NOTIFICATION = PACKAGE_NAME +".started_from_notification";
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
        /*
        boolean startedFromNotification = intent.getBooleanExtra(EXTRA_STARTED_FROM_NOTIFICATION,
                false);

        if (startedFromNotification) {
            removeLocationUpdates();
            stopSelf();
        }
        */
        return START_NOT_STICKY;
    }

    private void onNewLocation(Location location) {
        mLocation = location;
        Log.i(TAG, "Service NewLocation: ("+mLocation.getLatitude()+" , "+mLocation.getLongitude()+")");
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
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
                .setContentText(MyApp.getInstance().getString(R.string.emergency_notification_text))
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setSmallIcon(R.drawable.ic_baseline_dashboard_white_36)
                .setTicker(MyApp.getInstance().getString(R.string.emergency_notification_text))
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
        stopForeground(true);
        mChangingConfiguration = false;
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
