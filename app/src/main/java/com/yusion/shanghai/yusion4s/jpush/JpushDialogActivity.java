package com.yusion.shanghai.yusion4s.jpush;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.Yusion4sApp;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.ui.entrance.LoginActivity;
import com.yusion.shanghai.yusion4s.ui.order.OrderDetailActivity;
import com.yusion.shanghai.yusion4s.utils.PopupDialogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import static com.yusion.shanghai.yusion4s.R.id.btn_cancel;

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
                case "login":
                    PopupDialogUtil.showOneButtonDialog(this, content, dialog -> {
                        myApp.clearUserData();
                        startActivity(new Intent(JpushDialogActivity.this, LoginActivity.class));
                        finish();
                    });
                    break;
                case "application":
                    switch (order_state) {
                        case "pass":
                            new JpushDialogPass(this, title, content).show();
                            break;
                        case "refuse":
                            new JpushDialogRefuse(this, title, content).show();
                            break;
                        default:
                            new AlertDialog.Builder(JpushDialogActivity.this)
                                    .setCancelable(false)
                                    .setTitle(title)
                                    .setMessage(content)
                                    .setPositiveButton("知道啦", (dialog, which) -> {
                                        dialog.dismiss();
                                        finish();
                                    })
                                    .show();
                            break;
                    }


                    break;


                default:
                    new AlertDialog.Builder(JpushDialogActivity.this)
                            .setTitle(title)
                            .setMessage(content)
                            .setCancelable(false)
                            .setPositiveButton("这不是一个推送", (dialog, which) -> {
                                dialog.dismiss();
                                finish();
                            })
                            .show();
                    break;
            }
        } else {
            finish();
        }
    }


    private class JpushDialogPass implements View.OnClickListener {
        private Context mContext;
        private Dialog mDialog;
        private View mView;
        private TextView mMessage;
        private TextView mTitle;

        JpushDialogPass(Context context, String title, String message) {
            mContext = context;
            mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_approval_pass, null);

            mTitle = (TextView) mView.findViewById(R.id.dialog_approve_pass_title);
            mTitle.setText(title);
            mMessage = (TextView) mView.findViewById(R.id.dialog_approve_pass_message);
            mMessage.setText(message);

            mView.findViewById(btn_cancel).setOnClickListener(this);
            mView.findViewById(R.id.btn_ok).setOnClickListener(this);

            mDialog = new Dialog(mContext, R.style.MyDialogStyle);
            mDialog.setCancelable(false);

            mDialog.setContentView(mView);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show();
        }

        void show() {
            if (mDialog != null) {
                mDialog.show();
            }
        }

        void dismiss() {
            if (mDialog != null) {
                mDialog.dismiss();
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case btn_cancel:
                    dismiss();
                    finish();
                    break;
                case R.id.btn_ok:
                    dismiss();
                    Intent intent = new Intent(JpushDialogActivity.this, OrderDetailActivity.class);
                    intent.putExtra("app_id", app_id);
                    startActivity(intent);

                    finish();
                    break;
                default:
                    dismiss();
                    break;
            }
        }
    }

    private class JpushDialogRefuse implements View.OnClickListener {
        private Context mContext;
        private Dialog mDialog;
        private View mView;
        private TextView mMessage;
        private TextView mTitle;
        private TextView mCall;

        JpushDialogRefuse(Context context, String title, String message) {
            mContext = context;
            mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_approval_refuse, null);

            mTitle = (TextView) mView.findViewById(R.id.dialog_approve_refuse_title);
            mTitle.setText(title);
            mMessage = (TextView) mView.findViewById(R.id.dialog_approve_refuse_message);
            mMessage.setText(message);
            mCall = (TextView) mView.findViewById(R.id.btn_calltocustomer);

            mView.findViewById(btn_cancel).setOnClickListener(this);
            mView.findViewById(R.id.btn_calltocustomer).setOnClickListener(this);

            mDialog = new Dialog(mContext, R.style.MyDialogStyle);
            mDialog.setCancelable(false);

            mDialog.setContentView(mView);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show();
        }

        void show() {
            if (mDialog != null) {
                mDialog.show();
            }
        }

        void dismiss() {
            if (mDialog != null) {
                mDialog.dismiss();
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case btn_cancel:
                    dismiss();
                    finish();
                    break;
                case R.id.btn_calltocustomer:
                    dismiss();
                    Intent intent = new Intent(JpushDialogActivity.this, OrderDetailActivity.class);
                    intent.putExtra("app_id", app_id);
                    startActivity(intent);

                    finish();
                    break;
                default:
                    dismiss();
                    break;
            }
        }
    }


}