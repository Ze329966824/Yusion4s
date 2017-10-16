package com.yusion.shanghai.yusion4s.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import cn.jpush.android.api.JPushInterface;
import io.sentry.Sentry;

/**
 * Created by LX on 2017/8/14.
 */

public class YusionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Intent i = new Intent(context, JpushDialogActivity.class);

            String string = bundle.getString(JPushInterface.EXTRA_EXTRA);
            if (TextUtils.isEmpty(string)) {
                return;
            }else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                int notificationID = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                String content = bundle.getString(JPushInterface.EXTRA_ALERT);
                JPushInterface.clearNotificationById(context, notificationID);
            }
            Log.e("EXTRA_EXTRA",string);
            Sentry.capture(string);
            i.putExtra("jsonObject", string);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (!TextUtils.isEmpty(string)) {
                context.startActivity(i);
            }

        }
    }
}
