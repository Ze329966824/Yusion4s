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
import com.yusion.shanghai.yusion4s.ubt.UBT;
import com.yusion.shanghai.yusion4s.ubt.annotate.BindView;
import com.yusion.shanghai.yusion4s.ui.entrance.apply_financing.AlterCarInfoActivity;
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


    @BindView(id = R.id.order_detail_sign, widgetName = "order_detail_sign", onClick = "submitMaterial")
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
    private TextView beforeDlrNameTv;
    private TextView beforeBrandTv;
    private TextView beforeTrixTv;
    private TextView beforeModelTv;
    private TextView beforeColorTv;
    private TextView beforeCondTv;
    private TextView beforeGuidePriceTv;
    private TextView beforeBillPriceTv;
    private TextView beforeLoanAmtTv;
    private TextView beforeDownPaymentTv;
    private TextView beforeManagementFeeTv;
    private TextView beforeOtherFeeTv;
    private TextView beforeTotalLoanAmtTv;
    private TextView beforeLoanBankTv;
    private TextView beforeProductTypeTv;
    private TextView beforeNperTv;
    private TextView beforeRegAddrTv;
    private TextView colorTv;
    private TextView condTv;
    private TextView billPriceTv;
    private TextView loanAmtTv;
    private TextView downPaymentTv;
    private TextView managementFeeTv;
    private TextView otherFeeTv;
    private TextView totalLoanAmtTv;
    private TextView loanBankTv;
    private TextView productTypeTv;
    private TextView nperTv;
    private TextView regAddrTv;
    private LinearLayout orderInfoTitleLin;
    private LinearLayout order_detail_sign_layout;
    private LinearLayout order_detail_change_layout;
    private TextView orderDetailChangeBtn;
    private TextView orderDetailUploadBtn;


    private int status_st;
    private Boolean modify_permission;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_detail);
        UBT.bind(this);
        app_id = getIntent().getStringExtra("app_id");
        status_st = getIntent().getIntExtra("status_st", 0);
        modify_permission = getIntent().getBooleanExtra("modify_permission", false);


        initView();
        initTitleBar(this, "申请详情");
    }

    @Override
    protected void onResume() {
        super.onResume();
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


        orderInfoTitleLin = (LinearLayout) findViewById(R.id.order_detail_order_info_title);
        dlrNameTv = (TextView) findViewById(R.id.order_detail_dlr_name_tv);
        beforeDlrNameTv = (TextView) findViewById(R.id.order_detail_before_dlr_name_tv);
        brandTv = (TextView) findViewById(R.id.order_detail_brand_tv);
        beforeBrandTv = (TextView) findViewById(R.id.order_detail_before_brand_tv);
        trixTv = (TextView) findViewById(R.id.order_detail_trix_tv);
        beforeTrixTv = (TextView) findViewById(R.id.order_detail_before_trix_tv);
        modelTv = (TextView) findViewById(R.id.order_detail_model_tv);
        beforeModelTv = (TextView) findViewById(R.id.order_detail_before_model_tv);
        colorTv = (TextView) findViewById(R.id.order_detail_color_tv);
        beforeColorTv = (TextView) findViewById(R.id.order_detail_before_color_tv);
        condTv = (TextView) findViewById(R.id.order_detail_vehicle_cond_tv);
        beforeCondTv = (TextView) findViewById(R.id.order_detail_before_vehicle_cond_tv);
        guidePriceTv = (TextView) findViewById(R.id.order_detail_guide_price_tv);
        beforeGuidePriceTv = (TextView) findViewById(R.id.order_detail_before_guide_price_tv);
        billPriceTv = (TextView) findViewById(R.id.order_detail_vehicle_price_tv);
        beforeBillPriceTv = (TextView) findViewById(R.id.order_detail_before_vehicle_price_tv);
        //贷款额
        loanAmtTv = (TextView) findViewById(R.id.order_detail_vehicle_loan_amt_tv);
        beforeLoanAmtTv = (TextView) findViewById(R.id.order_detail_before_vehicle_loan_amt_tv);
        downPaymentTv = (TextView) findViewById(R.id.order_detail_vehicle_down_payment_tv);
        beforeDownPaymentTv = (TextView) findViewById(R.id.order_detail_before_vehicle_down_payment_tv);
        managementFeeTv = (TextView) findViewById(R.id.order_detail_management_fee_tv);
        beforeManagementFeeTv = (TextView) findViewById(R.id.order_detail_before_management_fee_tv);
        otherFeeTv = (TextView) findViewById(R.id.order_detail_other_fee_tv);
        beforeOtherFeeTv = (TextView) findViewById(R.id.order_detail_before_other_fee_tv);
        //总贷款额
        totalLoanAmtTv = (TextView) findViewById(R.id.order_detail_loan_amt_tv);
        beforeTotalLoanAmtTv = (TextView) findViewById(R.id.order_detail_before_loan_amt_tv);
        loanBankTv = (TextView) findViewById(R.id.order_detail_loan_bank_tv);
        beforeLoanBankTv = (TextView) findViewById(R.id.order_detail_before_loan_bank_tv);
        productTypeTv = (TextView) findViewById(R.id.order_detail_product_type_tv);
        beforeProductTypeTv = (TextView) findViewById(R.id.order_detail_before_product_type_tv);
        nperTv = (TextView) findViewById(R.id.order_detail_nper_tv);
        beforeNperTv = (TextView) findViewById(R.id.order_detail_before_nper_tv);
        regAddrTv = (TextView) findViewById(R.id.order_detail_plate_reg_addr_tv);
        beforeRegAddrTv = (TextView) findViewById(R.id.order_detail_before_plate_reg_addr_tv);


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

        order_detail_sign_layout = (LinearLayout) findViewById(R.id.order_detail_sign_layout);
        order_detail_change_layout = (LinearLayout) findViewById(R.id.order_detail_change_layout);

        orderDetailSignBtn = (Button) findViewById(R.id.order_detail_sign);
        orderDetailChangeBtn = (TextView) findViewById(R.id.order_detail_change_tv);
        orderDetailUploadBtn = (TextView) findViewById(R.id.order_detail_upload_tv);

        orderDetailFinanceProgramLin = (LinearLayout) findViewById(R.id.order_detail_finance_program_lin);




//        order_detail_sign_layout.setVisibility(View.VISIBLE);
//        order_detail_change_layout.setVisibility(View.GONE);
        if (modify_permission){
            order_detail_sign_layout.setVisibility(View.GONE);
            order_detail_change_layout.setVisibility(View.VISIBLE);
        }else {
            order_detail_sign_layout.setVisibility(View.VISIBLE);
            order_detail_change_layout.setVisibility(View.GONE);
        }
        orderDetailChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetailActivity.this, AlterCarInfoActivity.class);
                intent.putExtra("app_id", app_id);
                startActivity(intent);
            }
        });
        orderDetailUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetailActivity.this, SubmitInformationActivity.class);
                intent.putExtra("app_id", app_id);
                startActivity(intent);
            }
        });


        orderDetailChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetailActivity.this, AlterCarInfoActivity.class);
                intent.putExtra("app_id", app_id);
                startActivity(intent);
            }
        });
        orderDetailUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetailActivity.this, SubmitInformationActivity.class);
                intent.putExtra("app_id", app_id);
                startActivity(intent);
            }
        });

    }

    private void initData() {
        OrderApi.getAppDetails(this, app_id, new OnItemDataCallBack<OrderDetailBean>() {
            @Override
            public void onItemDataCallBack(OrderDetailBean resp) {
                if (resp == null) {
                    return;
                }
                if (resp.status_st == 2) {//待审核
                    waitRel.setVisibility(View.VISIBLE);
                    passRel.setVisibility(View.GONE);
                    rejectRel.setVisibility(View.GONE);
                    applyLin.setVisibility(View.VISIBLE);//visiable
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
                //金融方案申请和批复
                if (resp.uw && resp.uw_detail != null) {
                    orderDetailFinanceProgramLin.setVisibility(View.VISIBLE);
                    replayBillPriceTv.setText(resp.uw_detail.vehicle_price);
                    replayFirstPriceTv.setText(resp.uw_detail.vehicle_down_payment);
                    replayLoanPriceTv.setText(resp.uw_detail.vehicle_loan_amt);
                    replayManagementPriceTv.setText(resp.uw_detail.management_fee);
                    replayOtherPriceTv.setText(resp.uw_detail.other_fee);
                    replayTotalLoanPriceTv.setText(resp.uw_detail.loan_amt);
                    replayLoanBankTv.setText(resp.uw_detail.loan_bank);
                    replayProductTypeTv.setText(resp.uw_detail.product_type);
                    replayPeriodsTv.setText(resp.uw_detail.nper);
                    replyBillPriceTv2.setText(resp.uw_detail.vehicle_price);
                    replyFirstPriceTv2.setText(resp.uw_detail.vehicle_down_payment);
                    replyLoanPriceTv2.setText(resp.uw_detail.vehicle_loan_amt);
                    replyManagementPriceTv2.setText(resp.uw_detail.management_fee);
                    replyOtherPriceTv2.setText(resp.uw_detail.other_fee);
                    replyTotalPriceTv2.setText(resp.uw_detail.loan_amt);
                    replyBankTv2.setText(resp.uw_detail.loan_bank);
                    ReplyRepayDateTv2.setText(resp.uw_detail.nper + "期");
                    replyFirstPercentTv2.setText(resp.uw_detail.vehicle_down_payment_percent * 100 + "%");
                    replyProductTypeTv2.setText(resp.uw_detail.product_type);
                    replyMonthPriceTv.setText(resp.uw_detail.monthly_payment);
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
                applyBillPriceTv2.setText(resp.vehicle_price);
                applyFirstPriceTv2.setText(resp.vehicle_down_payment);
                applyLoanPriceTv2.setText(resp.vehicle_loan_amt);
                applyManagementPriceTv2.setText(resp.management_fee);
                applyOtherPriceTv2.setText(resp.other_fee);
                applyTotalPriceTv2.setText(resp.loan_amt);
                applyBankTv2.setText(resp.loan_bank);
                applyReplyDateTv2.setText(resp.nper + "期");
                applyFirstPercentTv2.setText(resp.vehicle_down_payment_percent * 100 + "%");
                applyProductTypeTv2.setText(resp.product_type);
                applyMonthPriceTv.setText(resp.monthly_payment);


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

                //----------------------------------------------------------------------------------------------------
//车辆原订单信息
                dlrNameTv.setText(resp.dlr_nm);
                brandTv.setText(resp.brand);
                trixTv.setText(resp.trix);
                modelTv.setText(resp.model_name);
                guidePriceTv.setText(resp.msrp);
                colorTv.setText(resp.vehicle_color);
                condTv.setText(resp.vehicle_cond);
                billPriceTv.setText(resp.vehicle_price);
                loanAmtTv.setText(resp.vehicle_loan_amt);
                totalLoanAmtTv.setText(resp.loan_amt);
                downPaymentTv.setText(resp.vehicle_down_payment);
                managementFeeTv.setText(resp.management_fee);
                otherFeeTv.setText(resp.other_fee);
                loanBankTv.setText(resp.loan_bank);
                productTypeTv.setText(resp.product_type);
                nperTv.setText(resp.nper);
                regAddrTv.setText(resp.plate_reg_addr);
                //----------------------------------------------------------------------------------------------------

                salesNameTv.setText(resp.dlr_sales_name);
                customerIdTv.setText(resp.id_no);
                customerNameTv.setText(resp.clt_nm);
// 车辆原信息和修改信息
                if (resp.is_modify && resp.old_app != null) {
                    orderInfoTitleLin.setVisibility(View.VISIBLE);

                    beforeDlrNameTv.setText(resp.old_app.dlr_nm);
                    beforeBrandTv.setText(resp.old_app.brand);
                    beforeTrixTv.setText(resp.old_app.trix);
                    beforeModelTv.setText(resp.old_app.model_name);
                    beforeGuidePriceTv.setText(resp.old_app.msrp);
                    beforeColorTv.setText(resp.old_app.vehicle_color);
                    beforeCondTv.setText(resp.old_app.vehicle_cond);
                    beforeBillPriceTv.setText(resp.old_app.vehicle_price);
                    beforeLoanAmtTv.setText(resp.old_app.vehicle_loan_amt);
                    beforeTotalLoanAmtTv.setText(resp.old_app.loan_amt);
                    beforeDownPaymentTv.setText(resp.old_app.vehicle_down_payment);
                    beforeManagementFeeTv.setText(resp.old_app.management_fee);
                    beforeOtherFeeTv.setText(resp.old_app.other_fee);
                    beforeLoanBankTv.setText(resp.old_app.loan_bank);
                    beforeProductTypeTv.setText(resp.old_app.product_type);
                    beforeNperTv.setText(resp.old_app.nper);
                    beforeRegAddrTv.setText(resp.old_app.plate_reg_addr);

                    dlrNameTv.setText(resp.new_app.dlr_nm);
                    brandTv.setText(resp.new_app.brand);
                    trixTv.setText(resp.new_app.trix);
                    modelTv.setText(resp.new_app.model_name);
                    guidePriceTv.setText(resp.new_app.msrp);
                    colorTv.setText(resp.new_app.vehicle_color);
                    condTv.setText(resp.new_app.vehicle_cond);
                    billPriceTv.setText(resp.new_app.vehicle_price);
                    loanAmtTv.setText(resp.new_app.vehicle_loan_amt);
                    totalLoanAmtTv.setText(resp.new_app.loan_amt);
                    downPaymentTv.setText(resp.new_app.vehicle_down_payment);
                    managementFeeTv.setText(resp.new_app.management_fee);
                    otherFeeTv.setText(resp.new_app.other_fee);
                    loanBankTv.setText(resp.new_app.loan_bank);
                    productTypeTv.setText(resp.new_app.product_type);
                    nperTv.setText(resp.new_app.nper);
                    regAddrTv.setText(resp.new_app.plate_reg_addr);

                    compare(beforeDlrNameTv, dlrNameTv);
                    compare(beforeBrandTv, brandTv);
                    compare(beforeTrixTv, trixTv);
                    compare(beforeModelTv, modelTv);
                    compare(beforeGuidePriceTv, guidePriceTv);
                    compare(beforeColorTv, colorTv);
                    compare(beforeCondTv, condTv);
                    compare(beforeBillPriceTv, billPriceTv);
                    compare(beforeLoanAmtTv, loanAmtTv);
                    compare(beforeTotalLoanAmtTv, totalLoanAmtTv);
                    compare(beforeDownPaymentTv, downPaymentTv);
                    compare(beforeManagementFeeTv, managementFeeTv);
                    compare(beforeOtherFeeTv, otherFeeTv);
                    compare(beforeLoanBankTv, loanBankTv);
                    compare(beforeProductTypeTv, productTypeTv);
                    compare(beforeNperTv, nperTv);
                    compare(beforeRegAddrTv, regAddrTv);

                } else {
                    orderInfoTitleLin.setVisibility(View.GONE);
                }


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
                orderDetailChangeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(OrderDetailActivity.this, "修改资料", Toast.LENGTH_SHORT).show();
                        Intent i1 = new Intent(OrderDetailActivity.this, AlterCarInfoActivity.class);
                        i1.putExtra("app_id", app_id);
                        startActivity(i1);
                    }
                });
                orderDetailUploadBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i2 = new Intent(OrderDetailActivity.this, SubmitInformationActivity.class);
                        i2.putExtra("app_id", app_id);
                        startActivity(i2);
                    }
                });

            }
        });


//        orderDetailSignBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(OrderDetailActivity.this, SubmitInformationActivity.class);
//                intent.putExtra("app_id", app_id);
//                startActivity(intent);
//            }
//        });

    }

    private void submitMaterial(View view) {
        Intent intent = new Intent(OrderDetailActivity.this, SubmitInformationActivity.class);
        intent.putExtra("app_id", app_id);
        startActivity(intent);
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
