package com.yusion.shanghai.yusion4s.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.pgyersdk.crash.PgyCrashManager;
import com.umeng.analytics.MobclickAgent;
import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.Yusion4sApp;
import com.yusion.shanghai.yusion4s.ubt.UBT;
import com.yusion.shanghai.yusion4s.ui.entrance.LaunchActivity;
import com.yusion.shanghai.yusion4s.widget.TitleBar;

import static com.instabug.library.Instabug.isAppOnForeground;

/**
 * Created by ice on 2017/8/3.
 */

public class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    protected Yusion4sApp myApp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.addActivity(this);
        myApp = ((Yusion4sApp) getApplication());
        PgyCrashManager.register(this);

    }

    public TitleBar initTitleBar(final Activity activity, String title) {
        TitleBar titleBar = (TitleBar) activity.findViewById(R.id.title_bar);
        titleBar.setLeftClickListener(view -> activity.finish());
        titleBar.setImmersive(false);
        titleBar.setTitle(title);
        titleBar.setLeftImageResource(R.mipmap.title_back_arrow);
        titleBar.setBackgroundResource(R.color.system_color);
        titleBar.setTitleColor(activity.getResources().getColor(R.color.title_bar_text_color));
        titleBar.setDividerColor(activity.getResources().getColor(R.color.system_color));
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
        UBT.addPageEvent(this, "page_hidden", "activity", getClass().getSimpleName());
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.removeActivity(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!isAppOnForeground()) {
            Yusion4sApp.isForeground = false;
            UBT.addAppEvent(this, "app_pause");
        }
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        UBT.addAppEvent(this, "app_pause");
    }
}
