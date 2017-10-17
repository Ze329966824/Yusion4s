package com.yusion.shanghai.yusion4s.ubt;

import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yusion.shanghai.yusion4s.ubt.sql.SqlLiteUtil;

import java.util.Date;

public class AddEventThread implements Runnable {
    private String action;
    private View view;
    private String pageName;//MainActivity
    private String widget;
    private String action_value;

    private boolean isPageEvent;
    private boolean isAppEvent;
    private String object;
    private Context context;
    private String TAG = "UBT";

    public AddEventThread(Context context, String action, View view, String pageName, String action_value, String widget) {
        this.context = context;
        this.action = action;
        this.pageName = pageName;
        this.action_value = action_value;
        this.view = view;
        this.widget = widget;
    }

    public AddEventThread(Context context, String action, String object, String pageName) {
        this.context = context;
        this.action = action;
        this.pageName = pageName;
        this.object = object;
        isPageEvent = true;
    }

    public AddEventThread(Context context, String action) {
        this.context = context;
        this.action = action;
        isAppEvent = true;
    }

    @Override
    public void run() {
        ContentValues values = new ContentValues();
        if (isPageEvent) {
            values.put("object", object);
        } else if (isAppEvent) {
            values.put("object", "");
        } else {
            String object = "";
            if (view instanceof EditText) {
                object = "edit_text";
            } else if (view instanceof Button) {
                object = "button";
            }else if (view instanceof TextView) {
                object = "text_view";
            }else {
                object = view.getClass().getSimpleName();
            }
            values.put("object", object);
        }

        values.put("action", action);
        values.put("action_value", action_value);

        if (isAppEvent) {
            values.put("page", "");
            values.put("page_cn", "");
        } else {
            values.put("page", UBTCollections.getPageNm(pageName));
            values.put("page_cn", UBTCollections.getPageNmCn(pageName));
        }
        values.put("ts", new Date().getTime());

        if (!TextUtils.isEmpty(widget)) {
            values.put("widget", widget);
            values.put("widget_cn", UBTCollections.getWidgetNmCn(widget));
        }

        for (String s : values.keySet()) {
            Log.e("VALUES", "run: " + values.get(s));
        }

        SqlLiteUtil.insert(values);
//        Log.e(TAG, "run: 插入成功 action=" + action + ",page=" + pageName);
        Log.e(TAG, "run: 插入成功 ----- " + AddEventThread.this.toString());


        //....................


        UBT.sendUBTEvents(context, UBT.LIMIT);

    }

    @Override
    public String toString() {
        return "AddEventThread{" +
                "action='" + action + '\'' +
                ", view=" + view +
                ", pageName='" + pageName + '\'' +
                ", action_value='" + action_value + '\'' +
                ", isPageEvent=" + isPageEvent +
                ", isAppEvent=" + isAppEvent +
                ", object='" + object + '\'' +
                ", context=" + context +
                ", TAG='" + TAG + '\'' +
                '}';
    }
}
