package com.yusion.shanghai.yusion4s.ui.apply;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
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

import com.yusion.shanghai.yusion.event.ApplyActivityEvent;
import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.Yusion4sApp;
import com.yusion.shanghai.yusion4s.base.DoubleCheckFragment;
import com.yusion.shanghai.yusion4s.bean.ocr.OcrResp;
import com.yusion.shanghai.yusion4s.bean.upload.UploadFilesUrlReq;
import com.yusion.shanghai.yusion4s.bean.upload.UploadImgItemBean;
import com.yusion.shanghai.yusion4s.retrofit.api.ProductApi;
import com.yusion.shanghai.yusion4s.retrofit.api.UploadApi;
import com.yusion.shanghai.yusion4s.settings.Constants;
import com.yusion.shanghai.yusion4s.ubt.UBT;
import com.yusion.shanghai.yusion4s.ubt.annotate.BindView;
import com.yusion.shanghai.yusion4s.ui.upload.DocumentActivity;
import com.yusion.shanghai.yusion4s.ui.upload.UploadListActivity;
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

    @BindView(id = R.id.spouse_info_submit_btn, widgetName = "spouse_info_submit_btn", onClick = "submitSpouseInfo")
    private Button spouse_info_submit_btn;

    void submitSpouseInfo(View view) {
        if (checkCanNextStep()) {
            if (spouse_info_marriage_tv.getText().toString() == "已婚") {
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
        applyActivity.getMClientInfo().marriage = spouse_info_marriage_tv.getText().toString();
        if ("已婚".equals(applyActivity.getMClientInfo().marriage)) {
            applyActivity.getMClientInfo().spouse.marriage = "已婚";
            if (ocrResp != null) {
                if (TextUtils.isEmpty(ocrResp.addr)) {
                    applyActivity.getMClientInfo().spouse.reg_addr_details = "";
                } else {
                    applyActivity.getMClientInfo().spouse.reg_addr_details = ocrResp.addr;
                }

                applyActivity.getMClientInfo().spouse.reg_addr.province = ocrResp.province;
                applyActivity.getMClientInfo().spouse.reg_addr.city = ocrResp.city;
                applyActivity.getMClientInfo().spouse.reg_addr.district = ocrResp.town;
            }
            applyActivity.getMClientInfo().spouse.clt_nm = spouse_info_clt_nm_edt.getText().toString();
            applyActivity.getMClientInfo().spouse.id_no = spouse_info_id_no_edt.getText().toString();
            applyActivity.getMClientInfo().spouse.gender = spouse_info_gender_tv.getText().toString();
            applyActivity.getMClientInfo().spouse.mobile = spouse_info_mobile_edt.getText().toString();
            applyActivity.getMClientInfo().child_num = spouse_info_child_count_edt.getText().toString();

            //主要收入来源
            switch (spouse_info_income_from_tv.getText().toString()) {
                case "工资":
                    applyActivity.getMClientInfo().spouse.major_income_type = "工资";
                    applyActivity.getMClientInfo().spouse.major_income = spouse_info_from_income_year_edt.getText().toString();
                    applyActivity.getMClientInfo().spouse.major_company_name = spouse_info_from_income_company_name_edt.getText().toString();
                    if (!TextUtils.isEmpty(spouse_info_from_income_company_address_tv.getText().toString())) {
                        applyActivity.getMClientInfo().spouse.major_company_addr.province = spouse_info_from_income_company_address_tv.getText().toString().trim().split("/")[0];
                        applyActivity.getMClientInfo().spouse.major_company_addr.city = spouse_info_from_income_company_address_tv.getText().toString().trim().split("/")[1];
                        applyActivity.getMClientInfo().spouse.major_company_addr.district = spouse_info_from_income_company_address_tv.getText().toString().trim().split("/")[2];
                    } else {
                        applyActivity.getMClientInfo().spouse.major_company_addr.province = "";
                        applyActivity.getMClientInfo().spouse.major_company_addr.city = "";
                        applyActivity.getMClientInfo().spouse.major_company_addr.district = "";
                    }
                    applyActivity.getMClientInfo().spouse.major_company_addr.address1 = spouse_info_from_income_company_address1_tv.getText().toString();
                    applyActivity.getMClientInfo().spouse.major_company_addr.address2 = spouse_info_from_income_company_address2_tv.getText().toString();
                    applyActivity.getMClientInfo().spouse.major_work_position = spouse_info_from_income_work_position_tv.getText().toString();
                    applyActivity.getMClientInfo().spouse.major_work_phone_num = spouse_info_from_income_work_phone_num_edt.getText().toString();
                    break;
                case "自营":
                    applyActivity.getMClientInfo().spouse.major_income_type = "自营";
                    applyActivity.getMClientInfo().spouse.major_income = spouse_info_from_self_year_edt.getText().toString();
                    applyActivity.getMClientInfo().spouse.major_busi_type = spouse_info_from_self_type_tv.getText().toString();
                    applyActivity.getMClientInfo().spouse.major_company_name = spouse_info_from_self_company_name_edt.getText().toString();
                    if (TextUtils.isEmpty(spouse_info_from_self_company_address_tv.getText())) {
                        applyActivity.getMClientInfo().spouse.major_company_addr.province = "";
                        applyActivity.getMClientInfo().spouse.major_company_addr.city = "";
                        applyActivity.getMClientInfo().spouse.major_company_addr.district = "";
                    } else {
                        applyActivity.getMClientInfo().spouse.major_company_addr.province = spouse_info_from_self_company_address_tv.getText().toString().trim().split("/")[0];
                        applyActivity.getMClientInfo().spouse.major_company_addr.city = spouse_info_from_self_company_address_tv.getText().toString().trim().split("/")[1];
                        ;
                        applyActivity.getMClientInfo().spouse.major_company_addr.district = spouse_info_from_self_company_address_tv.getText().toString().trim().split("/")[2];
                    }
                    applyActivity.getMClientInfo().spouse.major_company_addr.address1 = spouse_info_from_self_company_address1_tv.getText().toString();
                    applyActivity.getMClientInfo().spouse.major_company_addr.address2 = spouse_info_from_self_company_address2_tv.getText().toString();
                    break;
                case "其他":
                    applyActivity.getMClientInfo().spouse.major_income_type = "其他";
                    applyActivity.getMClientInfo().spouse.major_income = spouse_info_from_other_year_edt.getText().toString();
                    applyActivity.getMClientInfo().spouse.major_remark = spouse_info_from_other_remark_edt.getText().toString();

                    break;
                default:
                    break;
            }
            //额外收入来源
            switch (spouse_info_extra_income_from_tv.getText().toString()) {
                case "工资":
                    applyActivity.getMClientInfo().spouse.extra_income_type = "工资";
                    applyActivity.getMClientInfo().spouse.extra_income = spouse_info_extra_from_income_year_edt.getText().toString();
                    applyActivity.getMClientInfo().spouse.extra_company_name = spouse_info_extra_from_income_company_name_edt.getText().toString();
                    applyActivity.getMClientInfo().spouse.extra_company_addr.province = spouse_info_extra_from_income_company_address_tv.getText().toString().trim().split("/")[0];
                    applyActivity.getMClientInfo().spouse.extra_company_addr.city = spouse_info_extra_from_income_company_address_tv.getText().toString().trim().split("/")[1];
                    applyActivity.getMClientInfo().spouse.extra_company_addr.district = spouse_info_extra_from_income_company_address_tv.getText().toString().trim().split("/")[2];
                    applyActivity.getMClientInfo().spouse.extra_company_addr.address1 = spouse_info_extra_from_income_company_address1_tv.getText().toString();
                    applyActivity.getMClientInfo().spouse.extra_company_addr.address2 = spouse_info_extra_from_income_company_address2_tv.getText().toString();
                    applyActivity.getMClientInfo().spouse.extra_work_position = spouse_info_extra_from_income_work_position_tv.getText().toString();
                    applyActivity.getMClientInfo().spouse.extra_work_phone_num = spouse_info_extra_from_income_work_phone_num_edt.getText().toString();
                    break;
                case "无":
                    applyActivity.getMClientInfo().spouse.extra_income_type = "无";
                    break;
                default:
                    break;
            }
        } else if ("离异".equals(applyActivity.getMClientInfo().marriage)) {
            applyActivity.getMClientInfo().child_num = spouse_info_divorced_child_count_edt.getText().toString();
        } else if ("丧偶".equals(applyActivity.getMClientInfo().marriage)) {
            applyActivity.getMClientInfo().child_num = spouse_info_die_child_count_edt.getText().toString();
        }
        Log.e("current_addr2--------", applyActivity.getMClientInfo().current_addr.province);
        Log.e("current_addr2--------", applyActivity.getMClientInfo().current_addr.city);
        Log.e("current_addr2--------", applyActivity.getMClientInfo().current_addr.district);
        Log.e("current_addr2--------", applyActivity.getMClientInfo().current_addr.address1);
        Log.e("current_addr2--------", applyActivity.getMClientInfo().current_addr.address2);

        FileUtil.saveLog(applyActivity.getMClientInfo().toString());
        ProductApi.updateClientInfo(mContext, applyActivity.getMClientInfo(), data1 -> {
            if (data1 != null) {
                applyActivity.setMClientInfo(data1);
                uploadUrl(applyActivity.getMClientInfo().clt_id, applyActivity.getMClientInfo().spouse.clt_id);
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
            } else if (spouse_info_mobile_edt.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "手机号不能为空", Toast.LENGTH_SHORT).show();
            } else if (!CheckMobileUtil.checkMobile(spouse_info_mobile_edt.getText().toString())) {
                Toast.makeText(mContext, "手机号码格式错误", Toast.LENGTH_SHORT).show();
            } else if (spouse_info_child_count_edt.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "子女数量不能为空", Toast.LENGTH_SHORT).show();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.spouse_info, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        onclick(view);
    }

    private void initView(View view) {
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
        step1 = (TextView) view.findViewById(R.id.step1);
        step2 = (TextView) view.findViewById(R.id.step2);
        step3 = (TextView) view.findViewById(R.id.step3);
    }


    private void onclick(View view) {
        UBT.bind(this, view, getClass().getSimpleName());

        spouse_info_mobile_img.setOnClickListener(v -> {
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
            Intent intent = new Intent(mContext, DocumentActivity.class);
            intent.putExtra("type", Constants.FileLabelType.ID_BACK);
            intent.putExtra("needUploadFidToServer", false);
            intent.putExtra("role", Constants.PersonType.LENDER_SP);
            intent.putExtra("ocrResp", ocrResp);
            intent.putExtra("imgUrl", idBackImgUrl);
            intent.putExtra("objectKey", ID_BACK_FID);
            startActivityForResult(intent, Constants.REQUEST_DOCUMENT);
        });

        spouse_info_id_front_lin.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, DocumentActivity.class);
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
                if (((Yusion4sApp) applyActivity.getApplication()).getConfigResp().marriage_value.get(_MARRIAGE_INDEX) == "已婚") {
                    spouse_info_marriage_group_lin.setVisibility(View.VISIBLE);
                } else {
                    spouse_info_marriage_group_lin.setVisibility(View.GONE);
                }

                if (((Yusion4sApp) applyActivity.getApplication()).getConfigResp().marriage_value.get(_MARRIAGE_INDEX) == "离异") {
                    spouse_info_divorced_group_lin.setVisibility(View.VISIBLE);
                } else {
                    spouse_info_divorced_group_lin.setVisibility(View.GONE);
                }

                if (((Yusion4sApp) applyActivity.getApplication()).getConfigResp().marriage_value.get(_MARRIAGE_INDEX) == "丧偶") {
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
            WheelViewUtil.showWheelView(incomelist, _EXTRA_INCOME_FROME_INDEX, spouse_info_extra_income_from_lin, spouse_info_extra_income_from_tv, "请选择", (clickedView, selectedIndex) -> {
                _EXTRA_INCOME_FROME_INDEX = selectedIndex;
                if ("工资".equals(incomelist.get(_EXTRA_INCOME_FROME_INDEX))) {
                    view.findViewById(R.id.spouse_info_extra_from_income_group_lin).setVisibility(View.VISIBLE);
                } else {
                    view.findViewById(R.id.spouse_info_extra_from_income_group_lin).setVisibility(View.GONE);
                }

            });
        });
        spouse_info_divorced_lin.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, UploadListActivity.class);
            intent.putExtra("type", Constants.FileLabelType.MARRIAGE_PROOF);
            intent.putExtra("needUploadFidToServer", false);
            intent.putExtra("role", Constants.PersonType.LENDER);
            intent.putExtra("imgList", (Serializable) divorceImgsList);
            intent.putExtra("title", "离婚证");
            startActivityForResult(intent, Constants.REQUEST_MULTI_DOCUMENT);
        });
        spouse_info_register_addr_lin.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, UploadListActivity.class);
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
        switch (applyActivity.getMClientInfo().marriage) {
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
                spouse_info_mobile_edt.setText(result[1].replace(" ", ""));
                UBT.addEvent(mContext, "text_change", "edit_text", "spouse_info_mobile_edt", ApplyActivity.class.getSimpleName(), "手机号");
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
                }
            }
        }
    }
}