package com.yusion.shanghai.yusion4s.ui.upload;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pbq.pickerlib.activity.PhotoMediaActivity;
import com.pbq.pickerlib.entity.PhotoVideoDir;
import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.bean.oss.OSSObjectKeyBean;
import com.yusion.shanghai.yusion4s.bean.upload.DelImgsReq;
import com.yusion.shanghai.yusion4s.bean.upload.GetTemplateResp;
import com.yusion.shanghai.yusion4s.bean.upload.ListDealerLabelsResp;
import com.yusion.shanghai.yusion4s.bean.upload.ListImgsReq;
import com.yusion.shanghai.yusion4s.bean.upload.UploadFilesUrlReq;
import com.yusion.shanghai.yusion4s.bean.upload.UploadImgItemBean;
import com.yusion.shanghai.yusion4s.glide.StatusImageRel;
import com.yusion.shanghai.yusion4s.retrofit.api.UploadApi;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnCodeAndMsgCallBack;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnVoidCallBack;
import com.yusion.shanghai.yusion4s.settings.Constants;
import com.yusion.shanghai.yusion4s.utils.DensityUtil;
import com.yusion.shanghai.yusion4s.utils.GlideUtil;
import com.yusion.shanghai.yusion4s.utils.LoadingUtils;
import com.yusion.shanghai.yusion4s.utils.OssUtil;
import com.yusion.shanghai.yusion4s.utils.SharedPrefsUtil;
import com.yusion.shanghai.yusion4s.widget.TitleBar;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UploadListActivity extends BaseActivity {
    private ListDealerLabelsResp.LabelListBean topItem;
    private TextView errorTv;
    private LinearLayout errorLin;
    private RvAdapter adapter;
    private List<UploadImgItemBean> lists = new ArrayList<>();
    private List<UploadImgItemBean> hasUploadOSSLists = new ArrayList<>();

    private String app_id;
    private String clt_id;
    private TextView mEditTv;
    private LinearLayout uploadBottomLin;
    private TextView uploadTv1;
    private TextView uploadTv2;
    private boolean isEditing = false;
    private ImageView expandImg;


    private boolean isTemplateExpand = true;
    private LinearLayout templateLin;
    private LinearLayout templateTitleLin;
    private TextView templateTitle;
    private TextView templateSl;
    private TextView templateContent;
    private Button templateImgLook;
    private ImageView templateImg;
    private String detail_url;
    private String sample_url;
    private View anchor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_list);
        topItem = (ListDealerLabelsResp.LabelListBean) getIntent().getSerializableExtra("topItem");
        app_id = getIntent().getStringExtra("app_id");
        clt_id = getIntent().getStringExtra("clt_id");
        initView();
        initData();
    }

    private void initView() {
        anchor = findViewById(R.id.preview_anchor);
        TitleBar titleBar = initTitleBar(this, topItem.name).setLeftClickListener(v -> onBack());
        mEditTv = titleBar.getRightTextTv();
        mEditTv.setEnabled(false);
        mEditTv.setTextColor(Color.parseColor("#d1d1d1"));
        titleBar.setRightText("编辑").setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditing) {
                    isEditing = false;
                    mEditTv.setText("编辑");
                    uploadBottomLin.setVisibility(View.GONE);
                } else {
                    if (isTemplateExpand) {
                        foldTemplate();
                    }

                    isEditing = true;
                    mEditTv.setText("取消");
                    uploadBottomLin.setVisibility(View.VISIBLE);

                    uploadTv1.setText("全选");
                    uploadTv2.setText("删除");
                    uploadTv2.setTextColor(Color.parseColor("#d1d1d1"));
                }
                adapter.setIsEditing(isEditing);
            }
        });
        uploadBottomLin = (LinearLayout) findViewById(R.id.upload_bottom_lin);
        uploadTv1 = (TextView) findViewById(R.id.upload_bottom_tv1);
        uploadTv2 = (TextView) findViewById(R.id.upload_bottom_tv2);
        uploadTv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploadTv1.getText().toString().equals("全选")) {
                    for (UploadImgItemBean itemBean : lists) {
                        itemBean.hasChoose = true;
                    }
                    uploadTv1.setText("取消全选");
                    uploadTv2.setText(String.format("删除(%d)", getCurrentChooseItemCount()));
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
            }
        });
        uploadTv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //要删除的图片的id集合
                List<String> delImgIdList = new ArrayList<>();

                //要删除的索引集合
                List<Integer> indexList = new ArrayList<>();
                for (int i = 0; i < lists.size(); i++) {
                    if (lists.get(i).hasChoose) indexList.add(i);
                }
                Collections.sort(indexList);

                //没删除一个对象就该偏移+1
                int offset = 0;
                for (int i = 0; i < indexList.size(); i++) {
                    int delIndex = indexList.get(i) - offset;
                    delImgIdList.add(lists.get(delIndex).id);
                    lists.remove(delIndex);
                    offset++;
                }

                uploadTv2.setText("删除");
                uploadTv2.setTextColor(Color.parseColor("#d1d1d1"));
                adapter.notifyDataSetChanged();

                DelImgsReq req = new DelImgsReq();
                req.clt_id = clt_id;
                req.app_id = app_id;
                req.id.addAll(delImgIdList);
                if (delImgIdList.size() > 0) {
                    UploadApi.delImgs(UploadListActivity.this, req, new OnCodeAndMsgCallBack() {
                        @Override
                        public void callBack(int code, String msg) {
                            if (code == 0) {
                                Toast.makeText(myApp, "删除成功", Toast.LENGTH_SHORT).show();
                                onImgCountChange(lists.size() > 0);
                            }
                        }
                    });
                }
            }
        });

        errorTv = (TextView) findViewById(R.id.upload_list_error_tv);
        errorLin = (LinearLayout) findViewById(R.id.upload_list_error_lin);

        RecyclerView rv = (RecyclerView) findViewById(R.id.upload_list_rv);
        rv.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new RvAdapter(this, lists, topItem.name.contains("视频"));
        rv.setAdapter(adapter);
        adapter.setOnItemClick(new RvAdapter.OnItemClick() {
            @Override
            public void onItemClick(View v, UploadImgItemBean item, int index) {
                if (isEditing) {
                    if (item.hasChoose) {
                        item.hasChoose = false;
                    } else {
                        item.hasChoose = true;
                    }
                    adapter.notifyDataSetChanged();
                    if (getCurrentChooseItemCount() != 0) {
                        uploadTv2.setText(String.format("删除(%d)", getCurrentChooseItemCount()));
                        uploadTv2.setTextColor(Color.RED);
                    } else {
                        uploadTv2.setText("删除");
                        uploadTv2.setTextColor(Color.parseColor("#d1d1d1"));
                    }
                } else {
                    if (isVideoPage) {
                        playVideo(item);
                    } else {
                        String imgUrl;
                        if (!TextUtils.isEmpty(item.local_path)) {
                            imgUrl = item.local_path;
                        } else {
                            imgUrl = item.raw_url;
                        }
                        previewImg(anchor, imgUrl);
                    }
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


        expandImg = (ImageView) findViewById(R.id.upload_list_expand_img);
        templateImg = (ImageView) findViewById(R.id.upload_list_template_img);
        templateImgLook = (Button) findViewById(R.id.upload_list_template_img_look);
        templateLin = (LinearLayout) findViewById(R.id.upload_list_template);
        templateTitle = (TextView) findViewById(R.id.upload_list_template_title);
        templateSl = (TextView) findViewById(R.id.upload_list_template_sl);
        templateContent = (TextView) findViewById(R.id.upload_list_template_content);
        templateTitleLin = (LinearLayout) findViewById(R.id.upload_list_template_title_lin);
        templateTitle.setText(topItem.name + "要求");
        templateSl.setText(topItem.name + "示例");
        templateTitleLin.setOnClickListener(v -> {
            if (isTemplateExpand) {
                foldTemplate();
            } else {
                expandTemplate();
            }
        });
        templateLin.setOnClickListener(v -> {
        });
        templateImg.setOnClickListener(img ->
        {
            if (isVideoPage) {
                playVideo(detail_url);
            } else {
                previewImg(anchor, detail_url, true);
            }
        });
        templateImgLook.setOnClickListener(v -> {
            if (isVideoPage) {
                playVideo(detail_url);
            } else {
                previewImg(anchor, detail_url, true);
            }
        });
    }

    private void playVideo(UploadImgItemBean item) {
//        Intent it = new Intent(Intent.ACTION_VIEW);
//        Uri uri;
//        if (TextUtils.isEmpty(item.s_url)) {
//            uri = Uri.parse(item.local_path);
//        } else {
//            uri = Uri.parse(item.raw_url);
//        }
//        Log.e("TAG", "onItemClick: " + uri.toString());
//        it.setDataAndType(uri, "video/mp4");
//        startActivity(it);
        String url;
        if (TextUtils.isEmpty(item.s_url)) {
            url = item.local_path;
        } else {
            url = item.raw_url;
        }
        playVideo(url);
    }

    private void playVideo(String url) {
        Intent it = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(url);
        it.setDataAndType(uri, "video/mp4");
        startActivity(it);
    }

    private void expandTemplate() {
        ViewCompat.animate(templateLin).translationY(0).setDuration(200).setListener(new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationStart(View view) {
                expandImg.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(View view) {
                isTemplateExpand = true;
                expandImg.setImageResource(R.mipmap.arrow_down);
                expandImg.setEnabled(true);
            }

            @Override
            public void onAnimationCancel(View view) {
                expandImg.setEnabled(true);
            }
        }).start();
    }

    private void foldTemplate() {
        ViewCompat.animate(templateLin).translationY(DensityUtil.dip2px(this, 350)).setDuration(200).setListener(new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationStart(View view) {
                expandImg.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(View view) {
                isTemplateExpand = false;
                expandImg.setImageResource(R.mipmap.arrow_up);
                expandImg.setEnabled(true);
            }

            @Override
            public void onAnimationCancel(View view) {
                expandImg.setEnabled(true);
            }
        }).start();

    }

    private boolean isVideoPage;

    private void initData() {
        isVideoPage = topItem.name.contains("视频");
        ListImgsReq req = new ListImgsReq();
        req.label = topItem.value;
        req.app_id = app_id;
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


        String id = topItem.id;
        Log.e("TAG", "initData: " + id);
        UploadApi.getTemplate(this, id, new OnItemDataCallBack<GetTemplateResp>() {

            @Override
            public void onItemDataCallBack(GetTemplateResp data) {
                if (data != null) {
                    templateLin.setVisibility(View.VISIBLE);
                    templateContent.setText(Html.fromHtml(data.checker_item.description));
                    sample_url = data.checker_item.sample_url;
                    detail_url = data.checker_item.detail_url;
                    if (!isFinishing()) {
                        Glide.with(UploadListActivity.this).load(sample_url).into(templateImg);
                    }
                }
            }
        });
    }

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

    private int getCurrentChooseItemCount() {
        int totalCount = 0;
        for (UploadImgItemBean itemBean : lists) {
            if (itemBean.hasChoose) {
                totalCount++;
            }
        }
        return totalCount;
    }

    private void previewImg(View previewAnchor, String imgUrl, boolean isBreviary) {
        if (TextUtils.isEmpty(imgUrl)) {
            Toast.makeText(myApp, "没有找到图片", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, PreviewActivity.class);
        intent.putExtra("PreviewImg", imgUrl);
        intent.putExtra("breviary", isBreviary);
        if (isBreviary) {
            startActivity(intent);
        } else {
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, previewAnchor, "shareNames");
            ActivityCompat.startActivity(this, intent, compat.toBundle());
        }
    }

    private void previewImg(View previewAnchor, String imgUrl) {
        previewImg(previewAnchor, imgUrl, false);
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
                item.role = Constants.PersonType.LENDER;
                item.type = topItem.value;
                toAddList.add(item);
            }

            hasUploadOSSLists.clear();

            Dialog dialog = LoadingUtils.createLoadingDialog(this);
            dialog.show();
            new Thread(() -> {
                for (UploadImgItemBean uploadImgItemBean : toAddList) {
                    String suffix = isVideoPage ? ".mp4" : ".png";
                    OssUtil.synchronizationUploadOss(UploadListActivity.this, uploadImgItemBean.local_path, new OSSObjectKeyBean(Constants.PersonType.LENDER, topItem.value, suffix), objectKey -> {
                        hasUploadOSSLists.add(uploadImgItemBean);
                        uploadImgItemBean.objectKey = objectKey;
                    }, throwable -> {
                        hasUploadOSSLists.add(uploadImgItemBean);
                        onUploadOssFinish(hasUploadOSSLists.size(), files, dialog, toAddList);
                    });
                    onUploadOssFinish(hasUploadOSSLists.size(), files, dialog, toAddList);
                }
            }).start();
        }
    }

    private void onUploadOssFinish(int finalAccount, ArrayList<String> files, Dialog dialog, final List<UploadImgItemBean> toAddList) {
        Log.e("TAG", "finalAccount: " + finalAccount);
        Log.e("TAG", "files.size(): " + files.size());
        if (finalAccount == files.size()) {
            dialog.dismiss();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    calculateRelToAddList(toAddList, new OnItemDataCallBack<List<UploadImgItemBean>>() {
                        @Override
                        public void onItemDataCallBack(List<UploadImgItemBean> relToAddList) {
                            uploadImgs(app_id, clt_id, relToAddList, new OnVoidCallBack() {
                                @Override
                                public void callBack() {
                                    lists.addAll(relToAddList);
                                    adapter.notifyItemRangeInserted(adapter.getItemCount(), relToAddList.size());
                                    onImgCountChange(lists.size() > 0);
                                }
                            });
                        }
                    });
                }
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

    public void uploadImgs(String app_id, String clt_id, List<UploadImgItemBean> lists, OnVoidCallBack onSuccessCallBack) {
        List<UploadFilesUrlReq.FileUrlBean> uploadFileUrlBeanList = new ArrayList<>();
        for (UploadImgItemBean imgItemBean : lists) {
            UploadFilesUrlReq.FileUrlBean fileUrlBean = new UploadFilesUrlReq.FileUrlBean();
            fileUrlBean.app_id = app_id;
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
                Toast.makeText(UploadListActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    private void onBack() {
        topItem.has_img = lists.size();
        setResult(RESULT_OK, getIntent());
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
        private boolean isVideoPage;

        public RvAdapter(Context context, List<UploadImgItemBean> items, boolean isVideoPage) {
            mItems = items;
            mContext = context;
            mLayoutInflater = LayoutInflater.from(mContext);
            this.isVideoPage = isVideoPage;
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;
            if (viewType == TYPE_ADD_IMG) {
                view = mLayoutInflater.inflate(R.layout.upload_list_add_img_item, parent, false);
            } else if (viewType == TYPE_IMG) {
//                view = new StatusImageRel(mContext);
                view = mLayoutInflater.inflate(R.layout.upload_list_img_item, parent, false);
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
                    if (isVideoPage) {
                        GlideUtil.loadImg(mContext, statusImageRel, item.raw_url);
                    } else {
                        GlideUtil.loadImg(mContext, statusImageRel, item.s_url);
                    }
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
            public VH(View itemView) {
                super(itemView);
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

