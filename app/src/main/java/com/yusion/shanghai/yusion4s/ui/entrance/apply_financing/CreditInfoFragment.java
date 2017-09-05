package com.yusion.shanghai.yusion4s.ui.entrance.apply_financing;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.Yusion4sApp;
import com.yusion.shanghai.yusion4s.base.BaseFragment;
import com.yusion.shanghai.yusion4s.bean.order.submit.SubmitOrderReq;
import com.yusion.shanghai.yusion4s.bean.order.submit.SubmitOrderResp;
import com.yusion.shanghai.yusion4s.bean.upload.ListImgsReq;
import com.yusion.shanghai.yusion4s.bean.upload.UploadFilesUrlReq;
import com.yusion.shanghai.yusion4s.bean.upload.UploadImgItemBean;
import com.yusion.shanghai.yusion4s.event.ApplyFinancingFragmentEvent;
import com.yusion.shanghai.yusion4s.retrofit.api.OrderApi;
import com.yusion.shanghai.yusion4s.retrofit.api.UploadApi;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnCodeAndMsgCallBack;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.settings.Constants;
import com.yusion.shanghai.yusion4s.ui.ApplyFinancingFragment;
import com.yusion.shanghai.yusion4s.ui.order.SearchClientActivity;
import com.yusion.shanghai.yusion4s.ui.upload.UploadSqsListActivity;
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
    private TextView findTv;

    private TextView client_info_name;
    private TextView client_phoneNumber;
    private TextView client_ID_card;
    private LinearLayout client_credit__book_lin;  //申请人征信
    private LinearLayout client_spouse_credit__book_lin;//申请人配偶
    private LinearLayout credit_applicate_detail_lin;//用户详情
    private LinearLayout guarantor_credit_book_lin;//担保人授权
    private LinearLayout guarantor_spouse_credit_book_lin;//担保人配偶

    private LinearLayout client_relationship_lin;//车主与申请人关系
    private TextView chooseRelationTv;
    public static int CLIENT_RELATIONSHIP_POSITION_INDEX = 0;

    private TextView autonym_certify_id_back_tv3;
    private TextView autonym_certify_id_back_tv2;
    private TextView autonym_certify_id_back_tv1;
    private TextView autonym_certify_id_back_tv;

    private LinearLayout mobile_sfz_lin;

    private LinearLayout personal_info_group;

    private Button submitBtn;

    //存放最后提交订单时需要上传的授权书url
    private List<UploadFilesUrlReq.FileUrlBean> uploadFileUrlList = new ArrayList<>();

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

        TextView step1 = (TextView) view.findViewById(R.id.step1);
        step1.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "yj.ttf"));
        step1.setOnClickListener(v -> EventBus.getDefault().post(ApplyFinancingFragmentEvent.showCarInfo));
        ((TextView) view.findViewById(R.id.step2)).setTypeface(Typeface.createFromAsset(mContext.getAssets(), "yj.ttf"));

        submitBtn = (Button) view.findViewById(R.id.credit_info_submit_btn);

        client_info_name = (TextView) view.findViewById(R.id.client_info_name);
        client_phoneNumber = (TextView) view.findViewById(R.id.client_phoneNumber);
        client_ID_card = (TextView) view.findViewById(R.id.client_ID_card);
        findTv = (TextView) view.findViewById(R.id.tv_find);
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

        credit_applicate_detail_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ApplicantDetailActivity.class);
                intent.putExtra("clt_id", lender_clt_id);
                startActivity(intent);
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
                if (chooseRelationTv.getText().toString().equals("请选择")) {
                    Toast.makeText(mContext, "请选择车主与申请人关系", Toast.LENGTH_LONG).show();
                } else {
                    SubmitOrderReq req = ((ApplyFinancingFragment) getParentFragment()).req;
                    req.clt_id = lender_clt_id;
                    req.vehicle_owner_lender_relation = chooseRelationTv.getText().toString();
                    OrderApi.submitOrder(mContext, req, new OnItemDataCallBack<SubmitOrderResp>() {
                        @Override
                        public void onItemDataCallBack(SubmitOrderResp data) {
                            if (data == null) {
                                return;
                            }
                            Toast.makeText(mContext, "订单提交成功", Toast.LENGTH_SHORT).show();
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
                                EventBus.getDefault().post(ApplyFinancingFragmentEvent.reset);
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 2000) {//搜索用户所返回的数据
                //清空数据
                lenderList.clear();
                lenderSpList.clear();
                guarantorList.clear();
                guarantorSpList.clear();
                uploadFileUrlList.clear();

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
                        if (guarantorList.size() > 0) {
                            autonym_certify_id_back_tv3.setText("已上传");
                            autonym_certify_id_back_tv3.setTextColor(Color.parseColor("#06b7a3"));
                        } else {
                            autonym_certify_id_back_tv3.setText("请上传");
                            autonym_certify_id_back_tv3.setTextColor(Color.parseColor("#d1d1d1"));
                        }
                        break;
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.client_credit__book_lin1://申请人征信授权
                Intent intent = new Intent(mContext, UploadSqsListActivity.class);
                intent.putExtra("clt_id", lender_clt_id);
                intent.putExtra("type", Constants.FileLabelType.AUTH_CREDIT);
                intent.putExtra("role", Constants.PersonType.LENDER);
                intent.putExtra("imgList", (Serializable) lenderList);
                intent.putExtra("uploadFileUrlList", (Serializable) uploadFileUrlList);
                intent.putExtra("title", "申请人征信授权书");
                startActivityForResult(intent, Constants.REQUEST_MULTI_DOCUMENT);
                break;
            case R.id.client_spouse_credit__book_lin2://申请人配偶
                Intent intent1 = new Intent(mContext, UploadSqsListActivity.class);
                intent1.putExtra("clt_id", lender_sp_clt_id);
                intent1.putExtra("type", Constants.FileLabelType.AUTH_CREDIT);
                intent1.putExtra("role", Constants.PersonType.LENDER_SP);
                intent1.putExtra("imgList", (Serializable) lenderSpList);
                intent1.putExtra("uploadFileUrlList", (Serializable) uploadFileUrlList);
                intent1.putExtra("title", "申请人配偶征信授权书");
                startActivityForResult(intent1, Constants.REQUEST_MULTI_DOCUMENT);
                break;
            case R.id.guarantor_credit_book_lin3://担保人征信授权
                Intent intent2 = new Intent(mContext, UploadSqsListActivity.class);
                intent2.putExtra("clt_id", guarantor_clt_id);
                intent2.putExtra("type", Constants.FileLabelType.AUTH_CREDIT);
                intent2.putExtra("role", Constants.PersonType.GUARANTOR);
                intent2.putExtra("imgList", (Serializable) guarantorList);
                intent2.putExtra("uploadFileUrlList", (Serializable) uploadFileUrlList);
                intent2.putExtra("title", "担保人征信授权书");
                startActivityForResult(intent2, Constants.REQUEST_MULTI_DOCUMENT);
                break;
            case R.id.guarantor_spouse_credit_book_lin4://担保人配偶征信授权
                Intent intent3 = new Intent(mContext, UploadSqsListActivity.class);
                intent3.putExtra("clt_id", guarantor_sp_clt_id);
                intent3.putExtra("type", Constants.FileLabelType.AUTH_CREDIT);
                intent3.putExtra("role", Constants.PersonType.GUARANTOR_SP);
                intent3.putExtra("imgList", (Serializable) guarantorSpList);
                intent3.putExtra("uploadFileUrlList", (Serializable) uploadFileUrlList);
                intent3.putExtra("title", "担保人配偶征信授权书");
                startActivityForResult(intent3, Constants.REQUEST_MULTI_DOCUMENT);
                break;
            case R.id.client_relationship_lin://车主和申请人的关系
                WheelViewUtil.showWheelView(Yusion4sApp.CONFIG_RESP.owner_applicant_relation_key,
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
        }
    }
}