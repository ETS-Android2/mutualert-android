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

    public void ButtonPrimaryEnable(Button button){
        Drawable drawable = context.getResources().getDrawable(R.drawable.button_dialog_primary_background);
        button.setBackground(drawable);
        button.setEnabled(true);
    }

    public void ButtonPrimaryDisable(Button button){
        Drawable drawable = context.getResources().getDrawable(R.drawable.button_dialog_disable_background);
        button.setBackground(drawable);
        button.setEnabled(false);
    }

    public void ButtonSecondaryEnable(Button button){
        Drawable drawable = context.getResources().getDrawable(R.drawable.button_dialog_secondary_background);
        button.setBackground(drawable);
        button.setEnabled(true);
    }

    public void ButtonSecondaryDisable(Button button){
        Drawable drawable = context.getResources().getDrawable(R.drawable.button_secondary_disable_background);
        button.setBackground(drawable);
        button.setEnabled(false);
    }

    public void ButtonRaisedEnable(Button button){
        Drawable drawable = context.getResources().getDrawable(R.drawable.button_round_white_background);
        button.setBackground(drawable);
        button.setEnabled(true);
    }

    public void ButtonRaisedDisable(Button button){
        Drawable drawable = context.getResources().getDrawable(R.drawable.button_raised_disable_background);
        button.setBackground(drawable);
        button.setEnabled(false);
    }
}
