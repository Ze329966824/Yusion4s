package com.yusion.shanghai.yusion4s.ubt;

import android.util.Pair;

import com.yusion.shanghai.yusion4s.ui.MainActivity;
import com.yusion.shanghai.yusion4s.ui.MineFragment;
import com.yusion.shanghai.yusion4s.ui.entrance.LaunchActivity;
import com.yusion.shanghai.yusion4s.ui.entrance.LoginActivity;
import com.yusion.shanghai.yusion4s.ui.entrance.WebViewActivity;
import com.yusion.shanghai.yusion4s.ui.entrance.apply_financing.CarInfoFragment;
import com.yusion.shanghai.yusion4s.ui.entrance.apply_financing.CreditInfoFragment;
import com.yusion.shanghai.yusion4s.ui.main.SettingsActivity;
import com.yusion.shanghai.yusion4s.ui.order.OrderDetailActivity;
import com.yusion.shanghai.yusion4s.ui.order.SearchClientActivity;
import com.yusion.shanghai.yusion4s.ui.upload.PreviewActivity;
import com.yusion.shanghai.yusion4s.ui.upload.UploadLabelListActivity;
import com.yusion.shanghai.yusion4s.ui.upload.UploadListActivity;

import java.util.HashMap;

/**
 * Created by ice on 2017/9/26.
 */

public class UBTCollections {
    public static HashMap<String, Pair<String, String>> pageNameMaps = new HashMap<>();
    public static HashMap<String, String> widgetNameMaps = new HashMap<>();

    static {
        pageNameMaps.put(LaunchActivity.class.getSimpleName(), new Pair<>("launch", "启动页面"));
        pageNameMaps.put(CarInfoFragment.class.getSimpleName(), new Pair<>("carinfo", "提交订单-车辆信息页面"));
        pageNameMaps.put(CreditInfoFragment.class.getSimpleName(), new Pair<>("creditinfo", "提交订单-征信信息页面"));
        pageNameMaps.put(LoginActivity.class.getSimpleName(), new Pair<>("login", "登陆页面"));
        pageNameMaps.put(WebViewActivity.class.getSimpleName(), new Pair<>("webview", "H5页面"));
//        pageNameMaps.put(AgreeMentActivity.class.getSimpleName(), new Pair<>("agreement", "协议页面"));
        pageNameMaps.put(MainActivity.class.getSimpleName(), new Pair<>("home_order_mine", "首页-订单-我的页面"));

//        pageNameMaps.put(HomeFragment.class.getSimpleName(), new Pair<>("home", "首页"));
//        pageNameMaps.put(MyOrderFragment.class.getSimpleName(), new Pair<>("order_list", "订单列表页面"));
        pageNameMaps.put(MineFragment.class.getSimpleName(), new Pair<>("mine", "个人信息页面"));

        pageNameMaps.put(SettingsActivity.class.getSimpleName(), new Pair<>("settings", "设置页面"));

//        pageNameMaps.put(FinancePlanActivity.class.getSimpleName(), new Pair<>("finance_plan", "金融方案页面"));
        pageNameMaps.put(OrderDetailActivity.class.getSimpleName(), new Pair<>("order_detail", "申请详情页面"));

        pageNameMaps.put(SearchClientActivity.class.getSimpleName(), new Pair<>("search_client", "搜索客户页面"));
//
//        pageNameMaps.put(CommitActivity.class.getSimpleName(), new Pair<>("commit_apply_success", "提交申请成功页面"));
//        pageNameMaps.put(ImgsListFragment.class.getSimpleName(), new Pair<>("info_img", "资料列表-影像件页面"));
//        pageNameMaps.put(InfoListActivity.class.getSimpleName(), new Pair<>("info", "资料列表页面"));
//        pageNameMaps.put(InfoListFragment.class.getSimpleName(), new Pair<>("info_info", "资料列表-资料页面"));
//
//        pageNameMaps.put(UpdateGuarantorInfoActivity.class.getSimpleName(), new Pair<>("update_guarantor", "修改担保人资料页面"));
//        pageNameMaps.put(UpdateGuarantorSpouseInfoActivity.class.getSimpleName(), new Pair<>("update_guarantor_spouse", "修改担保人配偶资料页面"));
//        pageNameMaps.put(UpdatePersonalInfoActivity.class.getSimpleName(), new Pair<>("update_lender", "修改个人资料页面"));
//        pageNameMaps.put(UpdateSpouseInfoActivity.class.getSimpleName(), new Pair<>("update_lender_spouse", "修改个人配偶资料页面"));
//
//        pageNameMaps.put(ApplyActivity.class.getSimpleName(), new Pair<>("lender_apply", "主贷人申请页面"));
//        pageNameMaps.put(AutonymCertifyFragment.class.getSimpleName(), new Pair<>("lender_apply_credit", "主贷人-征信信息页面"));
//        pageNameMaps.put(PersonalInfoFragment.class.getSimpleName(), new Pair<>("lender_apply_personal", "主贷人-个人信息页面"));
//        pageNameMaps.put(SpouseInfoFragment.class.getSimpleName(), new Pair<>("lender_apply_spouse", "主贷人-配偶页面"));
//        pageNameMaps.put(AddGuarantorActivity.class.getSimpleName(), new Pair<>("guarantor_apply", "担保人申请页面"));
//        pageNameMaps.put(GuarantorCreditInfoFragment.class.getSimpleName(), new Pair<>("guarantor_apply_personal", "担保人-征信信息页面"));
//        pageNameMaps.put(GuarantorInfoFragment.class.getSimpleName(), new Pair<>("guarantor_apply_personal", "担保人-个人信息页面"));
//        pageNameMaps.put(GuarantorSpouseInfoFragment.class.getSimpleName(), new Pair<>("guarantor_apply_personal", "担保人-配偶页面"));
//        pageNameMaps.put(AMapPoiListActivity.class.getSimpleName(), new Pair<>("poi_address", "高德地图POI检索界面"));

//        pageNameMaps.put(DocmtActivity.class.getSimpleName(), new Pair<>("single_img_upload", "单张图片上传界面(身份证正反面,驾照)"));
//        pageNameMaps.put(DocumentFromLabelListActivity.class.getSimpleName(), new Pair<>("multi_img_upload", "多张图片上传界面"));

        pageNameMaps.put(PreviewActivity.class.getSimpleName(), new Pair<>("img_preview", "图片预览界面"));
//        pageNameMaps.put(OnlyReadUploadListActivity.class.getSimpleName(), new Pair<>("img_authorization_book", "授权书查看页面"));
        pageNameMaps.put(UploadLabelListActivity.class.getSimpleName(), new Pair<>("upload_label_list", "上传影像件标签列表页面"));
        pageNameMaps.put(UploadListActivity.class.getSimpleName(), new Pair<>("upload_img_list", "上传影像件列表页面"));
    }

    static {
        widgetNameMaps.put("car_info_dlr_tv", "门店选择");
        widgetNameMaps.put("car_info_brand_tv", "品牌选择");
        widgetNameMaps.put("car_info_trix_tv", "车系选择");
        widgetNameMaps.put("car_info_model_tv", "车型选择");
        widgetNameMaps.put("car_info_management_price_tv", "是否管贷档案管理费选择");
        widgetNameMaps.put("car_info_loan_bank_tv", "贷款银行选择");
        widgetNameMaps.put("car_info_product_type_tv", "产品类型选择");
        widgetNameMaps.put("car_info_loan_periods_tv", "还款期限选择");
        widgetNameMaps.put("car_info_plate_reg_addr_tv", "上牌地选择");
        widgetNameMaps.put("car_info_bill_price_tv", "车辆开票价输入");
        widgetNameMaps.put("car_info_car_loan_price_tv", "车辆贷款额输入");
        widgetNameMaps.put("car_info_first_price_tv", "车辆首付款输入");
        widgetNameMaps.put("car_info_other_price_tv", "其他费用输入");
        widgetNameMaps.put("car_info_color_tv", "颜色输入");

        widgetNameMaps.put("tv_find", "搜索客户");
        widgetNameMaps.put("choose_relation", "选择车主与申请人关系");
        widgetNameMaps.put("autonym_certify_id_back_tv10", "详情");
        widgetNameMaps.put("autonym_certify_id_back_tv", "上传申请人征信授权书");
        widgetNameMaps.put("autonym_certify_id_back_tv1", "上传申请人配偶征信授权书");
        widgetNameMaps.put("autonym_certify_id_back_tv2", "上传担保人征信授权书");
        widgetNameMaps.put("autonym_certify_id_back_tv3", "上传担保人配偶征信授权书");

        widgetNameMaps.put("et_search", "输入搜索内容");
        widgetNameMaps.put("search_info", "点击搜索客户按钮");

        widgetNameMaps.put("order_detail_sign", "提交用户资料");

    }

    public static String getPageNm(String key) {
        Pair<String, String> pair = pageNameMaps.get(key);
        if (pair == null) {
            return "";
        } else {
            return pair.first;
        }
    }

    public static String getPageNmCn(String key) {
        Pair<String, String> pair = pageNameMaps.get(key);
        if (pair == null) {
            return "";
        } else {
            return pair.second;
        }
    }

    public static String getWidgetNmCn(String widget) {
        return widgetNameMaps.get(widget);
    }
}
