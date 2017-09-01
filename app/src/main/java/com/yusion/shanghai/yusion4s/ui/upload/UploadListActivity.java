package com.yusion.shanghai.yusion4s.ui.upload;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pbq.pickerlib.activity.PhotoMediaActivity;
import com.pbq.pickerlib.entity.PhotoVideoDir;
import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.adapter.UploadImgListAdapter;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.bean.oss.OSSObjectKeyBean;
import com.yusion.shanghai.yusion4s.bean.upload.DelImgsReq;
import com.yusion.shanghai.yusion4s.bean.upload.ListImgsReq;
import com.yusion.shanghai.yusion4s.bean.upload.UploadImgItemBean;
import com.yusion.shanghai.yusion4s.bean.upload.UploadLabelItemBean;
import com.yusion.shanghai.yusion4s.retrofit.api.UploadApi;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnCodeAndMsgCallBack;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.utils.LoadingUtils;
import com.yusion.shanghai.yusion4s.utils.OssUtil;
import com.yusion.shanghai.yusion4s.widget.TitleBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class UploadListActivity extends BaseActivity {

    private UploadImgListAdapter adapter;
    private Intent mGetIntent;
    private UploadLabelItemBean mTopItem;//上级页面传过来的bean
    private TextView errorTv;
    private LinearLayout errorLin;
    private List<UploadImgItemBean> imgList;
    private TitleBar titleBar;
    private String mRole;
    private String mType;

    private String kind;


    private int currentChooseCount = 0;
    private boolean hasImg = false;
    private boolean isEditing = false;
    private TextView mEditTv;
    private LinearLayout uploadBottomLin;
    private TextView uploadTv2;
    private TextView uploadTv1;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inFlater = getLayoutInflater();
        mGetIntent = getIntent();
        mRole = mGetIntent.getStringExtra("role");
        kind = mGetIntent.getStringExtra("sqs");
        if (kind.equals("shouquanshu")) {
            //view =  view = inFlater.inflate(R.layout.activity_authorize, null);
            view = inFlater.inflate(R.layout.activi_upload_list_sqs, null);

        } else {
            view = inFlater.inflate(R.layout.activity_upload_list, null);
        }
        setContentView(view);

        mTopItem = (UploadLabelItemBean) mGetIntent.getSerializableExtra("topItem");
        if (mTopItem != null) {
            imgList = mTopItem.img_list;
            mType = mTopItem.value;
            titleBar = initTitleBar(this, mTopItem.name).setLeftClickListener(v -> onBack());
        } else {
            //户口本等
            imgList = ((ArrayList<UploadImgItemBean>) mGetIntent.getSerializableExtra("imgList"));
            mType = mGetIntent.getStringExtra("type");
            titleBar = initTitleBar(this, mGetIntent.getStringExtra("title")).setLeftClickListener(v -> onBack());
        }

        uploadBottomLin = (LinearLayout) findViewById(R.id.upload_bottom_lin);
        uploadTv1 = (TextView) findViewById(R.id.upload_bottom_tv1);
        uploadTv2 = (TextView) findViewById(R.id.upload_bottom_tv2);
        uploadTv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploadTv1.getText().toString().equals("全选")) {
                    for (UploadImgItemBean itemBean : imgList) {
                        itemBean.hasChoose = true;
                    }
                    currentChooseCount = imgList.size();
                    uploadTv1.setText("取消全选");
                    uploadTv2.setText(String.format("删除(%d)", imgList.size()));
                    uploadTv2.setTextColor(Color.RED);
                    adapter.notifyDataSetChanged();
                } else if (uploadTv1.getText().toString().equals("取消全选")) {
                    for (UploadImgItemBean itemBean : imgList) {
                        itemBean.hasChoose = false;
                    }
                    currentChooseCount = 0;
                    uploadTv1.setText("全选");
                    uploadTv2.setText("删除");
                    uploadTv2.setTextColor(Color.parseColor("#d1d1d1"));
                    adapter.notifyDataSetChanged();
                }
            }
        });
        uploadTv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> delImgIdList = new ArrayList<>();
                List<Integer> integerList = new ArrayList<>();
                for (int i = 0; i < imgList.size(); i++) {
                    if (imgList.get(i).hasChoose) integerList.add(i);
                }
                Collections.sort(integerList);
                int pyl = 0;
                for (int i = 0; i < integerList.size(); i++) {
                    int delIndex = integerList.get(i) - pyl;
                    delImgIdList.add(imgList.get(delIndex).id);
                    imgList.remove(delIndex);
                    pyl++;
                }
                uploadTv2.setText("删除");
                uploadTv2.setTextColor(Color.parseColor("#d1d1d1"));
                adapter.notifyDataSetChanged();
                DelImgsReq req = new DelImgsReq();
                req.clt_id = mGetIntent.getStringExtra("clt_id");
                //删除的图片中包括用户拍摄后没有上传到服务器的图片 这个时候没有id
                List<String> relDelImgIdList = new ArrayList<>();
                for (String s : delImgIdList) {
                    if (!TextUtils.isEmpty(s)) {
                        relDelImgIdList.add(s);
                    }
                }
                req.id.addAll(relDelImgIdList);
                if (relDelImgIdList.size() > 0) {
                    UploadApi.delImgs(UploadListActivity.this, req, new OnCodeAndMsgCallBack() {
                        @Override
                        public void callBack(int code, String msg) {
                            if (code == 0) {
                                Toast.makeText(myApp, "删除成功", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        mEditTv = titleBar.getRightTextTv();
        titleBar.setRightText("编辑").setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditing) {
                    isEditing = false;
                    mEditTv.setText("编辑");
                    uploadBottomLin.setVisibility(View.GONE);
                } else {
                    isEditing = true;
                    mEditTv.setText("取消");
                    uploadBottomLin.setVisibility(View.VISIBLE);
                }
                adapter.setIsEditing(isEditing);
                adapter.notifyDataSetChanged();
            }
        });
        hasImg = imgList.size() > 0;
        onImgCountChange(hasImg);
        RecyclerView rv = (RecyclerView) findViewById(R.id.upload_list_rv);
        errorTv = (TextView) findViewById(R.id.upload_list_error_tv);
        errorLin = (LinearLayout) findViewById(R.id.upload_list_error_lin);
        rv.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new UploadImgListAdapter(this, imgList);
        adapter.setOnItemClick(new UploadImgListAdapter.OnItemClick() {
            @Override
            public void onItemClick(View v, UploadImgItemBean item, ImageView cbImg) {
                if (isEditing) {
                    boolean hasChoose = (Boolean) cbImg.getTag(R.id.hasChoose);
                    if (hasChoose) {
                        cbImg.setTag(R.id.hasChoose, false);
                        cbImg.setImageResource(R.mipmap.choose_icon);
                        item.hasChoose = false;
                        currentChooseCount--;
                    } else {
                        cbImg.setTag(R.id.hasChoose, true);
                        cbImg.setImageResource(R.mipmap.surechoose_icon);
                        item.hasChoose = true;
                        currentChooseCount++;

                    }
                    if (currentChooseCount != 0) {
                        uploadTv2.setText(String.format("删除(%d)", currentChooseCount));
                        uploadTv2.setTextColor(Color.RED);
                    } else {
                        uploadTv2.setText("删除");
                        uploadTv2.setTextColor(Color.parseColor("#d1d1d1"));
                    }
                }
            }

            @Override
            public void onFooterClick(View v) {
                Intent i = new Intent(UploadListActivity.this, PhotoMediaActivity.class);
                i.putExtra("loadType", PhotoVideoDir.Type.IMAGE.toString());
                startActivityForResult(i, 100);
            }
        });
        rv.setAdapter(adapter);
        initData();
    }

    private void initData() {
        if (mTopItem == null) {

        } else {
            if (!mTopItem.hasGetImgsFromServer) {
                //第一次进入
                ListImgsReq req = new ListImgsReq();
                req.label = mTopItem.value;
                req.clt_id = mGetIntent.getStringExtra("clt_id");
                UploadApi.listImgs(this, req, resp -> {
                    mTopItem.hasGetImgsFromServer = true;
                    if (resp.has_err) {
                        errorLin.setVisibility(View.VISIBLE);
                        errorTv.setText("您提交的资料有误：" + resp.error);
                        mTopItem.errorInfo = "您提交的资料有误：" + resp.error;
                    } else {
                        errorLin.setVisibility(View.GONE);
                        mTopItem.errorInfo = "";
                    }
                    if (resp.list.size() != 0) {
                        imgList.addAll(resp.list);
                        hasImg = true;
                        onImgCountChange(hasImg);
                        adapter.notifyDataSetChanged();
                    }
                });
            } else if (mTopItem.hasError) {
                errorLin.setVisibility(View.VISIBLE);
                errorTv.setText(mTopItem.errorInfo);
            }
        }
    }

    private void onImgCountChange(boolean hasImg) {
        if (hasImg) {
            mEditTv.setEnabled(true);
            mEditTv.setTextColor(Color.parseColor("#ffffff"));
        } else {
            mEditTv.setEnabled(false);
            mEditTv.setTextColor(Color.parseColor("#d1d1d1"));
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> files = data.getStringArrayListExtra("files");
                if (files.size() > 0) {
                    hasImg = true;
                    onImgCountChange(hasImg);
                }
                List<UploadImgItemBean> toAddList = new ArrayList<>();
                for (String file : files) {
                    UploadImgItemBean item = new UploadImgItemBean();
                    item.local_path = file;
                    item.role = mRole;
                    item.type = mType;
                    toAddList.add(item);
                }

                imgList.addAll(toAddList);
                adapter.notifyItemRangeInserted(adapter.getItemCount(), files.size());

                Dialog dialog = LoadingUtils.createLoadingDialog(this);
                dialog.show();
                int account = 0;
                for (UploadImgItemBean imgItemBean : toAddList) {
                    account++;
                    int finalAccount = account;
                    OssUtil.uploadOss(this, false, imgItemBean.local_path, new OSSObjectKeyBean(mRole, mType, ".png"), new OnItemDataCallBack<String>() {
                        @Override
                        public void onItemDataCallBack(String objectKey) {
                            imgItemBean.objectKey = objectKey;
                            if (finalAccount == files.size()) {
                                dialog.dismiss();
                            }
                        }
                    }, new OnItemDataCallBack<Throwable>() {
                        @Override
                        public void onItemDataCallBack(Throwable data) {
                            if (finalAccount == files.size()) {
                                dialog.dismiss();
                            }
                        }
                    });
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    private void onBack() {
        imgList = ((ArrayList<UploadImgItemBean>) mGetIntent.getSerializableExtra("imgList"));
        setResult(RESULT_OK, mGetIntent);
        finish();
    }
}

