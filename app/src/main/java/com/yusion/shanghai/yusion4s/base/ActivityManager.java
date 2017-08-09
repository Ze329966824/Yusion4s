package com.yusion.shanghai.yusion4s.base;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 伟大的创建人：ice
 * 不可相信的创建时间：17/4/19 下午7:07
 */
public class ActivityManager {

    public static List<AppCompatActivity> list = new ArrayList<>();

    public static void addActivity(AppCompatActivity activity) {
        list.add(activity);
    }

    public static void removeActivity(AppCompatActivity activity) {
        list.remove(activity);
        activity.finish();

    }

    public static String getPrevActivityName() {
        if (list.size() <= 1) {
            Log.e("TAG", "没有别的Activity了");
            return null;
        }
        return list.get(list.size() - 2).getClass().getSimpleName();
    }

    public static void finish() {
        for (AppCompatActivity activity : list) {
            list.remove(activity);
            activity.finish();
        }
    }

    public static void finishActivityByClassName(Class type) {
        for (AppCompatActivity activity : list) {
            if (activity.getClass() == type) {
                activity.finish();
            }
        }
    }

    public static void finishOtherActivityEx(Class type) {
        for (AppCompatActivity activity : list) {
            if (activity.getClass() != type) {
                activity.finish();
            }
        }
    }
}
