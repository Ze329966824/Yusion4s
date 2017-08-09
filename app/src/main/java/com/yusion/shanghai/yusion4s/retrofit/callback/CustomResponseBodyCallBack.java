package com.yusion.shanghai.yusion4s.retrofit.callback;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.pgyersdk.crash.PgyCrashManager;
import com.yusion.shanghai.yusion4s.settings.Settings;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class CustomResponseBodyCallBack implements Callback<ResponseBody> {
    private Dialog dialog;
    private Context context;

    public CustomResponseBodyCallBack(Context context) {
        this(context, null);
    }

    public CustomResponseBodyCallBack(Context context, Dialog dialog) {
        this.context = context;
        this.dialog = dialog;
        if (this.dialog != null) {
            this.dialog.show();
        }
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        try {
            String body = response.body().string();
            Log.e("API", "onResponse: " + body);
            onCustomResponse(body);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        if (Settings.isOnline) {
            Toast.makeText(context, "请求失败", Toast.LENGTH_SHORT).show();
            PgyCrashManager.reportCaughtException(context, ((Exception) t));
        } else {
            Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show();
        }
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public abstract void onCustomResponse(String body);

}
