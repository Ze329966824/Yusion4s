package com.yusion.shanghai.yusion4s.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.Yusion4sApp;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.bean.order.SearchClientResp;
import com.yusion.shanghai.yusion4s.bean.upload.ListDealerLabelsResp;
import com.yusion.shanghai.yusion4s.retrofit.Api;
import com.yusion.shanghai.yusion4s.ui.order.OrderCreateActivity;
import com.yusion.shanghai.yusion4s.ui.upload.UploadLabelListActivity;
import com.yusion.shanghai.yusion4s.utils.ApiUtil;

public class CommitActivity extends BaseActivity {
    private Intent mGetIntent;
    private String clt_id;
    private String role;
    private String title;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commit);
        initTitleBar(this, "提交成功");
        intent = getIntent();

        switch (getIntent().getStringExtra("why_commit")) {

            case "old_car":             //二手车
                fromOldCar();
                break;

            case "new_car":             //新车
                fromNewCar();
                break;

            case "create_user":         //创建用户
                fromCreateUser();
                break;


            default:
                fromUnknown();
                break;
        }
    }

    private void fromUnknown() {
        ((TextView) findViewById(R.id.commit_title_tv)).setText("why come here?");
        findViewById(R.id.commit_oldcar_layout).setVisibility(View.GONE);
        findViewById(R.id.commit_create_layout).setVisibility(View.GONE);
        findViewById(R.id.commit_newcar_layout).setVisibility(View.GONE);
    }

    private void fromNewCar() {
        findViewById(R.id.commit_oldcar_layout).setVisibility(View.GONE);
        findViewById(R.id.commit_create_layout).setVisibility(View.GONE);
        findViewById(R.id.commit_newcar_layout).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.commit_title_tv)).setText("提交成功");
        TextView submitBtn = findViewById(R.id.commit_newcar_btn);
        submitBtn.setOnClickListener(v -> {
//            intent.setClass(CommitActivity.this,MainActivity.class);
            Intent i1 = new Intent(CommitActivity.this, MainActivity.class);
            i1.putExtra("from_commit", true);
            startActivity(i1);
            finish();
        });


    }

    private void fromCreateUser() {
        ((TextView) findViewById(R.id.commit_title_tv)).setText("客户创建成功");
        findViewById(R.id.commit_oldcar_layout).setVisibility(View.GONE);
        findViewById(R.id.commit_newcar_layout).setVisibility(View.GONE);
        findViewById(R.id.commit_create_layout).setVisibility(View.VISIBLE);
        TextView returnBtn = findViewById(R.id.commit_create_btn);
        intent.setClass(CommitActivity.this, OrderCreateActivity.class);
        returnBtn.setOnClickListener(v ->
                ApiUtil.requestUrl4Data(CommitActivity.this, Api.getOrderService().searchClient(intent.getStringExtra("id_no")), data -> {
//                OrderApi.searchClientExist(CommitActivity.this, intent.getStringExtra("id_no"), data -> {
                    if (data.size() == 1 && data.get(0) != null) {
                        checkAuthCreditExist(intent, data.get(0));
                        intent.putExtra("name", data.get(0).clt_nm);
                        intent.putExtra("sfz", data.get(0).id_no);
                        intent.putExtra("mobile", data.get(0).mobile);
                        intent.putExtra("why_come", "create_user");
                        intent.putExtra("enable", true);
                        startActivity(intent);
                        finish();
                    }
                }));

    }

    private void fromOldCar() {
        findViewById(R.id.commit_create_layout).setVisibility(View.GONE);
        findViewById(R.id.commit_newcar_layout).setVisibility(View.GONE);
        findViewById(R.id.commit_oldcar_layout).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.commit_title_tv)).setText("还差最后一步!");

        TextView submitBtn = findViewById(R.id.commit_oldcar_btn);
        submitBtn.setOnClickListener(v ->
                ApiUtil.requestUrl4Data(CommitActivity.this, Api.getUploadService().listDealerLabels(getIntent().getStringExtra("app_id")), data -> {
//                UploadApi.listDealerLabels(CommitActivity.this, getIntent().getStringExtra("app_id"), data -> {
                    ListDealerLabelsResp item = null;
                    for (ListDealerLabelsResp listDealerLabelsResp : data) {
                        if (listDealerLabelsResp.value.equals(((Yusion4sApp) getApplication()).getConfigResp().send_hand_base_material)) {
                            item = listDealerLabelsResp;
                            break;
                        }
                    }
                    if (item == null) {
                        Toast.makeText(myApp, "没有找到指定影像件标签,请稍后补充上传", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent intent1 = new Intent(CommitActivity.this, UploadLabelListActivity.class);
                    intent1.putExtra("topItem", item);
                    intent1.putExtra("app_id", getIntent().getStringExtra("app_id"));
                    startActivity(intent1);
                    finish();
                }));
    }

    private void checkAuthCreditExist(Intent intent, SearchClientResp item) {
        if (item.auth_credit.lender != null) {//如果不等于空
            intent.putExtra("isHasLender", "1");
            intent.putExtra("lender_clt_id", item.auth_credit.lender.clt_id);
            intent.putExtra("lender", item.auth_credit.lender.auth_credit_img_count);
        } else {
            intent.putExtra("isHasLender", "2");
        }
        if (item.auth_credit.lender_sp != null) {//如果不等于空
            intent.putExtra("isHasLender_sp", "1");
            intent.putExtra("lender_sp_clt_id", item.auth_credit.lender_sp.clt_id);
            intent.putExtra("lender_sp", item.auth_credit.lender_sp.auth_credit_img_count);
        } else {
            intent.putExtra("isHasLender_sp", "2");
        }

        if (item.auth_credit.guarantor != null) {//如果不等于空
            intent.putExtra("isGuarantor", "1");
            intent.putExtra("guarantor_clt_id", item.auth_credit.guarantor.clt_id);
            intent.putExtra("guarantor", item.auth_credit.guarantor.auth_credit_img_count);
        } else {
            intent.putExtra("isGuarantor", "2");
        }

        if (item.auth_credit.guarantor_sp != null) {//如果不等于空
            intent.putExtra("isGuarantor_sp", "1");
            intent.putExtra("guarantor_sp_clt_id", item.auth_credit.guarantor_sp.clt_id);
            intent.putExtra("guarantor_sp", item.auth_credit.guarantor_sp.auth_credit_img_count);

        } else {
            intent.putExtra("isGuarantor_sp", "2");
        }

    }
}

