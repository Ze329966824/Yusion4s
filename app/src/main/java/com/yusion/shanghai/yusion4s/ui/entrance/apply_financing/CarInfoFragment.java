package com.yusion.shanghai.yusion4s.ui.entrance.apply_financing;

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
import com.yusion.shanghai.yusion4s.bean.dlr.GetTrixResp;
import com.yusion.shanghai.yusion4s.bean.dlr.GetproductResp;
import com.yusion.shanghai.yusion4s.bean.order.submit.SubmitOrderReq;
import com.yusion.shanghai.yusion4s.car_select.CarSelectActivity;
import com.yusion.shanghai.yusion4s.car_select.DlrStoreSelectActivity;
import com.yusion.shanghai.yusion4s.event.ApplyFinancingFragmentEvent;
import com.yusion.shanghai.yusion4s.retrofit.api.DlrApi;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.settings.Settings;
import com.yusion.shanghai.yusion4s.ubt.UBT;
import com.yusion.shanghai.yusion4s.ubt.annotate.BindView;
import com.yusion.shanghai.yusion4s.ui.order.OrderCreateActivity;
import com.yusion.shanghai.yusion4s.utils.wheel.WheelViewUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
/**
 * {@link com.yusion.shanghai.yusion4s.ui.entrance.apply_financing.CarI
 * {@link CarInfoFragment#writeOtherPrice(View, boolean)}获取 ()}
 * 1.其他费用{@link com.yusion.shanghai.yusion4s.ui.entrance.apply_financing.CarInfoFragment#writeOtherPrice(android.view.View, boolean)}  }
 * addTextChange
 */

/**
 * Created by aa on 2017/8/9.
 */

public class CarInfoFragment extends BaseFragment {
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
    private int model_id;

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

    private boolean isRestCarinfo = true;
    private boolean isRestDlrinfo = true;
    private LinearLayout dlr_lin2;//用于选择经销商后是否显示下面的内容
    private TextView car_info_dlr_tv2;//显示下面的内容

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1://其他费用
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
                    changeFirstPriceByCode = false;
                    int sum = 0;
                    if (getPrice(firstPriceTv) > getPrice(billPriceTv)) {//大于开票价
                        Toast.makeText(mContext, "首付款不能大于开票价", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(mContext, "贷款总额不能大于开票价", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(mContext, "开票价不能大于厂商指导价", Toast.LENGTH_SHORT).show();
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


    /**
     * 其他费用获得焦点时候调用
     *
     * @param view
     * @param hasFocus
     */
    //// TODO: 2017/12/1 点击闪屏问题
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
                Log.e("TAG", "writeOtherPrice: 1");
                DlrApi.getOtherFeeLimit(mContext, carLoanPriceTv.getText().toString(), new OnItemDataCallBack<String>() {
                    @Override
                    public void onItemDataCallBack(String data) {
                        Log.e("TAG", "onItemDataCallBack: 2 " + data);
                        if (!TextUtils.isEmpty(data)) {
                            otherLimit = data;
                            Toast toast = Toast.makeText(mContext, "其他费用可输入最大金额为" + otherLimit, Toast.LENGTH_SHORT);
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
    private String oldCarcityJson;

    //上牌地选择
    private LinearLayout plateRegAddrLin;
    //还款期限选择
    private LinearLayout carInfoLoanPeriodsLin;

    //产品类型选择
    private LinearLayout carInfoProductTypeLin;

    //贷款银行选择
    private LinearLayout carInfoLoanBankLin;

    //是否管贷档案管理费选择
    private LinearLayout managementPriceLl;
    //车型选择
    private LinearLayout car_info_lin;
    private TextView car_info_tv;

    //车型选择
    private LinearLayout carInfoModelLin;

    //车系选择
    private LinearLayout carInfoTrixLin;

    //品牌选择
    private LinearLayout carInfoBrandLin;

    //经销商选择
    private LinearLayout carInfoDlrLin;
    private LinearLayout distributorLin;

    private LinearLayout personal_info_detail_home_address_lin;


    private View kaipiaojia_line;


    public static CarInfoFragment newInstance() {
        Bundle args = new Bundle();
        CarInfoFragment fragment = new CarInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_car_info, container, false);
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
        personal_info_detail_home_address_lin = (LinearLayout) view.findViewById(R.id.personal_info_detail_home_address_lin);//车辆开票价a
        kaipiaojia_line = view.findViewById(R.id.kaipiaojia_line);
        cartype = getActivity().getIntent().getStringExtra("car_type");
        carInfoNextBtn = (Button) view.findViewById(R.id.car_info_next_btn);
        car_info_lin = view.findViewById(R.id.car_info_lin);
        car_info_tv = view.findViewById(R.id.car_info_tv);
        dlr_lin2 = view.findViewById(R.id.dlr_lin2);
        car_info_dlr_tv2 = view.findViewById(R.id.car_info_dlr_tv2);

        /**
         * 进行经销商选择
         */
        carInfoDlrLin.setOnClickListener(v ->
                selectDlrStore2()
        );
        car_info_lin.setOnClickListener(v ->
                selectCarInfo()
        );
        //品牌选择
        carInfoBrandLin.setOnClickListener(v ->
                selectBrand()
        );
        //     车系选择
        carInfoTrixLin.setOnClickListener(v ->
                selectTrix()
        );
        // 车型选择
        carInfoModelLin.setOnClickListener(v ->
                selectModel()
        );
        //上牌地
        plateRegAddrLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPlateRegAddrLin();
            }
        });
        //档案管理费
        managementPriceLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickManagentPriceLl();
            }
        });
        //产品期限
        carInfoLoanPeriodsLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLoanPeriods();
            }
        });
        //选择银行
        carInfoLoanBankLin.setOnClickListener(v ->
                selectBank()
        );
        // 选择产品类型
        carInfoProductTypeLin.setOnClickListener(v ->
                selectProductType()
        );
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


        //贷款额度
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
                        req.dlr_id = mDlrList.get(mDlrIndex).dlr_id;
                        req.vehicle_model_id = model_id;
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
                        req.vehicle_price = billPriceTv.getText().toString();
                        req.plate_reg_addr = plateRegAddrTv.getText().toString();
                        EventBus.getDefault().post(ApplyFinancingFragmentEvent.showCreditInfo);
                    }
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

            DlrApi.getProductType(mContext, mLoanBankList.get(mLoanBankIndex).bank_id, mDlrList.get(mDlrIndex).dlr_id, cartype, new OnItemDataCallBack<GetproductResp>() {
                @Override
                public void onItemDataCallBack(GetproductResp resp) {
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
                }
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
            DlrApi.getLoanBank(mContext, mDlrList.get(mDlrIndex).dlr_id, resp -> {

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

    private void selectModel() {
        if (!TextUtils.isEmpty(trixTv.getText())) {
            DlrApi.getModel(mContext, mTrixList.get(mTrixIndex).trix_id, "新车", resp -> {
                if (resp != null && !resp.isEmpty()) {
                    mModelList = resp;
                    modelItems = new ArrayList<String>();
                    for (GetModelResp modelResp : resp) {
                        modelItems.add(modelResp.model_name);
                    }
                    WheelViewUtil.showWheelView(modelItems, mModelIndex, carInfoModelLin, modelTv, "请选择车型", (clickedView, selectedIndex) -> {
                        mModelIndex = selectedIndex;
                        mGuidePrice = (int) resp.get(mModelIndex).msrp;
                        min_reg_year = resp.get(mModelIndex).min_reg_year;
                        max_reg_year = resp.get(mModelIndex).max_reg_year;
                        guidePriceTv.setText(mGuidePrice + "");
                        billPriceTv.setEnabled(true);

                        clearExceptCarInfo();
                    });
                }
            });

        } else if (TextUtils.isEmpty(dlrTV.getText())) {
            Toast toast = Toast.makeText(mContext, "请您先完成经销商选择", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (TextUtils.isEmpty(trixTv.getText()) && !TextUtils.isEmpty(dlrTV.getText())) {
            Toast toast = Toast.makeText(mContext, "请您先完成车系选择", Toast.LENGTH_LONG);
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
    }

    private void selectTrix() {
        if (!TextUtils.isEmpty(brandTv.getText()) && !TextUtils.isEmpty(dlrTV.getText())) {
            DlrApi.getTrix(mContext, mBrandList.get(mBrandIndex).brand_id, resp -> {
                mTrixList = resp;
                trixItems = new ArrayList<String>();
                for (GetTrixResp trixResp : resp) {
                    trixItems.add(trixResp.trix_name);
                }
                WheelViewUtil.showWheelView(trixItems, mTrixIndex, carInfoBrandLin, trixTv, "请选择车系", (clickedView, selectedIndex) -> {
                    mTrixIndex = selectedIndex;

                    mModelList.clear();
                    mModelIndex = 0;
                    modelTv.setText("");
                    mGuidePrice = 0;
                    guidePriceTv.setText("");
                    clearExceptCarInfo();
                });
            });
        } else if (TextUtils.isEmpty(dlrTV.getText())) {
            Toast toast = Toast.makeText(mContext, "请您先完成经销商选择", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (TextUtils.isEmpty(brandTv.getText()) && !TextUtils.isEmpty(dlrTV.getText())) {
            Toast toast = Toast.makeText(mContext, "请您先完成品牌选择", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    private void selectBrand() {
        if (!TextUtils.isEmpty(dlrTV.getText())) {
            Log.e("!!!--经销商-----", dlrTV.getText().toString());
            DlrApi.getBrand(mContext, mDlrList.get(mDlrIndex).dlr_id, resp -> {
                mBrandList = resp;
                brandItems = new ArrayList<String>();
                for (GetBrandResp item : resp) {
                    brandItems.add(item.brand_name);
                }
                Log.e("!!!----品牌---", brandItems.toString());
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
            });
        } else {
            Toast toast = Toast.makeText(mContext, "请您先完成经销商选择", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    private void selectCarInfo() {
        if (TextUtils.isEmpty(dlrTV.getText())) {
            Toast toast = Toast.makeText(mContext, "请您先完成经销商选择", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            Intent intent = new Intent(mContext, CarSelectActivity.class);
            intent.putExtra("vehicle_cond", "新车");
            intent.putExtra("class", OrderCreateActivity.class);
            intent.putExtra("dlr_id", mDlrList.get(mDlrIndex).dlr_id);
            intent.putExtra("should_reset", isRestCarinfo);//true表示重置该页面 默认false
            startActivity(intent);
            isRestCarinfo = false;
        }
    }


    public void getCarInfo(Intent data) {
        GetModelResp modleResp = (GetModelResp) data.getSerializableExtra("modleResp");
        model_id = modleResp.model_id;
        car_info_tv.setText(modleResp.model_name);

        mGuidePrice = (int) modleResp.msrp;
        guidePriceTv.setText(mGuidePrice + "");


        clearExceptCarInfo();
        isRestCarinfo = false;
        billPriceTv.setEnabled(true);
    }

    private void selectDlrStore2() {

        Intent intent = new Intent(mContext, DlrStoreSelectActivity.class);
        intent.putExtra("vehicle_cond", "新车");
        intent.putExtra("class", OrderCreateActivity.class);
        //intent.putExtra("dlr_id", mDlrList.get(mDlrIndex).dlr_id);
        intent.putExtra("should_reset", isRestDlrinfo);//true表示重置该页面 默认false
        startActivity(intent);
        isRestDlrinfo = false;
    }

    private void selectDlrStore() {
        DlrApi.getDlrListByToken(mContext, resp -> {
            if (resp != null && !resp.isEmpty()) {
                mDlrList = resp;
                dlrItems = new ArrayList<String>();
                for (GetDlrListByTokenResp item : resp) {
                    dlrItems.add(item.dlr_nm);
                }
                //dlrTV  经销商显示的textview
                WheelViewUtil.showWheelView(dlrItems, mDlrIndex, carInfoDlrLin, dlrTV, "请选择经销商", (clickedView, selectedIndex) -> {
                    mDlrIndex = selectedIndex;
                    car_info_tv.setText("");
                    isRestCarinfo = true;

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

    private void clickManagentPriceLl() {
        if (TextUtils.isEmpty(dlrTV.getText())) {
            Toast toast = Toast.makeText(mContext, "请您先完成经销商选择", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
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

    private void clickPlateRegAddrLin() {
        if (TextUtils.isEmpty(productTypeTv.getText())) {
            Toast toast = Toast.makeText(mContext, "请选择产品类型", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }
        if (!TextUtils.isEmpty(cityJson)) {
            WheelViewUtil.showCityWheelView(CarInfoFragment.this.getClass().getSimpleName(), plateRegAddrLin, plateRegAddrTv, "请选择", new WheelViewUtil.OnCitySubmitCallBack() {
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
            managementPrice = mDlrList.get(mDlrIndex).management_fee.get(mManagementPriceIndex);
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
            Toast.makeText(mContext, "首付款必须大于开票价20%", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(carLoanPriceTv.getText())) {
            Toast.makeText(mContext, "车辆贷款额不能为空", Toast.LENGTH_SHORT).show();
        } else if (Integer.valueOf(carLoanPriceTv.getText().toString()) == 0) {
            Toast.makeText(mContext, "车辆贷款额不能为0", Toast.LENGTH_LONG).show();
        } else if (!checkCarLoanPriceValid()) {
            Toast.makeText(mContext, "车辆贷款额必须小于开票价80%", Toast.LENGTH_SHORT).show();
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
        if (priceStr.contains(".")) {
            Toast.makeText(mContext, "数据不合规", Toast.LENGTH_SHORT).show();
        }
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
}
