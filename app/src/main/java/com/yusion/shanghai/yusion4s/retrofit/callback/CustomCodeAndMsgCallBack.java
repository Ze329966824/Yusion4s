package com.yusion.shanghai.yusion4s.retrofit.callback;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.pgyersdk.crash.PgyCrashManager;
import com.yusion.shanghai.yusion4s.base.BaseResult;
import com.yusion.shanghai.yusion4s.settings.Settings;
import com.yusion.shanghai.yusion4s.ui.entrance.LoginActivity;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class CustomCodeAndMsgCallBack implements Callback<BaseResult> {

    private Dialog dialog;
    private Context context;

    public CustomCodeAndMsgCallBack(Context context) {
        this(context, null);
    }

    public CustomCodeAndMsgCallBack(Context context, Dialog dialog) {
        this.context = context;
        this.dialog = dialog;
        if (this.dialog != null) {
            this.dialog.show();
        }
    }

    @Override
    public void onResponse(Call<BaseResult> call, Response<BaseResult> response) {
        BaseResult body = response.body();
        if (body == null) {
            Toast.makeText(context, "调用接口返回数据为空,请稍后再试", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.e("API", "onResponse: " + body);
        if (body.code < 0) {
            if (Settings.isOnline) {
                Toast.makeText(context, body.msg, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, String.format(Locale.CHINA, "code = %d and msg = %s", body.code, body.msg), Toast.LENGTH_SHORT).show();
            }
            if (body.code == -1) {
                context.startActivity(new Intent(context, LoginActivity.class));
                return;
            }
        }
        onCustomResponse(body.code,body.msg);
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void onFailure(Call<BaseResult> call, Throwable t) {
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

    public abstract void onCustomResponse(int code, String msg);
}
