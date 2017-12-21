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
    private static boolean isShow = true;//默认显示
    private static Toast mToast = null;//全局唯一的toast

    //用于显示带图片的toast
    private static TextView mTextView;
    private static ImageView mImageView;

    /**
     * 控制不应该被实例化
     */
    private ToastUtil() {
        throw new UnsupportedOperationException("不能被实例化");
    }

    /**
     * 全局控制是否显示toast
     *
     * @param isShowToast
     */
    public static void controlShow(boolean isShowToast) {
        isShow = isShowToast;
    }

    /**
     * 取消toast的显示
     */
    public void cancleToast() {
        if (isShow && mToast != null) {
            mToast.cancel();
        }
    }

    /**
     * 短时间显示toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, CharSequence message) {
        if (isShow) {
            if (mToast == null) {
                mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            } else {
                mToast.setText(message);
            }
            mToast.show();
        }
    }

    /**
     * 长时间显示toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, CharSequence message) {
        if (isShow) {
            if (mToast == null) {
                mToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
            } else {
                mToast.setText(message);
            }
            mToast.show();
        }
    }

    /**
     * 自定义显示toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, CharSequence message, int duration) {
        if (isShow) {
            if (mToast == null) {
                mToast = Toast.makeText(context, message, duration);
            } else {
                mToast.setText(message);
            }
            mToast.show();
        }
    }

    /**
     * 自定义toast的位置
     *
     * @param context

     * @param message
     * @param duration short是2  long是3.5
     * @param gravity
     * @param xoffset
     * @param yoffset
     */
    public static void customToastGravity(Context context, CharSequence message,int gravity, int xoffset, int yoffset) {
        if (isShow) {
            if (mToast == null) {
                mToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
            } else {
                mToast.setText(message);
            }
            mToast.setGravity(gravity, xoffset, yoffset);
            mToast.show();
        }
    }

    /**
     * 带图片显示的toast
     *
     * @param context
     * @param message
     * @param mipmpid
     */
    public static void showImageToast(Context context, CharSequence message, int mipmpid) {
        //加载Toast布局
        View toastView = LayoutInflater.from(context).inflate(R.layout.toast_replace, null);
        //初始化布局控件
        mTextView = toastView.findViewById(R.id.toast_success_tv);
        mImageView = toastView.findViewById(R.id.toast_success_img);
        //为控件设置属性
        mTextView.setText(message);
        mImageView.setImageResource(mipmpid);
        //Toast的初始化
        //Toast toastStart = new Toast(context);
        if (isShow) {
            if (mToast == null) {
                mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            } else {
                mToast.setText(message);
            }
        }
        //获取屏幕高度
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        //Toast的Y坐标是屏幕高度的1/3，不会出现不适配的问题
        mToast.setGravity(Gravity.TOP, 0, height / 3);
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.setView(toastView);
        mToast.show();
    }

}
