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
import com.yusion.shanghai.yusion4s.bean.upload.ListImgsResp;
import com.yusion.shanghai.yusion4s.bean.upload.UploadFilesUrlReq;
import com.yusion.shanghai.yusion4s.bean.upload.UploadImgItemBean;
import com.yusion.shanghai.yusion4s.glide.StatusImageRel;
import com.yusion.shanghai.yusion4s.retrofit.Api;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnVoidCallBack;
import com.yusion.shanghai.yusion4s.settings.Constants;
import com.yusion.shanghai.yusion4s.utils.ApiUtil;
import com.yusion.shanghai.yusion4s.utils.DensityUtil;
import com.yusion.shanghai.yusion4s.utils.ExtraURLEncoder;
import com.yusion.shanghai.yusion4s.utils.GlideUtil;
import com.yusion.shanghai.yusion4s.utils.LoadingUtils;
import com.yusion.shanghai.yusion4s.utils.OssUtil;
import com.yusion.shanghai.yusion4s.utils.PreviewImgUtil;
import com.yusion.shanghai.yusion4s.utils.SharedPrefsUtil;
import com.yusion.shanghai.yusion4s.widget.TitleBar;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

// TODO: 2017/12/22  上传oss操作需要花时间优化
public class ExtraUploadListActivity extends BaseActivity {

    private ListDealerLabelsResp.LabelListBean topItem;
    private TextView errorTv;
    private LinearLayout errorLin;
    private RvAdapter adapter;
    private List<UploadImgItemBean> imgList = new ArrayList<>();
    private List<UploadImgItemBean> hasUploadOSSLists = new ArrayList<>();

    private String app_id;
    private String clt_id;
    private TextView mEditTv;
    private LinearLayout uploadBottomLin;
    private TextView selectTv;
    private TextView delTv;
    private boolean isEditing = false;
    private ImageView expandImg;


    private boolean isTemplateExpand = true;
    private LinearLayout templateLin;
    private TextView templateContent;
    private ImageView templateImg;
    private ArrayList<String> url_list;
    private TextView imgsSizeTv;

    private ArrayList<UploadImgItemBean> enterDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_list);

        //以下数据必须优先初始化
        topItem = (ListDealerLabelsResp.LabelListBean) getIntent().getSerializableExtra("topItem");
        app_id = getIntent().getStringExtra("app_id");
        clt_id = getIntent().getStringExtra("clt_id");
        isVideoPage = topItem.ftype.equals("video");

        initView();
        initData();
    }

    private void initView() {
        TitleBar titleBar = initTitleBar(this, topItem.name);
        //默认编辑按钮不可使用
        mEditTv = titleBar.getRightTextTv();
        mEditTv.setEnabled(false);
        mEditTv.setTextColor(Color.parseColor("#d1d1d1"));
        titleBar.setRightText("编辑").setRightClickListener(v -> onTitleBarRightTextClick());

        //点击编辑按钮后下方的 选择/删除 布局
        uploadBottomLin = findViewById(R.id.upload_bottom_lin);
        selectTv = findViewById(R.id.upload_bottom_tv1);//全选
        delTv = findViewById(R.id.upload_bottom_tv2);//删除
        selectTv.setOnClickListener(v -> onSelectTextClick());
        delTv.setOnClickListener(v -> delImgs());

        //显示该项影像件是否有误
        errorTv = findViewById(R.id.upload_list_error_tv);
        errorLin = findViewById(R.id.upload_list_error_lin);

        //加载图片列表
        RecyclerView rv = findViewById(R.id.upload_list_rv);
        rv.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new RvAdapter(this, imgList, isVideoPage);
        rv.setAdapter(adapter);
        adapter.setOnItemClick(new RvAdapter.OnItemClick() {
            @Override
            public void onItemClick(View v, UploadImgItemBean item, int index) {
                onImgClick(item);
            }

            @Override
            public void onFooterClick(View v) {
                onAddBtnClick();
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
        //预览模板图片或视频
        findViewById(R.id.upload_list_template_img_group).setOnClickListener(v -> {
                    if (isVideoPage) {
                        playVideo(url_list.get(0));
                    } else {
                        previewTemplateImgs();
                    }
                }
        );
    }

    private void onAddBtnClick() {
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

                //https://yusiontech-test.oss-cn-hangzhou.aliyuncs.com/CUSTOMER/19999999999/lender/id_card_copy/1514181347146.png?OSSAccessKeyId=STS.CnHYMco6ZAz3XGU7ERnaJiNCQ&security-token=CAIS9AF1q6Ft5B2yfSjIoq39EvfXgul79rjYWmHk00UHYu5mhovoszz2IHxOfnBtAekbsPo%2FlWBZ6PYclqN6U4cATkjFYM1stkHBVYxBJ9ivgde8yJBZor%2FHcDHhJnyW9cvWZPqDP7G5U%2FyxalfCuzZuyL%2FhD1uLVECkNpv74vwOLK5gPG%2BCYCFBGc1dKyZ7tcYeLgGxD%2Fu2NQPwiWeiZygB%2BCgE0Dwjt%2Fnun5LCsUOP0AGrkdV4%2FdqhfsKWCOB3J4p6XtuP2%2Bh7S7HMyiY46WIRqP0s1fEcoGqZ4IHMUwgPs0ucUOPM6phoNxQ8aaUmCzu4ZDBEbRgTGoABAyF1tXk9Mnvt966OzQ0PX6RMWg7Td5Nmpj2VJTZQK%2BT4NGrtimboZWDwYJNNxwlTlUNzzF9ZvhcavgZzV06VkpZwI8AkQZHdyXrjV0ufT1W6BJ%2BD%2FR5304B8DoHa5N%2BRIIe%2FfVi4DcTy42SM52qh%2BWxmk8Cou5I7UyUpI9XaeLM%3D&Expires=1514192939&Signature=8jpFpWoHVFVnlkZeE9L6UtquUTY%3D
//                预览方式1：可以有加载进度,但是没有放大拖拽等功能
                Intent intent = new Intent(ExtraUploadListActivity.this, ExtraPreviewActivity.class);
                intent.putExtra("PreviewImg", imgUrl);
                startActivity(intent);
                overridePendingTransition(R.anim.center_zoom_in, R.anim.stay);

//                预览方式1：可以有加载进度,也有放大拖拽等功能 但是加载oss图片失败
//                ArrayList<String> showImgUrls = new ArrayList<>();
//                for (String url : url_list) {
//                    showImgUrls.add(ExtraURLEncoder.encode(imgUrl));
//                }
//                PreviewImgUtil.showImg(this, showImgUrls);
//                overridePendingTransition(R.anim.center_zoom_in, R.anim.stay);
            }
        }
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

    private void delImgs() {
        //要删除的图片的id集合
        List<String> delImgIdList = new ArrayList<>();
        //要删除的索引集合
        List<Integer> indexList = new ArrayList<>();
        for (int i = 0; i < imgList.size(); i++) {
            if (imgList.get(i).hasChoose) indexList.add(i);
        }
        Collections.sort(indexList);

        if (indexList.size() == 0) {
            //没有选中图片就不予点击删除按键
            return;
        }

        ArrayList<UploadImgItemBean> tempList = new ArrayList<>();
        tempList.addAll(imgList);
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
            ApiUtil.requestUrl4CodeAndMsg(ExtraUploadListActivity.this, Api.getUploadService().delImgs(req), (code, msg) -> {
//                UploadApi.delImgs(ExtraUploadListActivity.this, req, (code, msg) -> {
                if (code == 0) {
                    Toast.makeText(myApp, "删除成功", Toast.LENGTH_SHORT).show();

                    delTv.setText("删除");
                    delTv.setTextColor(Color.parseColor("#d1d1d1"));

                    imgList.clear();
                    imgList.addAll(tempList);

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

    private void playVideo(String url) {
        if (TextUtils.isEmpty(url)) {
            Toast.makeText(myApp, "未找到可播放的视频", Toast.LENGTH_SHORT).show();
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
        ApiUtil.requestUrl4Data(this, Api.getUploadService().listImgs(req),
                (OnItemDataCallBack<ListImgsResp>) listImgsResp -> {
                    if (listImgsResp.has_err) {
                        errorLin.setVisibility(View.VISIBLE);
                        errorTv.setText("您提交的资料有误：" + listImgsResp.error);
                    } else {
                        errorLin.setVisibility(View.GONE);
                    }

                    if (listImgsResp.list.size() != 0) {
                        imgList.addAll(listImgsResp.list);
                        onImgCountChange(listImgsResp.list.size() > 0);
                        //记录页面初始数据
                        enterDataList.addAll(imgList);
                    }

                    ApiUtil.requestUrl4Data(this, Api.getUploadService().getTemplate(topItem.id),
                            (OnItemDataCallBack<GetTemplateResp>) getTemplateResp -> showTemplate(getTemplateResp));
                });
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

        Glide.with(ExtraUploadListActivity.this).load(url_list.get(0)).into(templateImg);
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

            //开始遍历集合 一张一张上传到oss上
            Dialog dialog = LoadingUtils.createLoadingDialog(this);
            dialog.show();
            new Thread(() -> {
                String suffix = isVideoPage ? ".mp4" : ".png";
                for (UploadImgItemBean uploadImgItemBean : toAddList) {
                    OssUtil.synchronizationUploadOss(ExtraUploadListActivity.this, uploadImgItemBean.local_path, new OSSObjectKeyBean(Constants.PersonType.LENDER, topItem.value, suffix), objectKey -> {
                        hasUploadOSSLists.add(uploadImgItemBean);
                        uploadImgItemBean.objectKey = objectKey;
                    }, throwable -> hasUploadOSSLists.add(uploadImgItemBean));
                    onUploadOssFinish(hasUploadOSSLists.size(), files, dialog, toAddList);
                }
            }).start();
        }
    }

    private void onUploadOssFinish(int finalAccount, ArrayList<String> files, Dialog dialog, final List<UploadImgItemBean> toAddList) {
        if (finalAccount == files.size()) {
            dialog.dismiss();
            runOnUiThread(() -> calculateRelToAddList(toAddList, relToAddList -> uploadImgs(app_id, clt_id, relToAddList, () -> {
                if (relToAddList.size() != toAddList.size()) {
                    Toast.makeText(myApp, "由于网络因素，有些照片未上传成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(myApp, "上传成功", Toast.LENGTH_SHORT).show();
                }
                imgList.addAll(relToAddList);
                onImgCountChange(imgList.size() > 0);
            })));
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

        UploadFilesUrlReq uploadFilesUrlReq = new UploadFilesUrlReq();
        uploadFilesUrlReq.files = uploadFileUrlBeanList;
        uploadFilesUrlReq.region = SharedPrefsUtil.getInstance(this).getValue("region", "");
        uploadFilesUrlReq.bucket = SharedPrefsUtil.getInstance(this).getValue("bucket", "");
        ApiUtil.requestUrl4Data(this, Api.getUploadService().uploadFileUrlWithIdsResp(uploadFilesUrlReq),
                (OnItemDataCallBack<List<String>>) ids -> {
                    for (int i = 0; i < lists.size(); i++) {
                        lists.get(i).id = ids.get(i);
                    }
                    onSuccessCallBack.callBack();
                });
    }

    @Override
    public void onBackPressed() {
        boolean hasChange = false;
        //检查数据源是否改变
        if (imgList.size() == enterDataList.size()) {
            for (UploadImgItemBean uploadImgItemBean : enterDataList) {
                if (!imgList.contains(uploadImgItemBean)) {
                    hasChange = true;
                }
            }
        } else {
            hasChange = true;
        }
        topItem.has_change = hasChange;

        topItem.has_img = imgList.size();
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

