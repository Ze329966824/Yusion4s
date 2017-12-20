package com.yusion.shanghai.yusion4s.ubt;

import android.util.Pair;

import com.yusion.shanghai.yusion4s.ui.CommitActivity;
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
import com.yusion.shanghai.yusion4s.ui.upload.ExtraPreviewActivity;
import com.yusion.shanghai.yusion4s.ui.upload.ExtraUploadListActivity;
import com.yusion.shanghai.yusion4s.ui.upload.UploadLabelListActivity;
import com.yusion.shanghai.yusion4s.ui.upload.UploadSqsListActivity;
import com.yusion.shanghai.yusion4s.ui.yusion.apply.AutonymCertifyFragment;
import com.yusion.shanghai.yusion4s.ui.yusion.apply.PersonalInfoFragment;
import com.yusion.shanghai.yusion4s.ui.yusion.apply.SpouseInfoFragment;

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
        pageNameMaps.put(UploadSqsListActivity.class.getSimpleName(), new Pair<>("upload_sqs", "上传授权书页面"));

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

        pageNameMaps.put(ExtraPreviewActivity.class.getSimpleName(), new Pair<>("img_preview", "图片预览界面"));
//        pageNameMaps.put(OnlyReadUploadListActivity.class.getSimpleName(), new Pair<>("img_authorization_book", "授权书查看页面"));
        pageNameMaps.put(UploadLabelListActivity.class.getSimpleName(), new Pair<>("upload_label_list", "上传影像件标签列表页面"));
        pageNameMaps.put(ExtraUploadListActivity.class.getSimpleName(), new Pair<>("upload_img_list", "上传影像件列表页面"));
        pageNameMaps.put(CommitActivity.class.getSimpleName(), new Pair<>("commit_apply_success", "提交申请成功页面"));
        pageNameMaps.put(AutonymCertifyFragment.class.getSimpleName(), new Pair<>("lender_apply_credit", "主贷人-征信信息页面"));
        pageNameMaps.put(PersonalInfoFragment.class.getSimpleName(), new Pair<>("lender_apply_personal", "主贷人-个人信息页面"));
        pageNameMaps.put(SpouseInfoFragment.class.getSimpleName(), new Pair<>("lender_apply_spouse", "主贷人-配偶页面"));

    }

    static {
        widgetNameMaps.put("car_info_dlr_tv", "经销商选择");
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


         /*
     *   主贷人征信信息
     */
        widgetNameMaps.put("autonym_certify_id_back_tv", "上传身份证人像面");
        widgetNameMaps.put("autonym_certify_id_front_tv", "上传身份证国徽面");
        widgetNameMaps.put("autonym_certify_name_tv", "输入姓名");
        widgetNameMaps.put("autonym_certify_id_number_tv", "输入身份证号");
        widgetNameMaps.put("autonym_certify_mobile_tv", "输入手机号");
        widgetNameMaps.put("autonym_certify_driving_license_tv", "上传驾驶证");
        widgetNameMaps.put("autonym_certify_driving_license_rel_tv", "选择驾驶证与本人关系");


        widgetNameMaps.put("autonym_certify_next_btn", "提交征信信息");


     /*
      *   主贷人
      */

        widgetNameMaps.put("personal_info_gender_tv", "选择性别");
        widgetNameMaps.put("personal_info_reg_tv", "选择户籍地");
        widgetNameMaps.put("personal_info_mobile_edt", "输入手机号");
        widgetNameMaps.put("personal_info_education_tv", "选择学历");
        widgetNameMaps.put("personal_info_current_address_tv", "选择现住地址");
        widgetNameMaps.put("personal_info_current_address1_tv", "选择详细地址");
        widgetNameMaps.put("personal_info_current_address2_tv", "输入门牌号");
        widgetNameMaps.put("personal_info_live_with_parent_tv", "选择是否与父母同住");

        widgetNameMaps.put("personal_info_income_from_tv", "选择主要收入来源");

        widgetNameMaps.put("personal_info_from_income_year_edt", "输入年收入(主要工资)");
        widgetNameMaps.put("personal_info_from_income_company_name_edt", "输入单位名称(主要工资)");
        widgetNameMaps.put("personal_info_from_income_company_address_tv", "选择单位地址(主要工资)");
        widgetNameMaps.put("personal_info_from_income_company_address1_tv", "选择详细地址(主要工资)");
        widgetNameMaps.put("personal_info_from_income_company_address2_tv", "输入门牌号(主要工资)");
        widgetNameMaps.put("personal_info_from_income_work_position_tv", "选择职务(主要工资)");
        widgetNameMaps.put("personal_info_from_income_work_phone_num_edt", "输入单位座机(主要工资)");

        widgetNameMaps.put("personal_info_from_self_year_edt", "输入年收入(主要自营)");
        widgetNameMaps.put("personal_info_from_self_type_tv", "选择业务类型(主要自营)");
        widgetNameMaps.put("personal_info_from_self_company_name_edt", "输入店铺名称(主要自营)");
        widgetNameMaps.put("personal_info_from_self_company_address_tv", "选择经营项目地址(主要自营)");
        widgetNameMaps.put("personal_info_from_self_company_address1_tv", "选择详细地址(主要自营)");
        widgetNameMaps.put("personal_info_from_self_company_address2_tv", "输入门牌号(主要自营)");


        widgetNameMaps.put("personal_info_extra_income_from_tv", "选择额外收入来源");
        widgetNameMaps.put("personal_info_extra_from_income_year_edt", "输入年收入(额外工资)");
        widgetNameMaps.put("personal_info_extra_from_income_company_name_edt", "输入单位名称(额外工资)");
        widgetNameMaps.put("personal_info_extra_from_income_company_address_tv", "选择单位地址(额外工资)");
        widgetNameMaps.put("personal_info_extra_from_income_company_address1_tv", "选择详细地址(额外工资)");
        widgetNameMaps.put("personal_info_extra_from_income_company_address2_tv", "输入门牌号(额外工资)");
        widgetNameMaps.put("personal_info_extra_from_income_work_position_tv", "选择职务(额外工资)");
        widgetNameMaps.put("personal_info_extra_from_income_work_phone_num_edt", "输入单位座机(额外工资)");


        widgetNameMaps.put("personal_info_house_type_tv", "选择房屋性质");
        widgetNameMaps.put("personal_info_house_area_edt", "输入房屋面积");
        widgetNameMaps.put("personal_info_house_owner_name_edt", "输入房屋所有人");
        widgetNameMaps.put("personal_info_house_owner_relation_tv", "选择与申请人关系(房屋所有人)");

        widgetNameMaps.put("personal_info_urg_relation1_tv", "与申请人关系(紧急联系人1)");
        widgetNameMaps.put("personal_info_urg_mobile1_edt", "手机号码(紧急联系人1)");
        widgetNameMaps.put("personal_info_urg_contact1_edt", "联系人姓名(紧急联系人1)");
        widgetNameMaps.put("personal_info_urg_relation2_tv", "与申请人关系(紧急联系人2)");
        widgetNameMaps.put("personal_info_urg_mobile2_edt", "手机号码(紧急联系人2)");
        widgetNameMaps.put("personal_info_urg_contact2_edt", "联系人姓名(紧急联系人2)");

         /*
     *   主贷人配偶
     */
        widgetNameMaps.put("spouse_info_marriage_tv", "选择婚否");

        widgetNameMaps.put("spouse_info_divorced_child_count_edt", "输入子女数量(离异)");
        widgetNameMaps.put("spouse_info_divorced_tv", "上传离婚证");

        widgetNameMaps.put("spouse_info_die_child_count_edt", "输入子女数量(丧偶)");
        widgetNameMaps.put("spouse_info_register_addr_tv", "上传户口本");

        widgetNameMaps.put("spouse_info_id_back_tv", "上传配偶身份证人像面");
        widgetNameMaps.put("spouse_info_id_front_tv", "上传配偶身份证国徽面");
        widgetNameMaps.put("spouse_info_clt_nm_edt", "输入配偶姓名");
        widgetNameMaps.put("spouse_info_id_no_edt", "输入配偶身份证号");
        widgetNameMaps.put("spouse_info_gender_tv", "选择配偶性别");
        widgetNameMaps.put("spouse_info_mobile_edt", "输入配偶手机号");
        widgetNameMaps.put("spouse_info_child_count_edt", "输入子女数量(已婚)");
        widgetNameMaps.put("spouse_info_reg_tv", "选择配偶户籍地");
        widgetNameMaps.put("spouse_info_education_tv", "选择配偶学历");
        widgetNameMaps.put("spouse_info_current_address_tv", "选择配偶现住地址");
        widgetNameMaps.put("spouse_info_current_address1_tv", "选择配偶详细地址");
        widgetNameMaps.put("spouse_info_current_address2_tv", "输入配偶门牌号");
        widgetNameMaps.put("spouse_info_live_with_parent_tv", "选择配偶是否与父母同住");
        

        widgetNameMaps.put("spouse_info_income_from_tv", "选择配偶主要收入来源");
        widgetNameMaps.put("spouse_info_from_income_year_edt", "输入配偶年收入(主要工资)");
        widgetNameMaps.put("spouse_info_from_income_company_name_edt", "输入配偶单位名称(主要工资)");
        widgetNameMaps.put("spouse_info_from_income_company_address_tv", "选择配偶单位地址(主要工资)");
        widgetNameMaps.put("spouse_info_from_income_company_address1_tv", "选择配偶详细地址(主要工资)");
        widgetNameMaps.put("spouse_info_from_income_company_address2_tv", "输入配偶门牌号(主要工资)");
        widgetNameMaps.put("spouse_info_from_income_work_position_tv", "选择配偶职务(主要工资)");
        widgetNameMaps.put("spouse_info_from_income_work_phone_num_edt", "输入配偶单位座机(主要工资)");

        widgetNameMaps.put("spouse_info_from_self_year_edt", "输入配偶年收入(主要自营)");
        widgetNameMaps.put("spouse_info_from_self_type_tv", "选择配偶业务类型(主要自营)");
        widgetNameMaps.put("spouse_info_from_self_company_name_edt", "输入配偶店铺名称(主要自营)");
        widgetNameMaps.put("spouse_info_from_self_company_address_tv", "选择配偶经营项目地址(主要自营)");
        widgetNameMaps.put("spouse_info_from_self_company_address1_tv", "选择配偶详细地址(主要自营)");
        widgetNameMaps.put("spouse_info_from_self_company_address2_tv", "输入配偶门牌号(主要自营)");

        widgetNameMaps.put("spouse_info_extra_income_from_tv", "选择配偶额外收入来源");

        widgetNameMaps.put("spouse_info_extra_from_income_year_edt", "输入配偶年收入(额外工资)");
        widgetNameMaps.put("spouse_info_extra_from_income_company_name_edt", "输入配偶单位名称(额外工资)");
        widgetNameMaps.put("spouse_info_extra_from_income_company_address_tv", "选择配偶单位地址(额外工资)");
        widgetNameMaps.put("spouse_info_extra_from_income_company_address1_tv", "选择配偶详细地址(额外工资)");
        widgetNameMaps.put("spouse_info_extra_from_income_company_address2_tv", "输入配偶门牌号(额外工资)");
        widgetNameMaps.put("spouse_info_extra_from_income_work_position_tv", "选择配偶职务(额外工资)");
        widgetNameMaps.put("spouse_info_extra_from_income_work_phone_num_edt", "输入配偶单位座机(额外工资)");

        widgetNameMaps.put("spouse_info_house_type_tv", "选择配偶房屋性质");
        widgetNameMaps.put("spouse_info_house_area_edt", "输入配偶房屋面积");
        widgetNameMaps.put("spouse_info_house_owner_name_edt", "输入配偶房屋所有人");
        widgetNameMaps.put("spouse_info_house_owner_relation_tv", "选择配偶与申请人关系(房屋所有人)");

        widgetNameMaps.put("spouse_info_urg_relation1_tv", "与申请人关系(配偶紧急联系人1)");
        widgetNameMaps.put("spouse_info_urg_mobile1_edt", "手机号码(配偶紧急联系人1)");
        widgetNameMaps.put("spouse_info_urg_contact1_edt", "联系人姓名(配偶紧急联系人1)");
        widgetNameMaps.put("spouse_info_urg_relation2_tv", "与申请人关系(配偶紧急联系人2)");
        widgetNameMaps.put("spouse_info_urg_mobile2_edt", "手机号码(配偶紧急联系人2)");
        widgetNameMaps.put("spouse_info_urg_contact2_edt", "联系人姓名(配偶紧急联系人2)");
        widgetNameMaps.put("spouse_info_submit_btn", "提交配偶信息");


    }

    static {
        widgetNameMaps.put("car_info_dlr_tv", "经销商选择");
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
