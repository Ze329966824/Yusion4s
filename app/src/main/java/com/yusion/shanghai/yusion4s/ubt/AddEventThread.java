package com.yusion.shanghai.yusion4s.ubt;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.yusion.shanghai.yusion4s.ubt.sql.SqlLiteUtil;

import java.util.Date;

public class AddEventThread implements Runnable {
    private String action;
    private View view;
    private String pageName;
    private String action_value;

    private boolean isPageEvent;
    private boolean isAppEvent;
    private String object;
    private Context context;
    private String TAG = "UBT";

    public AddEventThread(Context context, String action, View view, String pageName, String action_value) {
        this.context = context;
        this.action = action;
        this.view = view;
        this.pageName = pageName;
        this.action_value = action_value;
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
            values.put("object", view.getClass().getSimpleName());
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
        SqlLiteUtil.insert(values);
//        Log.e(TAG, "run: 插入成功 action=" + action + ",page=" + pageName);
        Log.e(TAG, "run: 插入成功 ----- " + this);


        //....................


        UBT.sendUBTEvents(context, UBT.LIMIT);

    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
