package com.mcuevapps.mutualert.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.util.Log;

import com.google.android.gms.location.LocationResult;
import com.mcuevapps.mutualert.model.Point;
import com.mcuevapps.mutualert.retrofit.AuthMutuAlertClient;
import com.mcuevapps.mutualert.retrofit.AuthMutuAlertService;
import com.mcuevapps.mutualert.retrofit.request.RequestUserStateLocation;
import com.mcuevapps.mutualert.retrofit.response.ResponseSuccess;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = LocationBroadcastReceiver.class.getSimpleName();

    public static final String ACTION_LOCATION_UPDATE =
            "com.mcuevapps.mutualert.Service.LocationBroadcastReceiver.ACTION_LOCATION_UPDATE";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) return;

        final String action = intent.getAction();

        if ( action.equals(ACTION_LOCATION_UPDATE) ) {
            LocationResult result = LocationResult.extractResult(intent);

            if (result == null) return;

            List<Location> mLocations = result.getLocations();
            Location lastLocation = mLocations.get(mLocations.size() - 1);
            Point point = new Point(lastLocation.getLatitude(), lastLocation.getLongitude(), lastLocation.getAccuracy());

            AuthMutuAlertClient authMutuAlertClient = AuthMutuAlertClient.getInstance();;
            AuthMutuAlertService authMutuAlertService = authMutuAlertClient.getAuthMutuAlertService();

            RequestUserStateLocation requestUserStateLocation = new RequestUserStateLocation(String.valueOf(point.getLatitude()), String.valueOf(point.getLongitude()), String.valueOf(point.getAccuracy()));
            Call<ResponseSuccess> call = authMutuAlertService.sendLocation(requestUserStateLocation);
            call.enqueue(new Callback<ResponseSuccess>() {
                 @Override
                 public void onResponse(Call<ResponseSuccess> call, Response<ResponseSuccess> response) { }

                 @Override
                 public void onFailure(Call<ResponseSuccess> call, Throwable t) { }
             });

            String strPoint = "( " + point.getLatitude() + " , " + point.getLongitude() + " , " + point.getAccuracy() + " )";
            Log.i(TAG, "Receiver LocationUpdate: "+strPoint);
        } else if ( action.equals(Intent.ACTION_BOOT_COMPLETED) ) {
            /*
            Intent i = new Intent(context, LocationService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(i);
            } else {
                context.startService(i);
            }
            */
        }
    }
}
