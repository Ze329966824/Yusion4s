package com.yusion.shanghai.yusion4s.ui.entrance;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.yusion.shanghai.yusion4s.BuildConfig;
import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.ActivityManager;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.retrofit.api.AuthApi;
import com.yusion.shanghai.yusion4s.retrofit.api.ConfigApi;
import com.yusion.shanghai.yusion4s.settings.Settings;
import com.yusion.shanghai.yusion4s.ui.MainActivity;
import com.yusion.shanghai.yusion4s.utils.PopupDialogUtil;
import com.yusion.shanghai.yusion4s.utils.SharedPrefsUtil;
import com.yusion.shanghai.yusion4s.utils.UpdateUtil;

import java.util.Date;

import static com.yusion.shanghai.yusion4s.settings.Settings.isOnline;

public class LaunchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        if (Settings.isOnline) {
            checkVersion();
        } else {
            checkServerUrl();
        }
    }

    private void checkServerUrl() {
        String str = SharedPrefsUtil.getInstance(this).getValue("SERVER_URL", "");
        if (!isOnline && !TextUtils.isEmpty(str)) {
            PopupDialogUtil.showTwoButtonsDialog(this, "还原", "确定", "服务器地址为：\n" + str, dialog -> {
                        Settings.SERVER_URL = "http://api.alpha.yusiontech.com:8000/";
                        dialog.dismiss();
                        SharedPrefsUtil.getInstance(LaunchActivity.this).putValue("SERVER_URL", "");
                        new AlertDialog.Builder(LaunchActivity.this)
                                .setMessage("重启app生效")
                                .setPositiveButton("确定", (dialog1, which1) -> ActivityManager.exit())
                                .show();
                    }, dialog -> {
                        Settings.SERVER_URL = str;
                        dialog.dismiss();
                        getConfigJson();
                    }
            );
        } else {
            getConfigJson();
        }
    }

    private void checkVersion() {
        String versionCode = BuildConfig.VERSION_NAME;
        AuthApi.update(this, this.getResources().getString(R.string.app_name), data -> {
            if (data != null) {
                int result = splitVersion(versionCode.substring(1)).compareTo(splitVersion(data.version));
                if (result < 0) {
                    UpdateUtil.showUpdateDialog(LaunchActivity.this, data.change_log, true, data.download_url);
                } else {
                    getConfigJson();
                }
            } else {
                getConfigJson();
            }
        });
    }


    private void getConfigJson() {
        ConfigApi.getConfigJson(LaunchActivity.this, this::goNextActivity);
    }

    private void goNextActivity() {
        if (!Settings.isOnline) {
            Toast.makeText(myApp, "当前服务器地址:\n" + Settings.SERVER_URL, Toast.LENGTH_SHORT).show();
        }
        AuthApi.checkToken(this, data -> {
            if (data.valid) {
                onTokenValid();
            } else {
                onTokenInvalid();
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

    private String splitVersion(String s) {
        String splitVersion = null;
        char[] str = s.toCharArray();
        splitVersion = String.valueOf(str[0]) + String.valueOf(str[2]) + String.valueOf(str[4]);
        return splitVersion;
    }

}
