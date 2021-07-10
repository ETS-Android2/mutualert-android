package com.mcuevapps.mutualert.ui;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.mcuevapps.mutualert.R;

public class InfoWindowAdapter implements GoogleMap.InfoWindowAdapter{

    private Context context;

    public InfoWindowAdapter(Context context){
        this.context = context;
    }

    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        return null;
    }

    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {
        LinearLayout info = new LinearLayout(context);
        info.setOrientation(LinearLayout.VERTICAL);

        TextView title = new TextView(context);
        title.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        title.setGravity(Gravity.CENTER);
        title.setTypeface(null, Typeface.BOLD);
        title.setText(marker.getTitle());

        TextView snippet = new TextView(context);
        snippet.setGravity(Gravity.RIGHT);
        snippet.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        snippet.setPaintFlags(snippet.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        snippet.setText(marker.getSnippet());

        info.addView(title);
        info.addView(snippet);

        return info;
    }
}
