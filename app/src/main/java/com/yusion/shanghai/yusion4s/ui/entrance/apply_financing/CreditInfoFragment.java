package com.yusion.shanghai.yusion4s.ui.entrance.apply_financing;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.BaseFragment;
import com.yusion.shanghai.yusion4s.bean.order.CheckClientExistResp;
import com.yusion.shanghai.yusion4s.bean.order.submit.SubmitOrderReq;
import com.yusion.shanghai.yusion4s.bean.order.submit.SubmitOrderResp;
import com.yusion.shanghai.yusion4s.bean.oss.OSSObjectKeyBean;
import com.yusion.shanghai.yusion4s.bean.upload.UploadFilesUrlReq;
import com.yusion.shanghai.yusion4s.event.ApplyFinancingFragmentEvent;
import com.yusion.shanghai.yusion4s.retrofit.api.OrderApi;
import com.yusion.shanghai.yusion4s.retrofit.api.UploadApi;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.settings.Constants;
import com.yusion.shanghai.yusion4s.ui.ApplyFinancingFragment;
import com.yusion.shanghai.yusion4s.utils.LoadingUtils;
import com.yusion.shanghai.yusion4s.utils.OssUtil;
import com.yusion.shanghai.yusion4s.utils.SharedPrefsUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;

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

    private File sqsFile1;
    private File sqsFile2;
    private File sqsFile3;

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_credit_info, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        sqsFile1 = new File("");

        TextView step1 = (TextView) view.findViewById(R.id.step1);
        step1.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "yj.ttf"));
        step1.setOnClickListener(v -> EventBus.getDefault().post(ApplyFinancingFragmentEvent.showCarInfo));
        ((TextView) view.findViewById(R.id.step2)).setTypeface(Typeface.createFromAsset(mContext.getAssets(), "yj.ttf"));
        sqs2Lin = view.findViewById(R.id.credit_info_sqs2_lin);
        sqs3Lin = view.findViewById(R.id.credit_info_sqs3_lin);
        sqs2expandImg = ((ImageView) view.findViewById(R.id.credit_info_sqs2_expand_img));
        sqs3expandImg = ((ImageView) view.findViewById(R.id.credit_info_sqs3_expand_img));
        TextView submitBtn = (TextView) view.findViewById(R.id.credit_info_submit_btn);
        findTv = (TextView) view.findViewById(R.id.tv_find);
        findTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击跳转  到 检索页面 并 返回 数据 进行 展示。
                //Intent intent = new Intent(mContext, FindActivity.class);
                //startActivity(intent);//暂且 进行跳转操作
            }
        });


        view.findViewById(R.id.credit_info_sqs2_expand_lin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSqs2ExpandLinExpand) {
                    isSqs2ExpandLinExpand = false;
                    sqs2expandImg.setImageResource(R.mipmap.expand_plus);
                    sqs2Lin.setVisibility(View.GONE);
                } else {
                    isSqs2ExpandLinExpand = true;
                    sqs2expandImg.setImageResource(R.mipmap.expand_sub);
                    sqs2Lin.setVisibility(View.VISIBLE);
                }
            }
        });
        view.findViewById(R.id.credit_info_sqs3_expand_lin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSqs3ExpandLinExpand) {
                    isSqs3ExpandLinExpand = false;
                    sqs3expandImg.setImageResource(R.mipmap.expand_plus);
                    sqs3Lin.setVisibility(View.GONE);
                } else {
                    isSqs3ExpandLinExpand = true;
                    sqs3expandImg.setImageResource(R.mipmap.expand_sub);
                    sqs3Lin.setVisibility(View.VISIBLE);
                }
            }
        });
        EditText idNo = (EditText) view.findViewById(R.id.credit_info_id_tv);
        idNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 18) {
                    OrderApi.checkClientExist(mContext, idNo.getText().toString(), new OnItemDataCallBack<CheckClientExistResp>() {
                        @Override
                        public void onItemDataCallBack(CheckClientExistResp resp) {
                            if (resp.exsits) {
                                Toast.makeText(mContext, "用户存在", Toast.LENGTH_SHORT).show();
                                clt_id = resp.clt_id;
                                submitBtn.setEnabled(true);
                            } else {
                                Toast.makeText(mContext, "用户不存在", Toast.LENGTH_SHORT).show();
                                submitBtn.setEnabled(false);
                                clt_id = "";
                            }
                        }
                    });
                }
            }
        });

        sqs1Img = (ImageView) view.findViewById(R.id.credit_info_sqs1_img);
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
        sqs2Img = (ImageView) view.findViewById(R.id.credit_info_sqs2_img);
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
        sqs3Img = (ImageView) view.findViewById(R.id.credit_info_sqs3_img);

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

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<UploadFilesUrlReq.FileUrlBean> files = new ArrayList<>();
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

                if (files.size() == 0) {
                    SubmitOrderReq req = ((ApplyFinancingFragment) getParentFragment()).req;
                    req.id_no = idNo.getText().toString();
                    OrderApi.submitOrder(mContext, req, new OnItemDataCallBack<SubmitOrderResp>() {
                        @Override
                        public void onItemDataCallBack(SubmitOrderResp data) {
                            Toast.makeText(mContext, "订单提交成功", Toast.LENGTH_SHORT).show();
                            EventBus.getDefault().post(ApplyFinancingFragmentEvent.reset);
                        }
                    });
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
                            req.id_no = idNo.getText().toString();
                            OrderApi.submitOrder(mContext, req, new OnItemDataCallBack<SubmitOrderResp>() {
                                @Override
                                public void onItemDataCallBack(SubmitOrderResp resp) {
                                    Toast.makeText(mContext, "订单提交成功", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    EventBus.getDefault().post(ApplyFinancingFragmentEvent.reset);
                                }
                            });
                        }
                    });
                }

            }
        });
    }


    private String sqs1Url;
    private String sqs2Url;
    private String sqs3Url;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1000) {
                switch (currentClickedView) {
                    case R.id.credit_info_sqs1_img:
                        Glide.with(mContext).load(sqsFile1).into(sqs1Img);
                        OssUtil.uploadOss(mContext, true, sqsFile1.getAbsolutePath(), new OSSObjectKeyBean("lender", "auth_credit", ".png"), new OnItemDataCallBack<String>() {
                            @Override
                            public void onItemDataCallBack(String objectKey) {
                                sqs1Url = objectKey;
                            }
                        }, null);
                        break;

                    case R.id.credit_info_sqs2_img:
                        Glide.with(mContext).load(sqsFile2).into(sqs2Img);
                        OssUtil.uploadOss(mContext, true, sqsFile2.getAbsolutePath(), new OSSObjectKeyBean("lender_sp", "auth_credit", ".png"), new OnItemDataCallBack<String>() {
                            @Override
                            public void onItemDataCallBack(String objectKey) {
                                sqs2Url = objectKey;
                            }
                        }, null);
                        break;

                    case R.id.credit_info_sqs3_img:
                        Glide.with(mContext).load(sqsFile3).into(sqs3Img);
                        OssUtil.uploadOss(mContext, true, sqsFile3.getAbsolutePath(), new OSSObjectKeyBean("guarantor", "auth_credit", ".png"), new OnItemDataCallBack<String>() {
                            @Override
                            public void onItemDataCallBack(String objectKey) {
                                sqs3Url = objectKey;
                            }
                        }, null);
                        break;
                }
            }
        } else if (requestCode == Constants.REQUEST_IDCARD_1_CAPTURE) {
            String url = data.getStringExtra("url");// /storage/emulated/0/Pictures/loner_upload_image_1499744608501.jpg
            if (!TextUtils.isEmpty(url)) {

            }
        }
    }
}
