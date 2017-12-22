package com.yusion.shanghai.yusion4s.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.bean.login.LoginReq;
import com.yusion.shanghai.yusion4s.bean.login.LoginResp;
import com.yusion.shanghai.yusion4s.retrofit.api.AuthApi;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.settings.Settings;
import com.yusion.shanghai.yusion4s.ui.yusion.apply.CreateUserActivity;
import com.yusion.shanghai.yusion4s.utils.CheckMobileUtil;
import com.yusion.shanghai.yusion4s.widget.IdentifyingCodeView;


public class VerificationCodeActivity extends BaseActivity {
    private IdentifyingCodeView icv;
    private Button sentVerificationBtn;
    private CountDownTimer timer;
    private ImageView successImg;
    private String relVerifyCode;
    private TextView mobileTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code);
        initTitleBar(VerificationCodeActivity.this, "").setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(VerificationCodeActivity.this).setPositiveButton("确定退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onBackPressed();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                }).show();
            }
        });
        findViewById(R.id.syz_tessssssssst_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VerificationCodeActivity.this, CreateUserActivity.class);
                intent.putExtra("mobile", "mobile");
                startActivity(intent);
            }
        });
        successImg = (ImageView) findViewById(R.id.code_success_img);
        mobileTV = (TextView) findViewById(R.id.verification_code_mobile_tv);
        mobileTV.setText(getIntent().getStringExtra("mobile"));
        sentVerificationBtn = (Button) findViewById(R.id.send_verification_code);
        icv = (IdentifyingCodeView) findViewById(R.id.icv);
        initData();

        icv.setInputCompleteListener(new IdentifyingCodeView.InputCompleteListener() {
            @Override
            public void inputComplete() {
                if (icv.getTextContent().length() == 4) {
                    LoginReq loginReq = new LoginReq();
                    loginReq.mobile = mobileTV.getText().toString();
                    loginReq.verify_code = icv.getTextContent().toString();
                    AuthApi.yusionLogin(VerificationCodeActivity.this, loginReq, new OnItemDataCallBack<LoginResp>() {
                        @Override
                        public void onItemDataCallBack(LoginResp data) {
                            if (data == null) {
                                icv.clearAllText();
                            } else {
                                Toast.makeText(myApp, "授权成功", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(VerificationCodeActivity.this, CreateUserActivity.class);
                                intent.putExtra("mobile", mobileTV.getText().toString());
                                intent.putExtra("token", data.token);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }

            @Override
            public void deleteContent() {
                Log.i("icv_delete", icv.getTextContent());
            }
        });
    }

    public void initData() {
        long totalTime = 60000;
        if (!Settings.isOnline) {
            totalTime = 50000;
        }
        timer = new CountDownTimer(totalTime, 1000) {
            @Override
            public void onTick(long l) {
                sentVerificationBtn.setText(l / 1000 + "秒后重试");
            }

            @Override
            public void onFinish() {
                sentVerificationBtn.setEnabled(true);
                sentVerificationBtn.setText("发送验证码");
            }
        };
        sentVerificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!CheckMobileUtil.checkMobile(mobileTV.getText().toString())) {
                    Toast.makeText(myApp, "手机号格式不正确", Toast.LENGTH_SHORT).show();
                    return;
                }
                view.setEnabled(false);
                timer.start();

                AuthApi.getVCode(VerificationCodeActivity.this, mobileTV.getText().toString(), data -> {
                    if (data == null) {
                        Toast.makeText(myApp, "获取验证码失败", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(myApp, "获取验证码成功", Toast.LENGTH_SHORT).show();
                        relVerifyCode = data.verify_code;
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View view) {
        icv.clearAllText();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.activity_close);
    }
}
