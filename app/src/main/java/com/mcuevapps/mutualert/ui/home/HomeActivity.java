package com.mcuevapps.mutualert.ui.home;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mcuevapps.mutualert.R;
import com.mcuevapps.mutualert.Service.LocationBroadcastReceiver;
import com.mcuevapps.mutualert.Service.LocationService;
import com.mcuevapps.mutualert.Service.UIService;
import com.mcuevapps.mutualert.common.Constantes;
import com.mcuevapps.mutualert.common.SharedPreferencesManager;
import com.mcuevapps.mutualert.data.ContactViewModel;
import com.mcuevapps.mutualert.retrofit.response.AlertContact;
import com.mcuevapps.mutualert.ui.DashboardFragment;
import com.mcuevapps.mutualert.ui.EmergencyFragment;
import com.mcuevapps.mutualert.ui.contacts.ContactListFragment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();

    private boolean emergency;
    private TextView textViewToolbar;
    private FloatingActionButton fab;
    private BottomAppBar bottomAppBar;
    private AppBarLayout appBarLayout;
    private int appBarLayoutHeight;

    private String currentFragment;

    private ContactViewModel contactViewModel;

    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationService mService = null;
    private boolean mBound = false;

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationService.LocalBinder binder = (LocationService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            Log.i(TAG, "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
            Log.i(TAG, "onServiceDisconnected");
        }
    };

    private Toolbar.OnMenuItemClickListener mOnMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int id = item.getItemId();

            Fragment fragment = null;
            switch (id) {
                case R.id.navigation_dashboard:
                    fragment = new DashboardFragment();
                    break;
                case R.id.navigation_contact:
                    fragment = new ContactListFragment();
                    break;
            }
            if( fragment != null && !currentFragment.equals(fragment.getClass().toString()) ) {
                fabDefault();
                changeFragment(fragment);
                setToolbarText(item.getTitle().toString());
                return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (savedInstanceState == null) {
            initUI();
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        createLocationRequest();
    }

    @Override
    protected void onStart() {
        super.onStart();

        bindService(new Intent(this, LocationService.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!emergency){
            requestLocationUpdates();
        }
    }

    private void createLocationRequest() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(Constantes.LOCATION_UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(Constantes.LOCATION_FASTEST_UPDATE_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setMaxWaitTime(Constantes.LOCATION_MAX_WAIT_TIME);
    }

    private void initUI() {
        emergency = SharedPreferencesManager.getSomeBooleanValue(Constantes.PREF_ALERT_APP, false);

        contactViewModel = new ViewModelProvider(this).get(ContactViewModel.class);

        getSupportActionBar().hide();

        textViewToolbar = findViewById(R.id.textViewToolbar);
        appBarLayout = findViewById(R.id.appBarLayout);
        ViewGroup.LayoutParams appBarParams = appBarLayout.getLayoutParams();
        appBarLayoutHeight = appBarParams.height;

        fab = findViewById(R.id.fab);
        fab.setOnLongClickListener(v -> {
            sendAlert(false);
            return true;
        });
        fabDefault();

        bottomAppBar = findViewById(R.id.bottomAppBar);
        bottomAppBar.setOnMenuItemClickListener(mOnMenuItemClickListener);
        bottomAppBar.setNavigationOnClickListener(v -> showBottomModal());
        bottomAppBar.post(() -> {
            if(emergency) bottomAppBar.performHide();
        });

        if(!emergency){
            changeFragment(new DashboardFragment());
        } else{
            changeFragment(new EmergencyFragment());
            hideAppBarFab();
        }
        loadContactData();
    }

    private void loadContactData() {
        contactViewModel.getContacts().observe(this, new Observer<List<AlertContact>>() {
            @Override
            public void onChanged(@Nullable List<AlertContact> contacts) {
                if(currentFragment.equals(new ContactListFragment().getClass().toString()) && contacts.size()<Constantes.CONTACT_LENGTH){
                    fab.setImageResource(R.drawable.ic_baseline_person_add_white_24);
                    fab.setOnClickListener(v -> contactViewModel.createContact((HomeActivity.this)));
                } else {
                    fabDefault();
                }
            }
        });
    }

    private void fabDefault(){
        fab.setImageResource(R.drawable.ic_baseline_my_location_white_24);
        fab.setOnClickListener(v -> sendAlert(true));
    }

    private void setToolbarText(String text){
        textViewToolbar.setText(text);
    }

    private void sendAlert(boolean confirm){
        if(confirm){
            UIService.showDialogConfirm(success -> {
                if(success){
                    sendAlert(false);
                }
            }, this, UIService.DIALOG_SECONDARY, getString(R.string.emergency_dialog_title),
                    getString(R.string.emergency_dialog_message), getString(R.string.emergency_dialog_confirm), getString(R.string.emergency_dialog_cancel));
        }else{
            removeLocationUpdates();
            hideAppBarFab();
            bottomAppBar.performHide();
            changeFragment(new EmergencyFragment());
            mService.requestLocationUpdates();
        }
    }

    public void stopAlert(){
        changeFragment(new DashboardFragment());
        showAppBarFabBottom();
        fabDefault();
        mService.removeLocationUpdates();
        requestLocationUpdates();
    }

    public void hideAppBarFab(){
        ViewGroup.LayoutParams appBarParams = appBarLayout.getLayoutParams();
        appBarParams.height = 0;
        appBarLayout.setLayoutParams(appBarParams);
        fab.hide();
    }

    public void showAppBarFabBottom(){
        bottomAppBar.performShow();
        fab.show();
        ViewGroup.LayoutParams appBarParams = appBarLayout.getLayoutParams();
        appBarParams.height = appBarLayoutHeight;
        appBarLayout.setLayoutParams(appBarParams);
    }

    private void showBottomModal(){
        HomeBottomModalFragment bottomModalFragment = new HomeBottomModalFragment();
        bottomModalFragment.show( getSupportFragmentManager(), "HomeBottomModalFragment" );
    }

    public void requestLocationUpdates() {
        try {
            Log.i(TAG, "Receiver starting location updates");
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, getPendingIntent());
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(this, LocationBroadcastReceiver.class);
        intent.setAction(LocationBroadcastReceiver.ACTION_LOCATION_UPDATE);
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void removeLocationUpdates() {
        Log.i(TAG, "Receiver removing location updates");;
        mFusedLocationClient.removeLocationUpdates(getPendingIntent());
    }

    public void changeFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragment, fragment.getClass().toString())
                .commit();
        currentFragment = fragment.getClass().toString();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onStop() {
        if (mBound) {
            unbindService(mServiceConnection);
            mBound = false;
        }
        super.onStop();
    }
}