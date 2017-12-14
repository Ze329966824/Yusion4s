package com.yusion.shanghai.yusion4s.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.utils.DensityUtil;


/**
 * 类描述：
 * 伟大的创建人：ice
 * 不可相信的创建时间：17/4/19 下午7:07
 */
public class DoubleCheckFragment extends BaseFragment {

    protected Button mDoubleCheckChangeBtn;
    protected Button mDoubleCheckSubmitBtn;
    public Dialog mDoubleCheckDialog;
    private LinearLayout mDoubleCheckGroupLin;
    private LayoutInflater mInflater;


    public static Dialog createBottomDialog(Context context, View contentView) {
        Dialog dialog = new Dialog(context, R.style.MyDialogStyle);
        dialog.setContentView(contentView);
        dialog.setCanceledOnTouchOutside(false);
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow != null) {
            dialogWindow.setWindowAnimations(R.style.dialogAnimationStyle);
            dialogWindow.setGravity(Gravity.BOTTOM);
            dialogWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        return dialog;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mInflater = LayoutInflater.from(mContext);
        View mDoubleCheckLayout = mInflater.inflate(R.layout.double_check_layout, null);
        mDoubleCheckChangeBtn = ((Button) mDoubleCheckLayout.findViewById(R.id.double_check_change_btn));
        mDoubleCheckSubmitBtn = ((Button) mDoubleCheckLayout.findViewById(R.id.double_check_submit_btn));
        mDoubleCheckGroupLin = ((LinearLayout) mDoubleCheckLayout.findViewById(R.id.double_check_group_lin));
        if (mDoubleCheckDialog == null) {
            mDoubleCheckDialog = createBottomDialog(mContext, mDoubleCheckLayout);

            Window dialogWindow = mDoubleCheckDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.getDecorView().setBackgroundResource(android.R.color.transparent);
                dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
                dialogWindow.setGravity(Gravity.BOTTOM);

                DisplayMetrics metrics = new DisplayMetrics();
                ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(metrics);
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                lp.width = (int) (metrics.widthPixels - DensityUtil.dip2px(mContext, 15) * 2);
                dialogWindow.setAttributes(lp);
            }
        }
    }

    public void clearDoubleCheckItems() {
        mDoubleCheckGroupLin.removeAllViews();
    }

    public void addDoubleCheckItem(String title, String content) {
        LinearLayout item = (LinearLayout) mInflater.inflate(R.layout.double_check_item, mDoubleCheckGroupLin, false);
        ((TextView) item.findViewById(R.id.double_check_item_title)).setText(title);
        ((TextView) item.findViewById(R.id.double_check_item_content)).setText(content);
        mDoubleCheckGroupLin.addView(item);
    }
}