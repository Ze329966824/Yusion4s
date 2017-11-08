package com.yusion.shanghai.yusion4s.ui.entrance;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.yusion.shanghai.yusion4s.BuildConfig;
import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.bean.token.CheckTokenResp;
import com.yusion.shanghai.yusion4s.retrofit.api.AuthApi;
import com.yusion.shanghai.yusion4s.retrofit.api.ConfigApi;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
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

//        ApplicationInfo applicationInfo = null;
//        try {
//            applicationInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
//            String pgyer_appid = applicationInfo.metaData.getString("PGYER_APPID");
//            Log.e("TAG", "onCreate: pgyer_appid = " + pgyer_appid);
//        } catch (PackageManager.NameNotFoundException e) {
//            Log.e("TAG", "onCreate: " + e);
//            e.printStackTrace();
//        }
//        Log.e("TAG", "onCreate: Settings.isOnline = " + Settings.isOnline);
//        Log.e("TAG", "onCreate: Settings.SERVER_URL = " + Settings.SERVER_URL);
//        Log.e("TAG", "onCreate: Settings.OSS_SERVER_URL = " + Settings.OSS_SERVER_URL);

//        Yusion4sApp.TOKEN = SharedPrefsUtil.getInstance(this).getValue("token", "");

        if (Settings.isOnline) {
            checkVersion();
        } else {
            checkServerUrl();
        }

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(10000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (Settings.isOnline) {
//                            checkVersion();
//                        } else {
//                            checkServerUrl();
//                        }
//                    }
//                });
//            }
//        }).start();

    }

    private String str = SharedPrefsUtil.getInstance(this).getValue("SERVER_URL", "");

    private void checkServerUrl() {
        if (!isOnline) {
//            String str = SharedPrefsUtil.getInstance(this).getValue("SERVER_URL", "");
            if (!TextUtils.isEmpty(str)) {
                PopupDialogUtil.showTwoButtonsDialog(this, "还原", "确定", "服务器地址为：\n" + str, new PopupDialogUtil.OnOkClickListener() {                               @Override
                                public void onOkClick(Dialog dialog) {
//                                YusionApp.isChangeURL = false;
                                Settings.SERVER_URL = "http://api.alpha.yusiontech.com:8000/";
//                                Api.initRetrofit();
                                dialog.dismiss();
                                SharedPrefsUtil.getInstance(LaunchActivity.this).putValue("SERVER_URL", "");
                                Toast.makeText(myApp, "还原成功,请自己重启APP！！！", Toast.LENGTH_SHORT).show();
//                                    getConfigJson();
                            }
                        }, new PopupDialogUtil.OnCancelClickListener() {
                            @Override
                            public void onCancelClick(Dialog dialog) {
                                Settings.SERVER_URL = str;
//                                Api.initRetrofit();
                                dialog.dismiss();
                                getConfigJson();
                            }
                        }

                );
//                new AlertDialog.Builder(this)
//                        .setTitle("请确认服务器地址：")
//                        .setMessage(str)
//                        .setPositiveButton("是", (dialog, which) -> {
//                            Settings.SERVER_URL = str;
//                            getConfigJson();
//                            dialog.dismiss();
//                        })
//                        .setNegativeButton("否", (dialog, which) -> {
//                            getConfigJson();
//                            dialog.dismiss();
//                        }).show();
            } else {
                getConfigJson();
            }
        } else {
            getConfigJson();
        }
    }

    private void checkVersion() {
        String versionCode = BuildConfig.VERSION_NAME;
        AuthApi.update(this, "yusion4s", data -> {
            if (data != null) {
//                Toast.makeText(this, splitVersion(versionCode.substring(1)), Toast.LENGTH_SHORT).show();
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
        ConfigApi.getConfigJson(LaunchActivity.this, () -> goNextActivity());
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

    private String splitVersion(String s) {
        String ss = null;
        char[] str = s.toCharArray();
        ss = String.valueOf(str[0]) + String.valueOf(str[2]) + String.valueOf(str[4]);
        return ss;
    }

}
