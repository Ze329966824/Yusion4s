package com.yusion.shanghai.yusion4s.ui.entrance;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.Yusion4sApp;
import com.yusion.shanghai.yusion4s.base.ActivityManager;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.bean.login.LoginReq;
import com.yusion.shanghai.yusion4s.bean.login.LoginResp;
import com.yusion.shanghai.yusion4s.retrofit.api.AuthApi;
import com.yusion.shanghai.yusion4s.ui.MainActivity;
import com.yusion.shanghai.yusion4s.utils.SharedPrefsUtil;


public class LoginActivity extends BaseActivity {

    private EditText mLoginAccountTV;
    private EditText mLoginPasswordTV;
    private ImageView mLoginPasswordEyeImg;
    private boolean isShowPassword = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        mLoginAccountTV = (EditText) findViewById(R.id.login_account_edt);
        mLoginPasswordTV = (EditText) findViewById(R.id.login_password_edt);
        mLoginPasswordEyeImg = (ImageView) findViewById(R.id.login_password_eye_img);
        mLoginPasswordEyeImg.setOnClickListener(v -> {
            if (isShowPassword) {
                //隐藏密码
                isShowPassword = false;
                mLoginPasswordEyeImg.setImageResource(R.mipmap.password_hide);
                mLoginPasswordTV.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
            } else {
                isShowPassword = true;
                mLoginPasswordEyeImg.setImageResource(R.mipmap.password_show);
                mLoginPasswordTV.setInputType(EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }
        });
        findViewById(R.id.login_submit_btn).setOnClickListener(v -> {
            LoginReq req = new LoginReq();
            String account = mLoginAccountTV.getText().toString();
            String password = mLoginPasswordTV.getText().toString();
            if (TextUtils.isEmpty(account)) {
                Toast.makeText(myApp, "账号不能为空!", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(password)) {
                Toast.makeText(myApp, "密码不能为空!", Toast.LENGTH_SHORT).show();
                return;
            }
            req.username = account;
            req.password = password;
            AuthApi.login(this, req, this::loginSuccess);
        });
    }

    private void loginSuccess(LoginResp resp) {

        Yusion4sApp.TOKEN = resp.token;
        SharedPrefsUtil.getInstance(LoginActivity.this).putValue("token", Yusion4sApp.TOKEN);
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        myApp.clearUserData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityManager.finishOtherActivityEx(LoginActivity.class);
    }
}
