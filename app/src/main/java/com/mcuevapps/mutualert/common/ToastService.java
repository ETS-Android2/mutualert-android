package com.mcuevapps.mutualert.common;

import android.content.res.Resources;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mcuevapps.mutualert.R;
import com.mcuevapps.mutualert.retrofit.response.ResponseError;
import okhttp3.ResponseBody;

import java.io.IOException;

public class ToastService {
    public static void showErrorResponse(ResponseBody body) {
        try {
            ResponseError error = new Gson().fromJson(body.string(), ResponseError.class);
            Toast.makeText(MyApp.getContext(), error.getMsg(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(MyApp.getContext(), Resources.getSystem().getString(R.string.error_response), Toast.LENGTH_SHORT).show();
        }
    }
}
