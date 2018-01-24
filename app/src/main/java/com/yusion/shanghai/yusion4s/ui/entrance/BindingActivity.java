package com.yusion.shanghai.yusion4s.ui.entrance;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.Yusion4sApp;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.bean.auth.BindingReq;
import com.yusion.shanghai.yusion4s.bean.auth.BindingResp;
import com.yusion.shanghai.yusion4s.retrofit.api.AuthApi;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.settings.Settings;
import com.yusion.shanghai.yusion4s.ui.MainActivity;
import com.yusion.shanghai.yusion4s.utils.SharedPrefsUtil;
import com.yusion.shanghai.yusion4s.widget.CountDownButtonWrap;


public class BindingActivity extends BaseActivity {
    private EditText mBindingMobileTV;
    private EditText mBindingCodeTV;
    private Button mBindingSubmitBtn;
    private CountDownButtonWrap mCountDownBtnWrap;
    private BindingReq req;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding);
        initTitleBar(this, "绑定账号").setLeftClickListener(v -> showDoubleCheckForExit());
        initView();

    }

    private void initView() {
        mBindingMobileTV = findViewById(R.id.binding_mobile_edt);
        mBindingCodeTV = findViewById(R.id.bindling_code_edt);
        mBindingSubmitBtn = findViewById(R.id.binding_submit_btn);
        req = new BindingReq();
        req.reg_id = Yusion4sApp.reg_id;
        req.source = getIntent().getStringExtra("source");
        req.open_id = getIntent().getStringExtra("open_id");
        req.unionid = getIntent().getStringExtra("unionid");
        Log.e("TAG", "Bindreq  unionid: "+req.unionid);




        mBindingSubmitBtn.setOnClickListener(v -> {
            if (TextUtils.isEmpty(mBindingMobileTV.getText())) {
                Toast.makeText(BindingActivity.this, "账号不能为空", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(mBindingCodeTV.getText())) {
                Toast.makeText(BindingActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
            } else {


                AuthApi.checkOpenID(BindingActivity.this, mBindingMobileTV.getText().toString(), req.source,"2", data -> {
                    if (data != null) {
                        if (data == 1) {
                            new AlertDialog.Builder(BindingActivity.this)
                                    .setMessage("检测到账号：" + mBindingMobileTV.getText().toString() + "已经绑定过其他微信，是否替换？")
                                    .setPositiveButton("是", (dialog, which) -> {
                                        bind();
                                        dialog.dismiss();
                                    })
                                    .setNegativeButton("否", (dialog, which) -> {
                                        dialog.dismiss();
                                    })
                                    .setCancelable(false)
                                    .show();
                        } else {
                            bind();
                        }
                    }

                });
            }
        });
    }

    private void showDoubleCheckForExit() {
        new AlertDialog.Builder(this).setMessage("是否退出")
                .setPositiveButton("确定", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                })
                .setNegativeButton("取消", (dialog, which) -> dialog.dismiss()).show();
    }

    private void getVCode() {
        mCountDownBtnWrap.start();
        AuthApi.getVCode(BindingActivity.this, mBindingMobileTV.getText().toString(), data -> {
            if (data != null) {
                if (!Settings.isOnline) {
                    mBindingCodeTV.setText(data.verify_code);
//                    req.verify_code = data.verify_code;
                    req.mobile = mBindingMobileTV.getText().toString();

                }
            }
        });
    }

    private void bind() {
        req.dtype = "2";
        req.verify_code = mBindingCodeTV.getText().toString();
        req.mobile = mBindingMobileTV.getText().toString();
        AuthApi.binding(this, req, new OnItemDataCallBack<BindingResp>() {
            @Override
            public void onItemDataCallBack(BindingResp data) {
                if (data != null) {
                    Yusion4sApp.TOKEN = data.token;
                    Yusion4sApp.ACCOUNT = mBindingMobileTV.getText().toString();
                    SharedPrefsUtil.getInstance(BindingActivity.this).putValue("token", Yusion4sApp.TOKEN);
                    startActivity(new Intent(BindingActivity.this, MainActivity.class));
                    finish();
                }
            }
        });
    }
}
