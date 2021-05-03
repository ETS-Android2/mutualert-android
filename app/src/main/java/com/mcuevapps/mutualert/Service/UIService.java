package com.mcuevapps.mutualert.Service;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.mcuevapps.mutualert.R;
import com.mcuevapps.mutualert.common.MyApp;

public class UIService {
    public static final int TOAST_CARD = 0;
    public static final int TOAST_SUCCESS = 1;
    public static final int TOAST_INFO = 2;
    public static final int TOAST_WARNING = 3;
    public static final int TOAST_ERROR = 4;

    public static final int BUTTON_PRIMARY = 1;
    public static final int BUTTON_SECONDARY = 2;
    public static final int BUTTON_RAISED = 3;

    public static void showEventToast(String message){
        showEventToast(MyApp.getContext(), TOAST_CARD, message);
    }

    public static void showEventToast(int event, String message){
        showEventToast(MyApp.getContext(), event, message);
    }

    public static void showEventToast(Context context, int event, String message){
        int layoutId;
        switch (event){
            case TOAST_SUCCESS:
                layoutId = R.layout.ui_toast_success_layout;
                break;
            case TOAST_INFO:
                layoutId = R.layout.ui_toast_info_layout;
                break;
            case TOAST_WARNING:
                layoutId = R.layout.ui_toast_warning_layout;
                break;
            case TOAST_ERROR:
                layoutId = R.layout.ui_toast_error_layout;
                break;
            default:
                layoutId = R.layout.ui_toast_card_layout;
                break;
        }

        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View view = inflater.inflate(layoutId, null);
        TextView textViewMessage = (TextView) view.findViewById(R.id.textViewMessage);
        textViewMessage.setText(message);

        new Handler(Looper.getMainLooper()).post(new Runnable()
        {
            @Override
            public void run()
            {
                Toast toast = new Toast(MyApp.getContext());
                toast.setView(view);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

            }
        });
    }

    public static void showDialogConfirm(final InterfaceService.successListener listener, Context context, String title){
        showDialogConfirm(listener, context, title, "", MyApp.getInstance().getString(R.string.confirm), MyApp.getInstance().getString(R.string.cancel));
    }

    public static void showDialogConfirm(final InterfaceService.successListener listener, Context context, String title, String message){
        showDialogConfirm(listener, context, title, message, MyApp.getInstance().getString(R.string.confirm), MyApp.getInstance().getString(R.string.cancel));
    }

    public static void showDialogConfirm(final InterfaceService.successListener listener, Context context, String title, String message, String acceptBtn){
        showDialogConfirm(listener, context, title, message, acceptBtn, MyApp.getInstance().getString(R.string.cancel));
    }

    public static void showDialogConfirm(final InterfaceService.successListener listener, Context context,  String title, String message, String confirmBtn, String cancelBtn){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        if( !TextUtils.isEmpty(message) ){
            builder.setMessage(message);
        }
        builder.setPositiveButton(confirmBtn, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                listener.response(true);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(cancelBtn, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                listener.response(false);
                dialog.dismiss();
            }
        });
        builder.setIcon(R.drawable.ic_baseline_warning_black_24);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void ButtonEnable(int type, Button button){
        int drawableId;
        switch (type){
            case BUTTON_SECONDARY:
                drawableId = R.drawable.button_dialog_secondary_background;
                break;
            case BUTTON_RAISED:
                drawableId = R.drawable.button_round_white_background;
                break;
            default:
                drawableId = R.drawable.button_dialog_primary_background;
                break;
        }

        Drawable drawable = MyApp.getContext().getResources().getDrawable(drawableId);
        button.setBackground(drawable);
        button.setEnabled(true);
    }

    public static void ButtonDisable(int type, Button button){
        int drawableId;
        switch (type){
            case BUTTON_SECONDARY:
                drawableId = R.drawable.button_secondary_disable_background;
                break;
            case BUTTON_RAISED:
                drawableId = R.drawable.button_raised_disable_background;
                break;
            default:
                drawableId = R.drawable.button_dialog_disable_background;
                break;
        }

        Drawable drawable = MyApp.getContext().getResources().getDrawable(drawableId);
        button.setBackground(drawable);
        button.setEnabled(false);
    }
}
