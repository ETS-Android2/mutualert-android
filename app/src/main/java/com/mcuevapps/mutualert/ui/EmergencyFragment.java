package com.mcuevapps.mutualert.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mcuevapps.mutualert.R;
import com.mcuevapps.mutualert.Service.UIService;
import com.ncorti.slidetoact.SlideToActView;

public class EmergencyFragment extends Fragment {

    private static final String TAG = EmergencyFragment.class.getSimpleName();

    private View view;
    private SlideToActView slideToActView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_emergency, container, false);

        initUI();
        return view;
    }

    private void initUI() {
        slideToActView = view.findViewById(R.id.slideToActView);
        slideToActView.setOnSlideCompleteListener(slideToActView -> {
            UIService.showEventToast(UIService.TOAST_SUCCESS, "Alerta finalizada");
            ((HomeActivity) getActivity()).stopAlert();
        });
    }
}