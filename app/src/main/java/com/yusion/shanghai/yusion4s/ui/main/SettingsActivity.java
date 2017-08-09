package com.yusion.shanghai.yusion4s.ui.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.yusion.shanghai.yusion4s.BuildConfig;
import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.Yusion4sApp;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.ui.entrance.LoginActivity;

public class SettingsActivity extends BaseActivity  implements View.OnClickListener{
    private String desc;
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        TextView versionCodeTv = (TextView) findViewById(R.id.settings_version_code_tv);
        versionCodeTv.setText(BuildConfig.VERSION_NAME);
        initTitleBar(this, getResources().getString(R.string.main_setting_title));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.main_setting_agreement_layout:
//                Intent intent2 = new Intent(SettingsActivity.this, WebViewActivity.class);
//                intent2.putExtra("title", getResources().getString(R.string.main_setting_agreement));
//                intent2.putExtra("url", JsonUtils.getConfigValueFromKey("agreement_url"));
//                startActivity(intent2);
//                break;
            case R.id.main_setting_logout_layout:
                showLogoutDialog();

                break;
            case R.id.main_setting_version_name_layout:
//                PgyUpdateManager.setIsForced(true); //设置是否强制更新。true为强制更新；false为不强制更新（默认值）。
//                PgyUpdateManager.register(this, null);

//                //带回调更新
//                initUpdateListener();
                break;
        }
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(SettingsActivity.this)
                .setCancelable(true)
                .setTitle(getResources().getString(R.string.mine_logout_dialog_title))
                .setPositiveButton(getResources().getString(R.string.mine_logout_dialog_sure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        dialog.dismiss();
                        logout();

                    }
                })
                .setNegativeButton(getResources().getString(R.string.mine_logout_dialog_cancle), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        dialog.dismiss();


                    }
                })
                .setMessage(getResources().getString(R.string.mine_logout_dialog_msg))
                .show();
    }

    private void logout() {
        ((Yusion4sApp) getApplication()).clearUserData();
        startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
        finish();
    }
    private void initUpdateListener() {
        PgyUpdateManager.register(SettingsActivity.this, null, new UpdateManagerListener() {
            @Override
            public void onNoUpdateAvailable() {
                Toast.makeText(SettingsActivity.this,"已经是最新版本，不需要更新",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUpdateAvailable(String s) {
//                Log.e("result:           ", s);
                final AppBean appBean = getAppBeanFromString(s);
                desc = appBean.getReleaseNote();
                url = appBean.getDownloadURL();
//                Log.e("desc=           ", desc);
//                Log.e("url=           ", url);
//                UpdateUtil.showUpdateDialog(SettingsActivity.this, desc, true, url);
                UpdateManagerListener.updateLocalBuildNumber(s);
            }
        });
    }
}
