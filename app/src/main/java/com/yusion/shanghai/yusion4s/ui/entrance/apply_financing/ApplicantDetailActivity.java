package com.yusion.shanghai.yusion4s.ui.entrance.apply_financing;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.bean.order.submit.GetApplicateDetailResp;
import com.yusion.shanghai.yusion4s.retrofit.api.OrderApi;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;

public class ApplicantDetailActivity extends BaseActivity {
    private String clt_id;
    private TextView client_name;
    private TextView client_mobile;
    private TextView client_sfz;
    private TextView client_sex;

    private TextView spouse_name;
    private TextView spouse_mobile;
    private TextView spouse_sfz;
    private TextView spouse_sex;

    private TextView guarantor_name;
    private TextView guarantor_mobile;
    private TextView guarantor_sfz;
    private TextView gurrantor_sex;

    private TextView guarantor_spouse_name;
    private TextView guarantor_spouse_mobile;
    private TextView guarantor_spouse_sfz;
    private TextView guarantor_spouse_sex;


    private LinearLayout clent_spouse_lin;//主贷人配偶

    private LinearLayout guarantor_lin;//担保人

    private LinearLayout guarantor_spouse_lin;//担保人配偶


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_detail);

        initTitleBar(this, "申请人详情").setLeftClickListener(v -> onBack());

        Intent intent = getIntent();
        clt_id = intent.getStringExtra("x");

        client_name = (TextView) findViewById(R.id.client_name);
        client_mobile = (TextView) findViewById(R.id.client_mobile);
        client_sfz = (TextView) findViewById(R.id.client_sfz);
        client_sex = (TextView) findViewById(R.id.client_sex);

        spouse_name = (TextView) findViewById(R.id.spouse_name);
        spouse_mobile = (TextView) findViewById(R.id.spouse_mobile);
        spouse_sfz = (TextView) findViewById(R.id.spouse_sfz);
        spouse_sex = (TextView) findViewById(R.id.spouse_sex);

        guarantor_name = (TextView) findViewById(R.id.guarantor_name);
        guarantor_mobile = (TextView) findViewById(R.id.guarantor_mobile);
        guarantor_sfz = (TextView) findViewById(R.id.guarantor_sfz);
        gurrantor_sex = (TextView) findViewById(R.id.guarantor_sex);

        guarantor_spouse_name = (TextView) findViewById(R.id.guarantor_spouse_name);
        guarantor_spouse_mobile = (TextView) findViewById(R.id.guarantor_spouse_mobile);
        guarantor_spouse_sfz = (TextView) findViewById(R.id.guarantor_spouse_sfz);
        guarantor_spouse_sex = (TextView) findViewById(R.id.guarantor_spouse_sex);

        clent_spouse_lin = (LinearLayout) findViewById(R.id.clent_spouse_lin);
        guarantor_lin = (LinearLayout) findViewById(R.id.guarantor_lin);
        guarantor_spouse_lin = (LinearLayout) findViewById(R.id.guarantor_spouse_lin);


        OrderApi.getApplicateDetail(this, clt_id, new OnItemDataCallBack<GetApplicateDetailResp>() {
            @Override
            public void onItemDataCallBack(GetApplicateDetailResp data) {
                if (data == null) return;
//                if (TextUtils.isEmpty(data.lender.clt_nm)) {
//                    client_name.setText("");
//                } else {
//                    client_name.setText(data.lender.clt_nm);
//                }

                setText(client_name, data.lender.clt_nm);
                setText(client_mobile, data.lender.mobile);
                setText(client_sfz, data.lender.id_no);
                setText(client_sex, data.lender.gender);

                if (data.lender_sp == null) {
                    clent_spouse_lin.setVisibility(View.GONE);
                } else {
                    clent_spouse_lin.setVisibility(View.VISIBLE);
                    setText(spouse_name, data.lender_sp.clt_nm);
                    setText(spouse_mobile, data.lender_sp.mobile);
                    setText(spouse_sfz, data.lender_sp.id_no);
                    setText(spouse_sex, data.lender_sp.gender);
                }

                if (data.guarantor == null) {
                    guarantor_lin.setVisibility(View.GONE);
                } else {
                    guarantor_lin.setVisibility(View.VISIBLE);
                    setText(guarantor_name, data.guarantor.clt_nm);
                    setText(guarantor_mobile, data.guarantor.mobile);
                    setText(guarantor_sfz, data.guarantor.id_no);
                    setText(gurrantor_sex, data.guarantor.gender);
                }
                if (data.guarantor_sp == null) {
                    guarantor_spouse_lin.setVisibility(View.GONE);
                } else {
                    guarantor_spouse_lin.setVisibility(View.VISIBLE);
                    setText(guarantor_spouse_name, data.guarantor_sp.clt_nm);
                    setText(guarantor_spouse_mobile, data.guarantor_sp.mobile);
                    setText(guarantor_spouse_sfz, data.guarantor_sp.id_no);
                    setText(guarantor_spouse_sex, data.guarantor_sp.gender);
                }

            }
        });
    }

    private void setText(TextView text, String str) {
        if (TextUtils.isEmpty(str)) {
            //text.setText("");
            text.setText("未填写");
        } else {
            text.setText(str);
        }

    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    private void onBack() {
        finish();
    }
}
