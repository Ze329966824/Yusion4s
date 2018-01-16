package com.yusion.shanghai.yusion4s.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.yusion.shanghai.yusion4s.base.ActivityManager;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.utils.AppUtils;

import cn.jpush.android.api.JPushInterface;
import io.sentry.Sentry;


public class JpushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Intent i = new Intent(context, JpushDialogActivity.class);
            String string = bundle.getString(JPushInterface.EXTRA_EXTRA);
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

            BaseActivity activity = (BaseActivity) ActivityManager.getActivity();
            activity.initPopupWindow();
            activity.showPopupWindow();
        }
    }
}
