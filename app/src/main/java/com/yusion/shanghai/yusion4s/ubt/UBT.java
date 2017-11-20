package com.yusion.shanghai.yusion4s.ubt;
/*
 * 类描述：
 * 伟大的创建人：ice   
 * 不可相信的创建时间：17/6/13 上午9:41 
 * 爱信不信的修改时间：17/6/13 上午9:41 
 * @version
 */

import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.retrofit.api.UBTApi;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnVoidCallBack;
import com.yusion.shanghai.yusion4s.settings.Settings;
import com.yusion.shanghai.yusion4s.ubt.annotate.BindView;
import com.yusion.shanghai.yusion4s.ubt.bean.UBTData;
import com.yusion.shanghai.yusion4s.ubt.sql.SqlLiteUtil;
import com.yusion.shanghai.yusion4s.ubt.sql.UBTEvent;
import com.yusion.shanghai.yusion4s.utils.SharedPrefsUtil;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 1.在application中调用SqlLiteUtil.init(this);
 * 2.页面
 * *UBT.addPageEvent(this, "page_hidden", "activity", getClass().getSimpleName());
 * *UBT.addPageEvent(this, "page_show", "activity", getClass().getSimpleName());
 * *UBT.addAppEvent(this, "app_start");
 * *UBT.addAppEvent(this, "app_awake");
 * 3.控件
 * *UBT.bind(this, view, getClass().getSimpleName()
 * *UBT.bind(this);
 * *焦点改变事件
 * -   顶层布局添加
 * -   android:focusableInTouchMode="true"
 * -   android:orientation="vertical"
 * btn.setOnClickListener(new View.OnClickListener() {
 *
 * @Override public void onClick(View v) {
 * v.setFocusable(true);
 * v.setFocusableInTouchMode(true);
 * v.requestFocus();
 * v.requestFocusFromTouch();
 * }
 * });
 * btn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
 * @Override public void onFocusChange(View v, boolean hasFocus) {
 * if (hasFocus) {
 * v.clearFocus();
 * //doSomething
 * }
 * }
 * });
 */
public class UBT {

    public static int LIMIT;

    static {
        if (Settings.isOnline) {
            LIMIT = 20;
        } else {
            LIMIT = 10;
        }
    }

    private static ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();

    public static void sendAllUBTEvents(Context context) {
        sendUBTEvents(context, 0, null);
    }

    public static void sendAllUBTEvents(Context context, OnVoidCallBack callBack) {
        sendUBTEvents(context, 0, callBack);
    }

    public static void sendUBTEvents(Context context, int limit) {
        sendUBTEvents(context, limit, null);
    }

    /**
     * @param context
     * @param limit   limit为0时发送所有数据 不为0时若数据数量大于limit条则发送limit条
     */
    public static void sendUBTEvents(Context context, int limit, OnVoidCallBack callBack) {
        singleThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                String TAG = "UBT-DETAIL";
                //没有token和account的数据暂不发送
                if (TextUtils.isEmpty(SharedPrefsUtil.getInstance(context).getValue("token", ""))
                        || TextUtils.isEmpty(SharedPrefsUtil.getInstance(context).getValue("account", ""))) {
                    Log.i(TAG, "run: account或token为空 禁止发送");
                    return;
                }
                Cursor cursor = SqlLiteUtil.query(null, null, null, null);
                int count = cursor.getCount();
                if (count > limit) {
                    Log.i(TAG, "run:共有 " + count);
                    Cursor query;
                    if (limit == 0) {
                        query = SqlLiteUtil.query(null, null, null, null);
                    } else {
                        query = SqlLiteUtil.query(null, null, null, String.valueOf(UBT.LIMIT));
                    }
                    Log.i(TAG, "run:要删除的 " + query.getCount());
                    query.moveToFirst();
                    List<Long> tss = new ArrayList<>();
                    List<UBTEvent> data = new ArrayList<>();
                    while (query.moveToNext()) {
                        tss.add(query.getLong(query.getColumnIndex("ts")));
                        UBTEvent ubtEvent = new UBTEvent();
                        ubtEvent.object = query.getString(query.getColumnIndex("object"));
                        ubtEvent.action = query.getString(query.getColumnIndex("action"));
                        ubtEvent.page = query.getString(query.getColumnIndex("page"));
                        ubtEvent.page_cn = query.getString(query.getColumnIndex("page_cn"));
                        ubtEvent.ts = query.getLong(query.getColumnIndex("ts"));
                        ubtEvent.widget = query.getString(query.getColumnIndex("widget"));
                        ubtEvent.widget_cn = query.getString(query.getColumnIndex("widget_cn"));
                        ubtEvent.action_value = query.getString(query.getColumnIndex("action_value"));
                        data.add(ubtEvent);
                    }
                    Log.i(TAG, "run: " + tss);

                    //发送
                    UBTData req = new UBTData(context);
                    UBTData.DataBean dataBean = new UBTData.DataBean();
                    dataBean.category = "ubt";
                    dataBean.mobile = SharedPrefsUtil.getInstance(context).getValue("mobile", null);
                    dataBean.ubt_list = data;
                    req.data.add(dataBean);
                    Log.i(TAG, "run: 正在发送");
                    try {
                        if (UBTApi.getUBTService().postUBTData(req).execute().isSuccessful()) {
                            Log.i(TAG, "run: 发送成功");
                            for (Long aLong : tss) {
                                SqlLiteUtil.delete("ts = ?", new String[]{String.valueOf(aLong)});
                            }
                            if (callBack != null) {
                                callBack.callBack();
                            }
                        }
                    } catch (IOException e) {
                        Log.i(TAG, "run: " + e);
                    }
                }
            }
        });
    }

    public static void bind(final Object object, View sourceView, String pageName) {
        for (Field field : object.getClass().getDeclaredFields()) {
            Annotation[] annotations = field.getAnnotations();
            if (annotations.length == 0) {
                continue;
            }
            for (Annotation annotation : annotations) {
                if (annotation instanceof BindView) {
                    BindView viewAnnotation = (BindView) annotation;
                    View view = sourceView.findViewById(viewAnnotation.id());
                    view.setTag(R.id.UBT_WIDGET, ((BindView) annotation).widgetName());
                    try {
                        field.setAccessible(true);
                        field.set(object, view);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    //按钮才监听点击事件
                    if (view instanceof Button) {
                        processorOnClick(object, viewAnnotation.onClick(), view, pageName);
                    }
                    if (view instanceof EditText) {
                        processorOnFocusChange(object, viewAnnotation.onFocusChange(), view, pageName);
                    }
//                    if (view instanceof CompoundButton) {
//                        processorOnCheckedChange(object, viewAnnotation.onCheckedChanged(), (CompoundButton) view, pageName);
//                    }
                    //只有最原生的textview才有文本改变事件
                    //edittext没有文本改变事件
                    if (view instanceof TextView && !(view instanceof EditText)) {
                        processorOnTextChange((TextView) view, pageName);
                    }
//                    processorOnTouch(object, viewAnnotation.onTouch(), view, pageName);
                }
            }
        }
    }

    public static void bind(AppCompatActivity activity) {
        bind(activity, activity.getWindow().getDecorView(), activity.getClass().getSimpleName());
    }

    private static void processorOnClick(final Object object, final String methodName, final View view, final String pageName) {
        view.setOnClickListener(v -> {
            try {
                addEvent(view.getContext(), "click", view, pageName);
                try {
                    final Method method = object.getClass().getDeclaredMethod(methodName, View.class);
                    method.setAccessible(true);
                    method.invoke(object, view);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    private static void processorOnFocusChange(final Object object, final String methodName, final View view, final String pageName) {
        view.setOnFocusChangeListener((v, hasFocus) -> {
            try {
                try {
                    final Method method = object.getClass().getDeclaredMethod(methodName, View.class, boolean.class);
                    method.setAccessible(true);
                    method.invoke(object, view, hasFocus);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }

                String action = hasFocus ? "focus_in" : "focus_out";
                addEvent(view.getContext(), action, view, pageName, ((EditText) view).getText().toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        });

    }

    private static void processorOnTextChange(final TextView view, final String pageName) {
        view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    return;
                }
                addEvent(view.getContext(), "text_change", view, pageName, s.toString());
            }
        });
    }

    private static void processorOnCheckedChange(final Object object, final String methodName, final CompoundButton view, final String pageName) {
        view.setOnCheckedChangeListener((buttonView, isChecked) -> {
            try {
                addEvent(view.getContext(), "checked_change", view, pageName, isChecked ? "checked" : "unchecked");
                try {
                    final Method method = object.getClass().getDeclaredMethod(methodName, View.class, boolean.class);
                    method.setAccessible(true);
                    method.invoke(object, view, isChecked);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }

    private static void processorOnTouch(final Object object, final String methodName, final View view, final String pageName) {
        view.setOnTouchListener((v, event) -> {
//                String operation;
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    operation = "down";
//                } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                    operation = "up";
//                } else {
//                    try {
//                        final Method method = object.getClass().getDeclaredMethod(methodName, View.class);
//                        method.setAccessible(true);
//                        return (boolean) method.invoke(object, view, event);
//                    } catch (NoSuchMethodException e) {
//                        e.printStackTrace();
//                    } catch (InvocationTargetException e) {
//                        e.printStackTrace();
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    }
//                    return false;
//                }
//                addEvent(view.getContext(), "onTouch", view, pageName, String.format(Locale.CHINA, "{\"operation\":\"%s\",\"x\":\"%s\",\"y\":\"%s\"}", operation, event.getX(), event.getY()));
//                try {
//                    final Method method = object.getClass().getDeclaredMethod(methodName, View.class);
//                    method.setAccessible(true);
//                    return (boolean) method.invoke(object, view, event);
//                } catch (NoSuchMethodException e) {
//                    e.printStackTrace();
//                } catch (InvocationTargetException e) {
//                    e.printStackTrace();
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
            return false;
        });
    }

    private static void addEvent(Context context, String action, View view, final String pageName) {
        addEvent(context, action, view, pageName, null);
    }

    private static void addEvent(Context context, String action, View view, final String pageName, String action_value) {
        if (Settings.forAppium) return;
        singleThreadPool.execute(new AddEventThread(context, action, view, pageName, action_value, ((String) view.getTag(R.id.UBT_WIDGET))));
    }

    public static void addPageEvent(Context context, String action, String object, final String pageName) {
        if (Settings.forAppium) return;
        singleThreadPool.execute(new AddEventThread(context, action, object, pageName));
    }

    public static void addAppEvent(Context context, String action) {
        if (Settings.forAppium) return;
        singleThreadPool.execute(new AddEventThread(context, action));
    }
}

