package com.yusion.shanghai.yusion4s.ui.entrance.apply_financing;

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
import com.yusion.shanghai.yusion4s.bean.dlr.GetTrixResp;
import com.yusion.shanghai.yusion4s.bean.dlr.GetproductResp;
import com.yusion.shanghai.yusion4s.car_select.CarSelectActivity;
import com.yusion.shanghai.yusion4s.retrofit.api.DlrApi;
import com.yusion.shanghai.yusion4s.retrofit.api.OrderApi;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnCodeAndMsgCallBack;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.ubt.UBT;
import com.yusion.shanghai.yusion4s.ubt.annotate.BindView;
import com.yusion.shanghai.yusion4s.utils.PopupDialogUtil;
import com.yusion.shanghai.yusion4s.utils.wheel.WheelViewUtil;

import java.util.ArrayList;
import java.util.List;

public class AlterCarInfoActivity extends BaseActivity {
    public static int DELAY_MILLIS;
    private String otherLimit;
    private String clt_id;
    private String id_no;
    private String vehicle_owner_lender_relation;
    private String min_reg_year;
    private String max_reg_year;

    private List<GetLoanBankResp> mLoanBankList = new ArrayList<>();
    private List<GetproductResp.ProductListBean> mProductList = new ArrayList<>();
    private List<GetDlrListByTokenResp> mDlrList = new ArrayList<>();
    private List<GetModelResp> mModelList = new ArrayList<>();
    private List<GetBrandResp> mBrandList = new ArrayList<>();
    private List<GetTrixResp> mTrixList = new ArrayList<>();
    private String dlr_id;
    private String brand_id;
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

    private int mChangeLoanAndFirstPriceCount = 0;
    private boolean ischangeBillPriceBySys = false;

    private boolean changeCarLoanByCode = false;

    private boolean changeFirstPriceByCode = false;

    private boolean otherPriceChange = true;
    private boolean firstPriceChange = true;
    private boolean carLoanPriceChange = true;
    private boolean billPriceChange = true;

    private boolean isChangeCarInfoChange = true;
    private boolean isChangeOldCarInfo = true;
    private boolean isChangeOldCarOther = true;
    private boolean isAlterCarInfoChange = true;

    private String upNumberCity;
    private Button carInfoNextBtn;
    private boolean isChoose = false;
    private String app_id;
    private int model_id;
    private boolean isRestCarinfo = false;

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
                        Toast toast = Toast.makeText(AlterCarInfoActivity.this, "其他费用可输入最大金额为" + otherLimit, Toast.LENGTH_LONG);
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
                    changeFirstPriceByCode = false;
                    int sum = 0;
                    if (getPrice(firstPriceTv) > getPrice(billPriceTv)) {//大于开票价
                        Toast.makeText(AlterCarInfoActivity.this, "首付款不能大于开票价", Toast.LENGTH_SHORT).show();
                        // changeFirstPriceByCode = false;
                        firstPriceTv.setText(getPrice(billPriceTv) + "");
                        firstPriceTv.setSelection((getPrice(billPriceTv) + "").length());
                    } else {
                        if (Integer.valueOf(firstPriceTv.getText().toString()) % 100 != 0) {
                            //sum = getRounding(firstPriceTv);
                            sum = Integer.valueOf(firstPriceTv.getText().toString());
                            firstPriceTv.setText(sum + "");
                            firstPriceTv.setSelection(String.valueOf(sum).length());
                        } else {
                            firstPriceTv.setText(firstPriceTv.getText());
                            firstPriceTv.setSelection(firstPriceTv.getText().toString().length());
                        }
                    }
                    break;
                case 3://贷款额
                    carLoanPriceChange = false;
                    changeCarLoanByCode = false;
                    int sum3 = 0;
                    if (getPrice(carLoanPriceTv) > getPrice(billPriceTv)) {
                        Toast.makeText(AlterCarInfoActivity.this, "贷款总额不能大于开票价", Toast.LENGTH_SHORT).show();
                        carLoanPriceTv.setText(getPrice(billPriceTv) + "");
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
                    if (Integer.valueOf(billPriceTv.getText().toString()) > mGuidePrice) {
                        Toast.makeText(AlterCarInfoActivity.this, "开票价不能大于厂商指导价", Toast.LENGTH_SHORT).show();
                        billPriceTv.setText(mGuidePrice + "");//设置光标在右边
                        billPriceTv.setSelection((mGuidePrice + "").length());
                    } else {
                        if (Integer.valueOf(billPriceTv.getText().toString()) % 100 != 0) {
                            sum4 = Integer.valueOf(billPriceTv.getText().toString());
                            billPriceTv.setText(sum4 + "");
                            billPriceTv.setSelection(String.valueOf(sum4).length());
                        } else {
                            billPriceTv.setText(billPriceTv.getText());
                            billPriceTv.setSelection(billPriceTv.getText().toString().length());
                        }
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


    private void writeOtherPrice(View view, boolean hasFocus) {
        Log.e("TAG", "writeOtherPrice() called with: view = [" + view + "], hasFocus = [" + hasFocus + "]");
        if (hasFocus) {
            otherPriceTv.setHint("*金额取整到百位,如2300");
            otherPriceTv.setHintTextColor(Color.parseColor("#ed9121"));
            if (TextUtils.isEmpty(carLoanPriceTv.getText())) {
                Toast toast = Toast.makeText(AlterCarInfoActivity.this, "请输入车辆贷款额", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                otherPriceTv.setHint("请输入");
                otherPriceTv.setHintTextColor(Color.parseColor("#d1d1d1"));
                otherPriceTv.setEnabled(false);
            } else {
                otherLimit = "";
                Log.e("TAG", "writeOtherPrice: 1");
                DlrApi.getOtherFeeLimit(AlterCarInfoActivity.this, carLoanPriceTv.getText().toString(), new OnItemDataCallBack<String>() {
                    @Override
                    public void onItemDataCallBack(String data) {
                        Log.e("TAG", "onItemDataCallBack: 2 " + data);
                        if (!TextUtils.isEmpty(data)) {
                            otherLimit = data;
                            Toast toast = Toast.makeText(AlterCarInfoActivity.this, "其他费用可输入最大金额为" + otherLimit, Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
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
    private LinearLayout carInfoAlterLin;
    private TextView carInfoAlterTv;

    private LinearLayout personal_info_detail_home_address_lin;

    private View kaipiaojia_line;

    private String cartype;
    private LinearLayout car_info_lin;
    private TextView car_info_tv;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getCarInfo(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter_car_info);

        DELAY_MILLIS = ((Yusion4sApp) getApplication()).getConfigResp().DELAY_MILLIS;
        app_id = getIntent().getStringExtra("app_id");
        cartype = getIntent().getStringExtra("car_type");

        initTitleBar(this, "修改新车订单").setLeftText(" 返回").setLeftTextSize(17).setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        initView();
        initData();
        UBT.bind(this);
    }

    private void back() {
        PopupDialogUtil.showTwoButtonsDialog(this, "是否放弃本次编辑？", "放弃", "取消", new PopupDialogUtil.OnOkClickListener() {
            @Override
            public void onOkClick(Dialog dialog) {
                dialog.dismiss();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        back();

    }

    private void initView() {

        totalLoanPriceTv = (TextView) findViewById(R.id.car_info_total_loan_price_tv);//总贷款费用
        otherPriceTv = (EditText) findViewById(R.id.car_info_other_price_tv);//其他费用
        colorTv = (EditText) findViewById(R.id.car_info_color_tv);//车辆颜色
        plateRegAddrLin = (LinearLayout) findViewById(R.id.car_info_plate_reg_addr_lin);
        plateRegAddrTv = (TextView) findViewById(R.id.car_info_plate_reg_addr_tv);//选择上牌地

        dlrTV = (TextView) findViewById(R.id.car_info_dlr_tv);
        brandTv = (TextView) findViewById(R.id.car_info_brand_tv);
        trixTv = (TextView) findViewById(R.id.car_info_trix_tv);
        guidePriceTv = (TextView) findViewById(R.id.car_info_guide_price_tv);
        modelTv = (TextView) findViewById(R.id.car_info_model_tv);
        loanPeriodsTv = (TextView) findViewById(R.id.car_info_loan_periods_tv);
        carInfoLoanPeriodsLin = (LinearLayout) findViewById(R.id.car_info_loan_periods_lin);
        managementPriceTv = (TextView) findViewById(R.id.car_info_management_price_tv);
        managementPriceLl = (LinearLayout) findViewById(R.id.car_info_management_price_lin);
        loanBankTv = (TextView) findViewById(R.id.car_info_loan_bank_tv);
        productTypeTv = (TextView) findViewById(R.id.car_info_product_type_tv);
        billPriceTv = (EditText) findViewById(R.id.car_info_bill_price_tv);//开票价
        firstPriceTv = (EditText) findViewById(R.id.car_info_first_price_tv);//首付款
        carLoanPriceTv = (EditText) findViewById(R.id.car_info_car_loan_price_tv);//车辆贷款额
        carInfoDlrLin = (LinearLayout) findViewById(R.id.car_info_dlr_lin);
        carInfoBrandLin = (LinearLayout) findViewById(R.id.car_info_brand_lin);
        carInfoTrixLin = (LinearLayout) findViewById(R.id.car_info_trix_lin);
        carInfoModelLin = (LinearLayout) findViewById(R.id.car_info_model_lin);
        carInfoLoanBankLin = (LinearLayout) findViewById(R.id.car_info_loan_bank_lin);
        carInfoProductTypeLin = (LinearLayout) findViewById(R.id.car_info_product_type_lin);
        carInfoNextBtn = (Button) findViewById(R.id.car_info_alter_btn);
        carInfoAlterLin = (LinearLayout) findViewById(R.id.car_info_alter_lin);
        carInfoAlterTv = (TextView) findViewById(R.id.car_info_alter_tv);
        personal_info_detail_home_address_lin = (LinearLayout) findViewById(R.id.personal_info_detail_home_address_lin);//车辆开票价a
        kaipiaojia_line = findViewById(R.id.kaipiaojia_line);
        car_info_lin = findViewById(R.id.car_info_lin);
        car_info_tv = findViewById(R.id.car_info_tv);
    }

    private void initData() {
        //网络请求11000005
        OrderApi.getRawCarInfo(AlterCarInfoActivity.this, app_id, new OnItemDataCallBack<GetRawCarInfoResp>() {
            @Override
            public void onItemDataCallBack(GetRawCarInfoResp resp) {
                model_id = resp.vehicle_model_id;
                vehicle_owner_lender_relation = resp.vehicle_owner_lender_relation;
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
                car_info_tv.setText(resp.model_name);
                mGuidePrice = Integer.valueOf(resp.msrp);

                isChangeCarInfoChange = true;
                isChangeOldCarInfo = true;
                isChangeOldCarOther = true;

                if (!TextUtils.isEmpty(guidePriceTv.getText())) {//市场指导价
                    billPriceTv.setEnabled(true);
                }
                isChoose = true;
                DlrApi.getDlrListByToken(AlterCarInfoActivity.this, new OnItemDataCallBack<List<GetDlrListByTokenResp>>() {
                    @Override
                    public void onItemDataCallBack(List<GetDlrListByTokenResp> resp) {
                        mDlrList = resp;
                        dlrItems = new ArrayList<String>();
                        for (GetDlrListByTokenResp item : resp) {
                            dlrItems.add(item.dlr_nm);
                        }
                        mDlrIndex = selectIndex(dlrItems, mDlrIndex, dlrTV.getText().toString());
                        mManagementPriceIndex = selectIndexInteger(mDlrList.get(mDlrIndex).management_fee, mManagementPriceIndex, Integer.valueOf(managementPriceTv.getText().toString()));
                    }
                });

                DlrApi.getBrand(AlterCarInfoActivity.this, resp.dlr_id, new OnItemDataCallBack<List<GetBrandResp>>() {
                    @Override
                    public void onItemDataCallBack(List<GetBrandResp> resp) {
                        mBrandList = resp;
                        brandItems = new ArrayList<String>();
                        for (GetBrandResp item : resp) {
                            brandItems.add(item.brand_name);
                        }
                        mBrandIndex = selectIndex(brandItems, mBrandIndex, brandTv.getText().toString());
                    }
                });

                DlrApi.getTrix(AlterCarInfoActivity.this, resp.brand_id, new OnItemDataCallBack<List<GetTrixResp>>() {
                    @Override
                    public void onItemDataCallBack(List<GetTrixResp> resp) {
                        mTrixList = resp;
                        trixItems = new ArrayList<String>();
                        for (GetTrixResp item : resp) {
                            trixItems.add(item.trix_name);
                        }
                        mTrixIndex = selectIndex(trixItems, mTrixIndex, trixTv.getText().toString());
                    }
                });
                DlrApi.getModel(AlterCarInfoActivity.this, resp.trix_id, "二手车", new OnItemDataCallBack<List<GetModelResp>>() {
                    @Override
                    public void onItemDataCallBack(List<GetModelResp> resp) {
                        mModelList = resp;
                        modelItems = new ArrayList<String>();
                        for (GetModelResp item : resp) {
                            modelItems.add(item.model_name);
                        }
                        mModelIndex = selectIndex(modelItems, mModelIndex, modelTv.getText().toString());
                    }
                });

                DlrApi.getLoanBank(AlterCarInfoActivity.this, resp.dlr_id, new OnItemDataCallBack<List<GetLoanBankResp>>() {
                    @Override
                    public void onItemDataCallBack(List<GetLoanBankResp> resp) {
                        mLoanBankList = resp;
                        bankItems = new ArrayList<String>();
                        for (GetLoanBankResp item : resp) {
                            bankItems.add(item.name);
                        }
                        mLoanBankIndex = selectIndex(bankItems, mLoanBankIndex, loanBankTv.getText().toString());
                    }
                });
                DlrApi.getProductType(AlterCarInfoActivity.this, resp.bank_id, resp.dlr_id, cartype, new OnItemDataCallBack<GetproductResp>() {
                    @Override
                    public void onItemDataCallBack(GetproductResp resp) {
                        if (resp == null) {
                            return;
                        }
                        if (resp.product_list == null && resp.product_list.isEmpty()) {
                            return;
                        }
                        if (resp.support_area == null && resp.support_area.isEmpty()) {
                            return;
                        }
                        mProductList = resp.product_list;
                        productTypeItems = new ArrayList<String>();
                        cityJson = resp.support_area.toString();
                        for (GetproductResp.ProductListBean item : resp.product_list) {
                            productTypeItems.add(item.name);
                        }
                        if (isCanSelectIndex(productTypeItems, productTypeTv.getText().toString())) {
                            mProductTypeIndex = selectIndex(productTypeItems, mProductTypeIndex, productTypeTv.getText().toString());
                        } else {
                            productTypeTv.setText("");
                            Toast.makeText(AlterCarInfoActivity.this, "产品类型需重新选择", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        //选择门店
        carInfoDlrLin.setOnClickListener(v ->
                selectDlrStore()
        );
        car_info_lin.setOnClickListener(v ->
                selectCarInfo()
        );
//选择品牌
        carInfoBrandLin.setOnClickListener(v ->
                selectBrand()
        );

//选择车系
        carInfoTrixLin.setOnClickListener(v ->
                selectTrix()
        );
        //选择车型
        carInfoModelLin.setOnClickListener(v ->
                selectModel()
        );
        //选择银行列表
        carInfoLoanBankLin.setOnClickListener(v ->
                selectBank()
        );
//选择产品类型
        carInfoProductTypeLin.setOnClickListener(v ->
                selectProductType()
        );
        //选择上牌地
        plateRegAddrLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPlateAddr();
            }
        });
        //选择档案管理费
        managementPriceLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMangermentPrice();
            }
        });

//车辆开票价
        billPriceTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                ischangeBillPriceBySys = true;
                mChangeLoanAndFirstPriceCount = 2;

                changeFirstPriceByCode = true;
                changeCarLoanByCode = true;
                if (!isChangeCarInfoChange) {
                    firstPriceTv.setText("");
                    carLoanPriceTv.setText("");
                } else {
                    isChangeCarInfoChange = false;
                }

                if (billPriceChange) {
                    if (handler.hasMessages(4)) {
                        handler.removeMessages(4);
                    }
                    handler.sendEmptyMessageDelayed(4, DELAY_MILLIS);
                    firstPriceTv.setEnabled(false);
                    carLoanPriceTv.setEnabled(true);

                } else {
                    billPriceChange = true;
                }
                if (TextUtils.isEmpty(s)) {
                    handler.removeMessages(4);
                }
            }
        });

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
                        firstPriceTv.setText(getPrice(billPriceTv) + "");
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
                        firstPriceTv.setText(getPrice(billPriceTv) - getPrice(carLoanPriceTv) + "");
                    }
                }
                otherPriceTv.setEnabled(true);
                totalPrice();

            }
        });

        firstPriceTv.setEnabled(false);

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
                        //每次edittext有变化的时候，移除上次发出的延迟线程
                        // handler.removeCallbacks(delayRun);
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
                    req.dlr_id = mDlrList.get(mDlrIndex).dlr_id;
                    // req.vehicle_model_id = mModelList.get(mModelIndex).model_id;
                    req.vehicle_model_id = model_id;

                    req.bank_id = mLoanBankList.get(mLoanBankIndex).bank_id;
                    req.product_id = mProductList.get(mProductTypeIndex).product_id;
                    //  req.brand_id = mBrandList.get(mBrandIndex).brand_id;
                    req.dlr_nm = dlrTV.getText().toString();
                    req.guide_price = guidePriceTv.getText().toString();
                    //  req.trix_id = mTrixList.get(mTrixIndex).trix_id;
                    req.loan_bank = loanBankTv.getText().toString();
                    req.app_id = app_id;
                    req.product_name = productTypeTv.getText().toString();
                    req.dlr = dlrTV.getText().toString();
                    req.brand = brandTv.getText().toString();
                    req.trix = trixTv.getText().toString();
                    req.model_name = modelTv.getText().toString();
                    req.vehicle_color = colorTv.getText().toString();
                    req.vehicle_price = billPriceTv.getText().toString();

                    req.vehicle_down_payment = firstPriceTv.getText().toString();
                    req.vehicle_loan_amt = carLoanPriceTv.getText().toString();
                    req.loan_amt = totalLoanPriceTv.getText().toString();
                    req.management_fee = managementPriceTv.getText().toString();
                    req.other_fee = otherPriceTv.getText().toString();
                    req.nper = loanPeriodsTv.getText().toString();
                    req.plate_reg_addr = plateRegAddrTv.getText().toString();
                    req.msrp = guidePriceTv.getText().toString();
                    req.reason = carInfoAlterTv.getText().toString();

                    OrderApi.submitAlterInfo(AlterCarInfoActivity.this, req, new OnCodeAndMsgCallBack() {
                        @Override
                        public void callBack(int code, String msg) {
                            if (code > -1) {
                                Toast.makeText(AlterCarInfoActivity.this, "订单修改成功", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
                }
            }
        });

    }

    public void getCarInfo(Intent data) {
        GetModelResp modleResp = (GetModelResp) data.getSerializableExtra("modleResp");
        model_id = modleResp.model_id;
        car_info_tv.setText(modleResp.model_name);

        mGuidePrice = (int) modleResp.msrp;
        guidePriceTv.setText(mGuidePrice + "");

        clearExceptCarInfo();
        isRestCarinfo = true;
        billPriceTv.setEnabled(true);
    }

    private void selectPlateAddr() {
        if (TextUtils.isEmpty(productTypeTv.getText())) {
            Toast toast = Toast.makeText(AlterCarInfoActivity.this, "请选择产品类型", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }
        if (!TextUtils.isEmpty(cityJson)) {
            WheelViewUtil.showCityWheelView(AlterCarInfoActivity.this.getClass().getSimpleName(), plateRegAddrLin, plateRegAddrTv, "请选择", new WheelViewUtil.OnCitySubmitCallBack() {
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
            DlrApi.getProductType(AlterCarInfoActivity.this, mLoanBankList.get(mLoanBankIndex).bank_id, mDlrList.get(mDlrIndex).dlr_id, cartype, new OnItemDataCallBack<GetproductResp>() {
                @Override
                public void onItemDataCallBack(GetproductResp resp) {
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
                }
            });

        } else if (TextUtils.isEmpty(dlrTV.getText())) {
            Toast toast = Toast.makeText(AlterCarInfoActivity.this, "请您先完成门店选择", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (TextUtils.isEmpty(loanBankTv.getText()) && !TextUtils.isEmpty(dlrTV.getText())) {
            Toast toast = Toast.makeText(AlterCarInfoActivity.this, "请您先完成贷款银行选择", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    private void selectCarInfo() {
        if (TextUtils.isEmpty(dlrTV.getText())) {
            Toast toast = Toast.makeText(this, "请您先完成门店选择", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            Intent intent = new Intent(this, CarSelectActivity.class);
            intent.putExtra("class", AlterCarInfoActivity.class);
            intent.putExtra("vehicle_cond", "新车");
            intent.putExtra("dlr_id", mDlrList.get(mDlrIndex).dlr_id);
            intent.putExtra("should_reset", isRestCarinfo);//true表示重置该页面 默认false
            isRestCarinfo = true;
            startActivity(intent);
        }
    }

    private void selectBank() {
        if (!TextUtils.isEmpty(dlrTV.getText())) {
            DlrApi.getLoanBank(AlterCarInfoActivity.this, mDlrList.get(mDlrIndex).dlr_id, resp -> {
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
            Toast toast = Toast.makeText(AlterCarInfoActivity.this, "请您先完成门店选择", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    private void selectMangermentPrice() {
        if (TextUtils.isEmpty(dlrTV.getText())) {
            Toast toast = Toast.makeText(AlterCarInfoActivity.this, "请您先完成门店选择", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
        } else {//需要先请求最先的东西,也就是门店信息
            WheelViewUtil.showWheelView(mDlrList.get(mDlrIndex).management_fee, mManagementPriceIndex, managementPriceLl, managementPriceTv, "请选择档案管理费", new WheelViewUtil.OnSubmitCallBack() {
                @Override
                public void onSubmitCallBack(View clickedView, int selectedIndex) {
                    mManagementPriceIndex = selectedIndex;
                    isChoose = true;
                    totalPrice();
                }
            });
        }
    }

    private void selectModel() {
        if (!TextUtils.isEmpty(trixTv.getText())) {
            modelItems = new ArrayList<String>();
            DlrApi.getModel(AlterCarInfoActivity.this, mTrixList.get(mTrixIndex).trix_id, "二手车", resp -> {
                if (resp != null && !resp.isEmpty()) {
                    mModelList = resp;
                    modelItems = new ArrayList<String>();
                    for (GetModelResp modelResp : resp) {
                        modelItems.add(modelResp.model_name);
                    }
                    mModelIndex = selectIndex(modelItems, mModelIndex, modelTv.getText().toString());
                    WheelViewUtil.showWheelView(modelItems, mModelIndex, carInfoModelLin, modelTv, "请选择车型", (clickedView, selectedIndex) -> {
                        mModelIndex = selectedIndex;
                        mGuidePrice = (int) resp.get(mModelIndex).msrp;
                        guidePriceTv.setText(mGuidePrice + "");
                        billPriceTv.setEnabled(true);

                        clearExceptCarInfo();
                    });
                }
            });

        } else if (TextUtils.isEmpty(dlrTV.getText())) {
            Toast toast = Toast.makeText(AlterCarInfoActivity.this, "请您先完成门店选择", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (TextUtils.isEmpty(trixTv.getText()) && !TextUtils.isEmpty(dlrTV.getText())) {
            Toast toast = Toast.makeText(AlterCarInfoActivity.this, "请您先完成车系选择", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    private void clearExceptCarInfo() {
        mLoanBankList.clear();
        mLoanBankIndex = 0;
        loanBankTv.setText(null);
        mProductTypeIndex = 0;
        productTypeTv.setText(null);

        billPriceTv.setText("");
        mManagementPriceIndex = 0;
        managementPriceTv.setText("");
        totalLoanPriceTv.setText("");
        otherPriceTv.setText("");
        plateRegAddrTv.setText("");//上牌地选择
        loanPeriodsTv.setText("");//还款期限
        carInfoAlterTv.setText("");//修改理由
    }

    private void selectTrix() {
        if (!TextUtils.isEmpty(brandTv.getText()) && !TextUtils.isEmpty(dlrTV.getText())) {
            DlrApi.getTrix(AlterCarInfoActivity.this, mBrandList.get(mBrandIndex).brand_id, resp -> {
                if (resp != null && !resp.isEmpty()) {
                    mTrixList = resp;
                    trixItems = new ArrayList<String>();
                    for (GetTrixResp trixResp : resp) {
                        trixItems.add(trixResp.trix_name);
                    }
                    mTrixIndex = selectIndex(trixItems, mTrixIndex, trixTv.getText().toString());
                    WheelViewUtil.showWheelView(trixItems, mTrixIndex, carInfoBrandLin, trixTv, "请选择车系", (clickedView, selectedIndex) -> {
                        mTrixIndex = selectedIndex;

                        mModelList.clear();
                        mModelIndex = 0;
                        modelTv.setText("");

                        mGuidePrice = 0;
                        guidePriceTv.setText("");

                        clearExceptCarInfo();
                    });
                }
            });

        } else if (TextUtils.isEmpty(dlrTV.getText())) {
            Toast toast = Toast.makeText(AlterCarInfoActivity.this, "请您先完成门店选择", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (TextUtils.isEmpty(brandTv.getText()) && !TextUtils.isEmpty(dlrTV.getText())) {
            Toast toast = Toast.makeText(AlterCarInfoActivity.this, "请您先完成品牌选择", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    private void selectBrand() {
        if (!TextUtils.isEmpty(dlrTV.getText())) {
            DlrApi.getBrand(AlterCarInfoActivity.this, mDlrList.get(mDlrIndex).dlr_id, new OnItemDataCallBack<List<GetBrandResp>>() {
                @Override
                public void onItemDataCallBack(List<GetBrandResp> resp) {
                    if (resp != null && !resp.isEmpty()) {
                        mBrandList = resp;
                        brandItems = new ArrayList<String>();
                        for (GetBrandResp item : resp) {
                            brandItems.add(item.brand_name);
                        }
                        WheelViewUtil.showWheelView(brandItems, mBrandIndex, carInfoBrandLin, brandTv, "请选择品牌", (clickedView, selectedIndex) -> {
                            mBrandIndex = selectedIndex;
                            mTrixList.clear();
                            mTrixIndex = 0;
                            trixTv.setText("");

                            mModelList.clear();
                            mModelIndex = 0;
                            modelTv.setText("");

                            mGuidePrice = 0;
                            guidePriceTv.setText("");

                            clearExceptCarInfo();

                        });
                    }
                }
            });
        } else {
            Toast toast = Toast.makeText(AlterCarInfoActivity.this, "请您先完成门店选择", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    private void selectDlrStore() {
        DlrApi.getDlrListByToken(AlterCarInfoActivity.this, resp -> {
            if (resp != null && !resp.isEmpty()) {
                mDlrList = resp;
                dlrItems = new ArrayList<String>();
                for (GetDlrListByTokenResp item : resp) {
                    dlrItems.add(item.dlr_nm);
                }
                mDlrIndex = selectIndex(dlrItems, mDlrIndex, dlrTV.getText().toString());
                WheelViewUtil.showWheelView(dlrItems, mDlrIndex, carInfoDlrLin, dlrTV, "请选择门店", (clickedView, selectedIndex) -> {
                    mDlrIndex = selectedIndex;
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

                    clearExceptCarInfo();
                });

            }
        });
    }

    private void totalPrice() {
        Integer managementPrice = 0;
        Integer carLoanPrice = getPrice(carLoanPriceTv.getText().toString());
        if (isChoose) {
            managementPrice = mDlrList.get(mDlrIndex).management_fee.get(mManagementPriceIndex);
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
            Toast.makeText(AlterCarInfoActivity.this, "门店不能为空", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(carInfoAlterTv.getText())) {
            Toast.makeText(AlterCarInfoActivity.this, "车型不能为空", Toast.LENGTH_SHORT).show();
        }
//        else if (TextUtils.isEmpty(brandTv.getText())) {
//            Toast.makeText(AlterCarInfoActivity.this, "品牌不能为空", Toast.LENGTH_SHORT).show();
//        } else if (TextUtils.isEmpty(trixTv.getText())) {
//            Toast.makeText(AlterCarInfoActivity.this, "车系不能为空", Toast.LENGTH_SHORT).show();
//        } else if (TextUtils.isEmpty(modelTv.getText())) {
//            Toast.makeText(AlterCarInfoActivity.this, "车型不能为空", Toast.LENGTH_SHORT).show();
//        }
        else if (TextUtils.isEmpty(colorTv.getText())) {
            Toast.makeText(AlterCarInfoActivity.this, "颜色不能为空", Toast.LENGTH_SHORT).show();
        } else if (cartype.equals("新车") && TextUtils.isEmpty(billPriceTv.getText())) {
            Toast.makeText(AlterCarInfoActivity.this, "开票价不能为空", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(firstPriceTv.getText())) {
            Toast.makeText(AlterCarInfoActivity.this, "首付款不能为空", Toast.LENGTH_SHORT).show();
        } else if (!checkFirstPriceValid()) {
            Toast.makeText(AlterCarInfoActivity.this, "首付款必须大于开票价20%", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(carLoanPriceTv.getText())) {
            Toast.makeText(AlterCarInfoActivity.this, "车辆贷款额不能为空", Toast.LENGTH_SHORT).show();
        } else if (Integer.valueOf(carLoanPriceTv.getText().toString()) == 0) {
            Toast.makeText(AlterCarInfoActivity.this, "车辆贷款额不能为0", Toast.LENGTH_LONG).show();
        } else if (!checkCarLoanPriceValid()) {
            Toast.makeText(AlterCarInfoActivity.this, "车辆贷款额必须小于开票价80%", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(firstPriceTv.getText())) {
            Toast.makeText(AlterCarInfoActivity.this, "首付款不能为空", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(managementPriceTv.getText())) {
            Toast.makeText(AlterCarInfoActivity.this, "管理费不能为空", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(otherPriceTv.getText())) {
            Toast.makeText(AlterCarInfoActivity.this, "其他费用不能为空", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(loanBankTv.getText())) {
            Toast.makeText(AlterCarInfoActivity.this, "贷款银行不能为空", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(productTypeTv.getText())) {
            Toast.makeText(AlterCarInfoActivity.this, "产品类型不能为空", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(plateRegAddrTv.getText())) {
            Toast.makeText(AlterCarInfoActivity.this, "预计上牌地不能为空", Toast.LENGTH_SHORT).show();
        } else if (getPrice(otherPriceTv.getText().toString()) % 100 != 0) {
            Toast.makeText(AlterCarInfoActivity.this, "其他费用只能是整百", Toast.LENGTH_SHORT).show();
        } else if (cartype.equals("新车") && getPrice(firstPriceTv) + getPrice(carLoanPriceTv) != getPrice(billPriceTv)) {
            Toast.makeText(AlterCarInfoActivity.this, "首付款加车辆贷款额必须等于开票价", Toast.LENGTH_SHORT).show();
        } else if (Integer.valueOf(totalLoanPriceTv.getText().toString()) < 10000) {
            Toast.makeText(AlterCarInfoActivity.this, "总贷款额需多于10000元", Toast.LENGTH_LONG).show();
        } else if (cartype.equals("新车") && Integer.valueOf(totalLoanPriceTv.getText().toString()) > Integer.valueOf(billPriceTv.getText().toString()) * 0.8) {
            Toast.makeText(AlterCarInfoActivity.this, "总贷款额不能大于开票价的80%", Toast.LENGTH_LONG).show();
        } else if (cartype.equals("新车") && getPrice(billPriceTv) < getPrice(totalLoanPriceTv) + 1) {
            Toast.makeText(AlterCarInfoActivity.this, "车辆开票价要大于总贷款额", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(carInfoAlterTv.getText())) {
            Toast.makeText(AlterCarInfoActivity.this, "请选择末尾的修改理由", Toast.LENGTH_LONG).show();
        } else {
            return true;
        }
        return false;
    }

    private boolean checkFirstPriceValid() {
        return getPrice(firstPriceTv) * 100 >= getPrice(billPriceTv) * 20 && getPrice(firstPriceTv) <= getPrice(billPriceTv);
    }

    private boolean checkCarLoanPriceValid() {
        return getPrice(carLoanPriceTv) * 100 <= getPrice(billPriceTv) * 80 && getPrice(carLoanPriceTv) >= 0;
    }
}
