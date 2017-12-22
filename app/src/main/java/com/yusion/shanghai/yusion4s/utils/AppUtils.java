package com.yusion.shanghai.yusion4s.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.yusion.shanghai.yusion4s.Yusion4sApp;
import com.yusion.shanghai.yusion4s.base.ActivityManager;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnVoidCallBack;
import com.yusion.shanghai.yusion4s.ubt.UBT;
import com.yusion.shanghai.yusion4s.ui.entrance.LoginActivity;
import com.yusion.shanghai.yusion4s.ui.main.SettingsActivity;

import java.util.ArrayList;
import java.util.List;

import static com.yusion.shanghai.yusion4s.base.ActivityManager.finish;

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



    public static void exit(){
        for (Activity activity : ActivityManager.list) {
            if (activity!=null) {
                activity.finish();
            }
        }
        System.exit(0);
    }



}
