package com.yusion.shanghai.yusion4s.ui.entrance.apply_financing;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.Yusion4sApp;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.bean.dlr.GetBrandResp;
import com.yusion.shanghai.yusion4s.bean.dlr.GetDlrListByTokenResp;
import com.yusion.shanghai.yusion4s.bean.dlr.GetLoanBankResp;
import com.yusion.shanghai.yusion4s.bean.dlr.GetModelResp;
import com.yusion.shanghai.yusion4s.bean.dlr.GetRawCarInfoResp;
import com.yusion.shanghai.yusion4s.bean.dlr.GetStoreList;
import com.yusion.shanghai.yusion4s.bean.dlr.GetTrixResp;
import com.yusion.shanghai.yusion4s.bean.dlr.GetproductResp;
import com.yusion.shanghai.yusion4s.bean.order.submit.GetChePriceAndImageResp;
import com.yusion.shanghai.yusion4s.bean.order.submit.GetCheUrlResp;
import com.yusion.shanghai.yusion4s.bean.upload.UploadFilesUrlReq;
import com.yusion.shanghai.yusion4s.car_select.CarSelectActivity;
import com.yusion.shanghai.yusion4s.car_select.DlrStoreSelectActivity;
import com.yusion.shanghai.yusion4s.retrofit.Api;
import com.yusion.shanghai.yusion4s.retrofit.api.CheApi;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.ubt.UBT;
import com.yusion.shanghai.yusion4s.ubt.annotate.BindView;
import com.yusion.shanghai.yusion4s.ui.Car300WebViewActivity;
import com.yusion.shanghai.yusion4s.ui.entrance.AppraisalvalueActivity;
import com.yusion.shanghai.yusion4s.utils.ApiUtil;
import com.yusion.shanghai.yusion4s.utils.LoadingUtils;
import com.yusion.shanghai.yusion4s.utils.PopupDialogUtil;
import com.yusion.shanghai.yusion4s.utils.SharedPrefsUtil;
import com.yusion.shanghai.yusion4s.utils.wheel.WheelViewUtil;

import java.util.ArrayList;
import java.util.List;

public class AlterOldCarInfoActivity extends BaseActivity {
    private boolean ishadImg;
    public List<GetproductResp.SupportAreaBean> plateAddrlist = new ArrayList<>();
    //存放二手车的截图
    private List<UploadFilesUrlReq.FileUrlBean> uploadOldCarImgUrlList = new ArrayList<>();

    public static int DELAY_MILLIS;
    private String otherLimit;
    private String clt_id;
    private String id_no;
    private String vehicle_owner_lender_relation;
    private String min_reg_year;
    private String max_reg_year;
    private String che_300_label;

    private String bucket;
    private String region;
    private String file_id;

    private List<GetLoanBankResp> mLoanBankList = new ArrayList<>();
    private List<GetproductResp.ProductListBean> mProductList = new ArrayList<>();

    private List<GetDlrListByTokenResp> mDlrList = new ArrayList<>();
    private List<GetModelResp> mModelList = new ArrayList<>();
    private List<GetBrandResp> mBrandList = new ArrayList<>();
    private List<GetTrixResp> mTrixList = new ArrayList<>();
    private String dlr_id;
    private int mDlrIndex = 0;

    private int mLoanPeriodsIndex = 0;
    private int mLoanBankIndex = 0;
    private int mBrandIndex = 0;
    private int mTrixIndex = 0;
    private int mModelIndex = 0;
    private int mProductTypeIndex = 0;
    private int mManagementPriceIndex = 0;
    private int mGuidePrice = 0;
    private int mAlterReasonIndex = 0;

    private boolean changeCarLoanByCode = false;

    private boolean changeFirstPriceByCode = false;

    private boolean otherPriceChange = true;
    private boolean firstPriceChange = true;
    private boolean carLoanPriceChange = true;

    private boolean billPriceChange = true;

    private boolean isChangeCarInfoChange = true;
    private boolean isChangeOldCarInfo = true;
    private boolean isChangeOldCarOther = true;
    private boolean isChangeOldCarDance = true;
    private boolean ischangeOldCarguess = true;

    private boolean isAlterCarInfoChange = true;

    private Button carInfoNextBtn;

    private boolean isChoose = false;

    private String app_id;

    private String cheUrl;

    private String province_che_300_id;
    private String city_che_300_id;
    private String brand_id;
    private String trix_id;
    private String model_id;
    private String plate_year;
    private String plate_month;
    private String mile_age;
    private String guess_img;
    private int submit_model_id;
    private boolean isRestCarinfo = true;
    private boolean isRestDlrinfo = false;

    private String oldCarcityJson;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (TextUtils.isEmpty(otherLimit)) {
                        Log.e("TAG", "handleMessage: otherLimit错误");
                        return;
                    }
                    otherPriceChange = false;
                    int sum1 = 0;
                    if (Integer.valueOf(otherPriceTv.getText().toString()) > Integer.valueOf(otherLimit)) {
                        Toast toast = Toast.makeText(AlterOldCarInfoActivity.this, "其他费用可输入最大金额为" + otherLimit, Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        otherPriceTv.setText(String.valueOf(otherLimit));
                        otherPriceTv.setSelection(String.valueOf(otherLimit).toString().length());
                    } else {
                        if (Integer.valueOf(otherPriceTv.getText().toString()) % 100 != 0) {
                            sum1 = getRounding(otherPriceTv);
                            otherPriceTv.setText(sum1 + "");
                            otherPriceTv.setSelection(String.valueOf(sum1).toString().length());
                        } else {
                            otherPriceTv.setText(otherPriceTv.getText());
                            otherPriceTv.setSelection(otherPriceTv.getText().toString().length());
                        }
                    }
                    break;
                case 2://车辆首付款
                    firstPriceChange = false;
                    changeFirstPriceByCode = false;//jht
                    int sum = 0;

                    if (getPrice(firstPriceTv) > getPrice(oldcar_guess_price_tv)) {
                        firstPriceTv.setText(getPrice(oldcar_guess_price_tv));
                        firstPriceTv.setSelection((getPrice(oldcar_guess_price_tv) + "").length());
                    }

                    break;
                case 3://贷款额
                    carLoanPriceChange = false;
                    changeCarLoanByCode = false;
                    int sum3 = 0;
                    if (getPrice(carLoanPriceTv) > getPrice(oldcar_guess_price_tv) * 0.7) {
                        Toast.makeText(AlterOldCarInfoActivity.this, "车辆贷款额不能大于评估价的70%", Toast.LENGTH_SHORT).show();
                        carLoanPriceTv.setText("");
                    } else {
                        if (Integer.valueOf(carLoanPriceTv.getText().toString()) % 100 != 0) {
                            sum3 = getRounding(carLoanPriceTv);
                            carLoanPriceTv.setText(sum3 + "");
                            carLoanPriceTv.setSelection(String.valueOf(sum3).toString().length());
                        } else {
                            carLoanPriceTv.setText(carLoanPriceTv.getText());
                            carLoanPriceTv.setSelection(carLoanPriceTv.getText().toString().length());
                        }
                    }

                    break;
                case 4://车辆开票价
                    billPriceChange = false;
                    int sum4 = 0;
                    break;
                case 5:
                    mGuidePrice = 0;
                    guidePriceTv.setText("");

                    mLoanBankList.clear();
                    mLoanBankIndex = 0;
                    loanBankTv.setText(null);

                    mProductTypeIndex = 0;
                    productTypeTv.setText(null);

                    billPriceTv.setText("");
                    plateRegAddrTv.setText("");
                    loanPeriodsTv.setText("");
                    mLoanPeriodsIndex = 0;
                    mManagementPriceIndex = 0;

                    otherPriceTv.setText("");
                    plateRegAddrTv.setText("");//上牌地选择
                    loanPeriodsTv.setText("");//还款期限
                    carInfoAlterTv.setText("");//修改理由

                    oldcar_guess_price_tv.setText("");
                    oldcar_guess_price_tv.setEnabled(false);
                    firstPriceTv.setText("");
                    carLoanPriceTv.setText("");
                    totalLoanPriceTv.setText("");
                    managementPriceTv.setText("");
                    otherPriceTv.setText("");
                    if (TextUtils.isEmpty(oldcar_dance_tv.getText())) {
                        btn_fast_valuation.setEnabled(false);
                    }
                    look_guess_img_btn.setEnabled(false);
                    if (!TextUtils.isEmpty(car_info_tv.getText()) && !TextUtils.isEmpty(oldcar_addrtime_tv.getText()) && !TextUtils.isEmpty(oldcar_addr_tv.getText()) && !TextUtils.isEmpty(oldcar_dance_tv.getText())) {
                        btn_fast_valuation.setEnabled(true);
                        btn_reset.setEnabled(true);
                    }
                    break;
                case 6:
                    mGuidePrice = 0;
                    guidePriceTv.setText("");

                    mLoanBankList.clear();
                    mLoanBankIndex = 0;
                    loanBankTv.setText(null);

                    mProductTypeIndex = 0;
                    productTypeTv.setText(null);

                    billPriceTv.setText("");
                    plateRegAddrTv.setText("");
                    loanPeriodsTv.setText("");
                    mLoanPeriodsIndex = 0;
                    mManagementPriceIndex = 0;

                    otherPriceTv.setText("");
                    plateRegAddrTv.setText("");//上牌地选择
                    loanPeriodsTv.setText("");//还款期限
                    carInfoAlterTv.setText("");//修改理由

                    firstPriceTv.setText("");
                    carLoanPriceTv.setText("");
                    totalLoanPriceTv.setText("");
                    managementPriceTv.setText("");
                    otherPriceTv.setText("");
                    if (!TextUtils.isEmpty(oldcar_guess_price_tv.getText())) {
                        carLoanPriceTv.setEnabled(true);
                    }
                    break;

                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    @BindView(id = R.id.car_info_dlr_tv, widgetName = "car_info_dlr_tv")
    private TextView dlrTV;

    @BindView(id = R.id.car_info_dlr_tv2, widgetName = "ar_info_dlr_tv2")
    private TextView distributorTv;

    @BindView(id = R.id.car_info_brand_tv, widgetName = "car_info_brand_tv")
    private TextView brandTv;

    @BindView(id = R.id.car_info_trix_tv, widgetName = "car_info_trix_tv")
    private TextView trixTv;

    @BindView(id = R.id.car_info_model_tv, widgetName = "car_info_model_tv")
    private TextView modelTv;

    @BindView(id = R.id.car_info_management_price_tv, widgetName = "car_info_management_price_tv")
    private TextView managementPriceTv;

    @BindView(id = R.id.car_info_loan_bank_tv, widgetName = "car_info_loan_bank_tv")
    private TextView loanBankTv;

    @BindView(id = R.id.car_info_product_type_tv, widgetName = "car_info_product_type_tv")
    private TextView productTypeTv;

    @BindView(id = R.id.car_info_loan_periods_tv, widgetName = "car_info_loan_periods_tv")
    private TextView loanPeriodsTv;

    @BindView(id = R.id.car_info_plate_reg_addr_tv, widgetName = "car_info_plate_reg_addr_tv")
    private TextView plateRegAddrTv;

    @BindView(id = R.id.car_info_bill_price_tv, widgetName = "car_info_bill_price_tv")
    private EditText billPriceTv;

    @BindView(id = R.id.car_info_car_loan_price_tv, widgetName = "car_info_car_loan_price_tv")
    private EditText carLoanPriceTv;

    @BindView(id = R.id.car_info_first_price_tv, widgetName = "car_info_first_price_tv")
    private EditText firstPriceTv;

    @BindView(id = R.id.car_info_other_price_tv, widgetName = "car_info_other_price_tv", onFocusChange = "writeOtherPrice")
    private EditText otherPriceTv;

    public List<String> dlrItems;
    public List<String> brandItems;
    public List<String> trixItems;
    public List<String> modelItems;
    public List<String> bankItems;
    public List<String> productTypeItems;
    public List<Integer> management_fee_price;


    private Button btn_reset; //重置
    private Button btn_fast_valuation;//快速估值
    private EditText oldcar_guess_tv;//二手车评估价
    private Button look_guess_img_btn;//查看估值截图
    private GetDlrListByTokenResp getDlrListByTokenResp;

    /**
     * otherPrice 获取焦点执行的方法
     *
     * @param view
     * @param hasFocus
     */
    private void writeOtherPrice(View view, boolean hasFocus) {
        Log.e("TAG", "writeOtherPrice() called with: view = [" + view + "], hasFocus = [" + hasFocus + "]");
        if (hasFocus) {
            otherPriceTv.setHint("*金额取整到百位,如2300");
            otherPriceTv.setHintTextColor(Color.parseColor("#ed9121"));
            if (TextUtils.isEmpty(carLoanPriceTv.getText())) {
                Toast toast = Toast.makeText(AlterOldCarInfoActivity.this, "请输入车辆贷款额", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                otherPriceTv.setHint("请输入");
                otherPriceTv.setHintTextColor(Color.parseColor("#d1d1d1"));
                otherPriceTv.setEnabled(false);
            } else {
                otherLimit = "";
//                Log.e("TAG", "writeOtherPrice: 1");
                ApiUtil.requestUrl4Data(AlterOldCarInfoActivity.this, Api.getDlrService().getOtherFeeLimit(carLoanPriceTv.getText().toString()), data -> {
//                    DlrApi.getOtherFeeLimit(AlterOldCarInfoActivity.this, carLoanPriceTv.getText().toString(), data -> {
                    Log.e("TAG", "onItemDataCallBack: 2 " + data);
                    if (!TextUtils.isEmpty(data)) {
                        otherLimit = data;
                        Toast toast = Toast.makeText(AlterOldCarInfoActivity.this, "其他费用可输入最大金额为" + otherLimit, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                });
            }
        } else {
            otherPriceTv.setHint("请输入");
            otherPriceTv.setHintTextColor(Color.parseColor("#d1d1d1"));
        }
    }

    @BindView(id = R.id.car_info_color_tv, widgetName = "car_info_color_tv")
    private EditText colorTv;

    private TextView guidePriceTv;
    private TextView totalLoanPriceTv;
    private String cityJson;

    private LinearLayout plateRegAddrLin;
    private LinearLayout carInfoLoanPeriodsLin;
    private LinearLayout carInfoProductTypeLin;
    private LinearLayout carInfoLoanBankLin;
    private LinearLayout managementPriceLl;
    private LinearLayout carInfoModelLin;
    private LinearLayout carInfoTrixLin;
    private LinearLayout carInfoBrandLin;
    private LinearLayout carInfoDlrLin;
    private LinearLayout distributorLin;
    private LinearLayout carInfoAlterLin;
    private TextView carInfoAlterTv;

    private LinearLayout oldcar_info_lin;
    private LinearLayout oldcar_guess_price_lin;
    private LinearLayout oldcar_business_price_lin;
    private LinearLayout personal_info_detail_home_address_lin;
    private TextView oldcar_addr_tv;
    private TextView oldcar_addrtime_tv;
    private EditText oldcar_dance_tv;
    private EditText oldcar_guess_price_tv;

    private LinearLayout oldcar_guess_and_jiaoyi_lin;
    private LinearLayout oldcar_addr_lin;
    private LinearLayout oldcar_addrtime_lin;
    private View kaipiaojia_line;

    private String cartype;
    private Dialog dialog;
    private LinearLayout car_info_lin;
    private TextView car_info_tv;
    private String aid_id;
    private String aid_dlr_nm;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String why_come = intent.getStringExtra("why_come");
        if ("car_select".equals(why_come)) {
            getCarInfo(intent);
        } else if ("dlr_select".equals(why_come)) {
            getDlrInfo(intent);
        }
        //getCarInfo(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter_oldcar_info);
        DELAY_MILLIS = ((Yusion4sApp) getApplication()).getConfigResp().DELAY_MILLIS;
        app_id = getIntent().getStringExtra("app_id");
        cartype = getIntent().getStringExtra("car_type");
        dialog = LoadingUtils.createLoadingDialog(AlterOldCarInfoActivity.this);

        initTitleBar(this, "修改二手车订单").setLeftText(" 返回").setLeftTextSize(17).setLeftClickListener(v -> back());

        initView();
        initData();
        UBT.bind(this);
    }

    private void back() {
        PopupDialogUtil.showTwoButtonsDialog(this, "是否放弃本次编辑？", "放弃", "取消", dialog1 -> {
            dialog1.dismiss();
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void initView() {

        totalLoanPriceTv = findViewById(R.id.car_info_total_loan_price_tv);//总贷款费用
        otherPriceTv = findViewById(R.id.car_info_other_price_tv);//其他费用
        colorTv = findViewById(R.id.car_info_color_tv);//车辆颜色
        plateRegAddrLin = findViewById(R.id.car_info_plate_reg_addr_lin);
        plateRegAddrTv = findViewById(R.id.car_info_plate_reg_addr_tv);//选择上牌地

        dlrTV = findViewById(R.id.car_info_dlr_tv);
        brandTv = findViewById(R.id.car_info_brand_tv);
        trixTv = findViewById(R.id.car_info_trix_tv);
        guidePriceTv = findViewById(R.id.car_info_guide_price_tv);
        modelTv = findViewById(R.id.car_info_model_tv);
        loanPeriodsTv = findViewById(R.id.car_info_loan_periods_tv);
        carInfoLoanPeriodsLin = findViewById(R.id.car_info_loan_periods_lin);
        managementPriceTv = findViewById(R.id.car_info_management_price_tv);
        managementPriceLl = findViewById(R.id.car_info_management_price_lin);
        loanBankTv = findViewById(R.id.car_info_loan_bank_tv);
        productTypeTv = findViewById(R.id.car_info_product_type_tv);
        billPriceTv = findViewById(R.id.car_info_bill_price_tv);//开票价
        firstPriceTv = findViewById(R.id.car_info_first_price_tv);//首付款
        carLoanPriceTv = findViewById(R.id.car_info_car_loan_price_tv);//车辆贷款额
        carInfoDlrLin = findViewById(R.id.car_info_dlr_lin);
        distributorLin = findViewById(R.id.dlr_lin2);
        carInfoBrandLin = findViewById(R.id.car_info_brand_lin);
        carInfoTrixLin = findViewById(R.id.car_info_trix_lin);
        carInfoModelLin = findViewById(R.id.car_info_model_lin);
        carInfoLoanBankLin = findViewById(R.id.car_info_loan_bank_lin);
        carInfoProductTypeLin = findViewById(R.id.car_info_product_type_lin);
        carInfoNextBtn = findViewById(R.id.car_info_alter_btn);
        carInfoAlterLin = findViewById(R.id.car_info_alter_lin);
        carInfoAlterTv = findViewById(R.id.car_info_alter_tv);

        oldcar_info_lin = findViewById(R.id.oldcar_info_lin);//二手车上牌地 时间 里程数
        oldcar_guess_price_lin = findViewById(R.id.oldcar_guess_price_lin);//二手车评估价
        oldcar_business_price_lin = findViewById(R.id.oldcar_business_price_lin);//二手车交易价
        personal_info_detail_home_address_lin = findViewById(R.id.personal_info_detail_home_address_lin);//车辆开票价a
        oldcar_addr_tv = findViewById(R.id.oldcar_addr_tv);//二手车原上牌地
        oldcar_addrtime_tv = findViewById(R.id.oldcar_addrtime_tv);//二手车原上牌时间
        oldcar_dance_tv = findViewById(R.id.oldcar_dance_tv);//二手车里程数
        oldcar_guess_price_tv = findViewById(R.id.oldcar_guess_tv);//二手车预估价
        oldcar_guess_and_jiaoyi_lin = findViewById(R.id.oldcar_guess_and_jiaoyi_lin);//预估价和交易价的lin
        oldcar_addr_lin = findViewById(R.id.oldcar_addr_lin);
        oldcar_addrtime_lin = findViewById(R.id.oldcar_addrtime_lin);
        kaipiaojia_line = findViewById(R.id.kaipiaojia_line);
        btn_reset = findViewById(R.id.btn_reset); //重置
        btn_fast_valuation = findViewById(R.id.btn_fast_valuation);//快速估值
        look_guess_img_btn = findViewById(R.id.look_guess_img_btn);//查看估值截图
        car_info_lin = findViewById(R.id.car_info_lin);
        car_info_tv = findViewById(R.id.car_info_tv);
    }

    private void initData() {
        //网络请求11000005
        ApiUtil.requestUrl4Data(AlterOldCarInfoActivity.this, Api.getOrderService().getRawCarInfo(app_id), resp -> {
//        OrderApi.getRawCarInfo(AlterOldCarInfoActivity.this, app_id, resp -> {
            if (resp == null) {
                return;
            }
            aid_id = resp.aid_id;
            aid_dlr_nm = resp.aid_dlr_nm;
            if (aid_dlr_nm.equals("")) {
                distributorLin.setVisibility(View.GONE);
                distributorTv.setText("");
            } else {
                distributorLin.setVisibility(View.VISIBLE);
                distributorTv.setText(aid_dlr_nm);
            }
            resp.send_hand_mileage = resp.send_hand_mileage.substring(0, resp.send_hand_mileage.length() - 2);
            Log.e("TAG", "onItemDataCallBack: " + resp.send_hand_mileage);
            submit_model_id = resp.vehicle_model_id;
            che_300_label = resp.che_300_label;
            province_che_300_id = resp.province_che_300_id;
            city_che_300_id = resp.city_che_300_id;
            vehicle_owner_lender_relation = resp.vehicle_owner_lender_relation;
            brand_id = resp.brand_che_300_id;
            trix_id = resp.trix_che_300_id;
            model_id = resp.vehicle_model_che_300_id;

            min_reg_year = resp.min_reg_year;
            max_reg_year = resp.max_reg_year;
            clt_id = resp.clt_id;
            brand_id = resp.brand_id;
            id_no = resp.id_no;
            dlr_id = resp.dlr_id;
            dlrTV.setText(resp.dlr_nm);
            brandTv.setText(resp.brand);
            trixTv.setText(resp.trix);
            modelTv.setText(resp.model_name);
            colorTv.setText(resp.vehicle_color);
            guidePriceTv.setText(resp.msrp);//市场指导价
            billPriceTv.setText(resp.vehicle_price);//开票价

            carLoanPriceTv.setText(resp.vehicle_loan_amt);//车辆贷款额
            firstPriceTv.setText(resp.vehicle_down_payment);//首付款
            managementPriceTv.setText(resp.management_fee);//档案管理费
            otherPriceTv.setText(resp.other_fee);//其他费用
            totalLoanPriceTv.setText(resp.loan_amt);//总贷款额
            loanBankTv.setText(resp.loan_bank);//贷款银行
            productTypeTv.setText(resp.product_name);//产品类型
            loanPeriodsTv.setText(resp.nper + "");//还款期限
            loanPeriodsTv.setText(String.valueOf(resp.nper));
            plateRegAddrTv.setText(resp.plate_reg_addr);//上牌地
            mGuidePrice = Integer.valueOf(resp.msrp);
            oldcar_addr_tv.setText(resp.origin_plate_reg_addr);//二手车上牌地
            resp.send_hand_plate_time = resp.send_hand_plate_time.substring(0, resp.send_hand_plate_time.length() - 3);
            Log.e("TAG", "onItemDataCallBack:" + resp.send_hand_plate_time);
            oldcar_addrtime_tv.setText(resp.send_hand_plate_time);
            car_info_tv.setText(resp.model_name);
            String[] array = resp.send_hand_plate_time.split("-");
            plate_year = array[0];
            plate_month = array[1];

            isChangeCarInfoChange = true;
            isChangeOldCarInfo = true;
            isChangeOldCarOther = true;
            isChangeOldCarDance = true;
            ischangeOldCarguess = true;

            oldcar_dance_tv.setText(resp.send_hand_mileage);//里程数

            oldcar_guess_price_tv.setText(resp.send_hand_valuation);//估值价
            if (!TextUtils.isEmpty(guidePriceTv.getText())) {//市场指导价
                billPriceTv.setEnabled(true);
            }
            look_guess_img_btn.setEnabled(true);
            ApiUtil.requestUrl4Data(AlterOldCarInfoActivity.this, Api.getDlrService().getDlrListByToken(), resp1 -> {
//                DlrApi.getDlrListByToken(AlterOldCarInfoActivity.this, resp1 -> {

                mDlrList = resp1;
                dlrItems = new ArrayList<String>();
                for (GetDlrListByTokenResp item : resp1) {
                    dlrItems.add(item.dlr_nm);
                }

                mDlrIndex = selectIndex(dlrItems, mDlrIndex, dlrTV.getText().toString());
                management_fee_price = mDlrList.get(mDlrIndex).management_fee;
                Log.e("TAG", "onItemDataCallBack: ");
                Log.e("TAG", "onItemDataCallBack: " + managementPriceTv.getText().toString());
                mManagementPriceIndex = selectIndexInteger(mDlrList.get(mDlrIndex).management_fee, mManagementPriceIndex, Integer.valueOf(managementPriceTv.getText().toString()));

                isChoose = true;

            });
            ApiUtil.requestUrl4Data(AlterOldCarInfoActivity.this, Api.getDlrService().getLoanBank(resp.dlr_id), resp12 -> {
//                DlrApi.getLoanBank(AlterOldCarInfoActivity.this, resp.dlr_id, resp12 -> {
                mLoanBankList = resp12;
                bankItems = new ArrayList<String>();
                for (GetLoanBankResp item : resp12) {
                    bankItems.add(item.name);
                }
                mLoanBankIndex = selectIndex(bankItems, mLoanBankIndex, loanBankTv.getText().toString());
            });
            ApiUtil.requestUrl4Data(AlterOldCarInfoActivity.this, Api.getDlrService().getProductType(resp.bank_id, resp.dlr_id, cartype), resp13 -> {
//                DlrApi.getProductType(AlterOldCarInfoActivity.this, resp.bank_id, resp.dlr_id, cartype, resp13 -> {
                if (resp13 == null) {
                    return;
                }
                if (resp13.product_list == null && resp13.product_list.isEmpty()) {
                    return;
                }
                if (resp13.support_area == null && resp13.support_area.isEmpty()) {
                    return;
                }
                mProductList = resp13.product_list;
                productTypeItems = new ArrayList<String>();
                cityJson = resp13.support_area.toString();
                for (GetproductResp.ProductListBean item : resp13.product_list) {
                    productTypeItems.add(item.name);
                }
                if (isCanSelectIndex(productTypeItems, productTypeTv.getText().toString())) {
                    mProductTypeIndex = selectIndex(productTypeItems, mProductTypeIndex, productTypeTv.getText().toString());
                } else {
                    productTypeTv.setText("");
                    Toast.makeText(AlterOldCarInfoActivity.this, "产品类型需重新选择", Toast.LENGTH_LONG).show();
                }
            });

        });
        car_info_lin.setOnClickListener(v -> selectCarInfo());

        look_guess_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLookImgBtn();
            }
        });

        btn_fast_valuation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickFastValuationBtn();
            }
        });

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickResetBtn();
            }
        });

        oldcar_dance_tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // btn_reset.setEnabled(true);
                if (s.toString().length() == 3 && !s.toString().contains(".")) {
                    oldcar_dance_tv.setText(s.subSequence(0, s.length() - 1));
                    oldcar_dance_tv.setSelection(oldcar_dance_tv.getText().toString().length());
                    return;
                }
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                        oldcar_dance_tv.setText(s);
                        oldcar_dance_tv.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    oldcar_dance_tv.setText(s);
                    oldcar_dance_tv.setSelection(2);
                }

                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        oldcar_dance_tv.setText(s.subSequence(0, 1));
                        oldcar_dance_tv.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isChangeOldCarDance) {
                    handler.sendEmptyMessageDelayed(5, DELAY_MILLIS);
                } else {
                    isChangeOldCarDance = false;
                }
//                if (!TextUtils.isEmpty(brandTv.getText())
//                        && !TextUtils.isEmpty(trixTv.getText())
//                        && !TextUtils.isEmpty(modelTv.getText())
//                        && !TextUtils.isEmpty(oldcar_addr_tv.getText())
//                        && !TextUtils.isEmpty(oldcar_dance_tv.getText())) {
//                    btn_reset.setEnabled(true);
//                    btn_fast_valuation.setEnabled(true);
//
                if (!TextUtils.isEmpty(car_info_tv.getText()) && !TextUtils.isEmpty(oldcar_addr_tv.getText()) && !TextUtils.isEmpty(oldcar_dance_tv.getText()) && !TextUtils.isEmpty(oldcar_addrtime_tv.getText())) {
                    btn_reset.setEnabled(true);
                    Log.e("TAGGGGGGGG", "afterTextChanged: test");
                    btn_fast_valuation.setEnabled(true);
                    oldcar_guess_price_tv.setEnabled(false);
                }
            }
        });

        //上牌时间
        oldcar_addrtime_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPlateAddrTime();
            }
        });

        carInfoDlrLin.setOnClickListener(v -> selectDlrStore2());


        oldcar_addr_lin.setOnClickListener(v -> selectCarOldAddr());

        carLoanPriceTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isChangeOldCarOther) {
                    otherPriceTv.setText("");
                } else {
                    isChangeOldCarOther = false;
                }
                if (TextUtils.isEmpty(s)) {
                    handler.removeMessages(3);
                    if (changeCarLoanByCode) {
                        //开票价改变导致
                        changeCarLoanByCode = false;
                    } else {
                        //用户输入的
                        changeFirstPriceByCode = true;
                        // firstPriceTv.setText(getPrice(oldcar_guess_price_tv) + "");
                        firstPriceTv.setText("");

                    }
                } else {
                    if (changeCarLoanByCode) {
                        //首付款改变导致的
                        changeCarLoanByCode = false;
                    } else {
                        //用户输入的
                        if (carLoanPriceChange) {
                            if (handler.hasMessages(3)) {
                                handler.removeMessages(3);
                            }
                            handler.sendEmptyMessageDelayed(3, DELAY_MILLIS);
                        } else {
                            carLoanPriceChange = true;
                        }
                        changeFirstPriceByCode = true;
                        firstPriceTv.setText(getPrice(oldcar_guess_price_tv) - getPrice(carLoanPriceTv) + "");
                    }
                }
                otherPriceTv.setEnabled(true);
                totalPrice();

            }
        });

        firstPriceTv.setEnabled(false);
//档案管理费
        managementPriceLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMangerPrice();
            }
        });

        oldcar_guess_price_tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!ischangeOldCarguess) {
                    handler.sendEmptyMessageDelayed(6, DELAY_MILLIS);
                } else {
                    ischangeOldCarguess = false;
                }
                if (!TextUtils.isEmpty(oldcar_guess_price_tv.getText())) {
                    carLoanPriceTv.setEnabled(true);
                }

            }
        });
        //otherPriceTv 其他费用
        otherPriceTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (otherPriceChange) {
                    if (handler.hasMessages(1)) {
                        handler.removeMessages(1);
                    }
                    if (!TextUtils.isEmpty(s)) {
                        handler.sendEmptyMessageDelayed(1, DELAY_MILLIS);
                    }
                } else {
                    otherPriceChange = true;
                }
                if (TextUtils.isEmpty(s)) {
                    handler.removeMessages(1);
                }
                totalPrice();
            }
        });

        carInfoLoanBankLin.setOnClickListener(v -> {//选择银行列表
            selectBank();
        });
//产品类型
        carInfoProductTypeLin.setOnClickListener(v -> selectProductType());

        //上牌地
        plateRegAddrLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPlateAddr();
            }
        });
        carInfoAlterLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showWheelView(Yusion4sApp.CONFIG_RESP.carInfo_alter_key, mAlterReasonIndex, carInfoAlterLin, carInfoAlterTv, "请选择修改理由", new WheelViewUtil.OnSubmitCallBack() {
                    @Override
                    public void onSubmitCallBack(View clickedView, int selectedIndex) {
                        mAlterReasonIndex = selectedIndex;
                        carInfoNextBtn.setEnabled(true);
                    }
                });
            }
        });
        carInfoNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkCanNextStep()) {
                    GetRawCarInfoResp req = new GetRawCarInfoResp();
                    req.vehicle_owner_lender_relation = vehicle_owner_lender_relation;
                    req.vehicle_cond = cartype;

                    req.gps_fee = "0";
                    req.id_no = id_no;
                    req.clt_id = clt_id;
                    // req.dlr_id = mDlrList.get(mDlrIndex).dlr_id;
                    req.dlr_id = dlr_id;
                    req.aid_id = aid_id;
                    //req.vehicle_model_id = mModelList.get(mModelIndex).model_id;
                    req.vehicle_model_id = submit_model_id;

                    req.bank_id = mLoanBankList.get(mLoanBankIndex).bank_id;
                    req.product_id = mProductList.get(mProductTypeIndex).product_id;
                    // req.brand_id = mBrandList.get(mBrandIndex).brand_id;
                    req.dlr_nm = dlrTV.getText().toString();
                    req.guide_price = guidePriceTv.getText().toString();
                    // req.trix_id = mTrixList.get(mTrixIndex).trix_id;
                    req.loan_bank = loanBankTv.getText().toString();
                    req.app_id = app_id;
                    req.product_name = productTypeTv.getText().toString();
                    req.dlr = dlrTV.getText().toString();
//                    req.brand = brandTv.getText().toString();
//                    req.trix = trixTv.getText().toString();
//                    req.model_name = modelTv.getText().toString();

                    req.vehicle_color = colorTv.getText().toString();

                    req.vehicle_price = oldcar_guess_price_tv.getText().toString();
                    req.vehicle_down_payment = firstPriceTv.getText().toString();
                    req.vehicle_loan_amt = carLoanPriceTv.getText().toString();

                    req.loan_amt = totalLoanPriceTv.getText().toString();
                    req.management_fee = managementPriceTv.getText().toString();
                    req.other_fee = otherPriceTv.getText().toString();
                    req.nper = loanPeriodsTv.getText().toString();
                    req.plate_reg_addr = plateRegAddrTv.getText().toString();
                    req.msrp = guidePriceTv.getText().toString();
                    req.reason = carInfoAlterTv.getText().toString();
                    req.origin_plate_reg_addr = oldcar_addr_tv.getText().toString();
                    req.send_hand_plate_time = oldcar_addrtime_tv.getText().toString() + "-01";
                    //  req.send_hand_plate_time = oldcar_addrtime_tv.getText().toString();
                    req.send_hand_mileage = oldcar_dance_tv.getText().toString();
                    req.send_hand_valuation = oldcar_guess_price_tv.getText().toString();

                    ApiUtil.requestUrl4CodeAndMsg(AlterOldCarInfoActivity.this, Api.getOrderService().submitAlterInfo(req), (code, msg) -> {
//                        OrderApi.submitAlterInfo(AlterOldCarInfoActivity.this, req, (code, msg) -> {
                        if (code > -1) {
                            Toast.makeText(AlterOldCarInfoActivity.this, "订单修改成功", Toast.LENGTH_SHORT).show();
                            if (guess_img != null) {
                                UploadFilesUrlReq.FileUrlBean urlBean = new UploadFilesUrlReq.FileUrlBean();
                                urlBean.app_id = app_id;
                                urlBean.clt_id = clt_id;
                                urlBean.label = che_300_label;
                                urlBean.file_id = file_id;
                                uploadOldCarImgUrlList.add(urlBean);
                                UploadFilesUrlReq uploadFilesUrlReq = new UploadFilesUrlReq();
                                uploadFilesUrlReq.files = uploadOldCarImgUrlList;
                                uploadFilesUrlReq.region = region;
                                uploadFilesUrlReq.bucket = bucket;
                                ApiUtil.requestUrl4CodeAndMsg(AlterOldCarInfoActivity.this, Api.getUploadService().uploadFileUrl(uploadFilesUrlReq), true, (code1, msg1) -> {
//                                UploadApi.uploadFileUrl(AlterOldCarInfoActivity.this, uploadFilesUrlReq, (code1, msg1) -> {

                                });
                            }
                            finish();
                        }
                    });
                }
            }
        });

    }

    private void selectPlateAddr() {
        if (TextUtils.isEmpty(productTypeTv.getText())) {
            Toast toast = Toast.makeText(AlterOldCarInfoActivity.this, "请选择产品类型", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }
        if (!TextUtils.isEmpty(cityJson)) {
            WheelViewUtil.showCityWheelView(AlterOldCarInfoActivity.this.getClass().getSimpleName(), plateRegAddrLin, plateRegAddrTv, "请选择", new WheelViewUtil.OnCitySubmitCallBack() {
                @Override
                public void onCitySubmitCallBack(View clickedView, String city) {

                }
            }, cityJson);

        } else {
            return;
        }
    }

    private void selectProductType() {
        if (!TextUtils.isEmpty(loanBankTv.getText())) {
            ApiUtil.requestUrl4Data(AlterOldCarInfoActivity.this, Api.getDlrService().getProductType(mLoanBankList.get(mLoanBankIndex).bank_id, dlr_id, cartype), resp -> {
//                DlrApi.getProductType(AlterOldCarInfoActivity.this, mLoanBankList.get(mLoanBankIndex).bank_id, dlr_id, cartype, resp -> {
                if (resp == null) {
                    return;
                }
                cityJson = resp.support_area.toString();
                mProductList = resp.product_list;
                productTypeItems = new ArrayList<String>();
                for (GetproductResp.ProductListBean product_list : resp.product_list) {
                    productTypeItems.add(product_list.name);
                }
                mProductTypeIndex = selectIndex(productTypeItems, mProductTypeIndex, productTypeTv.getText().toString());
                WheelViewUtil.showWheelView(productTypeItems, mProductTypeIndex, carInfoProductTypeLin, productTypeTv, "请选择产品类型", new WheelViewUtil.OnSubmitCallBack() {
                    @Override
                    public void onSubmitCallBack(View clickedView, int selectedIndex) {
                        mProductTypeIndex = selectedIndex;
                        loanPeriodsTv.setText(null);
                        mLoanPeriodsIndex = 0;
                    }
                });
            });

        } else if (TextUtils.isEmpty(dlrTV.getText())) {
            Toast toast = Toast.makeText(AlterOldCarInfoActivity.this, "请您先完成经销商选择", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (TextUtils.isEmpty(loanBankTv.getText()) && !TextUtils.isEmpty(dlrTV.getText())) {
            Toast toast = Toast.makeText(AlterOldCarInfoActivity.this, "请您先完成贷款银行选择", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    private void selectBank() {
        if (!TextUtils.isEmpty(dlrTV.getText())) {
            ApiUtil.requestUrl4Data(AlterOldCarInfoActivity.this, Api.getDlrService().getLoanBank(dlr_id), resp -> {
//            DlrApi.getLoanBank(AlterOldCarInfoActivity.this, dlr_id, resp -> {
                //DlrApi.getLoanBank(AlterOldCarInfoActivity.this, mDlrList.get(mDlrIndex).dlr_id, resp -> {
                mLoanBankList = resp;//银行列表
                bankItems = new ArrayList<String>();
                for (GetLoanBankResp getLoanBankResp : resp) {
                    bankItems.add(getLoanBankResp.name);
                }
                WheelViewUtil.showWheelView(bankItems, mLoanBankIndex, carInfoLoanBankLin, loanBankTv, "请选择贷款银行", (clickedView, selectedIndex) -> {
                    mLoanBankIndex = selectedIndex;
                    mProductTypeIndex = 0;
                    productTypeTv.setText(null);
                    mLoanPeriodsIndex = 0;
                    loanPeriodsTv.setText(null);
                });

            });
        } else {
            Toast toast = Toast.makeText(AlterOldCarInfoActivity.this, "请您先完成经销商选择", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    private void selectMangerPrice() {
        if (TextUtils.isEmpty(dlrTV.getText())) {
            Toast toast = Toast.makeText(AlterOldCarInfoActivity.this, "请您先完成经销商选择", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
        } else {//需要先请求最先的东西,也就是经销商信息
            WheelViewUtil.showWheelView(management_fee_price, mManagementPriceIndex, managementPriceLl, managementPriceTv, "请选择档案管理费", new WheelViewUtil.OnSubmitCallBack() {
                //WheelViewUtil.showWheelView(mDlrList.get(mDlrIndex).management_fee, mManagementPriceIndex, managementPriceLl, managementPriceTv, "请选择档案管理费", new WheelViewUtil.OnSubmitCallBack() {
                @Override
                public void onSubmitCallBack(View clickedView, int selectedIndex) {
                    mManagementPriceIndex = selectedIndex;
                    isChoose = true;
                    totalPrice();
                }
            });
        }
    }

    private void selectCarOldAddr() {
        ApiUtil.requestUrl4Data(AlterOldCarInfoActivity.this, Api.getDlrService().getOldCarAddr(), data1 -> {
            if (data1 == null) {
                return;
            }
            oldCarcityJson = data1.toString();
            plateAddrlist = data1;
            WheelViewUtil.showCityWheelView("xxx", false, oldcar_addr_lin, oldcar_addr_tv, "原上牌地", new WheelViewUtil.OnCitySubmitCallBack() {
                // WheelViewUtil.showCityWheelView("xxx", oldcar_addr_lin, oldcar_addr_tv, "原上牌地", new WheelViewUtil.OnCitySubmitCallBack() {
                @Override
                public void onCitySubmitCallBack(View clickedView, String city) {
                    btn_reset.setEnabled(true);
                    btn_fast_valuation.setEnabled(false);
                    look_guess_img_btn.setEnabled(false);
                    if (!TextUtils.isEmpty(oldcar_addrtime_tv.getText()) && !TextUtils.isEmpty(oldcar_dance_tv.getText()) && !TextUtils.isEmpty(car_info_tv.getText())) {
                        btn_fast_valuation.setEnabled(true);
                    }
                    mGuidePrice = 0;
                    guidePriceTv.setText("");

                    mLoanBankList.clear();
                    mLoanBankIndex = 0;
                    loanBankTv.setText(null);

                    mProductTypeIndex = 0;
                    productTypeTv.setText(null);

                    billPriceTv.setText("");

                    plateRegAddrTv.setText("");

                    loanPeriodsTv.setText("");
                    mLoanPeriodsIndex = 0;
                    mManagementPriceIndex = 0;

                    oldcar_guess_price_tv.setText("");
                    oldcar_guess_price_tv.setEnabled(false);
                    firstPriceTv.setText("");
                    carLoanPriceTv.setText("");
                    managementPriceTv.setText("");
                    totalLoanPriceTv.setText("");
                    otherPriceTv.setText("");
                    plateRegAddrTv.setText("");//上牌地选择
                    loanPeriodsTv.setText("");//还款期限
                    carInfoAlterTv.setText("");//修改理由


                    // oldcar_guess_price_tv.setText("");
                    String array[] = city.split("/");
                    for (int i = 0; i < plateAddrlist.size(); i++) {
                        if (plateAddrlist.get(i).name.equals(array[0])) {
                            province_che_300_id = plateAddrlist.get(i).che_300_id;
                            for (int j = 0; j < plateAddrlist.get(i).cityList.size(); j++) {
                                if (plateAddrlist.get(i).cityList.get(j).name.equals(array[1])) {
                                    city_che_300_id = plateAddrlist.get(i).cityList.get(j).che_300_id;
                                }
                            }
                        }
                    }
                }


            }, oldCarcityJson);

        });
    }

    private void selectDlrStore2() {
        Intent intent = new Intent(this, DlrStoreSelectActivity.class);
        intent.putExtra("vehicle_cond", "二手车");
        intent.putExtra("class", AlterOldCarInfoActivity.class);
        intent.putExtra("should_reset", isRestDlrinfo);//true表示重置该页面 默认false
        startActivity(intent);
        isRestDlrinfo = false;
    }

    private void selectPlateAddrTime() {
        if (TextUtils.isEmpty(car_info_tv.getText())) {
            Toast toast = Toast.makeText(AlterOldCarInfoActivity.this, "请您先完成车型选择", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            WheelViewUtil.showDatePick(oldcar_addrtime_lin, oldcar_addrtime_tv, "请选择日期", min_reg_year, max_reg_year, new WheelViewUtil.OndateSubmitCallBack() {
                @Override
                public void OndateSubmitCallBack(View clickedView, String date) {
                    btn_reset.setEnabled(true);
                    Log.e("TAGGGGGGGG", "afterTextChanged: test3");

                    btn_fast_valuation.setEnabled(false);
                    look_guess_img_btn.setEnabled(false);


                    mGuidePrice = 0;
                    guidePriceTv.setText("");

                    mLoanBankList.clear();
                    mLoanBankIndex = 0;
                    loanBankTv.setText(null);

                    mProductTypeIndex = 0;
                    productTypeTv.setText(null);

                    billPriceTv.setText("");

                    plateRegAddrTv.setText("");

                    loanPeriodsTv.setText("");
                    mLoanPeriodsIndex = 0;
                    mManagementPriceIndex = 0;

                    oldcar_guess_price_tv.setText("");
                    oldcar_guess_price_tv.setEnabled(false);
                    //  oldcar_dance_tv.setText("");
                    //  oldcar_addr_tv.setText("");
                    // oldcar_addrtime_tv.setText("");
                    firstPriceTv.setText("");
                    carLoanPriceTv.setText("");
                    managementPriceTv.setText("");
                    totalLoanPriceTv.setText("");
                    otherPriceTv.setText("");
                    plateRegAddrTv.setText("");//上牌地选择
                    loanPeriodsTv.setText("");//还款期限
                    carInfoAlterTv.setText("");//修改理由

                    String[] array = date.split("-");
                    plate_year = array[0];
                    plate_month = array[1];
                    if (!TextUtils.isEmpty(oldcar_addrtime_tv.getText()) && !TextUtils.isEmpty(oldcar_dance_tv.getText())) {
                        btn_fast_valuation.setEnabled(true);
                    }
                }
            });
        }
    }

    public void getDlrInfo(Intent data) {
        getDlrListByTokenResp = (GetDlrListByTokenResp) data.getSerializableExtra("Dlr");
        management_fee_price = getDlrListByTokenResp.management_fee;
        mManagementPriceIndex = 0;
        GetStoreList getStoreList = (GetStoreList) data.getSerializableExtra("DlrStore");
        if (getStoreList == null) {//二级为空
            dlrTV.setText(getDlrListByTokenResp.dlr_nm);
            dlr_id = getDlrListByTokenResp.dlr_id;
            //下面的隐藏
            distributorLin.setVisibility(View.GONE);
            distributorTv.setText("");
        } else {
            //下面的展示
            distributorLin.setVisibility(View.VISIBLE);
            distributorTv.setText(getStoreList.dlr_nm);
            dlrTV.setText(getDlrListByTokenResp.dlr_nm);
            dlr_id = getDlrListByTokenResp.dlr_id;
            aid_id = getStoreList.id;
        }
        isRestDlrinfo = false;

        car_info_tv.setText("");
        isRestCarinfo = true;
        isAlterCarInfoChange = false;
        mBrandList.clear();
        mBrandIndex = 0;
        brandTv.setText("");//厂商指导价

        mTrixList.clear();
        mTrixIndex = 0;
        trixTv.setText("");//选择车型

        mModelList.clear();
        mModelIndex = 0;
        modelTv.setText("");

        mGuidePrice = 0;
        guidePriceTv.setText("");

        mLoanBankList.clear();
        mLoanBankIndex = 0;
        loanBankTv.setText(null);

        mProductTypeIndex = 0;
        productTypeTv.setText(null);

        billPriceTv.setText("");

        mManagementPriceIndex = 0;

        oldcar_guess_price_tv.setText("");
        oldcar_dance_tv.setText("");
        oldcar_addr_tv.setText("");
        oldcar_addrtime_tv.setText("");
        managementPriceTv.setText("");
        totalLoanPriceTv.setText("");
        otherPriceTv.setText("");
        plateRegAddrTv.setText("");//上牌地选择
        loanPeriodsTv.setText("");//还款期限
        carInfoAlterTv.setText("");//修改理由

        look_guess_img_btn.setEnabled(false);
        btn_reset.setEnabled(false);
        btn_fast_valuation.setEnabled(false);

    }

    public void getCarInfo(Intent data) {
        GetModelResp modleResp = (GetModelResp) data.getSerializableExtra("modleResp");
        if (modleResp == null) {
            return;
        }
        min_reg_year = modleResp.min_reg_year;
        max_reg_year = modleResp.max_reg_year;
        submit_model_id = modleResp.model_id;
        model_id = modleResp.che_300_id;//用于车300估价使用
        trix_id = data.getStringExtra("trix_che300_id");
        brand_id = data.getStringExtra("brand_che300_id");
        car_info_tv.setText(modleResp.model_name);
        plateAddrlist.clear();
        oldcar_addr_tv.setText("");

        oldcar_addrtime_tv.setText("");
        oldcar_dance_tv.setText("");

        mGuidePrice = 0;
        guidePriceTv.setText("");

        mLoanBankList.clear();
        mLoanBankIndex = 0;
        loanBankTv.setText(null);

        mProductTypeIndex = 0;
        productTypeTv.setText(null);

        billPriceTv.setText("");

        mManagementPriceIndex = 0;

        oldcar_guess_price_tv.setText("");
        oldcar_dance_tv.setText("");
        oldcar_addr_tv.setText("");
        oldcar_addrtime_tv.setText("");

        managementPriceTv.setText("");
        totalLoanPriceTv.setText("");
        otherPriceTv.setText("");
        plateRegAddrTv.setText("");//上牌地选择
        loanPeriodsTv.setText("");//还款期限

        look_guess_img_btn.setEnabled(false);
        btn_fast_valuation.setEnabled(false);
        isRestCarinfo = false;

        if (!TextUtils.isEmpty(oldcar_addrtime_tv.getText()) && !TextUtils.isEmpty(oldcar_dance_tv.getText()) && !TextUtils.isEmpty(oldcar_addr_tv.getText())) {
            btn_fast_valuation.setEnabled(true);
        }
        btn_reset.setEnabled(true);
        Log.e("TAGGGGGGGG", "afterTextChanged: test5");
    }

    private void clickResetBtn() {

        plateAddrlist.clear();
        oldcar_addr_tv.setText("");

        oldcar_addrtime_tv.setText("");
        oldcar_dance_tv.setText("");

//        btn_reset.setEnabled(false);
//        btn_fast_valuation.setEnabled(false);

        mGuidePrice = 0;
        guidePriceTv.setText("");

        mLoanBankList.clear();
        mLoanBankIndex = 0;
        loanBankTv.setText(null);

        mProductTypeIndex = 0;
        productTypeTv.setText(null);

        billPriceTv.setText("");

        mManagementPriceIndex = 0;

        oldcar_guess_price_tv.setText("");
        oldcar_dance_tv.setText("");
        oldcar_addr_tv.setText("");
        oldcar_addrtime_tv.setText("");

        managementPriceTv.setText("");
        totalLoanPriceTv.setText("");
        otherPriceTv.setText("");
        plateRegAddrTv.setText("");//上牌地选择
        loanPeriodsTv.setText("");//还款期限
        firstPriceTv.setText("");
        carLoanPriceTv.setText("");
        car_info_tv.setText("");

        look_guess_img_btn.setEnabled(false);
        btn_reset.setEnabled(false);
        btn_fast_valuation.setEnabled(false);
        isRestCarinfo = true;
    }

    private void clickFastValuationBtn() {
        mile_age = oldcar_dance_tv.getText().toString();
        if (mile_age.equals("0")) {
            Toast.makeText(AlterOldCarInfoActivity.this, "里程数不能为0，请重新输入", Toast.LENGTH_LONG).show();
            oldcar_dance_tv.setText("");
        } else {
            ApiUtil.requestUrl4Data(AlterOldCarInfoActivity.this, CheApi.getCheInfoService().getCheUrl(province_che_300_id, city_che_300_id, brand_id, trix_id, model_id, plate_year, plate_month, mile_age), new OnItemDataCallBack<GetCheUrlResp>() {
                @Override
                public void onItemDataCallBack(GetCheUrlResp data) {
                    if (data != null) {
                        cheUrl = data.url;
                        Intent intent = new Intent(AlterOldCarInfoActivity.this, Car300WebViewActivity.class);
                        intent.putExtra("cheUrl", cheUrl);
                        startActivityForResult(intent, 100);

                        ApiUtil.requestUrl4Data(AlterOldCarInfoActivity.this, CheApi.getCheInfoService().getChePriceAndImage(province_che_300_id, city_che_300_id, brand_id, trix_id, model_id, plate_year, plate_month, mile_age), true, new OnItemDataCallBack<GetChePriceAndImageResp>() {
                            @Override
                            public void onItemDataCallBack(GetChePriceAndImageResp data) {
                                ishadImg = true;
                                if (data != null && data.result != null && data.result.file_info != null) {
                                    SharedPrefsUtil.getInstance(AlterOldCarInfoActivity.this).putValue("priceAndImage", data.toString());
                                    if (data.result != null) {
                                        oldcar_guess_price_tv.setEnabled(false);
                                        oldcar_guess_price_tv.setText(data.result.price + "");

                                        mGuidePrice = 0;
                                        guidePriceTv.setText("");

                                        mLoanBankList.clear();
                                        mLoanBankIndex = 0;
                                        loanBankTv.setText(null);

                                        mProductTypeIndex = 0;
                                        productTypeTv.setText(null);

                                        billPriceTv.setText("");
                                        plateRegAddrTv.setText("");
                                        loanPeriodsTv.setText("");
                                        mLoanPeriodsIndex = 0;
                                        mManagementPriceIndex = 0;

                                        otherPriceTv.setText("");
                                        plateRegAddrTv.setText("");//上牌地选择
                                        loanPeriodsTv.setText("");//还款期限
                                        carInfoAlterTv.setText("");//修改理由

                                        firstPriceTv.setText("");
                                        carLoanPriceTv.setText("");
                                        totalLoanPriceTv.setText("");
                                        managementPriceTv.setText("");
                                        otherPriceTv.setText("");
                                        if (!TextUtils.isEmpty(oldcar_guess_price_tv.getText())) {
                                            carLoanPriceTv.setEnabled(true);
                                            look_guess_img_btn.setEnabled(true);
                                        }

                                        dialog.dismiss();
                                        guess_img = data.result.img;
                                        bucket = data.result.file_info.bucket;
                                        region = data.result.file_info.region;
                                        file_id = data.result.file_info.file_id;
                                        che_300_label = data.result.file_info.label;
                                    }
                                }
                            }
                        }, new OnItemDataCallBack<GetChePriceAndImageResp>() {
                            @Override
                            public void onItemDataCallBack(GetChePriceAndImageResp data) {
                                if (data == null || data.result == null || data.result.file_info == null) {
                                    dialog.dismiss();
                                    ishadImg = false;
                                    PopupDialogUtil.showOneButtonDialog(AlterOldCarInfoActivity.this, "由于网络问题，请手动输入二手车评估价", "好的", new PopupDialogUtil.OnPopupClickListener() {
                                        @Override
                                        public void onOkClick(Dialog dialog1) {
                                            dialog1.dismiss();
                                            oldcar_guess_price_tv.setEnabled(true);
                                            btn_fast_valuation.setEnabled(false);
                                            look_guess_img_btn.setEnabled(false);
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    private void selectCarInfo() {
        if (TextUtils.isEmpty(dlrTV.getText())) {
            Toast toast = Toast.makeText(this, "请您先完成经销商选择", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            Intent intent = new Intent(this, CarSelectActivity.class);
            intent.putExtra("vehicle_cond", "二手车");
            intent.putExtra("class", AlterOldCarInfoActivity.class);
            // intent.putExtra("dlr_id", mDlrList.get(mDlrIndex).dlr_id);
            intent.putExtra("dlr_id", dlr_id);
            intent.putExtra("should_reset", isRestCarinfo);//true表示重置该页面 默认false
            startActivity(intent);
            isRestCarinfo = false;
        }
    }

    private void clickLookImgBtn() {
        if (TextUtils.isEmpty(oldcar_guess_price_tv.getText())) {
            Toast.makeText(AlterOldCarInfoActivity.this, "请先进行车辆价格评估", Toast.LENGTH_LONG).show();
        }
        if (guess_img != null) {
            Intent intent = new Intent(AlterOldCarInfoActivity.this, AppraisalvalueActivity.class);
            intent.putExtra("guess_img", guess_img);
            startActivity(intent);
        } else {
            Intent intent = new Intent(AlterOldCarInfoActivity.this, AppraisalvalueActivity.class);

            intent.putExtra("clt_id", clt_id);
            intent.putExtra("app_id", app_id);
            intent.putExtra("role", vehicle_owner_lender_relation);
            intent.putExtra("label", che_300_label);
            startActivity(intent);
        }
    }

    private void totalPrice() {
        Integer managementPrice = 0;
        Integer carLoanPrice = getPrice(carLoanPriceTv.getText().toString());
        if (isChoose) {
            // managementPrice = mDlrList.get(mDlrIndex).management_fee.get(mManagementPriceIndex);
            managementPrice = management_fee_price.get(mManagementPriceIndex);
        } else {
            managementPrice = 0;
        }
        Integer otherPrice = getPrice(otherPriceTv.getText().toString());
        totalLoanPriceTv.setText(carLoanPrice + managementPrice + otherPrice + "");
    }

    private int getPrice(String priceStr) {
        int priceInt;
        if (TextUtils.isEmpty(priceStr)) {
            priceInt = 0;
        } else {
            priceInt = Integer.valueOf(priceStr);
        }
        return priceInt;
    }

    private int getPrice(TextView priceTv) {
        return getPrice(priceTv.getText().toString());
    }

    /**
     * 四舍五入取整的方法
     *
     * @param textView
     * @return
     */
    private int getRounding(TextView textView) {
        int yu = 0;
        int sum = 0;
        int shang = 0;
        yu = Integer.valueOf(textView.getText().toString()) % 100;
        shang = Integer.valueOf(textView.getText().toString()) / 100;
        if (yu < 50) {
            sum = shang * 100;
        } else {
            sum = (shang + 1) * 100;
        }
        return sum;
    }

    public int selectIndex(List<String> list, int index, String s) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(s)) {
                index = i;
            }
        }
        return index;
    }

    public int selectIndexInteger(List<Integer> list, int index, Integer s) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(s)) {
                index = i;
            }
        }
        return index;
    }

    public boolean isCanSelect(List<Integer> list, Integer s) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(s)) {
                return true;
            }
        }
        return false;
    }

    public boolean isCanSelectIndex(List<String> list, String s) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(s)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkCanNextStep() {
        if (TextUtils.isEmpty(dlrTV.getText())) {
            Toast.makeText(AlterOldCarInfoActivity.this, "经销商不能为空", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(car_info_tv.getText())) {
            Toast.makeText(AlterOldCarInfoActivity.this, "车型不能为空", Toast.LENGTH_SHORT).show();
        }
//        else if (TextUtils.isEmpty(brandTv.getText())) {
//            Toast.makeText(AlterOldCarInfoActivity.this, "品牌不能为空", Toast.LENGTH_SHORT).show();
//        } else if (TextUtils.isEmpty(trixTv.getText())) {
//            Toast.makeText(AlterOldCarInfoActivity.this, "车系不能为空", Toast.LENGTH_SHORT).show();
//        } else if (TextUtils.isEmpty(modelTv.getText())) {
//            Toast.makeText(AlterOldCarInfoActivity.this, "车型不能为空", Toast.LENGTH_SHORT).show();
//        }
        else if (TextUtils.isEmpty(colorTv.getText())) {
            Toast.makeText(AlterOldCarInfoActivity.this, "颜色不能为空", Toast.LENGTH_SHORT).show();
        } else if (cartype.equals("新车") && TextUtils.isEmpty(billPriceTv.getText())) {
            Toast.makeText(AlterOldCarInfoActivity.this, "开票价不能为空", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(firstPriceTv.getText())) {
            Toast.makeText(AlterOldCarInfoActivity.this, "首付款不能为空", Toast.LENGTH_SHORT).show();
        } else if (!checkFirstPriceValid()) {
            Toast.makeText(AlterOldCarInfoActivity.this, "首付款必须大于评估价30%", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(carLoanPriceTv.getText())) {
            Toast.makeText(AlterOldCarInfoActivity.this, "车辆贷款额不能为空", Toast.LENGTH_SHORT).show();
        } else if (Integer.valueOf(carLoanPriceTv.getText().toString()) == 0) {
            Toast.makeText(AlterOldCarInfoActivity.this, "车辆贷款额不能为0", Toast.LENGTH_LONG).show();
        } else if (!checkCarLoanPriceValid()) {
            Toast.makeText(AlterOldCarInfoActivity.this, "车辆贷款额必须小于评估价70%", Toast.LENGTH_SHORT).show();
        } else if (cartype.equals("二手车") && TextUtils.isEmpty(oldcar_addr_tv.getText())) {
            Toast.makeText(AlterOldCarInfoActivity.this, "二手车原上牌地不能为空", Toast.LENGTH_LONG).show();
        } else if (cartype.equals("二手车") && TextUtils.isEmpty(oldcar_dance_tv.getText())) {
            Toast.makeText(AlterOldCarInfoActivity.this, "二手车里程数不能为空", Toast.LENGTH_LONG).show();
        } else if (cartype.equals("二手车") && TextUtils.isEmpty(oldcar_guess_price_tv.getText())) {
            Toast.makeText(AlterOldCarInfoActivity.this, "二手车评估价不能为空", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(firstPriceTv.getText())) {
            Toast.makeText(AlterOldCarInfoActivity.this, "首付款不能为空", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(managementPriceTv.getText())) {
            Toast.makeText(AlterOldCarInfoActivity.this, "管理费不能为空", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(otherPriceTv.getText())) {
            Toast.makeText(AlterOldCarInfoActivity.this, "其他费用不能为空", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(loanBankTv.getText())) {
            Toast.makeText(AlterOldCarInfoActivity.this, "贷款银行不能为空", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(productTypeTv.getText())) {
            Toast.makeText(AlterOldCarInfoActivity.this, "产品类型不能为空", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(plateRegAddrTv.getText())) {
            Toast.makeText(AlterOldCarInfoActivity.this, "预计上牌地不能为空", Toast.LENGTH_SHORT).show();
        } else if (getPrice(otherPriceTv.getText().toString()) % 100 != 0) {
            Toast.makeText(AlterOldCarInfoActivity.this, "其他费用只能是整百", Toast.LENGTH_SHORT).show();
        } else if (cartype.equals("新车") && getPrice(firstPriceTv) + getPrice(carLoanPriceTv) != getPrice(billPriceTv)) {
            Toast.makeText(AlterOldCarInfoActivity.this, "首付款加车辆贷款额必须等于开票价", Toast.LENGTH_SHORT).show();
        } else if (Integer.valueOf(totalLoanPriceTv.getText().toString()) < 10000) {
            Toast.makeText(AlterOldCarInfoActivity.this, "总贷款额需多于10000元", Toast.LENGTH_LONG).show();
        } else if (cartype.equals("新车") && Integer.valueOf(totalLoanPriceTv.getText().toString()) > Integer.valueOf(billPriceTv.getText().toString()) * 0.8) {
            Toast.makeText(AlterOldCarInfoActivity.this, "总贷款额不能大于开票价的80%", Toast.LENGTH_LONG).show();
        } else if (cartype.equals("新车") && getPrice(billPriceTv) < getPrice(totalLoanPriceTv) + 1) {
            Toast.makeText(AlterOldCarInfoActivity.this, "车辆开票价要大于总贷款额", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(carInfoAlterTv.getText())) {
            Toast.makeText(AlterOldCarInfoActivity.this, "请选择末尾的修改理由", Toast.LENGTH_LONG).show();
        } else if (cartype.equals("二手车") && Integer.valueOf(totalLoanPriceTv.getText().toString()) > Integer.valueOf(oldcar_guess_price_tv.getText().toString()) * 0.7) {
            Toast.makeText(AlterOldCarInfoActivity.this, "总贷款额不能大于评估价的70%", Toast.LENGTH_LONG).show();
        } else {
            return true;
        }
        return false;
    }

    private boolean checkFirstPriceValid() {
        return getPrice(firstPriceTv) * 100 >= getPrice(oldcar_guess_price_tv) * 30 && getPrice(firstPriceTv) <= getPrice(oldcar_guess_price_tv);
    }

    private boolean checkCarLoanPriceValid() {
        return getPrice(carLoanPriceTv) * 100 <= getPrice(oldcar_guess_price_tv) * 80 && getPrice(carLoanPriceTv) >= 0;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            String priceAndImage = SharedPrefsUtil.getInstance(AlterOldCarInfoActivity.this).getValue("priceAndImage", "");
            if (priceAndImage == null || priceAndImage.isEmpty() && ishadImg) {
                dialog.show();
            } else {
                dialog.dismiss();
                look_guess_img_btn.setEnabled(true);
                SharedPrefsUtil.getInstance(AlterOldCarInfoActivity.this).remove("priceAndImage");
            }
        }
    }
}

