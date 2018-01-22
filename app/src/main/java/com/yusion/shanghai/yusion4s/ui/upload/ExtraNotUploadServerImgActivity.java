package com.yusion.shanghai.yusion4s.ui.upload;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
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
import com.yusion.shanghai.yusion4s.bean.upload.UploadFilesUrlReq;
import com.yusion.shanghai.yusion4s.bean.upload.UploadImgItemBean;
import com.yusion.shanghai.yusion4s.glide.StatusImageRel;
import com.yusion.shanghai.yusion4s.retrofit.Api;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.utils.ApiUtil;
import com.yusion.shanghai.yusion4s.utils.DensityUtil;
import com.yusion.shanghai.yusion4s.utils.ExtraURLEncoder;
import com.yusion.shanghai.yusion4s.utils.GlideUtil;
import com.yusion.shanghai.yusion4s.utils.LoadingUtils;
import com.yusion.shanghai.yusion4s.utils.OssUtil;
import com.yusion.shanghai.yusion4s.utils.PreviewImgUtil;
import com.yusion.shanghai.yusion4s.widget.TitleBar;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

//如果删除这些图片需要用图片id并从集合删除main

//如果删除刚上传的图片需要从集合删除还需要在fileUrlList集合删除

/**
 * 由提交订单功能的征信信息页面进入
 * 注意：该页面图片只上传到oss不上传到服务器 所以需要使用{@link ExtraNotUploadServerImgActivity#uploadFileUrlList}来存放即将上传到服务器的图片的fid
 * 先调用接口获取该用户之前上传的授权书图片
 * 删除图片功能有两种情况：
 * 1.要删除的图片是从服务器拉下来的
 * 2.要删除的图片是刚上传的 这时候fileUrlList集合也必须删除
 */
public class ExtraNotUploadServerImgActivity extends BaseActivity {
    private RvAdapter adapter;
    private List<UploadImgItemBean> imgList = new ArrayList<>();
    private List<UploadFilesUrlReq.FileUrlBean> uploadFileUrlList = new ArrayList<>();
    private List<UploadImgItemBean> hasUploadOSSLists = new ArrayList<>();
    private String clt_id;
    private String title;
    private String type;
    private String role;
    private TextView mEditTv;
    private LinearLayout uploadBottomLin;
    private TextView selectTv;
    private TextView delTv;
    private boolean isEditing = false;
    private Intent mGetIntent;
    private ImageView expandImg;
    private ImageView templateImg;
    private LinearLayout templateLin;
    private TextView templateContent;
    private boolean isTemplateExpand = true;
    private String dlr_id;
    private String bank_id;
    private ArrayList<String> url_list;
    private TextView imgsSizeTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_list);
        mGetIntent = getIntent();
        clt_id = mGetIntent.getStringExtra("clt_id");
        title = mGetIntent.getStringExtra("title");
        type = mGetIntent.getStringExtra("type");
        role = mGetIntent.getStringExtra("role");
        bank_id = mGetIntent.getStringExtra("bank_id");
        dlr_id = mGetIntent.getStringExtra("dlr_id");
        imgList = (List<UploadImgItemBean>) mGetIntent.getSerializableExtra("imgList");
        uploadFileUrlList = (List<UploadFilesUrlReq.FileUrlBean>) mGetIntent.getSerializableExtra("uploadFileUrlList");

        initView();
        initData();
    }

    private void onTitleBarRightTextClick() {
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

            selectTv.setText("全选");
            delTv.setText("删除");
            delTv.setTextColor(Color.parseColor("#d1d1d1"));
        }

        //通知adapter编辑状态改变 请求重绘
        adapter.setIsEditing(isEditing);
    }

    private void onSelectTextClick() {
        if (selectTv.getText().toString().equals("全选")) {
            for (UploadImgItemBean itemBean : imgList) {
                itemBean.hasChoose = true;
            }
            selectTv.setText("取消全选");
            delTv.setText(String.format(Locale.CHINA, "删除(%d)", getCurrentChooseItemCount()));
            delTv.setTextColor(Color.RED);
            adapter.notifyDataSetChanged();
        } else if (selectTv.getText().toString().equals("取消全选")) {
            for (UploadImgItemBean itemBean : imgList) {
                itemBean.hasChoose = false;
            }
            selectTv.setText("全选");
            delTv.setText("删除");
            delTv.setTextColor(Color.parseColor("#d1d1d1"));
            adapter.notifyDataSetChanged();
        }
    }

    private void onImgClick(UploadImgItemBean item) {
        if (isEditing) {
            item.hasChoose = !item.hasChoose;
            adapter.notifyDataSetChanged();
            if (getCurrentChooseItemCount() != 0) {
                delTv.setText(String.format(Locale.CHINA, "删除(%d)", getCurrentChooseItemCount()));
                delTv.setTextColor(Color.RED);
            } else {
                delTv.setText("删除");
                delTv.setTextColor(Color.parseColor("#d1d1d1"));
            }
        } else {
            String imgUrl;
            if (!TextUtils.isEmpty(item.local_path)) {
                imgUrl = item.local_path;
            } else {
                imgUrl = item.raw_url;
            }

            Intent intent = new Intent(ExtraNotUploadServerImgActivity.this, ExtraPreviewActivity.class);
            intent.putExtra("PreviewImg", imgUrl);
            startActivity(intent);
            overridePendingTransition(R.anim.center_zoom_in, R.anim.stay);
        }
    }

    private void onAddBtnClick() {
        Intent i = new Intent(ExtraNotUploadServerImgActivity.this, PhotoMediaActivity.class);
        i.putExtra("loadType", PhotoVideoDir.Type.IMAGE.toString());
        startActivityForResult(i, 100);
    }

    private void initView() {
        TitleBar titleBar = initTitleBar(this, title);
        mEditTv = titleBar.getRightTextTv();
        mEditTv.setEnabled(false);
        mEditTv.setTextColor(Color.parseColor("#d1d1d1"));
        titleBar.setRightText("编辑").setRightClickListener(v -> onTitleBarRightTextClick());
        uploadBottomLin = (LinearLayout) findViewById(R.id.upload_bottom_lin);
        selectTv = (TextView) findViewById(R.id.upload_bottom_tv1);
        delTv = (TextView) findViewById(R.id.upload_bottom_tv2);
        selectTv.setOnClickListener(v -> onSelectTextClick());
        delTv.setOnClickListener(v -> delImgs());

        RecyclerView rv = (RecyclerView) findViewById(R.id.upload_list_rv);
        rv.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new RvAdapter(this, imgList);
        rv.setAdapter(adapter);
        adapter.setOnItemClick(new RvAdapter.OnItemClick() {
            @Override
            public void onItemClick(View v, UploadImgItemBean item) {
                onImgClick(item);
            }

            @Override
            public void onFooterClick(View v) {
                onAddBtnClick();
            }
        });

        expandImg = (ImageView) findViewById(R.id.upload_list_expand_img);
        templateImg = (ImageView) findViewById(R.id.upload_list_template_img);
        Button templateImgLook = (Button) findViewById(R.id.upload_list_template_img_look);
        ImageView templateVideoLook = (ImageView) findViewById(R.id.upload_list_template_video_look);
        templateImgLook.setVisibility(View.VISIBLE);
        templateVideoLook.setVisibility(View.GONE);
        imgsSizeTv = (TextView) findViewById(R.id.upload_list_template_imgs_size);
        templateLin = (LinearLayout) findViewById(R.id.upload_list_template);
        TextView templateTitle = (TextView) findViewById(R.id.upload_list_template_title);
        templateTitle.setText(title + "要求");
        templateContent = (TextView) findViewById(R.id.upload_list_template_content);
        LinearLayout templateTitleLin = (LinearLayout) findViewById(R.id.upload_list_template_title_lin);
        templateTitleLin.setOnClickListener(v -> {
            if (isTemplateExpand) {
                foldTemplate();
            } else {
                expandTemplate();
            }
        });
        //预览模板图片或视频
        findViewById(R.id.upload_list_template_img_group).setOnClickListener(v -> previewTemplateImgs());
    }

    private void delImgs() {
        //要删除的图片的id集合 (图片是从服务器拉取)
        List<String> delImgIdList = new ArrayList<>();
        //要删除的图片的objectKey集合 (图片是现拍的)
        List<String> delImgObjectKeyList = new ArrayList<>();

        //要删除的索引集合
        List<Integer> indexList = new ArrayList<>();
        for (int i = 0; i < imgList.size(); i++) {
            if (imgList.get(i).hasChoose) indexList.add(i);
        }
        Collections.sort(indexList);//排序

        if (indexList.size() == 0) {
            //没有选中图片就不予点击删除按键
            return;
        }

        //每删除一个对象就该偏移+1
        int offset = 0;//indexList 删除的索引集合  删掉一个左标迁移，
        for (int i = 0; i < indexList.size(); i++) {
            int delIndex = indexList.get(i) - offset;
            UploadImgItemBean willDelImg = imgList.get(delIndex);
            if (!TextUtils.isEmpty(willDelImg.id)) {
                delImgIdList.add(willDelImg.id);//从服务器拿下来的有id，现拍的是 objectkey
            } else {
                delImgObjectKeyList.add(willDelImg.objectKey);
            }
            imgList.remove(delIndex);
            offset++;
        }

        delTv.setText("删除");
        delTv.setTextColor(Color.parseColor("#d1d1d1"));
        onImgCountChange(imgList.size() > 0);
        adapter.notifyDataSetChanged();


        //1.删除本地上传的图片
        for (String objectKey : delImgObjectKeyList) {
            for (UploadFilesUrlReq.FileUrlBean fileUrlBean : uploadFileUrlList) {
                if (!TextUtils.isEmpty(objectKey) && !TextUtils.isEmpty(fileUrlBean.file_id)) {
                    if (fileUrlBean.file_id.equals(objectKey)) {
                        uploadFileUrlList.remove(fileUrlBean);
                        break;
                    }
                }
            }
        }

        //2.删除服务器拉取的图片
        DelImgsReq req = new DelImgsReq();
        req.clt_id = clt_id;
        req.id.addAll(delImgIdList);//delImgIdList 删除id的集合
        if (delImgIdList.size() > 0) {
            ApiUtil.requestUrl4CodeAndMsg(ExtraNotUploadServerImgActivity.this, Api.getUploadService().delImgs(req), (code, msg) -> {
//            UploadApi.delImgs(ExtraNotUploadServerImgActivity.this, req, (code, msg) -> {
                if (code == 0) {
                    Toast.makeText(myApp, "删除成功", Toast.LENGTH_SHORT).show();
                    onImgCountChange(imgList.size() > 0);
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
            showImgUrls.add(ExtraURLEncoder.encode(url));
        }
        PreviewImgUtil.showImg(this, showImgUrls);
        overridePendingTransition(R.anim.center_zoom_in, R.anim.stay);
    }

    private void initData() {
        onImgCountChange(imgList.size() > 0);

        ApiUtil.requestUrl4Data(this, Api.getUploadService().getTemplateInSqs(bank_id, dlr_id),
                (OnItemDataCallBack<GetTemplateResp>) getTemplateResp -> showTemplate(getTemplateResp));
    }

    private void showTemplate(GetTemplateResp getTemplateResp) {
        if (getTemplateResp.checker_item_ == null || getTemplateResp.checker_item_.url_list == null || getTemplateResp.checker_item_.url_list.size() == 0) {
            Toast.makeText(myApp, "该文件暂无模板.", Toast.LENGTH_SHORT).show();
            return;
        }
        templateLin.setVisibility(View.VISIBLE);
        templateContent.setText(Html.fromHtml(getTemplateResp.checker_item_.description));
        url_list = getTemplateResp.checker_item_.url_list;
        imgsSizeTv.setText(getTemplateResp.checker_item_.url_list.size() + "");

        Glide.with(ExtraNotUploadServerImgActivity.this).load(url_list.get(0)).into(templateImg);
    }

    private void onImgCountChange(boolean hasImg) {
        if (hasImg) {
            mEditTv.setEnabled(true);
            mEditTv.setTextColor(Color.parseColor("#FF000000"));
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
        for (UploadImgItemBean itemBean : imgList) {
            if (itemBean.hasChoose) {
                totalCount++;
            }
        }
        return totalCount;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
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

                hasUploadOSSLists.clear();

                Dialog dialog = LoadingUtils.createLoadingDialog(this);
                dialog.show();
                new Thread(() -> {
                    for (UploadImgItemBean uploadImgItemBean : toAddList) {
                        OssUtil.synchronizationUploadOss(ExtraNotUploadServerImgActivity.this, uploadImgItemBean.local_path, new OSSObjectKeyBean(role, type, ".png"), objectKey -> {
                            hasUploadOSSLists.add(uploadImgItemBean);
                            uploadImgItemBean.objectKey = objectKey;
                        }, throwable -> hasUploadOSSLists.add(uploadImgItemBean));
                        onUploadOssFinish(hasUploadOSSLists.size(), files, dialog, toAddList);
                    }
                }).start();
            }
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

    private void onUploadOssFinish(int finalAccount, ArrayList<String> files, Dialog dialog, final List<UploadImgItemBean> toAddList) {
        if (finalAccount == files.size()) {
            dialog.dismiss();
            runOnUiThread(() -> calculateRelToAddList(toAddList, relToAddList -> {
                imgList.addAll(relToAddList);
                onImgCountChange(imgList.size() > 0);
                for (UploadImgItemBean imgItemBean1 : relToAddList) {
                    UploadFilesUrlReq.FileUrlBean fileUrlBean = new UploadFilesUrlReq.FileUrlBean();
                    fileUrlBean.clt_id = clt_id;
                    fileUrlBean.file_id = imgItemBean1.objectKey;
                    fileUrlBean.label = imgItemBean1.type;
                    uploadFileUrlList.add(fileUrlBean);
                }
            }));
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK, mGetIntent);
        finish();
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
                    GlideUtil.loadImg(mContext, statusImageRel, item.s_url);
                }
                holder.itemView.setOnClickListener(mOnItemClick == null ? null : (View.OnClickListener) v -> mOnItemClick.onItemClick(v, item));
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
            void onItemClick(View v, UploadImgItemBean item);

            void onFooterClick(View v);
        }

        public void setOnItemClick(OnItemClick mOnItemClick) {
            this.mOnItemClick = mOnItemClick;
        }
    }
}

