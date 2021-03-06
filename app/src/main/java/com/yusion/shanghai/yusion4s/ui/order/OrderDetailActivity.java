package com.yusion.shanghai.yusion4s.ui.order;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.bean.auth.ReplaceSPReq;
import com.yusion.shanghai.yusion4s.bean.order.submit.ReSubmitReq;
import com.yusion.shanghai.yusion4s.retrofit.Api;
import com.yusion.shanghai.yusion4s.ubt.UBT;
import com.yusion.shanghai.yusion4s.ubt.annotate.BindView;
import com.yusion.shanghai.yusion4s.ui.MainActivity;
import com.yusion.shanghai.yusion4s.ui.entrance.apply_financing.AlterCarInfoActivity;
import com.yusion.shanghai.yusion4s.ui.entrance.apply_financing.AlterOldCarInfoActivity;
import com.yusion.shanghai.yusion4s.ui.upload.SubmitMaterialActivity;
import com.yusion.shanghai.yusion4s.utils.ApiUtil;
import com.yusion.shanghai.yusion4s.utils.PopupDialogUtil;
import com.yusion.shanghai.yusion4s.utils.ToastUtil;

import static com.yusion.shanghai.yusion4s.R.id.order_detail_check_tv;
import static com.yusion.shanghai.yusion4s.R.id.order_detail_replace_tv;


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


    private LinearLayout onebtn_layout;              //一个按钮的layout
    @BindView(id = R.id.order_detail_sign, widgetName = "order_detail_sign", onClick = "submitMaterial")
    private Button orderDetailSignBtn;                          //提交资料(一个按钮)

    private LinearLayout twobtn_layout;            //两个按钮的layout
    private TextView orderDetailChangeBtn;                      //修改资料(两个按钮)
    private TextView orderDetailUploadBtn;                      //提交资料(两个按钮)


    private LinearLayout order_detail_replace_layout;            //更换配偶作为主贷人layout
    private TextView orderDetailReplaceBtn;                     //更换配偶作为主贷人
    private TextView orderDetailCheckBtn;                     //更换配偶作为主贷人

    private FloatingActionButton fab;
    private NestedScrollView mScrollView;
    private String app_id;
    private String come_from;
    private String spouse_clt_id;
    private int status_st;
    private String cartype;

    private LinearLayout oldcar_business_price_lin;//无批复车辆交易价lin
    private LinearLayout order_detail_finance_bill_price_lin;//无批复车辆开票价lin
    private LinearLayout havere_applyBillPrice_lin;//批复 车辆开票价lin
    private LinearLayout havere_oldcar_applyBillPrice_lin;// 批复交易价
    private LinearLayout newcar_zhidaoAnd_billPrice_lin;//新车的指导价和开票价
    private LinearLayout oldcar_info_lin;//二手车的信息


    private LinearLayout title_lin;
    private ImageView title_img;
    private TextView title_tv;
    private TextView remark_tv1;
    private TextView remark_tv2;

    private String comments;   // 备注
    private LinearLayout order_detail_schedule_lin;     //订单进度

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_detail);
        UBT.bind(this);
        come_from = getIntent().getStringExtra("come_from");
        app_id = getIntent().getStringExtra("app_id");
        status_st = getIntent().getIntExtra("status_st", 0);
//        spouse_clt_id = getIntent().getStringExtra("spouse_clt_id");
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
        title_lin = findViewById(R.id.title_lin);
        title_img = findViewById(R.id.title_img);
        title_tv = findViewById(R.id.title_tv);
        remark_tv1 = findViewById(R.id.remark_tv1);
        remark_tv2 = findViewById(R.id.remark_tv2);
        order_detail_schedule_lin = findViewById(R.id.order_detail_schedule_lin);
        OldcarbusnesspriceTv = findViewById(R.id.order_detail_busnessprice_tv);
        beforebusnesspriceTv = findViewById(R.id.oldcar_before_busnessprice_tv);
        beforeguesspriceTv = findViewById(R.id.oldcar_before_guessprice_tv);
        OldcarGuesspriceTv = findViewById(R.id.order_detail_guessprice_tv);
        beforedistancevTv = findViewById(R.id.oldcar_before_distance_tv);
        OldcardistancevTv = findViewById(R.id.order_detail_distance_tv);
        OldcartimeTv = findViewById(R.id.order_detail_addrtime_tv);
        beforeOldcartimeTv = findViewById(R.id.oldcar_before_addrtime_tv);
        beforeOldcarAddrTV = findViewById(R.id.oldcar_before_addr_tv);
        OldcarAddrTV = findViewById(R.id.order_detail_addr_tv);
        havere_applyBuinessPriceTv = findViewById(R.id.order_detail_havere_oldcar_apply_bill_price_tv);
        havere_replyBuinessPriceTv = findViewById(R.id.order_detail_havere_oldcar_reply_bill_price_tv);
        oldcar_business_price_lin = findViewById(R.id.oldcar_business_price_lin);
        order_detail_finance_bill_price_lin = findViewById(R.id.order_detail_finance_bill_price_lin);
        havere_applyBillPrice_lin = findViewById(R.id.havere_applyBillPrice_lin);
        havere_oldcar_applyBillPrice_lin = findViewById(R.id.havere_oldcar_applyBillPrice_lin);
        newcar_zhidaoAnd_billPrice_lin = findViewById(R.id.newcar_zhidaoAnd_billPrice_lin);
        oldcar_info_lin = findViewById(R.id.oldcar_info_lin);

        fab = findViewById(R.id.fab);
        mScrollView = findViewById(R.id.scrollView_four);
        fab.setOnClickListener(v -> mScrollView.smoothScrollTo(0, 0));
//        waitLin = findViewById(R.id.order_detail_status_wait_layout);
//        cancelRel = findViewById(R.id.order_detail_status_cancel_layout);
//        passRel = findViewById(R.id.order_detail_status_pass_layout);
//        rejectRel = findViewById(R.id.order_detail_status_reject_layout);
        nore_financeLin = findViewById(R.id.order_detail_finance_lin);

//        waitReason = findViewById(R.id.order_detail_status_wait_reason);
//        wait_title = findViewById(R.id.order_detail_status_wait);
//        pass_title = findViewById(R.id.order_detail_status_pass);
//        reject_title = findViewById(R.id.order_detail_status_reject);
//        cancel_title = findViewById(R.id.order_detail_status_cancel);
//
//        cancelReason = findViewById(R.id.order_detail_status_cancel_reason);
//        passReason = findViewById(R.id.order_detail_status_pass_reason);
//        rejectReason = findViewById(R.id.order_detail_status_reject_reason);

        applyBillPriceTv = findViewById(R.id.order_detail_finance_bill_price_tv);
        applyFirstPriceTv = findViewById(R.id.order_detail_finance_first_price_tv);
        applyLoanPriceTv = findViewById(R.id.order_detail_finance_loan_price_tv);
        applyManagementPriceTv = findViewById(R.id.order_detail_finance_management_price_tv);
        applyOtherPriceTv = findViewById(R.id.order_detail_finance_other_price_tv);
        applyTotalLoanPriceTv = findViewById(R.id.order_detail_finance_total_loan_price_tv);
        applyLoanBankTv = findViewById(R.id.order_detail_finance_loan_bank_tv);
        applyProductTypeTv = findViewById(R.id.order_detail_finance_product_type_tv);
        applyPeriodsTv = findViewById(R.id.order_detail_finance_periods_tv);
        applyBusinessTv = findViewById(R.id.oldcar_business_price_tv);


        orderInfoTitleLin = findViewById(R.id.order_detail_order_info_title);
        dlrNameTv = findViewById(R.id.order_detail_dlr_name_tv);
        beforeDlrNameTv = findViewById(R.id.order_detail_before_dlr_name_tv);
        brandTv = findViewById(R.id.order_detail_brand_tv);
        beforeBrandTv = findViewById(R.id.order_detail_before_brand_tv);
        trixTv = findViewById(R.id.order_detail_trix_tv);
        beforeTrixTv = findViewById(R.id.order_detail_before_trix_tv);
        modelTv = findViewById(R.id.order_detail_model_tv);
        beforeModelTv = findViewById(R.id.order_detail_before_model_tv);
        colorTv = findViewById(R.id.order_detail_color_tv);
        beforeColorTv = findViewById(R.id.order_detail_before_color_tv);
        condTv = findViewById(R.id.order_detail_vehicle_cond_tv);
        beforeCondTv = findViewById(R.id.order_detail_before_vehicle_cond_tv);
        guidePriceTv = findViewById(R.id.order_detail_guide_price_tv);
        beforeGuidePriceTv = findViewById(R.id.order_detail_before_guide_price_tv);
        billPriceTv = findViewById(R.id.order_detail_vehicle_price_tv);
        beforeBillPriceTv = findViewById(R.id.order_detail_before_vehicle_price_tv);
        //贷款额
        loanAmtTv = findViewById(R.id.order_detail_vehicle_loan_amt_tv);
        beforeLoanAmtTv = findViewById(R.id.order_detail_before_vehicle_loan_amt_tv);
        downPaymentTv = findViewById(R.id.order_detail_vehicle_down_payment_tv);
        beforeDownPaymentTv = findViewById(R.id.order_detail_before_vehicle_down_payment_tv);
        managementFeeTv = findViewById(R.id.order_detail_management_fee_tv);
        beforeManagementFeeTv = findViewById(R.id.order_detail_before_management_fee_tv);
        otherFeeTv = findViewById(R.id.order_detail_other_fee_tv);
        beforeOtherFeeTv = findViewById(R.id.order_detail_before_other_fee_tv);
        //总贷款额
        totalLoanAmtTv = findViewById(R.id.order_detail_loan_amt_tv);
        beforeTotalLoanAmtTv = findViewById(R.id.order_detail_before_loan_amt_tv);
        loanBankTv = findViewById(R.id.order_detail_loan_bank_tv);
        beforeLoanBankTv = findViewById(R.id.order_detail_before_loan_bank_tv);
        productTypeTv = findViewById(R.id.order_detail_product_type_tv);
        beforeProductTypeTv = findViewById(R.id.order_detail_before_product_type_tv);
        nperTv = findViewById(R.id.order_detail_nper_tv);
        beforeNperTv = findViewById(R.id.order_detail_before_nper_tv);
        regAddrTv = findViewById(R.id.order_detail_plate_reg_addr_tv);
        beforeRegAddrTv = findViewById(R.id.order_detail_before_plate_reg_addr_tv);


        salesNameTv = findViewById(R.id.order_detail_sales_name_tv);
        customerIdTv = findViewById(R.id.order_detail_customer_id_tv);
        customerNameTv = findViewById(R.id.order_detail_customer_name_tv);


        havere_applyFirstPercentTv = findViewById(R.id.order_detail_havere_apply_first_percent_tv);
        havere_replyFirstPercentTv = findViewById(R.id.order_detail_havere_reply_first_percent_tv);
        havere_applyBillPriceTv = findViewById(R.id.order_detail_havere_apply_bill_price_tv);
        havere_replyBillPriceTv = findViewById(R.id.order_detail_havere_reply_bill_price_tv);

        havere_applyFirstPriceTv = findViewById(R.id.order_detail_havere_apply_first_price_tv);
        havere_replyFirstPriceTv = findViewById(R.id.order_detail_havere_reply_first_price_tv);

        havere_applyLoanPriceTv = findViewById(R.id.order_detail_havere_apply_loan_price_tv);
        havere_replyLoanPriceTv = findViewById(R.id.order_detail_havere_reply_loan_price_tv);

        havere_applyManagementPriceTv = findViewById(R.id.order_detail_havere_apply_management_price_tv);
        havere_replyManagementPriceTv = findViewById(R.id.order_detail_havere_reply_management_price_tv);

        havere_applyOtherPriceTv = findViewById(R.id.order_detail_havere_apply_other_price_tv);
        havere_replyOtherPriceTv = findViewById(R.id.order_detail_havere_reply_other_price_tv);

        havere_applyBankTv = findViewById(R.id.order_detail_havere_apply_bank_tv);
        havere_replyBankTv = findViewById(R.id.order_detail_havere_reply_bank_tv);

        havere_applyRepayDateTv = findViewById(R.id.order_detail_havere_apply_repay_date_tv);
        havere_replyRepayDateTv = findViewById(R.id.order_detail_havere_reply_repay_data_tv);

        havere_applyTotalPriceTv = findViewById(R.id.order_detail_havere_apply_total_price_tv);
        havere_replyTotalPriceTv = findViewById(R.id.order_detail_havere_reply_total_price_tv);

        havere_applyProductTypeTv = findViewById(R.id.order_detail_havere_apply_product_type_tv);
        havere_replyProductTypeTv = findViewById(R.id.order_detail_havere_reply_product_type_tv);

        havere_applyMonthPriceTv = findViewById(R.id.order_detail_havere_apply_month_price_tv);
        havere_replyMonthPriceTv = findViewById(R.id.order_detail_havere_reply_month_price_tv);

        onebtn_layout = findViewById(R.id.order_detail_sign_layout);
        twobtn_layout = findViewById(R.id.order_detail_change_layout);
        order_detail_replace_layout = findViewById(R.id.order_detail_replace_layout);

        orderDetailSignBtn = findViewById(R.id.order_detail_sign);
        orderDetailChangeBtn = findViewById(R.id.order_detail_change_tv);
        orderDetailUploadBtn = findViewById(R.id.order_detail_upload_tv);
        orderDetailReplaceBtn = findViewById(order_detail_replace_tv);
        orderDetailCheckBtn = findViewById(order_detail_check_tv);

        havere_financeLin = findViewById(R.id.order_detail_havere_lin);

//
//        orderDetailChangeBtn.setOnClickListener(v -> {
//            Intent intent = new Intent(OrderDetailActivity.this, AlterCarInfoActivity.class);
//            intent.putExtra("app_id", app_id);
//            startActivity(intent);
//        });
//        orderDetailUploadBtn.setOnClickListener(v -> {
//            Intent intent = new Intent(OrderDetailActivity.this, SubmitMaterialActivity.class);
//            intent.putExtra("app_id", app_id);
//            startActivity(intent);
//        });


        order_detail_schedule_lin.setOnClickListener(v -> {
            Intent intent = new Intent(OrderDetailActivity.this, ProcessActivity.class);
            intent.putExtra("app_id", app_id);
            startActivity(intent);
        });


        orderDetailReplaceBtn.setOnClickListener(v -> {
            //   更换配偶作为主贷人 ： 接口一 激活配偶用户端登录 --> 接口二 更换配偶为主贷人  --> 接口三 获取新的订单详情
            PopupDialogUtil.showTwoButtonsDialog(OrderDetailActivity.this, R.layout.popup_dialog_two_hastitle_button, dialog -> {
                dialog.dismiss();
                checkAndReplace();

            });

        });

        orderDetailCheckBtn.setOnClickListener(v -> {
            Intent i2 = new Intent(OrderDetailActivity.this, SubmitMaterialActivity.class);
            i2.putExtra("car_type", cartype);
            i2.putExtra("app_id", app_id);
            startActivity(i2);
        });

    }

    private void checkAndReplace() {

        ReplaceSPReq replaceSPReq = new ReplaceSPReq();
        replaceSPReq.clt_id = spouse_clt_id;
        Log.e("TAG", "spouse_clt_id = " + spouse_clt_id);
        //1.激活配偶登录
        ApiUtil.requestUrl4Data(OrderDetailActivity.this, Api.getAuthService().replaceSpToP(replaceSPReq), data1 -> {
//        AuthApi.replaceSpToP(OrderDetailActivity.this, replaceSPReq, data1 -> {
            if (data1 == null) {
                return;
            }
            //2.检查配偶信息是否完善
            ApiUtil.requestUrl4Data(OrderDetailActivity.this, Api.getAuthService().CheckInfoComplete(spouse_clt_id), data -> {
//            AuthApi.CheckInfoComplete(OrderDetailActivity.this, spouse_clt_id, data -> {
                if (data == null) {
                    return;
                }
                //完善 - 提交成功
                if (data.info_completed) {
                    ReSubmitReq req = new ReSubmitReq();
                    req.clt_id = spouse_clt_id;
                    req.app_id = app_id;
                    //3：重新提报
                    ApiUtil.requestUrl4Data(OrderDetailActivity.this, Api.getOrderService().reSubmit(req), data2 -> {
//                        OrderApi.reSubmit(OrderDetailActivity.this, req, data2 -> {
                        if (data2 != null) {
                            ToastUtil.showImageToast(OrderDetailActivity.this, "提交成功", R.mipmap.toast_success);
                            app_id = data2.app_id;
                            Intent intent = new Intent(OrderDetailActivity.this, OrderDetailActivity.class);
                            intent.putExtra("app_id", app_id);
                            intent.putExtra("status_st", status_st);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
                //未完善
                else {
                    PopupDialogUtil.showOneButtonDialog(OrderDetailActivity.this, R.layout.popup_dialog_one_hastitle_button, dialog1 -> {

                        PackageManager packageManager = OrderDetailActivity.this.getPackageManager();  // 当前Activity获得packageManager对象
                        Intent intent = new Intent();
                        try {
                            intent = packageManager.getLaunchIntentForPackage("com.yusion.shanghai.yusion");
                        } catch (Exception e) {
                        }
                        if (intent != null) {
                            startActivity(intent);
                        }
                        dialog1.dismiss();
                    });
                }
            });
        });
    }

    private void initData() {
        ApiUtil.requestUrl4Data(this, Api.getOrderService().getAppDetails2(app_id), resp -> {
//        OrderApi.getAppDetails(this, app_id, resp -> {
            if (resp == null) {
                return;
            }
            spouse_clt_id = resp.origin_app.spouse_clt_id;
            cartype = resp.vehicle_cond;
            comments = resp.comments;
            if (cartype.equals("新车")) {
                initTitleBar(this, "新车申请详情");
            } else {
                initTitleBar(this, "二手车申请详情");
            }

            showNeworOldcarinfolayout(cartype);


            if (resp.is_view) {
                if (resp.can_switch_sp) {
                    onebtn_layout.setVisibility(View.GONE);
                    order_detail_replace_layout.setVisibility(View.VISIBLE);
                    twobtn_layout.setVisibility(View.GONE);
                } else {
                    onebtn_layout.setVisibility(View.VISIBLE);
                    order_detail_replace_layout.setVisibility(View.GONE);
                    twobtn_layout.setVisibility(View.GONE);
                }
            } else {
                onebtn_layout.setVisibility(View.GONE);
                order_detail_replace_layout.setVisibility(View.GONE);
                twobtn_layout.setVisibility(View.VISIBLE);
                if (resp.modify_permission) {
                    orderDetailChangeBtn.setVisibility(View.VISIBLE);
                } else {
                    orderDetailChangeBtn.setVisibility(View.GONE);
                }
            }

//            if (resp.status_st == 9 || resp.status_st == 11){
//                order_detail_replace_layout.setVisibility(View.VISIBLE);
//                if (spouse_clt_id != null) {
//                    orderDetailReplaceBtn.setVisibility(View.VISIBLE);
//                    twobtn_layout.setVisibility(View.GONE);
//                    onebtn_layout.setVisibility(View.GONE);
//                }else {
//                    orderDetailReplaceBtn.setVisibility(View.GONE);
//                }
//            }else {
//                if (resp.modify_permission) {
//                    order_detail_replace_layout.setVisibility(View.GONE);
//                    onebtn_layout.setVisibility(View.GONE);
//                    twobtn_layout.setVisibility(View.VISIBLE);
//                } else {
//                    order_detail_replace_layout.setVisibility(View.GONE);
//                    onebtn_layout.setVisibility(View.VISIBLE);
//                    twobtn_layout.setVisibility(View.GONE);
//                }
//            }


            if (resp.status_st == 2) {//待审核
                title_lin.setBackgroundResource(R.mipmap.order_st_back_lin1);
                title_img.setBackgroundResource(R.mipmap.order_st_back_img1);
                title_tv.setText("进行中");
                remark_tv2.setText(comments);
                title_tv.setTextColor(Color.parseColor("#FFFFFFFF"));
                remark_tv1.setTextColor(Color.parseColor("#FFFFFFFF"));
                remark_tv2.setTextColor(Color.parseColor("#FFFFFFFF"));
            } else if (resp.status_st == 4) {//待确认金融方案 //有批复的
                title_lin.setBackgroundResource(R.mipmap.order_st_back_lin1);
                title_img.setBackgroundResource(R.mipmap.order_st_back_img1);
                title_tv.setText("进行中");
                remark_tv2.setText(comments);
                title_tv.setTextColor(Color.parseColor("#FFFFFFFF"));
                remark_tv1.setTextColor(Color.parseColor("#FFFFFFFF"));
                remark_tv2.setTextColor(Color.parseColor("#FFFFFFFF"));
//
            } else if (resp.status_st == 6) {//放款中      //有批复的
                title_lin.setBackgroundResource(R.mipmap.order_st_back_lin1);
                title_img.setBackgroundResource(R.mipmap.order_st_back_img1);
                title_tv.setText("进行中");
                remark_tv2.setText(comments);
                title_tv.setTextColor(Color.parseColor("#FFFFFFFF"));
                remark_tv1.setTextColor(Color.parseColor("#FFFFFFFF"));
                remark_tv2.setTextColor(Color.parseColor("#FFFFFFFF"));
            }
//            else if (resp.status_st == 11) {//已完成
//                title_lin.setBackgroundResource(R.mipmap.order_st_back_lin1);
//                title_img.setBackgroundResource(R.mipmap.order_st_back_img1);
//                title_tv.setText("进行中");
//                remark_tv2.setText(comments);
//                title_tv.setTextColor(Color.parseColor("#FFFFFFFF"));
//                remark_tv1.setTextColor(Color.parseColor("#FFFFFFFF"));
//                remark_tv2.setTextColor(Color.parseColor("#FFFFFFFF"));
//            }
            else if (resp.status_st == 3) {//审核失败
                title_lin.setBackgroundResource(R.mipmap.order_st_back_lin2);
                title_img.setBackgroundResource(R.mipmap.order_st_back_img2);
                title_tv.setText("拒绝");
                remark_tv2.setText(comments);
                title_tv.setTextColor(Color.parseColor("#FFFFFFFF"));
                remark_tv1.setTextColor(Color.parseColor("#FFFFFFFF"));
                remark_tv2.setTextColor(Color.parseColor("#FFFFFFFF"));
            } else if (resp.status_st == 9) {
                title_lin.setBackgroundResource(R.mipmap.order_st_back_lin3);
                title_img.setBackgroundResource(R.mipmap.order_st_back_img3);
                title_tv.setText("取消");
                remark_tv2.setText(comments);
                title_tv.setTextColor(Color.parseColor("#FF666666"));
                remark_tv1.setTextColor(Color.parseColor("#FF666666"));
                remark_tv2.setTextColor(Color.parseColor("#FF666666"));
            } else if (resp.status_st == 11) {
                title_lin.setBackgroundResource(R.mipmap.order_st_back_lin4);
                title_img.setBackgroundResource(R.mipmap.order_st_back_img4);
                title_tv.setText("完成");
                remark_tv2.setText(comments);
                title_tv.setTextColor(Color.parseColor("#FFFFFFFF"));
                remark_tv1.setTextColor(Color.parseColor("#FFFFFFFF"));
                remark_tv2.setTextColor(Color.parseColor("#FFFFFFFF"));
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
                beforeDlrNameTv.setText(resp.old_app.dlr_nm + resp.aid_dlr_nm);
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
                dlrNameTv.setText(resp.new_app.dlr_nm + resp.aid_dlr_nm);
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
                dlrNameTv.setText(resp.dlr_nm + resp.aid_dlr_nm);
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

            findViewById(R.id.order_detail_customer_mobile_rel).setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + resp.mobile));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            });
            findViewById(R.id.order_detail_sales_mobile_rel).setOnClickListener(v -> {
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
                Intent i2 = new Intent(OrderDetailActivity.this, SubmitMaterialActivity.class);
                i2.putExtra("car_type", cartype);
                i2.putExtra("app_id", app_id);
                startActivity(i2);
            });
        });
    }

    private void submitMaterial(View view) {
        Intent intent = new Intent(OrderDetailActivity.this, SubmitMaterialActivity.class);
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
        if ("searchOrder".equals(come_from)) {
            setResult(RESULT_OK);
            finish();
        } else if ("orderitem".equals(come_from)) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            finish();
        }
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
