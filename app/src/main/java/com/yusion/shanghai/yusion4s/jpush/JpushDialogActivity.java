package com.yusion.shanghai.yusion4s.jpush;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.ui.entrance.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class JpushDialogActivity extends BaseActivity {
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
            if (jo != null) {
                mobile = jo.optString("mobile");
                title = jo.optString("title");
                content = jo.optString("content");
                app_st = jo.optString("app_st");
                app_id = jo.optString("app_id");
                category = jo.optString("category");
                JpushDialog();
            }
        }
    }

    private void JpushDialog() {
        switch (category) {
            case "login":
                myApp.clearUserData();
                if (myApp.isLogin == true) {
                    new AlertDialog.Builder(JpushDialogActivity.this)
                            .setCancelable(false)
                            .setTitle("")
                            .setMessage(content)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(JpushDialogActivity.this, LoginActivity.class));

                                    finish();
                                }
                            })
                            .show();
                }
                break;

//            case "2":
//
//                break;
//
//            case "3":
//
//                break;
//            default:
//                new AlertDialog.Builder(JpushDialogActivity.this)
//                        .setTitle(title)
//                        .setMessage(content)
//                        .setCancelable(false)
//                        .setPositiveButton("知道啦", (dialog, which) -> {
//                            dialog.dismiss();
//                            finish();
//                        });
//                break;
        }

//        Toast.makeText(myApp, stringExtra, Toast.LENGTH_SHORT).show();
//        new AlertDialog.Builder(JpushDialogActivity.this)
//                .setTitle("asdasdasadsad")
//                .setMessage("syz")
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                }).show();

    }
}
