package com.yusion.shanghai.yusion4s.ui.entrance;

import android.app.AlertDialog;
import android.app.LauncherActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.Yusion4sApp;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.bean.token.CheckTokenResp;
import com.yusion.shanghai.yusion4s.retrofit.api.AuthApi;
import com.yusion.shanghai.yusion4s.retrofit.api.ConfigApi;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.settings.Settings;
import com.yusion.shanghai.yusion4s.ui.MainActivity;
import com.yusion.shanghai.yusion4s.utils.SharedPrefsUtil;

import java.util.Date;

public class LaunchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        Yusion4sApp.TOKEN = SharedPrefsUtil.getInstance(this).getValue("token", "");
        String str = SharedPrefsUtil.getInstance(this).getValue("SERVER_URL", "");

        EditText editText = new EditText(this);
        editText.setText(str);
        if (!str.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setTitle("请再次确认服务器地址！")
                    .setView(editText)
                    .setCancelable(false)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Settings.SERVER_URL = editText.getText().toString();
                            getConfigJson();
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("还原", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getConfigJson();
                        }
                    }).show();
        } else {
            getConfigJson();
        }

    }


    private void getConfigJson() {
        ConfigApi.getConfigJson(LaunchActivity.this, resp -> {
            goNextActivity();
        });
    }

    private void goNextActivity() {
        AuthApi.checkToken(this, new OnItemDataCallBack<CheckTokenResp>() {
            @Override
            public void onItemDataCallBack(CheckTokenResp data) {
                if (data.valid) {
                    onTokenValid();
                } else {
                    onTokenInvalid();
                }
            }
        });
    }

    private void onTokenInvalid() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }


    private void onTokenValid() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private long lastClickBackTime = 0;

    @Override
    public void onBackPressed() {
        if (lastClickBackTime == 0) {
            lastClickBackTime = new Date().getTime();
        } else {
            long currentClickBackTime = new Date().getTime();
            if (currentClickBackTime - lastClickBackTime < 1000) {
                new AlertDialog.Builder(this).setMessage("是否退出APP?")
                        .setNegativeButton("取消", (dialog, which) -> {
                            dialog.dismiss();
                            lastClickBackTime = currentClickBackTime;
                        })
                        .setPositiveButton("确定", (dialog, which) -> super.onBackPressed())
                        .show();
            } else {
                lastClickBackTime = currentClickBackTime;
            }
        }
    }
}
