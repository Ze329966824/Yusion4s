package com.yusion.shanghai.yusion4s.ui.upload;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
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
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnVoidCallBack;
import com.yusion.shanghai.yusion4s.settings.Constants;
import com.yusion.shanghai.yusion4s.utils.DensityUtil;
import com.yusion.shanghai.yusion4s.utils.GlideUtil;
import com.yusion.shanghai.yusion4s.utils.LoadingUtils;
import com.yusion.shanghai.yusion4s.utils.OssUtil;
import com.yusion.shanghai.yusion4s.utils.PreviewImgUtil;
import com.yusion.shanghai.yusion4s.utils.SharedPrefsUtil;
import com.yusion.shanghai.yusion4s.utils.URLEncoder;
import com.yusion.shanghai.yusion4s.widget.TitleBar;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * 1.支持从证信信息页面点击授权书进入
 */
public class ExtraUploadListActivity extends BaseActivity {

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
    private TextView templateContent;
    private ImageView templateImg;
    private ArrayList<String> url_list;
    private TextView imgsSizeTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_list);

        topItem = (ListDealerLabelsResp.LabelListBean) getIntent().getSerializableExtra("topItem");
        app_id = getIntent().getStringExtra("app_id");
        clt_id = getIntent().getStringExtra("clt_id");

        // TODO: 2017/12/1 需要字段
        isVideoPage = topItem.name.contains("视频");

        initView();
        initData();
    }

    private void initView() {
        TitleBar titleBar = initTitleBar(this, topItem.name).setLeftClickListener(v -> onBack());
        mEditTv = titleBar.getRightTextTv();
        mEditTv.setEnabled(false);
        mEditTv.setTextColor(Color.parseColor("#d1d1d1"));
        titleBar.setRightText("编辑").setRightClickListener(v -> {

            if (isEditing) {
                //编辑状态点取消
                isEditing = false;
                mEditTv.setText("编辑");
                uploadBottomLin.setVisibility(View.GONE);
            } else {
                //未编辑状态点编辑
                //先折叠模板
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

            //通知adapter编辑状态改变 请求重绘
            adapter.setIsEditing(isEditing);

        });

        uploadBottomLin = findViewById(R.id.upload_bottom_lin);
        uploadTv1 = findViewById(R.id.upload_bottom_tv1);//全选
        uploadTv2 = findViewById(R.id.upload_bottom_tv2);//删除
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
        uploadTv2.setOnClickListener(v -> delImgs());

        errorTv = findViewById(R.id.upload_list_error_tv);
        errorLin = findViewById(R.id.upload_list_error_lin);

        RecyclerView rv = findViewById(R.id.upload_list_rv);
        rv.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new RvAdapter(this, lists, topItem.name.contains("视频"));
        rv.setAdapter(adapter);
        adapter.setOnItemClick(new RvAdapter.OnItemClick() {
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
                    if (isVideoPage) {
                        String url;
                        if (TextUtils.isEmpty(item.s_url)) {
                            url = item.local_path;
                        } else {
                            url = item.raw_url;
                        }
                        playVideo(url);
                    } else {
                        String imgUrl;
                        if (!TextUtils.isEmpty(item.local_path)) {
                            imgUrl = item.local_path;
                        } else {
                            imgUrl = item.raw_url;
                        }
                        ArrayList<String> showImgUrls = new ArrayList<>();
                        showImgUrls.add(imgUrl);
                        PreviewImgUtil.showImg(ExtraUploadListActivity.this, showImgUrls);
                        overridePendingTransition(R.anim.center_zoom_in, R.anim.stay);
                    }
                }
            }

            @Override
            public void onFooterClick(View v) {
                if (isVideoPage) {
                    Intent i = new Intent(ExtraUploadListActivity.this, PhotoMediaActivity.class);
                    i.putExtra("loadType", PhotoVideoDir.Type.VIDEO.toString());
                    startActivityForResult(i, 99);
                } else {
                    Intent i = new Intent(ExtraUploadListActivity.this, PhotoMediaActivity.class);
                    i.putExtra("loadType", PhotoVideoDir.Type.IMAGE.toString());
                    startActivityForResult(i, 100);
                }
            }
        });


        //------------以下是模板相关------------
        expandImg = findViewById(R.id.upload_list_expand_img);
        templateImg = findViewById(R.id.upload_list_template_img);
        Button templateImgLook = findViewById(R.id.upload_list_template_img_look);
        ImageView templateVideoLook = findViewById(R.id.upload_list_template_video_look);
        imgsSizeTv = findViewById(R.id.upload_list_template_imgs_size);
        if (isVideoPage) {
            templateVideoLook.setVisibility(View.VISIBLE);
            templateImgLook.setVisibility(View.GONE);
        } else {
            templateImgLook.setVisibility(View.VISIBLE);
            templateVideoLook.setVisibility(View.GONE);
        }
        templateLin = findViewById(R.id.upload_list_template);
        TextView templateTitle = findViewById(R.id.upload_list_template_title);
        templateContent = findViewById(R.id.upload_list_template_content);
        LinearLayout templateTitleLin = findViewById(R.id.upload_list_template_title_lin);
        templateTitle.setText(topItem.name + "要求");
        templateTitleLin.setOnClickListener(v -> {
            if (isTemplateExpand) {
                foldTemplate();
            } else {
                expandTemplate();
            }
        });
        findViewById(R.id.upload_list_template_img_group).setOnClickListener(v -> {
                    if (isVideoPage) {
                        playVideo(url_list.get(0));
                    } else {
                        previewTemplateImgs();
                    }
                }
        );
    }

    private void delImgs() {
        //要删除的图片的id集合
        List<String> delImgIdList = new ArrayList<>();

        //要删除的索引集合
        List<Integer> indexList = new ArrayList<>();
        for (int i = 0; i < lists.size(); i++) {
            if (lists.get(i).hasChoose) indexList.add(i);
        }
        Collections.sort(indexList);

        if (indexList.size() == 0) {
            //没有选中图片就不予点击删除按键
            return;
        }

        ArrayList<UploadImgItemBean> tempList = new ArrayList<>();
        tempList.addAll(lists);
        //每删除一个对象就该偏移+1
        int offset = 0;
        for (int i = 0; i < indexList.size(); i++) {
            int delIndex = indexList.get(i) - offset;
            delImgIdList.add(tempList.get(delIndex).id);
            tempList.remove(delIndex);
            offset++;
        }

        DelImgsReq req = new DelImgsReq();
        req.clt_id = clt_id;
        req.app_id = app_id;
        req.id.addAll(delImgIdList);
        if (delImgIdList.size() > 0) {
            UploadApi.delImgs(ExtraUploadListActivity.this, req, (code, msg) -> {
                if (code == 0) {
                    Toast.makeText(myApp, "删除成功", Toast.LENGTH_SHORT).show();

                    uploadTv2.setText("删除");
                    uploadTv2.setTextColor(Color.parseColor("#d1d1d1"));

                    lists.clear();
                    lists.addAll(tempList);

                    onImgCountChange(lists.size() > 0);
                }
            });
        }
    }

    private void previewTemplateImgs() {
        if (url_list == null || url_list.size() == 0) {
            Toast.makeText(myApp, "该文件暂无模板！！！", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<String> showImgUrls = new ArrayList<>();
        for (String url : url_list) {
            showImgUrls.add(URLEncoder.encode(url));
        }
        PreviewImgUtil.showImg(this, showImgUrls);
        overridePendingTransition(R.anim.center_zoom_in, R.anim.stay);
    }

    private void playVideo(String url) {
        if (TextUtils.isEmpty(url)) {
            Toast.makeText(myApp, "未找到可观看的视频", Toast.LENGTH_SHORT).show();
            return;
        }
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
        ListImgsReq req = new ListImgsReq();
        req.label = topItem.value;
        req.app_id = app_id;
        req.clt_id = clt_id;
        UploadApi.listImgs(this, req, resp -> {
            if (resp == null) {
                return;
            }
            if (resp.has_err) {
                errorLin.setVisibility(View.VISIBLE);
                errorTv.setText("您提交的资料有误：" + resp.error);
            } else {
                errorLin.setVisibility(View.GONE);
            }

            if (resp.list.size() != 0) {
                lists.addAll(resp.list);
                onImgCountChange(resp.list.size() > 0);
            }
        });

        UploadApi.getTemplate(this, topItem.id, data -> showTemplate(data));
    }

    private void showTemplate(GetTemplateResp data) {
        if (data != null) {
            if (data.checker_item_ == null || data.checker_item_.url_list == null || data.checker_item_.url_list.size() == 0) {
                Toast.makeText(myApp, "该文件暂无模板.", Toast.LENGTH_SHORT).show();
                return;
            }
            templateLin.setVisibility(View.VISIBLE);
            templateContent.setText(Html.fromHtml(data.checker_item_.description));
            url_list = data.checker_item_.url_list;
            imgsSizeTv.setText(data.checker_item_.url_list.size() + "");

            if (!isFinishing()) {
                Glide.with(ExtraUploadListActivity.this).load(url_list.get(0)).into(templateImg);
            }
        } else {
            Toast.makeText(myApp, "模板模块初始化失败", Toast.LENGTH_SHORT).show();
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
                    OssUtil.synchronizationUploadOss(ExtraUploadListActivity.this, uploadImgItemBean.local_path, new OSSObjectKeyBean(Constants.PersonType.LENDER, topItem.value, suffix), objectKey -> {
                        hasUploadOSSLists.add(uploadImgItemBean);
                        uploadImgItemBean.objectKey = objectKey;
                    }, throwable -> {
                        hasUploadOSSLists.add(uploadImgItemBean);
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
                Toast.makeText(ExtraUploadListActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
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

