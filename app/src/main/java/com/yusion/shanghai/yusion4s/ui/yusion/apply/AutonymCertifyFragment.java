package com.yusion.shanghai.yusion4s.ui.yusion.apply;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.Log;
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
import com.yusion.shanghai.yusion4s.base.DoubleCheckFragment;
import com.yusion.shanghai.yusion4s.bean.auth.Check3ElementsResp;
import com.yusion.shanghai.yusion4s.bean.ocr.OcrResp;
import com.yusion.shanghai.yusion4s.bean.upload.UploadFilesUrlReq;
import com.yusion.shanghai.yusion4s.bean.user.ClientInfo;
import com.yusion.shanghai.yusion4s.bean.user.GetClientInfoReq;
import com.yusion.shanghai.yusion4s.event.ApplyActivityEvent;
import com.yusion.shanghai.yusion4s.retrofit.api.ProductApi;
import com.yusion.shanghai.yusion4s.retrofit.api.UploadApi;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.settings.Constants;
import com.yusion.shanghai.yusion4s.settings.Settings;
import com.yusion.shanghai.yusion4s.ubt.UBT;
import com.yusion.shanghai.yusion4s.ubt.annotate.BindView;
import com.yusion.shanghai.yusion4s.ui.yusion.DocumentActivity;
import com.yusion.shanghai.yusion4s.utils.CheckIdCardValidUtil;
import com.yusion.shanghai.yusion4s.utils.CheckMobileUtil;
import com.yusion.shanghai.yusion4s.utils.PopupDialogUtil;
import com.yusion.shanghai.yusion4s.utils.SharedPrefsUtil;
import com.yusion.shanghai.yusion4s.utils.wheel.WheelViewUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import static android.graphics.Typeface.createFromAsset;
import static com.yusion.shanghai.yusion4s.R.layout.autonym_certify;

public class AutonymCertifyFragment extends DoubleCheckFragment {

    private int _DIR_REL_INDEX = 0;

    private String ID_BACK_FID = "";
    private String ID_FRONT_FID = "";
    private String DRI_FID = "";

    private String drivingLicImgUrl = "";
    private String idBackImgUrl = "";
    private String idFrontImgUrl = "";

    private OcrResp.ShowapiResBodyBean ocrResp = new OcrResp.ShowapiResBodyBean();

    @BindView(id = R.id.autonym_certify_id_back_tv, widgetName = "autonym_certify_id_back_tv")
    private TextView autonym_certify_id_back_tv;

    @BindView(id = R.id.autonym_certify_id_front_tv, widgetName = "autonym_certify_id_front_tv")
    private TextView autonym_certify_id_front_tv;

    @BindView(id = R.id.autonym_certify_name_tv, widgetName = "autonym_certify_name_tv")
    private EditText autonym_certify_name_tv;

    @BindView(id = R.id.autonym_certify_id_number_tv, widgetName = "autonym_certify_id_number_tv")
    private EditText autonym_certify_id_number_tv;

    @BindView(id = R.id.autonym_certify_mobile_tv, widgetName = "autonym_certify_mobile_tv")
    private EditText autonym_certify_mobile_tv;

    @BindView(id = R.id.autonym_certify_driving_license_tv, widgetName = "autonym_certify_driving_license_tv")
    private TextView autonym_certify_driving_license_tv;

    @BindView(id = R.id.autonym_certify_driving_license_rel_tv, widgetName = "autonym_certify_driving_license_rel_tv")
    private TextView autonym_certify_driving_license_rel_tv;

    @BindView(id = R.id.autonym_certify_next_btn, widgetName = "autonym_certify_next_btn", onClick = "submitAutonymCertify")
    private Button autonym_certify_next_btn;

    private LinearLayout autonym_certify_warnning_lin;
    private LinearLayout autonym_certify_driving_license_rel_lin;
    private LinearLayout autonym_certify_id_back_lin;
    private LinearLayout autonym_certify_id_front_lin;
    private LinearLayout autonym_certify_driving_license_lin;
    private LinearLayout autonym_certify_id_number_lin;
    //    private TextView personal_info_clt_nm_edt;
//    private TextView personal_info_id_no_edt;
//    private TextView personal_info_gender_tv;
//    private TextView personal_info_reg_tv;
    private TextView step1;
    private TextView step2;
    private TextView step3;
    private ApplyActivity applyActivity;


    void submitAutonymCertify(View view) {
        autonym_certify_next_btn.setFocusable(true);
        autonym_certify_next_btn.setFocusableInTouchMode(true);
        autonym_certify_next_btn.requestFocus();
        autonym_certify_next_btn.requestFocusFromTouch();
    }

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    ViewCompat.animate(autonym_certify_warnning_lin).translationY(-autonym_certify_warnning_lin.getHeight() * 1.0f).setDuration(1000).start();
                    break;
                default:
                    break;
            }
        }
    };

    public AutonymCertifyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(autonym_certify, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);

        onClick(view);

    }

    private void onClick(View view) {
        UBT.bind(this, view, ApplyActivity.class.getSimpleName());

        autonym_certify_id_number_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!autonym_certify_id_number_tv.isEnabled()) {
                    Toast.makeText(mContext, "请重新拍摄身份证人像面并识别成功！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mDoubleCheckChangeBtn.setOnClickListener(v -> {
            mDoubleCheckDialog.dismiss();
        });

        mDoubleCheckSubmitBtn.setOnClickListener(v -> {
            mDoubleCheckDialog.dismiss();

            checkMobile(data -> {
                ProductApi.getClientInfo(mContext, new GetClientInfoReq(autonym_certify_id_number_tv.getText().toString(), autonym_certify_name_tv.getText().toString(), autonym_certify_mobile_tv.getText().toString()), Yusion4sApp.TOKEN, data1 -> {
                    if (data1 == null) {
                        return;
                    }
                    applyActivity.mClientInfo = data1;

                    if (ocrResp != null) {
                        applyActivity.mClientInfo.gender = ocrResp.sex;
                        if (TextUtils.isEmpty(ocrResp.addr)) {
                            applyActivity.mClientInfo.reg_addr_details = "";
                        } else {
                            applyActivity.mClientInfo.reg_addr_details = ocrResp.addr;

                        }
                        applyActivity.mClientInfo.reg_addr.province = ocrResp.province;
                        applyActivity.mClientInfo.reg_addr.city = ocrResp.city;
                        applyActivity.mClientInfo.reg_addr.district = ocrResp.town;
                    }


                    applyActivity.mClientInfo.drv_lic_relationship = ((Yusion4sApp) applyActivity.getApplication()).getConfigResp().drv_lic_relationship_list_value.get(_DIR_REL_INDEX);
                    Log.e("TAG", "mClientInfo: {" + applyActivity.mClientInfo.toString() + "}");

                    uploadUrl(data1.clt_id);
//                nextStep()
                });
            });
        });

        autonym_certify_next_btn.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                autonym_certify_next_btn.clearFocus();

//                if (!Settings.isOnline) {
//                    nextStep();
//                    return;
//                }

                if (checkCanNextStep()) {
                    clearDoubleCheckItems();
                    addDoubleCheckItem("姓名", autonym_certify_name_tv.getText().toString());
                    addDoubleCheckItem("身份证号", autonym_certify_id_number_tv.getText().toString());
                    mDoubleCheckDialog.show();
                }

            }
        });
        autonym_certify_driving_license_rel_lin.setOnClickListener(v -> {
            WheelViewUtil.showWheelView(((Yusion4sApp) applyActivity.getApplication()).getConfigResp().drv_lic_relationship_list_key, _DIR_REL_INDEX, autonym_certify_driving_license_rel_lin, autonym_certify_driving_license_rel_tv, "请选择", new WheelViewUtil.OnSubmitCallBack() {
                @Override
                public void onSubmitCallBack(View clickedView, int selectedIndex) {
                    _DIR_REL_INDEX = selectedIndex;
                }
            });

        });
        autonym_certify_id_back_lin.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, DocumentActivity.class);
            intent.putExtra("type", Constants.FileLabelType.ID_BACK);
            intent.putExtra("role", Constants.PersonType.LENDER);
            intent.putExtra("imgUrl", idBackImgUrl);
            intent.putExtra("needUploadFidToServer", false);
            intent.putExtra("objectKey", ID_BACK_FID);
            intent.putExtra("ocrResp", ocrResp);
            startActivityForResult(intent, Constants.REQUEST_DOCUMENT);
        });
        autonym_certify_id_front_lin.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, DocumentActivity.class);
            intent.putExtra("type", Constants.FileLabelType.ID_FRONT);
            intent.putExtra("role", Constants.PersonType.LENDER);
            intent.putExtra("imgUrl", idFrontImgUrl);
            intent.putExtra("needUploadFidToServer", false);
            intent.putExtra("objectKey", ID_FRONT_FID);
            startActivityForResult(intent, Constants.REQUEST_DOCUMENT);
        });
        autonym_certify_driving_license_lin.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, DocumentActivity.class);
            intent.putExtra("type", Constants.FileLabelType.DRI_LIC);
            intent.putExtra("role", Constants.PersonType.LENDER);
            intent.putExtra("imgUrl", drivingLicImgUrl);
            intent.putExtra("needUploadFidToServer", false);
            intent.putExtra("objectKey", DRI_FID);
            startActivityForResult(intent, Constants.REQUEST_DOCUMENT);
        });


        step1.setTypeface(createFromAsset(mContext.getAssets(), "yj.ttf"));
        step2.setTypeface(createFromAsset(mContext.getAssets(), "yj.ttf"));
        step3.setTypeface(createFromAsset(mContext.getAssets(), "yj.ttf"));

        if (Settings.isShameData) {
            autonym_certify_name_tv.setText("just Test");
            autonym_certify_id_number_tv.setText("${Date().time}");
            ID_BACK_FID = "test";
            ID_FRONT_FID = "test";
        }

        autonym_certify_warnning_lin.post(() -> handler.sendEmptyMessageDelayed(0, 2000));
    }

    private void checkMobile(OnItemDataCallBack callBack) {
        ProductApi.check3Elements(mContext, new GetClientInfoReq(autonym_certify_id_number_tv.getText().toString(), autonym_certify_name_tv.getText().toString(), autonym_certify_mobile_tv.getText().toString()), Yusion4sApp.TOKEN, new OnItemDataCallBack<Check3ElementsResp>() {
            @Override
            public void onItemDataCallBack(Check3ElementsResp data) {
                if (data != null) {
                    if (data.match.equals("1")) {
                        callBack.onItemDataCallBack(true);
                    } else {
                        PopupDialogUtil.checkInfoDialog(mContext, "手机号未实名", "手机号码不存在", "手机号用户与身份证不匹配", dialog -> {
                            if (!applyActivity.isFinishing()) {
                                dialog.dismiss();
                            }
                        });
                    }
                }
            }
        });

    }

    private void initView(View view) {
        applyActivity = (ApplyActivity) getActivity();
//        autonym_certify_id_number_tv = (EditText) view.findViewById(R.id.autonym_certify_id_number_tv);
//        autonym_certify_name_tv = (EditText) view.findViewById(R.id.autonym_certify_name_tv);
        autonym_certify_warnning_lin = (LinearLayout) view.findViewById(R.id.autonym_certify_warnning_lin);
        autonym_certify_driving_license_rel_lin = (LinearLayout) view.findViewById(R.id.autonym_certify_driving_license_rel_lin);
        autonym_certify_id_back_lin = (LinearLayout) view.findViewById(R.id.autonym_certify_id_back_lin);
        autonym_certify_id_front_lin = (LinearLayout) view.findViewById(R.id.autonym_certify_id_front_lin);
        autonym_certify_driving_license_lin = (LinearLayout) view.findViewById(R.id.autonym_certify_driving_license_lin);
        autonym_certify_id_number_lin = (LinearLayout) view.findViewById(R.id.autonym_certify_id_number_lin);
        step1 = (TextView) view.findViewById(R.id.step1);
        step2 = (TextView) view.findViewById(R.id.step2);
        step3 = (TextView) view.findViewById(R.id.step3);

//        personal_info_clt_nm_edt = (TextView) view.findViewById(R.id.personal_info_clt_nm_edt);
//        personal_info_id_no_edt = (TextView) view.findViewById(R.id.personal_info_id_no_edt);
//        personal_info_gender_tv = (TextView) view.findViewById(R.id.personal_info_gender_tv);
//        personal_info_reg_tv = (TextView) view.findViewById(R.id.personal_info_reg_tv);
    }

    private void uploadUrl(String cltId) {
        UploadFilesUrlReq.FileUrlBean idBackBean = new UploadFilesUrlReq.FileUrlBean();
        idBackBean.file_id = ID_BACK_FID;
        idBackBean.label = Constants.FileLabelType.ID_BACK;
        idBackBean.clt_id = cltId;

        UploadFilesUrlReq.FileUrlBean idFrontBean = new UploadFilesUrlReq.FileUrlBean();
        idFrontBean.file_id = ID_FRONT_FID;
        idFrontBean.label = Constants.FileLabelType.ID_FRONT;
        idFrontBean.clt_id = cltId;

        UploadFilesUrlReq.FileUrlBean driBean = new UploadFilesUrlReq.FileUrlBean();
        driBean.file_id = DRI_FID;
        driBean.label = Constants.FileLabelType.DRI_LIC;
        driBean.clt_id = cltId;

        ArrayList<UploadFilesUrlReq.FileUrlBean> files = new ArrayList<>();
        files.add(idBackBean);
        files.add(idFrontBean);
        files.add(driBean);

        UploadFilesUrlReq uploadFilesUrlReq = new UploadFilesUrlReq();
        uploadFilesUrlReq.files = files;
        uploadFilesUrlReq.region = SharedPrefsUtil.getInstance(mContext).getValue("region", "");
        uploadFilesUrlReq.bucket = SharedPrefsUtil.getInstance(mContext).getValue("bucket", "");

        UploadApi.uploadFileUrl(mContext, uploadFilesUrlReq, (code, msg) -> {
            if (code >= 0) {
                nextStep();
            }
        });

    }


    private Boolean checkCanNextStep() {
        if (Settings.isShameData) {
            return true;
        }
        if (ID_BACK_FID.isEmpty()) {
            Toast.makeText(mContext, "请拍摄身份证人像面", Toast.LENGTH_SHORT).show();
        } else if (ID_FRONT_FID.isEmpty()) {
            Toast.makeText(mContext, "请拍摄身份证国徽面", Toast.LENGTH_SHORT).show();
        } else if (autonym_certify_name_tv.getText().toString().trim().isEmpty()) {
            Toast.makeText(mContext, "姓名不能为空", Toast.LENGTH_SHORT).show();
        } else if (autonym_certify_id_number_tv.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "身份证号不能为空", Toast.LENGTH_SHORT).show();
        } else if (!CheckIdCardValidUtil.isValidatedAllIdcard(autonym_certify_id_number_tv.getText().toString())) {
            Toast.makeText(mContext, "身份证号有误", Toast.LENGTH_SHORT).show();
        } else if (autonym_certify_mobile_tv.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "手机号不能为空", Toast.LENGTH_SHORT).show();
        } else if (!CheckMobileUtil.checkMobile(autonym_certify_mobile_tv.getText().toString().toString())) {
            Toast.makeText(mContext, "手机号码格式错误", Toast.LENGTH_SHORT).show();
        } else if (DRI_FID.isEmpty()) {
            Toast.makeText(mContext, "请拍摄驾照影像件", Toast.LENGTH_SHORT).show();
        } else if (autonym_certify_driving_license_rel_tv.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "请选择驾照证持有人与本人关系", Toast.LENGTH_SHORT).show();
        } else {
            return true;
        }
        return false;
    }

    private void nextStep() {
        EventBus.getDefault().post(ApplyActivityEvent.showPersonalInfoFragment);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            ClientInfo clientInfoBean = applyActivity.mClientInfo;
            if (clientInfoBean == null) return;
            autonym_certify_name_tv.setText(clientInfoBean.clt_nm);
            autonym_certify_id_number_tv.setText(clientInfoBean.id_no);
//            if (!TextUtils.isEmpty(clientInfoBean.gender)) {
//                personal_info_gender_tv.setText(clientInfoBean.gender);
//            }
//            Boolean empty = !TextUtils.isEmpty(clientInfoBean.reg_addr.province);
//            Boolean notEmpty = !TextUtils.isEmpty(clientInfoBean.reg_addr.city);
//            Boolean notEmpty1 = !TextUtils.isEmpty(clientInfoBean.reg_addr.district);
//            if (empty && notEmpty && notEmpty1) {
//                personal_info_reg_tv.setText(clientInfoBean.reg_addr.province + "/" + clientInfoBean.reg_addr.city + "/" + clientInfoBean.reg_addr.district);
//            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constants.REQUEST_DOCUMENT:

                    if (data != null) {
                        switch (data.getStringExtra("type")) {

                            case Constants.FileLabelType.ID_BACK:
                                ID_BACK_FID = data.getStringExtra("objectKey");
                                idBackImgUrl = data.getStringExtra("imgUrl");
                                if (!TextUtils.isEmpty(ID_BACK_FID)) {
                                    autonym_certify_id_back_tv.setText("已上传");
                                    autonym_certify_id_back_tv.setTextColor(getResources().getColor(R.color.system_color));
                                    ocrResp = (OcrResp.ShowapiResBodyBean) (data.getSerializableExtra("ocrResp"));
                                } else {
                                    autonym_certify_id_back_tv.setText("请上传");
                                    autonym_certify_id_back_tv.setTextColor(getResources().getColor(R.color.please_upload_color));
                                }
                                if (!TextUtils.isEmpty(ocrResp.idNo)) {
                                    autonym_certify_id_number_tv.setEnabled(true);
                                    autonym_certify_id_number_tv.setText(ocrResp.idNo);
                                } else {
                                    autonym_certify_id_number_tv.setText("");
                                    autonym_certify_id_number_tv.setEnabled(false);
                                }
                                if (!TextUtils.isEmpty(ocrResp.name)) {
                                    autonym_certify_name_tv.setText(ocrResp.name);
                                }else {
                                    autonym_certify_name_tv.setText("");

                                }
                                break;

                            case Constants.FileLabelType.ID_FRONT:
                                ID_FRONT_FID = data.getStringExtra("objectKey");
                                idFrontImgUrl = data.getStringExtra("imgUrl");
                                if (!TextUtils.isEmpty(ID_FRONT_FID)) {
                                    autonym_certify_id_front_tv.setText("已上传");
                                    autonym_certify_id_front_tv.setTextColor(getResources().getColor(R.color.system_color));
                                } else {
                                    autonym_certify_id_front_tv.setText("请上传");
                                    autonym_certify_id_front_tv.setTextColor(getResources().getColor(R.color.please_upload_color));
                                }
                                break;


                            case Constants.FileLabelType.DRI_LIC:
                                DRI_FID = data.getStringExtra("objectKey");
                                drivingLicImgUrl = data.getStringExtra("imgUrl");
                                if (!TextUtils.isEmpty(DRI_FID)) {
                                    autonym_certify_driving_license_tv.setText("已上传");
                                    autonym_certify_driving_license_tv.setTextColor(getResources().getColor(R.color.system_color));
                                } else {
                                    autonym_certify_driving_license_tv.setText("请上传");
                                    autonym_certify_driving_license_tv.setTextColor(getResources().getColor(R.color.please_upload_color));
                                }
                                break;
                            default:
                                break;

                        }
                    }

                    break;
                default:
                    break;


            }
        }
    }


}
