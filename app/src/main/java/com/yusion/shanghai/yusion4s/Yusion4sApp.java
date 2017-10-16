package com.yusion.shanghai.yusion4s;

import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.Log;

import com.instabug.library.Instabug;
import com.instabug.library.invocation.InstabugInvocationEvent;
import com.pgyersdk.crash.PgyCrashManager;
import com.umeng.analytics.MobclickAgent;
import com.yusion.shanghai.yusion4s.bean.config.ConfigResp;
import com.yusion.shanghai.yusion4s.bean.user.UserInfoBean;
import com.yusion.shanghai.yusion4s.ubt.sql.SqlLiteUtil;
import com.yusion.shanghai.yusion4s.utils.SharedPrefsUtil;

import cn.jpush.android.api.JPushInterface;
import io.sentry.Sentry;
import io.sentry.android.AndroidSentryClientFactory;

/**
 * Created by ice on 2017/8/9.
 */

public class Yusion4sApp extends MultiDexApplication {
    private static Yusion4sApp myApplication = null;
    public static String TOKEN;
    public static String ACCOUNT;
    //    public static String MOBILE;
    public static ConfigResp CONFIG_RESP;
    public static UserInfoBean USERINFOBEAN;
    public static boolean isForeground;

    public static boolean isLogin;
    private static String reg_id;

    @Override
    public void onCreate() {
        super.onCreate();
        TOKEN = SharedPrefsUtil.getInstance(this).getValue("token", "");
        ACCOUNT = SharedPrefsUtil.getInstance(this).getValue("account", "");
        if (BuildConfig.isOnline) {
            Sentry.init("http://99c65c10b5564f8280e1d8230cb97880:18d30de1e6c64542837a7d82bbd33e9c@116.62.161.180:9002/6", new AndroidSentryClientFactory(this));
        } else {
            Sentry.init("http://a38b78ed9d104631998185e97f1465ff:d7046f67331d4f8d860d922b0e02bc55@116.62.161.180:9002/8", new AndroidSentryClientFactory(this));
        }
        PgyCrashManager.register(this);
        jpush();
        umeng();
        instabug();
        SqlLiteUtil.init(this);
    }

    private void instabug() {
        new Instabug.Builder(this, "fac6ff642eec6bf5599d893a0a1224e3").setInvocationEvent(InstabugInvocationEvent.SHAKE).build();
    }

    private void umeng() {
        //禁止默认的页面统计方式
        MobclickAgent.openActivityDurationTrack(false);
        //捕获程序崩溃日志
        MobclickAgent.setCatchUncaughtExceptions(true);
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

    public static ConfigResp getConfigResp() {
        if (CONFIG_RESP == null) {
            Log.e("TAG", "getConfigResp: is null");
            Sentry.capture("getConfigResp: is nul");
        } else {

            Log.e("TAG", "getConfigResp: not null");
            Sentry.capture("getConfigResp: not nul");
        }
        return CONFIG_RESP;
    }

    public static void setConfigResp(ConfigResp configResp) {
        CONFIG_RESP = configResp;
    }
}
