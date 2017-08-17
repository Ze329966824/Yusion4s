package com.yusion.shanghai.yusion4s.ui.upload;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.Yusion4sApp;
import com.yusion.shanghai.yusion4s.adapter.UploadLabelListAdapter;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.bean.upload.ListLabelsErrorReq;
import com.yusion.shanghai.yusion4s.bean.upload.ListLabelsErrorResp;
import com.yusion.shanghai.yusion4s.bean.upload.UploadFilesUrlReq;
import com.yusion.shanghai.yusion4s.bean.upload.UploadLabelItemBean;
import com.yusion.shanghai.yusion4s.retrofit.api.UploadApi;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnCodeAndMsgCallBack;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.utils.LoadingUtils;
import com.yusion.shanghai.yusion4s.utils.SharedPrefsUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UploadLabelListActivity extends BaseActivity {

    public static List<UploadFilesUrlReq.FileUrlBean> uploadFileUrlBeanList = new ArrayList<>();
    private List<UploadLabelItemBean> mItems = new ArrayList<>();
    private UploadLabelListAdapter mAdapter;


    public static void start(Context context, String app_id) {
        Intent intent = new Intent(context, UploadLabelListActivity.class);
        intent.putExtra("app_id", app_id);
        context.startActivity(intent);
    }

    private Dialog mUploadFileDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_label_list);
        uploadFileUrlBeanList.clear();
        initTitleBar(this, "基本资料上传").setRightText("提交").setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploadFileUrlBeanList.size() == 0) {
                    Toast.makeText(myApp, "提交成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    if (mUploadFileDialog == null) {
                        mUploadFileDialog = LoadingUtils.createLoadingDialog(UploadLabelListActivity.this);
                    }
                    mUploadFileDialog.show();
                    UploadFilesUrlReq uploadFilesUrlReq = new UploadFilesUrlReq();
                    uploadFilesUrlReq.app_id = getIntent().getStringExtra("app_id");
                    uploadFilesUrlReq.files = uploadFileUrlBeanList;
                    uploadFilesUrlReq.region = SharedPrefsUtil.getInstance(UploadLabelListActivity.this).getValue("region", "");
                    uploadFilesUrlReq.bucket = SharedPrefsUtil.getInstance(UploadLabelListActivity.this).getValue("bucket", "");
                    UploadApi.uploadFileUrl(UploadLabelListActivity.this, uploadFilesUrlReq, new OnCodeAndMsgCallBack() {
                        @Override
                        public void callBack(int code, String msg) {
                            Toast.makeText(myApp, "提交成功", Toast.LENGTH_SHORT).show();
                            mUploadFileDialog.dismiss();
                            finish();
                        }
                    });
                }
            }
        }).setRightTextColor(Color.WHITE).setLeftClickListener(v -> onBack());
        RecyclerView rv = (RecyclerView) findViewById(R.id.upload_label_list_rv);
        rv.setLayoutManager(new LinearLayoutManager(this));

        try {
            JSONArray jsonArray = new JSONArray(Yusion4sApp.CONFIG_RESP.dealer_material);
            mItems.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                UploadLabelItemBean uploadLabelItemBean = new Gson().fromJson(jsonObject.toString(), UploadLabelItemBean.class);
                mItems.add(uploadLabelItemBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mAdapter = new UploadLabelListAdapter(this, mItems);
        mAdapter.setOnItemClick(new UploadLabelListAdapter.OnItemClick() {
            @Override
            public void onItemClick(View v, UploadLabelItemBean item, int index) {
                Intent intent = new Intent();
                if (item.label_list.size() == 0) {
                    //下级目录为图片页
                    intent.setClass(UploadLabelListActivity.this, UploadListActivity.class);
                } else {
                    //下级目录为标签页
                    intent.setClass(UploadLabelListActivity.this, UploadLabelListActivity.class);
                }
                intent.putExtra("item", item);
                intent.putExtra("name", item.name);
                intent.putExtra("index", index);
                intent.putExtra("app_id", getIntent().getStringExtra("app_id"));
                startActivityForResult(intent, 100);
            }
        });
        rv.setAdapter(mAdapter);
        initData();
    }

    private void initData() {
        ListLabelsErrorReq req = new ListLabelsErrorReq();
        req.app_id = getIntent().getStringExtra("app_id");
        ArrayList<String> labelsList = new ArrayList<>();
        for (UploadLabelItemBean itemBean : mItems) {
            labelsList.add(itemBean.value);
        }
        req.label_list = labelsList;
        UploadApi.listLabelsError(this, req, new OnItemDataCallBack<ListLabelsErrorResp>() {
            @Override
            public void onItemDataCallBack(ListLabelsErrorResp resp) {
                for (UploadLabelItemBean item : mItems) {
                    for (String errLabel : resp.lender.err_labels) {
                        if (item.value.equals(errLabel)) {
                            item.hasError = true;
                        }
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    private void onBack() {
        new AlertDialog.Builder(this).setMessage("您确定退出?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                UploadLabelItemBean item = (UploadLabelItemBean) data.getSerializableExtra("item");
                mItems.set(data.getIntExtra("index", -1), item);
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}