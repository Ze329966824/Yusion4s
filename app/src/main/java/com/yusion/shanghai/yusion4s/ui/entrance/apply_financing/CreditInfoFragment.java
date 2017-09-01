package com.yusion.shanghai.yusion4s.ui.entrance.apply_financing;

import android.app.Activity;
import android.app.Dialog;
import android.app.backup.FileBackupHelper;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.TtsSpan;
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

import com.bumptech.glide.Glide;
import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.Yusion4sApp;
import com.yusion.shanghai.yusion4s.base.BaseFragment;
import com.yusion.shanghai.yusion4s.bean.order.SearchClientResp;
import com.yusion.shanghai.yusion4s.bean.order.submit.SubmitOrderReq;
import com.yusion.shanghai.yusion4s.bean.order.submit.SubmitOrderResp;
import com.yusion.shanghai.yusion4s.bean.oss.OSSObjectKeyBean;
import com.yusion.shanghai.yusion4s.bean.upload.UploadFilesUrlReq;
import com.yusion.shanghai.yusion4s.bean.upload.UploadImgItemBean;
import com.yusion.shanghai.yusion4s.event.ApplyFinancingFragmentEvent;
import com.yusion.shanghai.yusion4s.retrofit.api.OrderApi;
import com.yusion.shanghai.yusion4s.retrofit.api.UploadApi;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.settings.Constants;
import com.yusion.shanghai.yusion4s.ui.ApplyFinancingFragment;
import com.yusion.shanghai.yusion4s.ui.order.SearchClientActivity;
import com.yusion.shanghai.yusion4s.ui.upload.UploadLabelListActivity;
import com.yusion.shanghai.yusion4s.ui.upload.UploadListActivity;
import com.yusion.shanghai.yusion4s.utils.LoadingUtils;
import com.yusion.shanghai.yusion4s.utils.OssUtil;
import com.yusion.shanghai.yusion4s.utils.SharedPrefsUtil;
import com.yusion.shanghai.yusion4s.utils.wheel.WheelViewUtil;
import com.yusion.shanghai.yusion4s.utils.wheel.model.ProvinceModel;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.Serializable;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aa on 2017/8/9.
 */

public class CreditInfoFragment extends BaseFragment {
    private TextView findTv;
    private View sqs2Lin;
    private View sqs3Lin;
    private int currentClickedView;
    private ImageView sqs1Img;
    private ImageView sqs2Img;
    private ImageView sqs3Img;
    private ImageView sqs2expandImg;
    private ImageView sqs3expandImg;
    private String clt_id;
    private String id_no_r;
    private EditText idNo;

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


    private File sqsFile1;
    private File sqsFile2;
    private File sqsFile3;
    private Button submitBtn;
    private List<UploadImgItemBean> lenderList = new ArrayList<>();
    private List<UploadImgItemBean> lenderSpList = new ArrayList<>();
    private List<UploadImgItemBean> guarantorList = new ArrayList<>();
    private List<UploadImgItemBean> guarantorSpList = new ArrayList<>();

    public String relation_name = "";


    public static CreditInfoFragment newInstance() {

        Bundle args = new Bundle();

        CreditInfoFragment fragment = new CreditInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private boolean isSqs2ExpandLinExpand = true;
    private boolean isSqs3ExpandLinExpand = true;

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
        //     sqs2Lin = view.findViewById(R.id.credit_info_sqs2_lin);
        //   sqs3Lin = view.findViewById(R.id.credit_info_sqs3_lin);
        // sqs2expandImg = ((ImageView) view.findViewById(R.id.credit_info_sqs2_expand_img));
        //sqs3expandImg = ((ImageView) view.findViewById(R.id.credit_info_sqs3_expand_img));
        submitBtn = (Button) view.findViewById(R.id.credit_info_submit_btn);

        client_info_name = (TextView) view.findViewById(R.id.client_info_name);
        client_phoneNumber = (TextView) view.findViewById(R.id.client_phoneNumber);
        client_ID_card = (TextView) view.findViewById(R.id.client_ID_card);
        findTv = (TextView) view.findViewById(R.id.tv_find);
//        private LinearLayout client_credit__book_lin;  //申请人征信
        client_credit__book_lin = (LinearLayout) view.findViewById(R.id.client_credit__book_lin1);
//        private LinearLayout client_spouse_credit__book_lin;//申请人配偶
        client_spouse_credit__book_lin = (LinearLayout) view.findViewById(R.id.client_spouse_credit__book_lin2);
//        private LinearLayout credit_applicate_detail_lin;//用户详情
        credit_applicate_detail_lin = (LinearLayout) view.findViewById(R.id.credit_applicate_detail_lin);
//        private LinearLayout guarantor_credit_book_lin;//担保人授权
        guarantor_credit_book_lin = (LinearLayout) view.findViewById(R.id.guarantor_credit_book_lin3);
//        private LinearLayout guarantor_spouse_credit_book_lin;//担保人配偶
        guarantor_spouse_credit_book_lin = (LinearLayout) view.findViewById(R.id.guarantor_spouse_credit_book_lin4);
//        private LinearLayout client_relationship_lin;//车主与申请人关系
        client_relationship_lin = (LinearLayout) view.findViewById(R.id.client_relationship_lin);

        chooseRelationTv = (TextView) view.findViewById(R.id.choose_relation);

        mobile_sfz_lin = (LinearLayout) view.findViewById(R.id.mobile_sfz_lin);

        autonym_certify_id_back_tv = (TextView) view.findViewById(R.id.autonym_certify_id_back_tv);
        autonym_certify_id_back_tv1 = (TextView) view.findViewById(R.id.autonym_certify_id_back_tv1);
        autonym_certify_id_back_tv2 = (TextView) view.findViewById(R.id.autonym_certify_id_back_tv2);
        autonym_certify_id_back_tv3 = (TextView) view.findViewById(R.id.autonym_certify_id_back_tv3);

        // credit_info = (ConstraintLayout) view.findViewById(R.id.credit_info);

        personal_info_group = (LinearLayout) view.findViewById(R.id.personal_info_group);

        credit_applicate_detail_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ApplicantDetailActivity.class);
                intent.putExtra("clt_id", clt_id);
                startActivity(intent);
            }
        });
        client_relationship_lin.setOnClickListener(new View.OnClickListener() {//申请人与车主关系
            @Override
            public void onClick(View v) {
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
            }
        });


        client_credit__book_lin.setOnClickListener(new View.OnClickListener() {//申请人授权书
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext, "点击了本人授权书", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(mContext, UploadListActivity.class);
                intent.putExtra("sqs", "shouquanshu");
                intent.putExtra("type", Constants.FileLabelType.POWER_OF_ATTORNEY_LENDER);
                intent.putExtra("role", Constants.PersonType.LENDER);
                intent.putExtra("imgList", (Serializable) lenderList);
                intent.putExtra("title", "申请人征信授权书");
                startActivityForResult(intent, Constants.REQUEST_MULTI_DOCUMENT);
            }
        });
        client_spouse_credit__book_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, UploadListActivity.class);
                intent.putExtra("sqs", "shouquanshu");
                intent.putExtra("type", Constants.FileLabelType.POWER_OF_ATTORNEY_LENDER_SP);
                intent.putExtra("role", Constants.PersonType.LENDER_SP);
                intent.putExtra("imgList", (Serializable) lenderSpList);
                intent.putExtra("title", "申请人配偶征信授权书");
                startActivityForResult(intent, Constants.REQUEST_MULTI_DOCUMENT);
            }
        });
        guarantor_credit_book_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UploadListActivity.class);
                intent.putExtra("sqs", "shouquanshu");
                intent.putExtra("type", Constants.FileLabelType.POWER_OF_ATTORNEY_GUARANTOR);
                intent.putExtra("role", Constants.PersonType.GUARANTOR);
                intent.putExtra("imgList", (Serializable) guarantorList);
                intent.putExtra("title", "担保人征信授权书");
                startActivityForResult(intent, Constants.REQUEST_MULTI_DOCUMENT);
            }
        });
        guarantor_spouse_credit_book_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UploadListActivity.class);
                intent.putExtra("sqs", "shouquanshu");
                intent.putExtra("type", "auth_credit");
                intent.putExtra("role", Constants.PersonType.GUARANTOR_SP);
                intent.putExtra("imgList", (Serializable) guarantorSpList);
                intent.putExtra("title", "担保人配偶征信授权书");
                startActivityForResult(intent, Constants.REQUEST_MULTI_DOCUMENT);
            }
        });


        findTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //点击跳转  到 检索页面 并 返回 数据 进行 展示。
                Intent intent = new Intent(mContext, SearchClientActivity.class);
                startActivityForResult(intent, 2000);
//                OrderApi.searchClientExist(mContext, "李拓", new OnItemDataCallBack<SearchClientResp>() {
//                    @Override
//                    public void onItemDataCallBack(SearchClientResp data) {
//
//                    }
//                });
            }
        });


//        view.findViewById(R.id.credit_info_sqs2_expand_lin).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isSqs2ExpandLinExpand) {
//                    isSqs2ExpandLinExpand = false;
//                    sqs2expandImg.setImageResource(R.mipmap.expand_plus);
//                    sqs2Lin.setVisibility(View.GONE);
//                } else {
//                    isSqs2ExpandLinExpand = true;
//                    sqs2expandImg.setImageResource(R.mipmap.expand_sub);
//                    sqs2Lin.setVisibility(View.VISIBLE);
//                }
//            }
//        });

//        view.findViewById(R.id.credit_info_sqs3_expand_lin).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isSqs3ExpandLinExpand) {
//                    isSqs3ExpandLinExpand = false;
//                    sqs3expandImg.setImageResource(R.mipmap.expand_plus);
//                    sqs3Lin.setVisibility(View.GONE);
//                } else {
//                    isSqs3ExpandLinExpand = true;
//                    sqs3expandImg.setImageResource(R.mipmap.expand_sub);
//                    sqs3Lin.setVisibility(View.VISIBLE);
//                }
//            }
//        });

//        idNo = (EditText) view.findViewById(R.id.credit_info_id_tv);
//
//        sqs1Img = (ImageView) view.findViewById(R.id.credit_info_sqs1_img);
        /*
        sqs1Img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentClickedView = v.getId();
                sqsFile1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), System.currentTimeMillis() + ".jpg");
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(sqsFile1));
                startActivityForResult(intent, 1000);

            }
        });
        */
        //sqs2Img = (ImageView) view.findViewById(R.id.credit_info_sqs2_img);
        /*
        sqs2Img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentClickedView = v.getId();
                //startActivityForResult(new Intent(mContext, CameraActivity.class), 1000);
                sqsFile2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), System.currentTimeMillis() + ".jpg");
                //File idBackFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), System.currentTimeMillis().)
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(sqsFile2));
                startActivityForResult(intent, 1000);
            }
        });
        */
        //  sqs3Img = (ImageView) view.findViewById(R.id.credit_info_sqs3_img);
/*
        sqs3Img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentClickedView = v.getId();
                sqsFile3 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), System.currentTimeMillis() + ".jpg");
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(sqsFile3));
                startActivityForResult(intent, 1000);
            }
        });
*/
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<UploadFilesUrlReq.FileUrlBean> files = new ArrayList<>();
                if (lenderList.size() > 0) {
                    for (int i = 0; i < lenderList.size(); i++) {
                        UploadFilesUrlReq.FileUrlBean fileUrlBean = new UploadFilesUrlReq.FileUrlBean();
                        UploadImgItemBean uploadImgItemBean = lenderList.get(i);
                        fileUrlBean.file_id = uploadImgItemBean.objectKey;
                        fileUrlBean.label = "auth_credit";
                        fileUrlBean.clt_id = clt_id;
                        files.add(fileUrlBean);
                    }
                }
                if (lenderSpList.size() > 0) {
                    for (int i = 0; i < lenderSpList.size(); i++) {
                        UploadFilesUrlReq.FileUrlBean fileUrlBean = new UploadFilesUrlReq.FileUrlBean();
                        UploadImgItemBean uploadImgItemBean = lenderList.get(i);
                        fileUrlBean.file_id = uploadImgItemBean.objectKey;
                        fileUrlBean.label = "auth_credit";
                        fileUrlBean.clt_id = clt_id;
                        files.add(fileUrlBean);
                    }
                }
                if (guarantorList.size() > 0) {
                    for (int i = 0; i < guarantorList.size(); i++) {
                        UploadFilesUrlReq.FileUrlBean fileUrlBean = new UploadFilesUrlReq.FileUrlBean();
                        UploadImgItemBean uploadImgItemBean = lenderList.get(i);
                        fileUrlBean.file_id = uploadImgItemBean.objectKey;
                        fileUrlBean.label = "auth_credit";
                        fileUrlBean.clt_id = clt_id;
                        files.add(fileUrlBean);
                    }
                }
                if (guarantorSpList.size() > 0) {
                    for (int i = 0; i < guarantorSpList.size(); i++) {
                        UploadFilesUrlReq.FileUrlBean fileUrlBean = new UploadFilesUrlReq.FileUrlBean();
                        UploadImgItemBean uploadImgItemBean = lenderList.get(i);
                        fileUrlBean.file_id = uploadImgItemBean.objectKey;
                        fileUrlBean.label = "auth_credit";
                        fileUrlBean.clt_id = clt_id;
                        files.add(fileUrlBean);
                    }
                }
                /*
                if (!TextUtils.isEmpty(sqs1Url)) {
                    UploadFilesUrlReq.FileUrlBean fileUrlBean = new UploadFilesUrlReq.FileUrlBean();
                    fileUrlBean.file_id = sqs1Url;
                    fileUrlBean.label = "auth_credit";
                    fileUrlBean.role = "lender";
                    files.add(fileUrlBean);
                }
                if (!TextUtils.isEmpty(sqs2Url)) {
                    UploadFilesUrlReq.FileUrlBean fileUrlBean = new UploadFilesUrlReq.FileUrlBean();
                    fileUrlBean.file_id = sqs2Url;
                    fileUrlBean.label = "auth_credit";
                    fileUrlBean.role = "lender_sp";
                    files.add(fileUrlBean);
                }
                if (!TextUtils.isEmpty(sqs3Url)) {
                    UploadFilesUrlReq.FileUrlBean fileUrlBean = new UploadFilesUrlReq.FileUrlBean();
                    fileUrlBean.file_id = sqs3Url;
                    fileUrlBean.label = "auth_credit";
                    fileUrlBean.role = "guarantor";
                    files.add(fileUrlBean);
                }
                */

                if (files.size() == 0) {
                    String s = chooseRelationTv.getText().toString();
                    //Toast.makeText(mContext, s, Toast.LENGTH_LONG).show();
                    SubmitOrderReq req = ((ApplyFinancingFragment) getParentFragment()).req;
                    //req.id_no = idNo.getText().toString();//*号导致这个位置会出问题
                    // req.id_no = id_no_r;
                    req.clt_id = clt_id;
                    req.vehicle_owner_lender_relation = chooseRelationTv.getText().toString();
                    //Log.e("s3", req.bank_id);
                    if (chooseRelationTv.getText().toString().equals("请选择")) {
                        Toast.makeText(mContext, "请选择车主与申请人关系", Toast.LENGTH_LONG).show();
                    } else {
                        OrderApi.submitOrder(mContext, req, new OnItemDataCallBack<SubmitOrderResp>() {
                            @Override
                            public void onItemDataCallBack(SubmitOrderResp data) {
                                if (data != null) {
                                    Toast.makeText(mContext, "订单提交成功", Toast.LENGTH_SHORT).show();
                                    EventBus.getDefault().post(ApplyFinancingFragmentEvent.reset);
                                } else {
                                    return;
                                }
                            }
                        });
                    }

                } else {
                    //未授权也可以 测试时用 后期去除
                    UploadFilesUrlReq uploadFilesUrlReq = new UploadFilesUrlReq();
                    uploadFilesUrlReq.clt_id = clt_id;
                    uploadFilesUrlReq.files = files;
                    uploadFilesUrlReq.region = SharedPrefsUtil.getInstance(mContext).getValue("region", "");
                    uploadFilesUrlReq.bucket = SharedPrefsUtil.getInstance(mContext).getValue("bucket", "");
                    Dialog dialog = LoadingUtils.createLoadingDialog(mContext);
                    UploadApi.uploadFileUrl(mContext, uploadFilesUrlReq, (code, msg) -> {
                        if (code == 0) {
                            SubmitOrderReq req = ((ApplyFinancingFragment) getParentFragment()).req;
                            // req.id_no = idNo.getText().toString();
                            req.id_no = id_no_r;
                            req.clt_id = clt_id;
                            req.vehicle_owner_lender_relation = chooseRelationTv.getText().toString();
                            OrderApi.submitOrder(mContext, req, new OnItemDataCallBack<SubmitOrderResp>() {
                                @Override
                                public void onItemDataCallBack(SubmitOrderResp resp) {
                                    if (resp != null) {
                                        Toast.makeText(mContext, "订单提交成功", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        EventBus.getDefault().post(ApplyFinancingFragmentEvent.reset);
                                    } else {
                                        return;
                                    }
                                }
                            });
                        } else return;
                    });
                }

            }
        });
    }


    private String sqs1Url;
    private String sqs2Url;
    private String sqs3Url;

    //private var divorceImgsList = ArrayList<UploadImgItemBean>()

    //client_credit__book_lin


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == 1000) {
//                switch (currentClickedView) {
        // case R.id.credit_info_sqs1_img:
//                        Glide.with(mContext).load(sqsFile1).into(sqs1Img);
//                        OssUtil.uploadOss(mContext, true, sqsFile1.getAbsolutePath(), new OSSObjectKeyBean("lender", "auth_credit", ".png"), new OnItemDataCallBack<String>() {
//                            @Override
//                            public void onItemDataCallBack(String objectKey) {
//                                sqs1Url = objectKey;
//                            }
//                        }, null);
//                        break;

        // case R.id.credit_info_sqs2_img:
//                        Glide.with(mContext).load(sqsFile2).into(sqs2Img);
//                        OssUtil.uploadOss(mContext, true, sqsFile2.getAbsolutePath(), new OSSObjectKeyBean("lender_sp", "auth_credit", ".png"), new OnItemDataCallBack<String>() {
//                            @Override
//                            public void onItemDataCallBack(String objectKey) {
//                                sqs2Url = objectKey;
//                            }
//                        }, null);
//                        break;

        //    case R.id.credit_info_sqs3_img:
//                        Glide.with(mContext).load(sqsFile3).into(sqs3Img);
//                        OssUtil.uploadOss(mContext, true, sqsFile3.getAbsolutePath(), new OSSObjectKeyBean("guarantor", "auth_credit", ".png"), new OnItemDataCallBack<String>() {
//                            @Override
//                            public void onItemDataCallBack(String objectKey) {
//                                sqs3Url = objectKey;
//                            }
//                        }, null);
        //break;
//                }
//            }
//        } else if (requestCode == Constants.REQUEST_IDCARD_1_CAPTURE) {
//            String url = data.getStringExtra("url");// /storage/emulated/0/Pictures/loner_upload_image_1499744608501.jpg
//            if (!TextUtils.isEmpty(url)) {
//
//            }
//        }

        // else if (resultCode == 2000) {
        //if (resultCode == Activity.RESULT_OK) {

        if (resultCode == 2000) {
            if (requestCode == 2000) {
//                sqs2Img.setImageResource(R.mipmap.sqs);
//                sqs1Img.setImageResource(R.mipmap.sqs);
//                sqs3Img.setImageResource(R.mipmap.sqs);
//                sqs1Url = "";
//                sqs2Url = "";
//                sqs3Url = "";

                //idNo.setText(data.getStringExtra("sfz"));
                personal_info_group.setVisibility(View.VISIBLE);

                client_info_name.setText(data.getStringExtra("name"));
                client_ID_card.setText(data.getStringExtra("sfz"));
                client_phoneNumber.setText(data.getStringExtra("mobile"));

                if (data.getStringExtra("isHasLender").equals("1")) { //不等于空是1 等于空是2 申请人征信授权书
                    client_credit__book_lin.setVisibility(View.VISIBLE);
                    if (Integer.valueOf(data.getStringExtra("lender")) > 0) {
                        autonym_certify_id_back_tv.setText("已上传");
                        autonym_certify_id_back_tv.setTextColor(Color.parseColor("#06b7a3"));
                    }
                } else {
                    client_credit__book_lin.setVisibility(View.GONE);
                }


                if (data.getStringExtra("isHasLender_sp").equals("1")) { //不等于空是1 等于空是2
                    client_spouse_credit__book_lin.setVisibility(View.VISIBLE);

                    if (Integer.valueOf(data.getStringExtra("lender_sp")) > 0) {
                        autonym_certify_id_back_tv1.setText("已上传");
                        autonym_certify_id_back_tv1.setTextColor(Color.parseColor("#06b7a3"));
                    }
                } else {
                    client_spouse_credit__book_lin.setVisibility(View.GONE);
                }


                if (data.getStringExtra("isGuarantor").equals("1")) { //不等于空是1 等于空是2
                    guarantor_credit_book_lin.setVisibility(View.VISIBLE);

                    if (Integer.valueOf(data.getStringExtra("guarantor")) > 0) {
                        autonym_certify_id_back_tv2.setText("已上传");
                        autonym_certify_id_back_tv2.setTextColor(Color.parseColor("#06b7a3"));
                    }
                } else {
                    guarantor_credit_book_lin.setVisibility(View.GONE);
                }

                if (data.getStringExtra("isGuarantor_sp").equals("1")) { //不等于空是1 等于空是2
                    guarantor_spouse_credit_book_lin.setVisibility(View.VISIBLE);

                    if (Integer.valueOf(data.getStringExtra("guarantor_sp")) > 0) {
                        autonym_certify_id_back_tv3.setText("已上传");
                        autonym_certify_id_back_tv3.setTextColor(Color.parseColor("#06b7a3"));
                    }
                } else {
                    guarantor_spouse_credit_book_lin.setVisibility(View.GONE);
                }


//                intent.putExtra("lender", item.auth_credit.lender.auth_credit_img_count);
//                intent.putExtra("lender_sp", item.auth_credit.lender_sp.auth_credit_img_count);
//                intent.putExtra("guarantor", item.auth_credit.guarantor.auth_credit_img_count);
//                intent.putExtra("guarantor_sp", item.auth_credit.guarantor_sp.auth_credit_img_count);

//                if (Integer.valueOf(data.getStringExtra("lender")) > 0) {
//                    autonym_certify_id_back_tv.setText("已上传");
//                    autonym_certify_id_back_tv.setTextColor(Color.parseColor("#06b7a3"));
//                }
//                if (Integer.valueOf(data.getStringExtra("lender_sp")) > 0) {
//                    autonym_certify_id_back_tv1.setText("已上传");
//                    autonym_certify_id_back_tv1.setTextColor(Color.parseColor("#06b7a3"));
//                }
//                if (Integer.valueOf(data.getStringExtra("guarantor")) > 0) {
//                    autonym_certify_id_back_tv2.setText("已上传");
//                    autonym_certify_id_back_tv2.setTextColor(Color.parseColor("#06b7a3"));
//                }
//                if (Integer.valueOf(data.getStringExtra("guarantor_sp")) > 0) {
//                    autonym_certify_id_back_tv3.setText("已上传");
//                    autonym_certify_id_back_tv3.setTextColor(Color.parseColor("#06b7a3"));
//                }

                //data.getStringExtra("sfz");

//                if (data.getStringExtra("image1") != null) {
//                    Glide.with(mContext).load(data.getStringExtra("image1")).into(sqs1Img);
//                }
//                if (data.getStringExtra("image2") != null) {
//                    Glide.with(mContext).load(data.getStringExtra("image2")).into(sqs2Img);
//                }
//                if (data.getStringExtra("image3") != null) {
//                    Glide.with(mContext).load(data.getStringExtra("image3")).into(sqs3Img);
//                }
                if (data.getStringExtra("clt_id") != null) {
                    clt_id = data.getStringExtra("clt_id");
                }
//                if (data.getStringExtra("id_no_r") != null) {
//                    id_no_r = data.getStringExtra("id_no_r");
//                }

//                Glide.with(mContext).load(data.getStringExtra("image1")).into(sqs1Img);
//                Glide.with(mContext).load(data.getStringExtra("image2")).into(sqs2Img);
//                Glide.with(mContext).load(data.getStringExtra("image3")).into(sqs3Img);
                submitBtn.setEnabled(data.getBooleanExtra("enable", false));

            }
        } else if (requestCode == Constants.REQUEST_MULTI_DOCUMENT) {
            switch (data.getStringExtra("type")) {
                case Constants.FileLabelType.POWER_OF_ATTORNEY_LENDER:
                    lenderList = (List<UploadImgItemBean>) data.getSerializableExtra("imgList");
                    if (lenderList.size() > 0) {
                        autonym_certify_id_back_tv.setText("已上传");
                        autonym_certify_id_back_tv.setTextColor(Color.parseColor("#06b7a3"));
                    } else {
                        autonym_certify_id_back_tv.setText("请上传");
                        autonym_certify_id_back_tv.setTextColor(Color.parseColor("#d1d1d1"));
                    }
                    break;
                case Constants.FileLabelType.POWER_OF_ATTORNEY_LENDER_SP:
                    lenderSpList = (List<UploadImgItemBean>) data.getSerializableExtra("imgList");
                    if (lenderSpList.size() > 0) {
                        autonym_certify_id_back_tv1.setText("已上传");
                        autonym_certify_id_back_tv1.setTextColor(Color.parseColor("#06b7a3"));
                    } else {
                        autonym_certify_id_back_tv1.setText("请上传");
                        autonym_certify_id_back_tv1.setTextColor(Color.parseColor("#d1d1d1"));
                    }

                    break;
                case Constants.FileLabelType.POWER_OF_ATTORNEY_GUARANTOR:
                    guarantorList = (List<UploadImgItemBean>) data.getSerializableExtra("imgList");
                    if (guarantorList.size() > 0) {
                        autonym_certify_id_back_tv2.setText("已上传");
                        autonym_certify_id_back_tv2.setTextColor(Color.parseColor("#06b7a3"));
                    } else {
                        autonym_certify_id_back_tv2.setText("请上传");
                        autonym_certify_id_back_tv2.setTextColor(Color.parseColor("#d1d1d1"));
                    }

                    break;
                case Constants.FileLabelType.POWER_OF_ATTORNEY_GUARANTOR_SP:
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
