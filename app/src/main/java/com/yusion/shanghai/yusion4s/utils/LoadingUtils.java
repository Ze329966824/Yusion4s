package com.yusion.shanghai.yusion4s.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;

import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.yusion.shanghai.yusion4s.R;

/**
 * Created by ice on 2017/8/7.
 */

public class LoadingUtils {
    public static Dialog createLoadingDialog(Context context) {
        Dialog dialog = new Dialog(context, R.style.MyDialogStyle);
        dialog.setCanceledOnTouchOutside(false);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.loading_dialog, null);
        dialog.setContentView(contentView);
//        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        dialog.getWindow().setBackgroundDrawableResource(android.R.color.white);
        ProgressBar progressBar = (ProgressBar) contentView.findViewById(R.id.loading_dialog_pro);
        ThreeBounce threeBounce = new ThreeBounce();
        progressBar.setIndeterminateDrawable(threeBounce);
        return dialog;
    }
}
