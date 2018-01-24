package com.yusion.shanghai.yusion4s.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yusion.shanghai.yusion4s.R;

import java.lang.reflect.Field;


public class ToastUtil {
    private static Toast mToast = null;

    private ToastUtil() {
    }

    public static void showShort(Context context, CharSequence message) {
        show(context, message, Toast.LENGTH_SHORT);
    }

    public static void showLong(Context context, CharSequence message) {
        show(context, message, Toast.LENGTH_LONG);
    }

    @SuppressLint("ShowToast")
    public static void show(Context context, CharSequence message, int duration) {
        if (mToast == null || context != getToastContext(mToast)) {
            mToast = Toast.makeText(context, message, duration);
        } else {
            mToast.setText(message);
        }
        mToast.show();
    }

    private static Context getToastContext(Toast toast) {
        Context context = null;
        Field mContext = null;
        try {
            mContext = toast.getClass().getDeclaredField("mContext");
            mContext.setAccessible(true);
            context = (Context) mContext.get(mToast);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return context;
    }

    /**
     * short是2  long是3.5
     */
    @SuppressLint("ShowToast")
    public static void customToastGravity(Context context, CharSequence message, int gravity, int xoffset, int yoffset) {
        if (mToast == null || context != getToastContext(mToast)) {
            mToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        } else {
            mToast.setText(message);
        }
        mToast.setGravity(gravity, xoffset, yoffset);
        mToast.show();
    }

    @SuppressLint("ShowToast")
    public static void showImageToast(Context context, CharSequence message, @DrawableRes int mipmpid) {
        View toastView = LayoutInflater.from(context).inflate(R.layout.toast_replace, null);
        TextView mTextView = toastView.findViewById(R.id.toast_success_tv);
        ImageView mImageView = toastView.findViewById(R.id.toast_success_img);
        mTextView.setText(message);
        mImageView.setImageResource(mipmpid);

        if (mToast == null || context != getToastContext(mToast)) {
            mToast = new Toast(context);
        }

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int height = wm != null ? wm.getDefaultDisplay().getHeight() : 0;
        //Toast的Y坐标是屏幕高度的1/3，不会出现不适配的问题
        mToast.setGravity(Gravity.TOP, 0, height / 3);
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.setView(toastView);
        mToast.show();
    }

}
