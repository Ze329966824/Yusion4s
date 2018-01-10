package com.yusion.shanghai.yusion4s.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.umeng.analytics.MobclickAgent;
import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.Yusion4sApp;
import com.yusion.shanghai.yusion4s.settings.Settings;
import com.yusion.shanghai.yusion4s.ubt.UBT;
import com.yusion.shanghai.yusion4s.ui.entrance.LaunchActivity;
import com.yusion.shanghai.yusion4s.ui.main.SettingsActivity;
import com.yusion.shanghai.yusion4s.widget.TitleBar;

import java.util.List;

/**
 * Created by ice on 2017/8/3.
 */

public class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    protected Yusion4sApp myApp;
    public int WIDTH;
    public int HEIGHT;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityManager.addActivity(this);
        WIDTH = this.getWindowManager().getDefaultDisplay().getWidth();
        HEIGHT = this.getWindowManager().getDefaultDisplay().getHeight();
        myApp = ((Yusion4sApp) getApplication());
        if (!Settings.isOnline) {
            //Toast.makeText(this, getClass().getSimpleName(), Toast.LENGTH_SHORT).show();
        }
//        PgyCrashManager.register(this);
        //UBT.bind(BaseActivity.this);
//        UBT.bind(this);
    }

    public TitleBar initTitleBar(final Activity activity, String title) {
        TitleBar titleBar = (TitleBar) activity.findViewById(R.id.title_bar);
        titleBar.setLeftClickListener(view -> onBackPressed());
        titleBar.setImmersive(false);
        titleBar.setTitle(title);
        titleBar.setLeftTextColor(Color.BLACK);
        titleBar.setRightTextColor(Color.BLACK);
        titleBar.setLeftImageResource(R.mipmap.title_back_arrow);
        titleBar.setBackgroundResource(R.color.white);
        titleBar.setTitleColor(activity.getResources().getColor(R.color.black));
        titleBar.setDividerColor(activity.getResources().getColor(R.color.separate_line_color));
        return titleBar;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);//友盟统计
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        // 自定义摇一摇的灵敏度，默认为950，数值越小灵敏度越高。
//        PgyFeedbackShakeManager.setShakingThreshold(1000);
        // 以对话框的形式弹出
//        PgyFeedbackShakeManager.register(this);
        if (getClass().getSimpleName().equals(LaunchActivity.class.getSimpleName())) {
            UBT.addAppEvent(this, "app_start");
        }
        if (!Yusion4sApp.isForeground) {
            Yusion4sApp.isForeground = true;
            UBT.addAppEvent(this, "app_awake");
        }
        UBT.addPageEvent(this, "page_show", "activity", getClass().getSimpleName());
    }

    @Override
    protected void onPause() {
        super.onPause();

//        PgyFeedbackShakeManager.unregister();
        MobclickAgent.onPause(this);
        MobclickAgent.onPause(this);
        if (getClass().getSimpleName().equals(SettingsActivity.class.getSimpleName())) {
            SettingsActivity settingsActivity = (SettingsActivity) this;
            if (settingsActivity.finishByLoginOut) {
                return;
            }
        }

        UBT.addPageEvent(this, "page_hidden", "activity", getClass().getSimpleName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.removeActivity(this);
        //destroy后显示最新列表
//        for (AppCompatActivity activity : ActivityManager.list) {
//            Log.e("TAG2222", "onDestroy: " + activity.getClass().getSimpleName());
//        }
//        Log.e("TAG2222", "onDestroy: ---------");
//        adb shell dumpsys activity | grep com.yusion.shanghai.yusion4s
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!isAppOnForeground()) {
            Yusion4sApp.isForeground = false;
            UBT.addAppEvent(this, "app_pause");
        }
    }

    /**
     * 并没有杀死进程
     */
    public void reOpenApp() {
        Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        UBT.addAppEvent(this, "app_pause");
    }


    private boolean isAppOnForeground() {
        android.app.ActivityManager activityManager = (android.app.ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<android.app.ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) return false;

        for (android.app.ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName) && appProcess.importance == android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }
}
