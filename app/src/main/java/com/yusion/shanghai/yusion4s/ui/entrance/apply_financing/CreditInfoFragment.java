package com.yusion.shanghai.yusion4s.ui.entrance.apply_financing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.Yusion4sApp;
import com.yusion.shanghai.yusion4s.base.BaseFragment;
import com.yusion.shanghai.yusion4s.bean.order.submit.SubmitOrderReq;
import com.yusion.shanghai.yusion4s.bean.order.submit.SubmitOrderResp;
import com.yusion.shanghai.yusion4s.bean.upload.UploadFilesUrlReq;
import com.yusion.shanghai.yusion4s.bean.upload.UploadImgItemBean;
import com.yusion.shanghai.yusion4s.event.ApplyFinancingFragmentEvent;
import com.yusion.shanghai.yusion4s.retrofit.api.OrderApi;
import com.yusion.shanghai.yusion4s.retrofit.api.UploadApi;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnCodeAndMsgCallBack;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.settings.Constants;
import com.yusion.shanghai.yusion4s.settings.Settings;
import com.yusion.shanghai.yusion4s.ubt.UBT;
import com.yusion.shanghai.yusion4s.ubt.annotate.BindView;
import com.yusion.shanghai.yusion4s.ui.MainActivity;
import com.yusion.shanghai.yusion4s.ui.order.OrderCreateActivity;
import com.yusion.shanghai.yusion4s.ui.order.SearchClientActivity;
import com.yusion.shanghai.yusion4s.ui.upload.UploadSqsListActivity;
import com.yusion.shanghai.yusion4s.ui.yusion.apply.ApplyActivity;
import com.yusion.shanghai.yusion4s.utils.SharedPrefsUtil;
import com.yusion.shanghai.yusion4s.utils.wheel.WheelViewUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aa on 2017/8/9.
 */

public class CreditInfoFragment extends BaseFragment implements View.OnClickListener {

    @BindView(id = R.id.tv_find, widgetName = "tv_find")
    private TextView findTv;

    private TextView client_info_name;
    private TextView client_phoneNumber;
    private TextView client_ID_card;
    private ImageView delete_icon;
    private LinearLayout credit_info;

    //    @BindView(id = R.id.client_credit__book_lin1, widgetName = "上传申请人征信授权书", onClick = "uploadClientCreditBook")
    private LinearLayout client_credit__book_lin;  //申请人征信

    //    @BindView(id = R.id.client_spouse_credit__book_lin2, widgetName = "上传申请人配偶征信授权书", onClick = "uploadClientSpouseCreditBook")
    private LinearLayout client_spouse_credit__book_lin;//申请人配偶

    //    @BindView(id = R.id.credit_applicate_detail_lin, widgetName = "查看用户详情", onClick = "lookClientDetail")
    private LinearLayout credit_applicate_detail_lin;//用户详情

    //    @BindView(id = R.id.guarantor_credit_book_lin3, widgetName = "上传担保人征信授权书", onClick = "uploadGuarantorCreditBook")
    private LinearLayout guarantor_credit_book_lin;//担保人授权

    //    @BindView(id = R.id.guarantor_spouse_credit_book_lin4, widgetName = "上传担保人配偶征信授权书", onClick = "uploadGuarantorSpouseCreditBook")
    private LinearLayout guarantor_spouse_credit_book_lin;//担保人配偶

    //    @BindView(id = R.id.client_relationship_lin, widgetName = "选择车主与申请人关系", onClick = "chooseRelationship")
    private LinearLayout client_relationship_lin;//车主与申请人关系

    @BindView(id = R.id.choose_relation, widgetName = "choose_relation")
    private TextView chooseRelationTv;
    public static int CLIENT_RELATIONSHIP_POSITION_INDEX = 0;

    @BindView(id = R.id.autonym_certify_id_back_tv, widgetName = "autonym_certify_id_back_tv")
    private TextView autonym_certify_id_back_tv;

    @BindView(id = R.id.autonym_certify_id_back_tv1, widgetName = "autonym_certify_id_back_tv1")
    private TextView autonym_certify_id_back_tv1;

    @BindView(id = R.id.autonym_certify_id_back_tv2, widgetName = "autonym_certify_id_back_tv2")
    private TextView autonym_certify_id_back_tv2;

    @BindView(id = R.id.autonym_certify_id_back_tv3, widgetName = "autonym_certify_id_back_tv3")
    private TextView autonym_certify_id_back_tv3;

    @BindView(id = R.id.autonym_certify_id_back_tv10, widgetName = "autonym_certify_id_back_tv10", onClick = "toApplicantDetailActivity")
    private Button autonym_certify_id_back_tv10;

    private void toApplicantDetailActivity(View view) {
        Intent intent = new Intent(mContext, ApplicantDetailActivity.class);
        intent.putExtra("clt_id", lender_clt_id);
        startActivity(intent);
    }

    private LinearLayout mobile_sfz_lin;

    private LinearLayout personal_info_group;

    private Button submitBtn;
    private Button createBtn;

    //存放最后提交订单时需要上传的授权书url
    private List<UploadFilesUrlReq.FileUrlBean> uploadFileUrlList = new ArrayList<>();
    //存放二手车的截图
    private List<UploadFilesUrlReq.FileUrlBean> uploadOldCarImgUrlList = new ArrayList<>();

    //用于和 UploadSqsListActivity 类间交互
    private List<UploadImgItemBean> lenderList = new ArrayList<>();
    private List<UploadImgItemBean> lenderSpList = new ArrayList<>();
    private List<UploadImgItemBean> guarantorList = new ArrayList<>();
    private List<UploadImgItemBean> guarantorSpList = new ArrayList<>();

    public String relation_name = "";
    private String lender_clt_id;
    private String lender_sp_clt_id;
    private String guarantor_clt_id;
    private String guarantor_sp_clt_id;

//    private void searchClient(View view) {
//        //点击跳转  到 检索页面 并 返回 数据 进行 展示。
//        Intent intent = new Intent(mContext, SearchClientActivity.class);
//        startActivityForResult(intent, 2000);
//    }
//
//    private void uploadClientCreditBook(View view) {
//        Intent intent = new Intent(mContext, UploadSqsListActivity.class);
//        intent.putExtra("clt_id", lender_clt_id);
//        intent.putExtra("type", Constants.FileLabelType.AUTH_CREDIT);
//        intent.putExtra("role", Constants.PersonType.LENDER);
//        intent.putExtra("imgList", (Serializable) lenderList);
//        intent.putExtra("uploadFileUrlList", (Serializable) uploadFileUrlList);
//        intent.putExtra("title", "申请人征信授权书");
//        startActivityForResult(intent, Constants.REQUEST_MULTI_DOCUMENT);
//    }
//
//    private void uploadClientSpouseCreditBook(View view) {
//        Intent intent1 = new Intent(mContext, UploadSqsListActivity.class);
//        intent1.putExtra("clt_id", lender_sp_clt_id);
//        intent1.putExtra("type", Constants.FileLabelType.AUTH_CREDIT);
//        intent1.putExtra("role", Constants.PersonType.LENDER_SP);
//        intent1.putExtra("imgList", (Serializable) lenderSpList);
//        intent1.putExtra("uploadFileUrlList", (Serializable) uploadFileUrlList);
//        intent1.putExtra("title", "申请人配偶征信授权书");
//        startActivityForResult(intent1, Constants.REQUEST_MULTI_DOCUMENT);
//    }
//
//    private void uploadGuarantorCreditBook(View view) {
//        Intent intent2 = new Intent(mContext, UploadSqsListActivity.class);
//        intent2.putExtra("clt_id", guarantor_clt_id);
//        intent2.putExtra("type", Constants.FileLabelType.AUTH_CREDIT);
//        intent2.putExtra("role", Constants.PersonType.GUARANTOR);
//        intent2.putExtra("imgList", (Serializable) guarantorList);
//        intent2.putExtra("uploadFileUrlList", (Serializable) uploadFileUrlList);
//        intent2.putExtra("title", "担保人征信授权书");
//        startActivityForResult(intent2, Constants.REQUEST_MULTI_DOCUMENT);
//    }
//
//    private void uploadGuarantorSpouseCreditBook(View view) {
//        Intent intent3 = new Intent(mContext, UploadSqsListActivity.class);
//        intent3.putExtra("clt_id", guarantor_sp_clt_id);
//        intent3.putExtra("type", Constants.FileLabelType.AUTH_CREDIT);
//        intent3.putExtra("role", Constants.PersonType.GUARANTOR_SP);
//        intent3.putExtra("imgList", (Serializable) guarantorSpList);
//        intent3.putExtra("uploadFileUrlList", (Serializable) uploadFileUrlList);
//        intent3.putExtra("title", "担保人配偶征信授权书");
//        startActivityForResult(intent3, Constants.REQUEST_MULTI_DOCUMENT);
//    }
//
//    private void chooseRelationship(View view) {
//        WheelViewUtil.showWheelView(Yusion4sApp.getConfigResp().owner_applicant_relation_key,
//                CLIENT_RELATIONSHIP_POSITION_INDEX,
//                client_relationship_lin,
//                chooseRelationTv,
//                "请选择",
//                new WheelViewUtil.OnSubmitCallBack() {
//                    @Override
//                    public void onSubmitCallBack(View clickedView, int selectedIndex) {
//                        CLIENT_RELATIONSHIP_POSITION_INDEX = selectedIndex;
//                        chooseRelationTv.setTextColor(Color.parseColor("#222A36"));
//                    }
//                });
//    }
//
//    private void lookClientDetail(View view) {
//        Intent intent = new Intent(mContext, ApplicantDetailActivity.class);
//        intent.putExtra("clt_id", lender_clt_id);
//        startActivity(intent);
//    }


    public static CreditInfoFragment newInstance() {
        Bundle args = new Bundle();
        CreditInfoFragment fragment = new CreditInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_credit_info, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UBT.bind(this, view, getClass().getSimpleName());
        TextView step1 = (TextView) view.findViewById(R.id.step1);
        step1.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "yj.ttf"));
        step1.setOnClickListener(v -> EventBus.getDefault().post(ApplyFinancingFragmentEvent.showCarInfo));
        ((TextView) view.findViewById(R.id.step2)).setTypeface(Typeface.createFromAsset(mContext.getAssets(), "yj.ttf"));

        submitBtn = (Button) view.findViewById(R.id.credit_info_submit_btn);
        createBtn = (Button) view.findViewById(R.id.credit_info_create_btn);
        client_info_name = (TextView) view.findViewById(R.id.client_info_name);
        client_phoneNumber = (TextView) view.findViewById(R.id.client_phoneNumber);
        client_ID_card = (TextView) view.findViewById(R.id.client_ID_card);
        // findTv = (TextView) view.findViewById(R.id.tv_find);

        //申请人征信
        client_credit__book_lin = (LinearLayout) view.findViewById(R.id.client_credit__book_lin1);
        client_credit__book_lin.setOnClickListener(this);
        //申请人配偶
        client_spouse_credit__book_lin = (LinearLayout) view.findViewById(R.id.client_spouse_credit__book_lin2);
        client_spouse_credit__book_lin.setOnClickListener(this);
        //用户详情
        credit_applicate_detail_lin = (LinearLayout) view.findViewById(R.id.credit_applicate_detail_lin);
        credit_applicate_detail_lin.setOnClickListener(this);
        //担保人授权
        guarantor_credit_book_lin = (LinearLayout) view.findViewById(R.id.guarantor_credit_book_lin3);
        guarantor_credit_book_lin.setOnClickListener(this);
        //担保人配偶
        guarantor_spouse_credit_book_lin = (LinearLayout) view.findViewById(R.id.guarantor_spouse_credit_book_lin4);
        guarantor_spouse_credit_book_lin.setOnClickListener(this);
        //车主与申请人关系
        client_relationship_lin = (LinearLayout) view.findViewById(R.id.client_relationship_lin);
        client_relationship_lin.setOnClickListener(this);
        chooseRelationTv = (TextView) view.findViewById(R.id.choose_relation);
        mobile_sfz_lin = (LinearLayout) view.findViewById(R.id.mobile_sfz_lin);

        autonym_certify_id_back_tv = (TextView) view.findViewById(R.id.autonym_certify_id_back_tv);
        autonym_certify_id_back_tv1 = (TextView) view.findViewById(R.id.autonym_certify_id_back_tv1);
        autonym_certify_id_back_tv2 = (TextView) view.findViewById(R.id.autonym_certify_id_back_tv2);
        autonym_certify_id_back_tv3 = (TextView) view.findViewById(R.id.autonym_certify_id_back_tv3);

        personal_info_group = (LinearLayout) view.findViewById(R.id.personal_info_group);
        delete_icon = (ImageView) view.findViewById(R.id.delete_icon);
        credit_info = (LinearLayout) view.findViewById(R.id.credit_info);

//        credit_applicate_detail_lin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, ApplicantDetailActivity.class);
//                intent.putExtra("clt_id", lender_clt_id);
//                startActivity(intent);
//            }
//        });

        delete_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUserInfo();
            }
        });

        findTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击跳转  到 检索页面 并 返回 数据 进行 展示。
                Intent intent = new Intent(mContext, SearchClientActivity.class);
                startActivityForResult(intent, 2000);
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startActivity(new Intent(mContext, MainActivity.class));
                TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
                if (Settings.isShameData) {
                    SubmitOrderReq req = new SubmitOrderReq();
                    req.bank_id = "1";
                    req.clt_id = "d90be890acd411e79f4a0242ac110002";
                    req.dlr_id = "YJCS0001";
                    req.gps_fee = "0";
                    req.loan_amt = "152000";
                    req.management_fee = "0";
                    req.other_fee = "2000";
                    req.plate_reg_addr = "北京/北京市/东城区";
                    req.vehicle_color = "ujj";
                    req.vehicle_cond = "新车";
                    req.vehicle_down_payment = "50000";
                    req.vehicle_loan_amt = "150000";
                    req.vehicle_owner_lender_relation = "本人";
                    req.vehicle_price = "200000";
                    req.nper = "24";
                    req.product_id = 1;
                    req.vehicle_model_id = 1128954;
                    req.imei = telephonyManager.getDeviceId();
                    OrderApi.submitOrder(mContext, req, new OnItemDataCallBack<SubmitOrderResp>() {
                        @Override
                        public void onItemDataCallBack(SubmitOrderResp data) {
                            if (data == null) {
                                return;
                            }
                            Toast.makeText(mContext, "订单提交成功", Toast.LENGTH_SHORT).show();
                            Log.e("TAG", "uploadFileUrlList: " + uploadFileUrlList);
                            if (uploadFileUrlList.size() > 0) {
                                for (UploadFilesUrlReq.FileUrlBean urlBean : uploadFileUrlList) {
                                    urlBean.app_id = data.app_id;
                                }
                                UploadFilesUrlReq uploadFilesUrlReq = new UploadFilesUrlReq();
                                uploadFilesUrlReq.files = uploadFileUrlList;
                                uploadFilesUrlReq.region = SharedPrefsUtil.getInstance(mContext).getValue("region", "");
                                uploadFilesUrlReq.bucket = SharedPrefsUtil.getInstance(mContext).getValue("bucket", "");
                                UploadApi.uploadFileUrl(mContext, uploadFilesUrlReq, new OnCodeAndMsgCallBack() {
                                    @Override
                                    public void callBack(int code, String msg) {
                                        if (code > -1) {
                                            Toast.makeText(mContext, "图片上传成功", Toast.LENGTH_SHORT).show();
                                            EventBus.getDefault().post(ApplyFinancingFragmentEvent.reset);
                                        }
                                    }
                                });
                            } else {
//                                EventBus.getDefault().post(ApplyFinancingFragmentEvent.reset);
                                startActivity(new Intent(mContext, MainActivity.class));

                            }
                        }
                    });
                } else if (checkCanSubmit()) {
                    SubmitOrderReq req = ((OrderCreateActivity) getActivity()).req;
                    // SubmitOrderReq req = ((ApplyFinancingFragment) getParentFragment()).req;
                    req.clt_id = lender_clt_id;
                    req.vehicle_owner_lender_relation = chooseRelationTv.getText().toString();
                    req.imei = telephonyManager.getDeviceId();
                    OrderApi.submitOrder(mContext, req, new OnItemDataCallBack<SubmitOrderResp>() {
                        @Override
                        public void onItemDataCallBack(SubmitOrderResp data) {
                            if (data == null) {
                                return;
                            }
                            Toast.makeText(mContext, "订单提交成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(mContext, MainActivity.class);
                            intent.putExtra("app_id", data.app_id);
                            intent.putExtra("cond", req.vehicle_cond);

                            if (((OrderCreateActivity) getActivity()).cartype.equals("二手车")) {
                                UploadFilesUrlReq.FileUrlBean urlBean = new UploadFilesUrlReq.FileUrlBean();
                                urlBean.label = ((OrderCreateActivity) getActivity()).label;
                                urlBean.file_id = ((OrderCreateActivity) getActivity()).file_id;
                                urlBean.app_id = data.app_id;
                                uploadOldCarImgUrlList.add(urlBean);
                                // uploadFileUrlList.add(urlBean);
                            }
                            if (uploadFileUrlList.size() > 0) {
                                for (UploadFilesUrlReq.FileUrlBean urlBean : uploadFileUrlList) {
                                    urlBean.app_id = data.app_id;
                                }
                                UploadFilesUrlReq uploadFilesUrlReq = new UploadFilesUrlReq();
                                uploadFilesUrlReq.files = uploadFileUrlList;
                                uploadFilesUrlReq.region = SharedPrefsUtil.getInstance(mContext).getValue("region", "");
                                uploadFilesUrlReq.bucket = SharedPrefsUtil.getInstance(mContext).getValue("bucket", "");
                                UploadApi.uploadFileUrl(mContext, uploadFilesUrlReq, new OnCodeAndMsgCallBack() {
                                    @Override
                                    public void callBack(int code, String msg) {
                                        if (code > -1) {
                                            Toast.makeText(mContext, "图片上传成功", Toast.LENGTH_SHORT).show();

                                            if (((OrderCreateActivity) getActivity()).cartype.equals("二手车")) {
                                                UploadFilesUrlReq uploadFilesUrlReq1 = new UploadFilesUrlReq();
                                                uploadFilesUrlReq1.bucket = ((OrderCreateActivity) getActivity()).bucket;
                                                uploadFilesUrlReq1.region = ((OrderCreateActivity) getActivity()).region;
                                                uploadFilesUrlReq1.files = uploadOldCarImgUrlList;
                                                UploadApi.uploadFileUrl(mContext, uploadFilesUrlReq1, new OnCodeAndMsgCallBack() {
                                                    @Override
                                                    public void callBack(int code, String msg) {
                                                        Log.e("TAG", uploadFilesUrlReq1.bucket);
                                                        Log.e("TAG", uploadFilesUrlReq1.region);

                                                    }
                                                });
                                            }
                                            startActivity(intent);
//                                            EventBus.getDefault().post(ApplyFinancingFragmentEvent.reset);
                                        }
                                    }
                                });
//                                EventBus.getDefault().post(ApplyFinancingFragmentEvent.reset);
                            } else {
                                // EventBus.getDefault().post(ApplyFinancingFragmentEvent.reset);
                                startActivity(intent);
                            }
                        }
                    });
                }

            }
        });

        createBtn.setOnClickListener(v -> startActivity(new Intent(mContext, ApplyActivity.class)));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 2000) {//搜索用户所返回的数据
                //清空数据
                client_credit__book_lin.setVisibility(View.GONE);
                client_spouse_credit__book_lin.setVisibility(View.GONE);
                guarantor_credit_book_lin.setVisibility(View.GONE);
                guarantor_spouse_credit_book_lin.setVisibility(View.GONE);
                lenderList.clear();
                lenderSpList.clear();
                guarantorList.clear();
                guarantorSpList.clear();
                uploadFileUrlList.clear();

                credit_info.setVisibility(View.VISIBLE);
                personal_info_group.setVisibility(View.VISIBLE);
                client_info_name.setText(data.getStringExtra("name"));
                client_ID_card.setText(data.getStringExtra("sfz"));
                client_phoneNumber.setText(data.getStringExtra("mobile"));

                if (data.getStringExtra("isHasLender").equals("1")) { //不等于空是1 等于空是2 申请人征信授权书
                    client_credit__book_lin.setVisibility(View.VISIBLE);
                    lender_clt_id = data.getStringExtra("lender_clt_id");
//                    if (Integer.valueOf(data.getStringExtra("lender")) > 0) {
//                        autonym_certify_id_back_tv.setText("已上传");
//                        autonym_certify_id_back_tv.setTextColor(Color.parseColor("#06b7a3"));
//
//                        ListImgsReq req = new ListImgsReq();
//                        req.label = Constants.FileLabelType.AUTH_CREDIT;
//                        req.clt_id = lender_clt_id;
//                        req.app_id = " ";
//                        UploadApi.listImgs(mContext, req, resp -> {
//                            if (resp.list.size() != 0) {
//                                lenderList.addAll(resp.list);
//                            }
//                        });
//                    } else {
                    autonym_certify_id_back_tv.setText("请上传");
                    autonym_certify_id_back_tv.setTextColor(getResources().getColor(R.color.please_upload_color));
//                    }
                } else {
                    client_credit__book_lin.setVisibility(View.GONE);
                }

                if (data.getStringExtra("isHasLender_sp").equals("1")) { //不等于空是1 等于空是2
                    client_spouse_credit__book_lin.setVisibility(View.VISIBLE);
                    lender_sp_clt_id = data.getStringExtra("lender_sp_clt_id");
//                    if (Integer.valueOf(data.getStringExtra("lender_sp")) > 0) {
//                        autonym_certify_id_back_tv1.setText("已上传");
//                        autonym_certify_id_back_tv1.setTextColor(Color.parseColor("#06b7a3"));
//
//                        ListImgsReq req = new ListImgsReq();
//                        req.label = Constants.FileLabelType.AUTH_CREDIT;
//                        req.clt_id = lender_sp_clt_id;
//                        req.app_id = " ";
//                        UploadApi.listImgs(mContext, req, resp -> {
//                            if (resp.list.size() != 0) {
//                                lenderSpList.addAll(resp.list);
//                            }
//                        });
//                    } else {
                    autonym_certify_id_back_tv1.setText("请上传");
                    autonym_certify_id_back_tv1.setTextColor(getResources().getColor(R.color.please_upload_color));
                    //                }
                } else {
                    client_spouse_credit__book_lin.setVisibility(View.GONE);
                }

                if (data.getStringExtra("isGuarantor").equals("1")) { //不等于空是1 等于空是2
                    guarantor_credit_book_lin.setVisibility(View.VISIBLE);
                    guarantor_clt_id = data.getStringExtra("guarantor_clt_id");
//                    if (Integer.valueOf(data.getStringExtra("guarantor")) > 0) {
//                        autonym_certify_id_back_tv2.setText("已上传");
//                        autonym_certify_id_back_tv2.setTextColor(Color.parseColor("#06b7a3"));
//
//
//                        ListImgsReq req = new ListImgsReq();
//                        req.label = Constants.FileLabelType.AUTH_CREDIT;
//                        req.clt_id = guarantor_clt_id;
//                        req.app_id = " ";
//                        UploadApi.listImgs(mContext, req, resp -> {
//                            if (resp.list.size() != 0) {
//                                guarantorList.addAll(resp.list);
//                            }
//                        });
//                    } else {
                    autonym_certify_id_back_tv2.setText("请上传");
                    autonym_certify_id_back_tv2.setTextColor(getResources().getColor(R.color.please_upload_color));
                    //      }
                } else {
                    guarantor_credit_book_lin.setVisibility(View.GONE);
                }

                if (data.getStringExtra("isGuarantor_sp").equals("1")) { //不等于空是1 等于空是2
                    guarantor_spouse_credit_book_lin.setVisibility(View.VISIBLE);
                    guarantor_sp_clt_id = data.getStringExtra("guarantor_sp_clt_id");
//                    if (Integer.valueOf(data.getStringExtra("guarantor_sp")) > 0) {
//                        autonym_certify_id_back_tv3.setText("已上传");
//                        autonym_certify_id_back_tv3.setTextColor(Color.parseColor("#06b7a3"));
//
//                        ListImgsReq req = new ListImgsReq();
//                        req.label = Constants.FileLabelType.AUTH_CREDIT;
//                        req.clt_id = guarantor_sp_clt_id;
//                        req.app_id = " ";
//                        UploadApi.listImgs(mContext, req, resp -> {
//                            if (resp.list.size() != 0) {
//                                guarantorSpList.addAll(resp.list);
//                            }
//                        });
//                    } else {
                    autonym_certify_id_back_tv3.setText("请上传");
                    autonym_certify_id_back_tv3.setTextColor(getResources().getColor(R.color.please_upload_color));
                    //  }
                } else {
                    guarantor_spouse_credit_book_lin.setVisibility(View.GONE);
                }

                submitBtn.setEnabled(data.getBooleanExtra("enable", false));

            } else if (requestCode == Constants.REQUEST_MULTI_DOCUMENT) {
                uploadFileUrlList = (List<UploadFilesUrlReq.FileUrlBean>) data.getSerializableExtra("uploadFileUrlList");

                switch (data.getStringExtra("role")) {
                    case Constants.PersonType.LENDER:
                        lenderList = (List<UploadImgItemBean>) data.getSerializableExtra("imgList");
                        if (lenderList.size() > 0) {
                            autonym_certify_id_back_tv.setText("已上传");
                            autonym_certify_id_back_tv.setTextColor(Color.parseColor("#06b7a3"));
                        } else {
                            autonym_certify_id_back_tv.setText("请上传");
                            autonym_certify_id_back_tv.setTextColor(Color.parseColor("#d1d1d1"));
                        }
                        break;
                    case Constants.PersonType.LENDER_SP:
                        lenderSpList = (List<UploadImgItemBean>) data.getSerializableExtra("imgList");
                        if (lenderSpList.size() > 0) {
                            autonym_certify_id_back_tv1.setText("已上传");
                            autonym_certify_id_back_tv1.setTextColor(Color.parseColor("#06b7a3"));
                        } else {
                            autonym_certify_id_back_tv1.setText("请上传");
                            autonym_certify_id_back_tv1.setTextColor(Color.parseColor("#d1d1d1"));
                        }
                        break;
                    case Constants.PersonType.GUARANTOR:
                        guarantorList = (List<UploadImgItemBean>) data.getSerializableExtra("imgList");
                        if (guarantorList.size() > 0) {
                            autonym_certify_id_back_tv2.setText("已上传");
                            autonym_certify_id_back_tv2.setTextColor(Color.parseColor("#06b7a3"));
                        } else {
                            autonym_certify_id_back_tv2.setText("请上传");
                            autonym_certify_id_back_tv2.setTextColor(Color.parseColor("#d1d1d1"));
                        }
                        break;
                    case Constants.PersonType.GUARANTOR_SP:
                        guarantorSpList = (List<UploadImgItemBean>) data.getSerializableExtra("imgList");
                        if (guarantorSpList.size() > 0) {
                            autonym_certify_id_back_tv3.setText("已上传");
                            autonym_certify_id_back_tv3.setTextColor(Color.parseColor("#06b7a3"));
                        } else {
                            autonym_certify_id_back_tv3.setText("请上传");
                            autonym_certify_id_back_tv3.setTextColor(Color.parseColor("#d1d1d1"));
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(mContext, UploadSqsListActivity.class);
        //SubmitOrderReq req = ((ApplyFinancingFragment) getParentFragment()).req;
        SubmitOrderReq req = ((OrderCreateActivity) getActivity()).req;


        intent.putExtra("dlr_id", req.dlr_id);
        intent.putExtra("bank_id", req.bank_id);
        switch (v.getId()) {
            case R.id.client_credit__book_lin1://申请人征信授权
                intent.putExtra("clt_id", lender_clt_id);
                intent.putExtra("type", Constants.FileLabelType.AUTH_CREDIT);
                intent.putExtra("role", Constants.PersonType.LENDER);
                intent.putExtra("imgList", (Serializable) lenderList);
                intent.putExtra("uploadFileUrlList", (Serializable) uploadFileUrlList);
                intent.putExtra("title", "申请人征信授权书");
                startActivityForResult(intent, Constants.REQUEST_MULTI_DOCUMENT);
                break;
            case R.id.client_spouse_credit__book_lin2://申请人配偶
                intent.putExtra("clt_id", lender_sp_clt_id);
                intent.putExtra("type", Constants.FileLabelType.AUTH_CREDIT);
                intent.putExtra("role", Constants.PersonType.LENDER_SP);
                intent.putExtra("imgList", (Serializable) lenderSpList);
                intent.putExtra("uploadFileUrlList", (Serializable) uploadFileUrlList);
                intent.putExtra("title", "申请人配偶征信授权书");
                startActivityForResult(intent, Constants.REQUEST_MULTI_DOCUMENT);
                break;
            case R.id.guarantor_credit_book_lin3://担保人征信授权
                intent.putExtra("clt_id", guarantor_clt_id);
                intent.putExtra("type", Constants.FileLabelType.AUTH_CREDIT);
                intent.putExtra("role", Constants.PersonType.GUARANTOR);
                intent.putExtra("imgList", (Serializable) guarantorList);
                intent.putExtra("uploadFileUrlList", (Serializable) uploadFileUrlList);
                intent.putExtra("title", "担保人征信授权书");
                startActivityForResult(intent, Constants.REQUEST_MULTI_DOCUMENT);
                break;
            case R.id.guarantor_spouse_credit_book_lin4://担保人配偶征信授权
                intent.putExtra("clt_id", guarantor_sp_clt_id);
                intent.putExtra("type", Constants.FileLabelType.AUTH_CREDIT);
                intent.putExtra("role", Constants.PersonType.GUARANTOR_SP);
                intent.putExtra("imgList", (Serializable) guarantorSpList);
                intent.putExtra("uploadFileUrlList", (Serializable) uploadFileUrlList);
                intent.putExtra("title", "担保人配偶征信授权书");
                startActivityForResult(intent, Constants.REQUEST_MULTI_DOCUMENT);
                break;
            case R.id.client_relationship_lin://车主和申请人的关系
                WheelViewUtil.showWheelView(((Yusion4sApp) getActivity().getApplication()).getConfigResp().owner_applicant_relation_key,
                        CLIENT_RELATIONSHIP_POSITION_INDEX,
                        client_relationship_lin,
                        chooseRelationTv,
                        "请选择",
                        new WheelViewUtil.OnSubmitCallBack() {
                            @Override
                            public void onSubmitCallBack(View clickedView, int selectedIndex) {
                                CLIENT_RELATIONSHIP_POSITION_INDEX = selectedIndex;
                                chooseRelationTv.setTextColor(Color.parseColor("#222A36"));
                            }
                        });
                break;
            default:
                break;
        }
    }

    private boolean checkCanSubmit() {
        if (chooseRelationTv.getText().toString().equals("请选择")) {
            Toast.makeText(mContext, "请选择车主与申请人关系", Toast.LENGTH_LONG).show();
        } else if (client_credit__book_lin.getVisibility() == View.VISIBLE && autonym_certify_id_back_tv.getText().toString().equals("请上传")) {//显示且是未上传的状态
            Toast.makeText(mContext, "请上传申请人征信授权书", Toast.LENGTH_LONG).show();//申请人征信
        } else if (client_spouse_credit__book_lin.getVisibility() == View.VISIBLE && autonym_certify_id_back_tv1.getText().toString().equals("请上传")) {//显示且是未上传的状态
            Toast.makeText(mContext, "请上传申请人配偶征信授权书", Toast.LENGTH_LONG).show();
        } else if (guarantor_credit_book_lin.getVisibility() == View.VISIBLE && autonym_certify_id_back_tv2.getText().toString().equals("请上传")) {//显示且是未上传的状态
            Toast.makeText(mContext, "请上传担保人征信授权书", Toast.LENGTH_LONG).show();
        } else if (guarantor_spouse_credit_book_lin.getVisibility() == View.VISIBLE && autonym_certify_id_back_tv3.getText().toString().equals("请上传")) {//显示且是未上传的状态
            Toast.makeText(mContext, "请上传担保人配偶征信授权书", Toast.LENGTH_LONG).show();
        } else {
            return true;
        }
        return false;
    }


    // 直接关联
    public void deleteUserInfo() {
        client_credit__book_lin.setVisibility(View.GONE);
        client_spouse_credit__book_lin.setVisibility(View.GONE);
        guarantor_credit_book_lin.setVisibility(View.GONE);
        guarantor_spouse_credit_book_lin.setVisibility(View.GONE);
        personal_info_group.setVisibility(View.GONE);
        credit_info.setVisibility(View.GONE);

        lenderList.clear();
        lenderSpList.clear();
        guarantorList.clear();
        guarantorSpList.clear();
        uploadFileUrlList.clear();
        submitBtn.setEnabled(false);
    }

    public void relevance(Intent data) {
        //清空数据
        client_credit__book_lin.setVisibility(View.GONE);
        client_spouse_credit__book_lin.setVisibility(View.GONE);
        guarantor_credit_book_lin.setVisibility(View.GONE);
        guarantor_spouse_credit_book_lin.setVisibility(View.GONE);
        lenderList.clear();
        lenderSpList.clear();
        guarantorList.clear();
        guarantorSpList.clear();
        uploadFileUrlList.clear();

        credit_info.setVisibility(View.VISIBLE);
        personal_info_group.setVisibility(View.VISIBLE);
        client_info_name.setText(data.getStringExtra("name"));
        client_ID_card.setText(data.getStringExtra("sfz"));
        client_phoneNumber.setText(data.getStringExtra("mobile"));

        if (data.getStringExtra("isHasLender").equals("1")) { //不等于空是1 等于空是2 申请人征信授权书
            client_credit__book_lin.setVisibility(View.VISIBLE);
            lender_clt_id = data.getStringExtra("lender_clt_id");
            autonym_certify_id_back_tv.setText("请上传");
            autonym_certify_id_back_tv.setTextColor(getResources().getColor(R.color.please_upload_color));
        } else {
            client_credit__book_lin.setVisibility(View.GONE);
        }

        if (data.getStringExtra("isHasLender_sp").equals("1")) { //不等于空是1 等于空是2
            client_spouse_credit__book_lin.setVisibility(View.VISIBLE);
            lender_sp_clt_id = data.getStringExtra("lender_sp_clt_id");
            autonym_certify_id_back_tv1.setText("请上传");
            autonym_certify_id_back_tv1.setTextColor(getResources().getColor(R.color.please_upload_color));
            //                }
        } else {
            client_spouse_credit__book_lin.setVisibility(View.GONE);
        }

        if (data.getStringExtra("isGuarantor").equals("1")) { //不等于空是1 等于空是2
            guarantor_credit_book_lin.setVisibility(View.VISIBLE);
            guarantor_clt_id = data.getStringExtra("guarantor_clt_id");
            autonym_certify_id_back_tv2.setText("请上传");
            autonym_certify_id_back_tv2.setTextColor(getResources().getColor(R.color.please_upload_color));
        } else {
            guarantor_credit_book_lin.setVisibility(View.GONE);
        }

        if (data.getStringExtra("isGuarantor_sp").equals("1")) { //不等于空是1 等于空是2
            guarantor_spouse_credit_book_lin.setVisibility(View.VISIBLE);
            guarantor_sp_clt_id = data.getStringExtra("guarantor_sp_clt_id");
            autonym_certify_id_back_tv3.setText("请上传");
            autonym_certify_id_back_tv3.setTextColor(getResources().getColor(R.color.please_upload_color));
        } else {
            guarantor_spouse_credit_book_lin.setVisibility(View.GONE);
        }

        submitBtn.setEnabled(data.getBooleanExtra("enable", false));

    }
}