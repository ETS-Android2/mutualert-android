package com.mcuevapps.mutualert.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.mcuevapps.mutualert.BuildConfig;
import com.mcuevapps.mutualert.R;
import com.mcuevapps.mutualert.common.Constantes;
import com.mcuevapps.mutualert.common.SharedPreferencesManager;
import com.mcuevapps.mutualert.model.Point;
import com.mcuevapps.mutualert.retrofit.AuthMutuAlertClient;
import com.mcuevapps.mutualert.retrofit.AuthMutuAlertService;
import com.mcuevapps.mutualert.retrofit.response.AlertEmergency;
import com.mcuevapps.mutualert.retrofit.response.ResponseAlertEmergencyList;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.HashMap;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private static final String TAG = DashboardFragment.class.getSimpleName();

    private View view;
    private GoogleMap gMap;

    private AuthMutuAlertClient authMutuAlertClient;
    private AuthMutuAlertService authMutuAlertService;

    private HashMap<Long, AlertEmergency> alertEmergencies;
    private HashMap<Long, Marker> markerEmergencies;

    private Socket mSocket;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        retrofitInit();
        initUI();
        return view;
    }

    private void retrofitInit() {
        authMutuAlertClient = AuthMutuAlertClient.getInstance();
        authMutuAlertService = authMutuAlertClient.getAuthMutuAlertService();
    }

    public void initUI() {
        alertEmergencies = new HashMap<>();
        markerEmergencies = new HashMap<>();

        try {
            String token = SharedPreferencesManager.getSomeStringValue(Constantes.PREF_TOKEN);
            IO.Options options = new IO.Options();
            options.forceNew = true;
            options.reconnection = true;
            options.query = "auth="+token;
            mSocket = IO.socket(BuildConfig.SOCKET_URL, options);
        } catch (URISyntaxException e) {
            Log.e(TAG, "Error socket conection " + e);
        }
        mSocket.on(Constantes.SOCKET_EVENT, onEvent);
        mSocket.connect();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        listEmergencies();
        /* Codigo no funcional */
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) { }
        /* *********** */
        gMap = googleMap;
        gMap.setOnInfoWindowClickListener(this);
        gMap.setMinZoomPreference(10);
        gMap.setMaxZoomPreference(16);
        gMap.setMyLocationEnabled(true);

        Criteria criteria = new Criteria();

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if(location!=null){
            LatLng myLocation  = new LatLng(location.getLatitude(),location.getLongitude());
            CameraPosition camera = new CameraPosition.Builder()
                    .target(myLocation)
                    .zoom(12)
                    .bearing(0)
                    .tilt(30)
                    .build();
            gMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera));
        }
    }

    public void listEmergencies(){
        Call<ResponseAlertEmergencyList> call = authMutuAlertService.getAlertEmergencies();
        call.enqueue(new Callback<ResponseAlertEmergencyList>() {
            @Override
            public void onResponse(Call<ResponseAlertEmergencyList> call, Response<ResponseAlertEmergencyList> response) {
                if( response.isSuccessful() ){
                    for (AlertEmergency emergency : response.body().getData()){
                        Long id = emergency.getId();
                        alertEmergencies.put(id, emergency);
                        createOrUpdateEmergencyMarker(id, emergency);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseAlertEmergencyList> call, Throwable t) { }
        });
    }

    public void createOrUpdateEmergencyMarker(Long id, AlertEmergency emergency){
        Point location = emergency.getLocation();
        LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
        Marker marker;
        if( markerEmergencies.containsKey(id) ){
            marker = markerEmergencies.get(id);
            marker.setPosition(position);
        }else{
            marker = gMap.addMarker(
                    new MarkerOptions().position(position)
                            .title(emergency.getPhone())
                            .snippet(emergency.getNombres()+" "+emergency.getApepat()+" "+emergency.getApemat())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_sos))
            );
            marker.setTag(id);
        }
        markerEmergencies.put(id, marker);
    }

    private Emitter.Listener onEvent = args -> getActivity().runOnUiThread(() -> {
        JSONObject data = (JSONObject) args[0];
        Gson gson = new Gson();
        try {
            String event = data.getString("event");
            if(event.equals(Constantes.EVENT_EMERGENCY_INIT) || event.equals(Constantes.EVENT_EMERGENCY_UPDATE)){
                String dataStr = data.getJSONObject("data").toString();
                AlertEmergency emergency = gson.fromJson(dataStr, AlertEmergency.class);
                Long id = emergency.getId();
                alertEmergencies.put(id, emergency);
                createOrUpdateEmergencyMarker(id, emergency);
            }else if(event.equals(Constantes.EVENT_EMERGENCY_END)){
                Long id = data.getLong("data");
                if (alertEmergencies.containsKey(id)) {
                    alertEmergencies.remove(id);
                    Marker marker = markerEmergencies.get(id);
                    marker.remove();
                    markerEmergencies.remove(id);
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error socket call " + e);
        }
    });

    @Override
    public void onInfoWindowClick(Marker marker) {
        Long id = (Long) marker.getTag();
        Log.i(TAG, "Marker id: "+id);
        Intent intent = new Intent(getActivity(), WebViewActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();
        mSocket.off(Constantes.SOCKET_EVENT, onEvent);
    }
}