package com.yusion.shanghai.yusion4s;

import android.app.Application;

import com.pgyersdk.crash.PgyCrashManager;

/**
 * Created by ice on 2017/8/9.
 */

public class Yusion4sApp extends Application{
    public static String TOKEN;
    public static String MOBILE;
    @Override
    public void onCreate() {
        super.onCreate();
        PgyCrashManager.register(this);
    }
}
