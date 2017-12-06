package com.yusion.shanghai.yusion4s.ui.upload;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.utils.DensityUtil;

/**
 * Created by ice on 2017/9/1.
 */

public class PreviewBottomDialogUtil {

    private Dialog mBottomDialog;
    private Context mContext;
    private View mPreviewAnchor;
    private String imgUrl;
    private OnReTakeCallback onReTakeCallback;
    private OnPreviewCallback onPreviewCallback;

    private PreviewBottomDialogUtil(Context context, View previewAnchor) {
        mContext = context;
        mPreviewAnchor = previewAnchor;
        mBottomDialog = createBottomDialog();
    }

    public void show() {
        mBottomDialog.show();
    }

    public static PreviewBottomDialogUtil init(Context context, View previewAnchor) {
        return new PreviewBottomDialogUtil(context, previewAnchor);
    }

    public void setSource(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public interface OnReTakeCallback {
        void onReTakeCallback();
    }

    public interface OnPreviewCallback {
        void onPreviewCallback();
    }

    private Dialog createBottomDialog() {
        View bottomLayout = LayoutInflater.from(mContext).inflate(R.layout.preview_bottom_dialog, null);
        TextView tv1 = ((TextView) bottomLayout.findViewById(R.id.tv1));
        TextView tv2 = ((TextView) bottomLayout.findViewById(R.id.tv2));
        TextView tv3 = ((TextView) bottomLayout.findViewById(R.id.tv3));
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPreviewCallback != null) {
                    onPreviewCallback.onPreviewCallback();
                }
                Intent intent = new Intent(mContext, ExtraPreviewActivity.class);
                intent.putExtra("PreviewImg", imgUrl);
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(((Activity) mContext), mPreviewAnchor, "shareNames");
                ActivityCompat.startActivity(mContext, intent, compat.toBundle());
                if (mBottomDialog.isShowing()) {
                    mBottomDialog.dismiss();
                }
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onReTakeCallback != null) {
                    onReTakeCallback.onReTakeCallback();
                }
                if (mBottomDialog.isShowing()) {
                    mBottomDialog.dismiss();
                }
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBottomDialog.isShowing()) {
                    mBottomDialog.dismiss();
                }
            }
        });
        if (mBottomDialog == null) {
            mBottomDialog = new Dialog(mContext, R.style.MyDialogStyle);
            mBottomDialog.setContentView(bottomLayout);
            mBottomDialog.setCanceledOnTouchOutside(false);
            mBottomDialog.getWindow().setWindowAnimations(R.style.dialogAnimationStyle);
            mBottomDialog.getWindow().setGravity(Gravity.BOTTOM);
            mBottomDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            Window dialogWindow = mBottomDialog.getWindow();
            dialogWindow.getDecorView().setBackgroundResource(android.R.color.transparent);
            dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
            dialogWindow.setGravity(Gravity.BOTTOM);
            DisplayMetrics metrics = new DisplayMetrics();
            ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(metrics);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = (int) (metrics.widthPixels - DensityUtil.dip2px(mContext, 15) * 2);
            dialogWindow.setAttributes(lp);
        }

        return mBottomDialog;
    }

    public void setOnReTakeCallback(OnReTakeCallback onReTakeCallback) {
        this.onReTakeCallback = onReTakeCallback;
    }

    public void setOnPreviewCallback(OnPreviewCallback onPreviewCallback) {
        this.onPreviewCallback = onPreviewCallback;
    }
}
