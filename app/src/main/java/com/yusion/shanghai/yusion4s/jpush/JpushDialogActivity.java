package com.yusion.shanghai.yusion4s.jpush;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.Yusion4sApp;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.ui.entrance.LoginActivity;

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
            username = jo.optString("username");
            mobile = jo.optString("mobile");
            title = jo.optString("title");
            content = jo.optString("content");
            app_st = jo.optString("app_st");
            app_id = jo.optString("app_id");
            category = jo.optString("category");
            JpushDialog();
        }else {
            finish();
        }
    }

    private void JpushDialog() {
        if (Yusion4sApp.isLogin) {
            if (username.equals(Yusion4sApp.ACCOUNT)) {
                switch (category) {
                    case "login":
                        new AlertDialog.Builder(JpushDialogActivity.this)
                                .setCancelable(false)
                                .setMessage(content)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(JpushDialogActivity.this, LoginActivity.class));
                                        myApp.clearUserData();
                                        finish();
                                    }
                                })
                                .show();
                        break;

                    case "application":
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
        } else {
            finish();
        }

    }
}
