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
import com.yusion.shanghai.yusion4s.bean.user.ClientInfo;
import com.yusion.shanghai.yusion4s.event.ApplyActivityEvent;
import com.yusion.shanghai.yusion4s.settings.Constants;
import com.yusion.shanghai.yusion4s.ubt.UBT;
import com.yusion.shanghai.yusion4s.ubt.annotate.BindView;
import com.yusion.shanghai.yusion4s.utils.CheckMobileUtil;
import com.yusion.shanghai.yusion4s.utils.ContactsUtil;
import com.yusion.shanghai.yusion4s.utils.InputMethodUtil;
import com.yusion.shanghai.yusion4s.utils.wheel.WheelViewUtil;
import com.yusion.shanghai.yusion4s.widget.NoEmptyEditText;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Typeface.createFromAsset;


public class PersonalInfoFragment extends DoubleCheckFragment {

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
    private int CURRENT_CLICKED_VIEW_FOR_CONTACT = 0;
    private int CURRENT_CLICKED_VIEW_FOR_ADDRESS = 0;
    private int _GENDER_INDEX = 0;
    private int _INCOME_FROM_INDEX = 0;
    private int _EDUCATION_INDEX = 0;
    private int _EXTRA_INCOME_FROM_INDEX = 0;
    private int _LIVE_WITH_PARENT_INDEX = 0;
    private int _FROM_INCOME_WORK_POSITION_INDEX = 0;
    private int _FROM_SELF_TYPE_INDEX = 0;
    private int _FROM_EXTRA_WORK_POSITION_INDEX = 0;
    private int _HOUSE_TYPE_INDEX = 0;
    private int _HOUSE_OWNER_RELATION_INDEX = 0;
    private int _URG_RELATION_INDEX1 = 0;
    private int _URG_RELATION_INDEX2 = 0;

    @BindView(id = R.id.personal_info_gender_tv, widgetName = "personal_info_gender_tv")
    private TextView personal_info_gender_tv;

    @BindView(id = R.id.personal_info_reg_tv, widgetName = "personal_info_reg_tv")
    private TextView personal_info_reg_tv;

    @BindView(id = R.id.personal_info_mobile_edt, widgetName = "personal_info_mobile_edt")
    private EditText personal_info_mobile_edt;

    @BindView(id = R.id.personal_info_education_tv, widgetName = "personal_info_education_tv")
    private TextView personal_info_education_tv;

    @BindView(id = R.id.personal_info_current_address_tv, widgetName = "personal_info_current_address_tv")
    private TextView personal_info_current_address_tv;

    @BindView(id = R.id.personal_info_current_address1_tv, widgetName = "personal_info_current_address1_tv")
    private TextView personal_info_current_address1_tv;

    @BindView(id = R.id.personal_info_current_address2_tv, widgetName = "personal_info_current_address2_tv")
    private NoEmptyEditText personal_info_current_address2_tv;

    @BindView(id = R.id.personal_info_live_with_parent_tv, widgetName = "personal_info_live_with_parent_tv")
    private TextView personal_info_live_with_parent_tv;

    //主要
    @BindView(id = R.id.personal_info_income_from_tv, widgetName = "personal_info_income_from_tv")
    private TextView personal_info_income_from_tv;

    //主要工资
    @BindView(id = R.id.personal_info_from_income_year_edt, widgetName = "personal_info_from_income_year_edt")
    private EditText personal_info_from_income_year_edt;

    @BindView(id = R.id.personal_info_from_income_company_name_edt, widgetName = "personal_info_from_income_company_name_edt")
    private EditText personal_info_from_income_company_name_edt;

    @BindView(id = R.id.personal_info_from_income_company_address_tv, widgetName = "personal_info_from_income_company_address_tv")
    private TextView personal_info_from_income_company_address_tv;

    @BindView(id = R.id.personal_info_from_income_company_address1_tv, widgetName = "personal_info_from_income_company_address1_tv")
    private TextView personal_info_from_income_company_address1_tv;

    @BindView(id = R.id.personal_info_from_income_company_address2_tv, widgetName = "personal_info_from_income_company_address2_tv")
    private TextView personal_info_from_income_company_address2_tv;

    @BindView(id = R.id.personal_info_from_income_work_position_tv, widgetName = "personal_info_from_income_work_position_tv")
    private TextView personal_info_from_income_work_position_tv;

    @BindView(id = R.id.personal_info_from_income_work_phone_num_edt, widgetName = "personal_info_from_income_work_phone_num_edt")
    private EditText personal_info_from_income_work_phone_num_edt;

    //主要自营
    @BindView(id = R.id.personal_info_from_self_year_edt, widgetName = "personal_info_from_self_year_edt")
    private EditText personal_info_from_self_year_edt;

    @BindView(id = R.id.personal_info_from_self_type_tv, widgetName = "personal_info_from_self_type_tv")
    private TextView personal_info_from_self_type_tv;

    @BindView(id = R.id.personal_info_from_self_company_name_edt, widgetName = "personal_info_from_self_company_name_edt")
    private EditText personal_info_from_self_company_name_edt;

    @BindView(id = R.id.personal_info_from_self_company_address_tv, widgetName = "personal_info_from_self_company_address_tv")
    private TextView personal_info_from_self_company_address_tv;

    @BindView(id = R.id.personal_info_from_self_company_address1_tv, widgetName = "personal_info_from_self_company_address1_tv")
    private TextView personal_info_from_self_company_address1_tv;

    @BindView(id = R.id.personal_info_from_self_company_address2_tv, widgetName = "personal_info_from_self_company_address2_tv")
    private NoEmptyEditText personal_info_from_self_company_address2_tv;


    //额外工资
    @BindView(id = R.id.personal_info_extra_income_from_tv, widgetName = "personal_info_extra_income_from_tv")
    private TextView personal_info_extra_income_from_tv;

    @BindView(id = R.id.personal_info_extra_from_income_year_edt, widgetName = "personal_info_extra_from_income_year_edt")
    private EditText personal_info_extra_from_income_year_edt;

    @BindView(id = R.id.personal_info_extra_from_income_company_name_edt, widgetName = "personal_info_extra_from_income_company_name_edt")
    private EditText personal_info_extra_from_income_company_name_edt;

    @BindView(id = R.id.personal_info_extra_from_income_company_address_tv, widgetName = "personal_info_extra_from_income_company_address_tv")
    private TextView personal_info_extra_from_income_company_address_tv;

    @BindView(id = R.id.personal_info_extra_from_income_company_address1_tv, widgetName = "personal_info_extra_from_income_company_address1_tv")
    private TextView personal_info_extra_from_income_company_address1_tv;

    @BindView(id = R.id.personal_info_extra_from_income_company_address2_tv, widgetName = "personal_info_extra_from_income_company_address2_tv")
    private NoEmptyEditText personal_info_extra_from_income_company_address2_tv;

    @BindView(id = R.id.personal_info_extra_from_income_work_position_tv, widgetName = "personal_info_extra_from_income_work_position_tv")
    private TextView personal_info_extra_from_income_work_position_tv;

    @BindView(id = R.id.personal_info_extra_from_income_work_phone_num_edt, widgetName = "personal_info_extra_from_income_work_phone_num_edt")
    private EditText personal_info_extra_from_income_work_phone_num_edt;


    @BindView(id = R.id.personal_info_house_type_tv, widgetName = "personal_info_house_type_tv")
    private TextView personal_info_house_type_tv;

    @BindView(id = R.id.personal_info_house_area_edt, widgetName = "personal_info_house_area_edt")
    private EditText personal_info_house_area_edt;

    @BindView(id = R.id.personal_info_house_owner_name_edt, widgetName = "personal_info_house_owner_name_edt")
    private EditText personal_info_house_owner_name_edt;

    @BindView(id = R.id.personal_info_house_owner_relation_tv, widgetName = "personal_info_house_owner_relation_tv")
    private TextView personal_info_house_owner_relation_tv;

    @BindView(id = R.id.personal_info_urg_relation1_tv, widgetName = "personal_info_urg_relation1_tv")
    private TextView personal_info_urg_relation1_tv;

    @BindView(id = R.id.personal_info_urg_mobile1_edt, widgetName = "personal_info_urg_mobile1_edt")
    private EditText personal_info_urg_mobile1_edt;

    @BindView(id = R.id.personal_info_urg_contact1_edt, widgetName = "personal_info_urg_contact1_edt")
    private EditText personal_info_urg_contact1_edt;

    @BindView(id = R.id.personal_info_urg_relation2_tv, widgetName = "personal_info_urg_relation2_tv")
    private TextView personal_info_urg_relation2_tv;

    @BindView(id = R.id.personal_info_urg_mobile2_edt, widgetName = "personal_info_urg_mobile2_edt")
    private EditText personal_info_urg_mobile2_edt;

    @BindView(id = R.id.personal_info_urg_contact2_edt, widgetName = "personal_info_urg_contact2_edt")
    private EditText personal_info_urg_contact2_edt;

    @BindView(id = R.id.personal_info_next_btn, widgetName = "personal_info_next_btn", onClick = "submitPersonalInfo")
    private Button personal_info_next_btn;


    private TextView personal_info_clt_nm_edt;
    private TextView personal_info_id_no_edt;
    private TextView personal_info_from_other_year_edt;
    private TextView personal_info_from_other_remark_edt;
    private LinearLayout personal_info_gender_lin;
    private LinearLayout personal_info_income_from_lin;
    private LinearLayout personal_info_extra_income_from_lin;
    private LinearLayout personal_info_live_with_parent_lin;
    private LinearLayout personal_info_reg_lin;
    private LinearLayout personal_info_education_lin;
    private LinearLayout personal_info_current_address_lin;
    private LinearLayout personal_info_current_address1_lin;
    private LinearLayout personal_info_from_income_company_address_lin;
    private LinearLayout personal_info_from_income_company_address1_lin;
    private LinearLayout personal_info_from_income_work_position_lin;
    private LinearLayout personal_info_from_self_company_address_lin;
    private LinearLayout personal_info_from_self_type_lin;
    private LinearLayout personal_info_from_self_company_address1_lin;
    private LinearLayout personal_info_extra_from_income_company_address_lin;
    private LinearLayout personal_info_extra_from_income_company_address1_lin;
    private LinearLayout personal_info_extra_from_income_work_position_lin;
    private LinearLayout personal_info_house_type_lin;
    private LinearLayout personal_info_house_owner_relation_lin;
    private LinearLayout personal_info_urg_relation1_lin;
    private LinearLayout personal_info_urg_relation2_lin;
    private ImageView personal_info_urg_mobile1_img;
    private ImageView personal_info_urg_mobile2_img;
    private TextView step1;
    private TextView step2;
    private TextView step3;
    private CreateUserActivity createUserActivity;

    void submitPersonalInfo(View view) {
        if (checkCanNextStep()) {
            submit();
        }
    }

    private void submit() {
        if (!TextUtils.isEmpty(personal_info_reg_tv.getText().toString())) {
            createUserActivity.mClientInfo.reg_addr.province = personal_info_reg_tv.getText().toString().trim().split("/")[0];
            createUserActivity.mClientInfo.reg_addr.city = personal_info_reg_tv.getText().toString().trim().split("/")[1];
            createUserActivity.mClientInfo.reg_addr.district = personal_info_reg_tv.getText().toString().trim().split("/")[2];
        }
        createUserActivity.mClientInfo.gender = personal_info_gender_tv.getText().toString();
        createUserActivity.mClientInfo.mobile = personal_info_mobile_edt.getText().toString();
        createUserActivity.mClientInfo.edu = personal_info_education_tv.getText().toString();

        //现住地址
        if (!TextUtils.isEmpty(personal_info_current_address_tv.getText().toString())) {
            createUserActivity.mClientInfo.current_addr.province = personal_info_current_address_tv.getText().toString().trim().split("/")[0];
            createUserActivity.mClientInfo.current_addr.city = personal_info_current_address_tv.getText().toString().trim().split("/")[1];
            createUserActivity.mClientInfo.current_addr.district = personal_info_current_address_tv.getText().toString().trim().split("/")[2];
        }
        createUserActivity.mClientInfo.current_addr.address1 = (personal_info_current_address1_tv.getText().toString());
        createUserActivity.mClientInfo.current_addr.address2 = (personal_info_current_address2_tv.getText().toString());
        createUserActivity.mClientInfo.is_live_with_parent = (personal_info_live_with_parent_tv.getText().toString());
        Log.e("current_addr1--------", createUserActivity.mClientInfo.current_addr.province);
        Log.e("current_addr1--------", createUserActivity.mClientInfo.current_addr.city);
        Log.e("current_addr1--------", createUserActivity.mClientInfo.current_addr.district);
        Log.e("current_addr1--------", createUserActivity.mClientInfo.current_addr.address1);
        Log.e("current_addr1--------", createUserActivity.mClientInfo.current_addr.address2);


        //主要收入来源
        switch (personal_info_income_from_tv.getText().toString()) {
            case "工资":
                createUserActivity.mClientInfo.major_income_type = "工资";
                createUserActivity.mClientInfo.major_income = personal_info_from_income_year_edt.getText().toString();
                createUserActivity.mClientInfo.major_company_name = personal_info_from_income_company_name_edt.getText().toString();
                createUserActivity.mClientInfo.major_company_addr.province = personal_info_from_income_company_address_tv.getText().toString().trim().split("/")[0];
                createUserActivity.mClientInfo.major_company_addr.city = personal_info_from_income_company_address_tv.getText().toString().trim().split("/")[1];
                createUserActivity.mClientInfo.major_company_addr.district = personal_info_from_income_company_address_tv.getText().toString().trim().split("/")[2];
                createUserActivity.mClientInfo.major_company_addr.address1 = personal_info_from_income_company_address1_tv.getText().toString();
                createUserActivity.mClientInfo.major_company_addr.address2 = personal_info_from_income_company_address2_tv.getText().toString();
                createUserActivity.mClientInfo.major_work_position = personal_info_from_income_work_position_tv.getText().toString();
                createUserActivity.mClientInfo.major_work_phone_num = personal_info_from_income_work_phone_num_edt.getText().toString();
                break;

            case "自营":
                createUserActivity.mClientInfo.major_income_type = "自营";
                createUserActivity.mClientInfo.major_income = personal_info_from_self_year_edt.getText().toString();
                createUserActivity.mClientInfo.major_busi_type = personal_info_from_self_type_tv.getText().toString();
                createUserActivity.mClientInfo.major_company_name = personal_info_from_self_company_name_edt.getText().toString();
                if (TextUtils.isEmpty(personal_info_from_self_company_address_tv.getText().toString())) {
                    createUserActivity.mClientInfo.major_company_addr.province = "";
                    createUserActivity.mClientInfo.major_company_addr.city = "";
                    createUserActivity.mClientInfo.major_company_addr.district = "";
                } else {
                    createUserActivity.mClientInfo.major_company_addr.province = personal_info_from_self_company_address_tv.getText().toString().trim().split("/")[0];
                    createUserActivity.mClientInfo.major_company_addr.city = personal_info_from_self_company_address_tv.getText().toString().trim().split("/")[1];
                    createUserActivity.mClientInfo.major_company_addr.district = personal_info_from_self_company_address_tv.getText().toString().trim().split("/")[2];
                }
                createUserActivity.mClientInfo.major_company_addr.address1 = personal_info_from_self_company_address1_tv.getText().toString();
                createUserActivity.mClientInfo.major_company_addr.address2 = personal_info_from_self_company_address2_tv.getText().toString();
                break;
            case "其他":
                createUserActivity.mClientInfo.major_income_type = "其他";
                createUserActivity.mClientInfo.major_income = personal_info_from_other_year_edt.getText().toString();
                createUserActivity.mClientInfo.major_remark = personal_info_from_other_remark_edt.getText().toString();
                break;
            default:
                break;

        }
        //额外收入来源
        switch (personal_info_extra_income_from_tv.getText().toString()) {
            case "工资":
                createUserActivity.mClientInfo.extra_income_type = "工资";
                createUserActivity.mClientInfo.extra_income = personal_info_extra_from_income_year_edt.getText().toString();
                createUserActivity.mClientInfo.extra_company_name = personal_info_extra_from_income_company_name_edt.getText().toString();
                createUserActivity.mClientInfo.extra_company_addr.province = personal_info_extra_from_income_company_address_tv.getText().toString().trim().split("/")[0];
                createUserActivity.mClientInfo.extra_company_addr.city = personal_info_extra_from_income_company_address_tv.getText().toString().trim().split("/")[1];
                createUserActivity.mClientInfo.extra_company_addr.district = personal_info_extra_from_income_company_address_tv.getText().toString().trim().split("/")[2];
                createUserActivity.mClientInfo.extra_company_addr.address1 = personal_info_extra_from_income_company_address1_tv.getText().toString();
                createUserActivity.mClientInfo.extra_company_addr.address2 = personal_info_extra_from_income_company_address2_tv.getText().toString();
                createUserActivity.mClientInfo.extra_work_position = personal_info_extra_from_income_work_position_tv.getText().toString();
                createUserActivity.mClientInfo.extra_work_phone_num = personal_info_extra_from_income_work_phone_num_edt.getText().toString();
                break;
            case "无":
                createUserActivity.mClientInfo.extra_income_type = "无";
                break;
            default:
                break;
        }

        //房屋性质
        createUserActivity.mClientInfo.house_owner_name = personal_info_house_owner_name_edt.getText().toString();
        createUserActivity.mClientInfo.house_owner_relation = personal_info_house_owner_relation_tv.getText().toString();
        createUserActivity.mClientInfo.house_type = personal_info_house_type_tv.getText().toString();
        createUserActivity.mClientInfo.house_area = personal_info_house_area_edt.getText().toString();
        createUserActivity.mClientInfo.urg_contact1 = personal_info_urg_contact1_edt.getText().toString();
        createUserActivity.mClientInfo.urg_mobile1 = personal_info_urg_mobile1_edt.getText().toString();
        createUserActivity.mClientInfo.urg_relation1 = personal_info_urg_relation1_tv.getText().toString();
        createUserActivity.mClientInfo.urg_contact2 = personal_info_urg_contact2_edt.getText().toString();
        createUserActivity.mClientInfo.urg_mobile2 = personal_info_urg_mobile2_edt.getText().toString();
        createUserActivity.mClientInfo.urg_relation2 = personal_info_urg_relation2_tv.getText().toString();
        TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        createUserActivity.mClientInfo.imei = telephonyManager.getDeviceId();
        nextStep();
    }

    public PersonalInfoFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.personal_info, container, false);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            ClientInfo clientInfoBean = createUserActivity.mClientInfo;
            personal_info_clt_nm_edt.setText(clientInfoBean.clt_nm);
            personal_info_id_no_edt.setText(clientInfoBean.id_no);
            personal_info_mobile_edt.setText(clientInfoBean.mobile);
            if (!TextUtils.isEmpty(clientInfoBean.gender)) {
                personal_info_gender_tv.setText(clientInfoBean.gender);
            }
            Boolean empty = !TextUtils.isEmpty(clientInfoBean.reg_addr.province);
            Boolean notEmpty = !TextUtils.isEmpty(clientInfoBean.reg_addr.city);
            Boolean notEmpty1 = !TextUtils.isEmpty(clientInfoBean.reg_addr.district);
            if (empty && notEmpty && notEmpty1) {
                personal_info_reg_tv.setText(clientInfoBean.reg_addr.province + "/" + clientInfoBean.reg_addr.city + "/" + clientInfoBean.reg_addr.district);
            }
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
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
                if (CURRENT_CLICKED_VIEW_FOR_CONTACT == personal_info_urg_mobile1_img.getId()) {
                    personal_info_urg_contact1_edt.setText(result[0]);
                    personal_info_urg_mobile1_edt.setText(result[1].replace(" ", ""));
                    UBT.addEvent(mContext, "text_change", "edit_text", "personal_info_urg_mobile1_edt", CreateUserActivity.class.getSimpleName(), "手机号");
                }
                if (CURRENT_CLICKED_VIEW_FOR_CONTACT == personal_info_urg_mobile2_img.getId()) {
                    personal_info_urg_contact2_edt.setText(result[0]);
                    personal_info_urg_mobile2_edt.setText(result[1].replace(" ", ""));
                    UBT.addEvent(mContext, "text_change", "edit_text", "personal_info_urg_mobile2_edt", CreateUserActivity.class.getSimpleName(), "手机号");
                }
            } else if (requestCode == Constants.REQUEST_ADDRESS) {
                if (CURRENT_CLICKED_VIEW_FOR_ADDRESS == personal_info_current_address1_lin.getId()) {
                    personal_info_current_address1_tv.setText(data.getStringExtra("result"));
                }
                if (CURRENT_CLICKED_VIEW_FOR_ADDRESS == personal_info_from_income_company_address1_lin.getId()) {
                    personal_info_from_income_company_address1_tv.setText(data.getStringExtra("result"));
                }
                if (CURRENT_CLICKED_VIEW_FOR_ADDRESS == personal_info_from_self_company_address1_lin.getId()) {
                    personal_info_from_self_company_address1_tv.setText(data.getStringExtra("result"));
                }
                if (CURRENT_CLICKED_VIEW_FOR_ADDRESS == personal_info_extra_from_income_company_address1_lin.getId()) {
                    personal_info_extra_from_income_company_address1_tv.setText(data.getStringExtra("result"));
                }
            }
        }
    }

    private void initView(View view) {
        UBT.bind(this, view, CreateUserActivity.class.getSimpleName());
        createUserActivity = (CreateUserActivity) getActivity();
        personal_info_clt_nm_edt = view.findViewById(R.id.personal_info_clt_nm_edt);
        personal_info_id_no_edt = view.findViewById(R.id.personal_info_id_no_edt);
        personal_info_from_other_year_edt = view.findViewById(R.id.personal_info_from_other_year_edt);
        personal_info_from_other_remark_edt = view.findViewById(R.id.personal_info_from_other_remark_edt);
        personal_info_gender_lin = view.findViewById(R.id.personal_info_gender_lin);
        personal_info_income_from_lin = view.findViewById(R.id.personal_info_income_from_lin);
        personal_info_extra_income_from_lin = view.findViewById(R.id.personal_info_extra_income_from_lin);
        personal_info_live_with_parent_lin = view.findViewById(R.id.personal_info_live_with_parent_lin);
        personal_info_reg_lin = view.findViewById(R.id.personal_info_reg_lin);
        personal_info_education_lin = view.findViewById(R.id.personal_info_education_lin);
        personal_info_current_address_lin = view.findViewById(R.id.personal_info_current_address_lin);
        personal_info_current_address1_lin = view.findViewById(R.id.personal_info_current_address1_lin);
        personal_info_from_income_company_address_lin = view.findViewById(R.id.personal_info_from_income_company_address_lin);
        personal_info_from_income_company_address1_lin = view.findViewById(R.id.personal_info_from_income_company_address1_lin);
        personal_info_from_income_work_position_lin = view.findViewById(R.id.personal_info_from_income_work_position_lin);
        personal_info_from_self_company_address_lin = view.findViewById(R.id.personal_info_from_self_company_address_lin);
        personal_info_from_self_type_lin = view.findViewById(R.id.personal_info_from_self_type_lin);
        personal_info_from_self_company_address1_lin = view.findViewById(R.id.personal_info_from_self_company_address1_lin);
        personal_info_extra_from_income_company_address_lin = view.findViewById(R.id.personal_info_extra_from_income_company_address_lin);
        personal_info_extra_from_income_company_address1_lin = view.findViewById(R.id.personal_info_extra_from_income_company_address1_lin);
        personal_info_extra_from_income_work_position_lin = view.findViewById(R.id.personal_info_extra_from_income_work_position_lin);
        personal_info_house_type_lin = view.findViewById(R.id.personal_info_house_type_lin);
        personal_info_house_owner_relation_lin = view.findViewById(R.id.personal_info_house_owner_relation_lin);
        personal_info_urg_relation1_lin = view.findViewById(R.id.personal_info_urg_relation1_lin);
        personal_info_urg_relation2_lin = view.findViewById(R.id.personal_info_urg_relation2_lin);
        personal_info_urg_mobile1_img = view.findViewById(R.id.personal_info_urg_mobile1_img);
        personal_info_urg_mobile2_img = view.findViewById(R.id.personal_info_urg_mobile2_img);
        step1 = view.findViewById(R.id.step1);
        step2 = view.findViewById(R.id.step2);
        step3 = view.findViewById(R.id.step3);



        mDoubleCheckChangeBtn.setOnClickListener(v -> {
            mDoubleCheckDialog.dismiss();
        });
        mDoubleCheckSubmitBtn.setOnClickListener(v -> {
            mDoubleCheckDialog.dismiss();
        });
        personal_info_gender_lin.setOnClickListener(v -> {
            WheelViewUtil.showWheelView(((Yusion4sApp) createUserActivity.getApplication()).getConfigResp().gender_list_key, _GENDER_INDEX, personal_info_gender_lin, personal_info_gender_tv, "请选择", (clickedView, selectedIndex) -> _GENDER_INDEX = selectedIndex);

        });
        //主要收入来源
        personal_info_income_from_lin.setOnClickListener(v -> {
            WheelViewUtil.showWheelView(incomelist, _INCOME_FROM_INDEX, personal_info_income_from_lin, personal_info_income_from_tv, "请选择", (clickedView, index) -> {
                _INCOME_FROM_INDEX = index;
                if (incomelist.get(_INCOME_FROM_INDEX).equals("工资")) {
                    view.findViewById(R.id.personal_info_from_income_group_lin).setVisibility(View.VISIBLE);
                } else {
                    view.findViewById(R.id.personal_info_from_income_group_lin).setVisibility(View.GONE);
                }
                if (incomelist.get(_INCOME_FROM_INDEX).equals("自营")) {
                    view.findViewById(R.id.personal_info_from_self_group_lin).setVisibility(View.VISIBLE);
                } else {
                    view.findViewById(R.id.personal_info_from_self_group_lin).setVisibility(View.GONE);
                }

            });
        });
        //额外收入来源
        personal_info_extra_income_from_lin.setOnClickListener(v -> {
            WheelViewUtil.showWheelView(incomeextarlist, _EXTRA_INCOME_FROM_INDEX, personal_info_extra_income_from_lin, personal_info_extra_income_from_tv, "请选择", (clickedView, index) -> {
                _EXTRA_INCOME_FROM_INDEX = index;
                if (incomeextarlist.get(_EXTRA_INCOME_FROM_INDEX).equals("工资")) {
                    view.findViewById(R.id.personal_info_extra_from_income_group_lin).setVisibility(View.VISIBLE);
                } else {
                    view.findViewById(R.id.personal_info_extra_from_income_group_lin).setVisibility(View.GONE);
                }
            });
        });


        //是否与父母同住
        personal_info_live_with_parent_lin.setOnClickListener(v -> {
            WheelViewUtil.showWheelView(ifwithparentlist, _LIVE_WITH_PARENT_INDEX, personal_info_live_with_parent_lin, personal_info_live_with_parent_tv, "请选择", (clickedView, selectedIndex) -> {
                _LIVE_WITH_PARENT_INDEX = selectedIndex;
            });
        });
        //户籍地
        personal_info_reg_lin.setOnClickListener(v -> {
            WheelViewUtil.showCityWheelView(getClass().getSimpleName(), personal_info_reg_lin, personal_info_reg_tv, "请选择所在地区", (clickedView, city) -> {
            });
        });
        //学历
        personal_info_education_lin.setOnClickListener(v -> {
            WheelViewUtil.showWheelView(((Yusion4sApp) createUserActivity.getApplication()).getConfigResp().education_list_key, _EDUCATION_INDEX, personal_info_education_lin, personal_info_education_tv, "请选择", (clickedView, selectedIndex) -> {
                _EDUCATION_INDEX = selectedIndex;
            });
        });
        //现住地址
        personal_info_current_address_lin.setOnClickListener(v -> {
            WheelViewUtil.showCityWheelView(getClass().getSimpleName(), personal_info_current_address_lin, personal_info_current_address_tv, "请选择所在地区", (clickedView, city) -> {
                personal_info_current_address1_tv.setText("");
            });
        });
        //现住地址详细地址
        personal_info_current_address1_lin.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(personal_info_current_address_tv.getText())) {
                CURRENT_CLICKED_VIEW_FOR_ADDRESS = personal_info_current_address1_lin.getId();
                requestPOI(personal_info_current_address_tv.getText().toString());
            }
        });

        //工资  公司地址
        personal_info_from_income_company_address_lin.setOnClickListener(v -> {
            WheelViewUtil.showCityWheelView(getClass().getSimpleName(), personal_info_from_income_company_address_lin, personal_info_from_income_company_address_tv, "请选择所在地区", (clickedView, city) -> {
                personal_info_from_income_company_address1_tv.setText("");
            });
        });
        // 工资 公司地址  详细地址
        personal_info_from_income_company_address1_lin.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(personal_info_from_income_company_address_tv.getText())) {
                CURRENT_CLICKED_VIEW_FOR_ADDRESS = personal_info_from_income_company_address1_lin.getId();
                requestPOI(personal_info_from_income_company_address_tv.getText().toString());
            }
        });
        //工资 职位
        personal_info_from_income_work_position_lin.setOnClickListener(v -> {
            WheelViewUtil.showWheelView(((Yusion4sApp) createUserActivity.getApplication()).getConfigResp().work_position_key, _FROM_INCOME_WORK_POSITION_INDEX, personal_info_from_income_work_position_lin, personal_info_from_income_work_position_tv, "请选择", (clickedView, index) ->
                    _FROM_INCOME_WORK_POSITION_INDEX = index);
        });

        //自营
        personal_info_from_self_company_address_lin.setOnClickListener(v -> {
            WheelViewUtil.showCityWheelView(getClass().getSimpleName(), personal_info_from_self_company_address_lin, personal_info_from_self_company_address_tv, "请选择所在地区", (clickedView, city) -> personal_info_from_self_company_address1_tv.setText(""));
        });
        //自营业务类型
        personal_info_from_self_type_lin.setOnClickListener(v -> {
            WheelViewUtil.showWheelView(((Yusion4sApp) createUserActivity.getApplication()).getConfigResp().busi_type_list_key, _FROM_SELF_TYPE_INDEX, personal_info_from_self_type_lin, personal_info_from_self_type_tv, "请选择", (clickedView, selectedIndex) -> {
                _FROM_SELF_TYPE_INDEX = selectedIndex;
                if (((Yusion4sApp) createUserActivity.getApplication()).getConfigResp().busi_type_list_value.get(_FROM_SELF_TYPE_INDEX).equals("其他")) {
                    EditText editText = new EditText(mContext);
                    new AlertDialog.Builder(mContext)
                            .setTitle("请输入业务类型")
                            .setView(editText)
                            .setCancelable(false)
                            .setPositiveButton("确定", (dialog, which) -> {
                                personal_info_from_self_type_tv.setText(editText.getText());
                                _FROM_SELF_TYPE_INDEX = 0;
                                InputMethodUtil.hideInputMethod(mContext);
                                dialog.dismiss();
                            })
                            .setNegativeButton("取消", (dialog, which) -> {
                                dialog.dismiss();
                                _FROM_SELF_TYPE_INDEX = 0;
                                personal_info_from_self_type_tv.setText("");
                                InputMethodUtil.hideInputMethod(mContext);
                            })
                            .show();
                }
            });
        });
        //自营公司地址
        personal_info_from_self_company_address1_lin.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(personal_info_from_self_company_address_tv.getText())) {
                CURRENT_CLICKED_VIEW_FOR_ADDRESS = personal_info_from_self_company_address1_lin.getId();
                requestPOI(personal_info_from_self_company_address_tv.getText().toString());
            }
        });

        //额外工资
        personal_info_extra_from_income_company_address_lin.setOnClickListener(v -> {
            WheelViewUtil.showCityWheelView(getClass().getSimpleName(), personal_info_extra_from_income_company_address_lin, personal_info_extra_from_income_company_address_tv, "请选择所在地区", new WheelViewUtil.OnCitySubmitCallBack() {
                @Override
                public void onCitySubmitCallBack(View clickedView, String city) {
                    personal_info_extra_from_income_company_address1_tv.setText("");
                }
            });
        });
        //额外公司地址
        personal_info_extra_from_income_company_address1_lin.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(personal_info_extra_from_income_company_address_tv.getText())) {
                CURRENT_CLICKED_VIEW_FOR_ADDRESS = personal_info_extra_from_income_company_address1_lin.getId();
                requestPOI(personal_info_extra_from_income_company_address_tv.getText().toString());
            }

        });
        //额外公司职务
        personal_info_extra_from_income_work_position_lin.setOnClickListener(v -> {
            WheelViewUtil.showWheelView(((Yusion4sApp) createUserActivity.getApplication()).getConfigResp().work_position_key, _FROM_EXTRA_WORK_POSITION_INDEX, personal_info_extra_from_income_work_position_lin, personal_info_extra_from_income_work_position_tv, "请选择", new WheelViewUtil.OnSubmitCallBack() {
                @Override
                public void onSubmitCallBack(View clickedView, int selectedIndex) {
                    _FROM_EXTRA_WORK_POSITION_INDEX = selectedIndex;
                }
            });

        });
        //房屋性质
        personal_info_house_type_lin.setOnClickListener(v -> {
            WheelViewUtil.showWheelView(((Yusion4sApp) createUserActivity.getApplication()).getConfigResp().house_type_list_key, _HOUSE_TYPE_INDEX, personal_info_house_type_lin, personal_info_house_type_tv, "请选择", new WheelViewUtil.OnSubmitCallBack() {
                @Override
                public void onSubmitCallBack(View clickedView, int selectedIndex) {
                    _HOUSE_TYPE_INDEX = selectedIndex;
                }
            });
        });
        //房屋与主贷人关系
        personal_info_house_owner_relation_lin.setOnClickListener(v -> {
            WheelViewUtil.showWheelView(((Yusion4sApp) createUserActivity.getApplication()).getConfigResp().house_relationship_list_key, _HOUSE_OWNER_RELATION_INDEX, personal_info_house_owner_relation_lin, personal_info_house_owner_relation_tv, "请选择", new WheelViewUtil.OnSubmitCallBack() {
                @Override
                public void onSubmitCallBack(View clickedView, int selectedIndex) {
                    _HOUSE_OWNER_RELATION_INDEX = selectedIndex;
                }
            });
        });
        //紧急联系人1与主贷人关系
        personal_info_urg_relation1_lin.setOnClickListener(v -> {
            WheelViewUtil.showWheelView(((Yusion4sApp) createUserActivity.getApplication()).getConfigResp().urg_rela_relationship_list_key, _URG_RELATION_INDEX1, personal_info_urg_relation1_lin, personal_info_urg_relation1_tv, "请选择", new WheelViewUtil.OnSubmitCallBack() {
                @Override
                public void onSubmitCallBack(View clickedView, int selectedIndex) {
                    _URG_RELATION_INDEX1 = selectedIndex;

                }
            });
        });
        //紧急联系人2与主贷人关系
        personal_info_urg_relation2_lin.setOnClickListener(v -> {
            WheelViewUtil.showWheelView(((Yusion4sApp) createUserActivity.getApplication()).getConfigResp().urg_other_relationship_list_key, _URG_RELATION_INDEX2, personal_info_urg_relation2_lin, personal_info_urg_relation2_tv, "请选择", new WheelViewUtil.OnSubmitCallBack() {
                @Override
                public void onSubmitCallBack(View clickedView, int selectedIndex) {
                    _URG_RELATION_INDEX2 = selectedIndex;
                }
            });

        });
        //紧急联系人1手机号
        personal_info_urg_mobile1_img.setOnClickListener(v -> {
            CURRENT_CLICKED_VIEW_FOR_CONTACT = v.getId();
            selectContact();
        });
        //紧急联系人2手机号
        personal_info_urg_mobile2_img.setOnClickListener(v -> {
            CURRENT_CLICKED_VIEW_FOR_CONTACT = v.getId();
            selectContact();
        });

        step1.setOnClickListener(v -> EventBus.getDefault().post(ApplyActivityEvent.showAutonymCertifyFragment));

        step1.setTypeface(createFromAsset(mContext.getAssets(), "yj.ttf"));
        step2.setTypeface(createFromAsset(mContext.getAssets(), "yj.ttf"));
        step3.setTypeface(createFromAsset(mContext.getAssets(), "yj.ttf"));
    }

    private void nextStep() {
        Log.e("TAG", "personal : clientinfo = {"+ createUserActivity.mClientInfo.toString()+"}");
        EventBus.getDefault().post(ApplyActivityEvent.showSpouseInfoFragment);
    }
    //选择详细地址
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
    //选择通讯录的手机号
    private void selectContact() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, Constants.REQUEST_CONTACTS);
    }
    //判空校验
    private boolean checkCanNextStep() {
        if (personal_info_gender_tv.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "性别不能为空", Toast.LENGTH_SHORT).show();
        } else if (personal_info_reg_tv.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "户籍地不能为空", Toast.LENGTH_SHORT).show();
        } else if (personal_info_mobile_edt.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "手机号不能为空", Toast.LENGTH_SHORT).show();
        } else if (!CheckMobileUtil.checkMobile(personal_info_mobile_edt.getText().toString().toString())) {
            Toast.makeText(mContext, "手机号码格式错误", Toast.LENGTH_SHORT).show();
        } else if (personal_info_education_tv.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "学历不能为空", Toast.LENGTH_SHORT).show();
        } else if (personal_info_current_address_tv.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "现住地址不能为空", Toast.LENGTH_SHORT).show();
        } else if (personal_info_current_address1_tv.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "现住地址的详细地址不能为空", Toast.LENGTH_SHORT).show();
        } else if (personal_info_current_address2_tv.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "现住地址的门牌号不能为空", Toast.LENGTH_SHORT).show();
        } else if (personal_info_live_with_parent_tv.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "是否与父母同住不能为空", Toast.LENGTH_SHORT).show();
        } else if (personal_info_income_from_tv.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "主要收入来源不能为空", Toast.LENGTH_SHORT).show();
        } else if (personal_info_income_from_tv.getText().toString().equals("工资") && personal_info_from_income_year_edt.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "年收入不能为空", Toast.LENGTH_SHORT).show();
        } else if (personal_info_income_from_tv.getText().toString().equals("工资") && personal_info_from_income_company_name_edt.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "单位名称不能为空", Toast.LENGTH_SHORT).show();
        } else if (personal_info_income_from_tv.getText().toString().equals("工资") && personal_info_from_income_company_address_tv.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "单位地址不能为空", Toast.LENGTH_SHORT).show();
        } else if (personal_info_income_from_tv.getText().toString().equals("工资") && personal_info_from_income_company_address1_tv.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "详细地址不能为空", Toast.LENGTH_SHORT).show();
        } else if (personal_info_income_from_tv.getText().toString().equals("工资") && personal_info_from_income_company_address2_tv.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "门牌号不能为空", Toast.LENGTH_SHORT).show();
        } else if (personal_info_income_from_tv.getText().toString().equals("工资") && personal_info_from_income_work_position_tv.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "职务不能为空", Toast.LENGTH_SHORT).show();
        } else if (personal_info_income_from_tv.getText().toString().equals("自营") && personal_info_from_self_year_edt.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "年收入不能为空", Toast.LENGTH_SHORT).show();
        } else if (personal_info_income_from_tv.getText().toString().equals("自营") && personal_info_from_self_type_tv.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "业务类型不能为空", Toast.LENGTH_SHORT).show();
        } else if (personal_info_income_from_tv.getText().toString().equals("自营") && personal_info_from_self_company_address_tv.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "项目经营地址不能为空", Toast.LENGTH_SHORT).show();
        } else if (personal_info_income_from_tv.getText().toString().equals("自营") && personal_info_from_self_company_address1_tv.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "自营的详细地址不能为空", Toast.LENGTH_SHORT).show();
        } else if (personal_info_income_from_tv.getText().toString().equals("自营") && personal_info_from_self_company_address2_tv.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "自营的门牌号不能为空", Toast.LENGTH_SHORT).show();
        } else if (personal_info_extra_income_from_tv.getText().toString().equals("工资") && personal_info_extra_from_income_year_edt.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "年收入不能为空", Toast.LENGTH_SHORT).show();
        } else if (personal_info_extra_income_from_tv.getText().toString().equals("工资") && personal_info_extra_from_income_company_name_edt.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "单位名称不能为空", Toast.LENGTH_SHORT).show();
        } else if (personal_info_extra_income_from_tv.getText().toString().equals("工资") && personal_info_extra_from_income_company_address_tv.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "单位地址不能为空", Toast.LENGTH_SHORT).show();
        } else if (personal_info_extra_income_from_tv.getText().toString().equals("工资") && personal_info_extra_from_income_company_address1_tv.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "详细地址不能为空", Toast.LENGTH_SHORT).show();
        } else if (personal_info_extra_income_from_tv.getText().toString().equals("工资") && personal_info_extra_from_income_company_address2_tv.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "门牌号不能为空", Toast.LENGTH_SHORT).show();
        } else if (personal_info_extra_income_from_tv.getText().toString().equals("工资") && personal_info_extra_from_income_work_position_tv.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "职务不能为空", Toast.LENGTH_SHORT).show();
        } else if (personal_info_house_type_tv.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "房屋性质不能为空", Toast.LENGTH_SHORT).show();
        } else if (personal_info_house_area_edt.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "房屋面积不能为空", Toast.LENGTH_SHORT).show();
        } else if (personal_info_house_owner_name_edt.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "房屋所有权人不能为空", Toast.LENGTH_SHORT).show();
        } else if (personal_info_house_owner_relation_tv.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "房屋所有权人与申请人关系不能为空", Toast.LENGTH_SHORT).show();
        } else if (personal_info_urg_contact1_edt.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "紧急联系人人姓名不能为空", Toast.LENGTH_SHORT).show();
        } else if (personal_info_urg_mobile1_edt.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "手机号不能为空", Toast.LENGTH_SHORT).show();
        } else if (!CheckMobileUtil.checkMobile(personal_info_urg_mobile1_edt.getText().toString().toString())) {
            Toast.makeText(mContext, "紧急联系人手机号格式错误", Toast.LENGTH_SHORT).show();
        } else if (personal_info_urg_relation1_tv.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "紧急联系人与申请人关系不能为空", Toast.LENGTH_SHORT).show();
        } else if (personal_info_urg_contact2_edt.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "紧急联系人姓名不能为空", Toast.LENGTH_SHORT).show();
        } else if (personal_info_urg_mobile2_edt.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "手机号不能为空", Toast.LENGTH_SHORT).show();
        } else if (!CheckMobileUtil.checkMobile(personal_info_urg_mobile2_edt.getText().toString().toString())) {
            Toast.makeText(mContext, "紧急联系人手机号格式错误", Toast.LENGTH_SHORT).show();
        } else if (personal_info_urg_relation2_tv.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "紧急联系人与申请人关系不能为空", Toast.LENGTH_SHORT).show();
        } else {
            return true;
        }
        return false;
    }
}
