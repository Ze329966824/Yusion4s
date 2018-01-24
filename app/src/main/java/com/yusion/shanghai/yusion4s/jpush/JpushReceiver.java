package com.yusion.shanghai.yusion4s.jpush;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.yusion.shanghai.yusion4s.Yusion4sApp;
import com.yusion.shanghai.yusion4s.base.ActivityManager;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.ui.order.OrderDetailActivity;
import com.yusion.shanghai.yusion4s.utils.AppUtils;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;
import io.sentry.Sentry;


public class JpushReceiver extends BroadcastReceiver {
    private NotificationManager nm;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (null == nm) {
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }


        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {

                openNotification(context,bundle);

            }
            Intent i = new Intent(context, JpushDialogActivity.class);
            String string = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Log.e("TAG", "onReceive: "+string);
            if (TextUtils.isEmpty(string)) {
                return;
            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                int notificationID = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                String content = bundle.getString(JPushInterface.EXTRA_ALERT);
                // 如果app在使用 就清掉通知栏推送  如果在后台就不清除
                if (AppUtils.isAppOnForeground()) {
                    JPushInterface.clearNotificationById(context, notificationID);
                }
            }
            Sentry.capture(string);
            i.putExtra("jsonObject", string);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(i);

            JSONObject jo = null;
            try {
                jo = new JSONObject(string);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String category = jo.optString("category");
            String username = jo.optString("username");
            Log.e("TAG", "onReceive: "+Yusion4sApp.ACCOUNT);
            Log.e("TAG", "onReceive: "+Yusion4sApp.isLogin);
            if (Yusion4sApp.isLogin && username.equals(Yusion4sApp.ACCOUNT)) {
                switch (category) {
                    case "login"://抢登
                        context.startActivity(i);
                        break;

                    case "application":
                        BaseActivity activity = (BaseActivity) ActivityManager.getActivity();
                        activity.title = jo.optString("title");
                        activity.content = jo.optString("content");
                        activity.app_id = jo.optString("app_id");
                        activity.vehicle_cond = jo.optString("vehicle_cond");
                        activity.initPopupWindow();
                        activity.showPopupWindow();
                        break;
                    default:
                        break;
                }
            }


        }
    }

    private void openNotification(Context context, Bundle bundle){
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        String myValue = "";
        String app_id = "";
        try {
            JSONObject extrasJson = new JSONObject(extras);
            myValue = extrasJson.optString("category");
            app_id = extrasJson.optString("app_id");
        } catch (Exception e) {
            return;
        }
        if (myValue.equals("application")) {
            Intent mIntent = new Intent(context, OrderDetailActivity.class);
            mIntent.putExtras(bundle);
            mIntent.putExtra("app_id",app_id);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mIntent);
        }
    }
}
