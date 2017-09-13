package com.yusion.shanghai.yusion4s.ui.upload;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pbq.pickerlib.activity.PhotoMediaActivity;
import com.pbq.pickerlib.entity.PhotoVideoDir;
import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.bean.oss.OSSObjectKeyBean;
import com.yusion.shanghai.yusion4s.bean.upload.DelImgsReq;
import com.yusion.shanghai.yusion4s.bean.upload.UploadFilesUrlReq;
import com.yusion.shanghai.yusion4s.bean.upload.UploadImgItemBean;
import com.yusion.shanghai.yusion4s.glide.StatusImageRel;
import com.yusion.shanghai.yusion4s.retrofit.api.UploadApi;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnCodeAndMsgCallBack;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.utils.GlideUtil;
import com.yusion.shanghai.yusion4s.utils.LoadingUtils;
import com.yusion.shanghai.yusion4s.utils.OssUtil;
import com.yusion.shanghai.yusion4s.widget.TitleBar;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//选完用户后获取授权书图片并传入该页面
//如果删除这些图片需要用图片id并从集合删除main

//如果删除刚上传的图片需要从集合删除还需要在fileUrlList集合删除

public class UploadSqsListActivity extends BaseActivity {
    private RvAdapter adapter;

    private List<UploadImgItemBean> imgList = new ArrayList<>();
    private List<UploadFilesUrlReq.FileUrlBean> uploadFileUrlList = new ArrayList<>();
    private List<UploadImgItemBean> hasUploadLists = new ArrayList<>();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_list);

        mGetIntent = getIntent();
        clt_id = mGetIntent.getStringExtra("clt_id");
        title = mGetIntent.getStringExtra("title");
        type = mGetIntent.getStringExtra("type");
        role = mGetIntent.getStringExtra("role");
        imgList = (List<UploadImgItemBean>) mGetIntent.getSerializableExtra("imgList");
        uploadFileUrlList = (List<UploadFilesUrlReq.FileUrlBean>) mGetIntent.getSerializableExtra("uploadFileUrlList");

        initView();
        initData();
    }

    private void initView() {
        titleBar = initTitleBar(this, title).setLeftClickListener(v -> onBack());
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
                        if (!TextUtils.isEmpty(objectKey) && TextUtils.isEmpty(fileUrlBean.file_id)) {
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
                    UploadApi.delImgs(UploadSqsListActivity.this, req, new OnCodeAndMsgCallBack() {
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
            public void onItemClick(View v, UploadImgItemBean item, ImageView cbImg) {
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
                }
            }

            @Override
            public void onFooterClick(View v) {
                Intent i = new Intent(UploadSqsListActivity.this, PhotoMediaActivity.class);
                i.putExtra("loadType", PhotoVideoDir.Type.IMAGE.toString());
                startActivityForResult(i, 100);
            }
        });
    }

    private void initData() {
        onImgCountChange(imgList.size() > 0);
        adapter.notifyDataSetChanged();
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
                imgList.addAll(toAddList);
                adapter.notifyItemRangeInserted(adapter.getItemCount(), toAddList.size());
                onImgCountChange(imgList.size() > 0);
                hasUploadLists.clear();

                Dialog dialog = LoadingUtils.createLoadingDialog(this);
                dialog.show();
                //int account = 0;
                for (UploadImgItemBean imgItemBean : toAddList) {
                    //account++;
                    //int finalAccount = account;
                    OssUtil.uploadOss(this, false, imgItemBean.local_path, new OSSObjectKeyBean(role, type, ".png"), new OnItemDataCallBack<String>() {
                        @Override
                        public void onItemDataCallBack(String objectKey) {
                            imgItemBean.objectKey = objectKey;
                            hasUploadLists.add(imgItemBean);
                            if (hasUploadLists.size() == files.size()) {
                                dialog.dismiss();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        for (UploadImgItemBean imgItemBean : toAddList) {
                                            UploadFilesUrlReq.FileUrlBean fileUrlBean = new UploadFilesUrlReq.FileUrlBean();
                                            fileUrlBean.clt_id = clt_id;
                                            fileUrlBean.file_id = imgItemBean.objectKey;
                                            fileUrlBean.label = imgItemBean.type;
                                            uploadFileUrlList.add(fileUrlBean);
                                        }
                                    }
                                });
                            }
                        }
                    }, new OnItemDataCallBack<Throwable>() {
                        @Override
                        public void onItemDataCallBack(Throwable data) {
                            if (hasUploadLists.size() == files.size()) {
                                dialog.dismiss();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        for (UploadImgItemBean imgItemBean : toAddList) {
                                            UploadFilesUrlReq.FileUrlBean fileUrlBean = new UploadFilesUrlReq.FileUrlBean();
                                            fileUrlBean.clt_id = clt_id;
                                            fileUrlBean.file_id = imgItemBean.objectKey;
                                            fileUrlBean.label = imgItemBean.type;
                                            uploadFileUrlList.add(fileUrlBean);
                                        }
                                    }
                                });
                            }
                        }
                    });
                }


            }
        }
    }

//    private Dialog mUploadFileDialog;
//    public void uploadImgs(String clt_id, List<UploadImgItemBean> imgList) {
//        uploadFileUrlList = new ArrayList<>();
//        for (UploadImgItemBean imgItemBean : imgList) {
//            UploadFilesUrlReq.FileUrlBean fileUrlBean = new UploadFilesUrlReq.FileUrlBean();
//            fileUrlBean.clt_id = clt_id;
//            fileUrlBean.file_id = imgItemBean.objectKey;
//            fileUrlBean.label = imgItemBean.type;
//            uploadFileUrlList.add(fileUrlBean);
//
//        }
//        if (mUploadFileDialog == null) {
//            mUploadFileDialog = LoadingUtils.createLoadingDialog(this);
//            mUploadFileDialog.setCancelable(false);
//        }
//        mUploadFileDialog.show();
//        UploadFilesUrlReq uploadFilesUrlReq = new UploadFilesUrlReq();
//        uploadFilesUrlReq.files = uploadFileUrlList;
//        uploadFilesUrlReq.region = SharedPrefsUtil.getInstance(this).getValue("region", "");
//        uploadFilesUrlReq.bucket = SharedPrefsUtil.getInstance(this).getValue("bucket", "");
//        UploadApi.uploadFileUrl(this, uploadFilesUrlReq, new OnItemDataCallBack<List<String>>() {
//            @Override
//            public void onItemDataCallBack(List<String> data) {
//                for (int i = 0; i < imgList.size(); i++) {
//                    imgList.get(i).id = data.get(i);
//                }
//                mUploadFileDialog.dismiss();
//                Toast.makeText(UploadSqsListActivity.this, "上传照片成功", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    private void onBack() {
        setResult(RESULT_OK, mGetIntent);
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
                view = mLayoutInflater.inflate(R.layout.upload_list_add_img_item, parent, false);
            } else if (viewType == TYPE_IMG) {
                //view = mLayoutInflater.inflate(R.layout.upload_list_img_item, parent, false);
                view = new StatusImageRel(mContext);
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
                //Dialog dialog = LoadingUtils.createLoadingDialog(mContext);
                //dialog.show();
                if (!TextUtils.isEmpty(item.local_path)) {
                    GlideUtil.loadImg(mContext, statusImageRel, new File(item.local_path));
                } else {
                    GlideUtil.loadImg(mContext, statusImageRel, item.s_url);
                }
                holder.itemView.setOnClickListener(mOnItemClick == null ? null : (View.OnClickListener) v -> mOnItemClick.onItemClick(v, item, holder.cbImg));
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

//        private class GlideRequestListener implements RequestListener<Drawable> {
//            private Dialog dialog;
//
//            public GlideRequestListener(Dialog dialog) {
//                this.dialog = dialog;
//            }
//
//            @Override
//            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                Toast.makeText(mContext, "图片加载失败", Toast.LENGTH_SHORT).show();
//                dialog.dismiss();
//                return false;
//            }
//
//            @Override
//            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                dialog.dismiss();
//                return false;
//            }
//        }


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
            public ImageView img;
            public ImageView cbImg;

            public VH(View itemView) {
                super(itemView);
                img = ((ImageView) itemView.findViewById(R.id.upload_list_img_item_img));
                cbImg = ((ImageView) itemView.findViewById(R.id.upload_list_img_item_cb_img));
            }
        }

        public interface OnItemClick {
            void onItemClick(View v, UploadImgItemBean item, ImageView cbImg);

            void onFooterClick(View v);
        }

        public void setOnItemClick(OnItemClick mOnItemClick) {
            this.mOnItemClick = mOnItemClick;
        }
    }
}

