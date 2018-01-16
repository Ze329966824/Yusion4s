package com.yusion.shanghai.yusion4s.jpush;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.Yusion4sApp;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.ui.entrance.LoginActivity;
import com.yusion.shanghai.yusion4s.utils.PopupDialogUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class JpushDialogActivity extends BaseActivity {
    private String username = null;
    private String mobile = null;
    private String title = null;
    private String content = null;
    private String app_st = null;
    private String app_id = null;
    private String category = null;
    private String stringExtra = null;
    private String order_state = null;

/*    "reg_id":xxxx,
            "mobile": 138xxx,
            "title":xxxx,
            "content":xxxx,
            "app_st": "SubmitApplication_Submit_PASS",
            "app_id": 可为空,
            "category": "login",*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jpush_dialog);
        try {
            initJpush();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void initJpush() throws JSONException {
        Intent intent = getIntent();
        if (intent != null) {
            stringExtra = intent.getStringExtra("jsonObject");
            JSONObject jo = new JSONObject(stringExtra);
            mobile = jo.optString("mobile");
            title = jo.optString("title");
            content = jo.optString("content");
            app_st = jo.optString("app_st");
            app_id = jo.optString("app_id");
            category = jo.optString("category");
            order_state = jo.optString("order_state");
            username = jo.optString("username");

            popJpushDialog();
        } else {
            finish();
        }
    }


    void popJpushDialog() {
        if (Yusion4sApp.isLogin && username.equals(Yusion4sApp.ACCOUNT)) {
            switch (category) {
                case "login"://抢登
                    PopupDialogUtil.showOneButtonDialog(this, content,"好的", dialog -> {
                        myApp.clearUserData();
                        startActivity(new Intent(JpushDialogActivity.this, LoginActivity.class));
                        finish();
                    });
                    break;
                case "application":
                    finish();
//                    switch (order_state) {
//                        case "pass":
//                            JpushApproveDialog approvePassDialog = new JpushApproveDialog(this,title,content,R.layout.dialog_approval_pass);
//                            approvePassDialog.setYesOnclickListener(() ->{
//                                approvePassDialog.dismiss();
//                                Intent intent = new Intent(JpushDialogActivity.this, OrderDetailActivity.class);
//                                intent.putExtra("app_id", app_id);
//                                startActivity(intent);
//                                finish();
//                            });
//                            approvePassDialog.setNoOnclickListener(() -> {
//                                approvePassDialog.dismiss();
//                                finish();
//                            });
//                            approvePassDialog.show();
//                            break;
//                        case "refuse":
//                            JpushApproveDialog approveRefuseDialog = new JpushApproveDialog(this,title,content,R.layout.dialog_approval_refuse);
//                            approveRefuseDialog.setYesOnclickListener(() ->{
//                                approveRefuseDialog.dismiss();
//                                Intent intent = new Intent(JpushDialogActivity.this, OrderDetailActivity.class);
//                                intent.putExtra("app_id", app_id);
//                                startActivity(intent);
//                                finish();
//                            });
//                            approveRefuseDialog.setNoOnclickListener(() -> {
//                                approveRefuseDialog.dismiss();
//                                finish();
//                            });
//                            approveRefuseDialog.show();
//                            break;
//                        default:
//                            new AlertDialog.Builder(JpushDialogActivity.this)
//                                    .setCancelable(false)
//                                    .setTitle(title)
//                                    .setMessage(content)
//                                    .setPositiveButton("知道啦", (dialog, which) -> {
//                                        dialog.dismiss();
//                                        finish();
//                                    })
//                                    .show();
//                            break;
//                    }
//
//                    break;
//
//                default:
//                    new AlertDialog.Builder(JpushDialogActivity.this)
//                            .setTitle(title)
//                            .setMessage(content)
//                            .setCancelable(false)
//                            .setPositiveButton("这不是一个推送", (dialog, which) -> {
//                                dialog.dismiss();
//                                finish();
//                            })
//                            .show();
//                    break;
            }
        } else {
            finish();
        }
    }



    // pass 和 refuse  一个dialog  传入不同的布局即可
    private static class JpushApproveDialog extends Dialog {
        private Context mContext;
        private TextView mMessage;
        private TextView mTitle;
        private String titleStr;
        private String messageStr;
        private int resID;

        public JpushApproveDialog(Context context) {
            super(context);
        }

        private onNoOnclickListener noOnclickListener;//取消按钮被点击了的监听器
        private onYesOnclickListener yesOnclickListener;//确定按钮被点击了的监听器

        public interface onYesOnclickListener {
            void onYesClick();
        }

        public interface onNoOnclickListener {
            void onNoClick();
        }

        public void setNoOnclickListener(onNoOnclickListener onNoOnclickListener) {
            this.noOnclickListener = onNoOnclickListener;
        }

        public void setYesOnclickListener(onYesOnclickListener onYesOnclickListener) {
            this.yesOnclickListener = onYesOnclickListener;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
//            setContentView(R.layout.dialog_approval_pass);
            setContentView(resID);
            setCanceledOnTouchOutside(false);
            setCancelable(false);
            //初始化
            initView();
            //填充自定义的title和msg
            initData();
            //点击事件
            initEvent();
        }

        private void initEvent() {
            findViewById(R.id.btn_cancel).setOnClickListener(v -> {
                if (yesOnclickListener != null) {
                    yesOnclickListener.onYesClick();
                }
            });
            findViewById(R.id.btn_ok).setOnClickListener(v -> {
                if (noOnclickListener != null) {
                    noOnclickListener.onNoClick();
                }
            });
        }

        private void initData() {
            //用户自定了title和message
            if (mTitle != null) {
                mTitle.setText(titleStr);
            }
            if (messageStr != null) {
                mMessage.setText(messageStr);
            }
        }

        private void initView() {
            mTitle = findViewById(R.id.dialog_approve_title);
            mMessage = findViewById(R.id.dialog_approve_message);
        }

        public void setMessage(String message) {
            messageStr = message;
        }

        public void setTitle(String title) {
            titleStr = title;
        }

        public JpushApproveDialog(Context context, String title, String message, int resid) {
            super(context);
            mContext = context;
            titleStr = title;
            messageStr = message;
            resID = resid;
        }
    }




}