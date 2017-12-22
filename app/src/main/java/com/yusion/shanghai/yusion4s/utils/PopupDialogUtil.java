package com.yusion.shanghai.yusion4s.utils;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.yusion.shanghai.yusion4s.R;


public class PopupDialogUtil {
    private static Dialog dialog;
    private static Context mContext;
    private static OnPopupClickListener onPopupClickListener;
    private static OnCancelClickListener onCancelClickListener;

    public static void setOnPopupClickListener(OnPopupClickListener onPopupClickListener) {
        PopupDialogUtil.onPopupClickListener = onPopupClickListener;
    }

    public static void OnCancelClickListener(OnCancelClickListener onCancelClickListener) {
        PopupDialogUtil.onCancelClickListener = onCancelClickListener;
    }


    /**
     * 暴露给外界okListener实现隐藏对话框的方法
     */
    public static void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    public static void show() {
        if (dialog != null) {
            dialog.show();
        }
    }


    /**
     * 只传入布局，最普通的一个按钮的弹窗，
     *
     * @param msg 弹窗title
     *            
     */
    public static void showOneButtonDialog(Context context, @LayoutRes int resID) {
        mContext = context;
        dialog = new Dialog(mContext, R.style.MyDialogStyle);
        View view = LayoutInflater.from(mContext).inflate(resID, null);
        TextView mOK = view.findViewById(R.id.pop_dialog_ok_tv);
        mOK.setOnClickListener(v -> {
            dismiss();
        });
        dialog.setContentView(view);
        dialog.setCancelable(false);
        show();
    }











    /**
     * 传入布局和ok的监听，没有传入任何数据，title和ok的text无法更改
     *
     * @param resID         自定义布局
     * @param clickListener 自定义监听
     */
    public static void showOneButtonDialog(Context context, @LayoutRes int resID, OnPopupClickListener clickListener) {
        mContext = context;
        dialog = new Dialog(mContext, R.style.MyDialogStyle);
        View view = LayoutInflater.from(mContext).inflate(resID, null);
        TextView mOK = view.findViewById(R.id.pop_dialog_ok_tv);
        mOK.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onOkClick(dialog);
            }
        });
        dialog.setContentView(view);
        dialog.setCancelable(false);
        show();
    }











    /**
     * 传入弹窗标题和按钮内容，可动态更改弹窗内容，布局框架不能修改
     *
     * @param title         弹窗title
     * @param okText        弹窗按钮
     * @param clickListener 自定义监听
     */
    public static void showOneButtonDialog(Context context, String title, String okText, OnPopupClickListener clickListener) {
        mContext = context;
        dialog = new Dialog(mContext, R.style.MyDialogStyle);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_login, null);
        TextView mMessage = view.findViewById(R.id.pop_dialog_title_tv);
        mMessage.setText(title);
        TextView mOK = view.findViewById(R.id.pop_dialog_ok_tv);
        mOK.setText(okText);
        mOK.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onOkClick(dialog);
            }
        });
        dialog.setContentView(view);
        dialog.setCancelable(false);
        show();
    }









    /**
     * all params can revise in this load as u imagine.
     * @param title         弹窗title
     * @param okText        弹窗按钮
     * @param resID         自定义布局
     * @param clickListener 自定义监听
     */
    public static void showOneButtonDialog(Context context, String title, String okText, @LayoutRes int resID, OnPopupClickListener clickListener) {
        mContext = context;
        dialog = new Dialog(mContext, R.style.MyDialogStyle);

        View view = LayoutInflater.from(mContext).inflate(resID, null);

        TextView mMessage = view.findViewById(R.id.pop_dialog_title_tv);
        mMessage.setText(title);
        TextView mOK = view.findViewById(R.id.pop_dialog_ok_tv);
        mOK.setText(okText);
        mOK.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onOkClick(dialog);
            }
        });
        dialog.setContentView(view);
        dialog.setCancelable(false);
        show();
    }







    /**
     * 特殊的弹窗,创建用户-身份证人像面ocr识别成功时，关联用户专属，三个参数都需要动态添加
     */
    public static void relevanceInfoDialog(Context context, String title, String name, String mobile, String idno, OnPopupClickListener clickListener) {
        mContext = context;
        dialog = new Dialog(mContext, R.style.MyDialogStyle);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_relevance_user, null);
        TextView mTitle = view.findViewById(R.id.popup_dialog_title);
        TextView mName = view.findViewById(R.id.dialog_create_nm);
        TextView mMobile = view.findViewById(R.id.dialog_create_mobile);
        TextView mIdno = view.findViewById(R.id.dialog_create_idno);
        TextView mOK = view.findViewById(R.id.pop_dialog_ok_tv);
        mTitle.setText(title);
        mName.setText("• 姓名：" + name);
        mMobile.setText("• 电话：" + mobile);
        mIdno.setText("• 身份证：" + idno);
        mOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onOkClick(dialog);
                }
            }
        });
        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.show();
    }







    /**
     * 特殊的弹窗,检查三要素未通过专属，三个参数都需要动态添加
     */
    public static void checkInfoDialog(Context context, String reason1, String reason2, String reason3, OnPopupClickListener clickListener) {
        mContext = context;
        dialog = new Dialog(mContext, R.style.MyDialogStyle);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_check_user, null);
        TextView mReason1 = view.findViewById(R.id.dialog_relevance_reason1);
        TextView mReason2 = view.findViewById(R.id.dialog_relevance_reason2);
        TextView mReason3 = view.findViewById(R.id.dialog_relevance_reason3);
        TextView mOK = view.findViewById(R.id.pop_dialog_ok_tv);
        mReason1.setText("1.：" + reason1);
        mReason2.setText("2.：" + reason2);
        mReason3.setText("3.：" + reason3);
        mOK.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onOkClick(dialog);
            }
        });
        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.show();
    }






    /**
     * 传入布局 内容无法更改   最普通的两个按钮的弹窗
     * @param resID         自定义布局
     * @param clickListener 右侧按钮的监听
     */
    public static void showTwoButtonsDialog(Context context, @LayoutRes int resID, OnPopupClickListener clickListener) {
        mContext = context;
        dialog = new Dialog(mContext, R.style.MyDialogStyle);
        View view = LayoutInflater.from(mContext).inflate(resID, null);
        TextView mOK = view.findViewById(R.id.pop_dialog_ok_tv);
        mOK.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onOkClick(dialog);
            }
        });
        TextView mCancel = view.findViewById(R.id.pop_dialog_cancel_tv);
        mCancel.setOnClickListener(v -> dismiss());
        TextView mMsg = view.findViewById(R.id.pop_dialog_title_tv);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        show();
    }









    /**
     * 退出登录，退出编辑，退出页面等共用一个布局，只需要手动传入title等值就行
     *
     * @param title         弹窗标题
     * @param rightMsg      右侧按钮
     * @param leftMsg       左侧按钮
     * @param clickListener 右侧按钮的监听
     */
    public static void showTwoButtonsDialog(Context context, String title, String rightMsg, String leftMsg, OnPopupClickListener clickListener) {
        mContext = context;
        dialog = new Dialog(mContext, R.style.MyDialogStyle);
        View view = LayoutInflater.from(mContext).inflate(R.layout.popup_dialog_two_button, null);
        TextView mOK = view.findViewById(R.id.pop_dialog_ok_tv);
        mOK.setText(rightMsg);
        mOK.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onOkClick(dialog);
            }
        });
        TextView mCancel = view.findViewById(R.id.pop_dialog_cancel_tv);
        mCancel.setText(leftMsg);
        mCancel.setOnClickListener(v -> dismiss());
        TextView mTitle = view.findViewById(R.id.pop_dialog_title_tv);
        mTitle.setText(title);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        show();
    }








    /**
     * all params can revise in this load as u imagine.
     *
     * @param resID         自定义布局
     * @param title         弹窗标题
     * @param rightMsg      右侧按钮
     * @param leftMsg       左侧按钮
     * @param clickListener 右侧按钮的监听
     */
    public static void showTwoButtonsDialog(Context context, @LayoutRes int resID, String title, String rightMsg, String leftMsg, OnPopupClickListener clickListener) {
        mContext = context;
        dialog = new Dialog(mContext, R.style.MyDialogStyle);
        View view = LayoutInflater.from(mContext).inflate(resID, null);
        TextView mOK = view.findViewById(R.id.pop_dialog_ok_tv);
        mOK.setText(rightMsg);
        mOK.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onOkClick(dialog);
            }
        });
        TextView mCancel = view.findViewById(R.id.pop_dialog_cancel_tv);
        mCancel.setText(leftMsg);
        mCancel.setOnClickListener(v -> dismiss());
        TextView mTitle = view.findViewById(R.id.pop_dialog_title_tv);
        mTitle.setText(title);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        show();
    }










    /**
     * 监听多个按钮的弹窗
     * @param title         弹窗标题
     * @param rightMsg      右侧按钮
     * @param leftMsg       左侧按钮
     * @param listener      按钮们的监听 listener[0]是右侧按钮  listener[1]是左侧按钮  等等...
     */
    public static void showTwoButtonsDialog(Context context, String leftMsg, String rightMsg, String title, OnPopupClickListener... listener) {
        mContext = context;
        dialog = new Dialog(mContext, R.style.MyDialogStyle);
        View view = LayoutInflater.from(mContext).inflate(R.layout.popup_dialog_two_button, null);
        TextView mOK = view.findViewById(R.id.pop_dialog_ok_tv);
        mOK.setText(leftMsg);
        mOK.setOnClickListener(v -> {
            if (listener != null && listener.length > 0) {
                listener[0].onOkClick(dialog);
            }
        });
        TextView mCancel = view.findViewById(R.id.pop_dialog_cancel_tv);
        mCancel.setText(rightMsg);
        mCancel.setOnClickListener(v -> {
            if (listener != null && listener.length > 1) {
                listener[1].onOkClick(dialog);
            }
        });
        TextView mMsg = view.findViewById(R.id.pop_dialog_title_tv);
        mMsg.setText(title);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        show();
    }






    /**
     * 传入宽高 内容无法更改
     * @param width         自定义宽度
     * @param height        自定义高度
     * @param clickListener 右侧按钮的监听
     */
    public static void showTwoButtonsDialog(Context context, int width, int height, OnPopupClickListener clickListener) {
        mContext = context;
        dialog = new Dialog(mContext, R.style.MyDialogStyle);
        View view = LayoutInflater.from(mContext).inflate(R.layout.popup_dialog_two_button, null);
        TextView mOK = view.findViewById(R.id.pop_dialog_ok_tv);
        mOK.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onOkClick(dialog);
            }
        });
        TextView mCancel = view.findViewById(R.id.pop_dialog_cancel_tv);
        mCancel.setOnClickListener(v -> dismiss());
        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().width = width;
        dialog.getWindow().getAttributes().height = height;
        show();
    }










    //更换配偶为主待人-重新提报
    public static void showTwoButtonsDialog(Context context, String title, String content, String leftMsg, String rightMsg, OnPopupClickListener clickListener) {
        mContext = context;
        dialog = new Dialog(mContext, R.style.MyDialogStyle);
        View view = LayoutInflater.from(mContext).inflate(R.layout.popup_dialog_two_hastitle_button, null);

        TextView mTitle = view.findViewById(R.id.popup_dialog_title);
        mTitle.setText(title);
        TextView mOK = view.findViewById(R.id.pop_dialog_ok_tv);
//        mOK.setOnClickListener(okListener);
        mOK.setText(rightMsg);
        mOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onOkClick(dialog);
                }
            }
        });
        TextView mCancel = view.findViewById(R.id.pop_dialog_cancel_tv);
        mCancel.setText(leftMsg);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        TextView mMsg = view.findViewById(R.id.pop_dialog_title_tv);
        mMsg.setText(content);
        dialog.setContentView(view);
        dialog.setCancelable(false);
//
//        dialog.getWindow().getAttributes().width = width;
//        dialog.getWindow().getAttributes().height = height;
        show();
    }

    public static void logoutDialog(Context context, String content, String leftMsg, String rightMsg, OnPopupClickListener clickListener) {
        mContext = context;
        dialog = new Dialog(mContext, R.style.MyDialogStyle);
        View view = LayoutInflater.from(mContext).inflate(R.layout.popup_dialog_logout, null);
        TextView mOK = view.findViewById(R.id.pop_dialog_ok_tv);
//        mOK.setOnClickListener(okListener);
        mOK.setText(rightMsg);
        mOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onOkClick(dialog);
                }
            }
        });
        TextView mCancel = view.findViewById(R.id.pop_dialog_cancel_tv);
        mCancel.setText(leftMsg);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        TextView mMsg = view.findViewById(R.id.pop_dialog_title_tv);
        mMsg.setText(content);
        dialog.setContentView(view);
        dialog.setCancelable(false);
//
//        dialog.getWindow().getAttributes().width = width;
//        dialog.getWindow().getAttributes().height = height;
        show();
    }

    public interface OnPopupClickListener {
        void onOkClick(Dialog dialog);
    }

    public interface OnCancelClickListener {
        void onCancelClick(Dialog dialog);
    }

}
