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
import com.yusion.shanghai.yusion4s.bean.dlr.GetRawCarInfoResp;
import com.yusion.shanghai.yusion4s.bean.dlr.GetTrixResp;
import com.yusion.shanghai.yusion4s.bean.dlr.GetproductResp;
import com.yusion.shanghai.yusion4s.bean.order.submit.SubmitOrderReq;
import com.yusion.shanghai.yusion4s.event.ApplyFinancingFragmentEvent;
import com.yusion.shanghai.yusion4s.glide.StatusImageRel;
import com.yusion.shanghai.yusion4s.retrofit.api.DlrApi;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.settings.Settings;
import com.yusion.shanghai.yusion4s.ubt.UBT;
import com.yusion.shanghai.yusion4s.ubt.annotate.BindView;
import com.yusion.shanghai.yusion4s.ui.ApplyFinancingFragment;
import com.yusion.shanghai.yusion4s.utils.wheel.WheelViewUtil;

import junit.framework.Test;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aa on 2017/8/9.
 */

public class CarInfoFragment extends BaseFragment {

    public static int DELAY_MILLIS;
    private String otherLimit;

    private List<GetLoanBankResp> mLoanBankList = new ArrayList<>();

    //private List<GetproductResp> mProductList = new ArrayList<>();
    //private GetproductResp.product_list mProductList = new GetproductResp.product_list();
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

    private int mNperIndex = 0;

    private int mChangeLoanAndFirstPriceCount = 0;
    private boolean ischangeBillPriceBySys = false;

    private boolean changeCarLoanByCode = false;

    private boolean changeFirstPriceByCode = false;

    private boolean otherPriceChange = true;
    private boolean firstPriceChange = true;
    private boolean carLoanPriceChange = true;

    private boolean billPriceChange = true;

    private boolean isChangeCarInfoChange;

    private int sum = 0;

    private String upNumberCity;
    private Button carInfoNextBtn;

    private boolean isChoose = false;


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
                            //sum3 = Integer.valueOf(carLoanPriceTv.getText().toString());
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
                            //sum4 = getRounding(billPriceTv);
                            sum4 = Integer.valueOf(billPriceTv.getText().toString());
                            billPriceTv.setText(sum4 + "");
                            billPriceTv.setSelection(String.valueOf(sum4).length());

                        } else {
                            billPriceTv.setText(billPriceTv.getText());
                            billPriceTv.setSelection(billPriceTv.getText().toString().length());
                        }
                    }
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


    //    @BindView(id = R.id.car_info_plate_reg_addr_lin, widgetName = "上牌地选择", onClick = "selectplateRegAddr")
    private LinearLayout plateRegAddrLin;

    //上牌地选择
//    private void selectplateRegAddr(View view) {
//        if (TextUtils.isEmpty(productTypeTv.getText())) {
//            Toast toast = Toast.makeText(mContext, "请选择产品类型", Toast.LENGTH_LONG);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();
//            return;
//        }
//        if (!TextUtils.isEmpty(cityJson)) {
//            WheelViewUtil.showCityWheelView(CarInfoFragment.this.getClass().getSimpleName(), plateRegAddrLin, plateRegAddrTv, "请选择", new WheelViewUtil.OnCitySubmitCallBack() {
//                @Override
//                public void onCitySubmitCallBack(View clickedView, String city) {
//                    if (!TextUtils.isEmpty(plateRegAddrTv.getText())) {
//                        carInfoNextBtn.setEnabled(true);
//                    }
//                }
//            }, cityJson);
//
//        } else {
//            return;
//        }
//    }

    //    @BindView(id = R.id.car_info_loan_periods_lin, widgetName = "还款期限选择", onClick = "selectLoanPeriods")
    private LinearLayout carInfoLoanPeriodsLin;

    //还款期限选择
//    private void selectLoanPeriods(View view) {
//        if (TextUtils.isEmpty(productTypeTv.getText())) {
//            Toast.makeText(mContext, "请先选择产品类型", Toast.LENGTH_LONG).show();

//        } else {
//            WheelViewUtil.showWheelView(mProductList.get(mProductTypeIndex).nper_list, mLoanPeriodsIndex, carInfoLoanPeriodsLin, loanPeriodsTv, "请选择还款期限", new WheelViewUtil.OnSubmitCallBack() {
//                @Override
//                public void onSubmitCallBack(View clickedView, int selectedIndex) {
//                    mLoanPeriodsIndex = selectedIndex;
//                }
//            });
//        }
//    }

    //    @BindView(id = R.id.car_info_product_type_lin, widgetName = "产品类型选择", onClick = "selectProductType")
    private LinearLayout carInfoProductTypeLin;

    //产品类型选择
//    private void selectProductType(View view) {
//        if (!TextUtils.isEmpty(loanBankTv.getText())) {
//
//            DlrApi.getProductType(mContext, mLoanBankList.get(mLoanBankIndex).bank_id, mDlrList.get(mDlrIndex).dlr_id, new OnItemDataCallBack<GetproductResp>() {
//                @Override
//                public void onItemDataCallBack(GetproductResp resp) {
//                    if (resp == null) {
//                        return;
//                    }
//                    cityJson = resp.support_area.toString();
////                        Log.e("TAG", "onItemDataCallBack: " + cityJson);
//
//                    mProductList = resp.product_list;
//
//                    //mProductList.get()
//
//                    List<String> items = new ArrayList<String>();
//
//                    //List<Integer> nper_list = new ArrayList<Integer>();
//
//                    for (GetproductResp.ProductListBean product_list : resp.product_list) {
//                        items.add(product_list.name);
//                    }
//
//                    WheelViewUtil.showWheelView(items, mProductTypeIndex, carInfoProductTypeLin, productTypeTv, "请选择产品类型", new WheelViewUtil.OnSubmitCallBack() {
//                        @Override
//                        public void onSubmitCallBack(View clickedView, int selectedIndex) {
//                            mProductTypeIndex = selectedIndex;
//                            loanPeriodsTv.setText(null);
//                            mLoanPeriodsIndex = 0;
//                        }
//                    });
//                }
//            });
//
//        } else if (TextUtils.isEmpty(dlrTV.getText())) {
//            Toast toast = Toast.makeText(mContext, "请您先完成门店选择", Toast.LENGTH_LONG);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();
//        } else if (TextUtils.isEmpty(loanBankTv.getText()) && !TextUtils.isEmpty(dlrTV.getText())) {
//            Toast toast = Toast.makeText(mContext, "请您先完成贷款银行选择", Toast.LENGTH_LONG);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();
//        }
//    }

    //贷款银行选择
//    @BindView(id = R.id.car_info_loan_bank_lin, widgetName = "贷款银行选择", onClick = "selectLoanBank")
    private LinearLayout carInfoLoanBankLin;

//    private void selectLoanBank(View view) {
//        if (!TextUtils.isEmpty(dlrTV.getText())) {
//            DlrApi.getLoanBank(mContext, mDlrList.get(mDlrIndex).dlr_id, resp -> {
//
//                mLoanBankList = resp;//银行列表
//                List<String> items = new ArrayList<String>();
//                for (GetLoanBankResp getLoanBankResp : resp) {
//                    items.add(getLoanBankResp.name);
//                }
//
//                WheelViewUtil.showWheelView(items, mLoanBankIndex, carInfoLoanBankLin, loanBankTv, "请选择贷款银行", (clickedView, selectedIndex) -> mLoanBankIndex = selectedIndex);
//
//            });
//        } else {
//            Toast toast = Toast.makeText(mContext, "请您先完成门店选择", Toast.LENGTH_LONG);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();
//        }
//    }

    //    @BindView(id = R.id.car_info_management_price_lin, widgetName = "是否管贷档案管理费选择", onClick = "selectManagementPrice")
    private LinearLayout managementPriceLl;

    //是否管贷档案管理费选择
//    private void selectManagementPrice(View view) {
//        if (TextUtils.isEmpty(dlrTV.getText())) {
//            Toast toast = Toast.makeText(mContext, "请您先完成门店选择", Toast.LENGTH_LONG);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//        } else {//(clickedView, selectedIndex) -> mManagementPriceIndex = selectedIndex)
//            WheelViewUtil.showWheelView(mDlrList.get(mDlrIndex).management_fee, mManagementPriceIndex, managementPriceLl, managementPriceTv, "请选择档案管理费", new WheelViewUtil.OnSubmitCallBack() {
//                @Override
//                public void onSubmitCallBack(View clickedView, int selectedIndex) {
//                    mManagementPriceIndex = selectedIndex;
//                    isChoose = true;
//                    totalPrice();
//                }
//            });
//        }
//
//    }

    //    @BindView(id = R.id.car_info_model_lin, widgetName = "车型选择", onClick = "selectModel")
    private LinearLayout carInfoModelLin;

    //车型选择
//    private void selectModel(View view) {
//        if (!TextUtils.isEmpty(trixTv.getText())) {
//            DlrApi.getModel(mContext, mTrixList.get(mTrixIndex).trix_id, resp -> {
//                if (resp != null && !resp.isEmpty()) {
//                    mModelList = resp;
//                    List<String> items = new ArrayList<>();
//                    for (GetModelResp modelResp : resp) {
//                        items.add(modelResp.model_name);
//                    }
//                    WheelViewUtil.showWheelView(items, mModelIndex, carInfoModelLin, modelTv, "请选择车型", (clickedView, selectedIndex) -> {
//                        mModelIndex = selectedIndex;
//                        mGuidePrice = (int) resp.get(mModelIndex).msrp;
//                        guidePriceTv.setText(mGuidePrice + "");
//                        billPriceTv.setEnabled(true);
//                        //otherPriceTv.setHint("整百且小于" + mDlrList.get(mDlrIndex).other_fee);
//
//                        mLoanBankList.clear();
//                        mLoanBankIndex = 0;
//                        loanBankTv.setText(null);
//                        mProductTypeIndex = 0;
//                        productTypeTv.setText(null);
//
//                        billPriceTv.setText("");
//                    });
//                }
//            });
//
//        } else if (TextUtils.isEmpty(dlrTV.getText())) {
//            Toast toast = Toast.makeText(mContext, "请您先完成门店选择", Toast.LENGTH_LONG);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();
//        } else if (TextUtils.isEmpty(trixTv.getText()) && !TextUtils.isEmpty(dlrTV.getText())) {
//            Toast toast = Toast.makeText(mContext, "请您先完成车系选择", Toast.LENGTH_LONG);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();
//        }
//    }

    //    @BindView(id = R.id.car_info_trix_lin, widgetName = "车系选择", onClick = "selectTrix")
    private LinearLayout carInfoTrixLin;

    //车系选择
//    private void selectTrix(View view) {
//        if (!TextUtils.isEmpty(brandTv.getText()) && !TextUtils.isEmpty(dlrTV.getText())) {
//            DlrApi.getTrix(mContext, mBrandList.get(mBrandIndex).brand_id, resp -> {
//                mTrixList = resp;
//                List<String> items = new ArrayList<>();
//                for (GetTrixResp trixResp : resp) {
//                    items.add(trixResp.trix_name);
//                }
//                WheelViewUtil.showWheelView(items, mTrixIndex, carInfoBrandLin, trixTv, "请选择车系", (clickedView, selectedIndex) -> {
//                    mTrixIndex = selectedIndex;
//
//                    mModelList.clear();
//                    mModelIndex = 0;
//                    modelTv.setText("");
//
//                    mGuidePrice = 0;
//                    guidePriceTv.setText("");
//
//                    mLoanBankList.clear();
//                    mLoanBankIndex = 0;
//                    loanBankTv.setText(null);
//
//                    mProductTypeIndex = 0;
//                    productTypeTv.setText(null);
//
//                    billPriceTv.setText("");
//                });
//            });
//        } else if (TextUtils.isEmpty(dlrTV.getText())) {
//            Toast toast = Toast.makeText(mContext, "请您先完成门店选择", Toast.LENGTH_LONG);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();
//        } else if (TextUtils.isEmpty(brandTv.getText()) && !TextUtils.isEmpty(dlrTV.getText())) {
//            Toast toast = Toast.makeText(mContext, "请您先完成品牌选择", Toast.LENGTH_LONG);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();
//        }
//    }

    //    @BindView(id = R.id.car_info_brand_lin, widgetName = "品牌选择", onClick = "selectBrand")
    private LinearLayout carInfoBrandLin;

    //品牌选择
//    private void selectBrand(View view) {
//        if (!TextUtils.isEmpty(dlrTV.getText())) {
//            DlrApi.getBrand(mContext, mDlrList.get(mDlrIndex).dlr_id, resp -> {
//                mBrandList = resp;
//                List<String> items = new ArrayList<>();
//                for (GetBrandResp item : resp) {
//                    items.add(item.brand_name);
//                }
//                WheelViewUtil.showWheelView(items, mBrandIndex, carInfoBrandLin, brandTv, "请选择品牌", (clickedView, selectedIndex) -> {
//                    mBrandIndex = selectedIndex;
//
//                    mTrixList.clear();
//                    mTrixIndex = 0;
//                    trixTv.setText("");
//
//                    mModelList.clear();
//                    mModelIndex = 0;
//                    modelTv.setText("");
//
//                    mGuidePrice = 0;
//                    guidePriceTv.setText("");
//
//                    mLoanBankList.clear();
//                    mLoanBankIndex = 0;
//                    loanBankTv.setText(null);
//
//                    mProductTypeIndex = 0;
//                    productTypeTv.setText(null);
//
//                    billPriceTv.setText("");
//                });
//            });
//        } else {
//            Toast toast = Toast.makeText(mContext, "请您先完成门店选择", Toast.LENGTH_LONG);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();
//        }
//    }

    //    @BindView(id = R.id.car_info_dlr_lin, widgetName = "门店选择", onClick = "selectDlr")
    private LinearLayout carInfoDlrLin;

    //门店选择
//    private void selectDlr(View view) {
//        DlrApi.getDlrListByToken(mContext, resp -> {
//            if (resp != null && !resp.isEmpty()) {
//                mDlrList = resp;
//                List<String> items = new ArrayList<>();
//                for (GetDlrListByTokenResp item : resp) {
//                    items.add(item.dlr_nm);
//                }
//
//                //dlrTV  门店显示的textview
//                WheelViewUtil.showWheelView(items, mDlrIndex, carInfoDlrLin, dlrTV, "请选择门店", (clickedView, selectedIndex) -> {
//                    mDlrIndex = selectedIndex;
//
//                    mBrandList.clear();
//                    mBrandIndex = 0;
//                    brandTv.setText("");//厂商指导价
//
//                    mTrixList.clear();
//                    mTrixIndex = 0;
//                    trixTv.setText("");//选择车型
//
//                    mModelList.clear();
//                    mModelIndex = 0;
//                    modelTv.setText("");
//
//                    mGuidePrice = 0;
//                    guidePriceTv.setText("");
//
//                    mLoanBankList.clear();
//                    mLoanBankIndex = 0;
//                    loanBankTv.setText(null);
//
//                    mProductTypeIndex = 0;
//                    productTypeTv.setText(null);
//
//                    billPriceTv.setText("");
//
//
//                    mManagementPriceIndex = 0;
////                mDlrList.clear();
//                    managementPriceTv.setText("");
//                    totalLoanPriceTv.setText("");
//                    otherPriceTv.setText("");
//                    plateRegAddrTv.setText("");//上牌地选择
//                    loanPeriodsTv.setText("");//还款期限
//
//                });
//            }
//        });
//    }

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
        // TODO: 2017/10/24 记得删除
        Button button = (Button) view.findViewById(R.id.change);
        button.setOnClickListener(v -> {
            startActivity(new Intent(mContext, Alter.class));
        });

        DELAY_MILLIS = Yusion4sApp.getConfigResp().DELAY_MILLIS;

        totalLoanPriceTv = (TextView) view.findViewById(R.id.car_info_total_loan_price_tv);//总贷款费用
//        otherPriceTv = (EditText) view.findViewById(R.id.car_info_other_price_tv);//其他费用
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
        carInfoBrandLin = (LinearLayout) view.findViewById(R.id.car_info_brand_lin);
        carInfoTrixLin = (LinearLayout) view.findViewById(R.id.car_info_trix_lin);
        carInfoModelLin = (LinearLayout) view.findViewById(R.id.car_info_model_lin);
        carInfoLoanBankLin = (LinearLayout) view.findViewById(R.id.car_info_loan_bank_lin);
        carInfoProductTypeLin = (LinearLayout) view.findViewById(R.id.car_info_product_type_lin);

        carInfoNextBtn = (Button) view.findViewById(R.id.car_info_next_btn);

        carInfoLoanPeriodsLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });

        /**
         * 进行门店选择
         */
//        view.findViewById(R.id.car_info_dlr_lin)
        carInfoDlrLin.setOnClickListener(v -> DlrApi.getDlrListByToken(mContext, resp -> {
            if (resp != null && !resp.isEmpty()) {
                mDlrList = resp;
                dlrItems = new ArrayList<String>();
                for (GetDlrListByTokenResp item : resp) {
                    dlrItems.add(item.dlr_nm);
                }

                //dlrTV  门店显示的textview
                WheelViewUtil.showWheelView(dlrItems, mDlrIndex, carInfoDlrLin, dlrTV, "请选择门店", (clickedView, selectedIndex) -> {
                    mDlrIndex = selectedIndex;

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

                    managementPriceTv.setText("");
                    totalLoanPriceTv.setText("");
                    otherPriceTv.setText("");
                    plateRegAddrTv.setText("");//上牌地选择
                    loanPeriodsTv.setText("");//还款期限

                });
            }
        }));


        //上牌地
        plateRegAddrLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
        managementPriceLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(dlrTV.getText())) {
                    Toast toast = Toast.makeText(mContext, "请您先完成门店选择", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                } else {//(clickedView, selectedIndex) -> mManagementPriceIndex = selectedIndex)
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
        });

//        otherPriceTv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    otherPriceTv.setHint("*金额取整到百位,如2300");
//                    otherPriceTv.setHintTextColor(Color.parseColor("#ed9121"));
//                    if (TextUtils.isEmpty(carLoanPriceTv.getText())) {
//                        Toast toast = Toast.makeText(mContext, "请输入车辆贷款额", Toast.LENGTH_SHORT);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
//                        otherPriceTv.setHint("请输入");
//                        otherPriceTv.setHintTextColor(Color.parseColor("#d1d1d1"));
//                        otherPriceTv.setEnabled(false);
//                    } else {
//                        otherLimit = "";
//                        DlrApi.getOtherFeeLimit(mContext, carLoanPriceTv.getText().toString(), new OnItemDataCallBack<String>() {
//                            @Override
//                            public void onItemDataCallBack(String data) {
//                                if (!TextUtils.isEmpty(data)) {
//                                    otherLimit = data;
//                                    Toast toast = Toast.makeText(mContext, "其他费用可输入最大金额为" + otherLimit, Toast.LENGTH_SHORT);
//                                    toast.setGravity(Gravity.CENTER, 0, 0);
//                                    toast.show();
//                                }
//                            }
//                        });
//
////                        if (Integer.valueOf(carLoanPriceTv.getText().toString()) < 50000) {
////
////                            Toast toast = Toast.makeText(mContext, "其他费用可输入最大金额为3000", Toast.LENGTH_LONG);
////                            toast.setGravity(Gravity.CENTER, 0, 0);
////                            toast.show();
////                        } else {
////                            Toast toast = Toast.makeText(mContext, "其他费用可输入最大金额为5000", Toast.LENGTH_LONG);
////                            toast.setGravity(Gravity.CENTER, 0, 0);
////                            toast.show();
////                        }
//                    }
//                } else {
//                    otherPriceTv.setHint("请输入");
//                    otherPriceTv.setHintTextColor(Color.parseColor("#d1d1d1"));
//                }
//            }
//        });

        carInfoBrandLin.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(dlrTV.getText())) {
                Log.e("!!!--门店-----", dlrTV.getText().toString());
                DlrApi.getBrand(mContext, mDlrList.get(mDlrIndex).dlr_id, resp -> {
                    mBrandList = resp;
                    brandItems = new ArrayList<String>();
                    for (GetBrandResp item : resp) {
                        brandItems.add(item.brand_name);
                    }
                    Log.e("!!!----品牌---", brandItems.toString());
                    WheelViewUtil.showWheelView(brandItems, mBrandIndex, carInfoBrandLin, brandTv, "请选择品牌", (clickedView, selectedIndex) -> {
                        mBrandIndex = selectedIndex;
                        // Log.e("sss", String.valueOf(mBrandIndex));
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
                    });
                });
            } else {
                Toast toast = Toast.makeText(mContext, "请您先完成门店选择", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });

//        view.findViewById(R.id.car_info_trix_lin)
        carInfoTrixLin.setOnClickListener(v -> {
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

                        mLoanBankList.clear();
                        mLoanBankIndex = 0;
                        loanBankTv.setText(null);

                        mProductTypeIndex = 0;
                        productTypeTv.setText(null);

                        billPriceTv.setText("");
                    });
                });
            } else if (TextUtils.isEmpty(dlrTV.getText())) {
                Toast toast = Toast.makeText(mContext, "请您先完成门店选择", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else if (TextUtils.isEmpty(brandTv.getText()) && !TextUtils.isEmpty(dlrTV.getText())) {
                Toast toast = Toast.makeText(mContext, "请您先完成品牌选择", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }

        });
        // view.findViewById(R.id.car_info_model_lin)
        carInfoModelLin.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(trixTv.getText())) {
                DlrApi.getModel(mContext, mTrixList.get(mTrixIndex).trix_id, resp -> {
                    if (resp != null && !resp.isEmpty()) {
                        mModelList = resp;
                        modelItems = new ArrayList<String>();
                        for (GetModelResp modelResp : resp) {
                            modelItems.add(modelResp.model_name);
                        }
                        WheelViewUtil.showWheelView(modelItems, mModelIndex, carInfoModelLin, modelTv, "请选择车型", (clickedView, selectedIndex) -> {
                            mModelIndex = selectedIndex;
                            mGuidePrice = (int) resp.get(mModelIndex).msrp;
                            guidePriceTv.setText(mGuidePrice + "");
                            billPriceTv.setEnabled(true);
                            //otherPriceTv.setHint("整百且小于" + mDlrList.get(mDlrIndex).other_fee);

                            mLoanBankList.clear();
                            mLoanBankIndex = 0;
                            loanBankTv.setText(null);
                            mProductTypeIndex = 0;
                            productTypeTv.setText(null);

                            billPriceTv.setText("");
                        });
                    }
                });

            } else if (TextUtils.isEmpty(dlrTV.getText())) {
                Toast toast = Toast.makeText(mContext, "请您先完成门店选择", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else if (TextUtils.isEmpty(trixTv.getText()) && !TextUtils.isEmpty(dlrTV.getText())) {
                Toast toast = Toast.makeText(mContext, "请您先完成车系选择", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });

        //billPriceTv 车辆开票价
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

//        view.findViewById(R.id.car_info_loan_bank_lin)
        carInfoLoanBankLin.setOnClickListener(v -> {//选择银行列表
            if (!TextUtils.isEmpty(dlrTV.getText())) {
                DlrApi.getLoanBank(mContext, mDlrList.get(mDlrIndex).dlr_id, resp -> {

                    mLoanBankList = resp;//银行列表
                    List<String> items = new ArrayList<String>();
                    for (GetLoanBankResp getLoanBankResp : resp) {
                        items.add(getLoanBankResp.name);
                    }

                    WheelViewUtil.showWheelView(items, mLoanBankIndex, carInfoLoanBankLin, loanBankTv, "请选择贷款银行", (clickedView, selectedIndex) -> mLoanBankIndex = selectedIndex);

                });
            } else {
                Toast toast = Toast.makeText(mContext, "请您先完成门店选择", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
//        view.findViewById(R.id.car_info_product_type_lin)
        carInfoProductTypeLin.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(loanBankTv.getText())) {

                DlrApi.getProductType(mContext, mLoanBankList.get(mLoanBankIndex).bank_id, mDlrList.get(mDlrIndex).dlr_id, new OnItemDataCallBack<GetproductResp>() {
                    @Override
                    public void onItemDataCallBack(GetproductResp resp) {
                        if (resp == null) {
                            return;
                        }
                        cityJson = resp.support_area.toString();
//                        Log.e("TAG", "onItemDataCallBack: " + cityJson);

                        mProductList = resp.product_list;

                        //mProductList.get()

                        List<String> items = new ArrayList<String>();

                        //List<Integer> nper_list = new ArrayList<Integer>();

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
                Toast toast = Toast.makeText(mContext, "请您先完成门店选择", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else if (TextUtils.isEmpty(loanBankTv.getText()) && !TextUtils.isEmpty(dlrTV.getText())) {
                Toast toast = Toast.makeText(mContext, "请您先完成贷款银行选择", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
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
                        /*
                        if (getPrice(carLoanPriceTv) > getPrice(billPriceTv)) {
                            Toast.makeText(mContext, "贷款总额不能大于开票价", Toast.LENGTH_SHORT).show();
                            changeCarLoanByCode = false;
                            carLoanPriceTv.setText(getPrice(billPriceTv) + "");
                        } else {
                            changeFirstPriceByCode = true;
                            firstPriceTv.setText(getPrice(billPriceTv) - getPrice(carLoanPriceTv) + "");
                        }
                        */

                    }
                }
                otherPriceTv.setEnabled(true);
                totalPrice();
            }
        });

        firstPriceTv.setEnabled(false);
        //车辆首付款
        /*
        firstPriceTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    handler.removeMessages(2);
                    if (changeFirstPriceByCode) {
                        //开票价改变导致
                        changeFirstPriceByCode = false;
                    } else {
                        //用户输入的
                        changeCarLoanByCode = true;
                        carLoanPriceTv.setText(getPrice(billPriceTv) + "");
                    }
                } else {//判断合法性 //改变原因是因为贷款额发生改变

                    if (changeFirstPriceByCode) {
                        //贷款总额改变导致的
                        changeFirstPriceByCode = false;
                    } else {
                        //用户输入的
                        if (firstPriceChange) {
                            if (handler.hasMessages(2)) {
                                handler.removeMessages(2);
                            }
                            handler.sendEmptyMessageDelayed(2, DELAY_MILLIS);
                        } else {
                            firstPriceChange = true;
                        }
                        changeCarLoanByCode = true;
                        carLoanPriceTv.setText(getPrice(billPriceTv) - getPrice(firstPriceTv) + "");

//
//                        if (getPrice(firstPriceTv) > getPrice(billPriceTv)) {
//                            Toast.makeText(mContext, "首付款不能大于开票价", Toast.LENGTH_SHORT).show();
//                            changeFirstPriceByCode = false;
//                            firstPriceTv.setText(getPrice(billPriceTv) + "");
//                        } else {
//                            changeCarLoanByCode = true;
//                            carLoanPriceTv.setText(getPrice(billPriceTv) - getPrice(firstPriceTv) + "");
//                        }
//

                    }
                }
                totalPrice();
            }
        });
        */


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
                if (hasFocus) {
                    v.clearFocus();
                    if (Settings.isShameData) {
                        EventBus.getDefault().post(ApplyFinancingFragmentEvent.showCreditInfo);
                    } else if (checkCanNextStep()) {

                        SubmitOrderReq req = ((ApplyFinancingFragment) getParentFragment()).req;

                        req.dlr_id = mDlrList.get(mDlrIndex).dlr_id;
                        req.vehicle_model_id = mModelList.get(mModelIndex).model_id;

                        req.vehicle_color = colorTv.getText().toString();
                        req.vehicle_price = billPriceTv.getText().toString();
                        req.vehicle_down_payment = firstPriceTv.getText().toString();
                        req.vehicle_loan_amt = carLoanPriceTv.getText().toString();
                        req.loan_amt = totalLoanPriceTv.getText().toString();
                        req.management_fee = managementPriceTv.getText().toString();
                        req.other_fee = otherPriceTv.getText().toString();

                        req.bank_id = mLoanBankList.get(mLoanBankIndex).bank_id;
                        //req.product_id = mProductList.get(mProductTypeIndex).getProduct_id();
                        req.product_id = mProductList.get(mProductTypeIndex).product_id;
                        req.nper = Integer.valueOf(loanPeriodsTv.getText().toString());

                        //req.product_type = productTypeTv.getText().toString();

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

                    SubmitOrderReq req = ((ApplyFinancingFragment) getParentFragment()).req;

                    req.dlr_id = mDlrList.get(mDlrIndex).dlr_id;
                    req.vehicle_model_id = mModelList.get(mModelIndex).model_id;

                    req.vehicle_color = colorTv.getText().toString();
                    req.vehicle_price = billPriceTv.getText().toString();
                    req.vehicle_down_payment = firstPriceTv.getText().toString();
                    req.vehicle_loan_amt = carLoanPriceTv.getText().toString();
                    req.loan_amt = totalLoanPriceTv.getText().toString();
                    req.management_fee = managementPriceTv.getText().toString();
                    req.other_fee = otherPriceTv.getText().toString();

                    req.bank_id = mLoanBankList.get(mLoanBankIndex).bank_id;
                    //req.product_id = mProductList.get(mProductTypeIndex).getProduct_id();
                    req.product_id = mProductList.get(mProductTypeIndex).product_id;
                    req.nper = Integer.valueOf(loanPeriodsTv.getText().toString());

                    //req.product_type = productTypeTv.getText().toString();

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

    private void totalPrice() {
        Integer managementPrice = 0;
        Integer carLoanPrice = getPrice(carLoanPriceTv.getText().toString());
        //Integer managementPrice = getPrice(managementPriceTv.getText().toString());
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
            Toast.makeText(mContext, "门店不能为空", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(brandTv.getText())) {
            Toast.makeText(mContext, "品牌不能为空", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(trixTv.getText())) {
            Toast.makeText(mContext, "车系不能为空", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(modelTv.getText())) {
            Toast.makeText(mContext, "车型不能为空", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(colorTv.getText())) {
            Toast.makeText(mContext, "颜色不能为空", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(billPriceTv.getText())) {
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
        } else if (TextUtils.isEmpty(loanPeriodsTv.getText())) {
            Toast.makeText(mContext, "还款期限不能为空", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(productTypeTv.getText())) {
            Toast.makeText(mContext, "产品类型不能为空", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(plateRegAddrTv.getText())) {
            Toast.makeText(mContext, "预计上牌地不能为空", Toast.LENGTH_SHORT).show();
        }
//        else if (getPrice(otherPriceTv.getText().toString()) > mDlrList.get(mDlrIndex).other_fee) {
//            Toast.makeText(mContext, "其他费用不能大于" + mDlrList.get(mDlrIndex).other_fee, Toast.LENGTH_SHORT).show();
//        }
        else if (getPrice(otherPriceTv.getText().toString()) % 100 != 0) {
            Toast.makeText(mContext, "其他费用只能是整百", Toast.LENGTH_SHORT).show();
        } else if (getPrice(firstPriceTv) + getPrice(carLoanPriceTv) != getPrice(billPriceTv)) {
            Toast.makeText(mContext, "首付款加车辆贷款额必须等于开票价", Toast.LENGTH_SHORT).show();
        } else if (Integer.valueOf(totalLoanPriceTv.getText().toString()) < 10000) {
            Toast.makeText(mContext, "总贷款额需多于10000元", Toast.LENGTH_LONG).show();
        } else if (Integer.valueOf(totalLoanPriceTv.getText().toString()) > Integer.valueOf(billPriceTv.getText().toString()) * 0.8) {
            Toast.makeText(mContext, "总贷款额不能大于开票价的80%", Toast.LENGTH_LONG).show();
        } else if (getPrice(billPriceTv) < getPrice(totalLoanPriceTv) + 1) {
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

    public void changeCarInfo() {
        //Toast.makeText(mContext, "测试能否调用", Toast.LENGTH_LONG).show();

        //先清理所有数据
        //mDlrList.clear();
        // mDlrIndex = 0;
        //mBrandList.clear();
        //mBrandIndex = 0;
        //brandTv.setText("");//厂商指导价

        //mTrixList.clear();
        //mTrixIndex = 0;
        //trixTv.setText("");//选择车型

        //mModelList.clear();
        //mModelIndex = 0;
        // modelTv.setText("");

        // guidePriceTv.setText("");

//        mLoanBankList.clear();
//        mLoanBankIndex = 0;
//        loanBankTv.setText(null);
//
//        //  mProductTypeIndex = 0;
//        productTypeTv.setText(null);
//
//        billPriceTv.setText("");


        // mManagementPriceIndex = 0;

//        managementPriceTv.setText("");
//        totalLoanPriceTv.setText("");
//        otherPriceTv.setText("");
//        plateRegAddrTv.setText("");//上牌地选择
//        loanPeriodsTv.setText("");//还款期限

        //调用接口，进行网络请求获取数据，重新填充并且index要对应， 门店测试经销商，宝马,宝马5系,2017款 宝马5系
        GetRawCarInfoResp resp = new GetRawCarInfoResp();
//        public String dlr;
//        public String brand;
//        public String trix;
//        public String model_name;
//        public String color;
//        public String guide_price;
//        public String vehicle_price;//开票价
//        public String loan_amt;//车辆总贷款额
//        public String vehicle_loan_amt;//贷款额
//        public String vehicle_down_payment;//车辆首付款
//        public double vehicle_down_payment_percent; //首付比例
//        public String monthly_payment;
//        public int nper;//还款期限
//        public String management_fee;//管理费
//        public String other_fee;//其他费用
//        public String loan_bank; //贷款银行
//        public String product_type;//产品类型
//        resp.dlr = "测试经销商";
//        resp.brand = "宝马";
//        resp.trix = "宝马5系";
//        resp.model_name = "2017款 宝马5系 530Le";
//        resp.guide_price = "698600";
//        resp.vehicle_price = "600000";
//        resp.vehicle_loan_amt = "50000";
//        resp.vehicle_down_payment = "550000";
//        resp.management_fee = "300";
//        resp.other_fee = "300";
//        resp.loan_amt = "50600";
//        resp.loan_bank = "中国工商银行台州路桥支行";
//        resp.product_type = "予见II型";
//        resp.nper = "36";
//        resp.plateRegAddress = "北京/北京市/东城区";


        dlrTV.setText(resp.dlr);
        brandTv.setText(resp.brand);
        trixTv.setText(resp.trix);
        modelTv.setText(resp.model_name);
       // colorTv.setText(resp.color);

//
//        //  selectIndex(dlrItems, mDlrIndex, "门店测试经销商");
//        Log.e("TAG", ((String.valueOf(mDlrIndex))));
//
//        brandTv.setText("宝马");
//        //selectIndex(brandItems, mBrandIndex, "宝马");
//        Log.e("TAG", ((String.valueOf(mBrandIndex))));
//
//        trixTv.setText("宝马5系");
//        //selectIndex(trixItems, mTrixIndex, "宝马5系");
//        Log.e("TAG", ((String.valueOf(mTrixIndex))));
//
//        modelTv.setText("2017款 宝马5系 530Le");

       isChangeCarInfoChange = true;
//        guidePriceTv.setText(resp.guide_price);//市场指导价
//        billPriceTv.setText(resp.vehicle_price);//开票价
//        carLoanPriceTv.setText(resp.vehicle_loan_amt);//车辆贷款额
//        firstPriceTv.setText(resp.vehicle_down_payment);//首付款
//        managementPriceTv.setText(resp.management_fee);//档案管理费
//        otherPriceTv.setText(resp.other_fee);//其他费用
//        totalLoanPriceTv.setText(resp.loan_amt);//总贷款额
//        loanBankTv.setText(resp.loan_bank);//贷款银行
//        productTypeTv.setText(resp.product_type);//产品类型
//        loanPeriodsTv.setText(resp.nper);//还款期限
//        plateRegAddrTv.setText(resp.plateRegAddress);//上牌地
//        mGuidePrice = Integer.valueOf(resp.guide_price);
        //selectIndex(modelItems, mModelIndex, "2017款 宝马5系 530le");
        Log.e("TAG", ((String.valueOf(mModelIndex))));


    }

    public void selectIndex(List<String> list, int index, String s) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(s)) {
                index = i;
            }
        }
    }
}
