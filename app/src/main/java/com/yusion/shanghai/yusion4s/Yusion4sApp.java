package com.yusion.shanghai.yusion4s;

import android.app.Application;
import android.text.TextUtils;

import com.pgyersdk.crash.PgyCrashManager;
import com.yusion.shanghai.yusion4s.bean.config.ConfigResp;
import com.yusion.shanghai.yusion4s.bean.user.UserInfoBean;
import com.yusion.shanghai.yusion4s.crash.CrashHandler;
import com.yusion.shanghai.yusion4s.utils.SharedPrefsUtil;

import cn.jpush.android.api.JPushInterface;
import io.sentry.Sentry;
import io.sentry.android.AndroidSentryClientFactory;

/**
 * Created by ice on 2017/8/9.
 */

public class Yusion4sApp extends Application {
    private static Yusion4sApp myApplication = null;
    public static String TOKEN;
    public static String ACCOUNT;
    public static ConfigResp CONFIG_RESP;
    public static UserInfoBean USERINFOBEAN;

    public static boolean isLogin;
    private static String reg_id;

    public void onCreate() {
        super.onCreate();
        Sentry.init("http://99c65c10b5564f8280e1d8230cb97880:18d30de1e6c64542837a7d82bbd33e9c@116.62.161.180:9002/6", new AndroidSentryClientFactory(this));
        Sentry.capture("xxx");
        PgyCrashManager.register(this);
//        initCrashReporter();
        jpush();
    }

    private void initCrashReporter() {
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
    }

    public void clearUserData() {
        TOKEN = "";
        ACCOUNT = "";
        USERINFOBEAN = null;

        SharedPrefsUtil.getInstance(this).putValue("token", TOKEN);
        SharedPrefsUtil.getInstance(this).putValue("account", ACCOUNT);
    }

    public static Yusion4sApp getInstance() {
        return myApplication;
    }

    private void jpush() {
        JPushInterface.setDebugMode(true);
        // 初始化 JPush
        JPushInterface.init(this);
        while (TextUtils.isEmpty(reg_id)) {
            reg_id = JPushInterface.getRegistrationID(Yusion4sApp.this);
            SharedPrefsUtil.getInstance(this).putValue("reg_id", reg_id);
        }
    }
}
