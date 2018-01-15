package com.yusion.shanghai.yusion4s.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.yusion.shanghai.yusion4s.Yusion4sApp;
import com.yusion.shanghai.yusion4s.base.ActivityManager;
import com.yusion.shanghai.yusion4s.ubt.UBT;
import com.yusion.shanghai.yusion4s.ui.entrance.LoginActivity;

import java.util.List;

import static com.instabug.library.Instabug.getApplicationContext;

/**
 * Created by LX on 2017/12/20.
 */

public class AppUtils {


    public static void logout(Context context) {
        Toast.makeText(context, "正在退出，请稍等...", Toast.LENGTH_SHORT).show();
        UBT.sendAllUBTEvents(context, () -> {
        });
        Yusion4sApp.getInstance().clearUserData();
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }


    public static void exit() {
        for (Activity activity : ActivityManager.list) {
            if (activity != null) {
                activity.finish();
            }
        }
        System.exit(0);
    }

    public static boolean isAppOnForeground() {
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
