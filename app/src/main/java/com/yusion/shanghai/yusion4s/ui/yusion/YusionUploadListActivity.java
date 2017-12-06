package com.yusion.shanghai.yusion4s.ui.yusion;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pbq.pickerlib.activity.PhotoMediaActivity;
import com.pbq.pickerlib.entity.PhotoVideoDir;
import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.bean.oss.OSSObjectKeyBean;
import com.yusion.shanghai.yusion4s.bean.upload.DelImgsReq;
import com.yusion.shanghai.yusion4s.bean.upload.ListImgsReq;
import com.yusion.shanghai.yusion4s.bean.upload.UploadFilesUrlReq;
import com.yusion.shanghai.yusion4s.bean.upload.UploadImgItemBean;
import com.yusion.shanghai.yusion4s.glide.StatusImageRel;
import com.yusion.shanghai.yusion4s.retrofit.api.UploadApi;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnVoidCallBack;
import com.yusion.shanghai.yusion4s.ui.upload.ExtraPreviewActivity;
import com.yusion.shanghai.yusion4s.utils.GlideUtil;
import com.yusion.shanghai.yusion4s.utils.LoadingUtils;
import com.yusion.shanghai.yusion4s.utils.OssUtil;
import com.yusion.shanghai.yusion4s.utils.SharedPrefsUtil;
import com.yusion.shanghai.yusion4s.widget.TitleBar;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by ice on 2017/9/8.
 */
//一个从立刻申请页面进入 一个从影像件列表进入
public class YusionUploadListActivity extends BaseActivity {
    private RvAdapter adapter;
    private TextView errorTv;
    private LinearLayout errorLin;
    private boolean isEditing = false;
    private TextView mEditTv;
    private LinearLayout uploadBottomLin;
    private TextView uploadTv2;
    private TextView uploadTv1;
    private List<UploadImgItemBean> lists = new ArrayList<>();
    private List<UploadImgItemBean> hasUploadOssLists = new ArrayList<>();
    private String role;
    private String type;
    private String clt_id;
    private String title;
    private boolean needUploadFidToServer;
    private Intent mGetIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yusion_activity_upload_list);

        mGetIntent = getIntent();
        role = mGetIntent.getStringExtra("role");
        type = mGetIntent.getStringExtra("type");
        clt_id = mGetIntent.getStringExtra("clt_id");
        title = mGetIntent.getStringExtra("title");
        needUploadFidToServer = mGetIntent.getBooleanExtra("needUploadFidToServer", true);
        initView();
        initData();
    }

    private void initView() {
        TitleBar titleBar = initTitleBar(this, title).setLeftClickListener(v -> onBack());
        mEditTv = titleBar.getRightTextTv();
        mEditTv.setEnabled(false);
        mEditTv.setTextColor(Color.parseColor("#d1d1d1"));
        titleBar.setRightText("编辑").setRightClickListener(v -> {
            if (isEditing) {
                isEditing = false;
                mEditTv.setText("编辑");
                uploadBottomLin.setVisibility(View.GONE);
            } else {
                isEditing = true;
                mEditTv.setText("取消");
                uploadBottomLin.setVisibility(View.VISIBLE);

                uploadTv1.setText("全选");
                uploadTv2.setText("删除");
                uploadTv2.setTextColor(Color.parseColor("#d1d1d1"));
            }
            adapter.setIsEditing(isEditing);
        });

        uploadBottomLin = (LinearLayout) findViewById(R.id.upload_bottom_lin);
        uploadTv1 = (TextView) findViewById(R.id.upload_bottom_tv1);
        uploadTv2 = (TextView) findViewById(R.id.upload_bottom_tv2);
        uploadTv1.setOnClickListener(v -> {
            if (uploadTv1.getText().toString().equals("全选")) {
                for (UploadImgItemBean itemBean : lists) {
                    itemBean.hasChoose = true;
                }
                uploadTv1.setText("取消全选");
                uploadTv2.setText(String.format(Locale.CHINA, "删除(%d)", getCurrentChooseItemCount()));
                uploadTv2.setTextColor(Color.RED);
                adapter.notifyDataSetChanged();
            } else if (uploadTv1.getText().toString().equals("取消全选")) {
                for (UploadImgItemBean itemBean : lists) {
                    itemBean.hasChoose = false;
                }
                uploadTv1.setText("全选");
                uploadTv2.setText("删除");
                uploadTv2.setTextColor(Color.parseColor("#d1d1d1"));
                adapter.notifyDataSetChanged();
            }
        });
        uploadTv2.setOnClickListener(v -> {
            //要删除的图片的id集合
            List<String> delImgIdList = new ArrayList<>();

            //要删除的索引集合
            List<Integer> indexList = new ArrayList<>();
            for (int i = 0; i < lists.size(); i++) {
                UploadImgItemBean imgItemBean = lists.get(i);
                if (imgItemBean.hasChoose) indexList.add(i);
            }
            Collections.sort(indexList);

            if (needUploadFidToServer) {
                for (int i = 0; i < indexList.size(); i++) {
                    UploadImgItemBean imgItemBean = lists.get(indexList.get(i));
                    delImgIdList.add(imgItemBean.id);
                }

                DelImgsReq req = new DelImgsReq();
                req.clt_id = clt_id;
                req.id.addAll(delImgIdList);
                if (delImgIdList.size() > 0) {
                    UploadApi.delImgs(YusionUploadListActivity.this, req, (code, msg) -> {
                        if (code == 0) {

                            int offset = 0;
                            for (int i = 0; i < indexList.size(); i++) {
                                int delIndex = indexList.get(i) - offset;
                                lists.remove(delIndex);
                                offset++;
                            }

                            Toast.makeText(myApp, "删除成功", Toast.LENGTH_SHORT).show();
                            onImgCountChange(lists.size() > 0);
                        }
                    });
                }


            } else {
                //每删除一个对象就该偏移+1
                int offset = 0;
                for (int i = 0; i < indexList.size(); i++) {
                    int delIndex = indexList.get(i) - offset;
                    lists.remove(delIndex);
                    offset++;
                }

                onImgCountChange(lists.size() > 0);
            }
        });

        errorTv = (TextView) findViewById(R.id.upload_list_error_tv);
        errorLin = (LinearLayout) findViewById(R.id.upload_list_error_lin);

        RecyclerView rv = (RecyclerView) findViewById(R.id.upload_list_rv);
        rv.setLayoutManager(new GridLayoutManager(this, 3));
        if (!needUploadFidToServer) {
            lists = ((ArrayList<UploadImgItemBean>) mGetIntent.getSerializableExtra("imgList"));
        }
        adapter = new RvAdapter(this, lists);
        rv.setAdapter(adapter);
        adapter.setOnItemClick(new RvAdapter.OnItemClick() {
            //不是编辑状态的话点击后进行预览,否则是编辑操作
            @Override
            public void onItemClick(View v, UploadImgItemBean item, int index) {
                if (isEditing) {
                    item.hasChoose = !item.hasChoose;
                    adapter.notifyDataSetChanged();
                    if (getCurrentChooseItemCount() != 0) {
                        uploadTv2.setText(String.format(Locale.CHINA, "删除(%d)", getCurrentChooseItemCount()));
                        uploadTv2.setTextColor(Color.RED);
                    } else {
                        uploadTv2.setText("删除");
                        uploadTv2.setTextColor(Color.parseColor("#d1d1d1"));
                    }
                } else {
                    String imgUrl;
                    if (!TextUtils.isEmpty(item.local_path)) {
                        imgUrl = item.local_path;
                    } else {
                        imgUrl = item.raw_url;
                    }
                    previewImg(findViewById(R.id.preview_anchor), imgUrl);
                }
            }

            @Override
            public void onFooterClick(View v) {
                Intent i = new Intent(YusionUploadListActivity.this, PhotoMediaActivity.class);
                i.putExtra("loadType", PhotoVideoDir.Type.IMAGE.toString());
                startActivityForResult(i, 100);
            }
        });
    }

    private void previewImg(View previewAnchor, String imgUrl) {
        Intent intent = new Intent(this, ExtraPreviewActivity.class);
        intent.putExtra("PreviewImg", imgUrl);
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, previewAnchor, "shareNames");
        ActivityCompat.startActivity(this, intent, compat.toBundle());
    }

    private void initData() {
        if (needUploadFidToServer) {
            ListImgsReq req = new ListImgsReq();
            req.label = type;
            req.clt_id = clt_id;
            UploadApi.listImgs(this, req, resp -> {
                if (resp.has_err) {
                    errorLin.setVisibility(View.VISIBLE);
                    errorTv.setText("您提交的资料有误：" + resp.error);
                } else {
                    errorLin.setVisibility(View.GONE);
                }

                onImgCountChange(resp.list.size() > 0);

                if (resp.list.size() != 0) {
                    lists.addAll(resp.list);
                    adapter.notifyDataSetChanged();
                }
            });
        } else {
            onImgCountChange(lists.size() > 0);
        }
    }

    private int getCurrentChooseItemCount() {
        int totalCount = 0;
        for (UploadImgItemBean itemBean : lists) {
            if (itemBean.hasChoose) {
                totalCount++;
            }
        }
        return totalCount;
    }

    //图片数量每次改变都需要清除编辑状态
    private void onImgCountChange(boolean hasImg) {
        if (hasImg) {
            mEditTv.setEnabled(true);
            mEditTv.setTextColor(Color.parseColor("#ffffff"));
        } else {
            mEditTv.setEnabled(false);
            mEditTv.setTextColor(Color.parseColor("#d1d1d1"));
        }
        isEditing = false;
        adapter.setIsEditing(false);
        mEditTv.setText("编辑");
        uploadBottomLin.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            ArrayList<String> files = data.getStringArrayListExtra("files");

            List<UploadImgItemBean> toAddList = new ArrayList<>();
            for (String file : files) {
                UploadImgItemBean item = new UploadImgItemBean();
                item.local_path = file;
                item.role = role;
                item.type = type;
                toAddList.add(item);
            }

            hasUploadOssLists.clear();

            Dialog dialog = LoadingUtils.createLoadingDialog(this);
            dialog.show();
            new Thread(() -> {
                for (UploadImgItemBean uploadImgItemBean : toAddList) {
                    OssUtil.synchronizationUploadOss(YusionUploadListActivity.this, uploadImgItemBean.local_path, new OSSObjectKeyBean(role, type, ".png"), objectKey -> {
                        hasUploadOssLists.add(uploadImgItemBean);
                        uploadImgItemBean.objectKey = objectKey;
                    }, data1 -> {
                        hasUploadOssLists.add(uploadImgItemBean);
                    });
                    onUploadOssFinish(hasUploadOssLists.size(), files, dialog, toAddList);
                }
            }).start();
        }
    }

    private void onUploadOssFinish(int finalAccount, ArrayList<String> files, Dialog dialog, final List<UploadImgItemBean> toAddList) {
        if (finalAccount == files.size()) {
            dialog.dismiss();
            runOnUiThread(() -> {
                calculateRelToAddList(toAddList, relToAddList -> {
                    if (needUploadFidToServer) {
                        uploadImgs(clt_id, relToAddList, () -> {
                            lists.addAll(relToAddList);
                            onImgCountChange(lists.size() > 0);
                        });
                    } else {
                        lists.addAll(relToAddList);
                        onImgCountChange(lists.size() > 0);
                    }
                });
            });
        }
    }

    private void calculateRelToAddList(List<UploadImgItemBean> toAddList, OnItemDataCallBack<List<UploadImgItemBean>> onRelToAddListCallBack) {
        List<UploadImgItemBean> relToAddList = new ArrayList<>();
        for (UploadImgItemBean itemBean : toAddList) {
            if (!TextUtils.isEmpty(itemBean.objectKey)) {
                relToAddList.add(itemBean);
            }
        }
        onRelToAddListCallBack.onItemDataCallBack(relToAddList);
    }

    private Dialog mUploadFileDialog;

    public void uploadImgs(String clt_id, List<UploadImgItemBean> lists, OnVoidCallBack onSuccessCallBack) {
        List<UploadFilesUrlReq.FileUrlBean> uploadFileUrlBeanList = new ArrayList<>();
        for (UploadImgItemBean imgItemBean : lists) {
            UploadFilesUrlReq.FileUrlBean fileUrlBean = new UploadFilesUrlReq.FileUrlBean();
            fileUrlBean.clt_id = clt_id;
            fileUrlBean.file_id = imgItemBean.objectKey;
            fileUrlBean.label = imgItemBean.type;
            uploadFileUrlBeanList.add(fileUrlBean);
        }

        if (isFinishing()) {
            return;
        }
        if (mUploadFileDialog == null) {
            mUploadFileDialog = LoadingUtils.createLoadingDialog(this);
            mUploadFileDialog.setCancelable(false);
        }
        mUploadFileDialog.show();
        UploadFilesUrlReq uploadFilesUrlReq = new UploadFilesUrlReq();
        uploadFilesUrlReq.files = uploadFileUrlBeanList;
        uploadFilesUrlReq.region = SharedPrefsUtil.getInstance(this).getValue("region", "");
        uploadFilesUrlReq.bucket = SharedPrefsUtil.getInstance(this).getValue("bucket", "");
        UploadApi.uploadFileUrl(this, uploadFilesUrlReq, new OnItemDataCallBack<List<String>>() {
            @Override
            public void onItemDataCallBack(List<String> data) {
                for (int i = 0; i < lists.size(); i++) {
                    lists.get(i).id = data.get(i);
                }
                mUploadFileDialog.dismiss();
                onSuccessCallBack.callBack();
                Toast.makeText(YusionUploadListActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    private void onBack() {
        if (!needUploadFidToServer) {
            setResult(RESULT_OK, mGetIntent);
        }
        finish();
    }

    public static class RvAdapter extends RecyclerView.Adapter<RvAdapter.VH> {

        private LayoutInflater mLayoutInflater;
        private List<UploadImgItemBean> mItems;
        private Context mContext;
        private OnItemClick mOnItemClick;
        public static final int TYPE_ADD_IMG = 100;
        public static final int TYPE_IMG = 101;
        private boolean isEditing = false;

        public RvAdapter(Context context, List<UploadImgItemBean> items) {
            mItems = items;
            mContext = context;
            mLayoutInflater = LayoutInflater.from(mContext);
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;
            if (viewType == TYPE_ADD_IMG) {
                view = mLayoutInflater.inflate(R.layout.yusion_upload_list_add_img_item, parent, false);
            } else if (viewType == TYPE_IMG) {
                view = mLayoutInflater.inflate(R.layout.yusion_upload_list_img_item, parent, false);
            }
            return new VH(view);
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
            if (position == mItems.size()) {
                holder.itemView.setOnClickListener(v -> mOnItemClick.onFooterClick(v));
            } else {
                UploadImgItemBean item = mItems.get(position);
                StatusImageRel statusImageRel = (StatusImageRel) holder.itemView;
                if (!TextUtils.isEmpty(item.local_path)) {
                    GlideUtil.loadLocalImg(mContext, statusImageRel, new File(item.local_path));
                } else {
                    GlideUtil.loadImg(mContext, statusImageRel, item.s_url);
                }
                holder.itemView.setOnClickListener(mOnItemClick == null ? null : (View.OnClickListener) v -> mOnItemClick.onItemClick(v, item, position));
                if (isEditing) {
                    statusImageRel.cbImg.setVisibility(View.VISIBLE);
                    if (item.hasChoose) {
                        statusImageRel.cbImg.setImageResource(R.mipmap.surechoose_icon);
                    } else {
                        statusImageRel.cbImg.setImageResource(R.mipmap.choose_icon);
                    }
                } else {
                    statusImageRel.cbImg.setVisibility(View.GONE);
                }
            }
        }

        public void setIsEditing(boolean isEditing) {
            if (isEditing) {
                for (UploadImgItemBean item : mItems) {
                    item.hasChoose = false;
                }
            }
            this.isEditing = isEditing;
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            return position == mItems.size() ? TYPE_ADD_IMG : TYPE_IMG;
        }

        //size+1是因为有 添加图片的item
        @Override
        public int getItemCount() {
            return mItems == null ? 0 : mItems.size() + 1;
        }

        protected class VH extends RecyclerView.ViewHolder {
//            public ImageView img;
//            public ImageView cbImg;

            public VH(View itemView) {
                super(itemView);
//                img = ((ImageView) itemView.findViewById(R.id.upload_list_img_item_img));
//                cbImg = ((ImageView) itemView.findViewById(R.id.upload_list_img_item_cb_img));
            }
        }

        public interface OnItemClick {
            void onItemClick(View v, UploadImgItemBean item, int index);

            void onFooterClick(View v);
        }

        public void setOnItemClick(OnItemClick mOnItemClick) {
            this.mOnItemClick = mOnItemClick;
        }
    }
}
