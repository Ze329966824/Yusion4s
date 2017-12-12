package com.yusion.shanghai.yusion4s.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yusion.shanghai.yusion4s.R;


public class PopupDialogUtil {
    private static Dialog dialog;
    private static Context mContext;
    private static OnOkClickListener onOkClickListener;
    private static OnCancelClickListener onCancelClickListener;

    public static void setOnOkClickListener(OnOkClickListener onOkClickListener) {
        PopupDialogUtil.onOkClickListener = onOkClickListener;
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

    public static void showOneButtonDialog(Context context, String msg, OnOkClickListener clickListener) {
        mContext = context;
        dialog = new Dialog(mContext, R.style.MyDialogStyle);

        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_login, null);

        TextView mMessage = (TextView) view.findViewById(R.id.dialog_login_msg);
        mMessage.setText(msg);
        TextView mOK = (TextView) view.findViewById(R.id.dialog_login_ok);
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
        show();
    }

    public static void relevanceInfoDialog(Context context, String title, String name, String mobile , String idno, OnOkClickListener clickListener ) {
        mContext = context;
        dialog = new Dialog(mContext, R.style.MyDialogStyle);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_relevance_user, null);
        TextView mTitle = (TextView) view.findViewById(R.id.popup_dialog_title);
        TextView mName = (TextView) view.findViewById(R.id.dialog_create_nm);
        TextView mMobile = (TextView) view.findViewById(R.id.dialog_create_mobile);
        TextView mIdno = (TextView) view.findViewById(R.id.dialog_create_idno);
        TextView mOK = (TextView) view.findViewById(R.id.popup_dialog_ok);
        mTitle.setText(title);
        mName.setText("• 姓名："+name);
        mMobile.setText("• 电话："+mobile);
        mIdno.setText("• 身份证："+idno);
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

    public static void checkInfoDialog(Context context, String reason1, String reason2 , String reason3, OnOkClickListener clickListener ) {
        mContext = context;
        dialog = new Dialog(mContext, R.style.MyDialogStyle);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_check_user, null);
        TextView mReason1 = (TextView) view.findViewById(R.id.dialog_relevance_reason1);
        TextView mReason2 = (TextView) view.findViewById(R.id.dialog_relevance_reason2);
        TextView mReason3 = (TextView) view.findViewById(R.id.dialog_relevance_reason3);
        TextView mOK = (TextView) view.findViewById(R.id.popup_dialog_ok);
        mReason1.setText("1.："+reason1);
        mReason2.setText("2.："+reason2);
        mReason3.setText("3.："+reason3);
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

    public static void showOneButtonDialog(Context context, String title, String msg,
                                           OnOkClickListener clickListener) {
        mContext = context;
        dialog = new Dialog(mContext, R.style.MyDialogStyle);
        View view = LayoutInflater.from(mContext).inflate(R.layout.popup_dialog_one_button, null);
        TextView mTitle = (TextView) view.findViewById(R.id.popup_dialog_title);
        mTitle.setText(title);
        TextView mMessage = (TextView) view.findViewById(R.id.popup_dialog_message);
        mMessage.setText(msg);
        TextView mOK = (TextView) view.findViewById(R.id.popup_dialog_ok);
        mOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onOkClick(dialog);
                }
            }
        });
//        mOK.setOnClickListener(clickListener);

        ImageView mCancel = (ImageView) view.findViewById(R.id.btn_cancel);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        dialog.setContentView(view);


//        int screenWidth = getWindowManager().getDefaultDisplay().getWidth(); // 屏幕宽
//        int screenHeight = getWindowManager().getDefaultDisplay().getHeight(); // 屏幕高


//        dialog.getWindow().getAttributes().width = 259;
//        dialog.getWindow().getAttributes().height = 259;
        dialog.setCancelable(false);
//        dialog.getWindow().getAttributes()
        dialog.show();
    }
    public static void showTwoButtonsDialog(Context context,String content,String leftMsg,String rightMsg,OnOkClickListener clickListener) {
        mContext = context;
        dialog = new Dialog(mContext, R.style.MyDialogStyle);
        View view = LayoutInflater.from(mContext).inflate(R.layout.popup_dialog_two_button, null);
        TextView mOK = (TextView) view.findViewById(R.id.popup_dialog_ok);
//        mOK.setOnClickListener(okListener);
        mOK.setText(leftMsg);
        mOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onOkClick(dialog);
                }
            }
        });
        TextView mCancel = (TextView) view.findViewById(R.id.popup_dialog_cancel);
        mCancel.setText(rightMsg);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        TextView mMsg = (TextView) view.findViewById(R.id.popup_dialog_msg);
        mMsg.setText(content);
        dialog.setContentView(view);
        dialog.setCancelable(false);
//
//        dialog.getWindow().getAttributes().width = width;
//        dialog.getWindow().getAttributes().height = height;
        show();
    }
    public static void showTwoButtonsDialog(Context context,String title,String content,String leftMsg,String rightMsg,OnOkClickListener clickListener) {
        mContext = context;
        dialog = new Dialog(mContext, R.style.MyDialogStyle);
        View view = LayoutInflater.from(mContext).inflate(R.layout.popup_dialog_two_button, null);
        TextView mOK = (TextView) view.findViewById(R.id.popup_dialog_ok);
//        mOK.setOnClickListener(okListener);
        mOK.setText(leftMsg);
        mOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onOkClick(dialog);
                }
            }
        });
        TextView mCancel = (TextView) view.findViewById(R.id.popup_dialog_cancel);
        mCancel.setText(rightMsg);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        TextView mMsg = (TextView) view.findViewById(R.id.popup_dialog_msg);
        mMsg.setText(content);
        dialog.setContentView(view);
        dialog.setCancelable(false);
//
//        dialog.getWindow().getAttributes().width = width;
//        dialog.getWindow().getAttributes().height = height;
        show();
    }

    public static void showTwoButtonsDialog(Context context,int width,int height, OnOkClickListener clickListener) {
        mContext = context;
//        if (dialog == null) {
        dialog = new Dialog(mContext, R.style.MyDialogStyle);
//        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.popup_dialog_two_button, null);
        TextView mOK = (TextView) view.findViewById(R.id.popup_dialog_ok);
//        mOK.setOnClickListener(okListener);
        mOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onOkClick(dialog);
                }
            }
        });
        TextView mCancel = (TextView) view.findViewById(R.id.popup_dialog_cancel);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.setCancelable(false);

        dialog.getWindow().getAttributes().width = width;
        dialog.getWindow().getAttributes().height = height;
        show();
    }
    public static void showTwoButtonsDialog(Context context, String leftbtn, String rightbtn,String message, OnOkClickListener clickListener, OnCancelClickListener cancelListener) {
        mContext = context;
//        if (dialog == null) {
        dialog = new Dialog(mContext, R.style.MyDialogStyle);
//        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.popup_dialog_two_button, null);
        TextView mOK = (TextView) view.findViewById(R.id.popup_dialog_ok);
//        mOK.setOnClickListener(okListener);
        mOK.setText(leftbtn);
        mOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onOkClick(dialog);
                }
            }
        });
        TextView mCancel = (TextView) view.findViewById(R.id.popup_dialog_cancel);
        mCancel.setText(rightbtn);
//        mCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancelListener != null) {
                    cancelListener.onCancelClick(dialog);
                }
            }
        });

        TextView mMsg = (TextView) view.findViewById(R.id.popup_dialog_msg);
        mMsg.setText(message);
        dialog.setContentView(view);
        dialog.setCancelable(false);

//        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(view.getWidth(),view.getHeight());
//        dialog.addContentView(view, params);
//
//        dialog.getWindow().getAttributes().width = width;
//        dialog.getWindow().getAttributes().height = height;

        show();
    }


    public static void showTwoButtonsDialog(Context context,int width,int height, String leftbtn, String rightbtn,String message, OnOkClickListener clickListener, OnCancelClickListener cancelListener) {
        mContext = context;
//        if (dialog == null) {
        dialog = new Dialog(mContext, R.style.MyDialogStyle);
//        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.popup_dialog_two_button, null);
        TextView mOK = (TextView) view.findViewById(R.id.popup_dialog_ok);
//        mOK.setOnClickListener(okListener);
        mOK.setText(leftbtn);
        mOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onOkClick(dialog);
                }
            }
        });
        TextView mCancel = (TextView) view.findViewById(R.id.popup_dialog_cancel);
        mCancel.setText(rightbtn);
//        mCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancelListener != null) {
                    cancelListener.onCancelClick(dialog);
                }
            }
        });

        TextView mMsg = (TextView) view.findViewById(R.id.popup_dialog_msg);
        mMsg.setText(message);
        dialog.setContentView(view);
        dialog.setCancelable(false);

        dialog.getWindow().getAttributes().width = width;
        dialog.getWindow().getAttributes().height = height;

        show();
    }

    public interface OnOkClickListener {
        void onOkClick(Dialog dialog);
    }
    public interface OnCancelClickListener {
        void onCancelClick(Dialog dialog);
    }

}
