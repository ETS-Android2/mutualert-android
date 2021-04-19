package com.mcuevapps.mutualert.common;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Button;

import com.mcuevapps.mutualert.R;

public class DesignService {
    private Context context;

    public DesignService(){
    }

    public DesignService(Context context){
        this.context = context;
    }

    public void ButtonDefaultEnable(Button button){
        Drawable drawable = context.getResources().getDrawable(R.drawable.button_dialog_primary_background);
        button.setBackground(drawable);
        button.setEnabled(true);
    }

    public void ButtonDefaultDisable(Button button){
        Drawable drawable = context.getResources().getDrawable(R.drawable.button_dialog_disable_background);
        button.setBackground(drawable);
        button.setEnabled(false);
    }

    public void ButtonSecondEnable(Button button){
        Drawable drawable = context.getResources().getDrawable(R.drawable.button_round_white_background);
        button.setBackground(drawable);
        button.setEnabled(true);
    }
}
