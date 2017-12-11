package com.yusion.shanghai.yusion4s.retrofit.callback;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.yusion.shanghai.yusion4s.retrofit.Api;
import com.yusion.shanghai.yusion4s.settings.Settings;
import com.yusion.shanghai.yusion4s.utils.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.UnknownHostException;

import io.sentry.Sentry;
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
        if (dialog != null) {
            dialog.dismiss();
        }
        try {
            if (response != null) {
                String body = response.body().string();
                try {
                    JSONObject object = new JSONObject(body.toString());
                    Logger.json(body.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e(Api.getTag(call.request()), "onResponse: " + body);
                onCustomResponse(body);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        if (dialog != null) {
            dialog.dismiss();
        }
        if (t instanceof UnknownHostException) {
            Toast.makeText(context, "网络繁忙,请检查网络", Toast.LENGTH_SHORT).show();
        } else if (Settings.isOnline) {
            Toast.makeText(context, "接口调用失败,请稍后再试...", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show();
        }
        Sentry.capture(t);
    }

    public abstract void onCustomResponse(String body);

}
