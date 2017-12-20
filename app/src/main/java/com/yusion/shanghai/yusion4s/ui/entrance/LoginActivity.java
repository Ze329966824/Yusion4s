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

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.Yusion4sApp;
import com.yusion.shanghai.yusion4s.base.ActivityManager;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.bean.login.LoginReq;
import com.yusion.shanghai.yusion4s.bean.login.LoginResp;
import com.yusion.shanghai.yusion4s.retrofit.Api;
import com.yusion.shanghai.yusion4s.retrofit.api.AuthApi;
import com.yusion.shanghai.yusion4s.retrofit.api.ConfigApi;
import com.yusion.shanghai.yusion4s.retrofit.api.PersonApi;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.settings.Settings;
import com.yusion.shanghai.yusion4s.ubt.bean.UBTData;
import com.yusion.shanghai.yusion4s.ui.MainActivity;
import com.yusion.shanghai.yusion4s.utils.ApiUtil;
import com.yusion.shanghai.yusion4s.utils.MobileDataUtil;
import com.yusion.shanghai.yusion4s.utils.SharedPrefsUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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
        Log.e("TAG", "onCreate: ");
    }

    private void initView() {
        telephonyManager = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        mLoginAccountTV = findViewById(R.id.login_account_edt);
        mLoginPasswordTV = findViewById(R.id.login_password_edt);
        mLoginPasswordEyeImg = findViewById(R.id.login_password_eye_img);
        mLoginPasswordEyeImg.setOnClickListener(v -> clickPasswordEye());
        loginBtn = findViewById(R.id.login_submit_btn);
        loginBtn.setOnClickListener(v -> login());


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
    private void login() {
//        StringBuilder builder = new StringBuilder();
//        for (int i1 = 0; i1 < "长城".length(); i1++) {
//            //利用TinyPinyin将char转成拼音
//            //查看源码，方法内 如果char为汉字，则返回大写拼音
//            //如果c不是汉字，则返回String.valueOf(c)
//            builder.append(Pinyin.toPinyin("长城".charAt(i1)).toUpperCase());
//        }
//        Log.e("TAG", "login: " + builder.toString());

//        Intent intent = new Intent(this, CarSelectActivity.class);
//        intent.putExtra("class", LoginActivity.class);
//        intent.putExtra("should_reset", false);
//        startActivity(intent);
        String account = mLoginAccountTV.getText().toString();
        String password = mLoginPasswordTV.getText().toString();
        if (TextUtils.isEmpty(account)) {
            Toast.makeText(myApp, "账号不能为空!", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(myApp, "密码不能为空!", Toast.LENGTH_SHORT).show();
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
                new Thread(this::uploadPersonAndDeviceInfo).start();
            }
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadPersonAndDeviceInfo() {
        UBTData req = new UBTData(this);
        String imei = telephonyManager.getDeviceId();
        String imsi = telephonyManager.getSubscriberId();
        req.imei = imei;
        req.imsi = imsi;
        req.app = "Yusion4s";
        req.token = SharedPrefsUtil.getInstance(this).getValue("token", null);
        req.mobile = SharedPrefsUtil.getInstance(this).getValue("account", null);
        JSONArray contactJsonArray = MobileDataUtil.getUserData(this, "contact");
        List<UBTData.DataBean.ContactBean> contactBeenList = new ArrayList<>();

        for (int i = 0; i < contactJsonArray.length(); i++) {
            JSONObject jsonObject = null;
            try {
                jsonObject = contactJsonArray.getJSONObject(i);
                UBTData.DataBean.ContactBean contactListBean = new UBTData.DataBean.ContactBean();

                contactListBean.data1 = jsonObject.optString("data1");
                contactListBean.display_name = jsonObject.optString("display_name");

                contactBeenList.add(contactListBean);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        UBTData.DataBean contactBean = new UBTData.DataBean();
        contactBean.category = "contact";
        req.data.add(contactBean);
        if (contactBeenList.size() > 0 && !contactBeenList.isEmpty()) {
            contactBean.contact_list = contactBeenList;
        }

        JSONArray smsJsonArray = MobileDataUtil.getUserData(this, "sms");
        List<UBTData.DataBean.SmsBean> smsList = new ArrayList<>();
        for (int i = 0; i < smsJsonArray.length(); i++) {
            JSONObject jsonObject = null;
            try {
                jsonObject = smsJsonArray.getJSONObject(i);
                UBTData.DataBean.SmsBean smsListBean = new UBTData.DataBean.SmsBean();
                String type = jsonObject.optString("type");
                if (type.equals("1")) {
                    smsListBean.from = jsonObject.optString("address");
                    smsListBean.content = jsonObject.optString("body");
                    smsListBean.type = "recv";
                    smsListBean.ts = jsonObject.optString("date");
                } else if (type.equals("2")) {
                    smsListBean.to = jsonObject.optString("address");
                    smsListBean.content = jsonObject.optString("body");
                    smsListBean.type = "snd";
                    smsListBean.ts = jsonObject.optString("date");//date
                }
                smsList.add(smsListBean);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        UBTData.DataBean simBean = new UBTData.DataBean();
        simBean.category = "sms";
        req.data.add(simBean);
        if (smsList.size() > 0 && !smsList.isEmpty()) {
            simBean.sms_list = smsList;
        }

        AuthApi.checkUserInfo(this, data -> {
            contactBean.clt_nm = data.name;
            contactBean.mobile = data.mobile;
            simBean.clt_nm = data.name;
            simBean.mobile = data.mobile;
            PersonApi.uploadPersonAndDeviceInfo(req, new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                }
            });
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Yusion4sApp.isLogin = false;
        myApp.clearUserData();

        ConfigApi.getConfigJson(LoginActivity.this, null);

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
        ActivityManager.exit();
    }
}
