package com.yusion.shanghai.yusion4s.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.yusion.shanghai.yusion4s.Yusion4sApp;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnVoidCallBack;
import com.yusion.shanghai.yusion4s.ubt.UBT;
import com.yusion.shanghai.yusion4s.ui.entrance.LoginActivity;
import com.yusion.shanghai.yusion4s.ui.main.SettingsActivity;

import static com.yusion.shanghai.yusion4s.base.ActivityManager.finish;

/**
 * Created by LX on 2017/12/20.
 */

public class AppUtils {

    public static void logout(Context context, String pageName) {
        Toast.makeText(context, "正在退出，请稍等...", Toast.LENGTH_SHORT).show();
        UBT.addPageEvent(context, "page_hidden", "activity", pageName);
        UBT.sendAllUBTEvents(context, () -> {
        });
        Yusion4sApp.getInstance().clearUserData();
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }





    public static void awaken(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();  // 当前Activity获得packageManager对象

            if (packageManager.getLaunchIntentForPackage("com.yusion.shanghai.yusion") != null) {
                PopupDialogUtil.showTwoButtonsDialog(context, "是否打开Yusion", "打开", "取消", dialog -> {
                    Intent intent = new Intent();
                    intent = packageManager.getLaunchIntentForPackage("com.yusion.shanghai.yusion");
                    context.startActivity(intent);
                });
            }
        } catch (Exception e) {
        }
    }

}
