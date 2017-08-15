package com.yusion.shanghai.yusion4s.ui.order;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.bean.order.OrderDetailBean;
import com.yusion.shanghai.yusion4s.retrofit.api.OrderApi;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;


/**
 * Description : 订单详情
 * Author : suijin
 * Date   : 17/03/31
 */

public class OrderDetailActivity extends BaseActivity {

    private RelativeLayout waitRel;
    private RelativeLayout cancelRel;
    private RelativeLayout passRel;
    private RelativeLayout rejectRel;
    private LinearLayout replyLin;
    private LinearLayout applyLin;
    private TextView applyBillPriceTv;
    private TextView applyFirstPriceTv;
    private TextView applyLoanPriceTv;
    private TextView applyManagementPriceTv;
    private TextView applyOtherPriceTv;
    private TextView applyTotalLoanPriceTv;
    private TextView applyLoanBankTv;
    private TextView applyProductTypeTv;
    private TextView applyPeriodsTv;
    private TextView replayBillPriceTv;
    private TextView replayFirstPriceTv;
    private TextView replayLoanPriceTv;
    private TextView replayManagementPriceTv;
    private TextView replayOtherPriceTv;
    private TextView replayTotalLoanPriceTv;
    private TextView replayLoanBankTv;
    private TextView replayProductTypeTv;
    private TextView replayPeriodsTv;
    private TextView dlrNameTv;
    private TextView salesNameTv;
    private TextView customerIdTv;
    private TextView customerNameTv;
    private TextView brandTv;
    private TextView trixTv;
    private TextView modelTv;
    private TextView guidePriceTv;
    private TextView waitReason;
    private TextView cancelReason;
    private TextView passReason;
    private TextView rejectReason;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_detail);
        initView();
        initTitleBar(this, "申请详情");
//        initView();
        initData();
    }

    private void initView() {
        waitRel = (RelativeLayout) findViewById(R.id.order_detail_status_wait_layout);
        cancelRel = (RelativeLayout) findViewById(R.id.order_detail_status_cancel_layout);
        passRel = (RelativeLayout) findViewById(R.id.order_detail_status_pass_layout);
        rejectRel = (RelativeLayout) findViewById(R.id.order_detail_status_reject_layout);
        replyLin = (LinearLayout) findViewById(R.id.order_detail_reply_lin);
        applyLin = (LinearLayout) findViewById(R.id.order_detail_apply_lin);

        waitReason = (TextView) findViewById(R.id.order_detail_status_wait_reason);
        cancelReason = (TextView) findViewById(R.id.order_detail_status_cancel_reason);
        passReason = (TextView) findViewById(R.id.order_detail_status_pass_reason);
        rejectReason = (TextView) findViewById(R.id.order_detail_status_reject_reason);

        applyBillPriceTv = (TextView) findViewById(R.id.order_detail_apply_bill_price_tv);
        applyFirstPriceTv = (TextView) findViewById(R.id.order_detail_apply_first_price_tv);
        applyLoanPriceTv = (TextView) findViewById(R.id.order_detail_apply_loan_price_tv);
        applyManagementPriceTv = (TextView) findViewById(R.id.order_detail_apply_management_price_tv);
        applyOtherPriceTv = (TextView) findViewById(R.id.order_detail_apply_other_price_tv);
        applyTotalLoanPriceTv = (TextView) findViewById(R.id.order_detail_apply_total_loan_price_tv);
        applyLoanBankTv = (TextView) findViewById(R.id.order_detail_apply_loan_bank_price_tv);
        applyProductTypeTv = (TextView) findViewById(R.id.order_detail_apply_product_type_tv);
        applyPeriodsTv = (TextView) findViewById(R.id.order_detail_apply_periods_tv);

        replayBillPriceTv = (TextView) findViewById(R.id.order_detail_replay_bill_price_tv);
        replayFirstPriceTv = (TextView) findViewById(R.id.order_detail_replay_first_price_tv);
        replayLoanPriceTv = (TextView) findViewById(R.id.order_detail_replay_loan_price_tv);
        replayManagementPriceTv = (TextView) findViewById(R.id.order_detail_replay_management_price_tv);
        replayOtherPriceTv = (TextView) findViewById(R.id.order_detail_replay_other_price_tv);
        replayTotalLoanPriceTv = (TextView) findViewById(R.id.order_detail_replay_total_loan_price_tv);
        replayLoanBankTv = (TextView) findViewById(R.id.order_detail_replay_loan_bank_price_tv);
        replayProductTypeTv = (TextView) findViewById(R.id.order_detail_replay_product_type_tv);
        replayPeriodsTv = (TextView) findViewById(R.id.order_detail_replay_periods_tv);

        brandTv = (TextView) findViewById(R.id.order_detail_brand_tv);
        trixTv = (TextView) findViewById(R.id.order_detail_trix_tv);
        modelTv = (TextView) findViewById(R.id.order_detail_model_tv);
        guidePriceTv = (TextView) findViewById(R.id.order_detail_guide_price_tv);


        dlrNameTv = (TextView) findViewById(R.id.order_detail_dlr_name_tv);
        salesNameTv = (TextView) findViewById(R.id.order_detail_sales_name_tv);
        customerIdTv = (TextView) findViewById(R.id.order_detail_customer_id_tv);
        customerNameTv = (TextView) findViewById(R.id.order_detail_customer_name_tv);
    }

    private void initData() {
        String app_id = getIntent().getStringExtra("app_id");
        OrderApi.getAppDetails(this, app_id, new OnItemDataCallBack<OrderDetailBean>() {
            @Override
            public void onItemDataCallBack(OrderDetailBean resp) {
                if (resp.status_st == 2) {//待审核
                    waitRel.setVisibility(View.VISIBLE);
                    passRel.setVisibility(View.GONE);
                    rejectRel.setVisibility(View.GONE);
                    applyLin.setVisibility(View.VISIBLE);
                    replyLin.setVisibility(View.GONE);
                } else if (resp.status_st == 4) {//待确认金融方案
                    passRel.setVisibility(View.VISIBLE);
                    waitRel.setVisibility(View.GONE);
                    rejectRel.setVisibility(View.GONE);
                    applyLin.setVisibility(View.VISIBLE);
                    replyLin.setVisibility(View.VISIBLE);
                } else if (resp.status_st == 6) {//放款中
                    passRel.setVisibility(View.VISIBLE);
                    waitRel.setVisibility(View.GONE);
                    rejectRel.setVisibility(View.GONE);
                    applyLin.setVisibility(View.VISIBLE);
                    replyLin.setVisibility(View.VISIBLE);
                } else if (resp.status_st == 3) {//审核失败
                    waitRel.setVisibility(View.GONE);
                    passRel.setVisibility(View.GONE);
                    rejectRel.setVisibility(View.VISIBLE);
                }

                applyBillPriceTv.setText(resp.vehicle_price);
                applyFirstPriceTv.setText(resp.vehicle_down_payment);
                applyLoanPriceTv.setText(resp.vehicle_loan_amt);
                applyManagementPriceTv.setText(resp.management_fee);
                applyOtherPriceTv.setText(resp.other_fee);
                applyTotalLoanPriceTv.setText(resp.loan_amt);
                applyLoanBankTv.setText(resp.loan_bank);
                applyProductTypeTv.setText(resp.product_type);
                applyPeriodsTv.setText(resp.nper);


                replayBillPriceTv.setText(resp.uw_detail.vehicle_price);
                replayFirstPriceTv.setText(resp.uw_detail.vehicle_down_payment);
                replayLoanPriceTv.setText(resp.uw_detail.vehicle_loan_amt);
                replayManagementPriceTv.setText(resp.uw_detail.management_fee);
                replayOtherPriceTv.setText(resp.uw_detail.other_fee);
                replayTotalLoanPriceTv.setText(resp.uw_detail.loan_amt);
                replayLoanBankTv.setText(resp.loan_bank);
                replayProductTypeTv.setText(resp.product_type);
                replayPeriodsTv.setText(resp.uw_detail.nper);

                brandTv.setText(resp.brand);
                trixTv.setText(resp.trix);
                modelTv.setText(resp.model_name);
                guidePriceTv.setText(resp.msrp);

                dlrNameTv.setText(resp.dlr_nm);
                salesNameTv.setText(resp.dlr_sales_name);
                customerIdTv.setText(resp.id_no);
                customerNameTv.setText(resp.clt_nm);
                findViewById(R.id.order_detail_customer_mobile_img).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + resp.mobile));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
                findViewById(R.id.order_detail_sales_mobile_img).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + resp.dlr_sales_mobile));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
            }
        });
    }

}
