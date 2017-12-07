package com.yusion.shanghai.yusion4s.ui.yusion.apply;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.Yusion4sApp;
import com.yusion.shanghai.yusion4s.base.DoubleCheckFragment;
import com.yusion.shanghai.yusion4s.bean.ocr.OcrResp;
import com.yusion.shanghai.yusion4s.bean.upload.UploadFilesUrlReq;
import com.yusion.shanghai.yusion4s.bean.upload.UploadImgItemBean;
import com.yusion.shanghai.yusion4s.event.ApplyActivityEvent;
import com.yusion.shanghai.yusion4s.retrofit.api.ProductApi;
import com.yusion.shanghai.yusion4s.retrofit.api.UploadApi;
import com.yusion.shanghai.yusion4s.settings.Constants;
import com.yusion.shanghai.yusion4s.ubt.UBT;
import com.yusion.shanghai.yusion4s.ubt.annotate.BindView;
import com.yusion.shanghai.yusion4s.ui.SingleImgUploadForCreateUserActivity;
import com.yusion.shanghai.yusion4s.ui.yusion.YusionUploadListActivity;
import com.yusion.shanghai.yusion4s.utils.CheckIdCardValidUtil;
import com.yusion.shanghai.yusion4s.utils.CheckMobileUtil;
import com.yusion.shanghai.yusion4s.utils.ContactsUtil;
import com.yusion.shanghai.yusion4s.utils.FileUtil;
import com.yusion.shanghai.yusion4s.utils.InputMethodUtil;
import com.yusion.shanghai.yusion4s.utils.SharedPrefsUtil;
import com.yusion.shanghai.yusion4s.utils.wheel.WheelViewUtil;
import com.yusion.shanghai.yusion4s.widget.NoEmptyEditText;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.graphics.Typeface.createFromAsset;


public class SpouseInfoFragment extends DoubleCheckFragment {

    private String idBackImgUrl = "";
    private String idFrontImgUrl = "";
    private String ID_BACK_FID = "";
    private String ID_FRONT_FID = "";
    private int _GENDER_INDEX = 0;
    private int _MARRIAGE_INDEX = 0;
    private int _INCOME_FROME_INDEX = 0;
    private int _FROM_SELF_TYPE_INDEX = 0;
    private int _EXTRA_INCOME_FROME_INDEX = 0;
    private int _FROM_EXTRA_WORK_POSITION_INDEX = 0;
    private int _FROM_INCOME_WORK_POSITION_INDEX = 0;
    private int CURRENT_CLICKED_VIEW_FOR_ADDRESS = -1;
    private int CURRENT_CLICKED_VIEW_FOR_CONTACT = 0;
    private int _LIVE_WITH_PARENT_INDEX = 0;
    private int _EDUCATION_INDEX = 0;
    private int _HOUSE_TYPE_INDEX = 0;
    private int _HOUSE_OWNER_RELATION_INDEX = 0;
    private int _URG_RELATION_INDEX1 = 0;
    private int _URG_RELATION_INDEX2 = 0;
    private List<UploadImgItemBean> divorceImgsList = new ArrayList<>();
    private List<UploadImgItemBean> resBookList = new ArrayList<>();


    private List<String> incomelist = new ArrayList<String>() {{
        add("工资");
        add("自营");
    }};
    private List<String> incomeextarlist = new ArrayList<String>() {{
        add("工资");
        add("无");
    }};
    private List<String> ifwithparentlist = new ArrayList<String>() {{
        add("是");
        add("否");
    }};

    private ApplyActivity applyActivity;

    private ImageView spouse_info_mobile_img;
    private LinearLayout spouse_info_id_back_lin;
    private LinearLayout spouse_info_id_front_lin;
    private LinearLayout spouse_info_marriage_lin;
    private LinearLayout spouse_info_income_from_lin;
    private LinearLayout spouse_info_extra_income_from_lin;
    private LinearLayout spouse_info_divorced_lin;
    private LinearLayout spouse_info_register_addr_lin;
    private LinearLayout spouse_info_gender_lin;
    private LinearLayout spouse_info_from_income_company_address_lin;
    private LinearLayout spouse_info_from_income_company_address1_lin;
    private LinearLayout spouse_info_from_income_work_position_lin;
    private LinearLayout spouse_info_from_self_company_address_lin;
    private LinearLayout spouse_info_from_self_company_address1_lin;
    private LinearLayout spouse_info_from_self_type_lin;
    private LinearLayout spouse_info_extra_from_income_company_address_lin;
    private LinearLayout spouse_info_extra_from_income_company_address1_lin;
    private LinearLayout spouse_info_extra_from_income_work_position_lin;
    private LinearLayout spouse_info_marriage_group_lin;
    private LinearLayout spouse_info_divorced_group_lin;
    private LinearLayout spouse_info_die_group_lin;
    private LinearLayout spouse_info_from_income_group_lin;
    private LinearLayout spouse_info_from_self_group_lin;
    private EditText spouse_info_from_other_year_edt;
    private EditText spouse_info_from_other_remark_edt;
    private LinearLayout spouse_info_reg_lin;
    private LinearLayout spouse_info_education_lin;
    private LinearLayout spouse_info_current_address_lin;
    private LinearLayout spouse_info_current_address1_lin;
    private LinearLayout spouse_info_live_with_parent_lin;
    private LinearLayout spouse_info_house_type_lin;
    private LinearLayout spouse_info_house_owner_relation_lin;
    private LinearLayout spouse_info_urg_relation1_lin;
    private LinearLayout spouse_info_urg_relation2_lin;
    private ImageView spouse_info_urg_mobile1_img;
    private ImageView spouse_info_urg_mobile2_img;
    private TextView step1;
    private TextView step2;
    private TextView step3;

    OcrResp.ShowapiResBodyBean ocrResp = new OcrResp.ShowapiResBodyBean();

    @BindView(id = R.id.spouse_info_marriage_tv, widgetName = "spouse_info_marriage_tv")
    private TextView spouse_info_marriage_tv;

    //离异
    @BindView(id = R.id.spouse_info_divorced_child_count_edt, widgetName = "spouse_info_divorced_child_count_edt")
    private EditText spouse_info_divorced_child_count_edt;

    @BindView(id = R.id.spouse_info_divorced_tv, widgetName = "spouse_info_divorced_tv")
    private TextView spouse_info_divorced_tv;

    //丧偶
    @BindView(id = R.id.spouse_info_die_child_count_edt, widgetName = "spouse_info_die_child_count_edt")
    private EditText spouse_info_die_child_count_edt;

    @BindView(id = R.id.spouse_info_register_addr_tv, widgetName = "spouse_info_register_addr_tv")
    private TextView spouse_info_register_addr_tv;

    //已婚
    @BindView(id = R.id.spouse_info_id_back_tv, widgetName = "spouse_info_id_back_tv")
    private TextView spouse_info_id_back_tv;

    @BindView(id = R.id.spouse_info_id_front_tv, widgetName = "spouse_info_id_front_tv")
    private TextView spouse_info_id_front_tv;

    @BindView(id = R.id.spouse_info_clt_nm_edt, widgetName = "spouse_info_clt_nm_edt")
    private EditText spouse_info_clt_nm_edt;

    @BindView(id = R.id.spouse_info_id_no_edt, widgetName = "spouse_info_id_no_edt")
    private EditText spouse_info_id_no_edt;

    @BindView(id = R.id.spouse_info_gender_tv, widgetName = "spouse_info_gender_tv")
    private TextView spouse_info_gender_tv;

    @BindView(id = R.id.spouse_info_mobile_edt, widgetName = "spouse_info_mobile_edt")
    private EditText spouse_info_mobile_edt;

    @BindView(id = R.id.spouse_info_child_count_edt, widgetName = "spouse_info_child_count_edt")
    private EditText spouse_info_child_count_edt;

    @BindView(id = R.id.spouse_info_reg_tv, widgetName = "spouse_info_reg_tv")
    private TextView spouse_info_reg_tv;

    @BindView(id = R.id.spouse_info_education_tv, widgetName = "spouse_info_education_tv")
    private TextView spouse_info_education_tv;

    @BindView(id = R.id.spouse_info_current_address_tv, widgetName = "spouse_info_current_address_tv")
    private TextView spouse_info_current_address_tv;

    @BindView(id = R.id.spouse_info_current_address1_tv, widgetName = "spouse_info_current_address1_tv")
    private TextView spouse_info_current_address1_tv;

    @BindView(id = R.id.spouse_info_current_address2_tv, widgetName = "spouse_info_current_address2_tv")
    private NoEmptyEditText spouse_info_current_address2_tv;

    @BindView(id = R.id.spouse_info_live_with_parent_tv, widgetName = "spouse_info_live_with_parent_tv")
    private TextView spouse_info_live_with_parent_tv;

    //主要收入来源
    @BindView(id = R.id.spouse_info_income_from_tv, widgetName = "spouse_info_income_from_tv")
    private TextView spouse_info_income_from_tv;
    //主要工资
    @BindView(id = R.id.spouse_info_from_income_year_edt, widgetName = "spouse_info_from_income_year_edt")
    private EditText spouse_info_from_income_year_edt;

    @BindView(id = R.id.spouse_info_from_income_company_name_edt, widgetName = "spouse_info_from_income_company_name_edt")
    private EditText spouse_info_from_income_company_name_edt;

    @BindView(id = R.id.spouse_info_from_income_company_address_tv, widgetName = "spouse_info_from_income_company_address_tv")
    private TextView spouse_info_from_income_company_address_tv;

    @BindView(id = R.id.spouse_info_from_income_company_address1_tv, widgetName = "spouse_info_from_income_company_address1_tv")
    private TextView spouse_info_from_income_company_address1_tv;

    @BindView(id = R.id.spouse_info_from_income_company_address2_tv, widgetName = "spouse_info_from_income_company_address2_tv")
    private NoEmptyEditText spouse_info_from_income_company_address2_tv;

    @BindView(id = R.id.spouse_info_from_income_work_position_tv, widgetName = "spouse_info_from_income_work_position_tv")
    private TextView spouse_info_from_income_work_position_tv;

    @BindView(id = R.id.spouse_info_from_income_work_phone_num_edt, widgetName = "spouse_info_from_income_work_phone_num_edt")
    private EditText spouse_info_from_income_work_phone_num_edt;

    //主要自营
    @BindView(id = R.id.spouse_info_from_self_year_edt, widgetName = "spouse_info_from_self_year_edt")
    private EditText spouse_info_from_self_year_edt;

    @BindView(id = R.id.spouse_info_from_self_type_tv, widgetName = "spouse_info_from_self_type_tv")
    private TextView spouse_info_from_self_type_tv;

    @BindView(id = R.id.spouse_info_from_self_company_name_edt, widgetName = "spouse_info_from_self_company_name_edt")
    private EditText spouse_info_from_self_company_name_edt;

    @BindView(id = R.id.spouse_info_from_self_company_address_tv, widgetName = "spouse_info_from_self_company_address_tv")
    private TextView spouse_info_from_self_company_address_tv;

    @BindView(id = R.id.spouse_info_from_self_company_address1_tv, widgetName = "spouse_info_from_self_company_address1_tv")
    private TextView spouse_info_from_self_company_address1_tv;

    @BindView(id = R.id.spouse_info_from_self_company_address2_tv, widgetName = "spouse_info_from_self_company_address2_tv")
    private NoEmptyEditText spouse_info_from_self_company_address2_tv;

    //额外输入来源
    @BindView(id = R.id.spouse_info_extra_income_from_tv, widgetName = "spouse_info_extra_income_from_tv")
    private TextView spouse_info_extra_income_from_tv;
    //额外工资
    @BindView(id = R.id.spouse_info_extra_from_income_year_edt, widgetName = "spouse_info_extra_from_income_year_edt")
    private EditText spouse_info_extra_from_income_year_edt;

    @BindView(id = R.id.spouse_info_extra_from_income_company_name_edt, widgetName = "spouse_info_extra_from_income_company_name_edt")
    private EditText spouse_info_extra_from_income_company_name_edt;

    @BindView(id = R.id.spouse_info_extra_from_income_company_address_tv, widgetName = "spouse_info_extra_from_income_company_address_tv")
    private TextView spouse_info_extra_from_income_company_address_tv;

    @BindView(id = R.id.spouse_info_extra_from_income_company_address1_tv, widgetName = "spouse_info_extra_from_income_company_address1_tv")
    private TextView spouse_info_extra_from_income_company_address1_tv;

    @BindView(id = R.id.spouse_info_extra_from_income_company_address2_tv, widgetName = "spouse_info_extra_from_income_company_address2_tv")
    private NoEmptyEditText spouse_info_extra_from_income_company_address2_tv;

    @BindView(id = R.id.spouse_info_extra_from_income_work_position_tv, widgetName = "spouse_info_extra_from_income_work_position_tv")
    private TextView spouse_info_extra_from_income_work_position_tv;

    @BindView(id = R.id.spouse_info_extra_from_income_work_phone_num_edt, widgetName = "spouse_info_extra_from_income_work_phone_num_edt")
    private EditText spouse_info_extra_from_income_work_phone_num_edt;


    @BindView(id = R.id.spouse_info_house_type_tv, widgetName = "spouse_info_house_type_tv")
    private TextView spouse_info_house_type_tv;

    @BindView(id = R.id.spouse_info_house_area_edt, widgetName = "spouse_info_house_area_edt")
    private EditText spouse_info_house_area_edt;

    @BindView(id = R.id.spouse_info_house_owner_name_edt, widgetName = "spouse_info_house_owner_name_edt")
    private EditText spouse_info_house_owner_name_edt;

    @BindView(id = R.id.spouse_info_house_owner_relation_tv, widgetName = "spouse_info_house_owner_relation_tv")
    private TextView spouse_info_house_owner_relation_tv;

    @BindView(id = R.id.spouse_info_urg_relation1_tv, widgetName = "spouse_info_urg_relation1_tv")
    private TextView spouse_info_urg_relation1_tv;

    @BindView(id = R.id.spouse_info_urg_mobile1_edt, widgetName = "spouse_info_urg_mobile1_edt")
    private EditText spouse_info_urg_mobile1_edt;

    @BindView(id = R.id.spouse_info_urg_contact1_edt, widgetName = "spouse_info_urg_contact1_edt")
    private EditText spouse_info_urg_contact1_edt;

    @BindView(id = R.id.spouse_info_urg_relation2_tv, widgetName = "spouse_info_urg_relation2_tv")
    private TextView spouse_info_urg_relation2_tv;

    @BindView(id = R.id.spouse_info_urg_mobile2_edt, widgetName = "spouse_info_urg_mobile2_edt")
    private EditText spouse_info_urg_mobile2_edt;

    @BindView(id = R.id.spouse_info_urg_contact2_edt, widgetName = "spouse_info_urg_contact2_edt")
    private EditText spouse_info_urg_contact2_edt;


    @BindView(id = R.id.spouse_info_submit_btn, widgetName = "spouse_info_submit_btn", onClick = "submitSpouseInfo")
    private Button spouse_info_submit_btn;

    void submitSpouseInfo(View view) {
//        spouse_info_submit_btn.setFocusable(true);
//        spouse_info_submit_btn.setFocusableInTouchMode(true);
//        spouse_info_submit_btn.requestFocus();
//        spouse_info_submit_btn.requestFocusFromTouch();

        if (checkCanNextStep()) {
            if (spouse_info_marriage_tv.getText().toString().equals("已婚")) {
                clearDoubleCheckItems();
                addDoubleCheckItem("姓名", spouse_info_clt_nm_edt.getText().toString());
                addDoubleCheckItem("身份证号", spouse_info_id_no_edt.getText().toString());
                addDoubleCheckItem("手机号", spouse_info_mobile_edt.getText().toString());
                mDoubleCheckDialog.show();
            } else {
                submit();
            }
        }
    }

    private void submit() {
        applyActivity.mClientInfo.marriage = spouse_info_marriage_tv.getText().toString();
        applyActivity.mClientInfo.spouse.marriage = spouse_info_marriage_tv.getText().toString();
        if ("已婚".equals(applyActivity.mClientInfo.marriage)) {
            applyActivity.mClientInfo.spouse.clt_nm = spouse_info_clt_nm_edt.getText().toString();
            applyActivity.mClientInfo.spouse.id_no = spouse_info_id_no_edt.getText().toString();
            applyActivity.mClientInfo.spouse.gender = spouse_info_gender_tv.getText().toString();
            applyActivity.mClientInfo.spouse.mobile = spouse_info_mobile_edt.getText().toString();
            applyActivity.mClientInfo.child_num = spouse_info_child_count_edt.getText().toString();
            if (!TextUtils.isEmpty(spouse_info_reg_tv.getText().toString())) {
                applyActivity.mClientInfo.spouse.reg_addr.province = spouse_info_reg_tv.getText().toString().trim().split("/")[0];
                applyActivity.mClientInfo.spouse.reg_addr.city = spouse_info_reg_tv.getText().toString().trim().split("/")[1];
                applyActivity.mClientInfo.spouse.reg_addr.district = spouse_info_reg_tv.getText().toString().trim().split("/")[2];
            }
            applyActivity.mClientInfo.spouse.edu = spouse_info_education_tv.getText().toString();

            //现住地址
            if (!TextUtils.isEmpty(spouse_info_current_address_tv.getText().toString())) {
                applyActivity.mClientInfo.spouse.current_addr.province = spouse_info_current_address_tv.getText().toString().trim().split("/")[0];
                applyActivity.mClientInfo.spouse.current_addr.city = spouse_info_current_address_tv.getText().toString().trim().split("/")[1];
                applyActivity.mClientInfo.spouse.current_addr.district = spouse_info_current_address_tv.getText().toString().trim().split("/")[2];
            }
            applyActivity.mClientInfo.spouse.current_addr.address1 = (spouse_info_current_address1_tv.getText().toString());
            applyActivity.mClientInfo.spouse.current_addr.address2 = (spouse_info_current_address2_tv.getText().toString());
            applyActivity.mClientInfo.spouse.is_live_with_parent = (spouse_info_live_with_parent_tv.getText().toString());

            //主要收入来源
            switch (spouse_info_income_from_tv.getText().toString()) {
                case "工资":
                    applyActivity.mClientInfo.spouse.major_income_type = "工资";
                    applyActivity.mClientInfo.spouse.major_income = spouse_info_from_income_year_edt.getText().toString();
                    applyActivity.mClientInfo.spouse.major_company_name = spouse_info_from_income_company_name_edt.getText().toString();
                    if (!TextUtils.isEmpty(spouse_info_from_income_company_address_tv.getText().toString())) {
                        applyActivity.mClientInfo.spouse.major_company_addr.province = spouse_info_from_income_company_address_tv.getText().toString().trim().split("/")[0];
                        applyActivity.mClientInfo.spouse.major_company_addr.city = spouse_info_from_income_company_address_tv.getText().toString().trim().split("/")[1];
                        applyActivity.mClientInfo.spouse.major_company_addr.district = spouse_info_from_income_company_address_tv.getText().toString().trim().split("/")[2];
                    } else {
                        applyActivity.mClientInfo.spouse.major_company_addr.province = "";
                        applyActivity.mClientInfo.spouse.major_company_addr.city = "";
                        applyActivity.mClientInfo.spouse.major_company_addr.district = "";
                    }
                    applyActivity.mClientInfo.spouse.major_company_addr.address1 = spouse_info_from_income_company_address1_tv.getText().toString();
                    applyActivity.mClientInfo.spouse.major_company_addr.address2 = spouse_info_from_income_company_address2_tv.getText().toString();
                    applyActivity.mClientInfo.spouse.major_work_position = spouse_info_from_income_work_position_tv.getText().toString();
                    applyActivity.mClientInfo.spouse.major_work_phone_num = spouse_info_from_income_work_phone_num_edt.getText().toString();
                    break;
                case "自营":
                    applyActivity.mClientInfo.spouse.major_income_type = "自营";
                    applyActivity.mClientInfo.spouse.major_income = spouse_info_from_self_year_edt.getText().toString();
                    applyActivity.mClientInfo.spouse.major_busi_type = spouse_info_from_self_type_tv.getText().toString();
                    applyActivity.mClientInfo.spouse.major_company_name = spouse_info_from_self_company_name_edt.getText().toString();
                    if (TextUtils.isEmpty(spouse_info_from_self_company_address_tv.getText())) {
                        applyActivity.mClientInfo.spouse.major_company_addr.province = "";
                        applyActivity.mClientInfo.spouse.major_company_addr.city = "";
                        applyActivity.mClientInfo.spouse.major_company_addr.district = "";
                    } else {
                        applyActivity.mClientInfo.spouse.major_company_addr.province = spouse_info_from_self_company_address_tv.getText().toString().trim().split("/")[0];
                        applyActivity.mClientInfo.spouse.major_company_addr.city = spouse_info_from_self_company_address_tv.getText().toString().trim().split("/")[1];
                        ;
                        applyActivity.mClientInfo.spouse.major_company_addr.district = spouse_info_from_self_company_address_tv.getText().toString().trim().split("/")[2];
                    }
                    applyActivity.mClientInfo.spouse.major_company_addr.address1 = spouse_info_from_self_company_address1_tv.getText().toString();
                    applyActivity.mClientInfo.spouse.major_company_addr.address2 = spouse_info_from_self_company_address2_tv.getText().toString();
                    break;
                case "其他":
                    applyActivity.mClientInfo.spouse.major_income_type = "其他";
                    applyActivity.mClientInfo.spouse.major_income = spouse_info_from_other_year_edt.getText().toString();
                    applyActivity.mClientInfo.spouse.major_remark = spouse_info_from_other_remark_edt.getText().toString();

                    break;
                default:
                    break;
            }
            //额外收入来源
            switch (spouse_info_extra_income_from_tv.getText().toString()) {
                case "工资":
                    applyActivity.mClientInfo.spouse.extra_income_type = "工资";
                    applyActivity.mClientInfo.spouse.extra_income = spouse_info_extra_from_income_year_edt.getText().toString();
                    applyActivity.mClientInfo.spouse.extra_company_name = spouse_info_extra_from_income_company_name_edt.getText().toString();
                    applyActivity.mClientInfo.spouse.extra_company_addr.province = spouse_info_extra_from_income_company_address_tv.getText().toString().trim().split("/")[0];
                    applyActivity.mClientInfo.spouse.extra_company_addr.city = spouse_info_extra_from_income_company_address_tv.getText().toString().trim().split("/")[1];
                    applyActivity.mClientInfo.spouse.extra_company_addr.district = spouse_info_extra_from_income_company_address_tv.getText().toString().trim().split("/")[2];
                    applyActivity.mClientInfo.spouse.extra_company_addr.address1 = spouse_info_extra_from_income_company_address1_tv.getText().toString();
                    applyActivity.mClientInfo.spouse.extra_company_addr.address2 = spouse_info_extra_from_income_company_address2_tv.getText().toString();
                    applyActivity.mClientInfo.spouse.extra_work_position = spouse_info_extra_from_income_work_position_tv.getText().toString();
                    applyActivity.mClientInfo.spouse.extra_work_phone_num = spouse_info_extra_from_income_work_phone_num_edt.getText().toString();
                    break;
                case "无":
                    applyActivity.mClientInfo.spouse.extra_income_type = "无";
                    break;
                default:
                    break;
            }


            //房屋性质
            applyActivity.mClientInfo.spouse.house_owner_name = spouse_info_house_owner_name_edt.getText().toString();
            applyActivity.mClientInfo.spouse.house_owner_relation = spouse_info_house_owner_relation_tv.getText().toString();
            applyActivity.mClientInfo.spouse.house_type = spouse_info_house_type_tv.getText().toString();
            applyActivity.mClientInfo.spouse.house_area = spouse_info_house_area_edt.getText().toString();
            applyActivity.mClientInfo.spouse.urg_contact1 = spouse_info_urg_contact1_edt.getText().toString();
            applyActivity.mClientInfo.spouse.urg_mobile1 = spouse_info_urg_mobile1_edt.getText().toString();
            applyActivity.mClientInfo.spouse.urg_relation1 = spouse_info_urg_relation1_tv.getText().toString();
            applyActivity.mClientInfo.spouse.urg_contact2 = spouse_info_urg_contact2_edt.getText().toString();
            applyActivity.mClientInfo.spouse.urg_mobile2 = spouse_info_urg_mobile2_edt.getText().toString();
            applyActivity.mClientInfo.spouse.urg_relation2 = spouse_info_urg_relation2_tv.getText().toString();
        } else if ("离异".equals(applyActivity.mClientInfo.marriage)) {
            applyActivity.mClientInfo.child_num = spouse_info_divorced_child_count_edt.getText().toString();
        } else if ("丧偶".equals(applyActivity.mClientInfo.marriage)) {
            applyActivity.mClientInfo.child_num = spouse_info_die_child_count_edt.getText().toString();
        }
        Log.e("current_addr2--------", applyActivity.mClientInfo.current_addr.province);
        Log.e("current_addr2--------", applyActivity.mClientInfo.current_addr.city);
        Log.e("current_addr2--------", applyActivity.mClientInfo.current_addr.district);
        Log.e("current_addr2--------", applyActivity.mClientInfo.current_addr.address1);
        Log.e("current_addr2--------", applyActivity.mClientInfo.current_addr.address2);

        FileUtil.saveLog(applyActivity.mClientInfo.toString());
        TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        applyActivity.mClientInfo.spouse.imei = telephonyManager.getDeviceId();
        ProductApi.updateClientInfo(mContext, applyActivity.mClientInfo, data1 -> {
            if (data1 != null && data1.commited.equals("1")) {
                applyActivity.mClientInfo = data1;
                uploadUrl(applyActivity.mClientInfo.clt_id, applyActivity.mClientInfo.spouse.clt_id);
            }
        });

    }

    private boolean checkCanNextStep() {
        if (spouse_info_marriage_tv.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "请选择婚姻状况", Toast.LENGTH_SHORT).show();
            return false;
        }
        if ("已婚".equals(spouse_info_marriage_tv.getText().toString())) {
            if (ID_BACK_FID.isEmpty()) {
                Toast.makeText(mContext, "请拍摄身份证人像面", Toast.LENGTH_SHORT).show();
            } else if (ID_FRONT_FID.isEmpty()) {
                Toast.makeText(mContext, "请拍摄身份证国徽面", Toast.LENGTH_SHORT).show();
            } else if (spouse_info_clt_nm_edt.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "姓名不能为空", Toast.LENGTH_SHORT).show();
            } else if (spouse_info_id_no_edt.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "身份证号不能为空", Toast.LENGTH_SHORT).show();
            } else if (!CheckIdCardValidUtil.isValidatedAllIdcard(spouse_info_id_no_edt.getText().toString())) {
                Toast.makeText(mContext, "身份证号有误", Toast.LENGTH_SHORT).show();
            } else if (spouse_info_gender_tv.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "性别不能为空", Toast.LENGTH_SHORT).show();
            } else if (spouse_info_reg_tv.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "户籍地不能为空", Toast.LENGTH_SHORT).show();
            } else if (spouse_info_mobile_edt.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "手机号不能为空", Toast.LENGTH_SHORT).show();
            } else if (!CheckMobileUtil.checkMobile(spouse_info_mobile_edt.getText().toString())) {
                Toast.makeText(mContext, "手机号码格式错误", Toast.LENGTH_SHORT).show();
            } else if (spouse_info_education_tv.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "学历不能为空", Toast.LENGTH_SHORT).show();
            }  else if (spouse_info_child_count_edt.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "子女数量不能为空", Toast.LENGTH_SHORT).show();
            } else if (spouse_info_current_address_tv.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "现住地址不能为空", Toast.LENGTH_SHORT).show();
            } else if (spouse_info_current_address1_tv.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "现住地址的详细地址不能为空", Toast.LENGTH_SHORT).show();
            } else if (spouse_info_current_address2_tv.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "现住地址的门牌号不能为空", Toast.LENGTH_SHORT).show();
            } else if (spouse_info_live_with_parent_tv.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "是否与父母同住不能为空", Toast.LENGTH_SHORT).show();
            } else if (spouse_info_income_from_tv.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "主要收入来源不能为空", Toast.LENGTH_SHORT).show();
            } else if (spouse_info_income_from_tv.getText().toString().equals("工资") && spouse_info_from_income_year_edt.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "年收入不能为空", Toast.LENGTH_SHORT).show();
            } else if (spouse_info_income_from_tv.getText().toString().equals("工资") && spouse_info_from_income_company_name_edt.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "单位名称不能为空", Toast.LENGTH_SHORT).show();
            } else if (spouse_info_income_from_tv.getText().toString().equals("工资") && spouse_info_from_income_company_address_tv.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "单位地址不能为空", Toast.LENGTH_SHORT).show();
            } else if (spouse_info_income_from_tv.getText().toString().equals("工资") && spouse_info_from_income_company_address1_tv.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "详细地址不能为空", Toast.LENGTH_SHORT).show();
            } else if (spouse_info_income_from_tv.getText().toString().equals("工资") && spouse_info_from_income_company_address2_tv.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "门牌号不能为空", Toast.LENGTH_SHORT).show();
            } else if (spouse_info_income_from_tv.getText().toString().equals("工资") && spouse_info_from_income_work_position_tv.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "职务不能为空", Toast.LENGTH_SHORT).show();
            } else if (spouse_info_income_from_tv.getText().toString().equals("自营") && spouse_info_from_self_year_edt.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "年收入不能为空", Toast.LENGTH_SHORT).show();
            } else if (spouse_info_income_from_tv.getText().toString().equals("自营") && spouse_info_from_self_type_tv.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "业务类型不能为空", Toast.LENGTH_SHORT).show();
            } else if (spouse_info_income_from_tv.getText().toString().equals("自营") && spouse_info_from_self_company_address_tv.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "项目经营地址不能为空", Toast.LENGTH_SHORT).show();
            } else if (spouse_info_income_from_tv.getText().toString().equals("自营") && spouse_info_from_self_company_address1_tv.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "自营的详细地址不能为空", Toast.LENGTH_SHORT).show();
            } else if (spouse_info_income_from_tv.getText().toString().equals("自营") && spouse_info_from_self_company_address2_tv.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "自营的门牌号不能为空", Toast.LENGTH_SHORT).show();
            } else if (spouse_info_income_from_tv.getText().toString().equals("其他") && spouse_info_from_other_year_edt.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "年收入不能为空", Toast.LENGTH_SHORT).show();
            } else if (spouse_info_income_from_tv.getText().toString().equals("其他") && spouse_info_from_other_remark_edt.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "备注不能为空", Toast.LENGTH_SHORT).show();
            } else if (spouse_info_extra_income_from_tv.getText().toString().equals("工资") && spouse_info_extra_from_income_year_edt.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "年收入不能为空", Toast.LENGTH_SHORT).show();
            } else if (spouse_info_extra_income_from_tv.getText().toString().equals("工资") && spouse_info_extra_from_income_company_name_edt.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "单位名称不能为空", Toast.LENGTH_SHORT).show();
            } else if (spouse_info_extra_income_from_tv.getText().toString().equals("工资") && spouse_info_extra_from_income_company_address_tv.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "单位地址不能为空", Toast.LENGTH_SHORT).show();
            } else if (spouse_info_extra_income_from_tv.getText().toString().equals("工资") && spouse_info_extra_from_income_company_address1_tv.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "详细地址不能为空", Toast.LENGTH_SHORT).show();
            } else if (spouse_info_extra_income_from_tv.getText().toString().equals("工资") && spouse_info_extra_from_income_company_address2_tv.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "门牌号不能为空", Toast.LENGTH_SHORT).show();
            } else if (spouse_info_extra_income_from_tv.getText().toString().equals("工资") && spouse_info_extra_from_income_work_position_tv.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "职务不能为空", Toast.LENGTH_SHORT).show();
            }else if (spouse_info_house_type_tv.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "房屋性质不能为空", Toast.LENGTH_SHORT).show();
            } else if (spouse_info_house_area_edt.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "房屋面积不能为空", Toast.LENGTH_SHORT).show();
            } else if (spouse_info_house_owner_name_edt.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "房屋所有权人不能为空", Toast.LENGTH_SHORT).show();
            } else if (spouse_info_house_owner_relation_tv.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "房屋所有权人与申请人关系不能为空", Toast.LENGTH_SHORT).show();
            } else if (spouse_info_urg_contact1_edt.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "紧急联系人人姓名不能为空", Toast.LENGTH_SHORT).show();
            } else if (spouse_info_urg_mobile1_edt.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "手机号不能为空", Toast.LENGTH_SHORT).show();
            } else if (!CheckMobileUtil.checkMobile(spouse_info_urg_mobile1_edt.getText().toString().toString())) {
                Toast.makeText(mContext, "紧急联系人手机号格式错误", Toast.LENGTH_SHORT).show();
            } else if (spouse_info_urg_relation1_tv.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "紧急联系人与申请人关系不能为空", Toast.LENGTH_SHORT).show();
            } else if (spouse_info_urg_contact2_edt.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "紧急联系人姓名不能为空", Toast.LENGTH_SHORT).show();
            } else if (spouse_info_urg_mobile2_edt.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "手机号不能为空", Toast.LENGTH_SHORT).show();
            } else if (!CheckMobileUtil.checkMobile(spouse_info_urg_mobile2_edt.getText().toString().toString())) {
                Toast.makeText(mContext, "紧急联系人手机号格式错误", Toast.LENGTH_SHORT).show();
            } else if (spouse_info_urg_relation2_tv.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "紧急联系人与申请人关系不能为空", Toast.LENGTH_SHORT).show();
            } else {
                return true;
            }
            return false;
        } else if (spouse_info_marriage_tv.getText().toString().equals("丧偶")) {
            if (spouse_info_die_child_count_edt.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "子女数量不能为空", Toast.LENGTH_SHORT).show();
            } else {
                return true;
            }
            return false;
        } else if (spouse_info_marriage_tv.getText().toString().equals("离异")) {
            if (spouse_info_divorced_child_count_edt.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "子女数量不能为空", Toast.LENGTH_SHORT).show();
            } else {
                return true;
            }
            return false;
        } else {
            return true;
        }
    }

    public SpouseInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.spouse_info, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        UBT.bind(this, view, getClass().getSimpleName());
        applyActivity = (ApplyActivity) getActivity();
        spouse_info_mobile_img = (ImageView) view.findViewById(R.id.spouse_info_mobile_img);
        spouse_info_id_back_lin = (LinearLayout) view.findViewById(R.id.spouse_info_id_back_lin);
        spouse_info_id_front_lin = (LinearLayout) view.findViewById(R.id.spouse_info_id_front_lin);
        spouse_info_marriage_lin = (LinearLayout) view.findViewById(R.id.spouse_info_marriage_lin);
        spouse_info_income_from_lin = (LinearLayout) view.findViewById(R.id.spouse_info_income_from_lin);
        spouse_info_extra_income_from_lin = (LinearLayout) view.findViewById(R.id.spouse_info_extra_income_from_lin);
        spouse_info_divorced_lin = (LinearLayout) view.findViewById(R.id.spouse_info_divorced_lin);
        spouse_info_register_addr_lin = (LinearLayout) view.findViewById(R.id.spouse_info_register_addr_lin);
        spouse_info_gender_lin = (LinearLayout) view.findViewById(R.id.spouse_info_gender_lin);
        spouse_info_from_income_company_address_lin = (LinearLayout) view.findViewById(R.id.spouse_info_from_income_company_address_lin);
        spouse_info_from_income_company_address1_lin = (LinearLayout) view.findViewById(R.id.spouse_info_from_income_company_address1_lin);
        spouse_info_from_income_work_position_lin = (LinearLayout) view.findViewById(R.id.spouse_info_from_income_work_position_lin);
        spouse_info_from_self_company_address_lin = (LinearLayout) view.findViewById(R.id.spouse_info_from_self_company_address_lin);
        spouse_info_from_self_company_address1_lin = (LinearLayout) view.findViewById(R.id.spouse_info_from_self_company_address1_lin);
        spouse_info_from_self_type_lin = (LinearLayout) view.findViewById(R.id.spouse_info_from_self_type_lin);
        spouse_info_extra_from_income_company_address_lin = (LinearLayout) view.findViewById(R.id.spouse_info_extra_from_income_company_address_lin);
        spouse_info_extra_from_income_company_address1_lin = (LinearLayout) view.findViewById(R.id.spouse_info_extra_from_income_company_address1_lin);
        spouse_info_extra_from_income_work_position_lin = (LinearLayout) view.findViewById(R.id.spouse_info_extra_from_income_work_position_lin);
        spouse_info_marriage_group_lin = (LinearLayout) view.findViewById(R.id.spouse_info_marriage_group_lin);
        spouse_info_divorced_group_lin = (LinearLayout) view.findViewById(R.id.spouse_info_divorced_group_lin);
        spouse_info_die_group_lin = (LinearLayout) view.findViewById(R.id.spouse_info_die_group_lin);
        spouse_info_from_income_group_lin = (LinearLayout) view.findViewById(R.id.spouse_info_from_income_group_lin);
        spouse_info_from_self_group_lin = (LinearLayout) view.findViewById(R.id.spouse_info_from_self_group_lin);
        spouse_info_from_other_remark_edt = (EditText) view.findViewById(R.id.spouse_info_from_other_remark_edt);
        spouse_info_from_other_year_edt = (EditText) view.findViewById(R.id.spouse_info_from_other_year_edt);

        spouse_info_reg_lin = view.findViewById(R.id.spouse_info_reg_lin);
        spouse_info_education_lin = view.findViewById(R.id.spouse_info_education_lin);
        spouse_info_current_address_lin = view.findViewById(R.id.spouse_info_current_address_lin);
        spouse_info_current_address1_lin = view.findViewById(R.id.spouse_info_current_address1_lin);
        spouse_info_live_with_parent_lin = view.findViewById(R.id.spouse_info_live_with_parent_lin);

        spouse_info_house_type_lin = view.findViewById(R.id.spouse_info_house_type_lin);
        spouse_info_house_owner_relation_lin = view.findViewById(R.id.spouse_info_house_owner_relation_lin);
        spouse_info_urg_relation1_lin = view.findViewById(R.id.spouse_info_urg_relation1_lin);
        spouse_info_urg_relation2_lin = view.findViewById(R.id.spouse_info_urg_relation2_lin);
        spouse_info_urg_mobile1_img = view.findViewById(R.id.spouse_info_urg_mobile1_img);
        spouse_info_urg_mobile2_img = view.findViewById(R.id.spouse_info_urg_mobile2_img);

        step1 = (TextView) view.findViewById(R.id.step1);
        step2 = (TextView) view.findViewById(R.id.step2);
        step3 = (TextView) view.findViewById(R.id.step3);

//        spouse_info_submit_btn.setOnFocusChangeListener((v, hasFocus) -> {
//            if (hasFocus) {
//                spouse_info_submit_btn.clearFocus();
//
//                if (checkCanNextStep()) {
//                    if (spouse_info_marriage_tv.getText().toString() == "已婚") {
//                        clearDoubleCheckItems();
//                        addDoubleCheckItem("姓名", spouse_info_clt_nm_edt.getText().toString());
//                        addDoubleCheckItem("身份证号", spouse_info_id_no_edt.getText().toString());
//                        addDoubleCheckItem("手机号", spouse_info_mobile_edt.getText().toString());
//                        mDoubleCheckDialog.show();
//                    } else {
//                        submit();
//                    }
//                }
//            }
//        });
        spouse_info_mobile_img.setOnClickListener(v -> {
            CURRENT_CLICKED_VIEW_FOR_CONTACT = v.getId();
            selectContact();
        });

        mDoubleCheckChangeBtn.setOnClickListener(v -> {
            mDoubleCheckDialog.dismiss();
        });
        mDoubleCheckSubmitBtn.setOnClickListener(v -> {
            mDoubleCheckDialog.dismiss();
            submit();
        });
        spouse_info_id_back_lin.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, SingleImgUploadForCreateUserActivity.class);
            intent.putExtra("type", Constants.FileLabelType.ID_BACK);
            intent.putExtra("needUploadFidToServer", false);
            intent.putExtra("role", Constants.PersonType.LENDER_SP);
            intent.putExtra("ocrResp", ocrResp);
            intent.putExtra("imgUrl", idBackImgUrl);
            intent.putExtra("objectKey", ID_BACK_FID);
            startActivityForResult(intent, Constants.REQUEST_DOCUMENT);
        });

        spouse_info_id_front_lin.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, SingleImgUploadForCreateUserActivity.class);
            intent.putExtra("type", Constants.FileLabelType.ID_FRONT);
            intent.putExtra("needUploadFidToServer", false);
            intent.putExtra("role", Constants.PersonType.LENDER_SP);
            intent.putExtra("imgUrl", idFrontImgUrl);
            intent.putExtra("objectKey", ID_FRONT_FID);
            startActivityForResult(intent, Constants.REQUEST_DOCUMENT);
        });
        spouse_info_marriage_lin.setOnClickListener(v -> {
            WheelViewUtil.showWheelView(((Yusion4sApp) applyActivity.getApplication()).getConfigResp().marriage_key, _MARRIAGE_INDEX, spouse_info_marriage_lin, spouse_info_marriage_tv, "请选择", (clickedView, selectedIndex) -> {
                _MARRIAGE_INDEX = selectedIndex;
                if ("已婚".equals(((Yusion4sApp) applyActivity.getApplication()).getConfigResp().marriage_value.get(_MARRIAGE_INDEX))) {
                    spouse_info_marriage_group_lin.setVisibility(View.VISIBLE);
                } else {
                    spouse_info_marriage_group_lin.setVisibility(View.GONE);
                }

                if ("离异".equals(((Yusion4sApp) applyActivity.getApplication()).getConfigResp().marriage_value.get(_MARRIAGE_INDEX))) {
                    spouse_info_divorced_group_lin.setVisibility(View.VISIBLE);
                } else {
                    spouse_info_divorced_group_lin.setVisibility(View.GONE);
                }

                if ("丧偶".equals(((Yusion4sApp) applyActivity.getApplication()).getConfigResp().marriage_value.get(_MARRIAGE_INDEX))) {
                    spouse_info_die_group_lin.setVisibility(View.VISIBLE);
                } else {
                    spouse_info_die_group_lin.setVisibility(View.GONE);
                }


            });
        });
        spouse_info_income_from_lin.setOnClickListener(v -> {
            WheelViewUtil.showWheelView(incomelist, _INCOME_FROME_INDEX, spouse_info_income_from_lin, spouse_info_income_from_tv, "请选择", (clickedView, selectedIndex) -> {
                _INCOME_FROME_INDEX = selectedIndex;
                if ("工资".equals(incomelist.get(_INCOME_FROME_INDEX))) {
                    spouse_info_from_income_group_lin.setVisibility(View.VISIBLE);
                } else {
                    spouse_info_from_income_group_lin.setVisibility(View.GONE);
                }

                if ("自营".equals(incomelist.get(_INCOME_FROME_INDEX))) {
                    spouse_info_from_self_group_lin.setVisibility(View.VISIBLE);
                } else {
                    spouse_info_from_self_group_lin.setVisibility(View.GONE);
                }
            });
        });
        spouse_info_extra_income_from_lin.setOnClickListener(v -> {
            WheelViewUtil.showWheelView(incomeextarlist, _EXTRA_INCOME_FROME_INDEX, spouse_info_extra_income_from_lin, spouse_info_extra_income_from_tv, "请选择", (clickedView, selectedIndex) -> {
                _EXTRA_INCOME_FROME_INDEX = selectedIndex;
                if ("工资".equals(incomeextarlist.get(_EXTRA_INCOME_FROME_INDEX))) {
                    view.findViewById(R.id.spouse_info_extra_from_income_group_lin).setVisibility(View.VISIBLE);
                } else {
                    view.findViewById(R.id.spouse_info_extra_from_income_group_lin).setVisibility(View.GONE);
                }

            });
        });
        spouse_info_divorced_lin.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, YusionUploadListActivity.class);
            intent.putExtra("type", Constants.FileLabelType.MARRIAGE_PROOF);
            intent.putExtra("needUploadFidToServer", false);
            intent.putExtra("role", Constants.PersonType.LENDER);
            intent.putExtra("imgList", (Serializable) divorceImgsList);
            intent.putExtra("title", "离婚证");
            startActivityForResult(intent, Constants.REQUEST_MULTI_DOCUMENT);
        });
        spouse_info_register_addr_lin.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, YusionUploadListActivity.class);
            intent.putExtra("type", Constants.FileLabelType.RES_BOOKLET);
            intent.putExtra("needUploadFidToServer", false);
            intent.putExtra("role", Constants.PersonType.LENDER);
            intent.putExtra("imgList", (Serializable) resBookList);
            intent.putExtra("title", "户口本");
            startActivityForResult(intent, Constants.REQUEST_MULTI_DOCUMENT);
        });
        spouse_info_gender_lin.setOnClickListener(v -> {
            WheelViewUtil.showWheelView(((Yusion4sApp) applyActivity.getApplication()).getConfigResp().gender_list_key, _GENDER_INDEX, spouse_info_gender_lin, spouse_info_gender_tv, "请选择", (clickedView, selectedIndex) -> {
                _GENDER_INDEX = selectedIndex;
            });
        });


        //工资
        spouse_info_from_income_company_address_lin.setOnClickListener(v -> {
            WheelViewUtil.showCityWheelView(getClass().getSimpleName(), spouse_info_from_income_company_address_lin, spouse_info_from_income_company_address_tv, "请选择所在地区", (clickedView, city) -> {
                spouse_info_from_income_company_address1_tv.setText("");
            });
        });
        spouse_info_from_income_company_address1_lin.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(spouse_info_from_income_company_address_tv.getText())) {
                CURRENT_CLICKED_VIEW_FOR_ADDRESS = spouse_info_from_income_company_address1_lin.getId();
                requestPOI(spouse_info_from_income_company_address_tv.getText().toString());
            }
        });
        spouse_info_from_income_work_position_lin.setOnClickListener(v -> {
            WheelViewUtil.showWheelView(((Yusion4sApp) applyActivity.getApplication()).getConfigResp().work_position_key, _FROM_INCOME_WORK_POSITION_INDEX, spouse_info_from_income_work_position_lin, spouse_info_from_income_work_position_tv, "请选择", (clickedView, selectedIndex) -> {
                _FROM_INCOME_WORK_POSITION_INDEX = selectedIndex;
            });
        });

        //自营
        spouse_info_from_self_company_address_lin.setOnClickListener(v -> {
            WheelViewUtil.showCityWheelView(getClass().getSimpleName(), spouse_info_from_self_company_address_lin, spouse_info_from_self_company_address_tv, "请选择所在地区", (clickedView, city) -> {
                spouse_info_from_self_company_address1_tv.setText("");
            });
        });
        spouse_info_from_self_company_address1_lin.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(spouse_info_from_self_company_address_tv.getText())) {
                CURRENT_CLICKED_VIEW_FOR_ADDRESS = spouse_info_from_self_company_address1_lin.getId();
                requestPOI(spouse_info_from_self_company_address_tv.getText().toString());
            }
        });
        spouse_info_from_self_type_lin.setOnClickListener(v -> {
            WheelViewUtil.showWheelView(((Yusion4sApp) applyActivity.getApplication()).getConfigResp().busi_type_list_key, _FROM_SELF_TYPE_INDEX, spouse_info_from_self_type_lin, spouse_info_from_self_type_tv, "请选择", (clickedView, selectedIndex) -> {
                _FROM_SELF_TYPE_INDEX = selectedIndex;
                if ("其他".equals(((Yusion4sApp) applyActivity.getApplication()).getConfigResp().busi_type_list_value.get(_FROM_SELF_TYPE_INDEX))) {
                    EditText editText = new EditText(mContext);
                    new AlertDialog.Builder(mContext)
                            .setTitle("请输入业务类型")
                            .setView(editText)
                            .setCancelable(false)
                            .setPositiveButton("确定", (dialog, which) -> {
                                spouse_info_from_self_type_tv.setText(editText.getText());
                                _FROM_SELF_TYPE_INDEX = 0;
                                InputMethodUtil.hideInputMethod(mContext);
                                dialog.dismiss();
                            })
                            .setNegativeButton("取消", (dialog, which) -> {
                                dialog.dismiss();
                                InputMethodUtil.hideInputMethod(mContext);
                                _FROM_SELF_TYPE_INDEX = 0;
                                spouse_info_from_self_type_tv.setText("");
                            }).
                            show();
                }
            });
        });

        //额外工资
        spouse_info_extra_from_income_company_address_lin.setOnClickListener(v -> {
            WheelViewUtil.showCityWheelView(getClass().getSimpleName(), spouse_info_extra_from_income_company_address_lin, spouse_info_extra_from_income_company_address_tv, "请选择所在地区", (clickedView, city) -> {
                spouse_info_extra_from_income_company_address1_tv.setText("");
            });
        });
        spouse_info_extra_from_income_company_address1_lin.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(spouse_info_extra_from_income_company_address_tv.getText())) {
                CURRENT_CLICKED_VIEW_FOR_ADDRESS = spouse_info_extra_from_income_company_address1_lin.getId();
                requestPOI(spouse_info_extra_from_income_company_address_tv.getText().toString());
            }
        });
        spouse_info_extra_from_income_work_position_lin.setOnClickListener(v -> {
            WheelViewUtil.showWheelView(((Yusion4sApp) applyActivity.getApplication()).getConfigResp().work_position_key, _FROM_EXTRA_WORK_POSITION_INDEX, spouse_info_extra_from_income_work_position_lin, spouse_info_extra_from_income_work_position_tv, "请选择", (clickedView, selectedIndex) -> {
                _FROM_EXTRA_WORK_POSITION_INDEX = selectedIndex;
            });
        });
        spouse_info_reg_lin.setOnClickListener(v -> {
            WheelViewUtil.showCityWheelView(getClass().getSimpleName(), spouse_info_reg_lin, spouse_info_reg_tv, "请选择所在地区", (clickedView, city) -> {
            });
        });
        spouse_info_education_lin.setOnClickListener(v -> {
            WheelViewUtil.showWheelView(((Yusion4sApp) applyActivity.getApplication()).getConfigResp().education_list_key, _EDUCATION_INDEX, spouse_info_education_lin, spouse_info_education_tv, "请选择", (clickedView, selectedIndex) -> {
                _EDUCATION_INDEX = selectedIndex;
            });
        });
        spouse_info_current_address_lin.setOnClickListener(v -> {
            WheelViewUtil.showCityWheelView(getClass().getSimpleName(), spouse_info_current_address_lin, spouse_info_current_address_tv, "请选择所在地区", (clickedView, city) -> {
                spouse_info_current_address1_tv.setText("");
            });
        });

        spouse_info_current_address1_lin.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(spouse_info_current_address_tv.getText())) {
                CURRENT_CLICKED_VIEW_FOR_ADDRESS = spouse_info_current_address1_lin.getId();
                requestPOI(spouse_info_current_address_tv.getText().toString());
            }
        });

        //是否与父母同住
        spouse_info_live_with_parent_lin.setOnClickListener(v -> {
            WheelViewUtil.showWheelView(ifwithparentlist, _LIVE_WITH_PARENT_INDEX, spouse_info_live_with_parent_lin, spouse_info_live_with_parent_tv, "请选择", (clickedView, selectedIndex) -> {
                _LIVE_WITH_PARENT_INDEX = selectedIndex;
            });
        });


        spouse_info_house_type_lin.setOnClickListener(v -> {
            WheelViewUtil.showWheelView(((Yusion4sApp) applyActivity.getApplication()).getConfigResp().house_type_list_key, _HOUSE_TYPE_INDEX, spouse_info_house_type_lin, spouse_info_house_type_tv, "请选择", new WheelViewUtil.OnSubmitCallBack() {
                @Override
                public void onSubmitCallBack(View clickedView, int selectedIndex) {
                    _HOUSE_TYPE_INDEX = selectedIndex;
                }
            });
        });


        spouse_info_house_owner_relation_lin.setOnClickListener(v -> {
            WheelViewUtil.showWheelView(((Yusion4sApp) applyActivity.getApplication()).getConfigResp().house_relationship_list_key, _HOUSE_OWNER_RELATION_INDEX, spouse_info_house_owner_relation_lin, spouse_info_house_owner_relation_tv, "请选择", new WheelViewUtil.OnSubmitCallBack() {
                @Override
                public void onSubmitCallBack(View clickedView, int selectedIndex) {
                    _HOUSE_OWNER_RELATION_INDEX = selectedIndex;
                }
            });
        });


        spouse_info_urg_relation1_lin.setOnClickListener(v -> {
            WheelViewUtil.showWheelView(((Yusion4sApp) applyActivity.getApplication()).getConfigResp().urg_rela_relationship_list_key, _URG_RELATION_INDEX1, spouse_info_urg_relation1_lin, spouse_info_urg_relation1_tv, "请选择", new WheelViewUtil.OnSubmitCallBack() {
                @Override
                public void onSubmitCallBack(View clickedView, int selectedIndex) {
                    _URG_RELATION_INDEX1 = selectedIndex;

                }
            });
        });


        spouse_info_urg_relation2_lin.setOnClickListener(v -> {
            WheelViewUtil.showWheelView(((Yusion4sApp) applyActivity.getApplication()).getConfigResp().urg_other_relationship_list_key, _URG_RELATION_INDEX2, spouse_info_urg_relation2_lin, spouse_info_urg_relation2_tv, "请选择", new WheelViewUtil.OnSubmitCallBack() {
                @Override
                public void onSubmitCallBack(View clickedView, int selectedIndex) {
                    _URG_RELATION_INDEX2 = selectedIndex;
                }
            });

        });

        spouse_info_urg_mobile1_img.setOnClickListener(v -> {
            CURRENT_CLICKED_VIEW_FOR_CONTACT = v.getId();
            selectContact();
        });


        spouse_info_urg_mobile2_img.setOnClickListener(v -> {
            CURRENT_CLICKED_VIEW_FOR_CONTACT = v.getId();
            selectContact();
        });
        step1.setOnClickListener(v -> EventBus.getDefault().post(ApplyActivityEvent.showAutonymCertifyFragment));
        step2.setOnClickListener(v -> EventBus.getDefault().post(ApplyActivityEvent.showPersonalInfoFragment));

        step1.setTypeface(createFromAsset(mContext.getAssets(), "yj.ttf"));
        step2.setTypeface(createFromAsset(mContext.getAssets(), "yj.ttf"));
        step3.setTypeface(createFromAsset(mContext.getAssets(), "yj.ttf"));
    }


    private void selectContact() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, Constants.REQUEST_CONTACTS);
    }

    private void requestPOI(String city) {
        if (city != null) {
            String[] citys = city.split("/");
            if (citys.length == 3) {
                String city1 = citys[1];
                String city2 = citys[2];
                Intent intent = new Intent(mContext, AMapPoiListActivity.class);
                intent.putExtra("city", city1);
                intent.putExtra("keywords", city2);
                startActivityForResult(intent, Constants.REQUEST_ADDRESS);
            }
        }
    }

    private void uploadUrl(String cltId, String spouseCltId) {
        ArrayList<UploadFilesUrlReq.FileUrlBean> files = new ArrayList<>();
        switch (applyActivity.mClientInfo.marriage) {
            case "离异":

                for (UploadImgItemBean divorceItem : divorceImgsList) {
                    UploadFilesUrlReq.FileUrlBean divorceFileItem = new UploadFilesUrlReq.FileUrlBean();
                    divorceFileItem.file_id = divorceItem.objectKey;
                    divorceFileItem.label = Constants.FileLabelType.MARRIAGE_PROOF;
                    divorceFileItem.clt_id = cltId;
                    files.add(divorceFileItem);
                }
                break;

            case "丧偶":
                for (UploadImgItemBean resItem : resBookList) {
                    UploadFilesUrlReq.FileUrlBean resBookFileItem = new UploadFilesUrlReq.FileUrlBean();
                    resBookFileItem.file_id = resItem.objectKey;
                    resBookFileItem.label = Constants.FileLabelType.RES_BOOKLET;
                    resBookFileItem.clt_id = cltId;
                    files.add(resBookFileItem);
                }

                break;
            case "已婚":
                UploadFilesUrlReq.FileUrlBean idBackBean = new UploadFilesUrlReq.FileUrlBean();
                idBackBean.file_id = ID_BACK_FID;
                idBackBean.label = Constants.FileLabelType.ID_BACK;
                idBackBean.clt_id = spouseCltId;
                files.add(idBackBean);

                UploadFilesUrlReq.FileUrlBean idFrontBean = new UploadFilesUrlReq.FileUrlBean();
                idFrontBean.file_id = ID_FRONT_FID;
                idFrontBean.label = Constants.FileLabelType.ID_FRONT;
                idFrontBean.clt_id = spouseCltId;
                files.add(idFrontBean);
                break;
            default:
                break;
        }
        UploadFilesUrlReq uploadFilesUrlReq = new UploadFilesUrlReq();
        uploadFilesUrlReq.files = files;
        uploadFilesUrlReq.region = SharedPrefsUtil.getInstance(mContext).getValue("region", "");
        uploadFilesUrlReq.bucket = SharedPrefsUtil.getInstance(mContext).getValue("bucket", "");
        UploadApi.uploadFileUrl(mContext, uploadFilesUrlReq, (code, msg) -> {
            if (code >= 0) {
                nextStep();
                UBT.sendAllUBTEvents(mContext, () -> {

                });
//                nextStep()
            }
        });
    }

    private void nextStep() {
        Log.e("TAG", "spouse : clientinfo = { " + applyActivity.mClientInfo.toString() + " }");
        applyActivity.requestSubmit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == Constants.REQUEST_CONTACTS) {
                Uri uri = data.getData();
                String[] contacts = ContactsUtil.getPhoneContacts(mContext, uri);
                String[] result = new String[2];
                if (contacts != null) {
                    System.arraycopy(contacts, 0, result, 0, contacts.length);
                }
                if (CURRENT_CLICKED_VIEW_FOR_CONTACT == spouse_info_mobile_img.getId()) {
                    spouse_info_mobile_edt.setText(result[1].replace(" ", ""));
                    UBT.addEvent(mContext, "text_change", "edit_text", "spouse_info_mobile_edt", ApplyActivity.class.getSimpleName(), "手机号");
                }
                if (CURRENT_CLICKED_VIEW_FOR_CONTACT == spouse_info_urg_mobile1_img.getId()) {
                    spouse_info_urg_contact1_edt.setText(result[0]);
                    spouse_info_urg_mobile1_edt.setText(result[1].replace(" ", ""));
                    UBT.addEvent(mContext, "text_change", "edit_text", "spouse_info_urg_mobile1_edt", ApplyActivity.class.getSimpleName(), "手机号");
                }
                if (CURRENT_CLICKED_VIEW_FOR_CONTACT == spouse_info_urg_mobile2_img.getId()) {
                    spouse_info_urg_contact2_edt.setText(result[0]);
                    spouse_info_urg_mobile2_edt.setText(result[1].replace(" ", ""));
                    UBT.addEvent(mContext, "text_change", "edit_text", "spouse_info_urg_mobile2_edt", ApplyActivity.class.getSimpleName(), "手机号");
                }
            } else if (requestCode == Constants.REQUEST_DOCUMENT) {
                switch (data.getStringExtra("type")) {
                    case Constants.FileLabelType.ID_BACK:
                        ID_BACK_FID = data.getStringExtra("objectKey");
                        idBackImgUrl = data.getStringExtra("imgUrl");
                        if (!TextUtils.isEmpty(ID_BACK_FID)) {
                            spouse_info_id_back_tv.setText("已上传");
                            spouse_info_id_back_tv.setTextColor(getResources().getColor(R.color.system_color));
                            ocrResp = (OcrResp.ShowapiResBodyBean) data.getSerializableExtra("ocrResp");
                        } else {
                            spouse_info_id_back_tv.setText("请上传");
                            spouse_info_id_back_tv.setTextColor(getResources().getColor(R.color.please_upload_color));
                        }
                        spouse_info_id_no_edt.setText(ocrResp.idNo);
                        spouse_info_clt_nm_edt.setText(ocrResp.name);
                        break;

                    case Constants.FileLabelType.ID_FRONT:
                        ID_FRONT_FID = data.getStringExtra("objectKey");
                        idFrontImgUrl = data.getStringExtra("imgUrl");
                        if (!TextUtils.isEmpty(ID_FRONT_FID)) {
                            spouse_info_id_front_tv.setText("已上传");
                            spouse_info_id_front_tv.setTextColor(getResources().getColor(R.color.system_color));
                        } else {
                            spouse_info_id_front_tv.setText("请上传");
                            spouse_info_id_front_tv.setTextColor(getResources().getColor(R.color.please_upload_color));
                        }
                        break;
                    default:
                        break;
                }
            } else if (requestCode == Constants.REQUEST_MULTI_DOCUMENT) {
                switch (data.getStringExtra("type")) {
                    case Constants.FileLabelType.RES_BOOKLET:
                        resBookList = (ArrayList<UploadImgItemBean>) data.getSerializableExtra("imgList");
                        if (resBookList.size() > 0) {
                            spouse_info_register_addr_tv.setText("已上传");
                            spouse_info_register_addr_tv.setTextColor(getResources().getColor(R.color.system_color));
                        } else {
                            spouse_info_register_addr_tv.setText("请上传");
                            spouse_info_register_addr_tv.setTextColor(getResources().getColor(R.color.please_upload_color));
                        }

                        break;
                    case Constants.FileLabelType.MARRIAGE_PROOF:
                        divorceImgsList = (ArrayList<UploadImgItemBean>) data.getSerializableExtra("imgList");
                        if (divorceImgsList.size() > 0) {
                            spouse_info_divorced_tv.setText("已上传");
                            spouse_info_divorced_tv.setTextColor(getResources().getColor(R.color.system_color));
                        } else {
                            spouse_info_divorced_tv.setText("请上传");
                            spouse_info_divorced_tv.setTextColor(getResources().getColor(R.color.please_upload_color));
                        }
                        break;
                    default:
                        break;
                }
            } else if (requestCode == Constants.REQUEST_ADDRESS) {
                if (spouse_info_from_income_company_address1_lin.getId() == CURRENT_CLICKED_VIEW_FOR_ADDRESS) {
                    spouse_info_from_income_company_address1_tv.setText(data.getStringExtra("result"));
                } else if (spouse_info_from_self_company_address1_lin.getId() == CURRENT_CLICKED_VIEW_FOR_ADDRESS) {
                    spouse_info_from_self_company_address1_tv.setText(data.getStringExtra("result"));
                } else if (spouse_info_extra_from_income_company_address1_lin.getId() == CURRENT_CLICKED_VIEW_FOR_ADDRESS) {
                    spouse_info_extra_from_income_company_address1_tv.setText(data.getStringExtra("result"));
                } else if (spouse_info_current_address1_lin.getId() == CURRENT_CLICKED_VIEW_FOR_ADDRESS) {
                    spouse_info_current_address1_tv.setText(data.getStringExtra("result"));
                }
            }
        }
    }

}