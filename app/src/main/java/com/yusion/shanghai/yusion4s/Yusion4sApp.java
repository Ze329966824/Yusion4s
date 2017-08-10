package com.yusion.shanghai.yusion4s;

import android.app.Application;

import com.pgyersdk.crash.PgyCrashManager;
import com.yusion.shanghai.yusion4s.bean.config.ConfigResp;
import com.yusion.shanghai.yusion4s.bean.user.UserInfoBean;
import com.yusion.shanghai.yusion4s.utils.SharedPrefsUtil;

/**
 * Created by ice on 2017/8/9.
 */

public class Yusion4sApp extends Application{
    private static Yusion4sApp myApplication = null;
    public static String TOKEN;
    public static String MOBILE;
    public static ConfigResp CONFIG_RESP;
    public static UserInfoBean USERINFOBEAN;
    @Override
    public void onCreate() {
        super.onCreate();
        PgyCrashManager.register(this);
    }

    public void clearUserData() {
        TOKEN = "";
        MOBILE = "";
        USERINFOBEAN = null;

        SharedPrefsUtil.getInstance(this).putValue("token", TOKEN);
        SharedPrefsUtil.getInstance(this).putValue("mobile", MOBILE);
    }
    public static Yusion4sApp getInstance() {
        return myApplication;
    }
}
