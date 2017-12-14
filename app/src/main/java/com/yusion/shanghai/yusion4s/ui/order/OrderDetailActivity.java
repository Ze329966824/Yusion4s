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
import com.yusion.shanghai.yusion4s.retrofit.api.OrderApi;
import com.yusion.shanghai.yusion4s.ubt.UBT;
import com.yusion.shanghai.yusion4s.ubt.annotate.BindView;
import com.yusion.shanghai.yusion4s.ui.MainActivity;
import com.yusion.shanghai.yusion4s.ui.entrance.apply_financing.AlterCarInfoActivity;
import com.yusion.shanghai.yusion4s.ui.entrance.apply_financing.AlterOldCarInfoActivity;
import com.yusion.shanghai.yusion4s.ui.upload.SubmitInformationActivity;


/**
 * Description : 订单详情
 * Author : suijin
 * Date   : 17/03/31
 */

public class OrderDetailActivity extends BaseActivity {


    private LinearLayout waitLin;        //审核中
    private RelativeLayout cancelRel;    //取消
    private RelativeLayout passRel;      //审核通过
    private RelativeLayout rejectRel;    //审核拒绝


    private LinearLayout nore_financeLin;         //金融方案
    private TextView applyBillPriceTv;        //金融方案-车辆开票价
    private TextView applyFirstPriceTv;       //金融方案-车辆首付款
    private TextView applyLoanPriceTv;        //金融方案-车辆贷款金额
    private TextView applyManagementPriceTv;  //金融方案-档案管理费
    private TextView applyOtherPriceTv;       //金融方案-其他费用
    private TextView applyTotalLoanPriceTv;   //金融方案-总贷款额
    private TextView applyLoanBankTv;         //金融方案-贷款银行
    private TextView applyProductTypeTv;      //金融方案-产品类型
    private TextView applyPeriodsTv;          //金融方案-还款期限
    private TextView applyBusinessTv;         //金融方案-二手车的交易价

    private LinearLayout havere_financeLin;                //金融方案(有批复)
    private TextView havere_applyFirstPercentTv;     //金融方案(有批复)-首付比例(申请信息)
    private TextView havere_replyFirstPercentTv;     //金融方案(有批复)-首付比例(批复信息)
    private TextView havere_applyBillPriceTv;        //金融方案(有批复)-车辆开票价(申请信息)
    private TextView havere_replyBillPriceTv;        //金融方案(有批复)-车辆开票价(批复信息)
    private TextView havere_applyFirstPriceTv;       //金融方案(有批复)-车辆首付款(申请信息)
    private TextView havere_replyFirstPriceTv;       //金融方案(有批复)-车辆首付款(批复信息)
    private TextView havere_applyLoanPriceTv;        //金融方案(有批复)-车辆贷款金额(申请信息)
    private TextView havere_replyLoanPriceTv;        //金融方案(有批复)-车辆贷款金额(批复信息)
    private TextView havere_applyManagementPriceTv;  //金融方案(有批复)-档案管理费(申请信息)
    private TextView havere_replyManagementPriceTv;  //金融方案(有批复)-档案管理费(批复信息)
    private TextView havere_applyOtherPriceTv;       //金融方案(有批复)-其他费用(申请信息)
    private TextView havere_replyOtherPriceTv;       //金融方案(有批复)-其他费用(批复信息)
    private TextView havere_applyTotalPriceTv;       //金融方案(有批复)-总贷款额(申请信息)
    private TextView havere_replyTotalPriceTv;       //金融方案(有批复)-总贷款额(批复信息)
    private TextView havere_applyBankTv;             //金融方案(有批复)-贷款银行(申请信息)
    private TextView havere_replyBankTv;             //金融方案(有批复)-贷款银行(批复信息)
    private TextView havere_applyRepayDateTv;        //金融方案(有批复)-还款期限(申请信息)
    private TextView havere_replyRepayDateTv;        //金融方案(有批复)-还款期限(批复信息)
    private TextView havere_applyProductTypeTv;      //金融方案(有批复)-产品类型(申请信息)
    private TextView havere_replyProductTypeTv;      //金融方案(有批复)-产品类型(批复信息)
    private TextView havere_applyMonthPriceTv;       //金融方案(有批复)-月供(申请信息)
    private TextView havere_replyMonthPriceTv;       //金融方案(有批复)-月供(批复信息)
    private TextView havere_applyBuinessPriceTv;      //金融方案(有批复)-二手车交易价(申请信息)
    private TextView havere_replyBuinessPriceTv;      //金融方案(有批复)-二手车交易价(批复信息)


    private TextView salesNameTv;               //报单人员姓名
    private TextView customerIdTv;              //客户身份证号
    private TextView customerNameTv;            //客户姓名

    private TextView dlrNameTv;                 //订单-经销商
    private TextView brandTv;                   //订单-品牌
    private TextView trixTv;                    //订单-车系
    private TextView modelTv;                   //订单-车型
    private TextView colorTv;                   //订单-颜色
    private TextView condTv;                    //订单-业务类型
    private TextView guidePriceTv;              //订单-厂商指导价
    private TextView billPriceTv;               //订单-车辆开票价
    private TextView loanAmtTv;                 //订单-车辆贷款额
    private TextView downPaymentTv;             //订单-车辆首付款
    private TextView managementFeeTv;           //订单-档案管理费
    private TextView otherFeeTv;                //订单-其他费用
    private TextView totalLoanAmtTv;            //订单-总贷款额
    private TextView loanBankTv;                //订单-贷款银行
    private TextView productTypeTv;             //订单-产品类型
    private TextView nperTv;                    //订单-还款期限
    private TextView regAddrTv;                 //订单-上牌地
    private LinearLayout orderInfoTitleLin;     //订单头布局
    private TextView OldcarAddrTV;    //订单 -二手车上牌地
    private TextView OldcartimeTv;   //二手车上牌时间
    private TextView OldcardistancevTv;
    private TextView OldcarGuesspriceTv;
    private TextView OldcarbusnesspriceTv;//二手车交易价


    private TextView beforeDlrNameTv;          //订单-经销商(修改前)
    private TextView beforeBrandTv;            //订单-品牌(修改前)
    private TextView beforeTrixTv;             //订单-车系(修改前)
    private TextView beforeModelTv;            //订单-车型(修改前)
    private TextView beforeColorTv;            //订单-颜色(修改前)
    private TextView beforeCondTv;             //订单-业务类型(修改前)
    private TextView beforeGuidePriceTv;       //订单-厂商指导价(修改前)
    private TextView beforeBillPriceTv;        //订单-车辆开票价(修改前)
    private TextView beforeLoanAmtTv;          //订单-车辆贷款额(修改前)
    private TextView beforeDownPaymentTv;      //订单-车辆首付款(修改前)
    private TextView beforeManagementFeeTv;    //订单-档案管理费(修改前)
    private TextView beforeOtherFeeTv;         //订单-其他费用(修改前)
    private TextView beforeTotalLoanAmtTv;     //订单-总贷款额(修改前)
    private TextView beforeLoanBankTv;         //订单-贷款银行(修改前)
    private TextView beforeProductTypeTv;      //订单-产品类型(修改前)
    private TextView beforeNperTv;             //订单-还款期限(修改前)
    private TextView beforeRegAddrTv;          //订单-上牌地(修改前)
    private TextView beforeOldcarAddrTV;    //订单 -二手车上牌地
    private TextView beforeOldcartimeTv;    //二手车上牌时间
    private TextView beforedistancevTv;    //二手车里程数
    private TextView beforeguesspriceTv;  //二手车评估价
    private TextView beforebusnesspriceTv; //二手车交易价


    private TextView waitReason;        //审核中备注
    private TextView wait_title;        //审核中标题
    private TextView passReason;        //通过备注
    private TextView pass_title;        //通过标题
    private TextView reject_title;      //拒绝标题
    private TextView rejectReason;      //拒绝备注
    private TextView cancelReason;      //取消备注
    private TextView cancel_title;      //取消标题


    private LinearLayout order_detail_sign_layout;              //一个按钮的layout
    @BindView(id = R.id.order_detail_sign, widgetName = "order_detail_sign", onClick = "submitMaterial")
    private Button orderDetailSignBtn;                          //提交资料(一个按钮)

    private LinearLayout order_detail_change_layout;            //两个按钮的layout
    private TextView orderDetailChangeBtn;                      //修改资料(两个按钮)
    private TextView orderDetailUploadBtn;                      //提交资料(两个按钮)

    private FloatingActionButton fab;
    private NestedScrollView mScrollView;
    private String app_id;
    private int status_st;
    private String cartype;

    private LinearLayout oldcar_business_price_lin;//无批复车辆交易价lin
    private LinearLayout order_detail_finance_bill_price_lin;//无批复车辆开票价lin
    private LinearLayout havere_applyBillPrice_lin;//批复 车辆开票价lin
    private LinearLayout havere_oldcar_applyBillPrice_lin;// 批复交易价
    private LinearLayout newcar_zhidaoAnd_billPrice_lin;//新车的指导价和开票价
    private LinearLayout oldcar_info_lin;//二手车的信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_detail);
        UBT.bind(this);
        app_id = getIntent().getStringExtra("app_id");
        status_st = getIntent().getIntExtra("status_st", 0);
        //cartype = getIntent().getStringExtra("car_type");
        initView();
        // initTitleBar(this, "申请详情");
        //showNeworOldcarinfolayout(cartype);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initView() {
        OldcarbusnesspriceTv = (TextView) findViewById(R.id.order_detail_busnessprice_tv);
        beforebusnesspriceTv = (TextView) findViewById(R.id.oldcar_before_busnessprice_tv);
        beforeguesspriceTv = (TextView) findViewById(R.id.oldcar_before_guessprice_tv);
        OldcarGuesspriceTv = (TextView) findViewById(R.id.order_detail_guessprice_tv);
        beforedistancevTv = (TextView) findViewById(R.id.oldcar_before_distance_tv);
        OldcardistancevTv = (TextView) findViewById(R.id.order_detail_distance_tv);
        OldcartimeTv = (TextView) findViewById(R.id.order_detail_addrtime_tv);
        beforeOldcartimeTv = (TextView) findViewById(R.id.oldcar_before_addrtime_tv);
        beforeOldcarAddrTV = (TextView) findViewById(R.id.oldcar_before_addr_tv);
        OldcarAddrTV = (TextView) findViewById(R.id.order_detail_addr_tv);
        havere_applyBuinessPriceTv = (TextView) findViewById(R.id.order_detail_havere_oldcar_apply_bill_price_tv);
        havere_replyBuinessPriceTv = (TextView) findViewById(R.id.order_detail_havere_oldcar_reply_bill_price_tv);
        oldcar_business_price_lin = (LinearLayout) findViewById(R.id.oldcar_business_price_lin);
        order_detail_finance_bill_price_lin = (LinearLayout) findViewById(R.id.order_detail_finance_bill_price_lin);
        havere_applyBillPrice_lin = (LinearLayout) findViewById(R.id.havere_applyBillPrice_lin);
        havere_oldcar_applyBillPrice_lin = (LinearLayout) findViewById(R.id.havere_oldcar_applyBillPrice_lin);
        newcar_zhidaoAnd_billPrice_lin = (LinearLayout) findViewById(R.id.newcar_zhidaoAnd_billPrice_lin);
        oldcar_info_lin = (LinearLayout) findViewById(R.id.oldcar_info_lin);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        mScrollView = (NestedScrollView) findViewById(R.id.scrollView_four);
        fab.setOnClickListener(v -> mScrollView.smoothScrollTo(0, 0));
        waitLin = (LinearLayout) findViewById(R.id.order_detail_status_wait_layout);
        cancelRel = (RelativeLayout) findViewById(R.id.order_detail_status_cancel_layout);
        passRel = (RelativeLayout) findViewById(R.id.order_detail_status_pass_layout);
        rejectRel = (RelativeLayout) findViewById(R.id.order_detail_status_reject_layout);
        nore_financeLin = (LinearLayout) findViewById(R.id.order_detail_finance_lin);

        waitReason = (TextView) findViewById(R.id.order_detail_status_wait_reason);
        wait_title = (TextView) findViewById(R.id.order_detail_status_wait);
        pass_title = (TextView) findViewById(R.id.order_detail_status_pass);
        reject_title = (TextView) findViewById(R.id.order_detail_status_reject);
        cancel_title = (TextView) findViewById(R.id.order_detail_status_cancel);

        cancelReason = (TextView) findViewById(R.id.order_detail_status_cancel_reason);
        passReason = (TextView) findViewById(R.id.order_detail_status_pass_reason);
        rejectReason = (TextView) findViewById(R.id.order_detail_status_reject_reason);

        applyBillPriceTv = (TextView) findViewById(R.id.order_detail_finance_bill_price_tv);
        applyFirstPriceTv = (TextView) findViewById(R.id.order_detail_finance_first_price_tv);
        applyLoanPriceTv = (TextView) findViewById(R.id.order_detail_finance_loan_price_tv);
        applyManagementPriceTv = (TextView) findViewById(R.id.order_detail_finance_management_price_tv);
        applyOtherPriceTv = (TextView) findViewById(R.id.order_detail_finance_other_price_tv);
        applyTotalLoanPriceTv = (TextView) findViewById(R.id.order_detail_finance_total_loan_price_tv);
        applyLoanBankTv = (TextView) findViewById(R.id.order_detail_finance_loan_bank_tv);
        applyProductTypeTv = (TextView) findViewById(R.id.order_detail_finance_product_type_tv);
        applyPeriodsTv = (TextView) findViewById(R.id.order_detail_finance_periods_tv);
        applyBusinessTv = (TextView) findViewById(R.id.oldcar_business_price_tv);


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


        havere_applyFirstPercentTv = (TextView) findViewById(R.id.order_detail_havere_apply_first_percent_tv);
        havere_replyFirstPercentTv = (TextView) findViewById(R.id.order_detail_havere_reply_first_percent_tv);
        havere_applyBillPriceTv = (TextView) findViewById(R.id.order_detail_havere_apply_bill_price_tv);
        havere_replyBillPriceTv = (TextView) findViewById(R.id.order_detail_havere_reply_bill_price_tv);

        havere_applyFirstPriceTv = (TextView) findViewById(R.id.order_detail_havere_apply_first_price_tv);
        havere_replyFirstPriceTv = (TextView) findViewById(R.id.order_detail_havere_reply_first_price_tv);

        havere_applyLoanPriceTv = (TextView) findViewById(R.id.order_detail_havere_apply_loan_price_tv);
        havere_replyLoanPriceTv = (TextView) findViewById(R.id.order_detail_havere_reply_loan_price_tv);

        havere_applyManagementPriceTv = (TextView) findViewById(R.id.order_detail_havere_apply_management_price_tv);
        havere_replyManagementPriceTv = (TextView) findViewById(R.id.order_detail_havere_reply_management_price_tv);

        havere_applyOtherPriceTv = (TextView) findViewById(R.id.order_detail_havere_apply_other_price_tv);
        havere_replyOtherPriceTv = (TextView) findViewById(R.id.order_detail_havere_reply_other_price_tv);

        havere_applyBankTv = (TextView) findViewById(R.id.order_detail_havere_apply_bank_tv);
        havere_replyBankTv = (TextView) findViewById(R.id.order_detail_havere_reply_bank_tv);

        havere_applyRepayDateTv = (TextView) findViewById(R.id.order_detail_havere_apply_repay_date_tv);
        havere_replyRepayDateTv = (TextView) findViewById(R.id.order_detail_havere_reply_repay_data_tv);

        havere_applyTotalPriceTv = (TextView) findViewById(R.id.order_detail_havere_apply_total_price_tv);
        havere_replyTotalPriceTv = (TextView) findViewById(R.id.order_detail_havere_reply_total_price_tv);

        havere_applyProductTypeTv = (TextView) findViewById(R.id.order_detail_havere_apply_product_type_tv);
        havere_replyProductTypeTv = (TextView) findViewById(R.id.order_detail_havere_reply_product_type_tv);

        havere_applyMonthPriceTv = (TextView) findViewById(R.id.order_detail_havere_apply_month_price_tv);
        havere_replyMonthPriceTv = (TextView) findViewById(R.id.order_detail_havere_reply_month_price_tv);

        order_detail_sign_layout = (LinearLayout) findViewById(R.id.order_detail_sign_layout);
        order_detail_change_layout = (LinearLayout) findViewById(R.id.order_detail_change_layout);

        orderDetailSignBtn = (Button) findViewById(R.id.order_detail_sign);
        orderDetailChangeBtn = (TextView) findViewById(R.id.order_detail_change_tv);
        orderDetailUploadBtn = (TextView) findViewById(R.id.order_detail_upload_tv);

        havere_financeLin = (LinearLayout) findViewById(R.id.order_detail_havere_lin);


        orderDetailChangeBtn.setOnClickListener(v -> {
            Intent intent = new Intent(OrderDetailActivity.this, AlterCarInfoActivity.class);
            intent.putExtra("app_id", app_id);
            startActivity(intent);
        });
        orderDetailUploadBtn.setOnClickListener(v -> {
            Intent intent = new Intent(OrderDetailActivity.this, SubmitInformationActivity.class);
            intent.putExtra("app_id", app_id);
            startActivity(intent);
        });

    }

    private void initData() {
        OrderApi.getAppDetails(this, app_id, resp -> {
            if (resp == null) {
                return;
            }
            cartype = resp.vehicle_cond;
            if (cartype.equals("新车")) {
                initTitleBar(this, "新车申请详情");
            } else {
                initTitleBar(this, "二手车申请详情");
            }

            showNeworOldcarinfolayout(cartype);
            if (resp.modify_permission) {
                order_detail_sign_layout.setVisibility(View.GONE);
                order_detail_change_layout.setVisibility(View.VISIBLE);
            } else {
                order_detail_sign_layout.setVisibility(View.VISIBLE);
                order_detail_change_layout.setVisibility(View.GONE);

            }
            if (resp.status_st == 2) {//待审核
                waitLin.setVisibility(View.VISIBLE);
                passRel.setVisibility(View.GONE);
                rejectRel.setVisibility(View.GONE);
                nore_financeLin.setVisibility(View.VISIBLE);
                if (resp.uw) {
                    waitReason.setText(resp.uw_detail.comments);
                }
                wait_title.setText(resp.client_status_code);
            } else if (resp.status_st == 4) {//待确认金融方案 //有批复的
                passRel.setVisibility(View.VISIBLE);
                waitLin.setVisibility(View.GONE);
                rejectRel.setVisibility(View.GONE);
                nore_financeLin.setVisibility(View.GONE);
                havere_financeLin.setVisibility(View.GONE);
                pass_title.setText(resp.client_status_code);
            } else if (resp.status_st == 6) {//放款中      //有批复的
                passRel.setVisibility(View.VISIBLE);
                waitLin.setVisibility(View.GONE);
                rejectRel.setVisibility(View.GONE);
                nore_financeLin.setVisibility(View.GONE);
                havere_financeLin.setVisibility(View.VISIBLE);
                if (resp.uw) {
                    passReason.setText(resp.uw_detail.comments);
                }
                pass_title.setText(resp.client_status_code);
            } else if (resp.status_st == 3) {//审核失败
                waitLin.setVisibility(View.GONE);
                passRel.setVisibility(View.GONE);
                rejectRel.setVisibility(View.VISIBLE);
                if (resp.uw) {
                    rejectReason.setText(resp.uw_detail.comments);
                }
                reject_title.setText(resp.client_status_code);
            }
            //金融方案申请和批复
            if (resp.uw && resp.uw_detail != null) {
                havere_financeLin.setVisibility(View.VISIBLE);
                nore_financeLin.setVisibility(View.GONE);

                havere_replyBillPriceTv.setText(resp.uw_detail.vehicle_price);
                havere_replyFirstPriceTv.setText(resp.uw_detail.vehicle_down_payment);
                havere_replyLoanPriceTv.setText(resp.uw_detail.vehicle_loan_amt);
                havere_replyManagementPriceTv.setText(resp.uw_detail.management_fee);
                havere_replyOtherPriceTv.setText(resp.uw_detail.other_fee);
                havere_replyTotalPriceTv.setText(resp.uw_detail.loan_amt);
                havere_replyBankTv.setText(resp.uw_detail.loan_bank);
                havere_replyRepayDateTv.setText(resp.uw_detail.nper + "期");
                havere_replyFirstPercentTv.setText(resp.uw_detail.vehicle_down_payment_percent * 100 + "%");
                havere_replyProductTypeTv.setText(resp.uw_detail.product_type);
                havere_replyMonthPriceTv.setText(resp.uw_detail.monthly_payment);
                havere_replyBuinessPriceTv.setText(resp.uw_detail.vehicle_price);

                havere_applyBillPriceTv.setText(resp.vehicle_price);
                havere_applyFirstPriceTv.setText(resp.vehicle_down_payment);
                havere_applyLoanPriceTv.setText(resp.vehicle_loan_amt);
                havere_applyManagementPriceTv.setText(resp.management_fee);
                havere_applyOtherPriceTv.setText(resp.other_fee);
                havere_applyTotalPriceTv.setText(resp.loan_amt);
                havere_applyBankTv.setText(resp.loan_bank);
                havere_applyRepayDateTv.setText(resp.nper + "期");
                havere_applyFirstPercentTv.setText(resp.vehicle_down_payment_percent * 100 + "%");
                havere_applyProductTypeTv.setText(resp.product_type);
                havere_applyMonthPriceTv.setText(resp.monthly_payment);
                havere_applyBuinessPriceTv.setText(resp.vehicle_price);

                compare(havere_applyBuinessPriceTv, havere_replyBuinessPriceTv);
                compare(havere_applyMonthPriceTv, havere_replyMonthPriceTv);
                compare(havere_applyFirstPercentTv, havere_replyFirstPercentTv);
                compare(havere_applyBillPriceTv, havere_replyBillPriceTv);
                compare(havere_applyFirstPriceTv, havere_replyFirstPriceTv);
                compare(havere_applyLoanPriceTv, havere_replyLoanPriceTv);
                compare(havere_applyManagementPriceTv, havere_replyManagementPriceTv);
                compare(havere_applyOtherPriceTv, havere_replyOtherPriceTv);
                compare(havere_applyTotalPriceTv, havere_replyTotalPriceTv);
                compare(havere_applyBankTv, havere_replyBankTv);
                compare(havere_applyRepayDateTv, havere_replyRepayDateTv);
                compare(havere_applyProductTypeTv, havere_replyProductTypeTv);
            } else {
                havere_financeLin.setVisibility(View.GONE);
                nore_financeLin.setVisibility(View.VISIBLE);
                applyBillPriceTv.setText(resp.vehicle_price);
                applyFirstPriceTv.setText(resp.vehicle_down_payment);
                applyLoanPriceTv.setText(resp.vehicle_loan_amt);
                applyManagementPriceTv.setText(resp.management_fee);
                applyOtherPriceTv.setText(resp.other_fee);
                applyTotalLoanPriceTv.setText(resp.loan_amt);
                applyLoanBankTv.setText(resp.loan_bank);
                applyProductTypeTv.setText(resp.product_type);
                applyPeriodsTv.setText(resp.nper);
                applyBusinessTv.setText(resp.vehicle_price);
            }


            salesNameTv.setText(resp.dlr_sales_name);
            customerIdTv.setText(resp.id_no);
            customerNameTv.setText(resp.clt_nm);
            // 车辆原信息和修改信息
            if (resp.is_modify && resp.old_app != null) {
                orderInfoTitleLin.setVisibility(View.VISIBLE);
                beforeDlrNameTv.setText(resp.old_app.dlr_nm+resp.aid_dlr_nm);
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
                beforebusnesspriceTv.setText(resp.old_app.vehicle_price);
                beforeguesspriceTv.setText(resp.old_app.send_hand_valuation);
                beforedistancevTv.setText(resp.old_app.send_hand_mileage);
                beforeOldcartimeTv.setText(resp.old_app.send_hand_plate_time);
                beforeOldcarAddrTV.setText(resp.old_app.origin_plate_reg_addr);


//                OldcarbusnesspriceTv.setText(resp.vehicle_price);
//                OldcarGuesspriceTv.setText(resp.send_hand_valuation);
//                OldcardistancevTv.setText(resp.send_hand_mileage);
//                OldcartimeTv.setText(resp.send_hand_plate_time);
//                OldcarAddrTV.setText(resp.origin_plate_reg_addr);
                dlrNameTv.setText(resp.new_app.dlr_nm+resp.aid_dlr_nm);
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
                OldcarbusnesspriceTv.setText(resp.new_app.vehicle_price);
                OldcarGuesspriceTv.setText(resp.new_app.send_hand_valuation);
                OldcardistancevTv.setText(resp.new_app.send_hand_mileage);
                OldcartimeTv.setText(resp.new_app.send_hand_plate_time);
                OldcarAddrTV.setText(resp.new_app.origin_plate_reg_addr);

                compare(beforebusnesspriceTv, OldcarbusnesspriceTv);
                compare(beforeguesspriceTv, OldcarGuesspriceTv);
                compare(beforedistancevTv, OldcardistancevTv);
                compare(beforeOldcartimeTv, OldcartimeTv);
                compare(beforeOldcarAddrTV, OldcarAddrTV);

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
                //车辆原订单信息
                dlrNameTv.setText(resp.dlr_nm+resp.aid_dlr_nm);
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
                OldcarbusnesspriceTv.setText(resp.vehicle_price);
                OldcarGuesspriceTv.setText(resp.send_hand_valuation);
                OldcardistancevTv.setText(resp.send_hand_mileage);
                OldcartimeTv.setText(resp.send_hand_plate_time);
                OldcarAddrTV.setText(resp.origin_plate_reg_addr);

            }

            findViewById(R.id.order_detail_customer_mobile_img).setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + resp.mobile));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            });
            findViewById(R.id.order_detail_sales_mobile_img).setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + resp.dlr_sales_mobile));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            });

            orderDetailChangeBtn.setOnClickListener(v -> {
                if (cartype.equals("二手车")) {
                    Intent i1 = new Intent(OrderDetailActivity.this, AlterOldCarInfoActivity.class);
                    i1.putExtra("car_type", cartype);
                    i1.putExtra("app_id", app_id);
                    startActivity(i1);
                } else {
                    Intent i1 = new Intent(OrderDetailActivity.this, AlterCarInfoActivity.class);
                    i1.putExtra("car_type", cartype);
                    i1.putExtra("app_id", app_id);
                    startActivity(i1);
                }
            });
            orderDetailUploadBtn.setOnClickListener(v -> {
                Intent i2 = new Intent(OrderDetailActivity.this, SubmitInformationActivity.class);
                i2.putExtra("car_type", cartype);
                i2.putExtra("app_id", app_id);
                startActivity(i2);
            });

        });
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


    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //    private LinearLayout oldcar_business_price_lin;//无批复车辆交易价lin
//    private LinearLayout order_detail_finance_bill_price_lin;//无批复车辆开票价lin
//    private LinearLayout havere_applyBillPrice_lin;//批复 车辆开票价lin
//    private LinearLayout havere_oldcar_applyBillPrice_lin;// 批复交易价
//    private LinearLayout newcar_zhidaoAnd_billPrice_lin;//新车的指导价和开票价
//    private LinearLayout oldcar_info_lin;//二手车的信息
    private void showNeworOldcarinfolayout(String cartype) {
        if (cartype.equals("二手车")) {
            oldcar_business_price_lin.setVisibility(View.VISIBLE);
            havere_oldcar_applyBillPrice_lin.setVisibility(View.VISIBLE);
            oldcar_info_lin.setVisibility(View.VISIBLE);
            order_detail_finance_bill_price_lin.setVisibility(View.GONE);
            havere_applyBillPrice_lin.setVisibility(View.GONE);
            newcar_zhidaoAnd_billPrice_lin.setVisibility(View.GONE);
        } else {
            oldcar_business_price_lin.setVisibility(View.GONE);
            havere_oldcar_applyBillPrice_lin.setVisibility(View.GONE);
            oldcar_info_lin.setVisibility(View.GONE);
            order_detail_finance_bill_price_lin.setVisibility(View.VISIBLE);
            havere_applyBillPrice_lin.setVisibility(View.VISIBLE);
            newcar_zhidaoAnd_billPrice_lin.setVisibility(View.VISIBLE);
        }
    }
}
