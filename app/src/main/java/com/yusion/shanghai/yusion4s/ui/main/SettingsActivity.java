package com.yusion.shanghai.yusion4s.ui.main;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pgyersdk.update.PgyUpdateManager;
import com.yusion.shanghai.yusion4s.BuildConfig;
import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.retrofit.Api;
import com.yusion.shanghai.yusion4s.settings.Settings;
import com.yusion.shanghai.yusion4s.ubt.UBT;
import com.yusion.shanghai.yusion4s.ui.entrance.WebViewActivity;
import com.yusion.shanghai.yusion4s.utils.ApiUtil;
import com.yusion.shanghai.yusion4s.utils.AppUtils;
import com.yusion.shanghai.yusion4s.utils.PopupDialogUtil;
import com.yusion.shanghai.yusion4s.utils.SharedPrefsUtil;
import com.yusion.shanghai.yusion4s.utils.UpdateUtil;

import static com.yusion.shanghai.yusion4s.settings.Settings.isOnline;

public class SettingsActivity extends BaseActivity implements View.OnClickListener {
    private String versionCode;
    public boolean finishByLoginOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        TextView versionCodeTv = (TextView) findViewById(R.id.settings_version_code_tv);
        versionCode = Settings.isOnline ? BuildConfig.VERSION_NAME : Settings.SERVER_URL;
        versionCodeTv.setText(versionCode);
        initTitleBar(this, getResources().getString(R.string.main_setting_title));
        //长按更改服务器地址
        findViewById(R.id.main_setting_logout_layout).setOnLongClickListener(v -> {
            if (!isOnline) {
                EditText editText = new EditText(SettingsActivity.this);
                editText.setText(Settings.SERVER_URL);
                new android.app.AlertDialog.Builder(SettingsActivity.this).setTitle("服务器地址：")
                        .setView(editText)
                        .setCancelable(false)
                        .setPositiveButton("确定", (dialog, which) -> {
                            SharedPrefsUtil.getInstance(SettingsActivity.this).putValue("SERVER_URL", editText.getText().toString());
                            dialog.dismiss();
                            new AlertDialog.Builder(SettingsActivity.this)
                                    .setMessage("重启app生效")
                                    .setPositiveButton("确定", (dialog1, which1) -> reOpenApp())
                                    .show();
                        }).show();
            }
            return true;
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //用户协议
            case R.id.main_setting_agreement_layout:
                Intent intent = new Intent(SettingsActivity.this, WebViewActivity.class);
                intent.putExtra("type", "Agreement");
                startActivity(intent);
                break;
            //退出登录
            case R.id.main_setting_logout_layout:
                if (!isFinishing()) {
                    showLogoutDialog();
                }
                break;
            //版本号
            case R.id.main_setting_version_name_layout:
                checkServerUrl();
                break;
            default:
                break;
        }
    }

    private void checkServerUrl() {
        if (Settings.isOnline) {
            //product：调用oss接口更新
            ApiUtil.requestUrl4Data(this, Api.getAuthService().update("yusion4s"), data -> {
//                AuthApi.update(this, "yusion4s", data -> {
                if (data != null) {
                    int result = splitVersion(versionCode.substring(1)).compareTo(splitVersion(data.version));
                    if (result < 0) {
                        UpdateUtil.showUpdateDialog(SettingsActivity.this, data.change_log, false, data.download_url);
                    } else {
                        Toast.makeText(this, "已经是最新的版本啦！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "已经是最新的版本啦！", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            //alpha：蒲公英更新
            PgyUpdateManager.setIsForced(false); //设置是否强制更新。true为强制更新；false为不强制更新（默认值）。
            PgyUpdateManager.register(this, null);
            //带回调更新
            //initUpdateListener();
        }
    }

    private void showLogoutDialog() {
        PopupDialogUtil.showTwoButtonsDialog(SettingsActivity.this,R.layout.popup_dialog_logout,"是否退出？","是","否", new PopupDialogUtil.OnPopupClickListener() {
            @Override
            public void onOkClick(Dialog dialog) {
                dialog.dismiss();
                AppUtils.logout(SettingsActivity.this);
                UBT.addPageEvent(SettingsActivity.this, "page_hidden", "activity", getClass().getSimpleName());
                finish();
                finishByLoginOut = true;
            }
        });
    }

    private String splitVersion(String s) {
        String ss = null;
        char[] str = s.toCharArray();
        ss = String.valueOf(str[0]) + String.valueOf(str[2]) + String.valueOf(str[4]);
        return ss;
    }

}
