package com.mcuevapps.mutualert.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;
import com.mcuevapps.mutualert.R;
import com.mcuevapps.mutualert.Service.Utils;

public class HomeBottomModalFragment extends BottomSheetDialogFragment {

    private static final String TAG = HomeBottomModalFragment.class.getSimpleName();

    private View view;

    private NavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            boolean success = false;

            switch (item.getItemId()) {
                case R.id.action_logout:
                    logout();
                    success = true;
                    break;
            }

            if(success){
                getDialog().dismiss();
            }

            return success;
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_bottom_modal, container, false);

        initUI();
        return view;
    }

    private void initUI() {
        final NavigationView nav = view.findViewById(R.id.navigationView);
        nav.setNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void logout(){
        Utils.removeDataLogin();
        Utils.goToLogin();
        ((HomeActivity) getActivity()).removeLocationUpdates();
        getActivity().finish();
    }
}