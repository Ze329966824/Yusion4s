package com.yusion.shanghai.yusion4s.ui.upload;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pbq.pickerlib.activity.PhotoMediaActivity;
import com.pbq.pickerlib.entity.PhotoVideoDir;
import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.adapter.UploadImgListAdapter;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.bean.oss.OSSObjectKeyBean;
import com.yusion.shanghai.yusion4s.bean.upload.ListImgsReq;
import com.yusion.shanghai.yusion4s.bean.upload.UploadFilesUrlReq;
import com.yusion.shanghai.yusion4s.bean.upload.UploadImgItemBean;
import com.yusion.shanghai.yusion4s.bean.upload.UploadLabelItemBean;
import com.yusion.shanghai.yusion4s.retrofit.api.UploadApi;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.utils.LoadingUtils;
import com.yusion.shanghai.yusion4s.utils.OssUtil;

import java.util.ArrayList;
import java.util.List;

public class UploadListActivity extends BaseActivity {

    private List<UploadImgItemBean> items;
    private UploadImgListAdapter adapter;
    private Intent mGetIntent;
    private int mIndex;//item对象在上级页面中list的索引
    private UploadLabelItemBean mTopItemBean;//上级页面传过来的bean
    private TextView errorTv;
    private LinearLayout errorLin;
    private boolean isVideoPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_list);
        mGetIntent = getIntent();
        mIndex = mGetIntent.getIntExtra("index", -1);
        mTopItemBean = (UploadLabelItemBean) mGetIntent.getSerializableExtra("item");
        initTitleBar(this, mGetIntent.getStringExtra("name")).setLeftClickListener(v -> onBack());
        RecyclerView rv = (RecyclerView) findViewById(R.id.upload_list_rv);
        rv.setLayoutManager(new GridLayoutManager(this, 3));
        errorTv = (TextView) findViewById(R.id.upload_list_error_tv);
        errorLin = (LinearLayout) findViewById(R.id.upload_list_error_lin);
        items = ((UploadLabelItemBean) mGetIntent.getSerializableExtra("item")).img_list;
        isVideoPage = mGetIntent.getStringExtra("name").contains("视频");
        adapter = new UploadImgListAdapter(this, items);
        adapter.setOnItemClick(new UploadImgListAdapter.OnItemClick() {
            @Override
            public void onItemClick(View v, UploadImgItemBean item) {
                if (isVideoPage) {
                    Intent it = new Intent(Intent.ACTION_VIEW);
                    Uri uri;
                    if (TextUtils.isEmpty(item.s_url)) {
                        uri = Uri.parse(item.local_path);
                    } else {
                        uri = Uri.parse(item.s_url);
                    }
                    it.setDataAndType(uri, "video/mp4");
                    startActivity(it);
                }
            }

            @Override
            public void onFooterClick(View v) {
                if (isVideoPage) {
                    Intent i = new Intent(UploadListActivity.this, PhotoMediaActivity.class);
                    i.putExtra("loadType", PhotoVideoDir.Type.VIDEO.toString());
                    startActivityForResult(i, 99);
                } else {
                    Intent i = new Intent(UploadListActivity.this, PhotoMediaActivity.class);
                    i.putExtra("loadType", PhotoVideoDir.Type.IMAGE.toString());
                    startActivityForResult(i, 100);
                }
            }
        });
        rv.setAdapter(adapter);
        initData();
    }

    private void initData() {
        if (!mTopItemBean.hasGetImgsFromServer) {
            ListImgsReq req = new ListImgsReq();
            req.role = "lender";
            req.label = mTopItemBean.value;
            req.app_id = mGetIntent.getStringExtra("app_id");
            UploadApi.listImgs(this, req, resp -> {
                if (resp.has_err) {
                    errorLin.setVisibility(View.VISIBLE);
                    errorTv.setText("您提交的资料有误：" + resp.error);
                    mTopItemBean.errorInfo = "您提交的资料有误：" + resp.error;
                } else {
                    errorLin.setVisibility(View.GONE);
                    mTopItemBean.errorInfo = "";
                }
                if (resp.list.size() != 0) {
                    mTopItemBean.hasGetImgsFromServer = true;
                    items.addAll(resp.list);
                    adapter.notifyDataSetChanged();
                }
            });
        } else if (mTopItemBean.hasError) {
            errorLin.setVisibility(View.VISIBLE);
            errorTv.setText(mTopItemBean.errorInfo);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> files = data.getStringArrayListExtra("files");
                for (String file : files) {
                    UploadImgItemBean item = new UploadImgItemBean();
                    item.local_path = file;
                    item.role = "lender";
                    item.type = mTopItemBean.value;
                    items.add(item);
                }
                adapter.notifyItemRangeInserted(adapter.getItemCount(), files.size());
                Dialog dialog = LoadingUtils.createLoadingDialog(this);
                dialog.show();
                int account = 0;
                for (String url : files) {
                    account++;
                    int finalAccount = account;
                    OssUtil.uploadOss(this, false, url, new OSSObjectKeyBean("lender", mTopItemBean.value, ".png"), new OnItemDataCallBack<String>() {
                        @Override
                        public void onItemDataCallBack(String objectKey) {
                            UploadFilesUrlReq.FileUrlBean fileUrlBean = new UploadFilesUrlReq.FileUrlBean();
                            fileUrlBean.file_id = objectKey;
                            fileUrlBean.label = mTopItemBean.value;
                            fileUrlBean.role = "lender";
                            UploadLabelListActivity.uploadFileUrlBeanList.add(fileUrlBean);
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
        } else if (requestCode == 99) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> files = data.getStringArrayListExtra("files");
                for (String file : files) {
                    UploadImgItemBean item = new UploadImgItemBean();
                    item.local_path = file;
                    item.role = "lender";
                    item.type = mTopItemBean.value;
                    items.add(item);
                }
                adapter.notifyItemRangeInserted(adapter.getItemCount(), files.size());
                Dialog dialog = LoadingUtils.createLoadingDialog(this);
                dialog.show();
                int account = 0;
                for (String url : files) {
                    account++;
                    int finalAccount = account;
                    OssUtil.uploadOss(this, false, url, new OSSObjectKeyBean("lender", mTopItemBean.value, ".mp4"), new OnItemDataCallBack<String>() {
                        @Override
                        public void onItemDataCallBack(String objectKey) {
                            UploadFilesUrlReq.FileUrlBean fileUrlBean = new UploadFilesUrlReq.FileUrlBean();
                            fileUrlBean.file_id = objectKey;
                            fileUrlBean.label = mTopItemBean.value;
                            fileUrlBean.role = "lender";
                            UploadLabelListActivity.uploadFileUrlBeanList.add(fileUrlBean);
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
        mGetIntent.putExtra("index", mIndex);
        setResult(RESULT_OK, mGetIntent);
        finish();
    }
}
