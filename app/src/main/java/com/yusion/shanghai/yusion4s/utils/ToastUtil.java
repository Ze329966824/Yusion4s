package com.yusion.shanghai.yusion4s.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yusion.shanghai.yusion4s.R;


public class ToastUtil {
    private static TextView mTextView;
    private static ImageView mImageView;

    public static void showToast(Context context, String message) {
        //加载Toast布局
        View toastView = LayoutInflater.from(context).inflate(R.layout.toast_replace, null);
        //初始化布局控件
        mTextView = toastView.findViewById(R.id.toast_success_tv);
        mImageView = toastView.findViewById(R.id.toast_success_img);
        //为控件设置属性
        mTextView.setText(message);
        mImageView.setImageResource(R.mipmap.toast_success);
        //Toast的初始化
        Toast toastStart = new Toast(context);
        //获取屏幕高度
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        //Toast的Y坐标是屏幕高度的1/3，不会出现不适配的问题
        toastStart.setGravity(Gravity.TOP, 0, height / 3);
        toastStart.setDuration(Toast.LENGTH_LONG);
        toastStart.setView(toastView);
        toastStart.show();
    }
}
