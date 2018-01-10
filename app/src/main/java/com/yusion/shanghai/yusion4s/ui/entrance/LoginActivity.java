package com.yusion.shanghai.yusion4s.ui.entrance;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.Yusion4sApp;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.bean.login.LoginReq;
import com.yusion.shanghai.yusion4s.bean.login.LoginResp;
import com.yusion.shanghai.yusion4s.retrofit.Api;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.settings.Settings;
import com.yusion.shanghai.yusion4s.ubt.service.UploadPersonInfoService;
import com.yusion.shanghai.yusion4s.ui.MainActivity;
import com.yusion.shanghai.yusion4s.utils.ApiUtil;
import com.yusion.shanghai.yusion4s.utils.AppUtils;
import com.yusion.shanghai.yusion4s.utils.SharedPrefsUtil;
import com.yusion.shanghai.yusion4s.utils.ToastUtil;


public class LoginActivity extends BaseActivity {

    private EditText mLoginAccountTV;               // 账号
    private EditText mLoginPasswordTV;              // 密码
    private ImageView mLoginPasswordEyeImg;
    private Button loginBtn;
    private TelephonyManager telephonyManager;
    private boolean isShowPassword = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        myApp.requestLocation(null);
        initView();
    }

    private void initView() {
        telephonyManager = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        mLoginAccountTV = findViewById(R.id.login_account_edt);
        mLoginPasswordTV = findViewById(R.id.login_password_edt);
        mLoginPasswordEyeImg = findViewById(R.id.login_password_eye_img);
        mLoginPasswordEyeImg.setOnClickListener(v -> clickPasswordEye());
        loginBtn = findViewById(R.id.login_submit_btn);
        loginBtn.setOnClickListener(v -> requestLogin());
        setTestAccount();
    }

    //测试账号
    private void setTestAccount() {
        if (!Settings.isOnline) {
            loginBtn.setOnLongClickListener(v -> {
                mLoginAccountTV.setText("13333333333");
                mLoginPasswordTV.setText("yujian");
                return true;
            });
            loginBtn.setOnTouchListener((v, event) -> {
                int offset = 300;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawY() > v.getBottom() + offset) {
                        mLoginAccountTV.setText("19999999999");
                        mLoginPasswordTV.setText("yujian");
                    } else if (event.getRawY() < v.getTop() - offset) {
                        mLoginAccountTV.setText("18888888888");
                        mLoginPasswordTV.setText("yujian");
                    }
                }
                return false;
            });
        }
    }

    //登录
    private void requestLogin() {
//        startActivity(new Intent(this, CustomCameraActivity.class));
        String account = mLoginAccountTV.getText().toString();
        String password = mLoginPasswordTV.getText().toString();
        if (TextUtils.isEmpty(account)) {
            ToastUtil.showShort(myApp, "账号不能为空");
            return;
        } else if (TextUtils.isEmpty(password)) {
            ToastUtil.showShort(myApp, "密码不能为空");
            return;
        }
        LoginReq req = new LoginReq();
        req.username = account;
        req.password = password;
        req.reg_id = SharedPrefsUtil.getInstance(LoginActivity.this).getValue("reg_id", "");

        ApiUtil.requestUrl4Data(this, Api.getAuthService().login(req), (OnItemDataCallBack<LoginResp>) this::loginSuccess);
    }


    //    隐藏&显示密码
    private void clickPasswordEye() {
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
    }

    //登陆成功
    private void loginSuccess(LoginResp resp) {
        if (resp != null) {
            Yusion4sApp.isLogin = true;

            Yusion4sApp.TOKEN = resp.token;
            Yusion4sApp.ACCOUNT = mLoginAccountTV.getText().toString();
            mLoginAccountTV.setText("");
            mLoginPasswordTV.setText("");
            SharedPrefsUtil.getInstance(LoginActivity.this).putValue("token", Yusion4sApp.TOKEN);
            SharedPrefsUtil.getInstance(LoginActivity.this).putValue("mobile", Yusion4sApp.ACCOUNT);
            SharedPrefsUtil.getInstance(LoginActivity.this).putValue("account", Yusion4sApp.ACCOUNT);
            if (!Settings.forAppium) {
                // new Thread(this::uploadPersonAndDeviceInfo).start();
                Intent intent = new Intent(this, UploadPersonInfoService.class);
                startService(intent);
                //   startService(intent);
            }
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            ToastUtil.showShort(LoginActivity.this, "登陆成功");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Yusion4sApp.isLogin = false;
        myApp.clearUserData();

        //清除存在sp的dlrid和订单状态数
        removeDlrNums();
    }

    private void removeDlrNums() {
        String dlr_nums = SharedPrefsUtil.getInstance(this).getValue("dlr_nums", null);
        if (dlr_nums != null) {
            String[] dlr_num = dlr_nums.split("/");
            for (String id : dlr_num) {
                SharedPrefsUtil.getInstance(this).remove(id);
                SharedPrefsUtil.getInstance(this).remove("dlr_nums");
            }
        }
    }

    @Override
    public void onBackPressed() {
        AppUtils.exit();
    }
}
