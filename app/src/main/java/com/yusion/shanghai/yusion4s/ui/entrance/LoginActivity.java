package com.yusion.shanghai.yusion4s.ui.entrance;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
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
    private ImageView wxBtn;
    private Button loginBtn;
    private TelephonyManager telephonyManager;
    private boolean isShowPassword = false;
    private String mtoken;
    private String username;

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
        wxBtn = findViewById(R.id.btn_wx);
        wxBtn.setOnClickListener(v -> wxLogin());
        setTestAccount();
    }

    private void wxLogin() {
        if (!api.isWXAppInstalled()) {
            Toast.makeText(this, "您还未安装微信客户端！", Toast.LENGTH_SHORT).show();
            return;
        }
        // 应用的作用域，获取个人信息®
        SendAuth.Req req = new SendAuth.Req();
        /**  用于保持请求和回调的状态，授权请求后原样带回给第三方  * 为了防止csrf攻击（跨站请求伪造攻击），后期改为随机数加session来校验   */
        req.scope = "snsapi_userinfo";
        req.state = "diandi_wx_login";
        api.sendReq(req);
    }

    private void wxLoginSuccess() {
        if (Yusion4sApp.TOKEN != null) {
            Yusion4sApp.ACCOUNT = username;
            SharedPrefsUtil.getInstance(LoginActivity.this).putValue("mobile", Yusion4sApp.ACCOUNT);
            SharedPrefsUtil.getInstance(LoginActivity.this).putValue("account", Yusion4sApp.ACCOUNT);
            Log.e("TAG", "wxLoginSuccess: "+Yusion4sApp.ACCOUNT);
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
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
                int offset = (int) getResources().getDimension(R.dimen.y150);
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
            ToastUtil.showShort(LoginActivity.this, "登录成功");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Yusion4sApp.isLogin = false;
        myApp.clearUserData();

        //清除存在sp的dlrid和订单状态数
        removeDlrNums();

        //由于 onNewIntent在onResume之前，所以在onNewIntent里存的token被清除了  所以要在这里存token
        if (mtoken != null) {
            Yusion4sApp.TOKEN = mtoken;
            SharedPrefsUtil.getInstance(LoginActivity.this).putValue("token", Yusion4sApp.TOKEN);
            mtoken = null;
            Log.e("TAG", "onResume: "+Yusion4sApp.ACCOUNT);

            wxLoginSuccess();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        mtoken = intent.getStringExtra("token");
        username = intent.getStringExtra("username");
        Yusion4sApp.TOKEN = mtoken;
        Log.e("TAG", "onNewIntent: "+username);
        Log.e("TAG", "onNewIntent: "+Yusion4sApp.ACCOUNT);

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
