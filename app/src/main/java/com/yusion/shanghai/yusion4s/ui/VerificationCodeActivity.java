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
import com.yusion.shanghai.yusion4s.bean.product.GetAuthenticationVerifyCodeReq;
import com.yusion.shanghai.yusion4s.bean.product.RequestAuthenticationReq;
import com.yusion.shanghai.yusion4s.retrofit.api.ProductApi;
import com.yusion.shanghai.yusion4s.ui.yusion.apply.ApplyActivity;
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
                Intent intent = new Intent(VerificationCodeActivity.this, ApplyActivity.class);
                intent.putExtra("mobile","mobile");
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
                    RequestAuthenticationReq req = new RequestAuthenticationReq();
//                    req.mobile = getIntent().getStringExtra("mobile");
//                    req.oneself_clt_id = getIntent().getStringExtra("oneself_clt_id");
                    req.other_clt_id = getIntent().getStringExtra("other_clt_id");
                    req.relation_ship = getIntent().getStringExtra("relation_ship");
                    req.verify_code = icv.getTextContent();
//                    ProductApi.requestAuthentication(VerificationCodeActivity.this, req, new OnCodeAndMsgCallBack() {
//                        @Override
//                        public void callBack(int code, String msg) {
//                            if (code >= 0) {
//                                successImg.setVisibility(View.VISIBLE);
//                                Handler handler = new Handler();
//                                handler.postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        Intent intent = new Intent();
//                                        intent.putExtra("isSuccess", true);
//                                        intent.putExtra("verifyCode", relVerifyCode);
//                                        setResult(RESULT_OK, intent);
//                                        finish();
//                                    }
//                                }, 1500);
//                            } else {
//                                Toast.makeText(myApp, "验证失败", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });


                }
            }

            @Override
            public void deleteContent() {
                Log.i("icv_delete", icv.getTextContent());
            }
        });
    }

    public void initData() {
        timer = new CountDownTimer(60000, 1000) {
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
                view.setEnabled(false);
                timer.start();

                GetAuthenticationVerifyCodeReq req = new GetAuthenticationVerifyCodeReq();
                req.mobile = getIntent().getStringExtra("mobile");
                req.oneself_clt_id = getIntent().getStringExtra("oneself_clt_id");
                req.other_clt_id = getIntent().getStringExtra("other_clt_id");
                req.relation_ship = getIntent().getStringExtra("relation_ship");
                ProductApi.getAuthenticationVerifyCode(VerificationCodeActivity.this, req, data -> {
                    if (data == null) {
                        Toast.makeText(myApp, "获取验证码失败", Toast.LENGTH_SHORT).show();
                    } else {
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
