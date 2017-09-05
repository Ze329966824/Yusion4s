package com.yusion.shanghai.yusion4s.ui.order;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.bean.order.OrderDetailBean;
import com.yusion.shanghai.yusion4s.retrofit.api.OrderApi;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.ui.upload.SubmitInformationActivity;


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

    private Button orderDetailSignBtn;
    private String app_id;
    //申请和批复的金融方案
    private TextView applyFirstPercentTv2;
    private TextView replyFirstPercentTv2;
    private TextView applyBillPriceTv2;
    private TextView replyBillPriceTv2;

    private TextView applyFirstPriceTv2;
    private TextView replyFirstPriceTv2;

    private TextView applyLoanPriceTv2;
    private TextView replyLoanPriceTv2;

    private TextView applyManagementPriceTv2;
    private TextView replyManagementPriceTv2;

    private TextView applyOtherPriceTv2;
    private TextView replyOtherPriceTv2;

    private TextView applyTotalPriceTv2;
    private TextView replyTotalPriceTv2;

    private TextView applyBankTv2;
    private TextView replyBankTv2;

    private TextView applyReplyDateTv2;
    private TextView ReplyRepayDateTv2;

    private TextView applyProductTypeTv2;
    private TextView replyProductTypeTv2;

    private FloatingActionButton fab;
    private NestedScrollView mScrollView;

    private TextView applyMonthPriceTv;
    private TextView replyMonthPriceTv;

    private LinearLayout orderDetailFinanceProgramLin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.order_detail);
        app_id = getIntent().getStringExtra("app_id");

        initView();
        initTitleBar(this, "申请详情");

        initData();

    }

    private void initView() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        mScrollView = (NestedScrollView) findViewById(R.id.scrollView_four);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScrollView.smoothScrollTo(0, 0);
            }
        });
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


        applyFirstPercentTv2 = (TextView) findViewById(R.id.apply_first_percent_tv);
        replyFirstPercentTv2 = (TextView) findViewById(R.id.reply_first_percent_tv);
        applyBillPriceTv2 = (TextView) findViewById(R.id.apply_bill_price_tv2);
        replyBillPriceTv2 = (TextView) findViewById(R.id.reply_bill_price_tv2);

        applyFirstPriceTv2 = (TextView) findViewById(R.id.apply_first_price_tv2);
        replyFirstPriceTv2 = (TextView) findViewById(R.id.reply_first_price_tv2);

        applyLoanPriceTv2 = (TextView) findViewById(R.id.apply_loan_price_tv2);
        replyLoanPriceTv2 = (TextView) findViewById(R.id.reply_loan_price_tv2);

        applyManagementPriceTv2 = (TextView) findViewById(R.id.apply_management_price_tv2);
        replyManagementPriceTv2 = (TextView) findViewById(R.id.reply_management_price_tv2);

        applyOtherPriceTv2 = (TextView) findViewById(R.id.apply_other_price_tv2);
        replyOtherPriceTv2 = (TextView) findViewById(R.id.reply_other_price_tv2);

        applyBankTv2 = (TextView) findViewById(R.id.apply_bank_tv2);
        replyBankTv2 = (TextView) findViewById(R.id.reply_bank_tv2);

        applyReplyDateTv2 = (TextView) findViewById(R.id.apply_repay_date_tv2);
        ReplyRepayDateTv2 = (TextView) findViewById(R.id.reply_repay_data_tv2);

        applyTotalPriceTv2 = (TextView) findViewById(R.id.apply_total_price_tv2);
        replyTotalPriceTv2 = (TextView) findViewById(R.id.reply_total_price_tv2);

        applyProductTypeTv2 = (TextView) findViewById(R.id.apply_product_type_tv2);
        replyProductTypeTv2 = (TextView) findViewById(R.id.reply_product_type_tv2);

        applyMonthPriceTv = (TextView) findViewById(R.id.apply_month_price_tv);
        replyMonthPriceTv = (TextView) findViewById(R.id.reply_month_price_tv);

        orderDetailSignBtn = (Button) findViewById(R.id.order_detail_sign);

        orderDetailFinanceProgramLin = (LinearLayout) findViewById(R.id.order_detail_finance_program_lin);
    }

    private void initData() {
        //String app_id = getIntent().getStringExtra("app_id");
        OrderApi.getAppDetails(this, app_id, new OnItemDataCallBack<OrderDetailBean>() {
            @Override
            public void onItemDataCallBack(OrderDetailBean resp) {
                if (resp.status_st == 2) {//待审核
                    waitRel.setVisibility(View.VISIBLE);
                    passRel.setVisibility(View.GONE);
                    rejectRel.setVisibility(View.GONE);
                    applyLin.setVisibility(View.GONE);//visiable
                    replyLin.setVisibility(View.GONE);
                    //orderDetailFinanceProgramLin.setVisibility(View.VISIBLE);
                } else if (resp.status_st == 4) {//待确认金融方案 //有批复的
                    passRel.setVisibility(View.VISIBLE);
                    waitRel.setVisibility(View.GONE);
                    rejectRel.setVisibility(View.GONE);
                    applyLin.setVisibility(View.GONE);
                    replyLin.setVisibility(View.VISIBLE);
                    // orderDetailFinanceProgramLin.setVisibility(View.VISIBLE);
                } else if (resp.status_st == 6) {//放款中      //有批复的
                    passRel.setVisibility(View.VISIBLE);
                    waitRel.setVisibility(View.GONE);
                    rejectRel.setVisibility(View.GONE);
                    applyLin.setVisibility(View.GONE);
                    replyLin.setVisibility(View.VISIBLE);
                    //orderDetailFinanceProgramLin.setVisibility(View.VISIBLE);
                } else if (resp.status_st == 3) {//审核失败
                    waitRel.setVisibility(View.GONE);
                    passRel.setVisibility(View.GONE);
                    rejectRel.setVisibility(View.VISIBLE);
                    //  orderDetailFinanceProgramLin.setVisibility(View.GONE);
                }
                if (resp.uw) {
                    orderDetailFinanceProgramLin.setVisibility(View.VISIBLE);
                } else {
                    orderDetailFinanceProgramLin.setVisibility(View.GONE);
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


                applyBillPriceTv2.setText(resp.vehicle_price);
                replyBillPriceTv2.setText(resp.uw_detail.vehicle_price);
                //compare(resp.getApp().getVehicle_price(),resp.getUw().getVehicle_price(),applyBillPriceTv,replyBillPriceTv);

                applyFirstPriceTv2.setText(resp.vehicle_down_payment);
                replyFirstPriceTv2.setText(resp.uw_detail.vehicle_down_payment);

                applyLoanPriceTv2.setText(resp.vehicle_loan_amt);
                replyLoanPriceTv2.setText(resp.uw_detail.vehicle_loan_amt);

                applyManagementPriceTv2.setText(resp.management_fee);
                replyManagementPriceTv2.setText(resp.uw_detail.management_fee);

                applyOtherPriceTv2.setText(resp.other_fee);
                replyOtherPriceTv2.setText(resp.uw_detail.other_fee);

                applyTotalPriceTv2.setText(resp.loan_amt);
                replyTotalPriceTv2.setText(resp.uw_detail.loan_amt);

                applyBankTv2.setText(resp.loan_bank);
                replyBankTv2.setText(resp.uw_detail.loan_bank);

                applyReplyDateTv2.setText(resp.nper + "期");
                ReplyRepayDateTv2.setText(resp.uw_detail.nper + "期");

                applyFirstPercentTv2.setText(resp.vehicle_down_payment_percent * 100 + "%");
                replyFirstPercentTv2.setText(resp.uw_detail.vehicle_down_payment_percent * 100 + "%");

                applyProductTypeTv2.setText(resp.product_type);
                replyProductTypeTv2.setText(resp.uw_detail.product_type);

                applyMonthPriceTv.setText(resp.monthly_payment);
                replyMonthPriceTv.setText(resp.uw_detail.monthly_payment);


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

        compare(applyMonthPriceTv, replyMonthPriceTv);
        compare(applyFirstPercentTv2, replyFirstPercentTv2);
        compare(applyBillPriceTv2, replyBillPriceTv2);
        compare(applyFirstPriceTv2, replyFirstPriceTv2);
        compare(applyLoanPriceTv2, replyLoanPriceTv2);
        compare(applyManagementPriceTv2, replyManagementPriceTv2);
        compare(applyOtherPriceTv2, replyOtherPriceTv2);
        compare(applyTotalPriceTv2, replyTotalPriceTv2);
        compare(applyBankTv2, replyBankTv2);
        compare(applyReplyDateTv2, ReplyRepayDateTv2);
        compare(applyProductTypeTv2, replyProductTypeTv2);

        orderDetailSignBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetailActivity.this, SubmitInformationActivity.class);
                intent.putExtra("app_id", app_id);
                startActivity(intent);
            }
        });

    }

    private void compare(TextView tv1, TextView tv2) {
        if (tv1.getText().toString().equals(tv2.getText().toString())) {
            tv1.setTextColor(Color.parseColor("#999999"));
            tv2.setTextColor(Color.parseColor("#222a36"));
        } else {
            tv1.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            tv1.setTextColor(Color.parseColor("#999999"));
            tv2.setTextColor(Color.parseColor("#CBA053"));
        }
    }


}
