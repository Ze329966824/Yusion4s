package com.yusion.shanghai.yusion4s.utils;

import android.content.Context;

import static android.R.attr.value;

/**
 * Created by suijin on 2017/3/21.
 */


public class SharedPrefsUtil {
    private Context context;
    private String spName = "yusion4s";
    private static SharedPrefsUtil instance;

    private SharedPrefsUtil(Context context) {
        this.context = context;
    }

    public static SharedPrefsUtil getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefsUtil(context);
        }
        return instance;
    }

    public void putValue(String key, int value) {
        context.getSharedPreferences(spName, Context.MODE_PRIVATE).edit().putInt(key, value).apply();
    }

    public void putValue(String key, Long value) {
        context.getSharedPreferences(spName, Context.MODE_PRIVATE).edit().putLong(key, value).apply();
    }

    public void putValue(String key, boolean value) {
        context.getSharedPreferences(spName, Context.MODE_PRIVATE).edit().putBoolean(key, value).apply();
    }

    public void putValue(String key, Float value) {
        context.getSharedPreferences(spName, Context.MODE_PRIVATE).edit().putFloat(key, value).apply();
    }

    public void putValue(String key, String value) {
        context.getSharedPreferences(spName, Context.MODE_PRIVATE).edit().putString(key, value).apply();
    }

    public int getValue(String key, int defValue) {
        return context.getSharedPreferences(spName, Context.MODE_PRIVATE).getInt(key, value);
    }

    public long getValue(String key, long defValue) {
        return context.getSharedPreferences(spName, Context.MODE_PRIVATE).getLong(key, defValue);
    }

    public boolean getValue(String key, boolean defValue) {
        return context.getSharedPreferences(spName, Context.MODE_PRIVATE).getBoolean(key, defValue);
    }

    public float getValue(String key, float defValue) {
        return context.getSharedPreferences(spName, Context.MODE_PRIVATE).getFloat(key, defValue);
    }

    public String getValue(String key, String defValue) {
        return context.getSharedPreferences(spName, Context.MODE_PRIVATE).getString(key, defValue);
    }

    public void remove(String key) {
        context.getSharedPreferences(spName, Context.MODE_PRIVATE).edit().remove(key).apply();
    }
}

