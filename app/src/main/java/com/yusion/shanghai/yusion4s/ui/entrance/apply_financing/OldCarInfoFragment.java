package com.yusion.shanghai.yusion4s.ui.entrance.apply_financing;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.Yusion4sApp;
import com.yusion.shanghai.yusion4s.base.BaseFragment;
import com.yusion.shanghai.yusion4s.bean.dlr.GetBrandResp;
import com.yusion.shanghai.yusion4s.bean.dlr.GetDlrListByTokenResp;
import com.yusion.shanghai.yusion4s.bean.dlr.GetLoanBankResp;
import com.yusion.shanghai.yusion4s.bean.dlr.GetModelResp;
import com.yusion.shanghai.yusion4s.bean.dlr.GetStoreList;
import com.yusion.shanghai.yusion4s.bean.dlr.GetTrixResp;
import com.yusion.shanghai.yusion4s.bean.dlr.GetproductResp;
import com.yusion.shanghai.yusion4s.bean.order.submit.GetChePriceAndImageResp;
import com.yusion.shanghai.yusion4s.bean.order.submit.GetCheUrlResp;
import com.yusion.shanghai.yusion4s.bean.order.submit.SubmitOrderReq;
import com.yusion.shanghai.yusion4s.car_select.CarSelectActivity;
import com.yusion.shanghai.yusion4s.car_select.DlrStoreSelectActivity;
import com.yusion.shanghai.yusion4s.event.ApplyFinancingFragmentEvent;
import com.yusion.shanghai.yusion4s.retrofit.Api;
import com.yusion.shanghai.yusion4s.retrofit.api.CheApi;
import com.yusion.shanghai.yusion4s.retrofit.api.DlrApi;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.settings.Settings;
import com.yusion.shanghai.yusion4s.ubt.UBT;
import com.yusion.shanghai.yusion4s.ubt.annotate.BindView;
import com.yusion.shanghai.yusion4s.ui.Car300WebViewActivity;
import com.yusion.shanghai.yusion4s.ui.entrance.AppraisalvalueActivity;
import com.yusion.shanghai.yusion4s.ui.order.OrderCreateActivity;
import com.yusion.shanghai.yusion4s.utils.ApiUtil;
import com.yusion.shanghai.yusion4s.utils.LoadingUtils;
import com.yusion.shanghai.yusion4s.utils.PopupDialogUtil;
import com.yusion.shanghai.yusion4s.utils.SharedPrefsUtil;
import com.yusion.shanghai.yusion4s.utils.wheel.WheelViewUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import static com.yusion.shanghai.yusion4s.R.id.oldcar_guess_tv;

/**
 * Created by aa on 2017/8/9.
 */

public class OldCarInfoFragment extends BaseFragment {
    public List<GetproductResp.SupportAreaBean> plateAddrlist = new ArrayList<>();
    private boolean ishadImg;
    public String min_reg_year;
    public String max_reg_year;
    public static int DELAY_MILLIS;
    private String otherLimit;
    private List<GetLoanBankResp> mLoanBankList = new ArrayList<>();
    private List<GetproductResp.ProductListBean> mProductList = new ArrayList<>();
    private List<GetDlrListByTokenResp> mDlrList = new ArrayList<>();
    private List<GetModelResp> mModelList = new ArrayList<>();
    private List<GetBrandResp> mBrandList = new ArrayList<>();
    private List<GetTrixResp> mTrixList = new ArrayList<>();
    private int mDlrIndex = 0;
    private int mLoanPeriodsIndex = 0;
    private int mLoanBankIndex = 0;
    private int mBrandIndex = 0;
    private int mTrixIndex = 0;
    private int mModelIndex = 0;
    private int mProductTypeIndex = 0;
    private int mManagementPriceIndex = 0;
    private int mGuidePrice = 0;

    private int mChangeLoanAndFirstPriceCount = 0;
    private boolean ischangeBillPriceBySys = false;

    private boolean changeCarLoanByCode = false;

    private boolean changeFirstPriceByCode = false;

    private boolean otherPriceChange = true;
    private boolean firstPriceChange = true;
    private boolean carLoanPriceChange = true;

    private boolean billPriceChange = true;

    private Button carInfoNextBtn;

    private boolean isChoose = false;
    private boolean isChangeCarInfoChange;

    private String cartype;
    private String cheUrl;
    private String province_che_300_id;
    private String city_che_300_id;

    private String brand_id;
    private String trix_id;
    private String model_id;

    private String dlr_id;
    private String aid_id;

    private String plate_year;
    private String plate_month;
    private String mile_age;
    private String guess_img;
    private int submit_model_id;
    private boolean isRestCarinfo = true;
    private boolean isRestDlrinfo = true;

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
                        Toast toast = Toast.makeText(mContext, "其他费用可输入最大金额为" + otherLimit, Toast.LENGTH_LONG);
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
                        Toast.makeText(mContext, "车辆贷款额不能大于评估价的70%", Toast.LENGTH_SHORT).show();
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
                case 4://
                    billPriceChange = false;
//                    oldcar_business_price_tv.setText(oldcar_business_price_tv.getText());
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
                    loanPeriodsTv.setText("");
                    mLoanPeriodsIndex = 0;
                    mManagementPriceIndex = 0;

                    plateRegAddrTv.setText("");//上牌地选择
                    loanPeriodsTv.setText("");//还款期限

                    oldcar_guess_price_tv.setEnabled(false);
                    oldcar_guess_price_tv.setText("");
                    firstPriceTv.setText("");
                    carLoanPriceTv.setText("");
                    totalLoanPriceTv.setText("");
                    managementPriceTv.setText("");
                    otherPriceTv.setText("");
                    look_guess_img_btn.setEnabled(false);


                    oldcar_guess_price_tv.setText("");
                    firstPriceTv.setText("");
                    carLoanPriceTv.setText("");
                    totalLoanPriceTv.setText("");
                    managementPriceTv.setText("");
                    otherPriceTv.setText("");
                    look_guess_img_btn.setEnabled(false);
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

    private ArrayList<String> dlrItems;
    private ArrayList<String> brandItems;
    private ArrayList<String> trixItems;
    private ArrayList<String> modelItems;
    private Dialog dialog;
    private GetDlrListByTokenResp getDlrListByTokenResp;

    /**
     * otherPrice 获取焦点后执行的方法
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
                Toast toast = Toast.makeText(mContext, "请输入车辆贷款额", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                otherPriceTv.setHint("请输入");
                otherPriceTv.setHintTextColor(Color.parseColor("#d1d1d1"));
                otherPriceTv.setEnabled(false);
            } else {
                otherLimit = "";
//                Log.e("TAG", "writeOtherPrice: 1");
                ApiUtil.requestUrl4Data(mContext, Api.getDlrService().getOtherFeeLimit(carLoanPriceTv.getText().toString()), data -> {
//                DlrApi.getOtherFeeLimit(mContext, carLoanPriceTv.getText().toString(), data -> {
                    Log.e("TAG", "onItemDataCallBack: 2 " + data);
                    if (!TextUtils.isEmpty(data)) {
                        otherLimit = data;
                        Toast toast = Toast.makeText(mContext, "其他费用可输入最大金额为" + otherLimit, Toast.LENGTH_SHORT);
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
    private String oldCarcityJson;

    //上牌地选择
    private LinearLayout plateRegAddrLin;
    //还款期限选择
    private LinearLayout carInfoLoanPeriodsLin;

    //产品类型选择", onClick = "selectProductType")
    private LinearLayout carInfoProductTypeLin;

    //贷款银行选择selectLoanBank"
    private LinearLayout carInfoLoanBankLin;

    // "是否管贷档案管理费选择", onClick = "selectManagementPrice"
    private LinearLayout managementPriceLl;

    // "车型选择", onClick = "selectModel"
    private LinearLayout carInfoModelLin;

    // "车系选择", onClick = "selectTrix"
    private LinearLayout carInfoTrixLin;

    // "品牌选择", onClick = "selectBrand"
    private LinearLayout carInfoBrandLin;

    // = "经销商选择", onClick = "selectDlr
    private LinearLayout carInfoDlrLin;
    private LinearLayout distributorLin;


    private LinearLayout oldcar_info_lin;
    private LinearLayout oldcar_guess_price_lin;
    private LinearLayout oldcar_business_price_lin;
    private LinearLayout personal_info_detail_home_address_lin;
    private TextView oldcar_addr_tv;
    private TextView oldcar_addrtime_tv;
    private EditText oldcar_dance_tv;
    private EditText oldcar_guess_price_tv;
    // private EditText oldcar_business_price_tv;
    private LinearLayout oldcar_guess_and_jiaoyi_lin;
    private LinearLayout oldcar_addr_lin;
    private LinearLayout oldcar_addrtime_lin;
    private View kaipiaojia_line;

    private Button btn_reset; //重置
    private Button btn_fast_valuation;//快速估值
    private Button look_guess_img_btn;//查看估值截图
    //车型选择
    private LinearLayout car_info_lin;
    private TextView car_info_tv;


    public static OldCarInfoFragment newInstance() {
        Bundle args = new Bundle();
        OldCarInfoFragment fragment = new OldCarInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_oldcar_info, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UBT.bind(this, view, getClass().getSimpleName());
        DELAY_MILLIS = ((Yusion4sApp) getActivity().getApplication()).getConfigResp().DELAY_MILLIS;
        totalLoanPriceTv = (TextView) view.findViewById(R.id.car_info_total_loan_price_tv);//总贷款费用
        colorTv = (EditText) view.findViewById(R.id.car_info_color_tv);//车辆颜色
        plateRegAddrLin = (LinearLayout) view.findViewById(R.id.car_info_plate_reg_addr_lin);
        plateRegAddrTv = (TextView) view.findViewById(R.id.car_info_plate_reg_addr_tv);//选择上牌地
        dlrTV = (TextView) view.findViewById(R.id.car_info_dlr_tv);
        brandTv = (TextView) view.findViewById(R.id.car_info_brand_tv);
        trixTv = (TextView) view.findViewById(R.id.car_info_trix_tv);
        guidePriceTv = (TextView) view.findViewById(R.id.car_info_guide_price_tv);
        modelTv = (TextView) view.findViewById(R.id.car_info_model_tv);
        loanPeriodsTv = (TextView) view.findViewById(R.id.car_info_loan_periods_tv);
        carInfoLoanPeriodsLin = (LinearLayout) view.findViewById(R.id.car_info_loan_periods_lin);
        managementPriceTv = (TextView) view.findViewById(R.id.car_info_management_price_tv);
        managementPriceLl = (LinearLayout) view.findViewById(R.id.car_info_management_price_lin);
        loanBankTv = (TextView) view.findViewById(R.id.car_info_loan_bank_tv);
        productTypeTv = (TextView) view.findViewById(R.id.car_info_product_type_tv);
        billPriceTv = (EditText) view.findViewById(R.id.car_info_bill_price_tv);//开票价
        firstPriceTv = (EditText) view.findViewById(R.id.car_info_first_price_tv);//首付款
        carLoanPriceTv = (EditText) view.findViewById(R.id.car_info_car_loan_price_tv);//车辆贷款额
        carInfoDlrLin = (LinearLayout) view.findViewById(R.id.car_info_dlr_lin);
        distributorLin = (LinearLayout) view.findViewById(R.id.dlr_lin2);
        carInfoBrandLin = (LinearLayout) view.findViewById(R.id.car_info_brand_lin);
        carInfoTrixLin = (LinearLayout) view.findViewById(R.id.car_info_trix_lin);
        carInfoModelLin = (LinearLayout) view.findViewById(R.id.car_info_model_lin);
        carInfoLoanBankLin = (LinearLayout) view.findViewById(R.id.car_info_loan_bank_lin);
        carInfoProductTypeLin = (LinearLayout) view.findViewById(R.id.car_info_product_type_lin);
        oldcar_info_lin = (LinearLayout) view.findViewById(R.id.oldcar_info_lin);//二手车上牌地 时间 里程数
        oldcar_guess_price_lin = (LinearLayout) view.findViewById(R.id.oldcar_guess_price_lin);//二手车评估价
        oldcar_business_price_lin = (LinearLayout) view.findViewById(R.id.oldcar_business_price_lin);//二手车交易价
        personal_info_detail_home_address_lin = (LinearLayout) view.findViewById(R.id.personal_info_detail_home_address_lin);//车辆开票价a
        oldcar_addr_tv = (TextView) view.findViewById(R.id.oldcar_addr_tv);//二手车原上牌地
        oldcar_addrtime_tv = (TextView) view.findViewById(R.id.oldcar_addrtime_tv);//二手车原上牌时间
        oldcar_dance_tv = (EditText) view.findViewById(R.id.oldcar_dance_tv);//二手车里程数

        oldcar_guess_price_tv = (EditText) view.findViewById(oldcar_guess_tv);//二手车预估价

        oldcar_guess_and_jiaoyi_lin = (LinearLayout) view.findViewById(R.id.oldcar_guess_and_jiaoyi_lin);//预估价和交易价的lin
        oldcar_addr_lin = (LinearLayout) view.findViewById(R.id.oldcar_addr_lin);
        oldcar_addrtime_lin = (LinearLayout) view.findViewById(R.id.oldcar_addrtime_lin);
        kaipiaojia_line = view.findViewById(R.id.kaipiaojia_line);
        btn_reset = (Button) view.findViewById(R.id.btn_reset); //重置
        btn_fast_valuation = (Button) view.findViewById(R.id.btn_fast_valuation);//快速估值
        look_guess_img_btn = (Button) view.findViewById(R.id.look_guess_img_btn);//查看估值截图
        cartype = getActivity().getIntent().getStringExtra("car_type");
        dialog = LoadingUtils.createLoadingDialog(mContext);
        carInfoNextBtn = (Button) view.findViewById(R.id.car_info_next_btn);
        car_info_lin = view.findViewById(R.id.car_info_lin);
        car_info_tv = view.findViewById(R.id.car_info_tv);

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

        carInfoLoanPeriodsLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLoanPeriods();
            }
        });

        oldcar_dance_tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btn_fast_valuation.setEnabled(false);
                look_guess_img_btn.setEnabled(false);
                oldcar_guess_price_tv.setText("");
                btn_reset.setEnabled(true);
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

                handler.sendEmptyMessageDelayed(5, DELAY_MILLIS);

                if (!TextUtils.isEmpty(car_info_tv.getText()) && !TextUtils.isEmpty(oldcar_addr_tv.getText()) && !TextUtils.isEmpty(oldcar_dance_tv.getText()) && !TextUtils.isEmpty(oldcar_addrtime_tv.getText())) {
                    btn_reset.setEnabled(true);
                    btn_fast_valuation.setEnabled(true);
                    oldcar_guess_price_tv.setEnabled(false);
                }


            }
        });
        //上牌时间
        oldcar_addrtime_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAddrTime();
            }
        });

        /**
         * 进行经销商选择
         */
//        view.findViewById(R.id.car_info_dlr_lin)
        carInfoDlrLin.setOnClickListener(v ->
//                selectDlrStore()
                selectDlrStore2());

        //上牌地
        plateRegAddrLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPlateRegAddrLin();
            }
        });
        managementPriceLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickManagentPriceLl();
            }
        });

        oldcar_addr_lin.setOnClickListener(v -> selectCarOldAddr());

        oldcar_guess_price_tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(oldcar_guess_price_tv.getText())) {
                    carLoanPriceTv.setEnabled(true);
                }
                carLoanPriceTv.setText("");
                firstPriceTv.setText("");
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

                totalLoanPriceTv.setText("");
                managementPriceTv.setText("");
                otherPriceTv.setText("");
            }
        });

        carInfoLoanBankLin.setOnClickListener(v -> //选择银行列表
                selectBank());
//        view.findViewById(R.id.car_info_product_type_lin)
        carInfoProductTypeLin.setOnClickListener(v -> selectProductType());
        carLoanPriceTv.addTextChangedListener(new TextWatcher() {//车辆贷款
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                otherPriceTv.setText("");
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
                        firstPriceTv.setText(getPrice(oldcar_guess_price_tv) - getPrice(carLoanPriceTv) + "");

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

        carInfoNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carInfoNextBtn.setFocusable(true);
                carInfoNextBtn.setFocusableInTouchMode(true);
                carInfoNextBtn.requestFocus();
                carInfoNextBtn.requestFocusFromTouch();
            }
        });

        carInfoNextBtn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.e("TAG", "onFocusChange: ");
                if (hasFocus) {
                    v.clearFocus();
                    if (Settings.isShameData) {
                        EventBus.getDefault().post(ApplyFinancingFragmentEvent.showCreditInfo);
                    } else if (checkCanNextStep()) {

                        SubmitOrderReq req = ((OrderCreateActivity) getActivity()).req;
//                        req.dlr_id = mDlrList.get(mDlrIndex).dlr_id;
                        req.dlr_id = dlr_id;
                        req.aid_id = aid_id;
                        req.vehicle_model_id = submit_model_id;
                        //req.vehicle_model_id = mModelList.get(mModelIndex).model_id;
                        req.vehicle_color = colorTv.getText().toString();
                        req.vehicle_down_payment = firstPriceTv.getText().toString();
                        req.vehicle_loan_amt = carLoanPriceTv.getText().toString();
                        req.loan_amt = totalLoanPriceTv.getText().toString();
                        req.management_fee = managementPriceTv.getText().toString();
                        req.other_fee = otherPriceTv.getText().toString();
                        req.bank_id = mLoanBankList.get(mLoanBankIndex).bank_id;
                        req.product_id = mProductList.get(mProductTypeIndex).product_id;
                        req.nper = loanPeriodsTv.getText().toString();
                        req.vehicle_cond = cartype;
                        req.vehicle_price = oldcar_guess_price_tv.getText().toString();
                        req.origin_plate_reg_addr = oldcar_addr_tv.getText().toString();
                        req.send_hand_plate_time = oldcar_addrtime_tv.getText().toString() + "-01";
                        req.send_hand_mileage = oldcar_dance_tv.getText().toString();
                        req.send_hand_valuation = oldcar_guess_price_tv.getText().toString();
                        req.plate_reg_addr = plateRegAddrTv.getText().toString();
                        req.plate_reg_addr = plateRegAddrTv.getText().toString();
                        EventBus.getDefault().post(ApplyFinancingFragmentEvent.showCreditInfo);
                    }
                }
            }
        });

        carInfoNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Settings.isShameData) {
                    EventBus.getDefault().post(ApplyFinancingFragmentEvent.showCreditInfo);
                } else if (checkCanNextStep()) {

                    SubmitOrderReq req = ((OrderCreateActivity) getActivity()).req;
                    //  req.dlr_id = mDlrList.get(mDlrIndex).dlr_id;
                    req.dlr_id = dlr_id;
                    req.aid_id = aid_id;
                    // req.vehicle_model_id = mModelList.get(mModelIndex).model_id;
                    req.vehicle_model_id = submit_model_id;

                    req.vehicle_color = colorTv.getText().toString();
                    req.vehicle_down_payment = firstPriceTv.getText().toString();
                    req.vehicle_loan_amt = carLoanPriceTv.getText().toString();
                    req.loan_amt = totalLoanPriceTv.getText().toString();
                    req.management_fee = managementPriceTv.getText().toString();
                    req.other_fee = otherPriceTv.getText().toString();

                    req.bank_id = mLoanBankList.get(mLoanBankIndex).bank_id;
                    req.product_id = mProductList.get(mProductTypeIndex).product_id;
                    req.nper = loanPeriodsTv.getText().toString();
                    req.vehicle_price = oldcar_guess_price_tv.getText().toString();
                    req.vehicle_cond = cartype;
                    req.origin_plate_reg_addr = oldcar_addr_tv.getText().toString();
                    req.send_hand_plate_time = oldcar_addrtime_tv.getText().toString() + "-01";
                    req.send_hand_mileage = oldcar_dance_tv.getText().toString();
                    req.send_hand_valuation = oldcar_guess_price_tv.getText().toString();
                    req.plate_reg_addr = plateRegAddrTv.getText().toString();
                    EventBus.getDefault().post(ApplyFinancingFragmentEvent.showCreditInfo);

                }
            }
        });
        ((TextView) view.findViewById(R.id.step1)).setTypeface(Typeface.createFromAsset(mContext.getAssets(), "yj.ttf"));
        ((TextView) view.findViewById(R.id.step2)).setTypeface(Typeface.createFromAsset(mContext.getAssets(), "yj.ttf"));

        if (Settings.isShameData) {
            carInfoNextBtn.setEnabled(true);
        }

    }

    private void selectProductType() {
        if (!TextUtils.isEmpty(loanBankTv.getText())) {
            ApiUtil.requestUrl4Data(mContext,  Api.getDlrService().getProductType(mLoanBankList.get(mLoanBankIndex).bank_id, dlr_id, cartype),resp ->{
//                DlrApi.getProductType(mContext, mLoanBankList.get(mLoanBankIndex).bank_id, dlr_id, cartype, resp -> {
                if (resp == null) {
                    return;
                }
                cityJson = resp.support_area.toString();
                mProductList = resp.product_list;

                List<String> items = new ArrayList<String>();

                for (GetproductResp.ProductListBean product_list : resp.product_list) {
                    items.add(product_list.name);
                }
                WheelViewUtil.showWheelView(items, mProductTypeIndex, carInfoProductTypeLin, productTypeTv, "请选择产品类型", new WheelViewUtil.OnSubmitCallBack() {
                    @Override
                    public void onSubmitCallBack(View clickedView, int selectedIndex) {
                        mProductTypeIndex = selectedIndex;
                        loanPeriodsTv.setText(null);
                        mLoanPeriodsIndex = 0;
                    }
                });
            });

        } else if (TextUtils.isEmpty(dlrTV.getText())) {
            Toast toast = Toast.makeText(mContext, "请您先完成经销商选择", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (TextUtils.isEmpty(loanBankTv.getText()) && !TextUtils.isEmpty(dlrTV.getText())) {
            Toast toast = Toast.makeText(mContext, "请您先完成贷款银行选择", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    private void selectBank() {
        if (!TextUtils.isEmpty(dlrTV.getText())) {
            ApiUtil.requestUrl4Data(mContext,Api.getDlrService().getLoanBank(dlr_id),resp ->{
//            DlrApi.getLoanBank(mContext, dlr_id, resp -> {
                mLoanBankList = resp;//银行列表
                List<String> items = new ArrayList<String>();
                for (GetLoanBankResp getLoanBankResp : resp) {
                    items.add(getLoanBankResp.name);
                }
                WheelViewUtil.showWheelView(items, mLoanBankIndex, carInfoLoanBankLin, loanBankTv, "请选择贷款银行", (clickedView, selectedIndex) -> {
                    mLoanBankIndex = selectedIndex;
                    mProductTypeIndex = 0;
                    productTypeTv.setText(null);
                    mLoanPeriodsIndex = 0;
                    loanPeriodsTv.setText(null);
                });

            });
        } else {
            Toast toast = Toast.makeText(mContext, "请您先完成经销商选择", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    private void selectCarOldAddr() {
        // TODO: 2018/1/11  
//        ApiUtil.requestUrl4Data(mContext,Api.getDlrService().getOldCarAddr(),data1 ->{

            DlrApi.getOldCarAddr(mContext, new OnItemDataCallBack<List<GetproductResp.SupportAreaBean>>() {
            @Override
            public void onItemDataCallBack(List<GetproductResp.SupportAreaBean> data) {
                if (data == null) {
                    return;
                }
                plateAddrlist = data;
                oldCarcityJson = data.toString();
                WheelViewUtil.showCityWheelView("xxx", oldcar_addr_lin, oldcar_addr_tv, "原上牌地", new WheelViewUtil.OnCitySubmitCallBack() {
                    @Override
                    public void onCitySubmitCallBack(View clickedView, String city) {
                        btn_reset.setEnabled(true);
                        btn_fast_valuation.setEnabled(false);
                        look_guess_img_btn.setEnabled(false);
                        if (!TextUtils.isEmpty(oldcar_addrtime_tv.getText()) && !TextUtils.isEmpty(oldcar_dance_tv.getText()) && !TextUtils.isEmpty(car_info_tv.getText())) {
                            btn_fast_valuation.setEnabled(true);
                        }
                        oldcar_guess_price_tv.setText("");
                        oldcar_guess_price_tv.setEnabled(false);
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
                        //oldcar_dance_tv.setText("");
                        // oldcar_addr_tv.setText("");
                        // oldcar_addrtime_tv.setText("");
                        firstPriceTv.setText("");
                        carLoanPriceTv.setText("");
                        managementPriceTv.setText("");
                        totalLoanPriceTv.setText("");
                        otherPriceTv.setText("");
                        plateRegAddrTv.setText("");//上牌地选择
                        loanPeriodsTv.setText("");//还款期限

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
            }
        });
    }


    private void selectAddrTime() {
        if (TextUtils.isEmpty(car_info_tv.getText())) {
            Toast toast = Toast.makeText(mContext, "请您先完成车型选择", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            WheelViewUtil.showDatePick(oldcar_addrtime_lin, oldcar_addrtime_tv, "请选择日期", min_reg_year, max_reg_year, new WheelViewUtil.OndateSubmitCallBack() {
                @Override
                public void OndateSubmitCallBack(View clickedView, String date) {
                    btn_reset.setEnabled(true);
                    btn_fast_valuation.setEnabled(false);
                    look_guess_img_btn.setEnabled(false);
                    oldcar_guess_price_tv.setText("");
                    oldcar_guess_price_tv.setEnabled(false);
                    if (!TextUtils.isEmpty(oldcar_addr_tv.getText()) && !TextUtils.isEmpty(oldcar_dance_tv.getText()) && !TextUtils.isEmpty(car_info_tv.getText())) {
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
                    firstPriceTv.setText("");
                    carLoanPriceTv.setText("");
                    managementPriceTv.setText("");
                    totalLoanPriceTv.setText("");
                    otherPriceTv.setText("");
                    plateRegAddrTv.setText("");//上牌地选择
                    loanPeriodsTv.setText("");//还款期限

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

    public void getCarInfo(Intent data) {
        GetModelResp modleResp = (GetModelResp) data.getSerializableExtra("modleResp");
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
        oldcar_guess_price_tv.setEnabled(false);
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
    }

    private void selectCarInfo() {
        if (TextUtils.isEmpty(dlrTV.getText())) {
            Toast toast = Toast.makeText(mContext, "请您先完成经销商选择", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            Intent intent = new Intent(mContext, CarSelectActivity.class);
            intent.putExtra("class", OrderCreateActivity.class);
            intent.putExtra("vehicle_cond", "二手车");
            intent.putExtra("dlr_id", dlr_id);
            intent.putExtra("should_reset", isRestCarinfo);//true表示重置该页面 默认false
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.pop_car_select_enter_anim, R.anim.stay);
            isRestCarinfo = false;
        }
    }

    private void clickResetBtn() {
        plateAddrlist.clear();
        oldcar_addr_tv.setText("");

        oldcar_addrtime_tv.setText("");
        oldcar_dance_tv.setText("");

        mBrandList.clear();
        mBrandIndex = 0;
        brandTv.setText("");

        mTrixList.clear();
        mTrixIndex = 0;
        trixTv.setText("");

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

        car_info_tv.setText("");
        look_guess_img_btn.setEnabled(false);
        btn_reset.setEnabled(false);
        btn_fast_valuation.setEnabled(false);
        isRestCarinfo = true;
    }

    private void clickFastValuationBtn() {
        mile_age = oldcar_dance_tv.getText().toString();
        if (mile_age.equals("0")) {
            Toast.makeText(mContext, "里程数不能为0，请重新输入", Toast.LENGTH_LONG).show();
            oldcar_dance_tv.setText("");
        } else {
            ApiUtil.requestUrl4Data(mContext, CheApi.getCheInfoService().getCheUrl(province_che_300_id, city_che_300_id, brand_id, trix_id, model_id, plate_year, plate_month, mile_age), new OnItemDataCallBack<GetCheUrlResp>() {
                @Override
                public void onItemDataCallBack(GetCheUrlResp data) {
                    if (data != null) {
                        cheUrl = data.url;
                        Intent intent = new Intent(mContext, Car300WebViewActivity.class);
                        intent.putExtra("cheUrl", cheUrl);
                        startActivityForResult(intent, 100);

                        ApiUtil.requestUrl4Data(mContext, CheApi.getCheInfoService().getChePriceAndImage(province_che_300_id, city_che_300_id, brand_id, trix_id, model_id, plate_year, plate_month, mile_age), false, new OnItemDataCallBack<GetChePriceAndImageResp>() {
                            @Override
                            public void onItemDataCallBack(GetChePriceAndImageResp data) {
                                if (data != null && data.result != null && data.result.file_info != null) {
                                    ishadImg = true;
                                    SharedPrefsUtil.getInstance(mContext).putValue("priceAndImage", data.toString());
                                    Log.e("SP", SharedPrefsUtil.getInstance(mContext).getValue("priceAndImage", ""));
                                    if (data.result != null) {
                                        oldcar_guess_price_tv.setText(data.result.price + "");
                                        if (!TextUtils.isEmpty(oldcar_guess_price_tv.getText())) {
                                            carLoanPriceTv.setEnabled(true);
                                            look_guess_img_btn.setEnabled(true);
                                        }
                                        dialog.dismiss();
                                        guess_img = "";
                                        guess_img = data.result.img;
                                        ((OrderCreateActivity) getActivity()).file_id = data.result.file_info.file_id;
                                        ((OrderCreateActivity) getActivity()).label = data.result.file_info.label;
                                        ((OrderCreateActivity) getActivity()).bucket = data.result.file_info.bucket;
                                        ((OrderCreateActivity) getActivity()).region = data.result.file_info.region;
                                    }
                                }
                            }
                        }, getChePriceAndImageResp -> {
                            if (getChePriceAndImageResp == null || getChePriceAndImageResp.result == null || getChePriceAndImageResp.result.file_info == null) {
                                dialog.dismiss();
                                ishadImg = false;
                                Log.e("TAG", "onItemDataCallBack: ssssssss");
                                PopupDialogUtil.showOneButtonDialog(mContext, "由于网络问题，请手动输入二手车评估价", "好的", new PopupDialogUtil.OnPopupClickListener() {
                                    @Override
                                    public void onOkClick(Dialog dialog1) {
                                        dialog1.dismiss();
                                        oldcar_guess_price_tv.setEnabled(true);
                                        btn_fast_valuation.setEnabled(false);
                                        look_guess_img_btn.setEnabled(false);
                                    }
                                });
                            }
                        });
                    }
                }
            });
        }
    }

    private void clickLookImgBtn() {
        if (TextUtils.isEmpty(oldcar_guess_price_tv.getText())) {
            Toast.makeText(mContext, "请先进行车辆价格评估", Toast.LENGTH_LONG).show();
        }
        if (guess_img != null) {
            Intent intent = new Intent(mContext, AppraisalvalueActivity.class);
            intent.putExtra("guess_img", guess_img);
            intent.putExtra("guess_img2", false);
            startActivity(intent);
        }
    }

    private void clickManagentPriceLl() {
        if (TextUtils.isEmpty(dlrTV.getText())) {
            Toast toast = Toast.makeText(mContext, "请您先完成经销商选择", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
        } else {//(clickedView, selectedIndex) -> mManagementPriceIndex = selectedIndex)
            WheelViewUtil.showWheelView(getDlrListByTokenResp.management_fee, mManagementPriceIndex, managementPriceLl, managementPriceTv, "请选择档案管理费", new WheelViewUtil.OnSubmitCallBack() {
                //  WheelViewUtil.showWheelView(mDlrList.get(mDlrIndex).management_fee, mManagementPriceIndex, managementPriceLl, managementPriceTv, "请选择档案管理费", new WheelViewUtil.OnSubmitCallBack() {
                @Override
                public void onSubmitCallBack(View clickedView, int selectedIndex) {
                    mManagementPriceIndex = selectedIndex;
                    isChoose = true;
                    totalPrice();
                }
            });
        }
    }

    private void clickPlateRegAddrLin() {
        if (TextUtils.isEmpty(productTypeTv.getText())) {
            Toast toast = Toast.makeText(mContext, "请选择产品类型", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }
        if (!TextUtils.isEmpty(cityJson)) {
            WheelViewUtil.showCityWheelView(OldCarInfoFragment.this.getClass().getSimpleName(), plateRegAddrLin, plateRegAddrTv, "请选择", new WheelViewUtil.OnCitySubmitCallBack() {
                @Override
                public void onCitySubmitCallBack(View clickedView, String city) {
                    if (!TextUtils.isEmpty(plateRegAddrTv.getText())) {
                        carInfoNextBtn.setEnabled(true);
                    }
                }
            }, cityJson);

        } else {
            return;
        }
    }

    private void clickLoanPeriods() {
        if (TextUtils.isEmpty(productTypeTv.getText())) {
            Toast.makeText(mContext, "请先选择产品类型", Toast.LENGTH_LONG).show();
        } else {
            WheelViewUtil.showWheelView(mProductList.get(mProductTypeIndex).nper_list, mLoanPeriodsIndex, carInfoLoanPeriodsLin, loanPeriodsTv, "请选择还款期限", new WheelViewUtil.OnSubmitCallBack() {
                @Override
                public void onSubmitCallBack(View clickedView, int selectedIndex) {
                    mLoanPeriodsIndex = selectedIndex;
                }
            });
        }
    }

    private void totalPrice() {
        Integer managementPrice = 0;
        Integer carLoanPrice = getPrice(carLoanPriceTv.getText().toString());
        if (isChoose) {
            managementPrice = getDlrListByTokenResp.management_fee.get(mManagementPriceIndex);
//            managementPrice = mDlrList.get(mDlrIndex).management_fee.get(mManagementPriceIndex);
        } else {
            managementPrice = 0;
        }
        Integer otherPrice = getPrice(otherPriceTv.getText().toString());
        totalLoanPriceTv.setText(carLoanPrice + managementPrice + otherPrice + "");
    }

    private boolean checkCanNextStep() {
        if (TextUtils.isEmpty(dlrTV.getText())) {
            Toast.makeText(mContext, "经销商不能为空", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(car_info_tv.getText())) {
            Toast.makeText(mContext, "车型不能为空", Toast.LENGTH_SHORT).show();
        }
//        else if (TextUtils.isEmpty(brandTv.getText())) {
//            Toast.makeText(mContext, "品牌不能为空", Toast.LENGTH_SHORT).show();
//        } else if (TextUtils.isEmpty(trixTv.getText())) {
//            Toast.makeText(mContext, "车系不能为空", Toast.LENGTH_SHORT).show();
//        } else if (TextUtils.isEmpty(modelTv.getText())) {
//            Toast.makeText(mContext, "车型不能为空", Toast.LENGTH_SHORT).show();
//        }
        else if (TextUtils.isEmpty(colorTv.getText())) {
            Toast.makeText(mContext, "颜色不能为空", Toast.LENGTH_SHORT).show();
        } else if (cartype.equals("新车") && TextUtils.isEmpty(billPriceTv.getText())) {
            Toast.makeText(mContext, "开票价不能为空", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(firstPriceTv.getText())) {
            Toast.makeText(mContext, "首付款不能为空", Toast.LENGTH_SHORT).show();
        } else if (!checkFirstPriceValid()) {
            Toast.makeText(mContext, "首付款必须大于评估价30%", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(carLoanPriceTv.getText())) {
            Toast.makeText(mContext, "车辆贷款额不能为空", Toast.LENGTH_SHORT).show();
        } else if (Integer.valueOf(carLoanPriceTv.getText().toString()) == 0) {
            Toast.makeText(mContext, "车辆贷款额不能为0", Toast.LENGTH_LONG).show();
        } else if (!checkCarLoanPriceValid()) {
            Toast.makeText(mContext, "车辆贷款额必须小于评估价70%", Toast.LENGTH_SHORT).show();
        } else if (cartype.equals("二手车") && TextUtils.isEmpty(oldcar_addr_tv.getText())) {
            Toast.makeText(mContext, "二手车原上牌地不能为空", Toast.LENGTH_LONG).show();
        } else if (cartype.equals("二手车") && TextUtils.isEmpty(oldcar_dance_tv.getText())) {
            Toast.makeText(mContext, "二手车里程数不能为空", Toast.LENGTH_LONG).show();
        } else if (cartype.equals("二手车") && TextUtils.isEmpty(oldcar_guess_price_tv.getText())) {
            Toast.makeText(mContext, "二手车评估价不能为空", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(firstPriceTv.getText())) {
            Toast.makeText(mContext, "首付款不能为空", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(managementPriceTv.getText())) {
            Toast.makeText(mContext, "管理费不能为空", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(otherPriceTv.getText())) {
            Toast.makeText(mContext, "其他费用不能为空", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(loanBankTv.getText())) {
            Toast.makeText(mContext, "贷款银行不能为空", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(productTypeTv.getText())) {
            Toast.makeText(mContext, "产品类型不能为空", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(plateRegAddrTv.getText())) {
            Toast.makeText(mContext, "预计上牌地不能为空", Toast.LENGTH_SHORT).show();
        } else if (getPrice(otherPriceTv.getText().toString()) % 100 != 0) {
            Toast.makeText(mContext, "其他费用只能是整百", Toast.LENGTH_SHORT).show();
        } else if (cartype.equals("新车") && getPrice(firstPriceTv) + getPrice(carLoanPriceTv) != getPrice(billPriceTv)) {
            Toast.makeText(mContext, "首付款加车辆贷款额必须等于开票价", Toast.LENGTH_SHORT).show();
        } else if (Integer.valueOf(totalLoanPriceTv.getText().toString()) < 10000) {
            Toast.makeText(mContext, "总贷款额需多于10000元", Toast.LENGTH_LONG).show();
        } else if (cartype.equals("新车") && Integer.valueOf(totalLoanPriceTv.getText().toString()) > Integer.valueOf(billPriceTv.getText().toString()) * 0.8) {
            Toast.makeText(mContext, "总贷款额不能大于开票价的80%", Toast.LENGTH_LONG).show();
        } else if (cartype.equals("新车") && getPrice(billPriceTv) < getPrice(totalLoanPriceTv) + 1) {
            Toast.makeText(mContext, "车辆开票价要大于总贷款额", Toast.LENGTH_LONG).show();
        } else if (cartype.equals("二手车") && Integer.valueOf(totalLoanPriceTv.getText().toString()) > Integer.valueOf(oldcar_guess_price_tv.getText().toString()) * 0.7) {
            Toast.makeText(mContext, "总贷款额不能大于评估价的70%", Toast.LENGTH_LONG).show();
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
            String priceAndImage = SharedPrefsUtil.getInstance(mContext).getValue("priceAndImage", "");
            if ((priceAndImage == null || priceAndImage.isEmpty()) && ishadImg) {
                dialog.show();
                Log.e("SP1", SharedPrefsUtil.getInstance(mContext).getValue("priceAndImage", ""));
            } else {
                dialog.dismiss();
                look_guess_img_btn.setEnabled(true);
                SharedPrefsUtil.getInstance(mContext).remove("priceAndImage");
                Log.e("SP2", SharedPrefsUtil.getInstance(mContext).getValue("priceAndImage", ""));
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            colorTv.setFocusable(true);
            colorTv.setFocusableInTouchMode(true);
            colorTv.requestFocus();
        } else {
            colorTv.setFocusable(true);
            colorTv.setFocusableInTouchMode(true);
            colorTv.requestFocus();
        }
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

    public void getDlrInfo(Intent data) {
        getDlrListByTokenResp = (GetDlrListByTokenResp) data.getSerializableExtra("Dlr");
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
        mBrandList.clear();
        mBrandIndex = 0;
        brandTv.setText("");

        mTrixList.clear();
        mTrixIndex = 0;
        trixTv.setText("");

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
        oldcar_guess_price_tv.setEnabled(false);
        oldcar_dance_tv.setText("");
        oldcar_addr_tv.setText("");
        oldcar_addrtime_tv.setText("");

        managementPriceTv.setText("");
        totalLoanPriceTv.setText("");
        otherPriceTv.setText("");
        plateRegAddrTv.setText("");//上牌地选择
        loanPeriodsTv.setText("");//还款期限

        look_guess_img_btn.setEnabled(false);
        btn_reset.setEnabled(false);
        btn_fast_valuation.setEnabled(false);
    }

    private void selectDlrStore2() {

        Intent intent = new Intent(mContext, DlrStoreSelectActivity.class);
        intent.putExtra("vehicle_cond", "二手车");
        intent.putExtra("class", OrderCreateActivity.class);
        intent.putExtra("should_reset", isRestDlrinfo);//true表示重置该页面 默认false
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.pop_car_select_enter_anim, R.anim.stay);
        isRestDlrinfo = false;
    }
}
