package com.yusion.shanghai.yusion4s.ui.upload;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import com.awen.photo.photopick.controller.PhotoPagerConfig;
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
import com.yusion.shanghai.yusion4s.retrofit.api.UploadApi;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnCodeAndMsgCallBack;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.utils.DensityUtil;
import com.yusion.shanghai.yusion4s.utils.GlideUtil;
import com.yusion.shanghai.yusion4s.utils.LoadingUtils;
import com.yusion.shanghai.yusion4s.utils.OssUtil;
import com.yusion.shanghai.yusion4s.utils.URLEncoder;
import com.yusion.shanghai.yusion4s.widget.TitleBar;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//选完用户后获取授权书图片并传入该页面
//如果删除这些图片需要用图片id并从集合删除main

//如果删除刚上传的图片需要从集合删除还需要在fileUrlList集合删除

public class ExtraUploadNotUploadServerImgActivity extends BaseActivity {
    private RvAdapter adapter;
    private boolean canBack = true;
    private List<UploadImgItemBean> imgList = new ArrayList<>();
    private List<UploadFilesUrlReq.FileUrlBean> uploadFileUrlList = new ArrayList<>();
    private List<UploadImgItemBean> hasUploadOssLists = new ArrayList<>();
    private String clt_id;
    private String title;
    private String type;
    private String role;
    private TitleBar titleBar;
    private TextView mEditTv;
    private LinearLayout uploadBottomLin;
    private TextView uploadTv1;
    private TextView uploadTv2;
    private boolean isEditing = false;
    private Intent mGetIntent;
    private ImageView expandImg;
    private ImageView templateImg;
    private Button templateImgLook;
    private ImageView templateVideoLook;
    private LinearLayout templateLin;
    private TextView templateTitle;
    private TextView templateContent;
    private LinearLayout templateTitleLin;
    private boolean isTemplateExpand = true;
    private String detail_url;
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

    private void initView() {
        titleBar = initTitleBar(this, title).setLeftClickListener(v -> onBack());
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
        uploadTv1 = (TextView) findViewById(R.id.upload_bottom_tv1);//全选
        uploadTv2 = (TextView) findViewById(R.id.upload_bottom_tv2);//删除
        uploadTv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploadTv1.getText().toString().equals("全选")) {
                    for (UploadImgItemBean itemBean : imgList) {
                        itemBean.hasChoose = true;
                    }
                    uploadTv1.setText("取消全选");
                    uploadTv2.setText(String.format("删除(%d)", getCurrentChooseItemCount()));
                    uploadTv2.setTextColor(Color.RED);
                    adapter.notifyDataSetChanged();
                } else if (uploadTv1.getText().toString().equals("取消全选")) {
                    for (UploadImgItemBean itemBean : imgList) {
                        itemBean.hasChoose = false;
                    }
                    uploadTv1.setText("全选");
                    uploadTv2.setText("删除");
                    uploadTv2.setTextColor(Color.parseColor("#d1d1d1"));
                    adapter.notifyDataSetChanged();
                }
            }
        });
        uploadTv2.setOnClickListener(new View.OnClickListener() {//删除
            @Override
            public void onClick(View v) {

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

                uploadTv2.setText("删除");
                uploadTv2.setTextColor(Color.parseColor("#d1d1d1"));
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
                    UploadApi.delImgs(ExtraUploadNotUploadServerImgActivity.this, req, new OnCodeAndMsgCallBack() {
                        @Override
                        public void callBack(int code, String msg) {
                            if (code == 0) {
                                Toast.makeText(myApp, "删除成功", Toast.LENGTH_SHORT).show();
                                onImgCountChange(imgList.size() > 0);
                            }
                        }
                    });
                }
            }
        });

        RecyclerView rv = (RecyclerView) findViewById(R.id.upload_list_rv);
        rv.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new RvAdapter(this, imgList);
        rv.setAdapter(adapter);
        adapter.setOnItemClick(new RvAdapter.OnItemClick() {
            @Override
            public void onItemClick(View v, UploadImgItemBean item) {
                if (isEditing) {
                    if (item.hasChoose) {
                        item.hasChoose = false;
                    } else {
                        item.hasChoose = true;
                    }
                    adapter.notifyDataSetChanged();
//
                    if (getCurrentChooseItemCount() != 0) {
                        uploadTv2.setText(String.format("删除(%d)", getCurrentChooseItemCount()));
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
                Intent i = new Intent(ExtraUploadNotUploadServerImgActivity.this, PhotoMediaActivity.class);
                i.putExtra("loadType", PhotoVideoDir.Type.IMAGE.toString());
                startActivityForResult(i, 100);
            }
        });

        expandImg = (ImageView) findViewById(R.id.upload_list_expand_img);
        templateImg = (ImageView) findViewById(R.id.upload_list_template_img);
        templateImgLook = (Button) findViewById(R.id.upload_list_template_img_look);
        templateVideoLook = (ImageView) findViewById(R.id.upload_list_template_video_look);
        templateImgLook.setVisibility(View.VISIBLE);
        templateVideoLook.setVisibility(View.GONE);
        imgsSizeTv = (TextView) findViewById(R.id.upload_list_template_imgs_size);
        templateLin = (LinearLayout) findViewById(R.id.upload_list_template);
        templateTitle = (TextView) findViewById(R.id.upload_list_template_title);
        templateContent = (TextView) findViewById(R.id.upload_list_template_content);
        templateTitleLin = (LinearLayout) findViewById(R.id.upload_list_template_title_lin);
        templateTitleLin.setOnClickListener(v -> {
            if (isTemplateExpand) {
                foldTemplate();
            } else {
                expandTemplate();
            }
        });
        templateLin.setOnClickListener(v -> {
        });
        templateImg.setOnClickListener(img -> previewImgs());
        templateImgLook.setOnClickListener(v -> previewImgs());
    }

    private void previewImg(View previewAnchor, String imgUrl) {
//        if (TextUtils.isEmpty(imgUrl)) {
//            Toast.makeText(myApp, "没有找到图片", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        Intent intent = new Intent(this, PreviewActivity.class);
//        intent.putExtra("PreviewImg", imgUrl);
//        intent.putExtra("breviary", true);
//        startActivity(intent);

        Intent intent = new Intent(this, ExtraPreviewActivity.class);
        intent.putExtra("PreviewImg", imgUrl);
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, previewAnchor, "shareNames");
        ActivityCompat.startActivity(this, intent, compat.toBundle());
    }

    private void previewImgs() {
        if (url_list == null || url_list.size() == 0) {
            Toast.makeText(myApp, "该文件暂无模板!!!", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<String> showImgUrls = new ArrayList<>();
        for (String url : url_list) {
            showImgUrls.add(URLEncoder.encode(url));
        }
        new PhotoPagerConfig.Builder(this)
                .setBigImageUrls(showImgUrls)
//                .setSavaImage(true)
//                        .setPosition(2)
//                        .setSaveImageLocalPath("这里是你想保存的图片地址")
                .build();
    }

    private void initData() {
        onImgCountChange(imgList.size() > 0);
        adapter.notifyDataSetChanged();

        UploadApi.getTemplateInSqs(this, bank_id, dlr_id, new OnItemDataCallBack<GetTemplateResp>() {

            @Override
            public void onItemDataCallBack(GetTemplateResp data) {
                if (data != null) {
                    templateLin.setVisibility(View.VISIBLE);
                    if (data.checker_item_ == null) {
                        Toast.makeText(myApp, "该文件暂无模板!!!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    templateContent.setText(Html.fromHtml(data.checker_item_.description));
                    detail_url = data.checker_item_.detail_url;
                    url_list = data.checker_item_.url_list;
                    if (url_list == null || url_list.size() == 0) {
                        Toast.makeText(myApp, "请相关人员添加模板图片!!!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    imgsSizeTv.setText(data.checker_item_.url_list.size() + "");
                    templateTitle.setText(data.checker_item_.name + "要求");
                    if (!isFinishing()) {
                        Glide.with(ExtraUploadNotUploadServerImgActivity.this).load(url_list.get(0)).into(templateImg);
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
                Dialog dialog = LoadingUtils.createLoadingDialog(this);
                dialog.show();
                new Thread(() -> {
                    for (UploadImgItemBean uploadImgItemBean : toAddList) {
                        OssUtil.synchronizationUploadOss(ExtraUploadNotUploadServerImgActivity.this, uploadImgItemBean.local_path, new OSSObjectKeyBean(role, type, ".png"), objectKey -> {
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
                            imgList.addAll(relToAddList);
                            onImgCountChange(imgList.size() > 0);
                            hasUploadOssLists.clear();
                            for (UploadImgItemBean imgItemBean1 : relToAddList) {
                                UploadFilesUrlReq.FileUrlBean fileUrlBean = new UploadFilesUrlReq.FileUrlBean();
                                fileUrlBean.clt_id = clt_id;
                                fileUrlBean.file_id = imgItemBean1.objectKey;
                                fileUrlBean.label = imgItemBean1.type;
                                uploadFileUrlList.add(fileUrlBean);
                                Log.e("TAG", uploadFileUrlList.toString());
                            }

                        }
                    });
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    private void onBack() {
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
//                view = new StatusImageRel(mContext);
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

