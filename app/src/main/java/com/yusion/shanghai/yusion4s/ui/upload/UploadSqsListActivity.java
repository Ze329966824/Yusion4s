package com.yusion.shanghai.yusion4s.ui.upload;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pbq.pickerlib.activity.PhotoMediaActivity;
import com.pbq.pickerlib.entity.PhotoVideoDir;
import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.bean.oss.OSSObjectKeyBean;
import com.yusion.shanghai.yusion4s.bean.upload.DelImgsReq;
import com.yusion.shanghai.yusion4s.bean.upload.ListDealerLabelsResp;
import com.yusion.shanghai.yusion4s.bean.upload.ListImgsReq;
import com.yusion.shanghai.yusion4s.bean.upload.UploadFilesUrlReq;
import com.yusion.shanghai.yusion4s.bean.upload.UploadImgItemBean;
import com.yusion.shanghai.yusion4s.retrofit.api.UploadApi;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.settings.Constants;
import com.yusion.shanghai.yusion4s.utils.LoadingUtils;
import com.yusion.shanghai.yusion4s.utils.OssUtil;
import com.yusion.shanghai.yusion4s.utils.SharedPrefsUtil;
import com.yusion.shanghai.yusion4s.widget.TitleBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UploadSqsListActivity extends BaseActivity {
    private ListDealerLabelsResp.LabelListBean topItem;
    private TextView errorTv;
    private LinearLayout errorLin;
    private RvAdapter adapter;
    private List<UploadImgItemBean> lists = new ArrayList<>();
    private String app_id;
    private String clt_id;
    private TitleBar titleBar;
    private TextView mEditTv;
    private LinearLayout uploadBottomLin;
    private TextView uploadTv1;
    private TextView uploadTv2;
    private boolean isEditing = false;

//    private UploadImgListAdapter adapter;
//    private Intent mGetIntent;
//    private UploadLabelItemBean mTopItem;//上级页面传过来的bean
//    private TextView errorTv;
//    private LinearLayout errorLin;
//    private List<UploadImgItemBean> imgList;
//    private TitleBar titleBar;
//    private String mRole;
//    private String mType;
//
//
//    private int currentChooseCount = 0;
//    private boolean hasImg = false;
//    private boolean isEditing = false;
//    private TextView mEditTv;
//    private LinearLayout uploadBottomLin;
//    private TextView uploadTv2;
//    private TextView uploadTv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_list);
        topItem = (ListDealerLabelsResp.LabelListBean) getIntent().getSerializableExtra("topItem");
        app_id = getIntent().getStringExtra("app_id");
        clt_id = getIntent().getStringExtra("clt_id");
        initView();
        initData();
//        hasImg = imgList.size() > 0;
//        onImgCountChange(hasImg);
    }

    private void initView() {
        titleBar = initTitleBar(this, topItem.name).setLeftClickListener(v -> onBack());
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
//                List<String> delImgIdList = new ArrayList<>();
//                List<Integer> integerList = new ArrayList<>();
//                for (int i = 0; i < imgList.size(); i++) {
//                    if (imgList.get(i).hasChoose) integerList.add(i);
//                }
//                Collections.sort(integerList);
//                int pyl = 0;
//                for (int i = 0; i < integerList.size(); i++) {
//                    int delIndex = integerList.get(i) - pyl;
//                    delImgIdList.add(imgList.get(delIndex).id);
//                    imgList.remove(delIndex);
//                    pyl++;
//                }
//                uploadTv2.setText("删除");
//                uploadTv2.setTextColor(Color.parseColor("#d1d1d1"));
//                adapter.notifyDataSetChanged();
                DelImgsReq req = new DelImgsReq();
                req.clt_id = clt_id;
                req.app_id = app_id;
//                //删除的图片中包括用户拍摄后没有上传到服务器的图片 这个时候没有id
//                List<String> relDelImgIdList = new ArrayList<>();
//                for (String s : delImgIdList) {
//                    if (!TextUtils.isEmpty(s)) {
//                        relDelImgIdList.add(s);
//                    }
//                }
//                req.id.addAll(relDelImgIdList);
//                if (relDelImgIdList.size() > 0) {
//                    UploadApi.delImgs(UploadListActivity.this, req, new OnCodeAndMsgCallBack() {
//                        @Override
//                        public void callBack(int code, String msg) {
//                            if (code == 0) {
//                                Toast.makeText(myApp, "删除成功", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//                }
            }
        });

        errorTv = (TextView) findViewById(R.id.upload_list_error_tv);
        errorLin = (LinearLayout) findViewById(R.id.upload_list_error_lin);

        RecyclerView rv = (RecyclerView) findViewById(R.id.upload_list_rv);
        rv.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new RvAdapter(this, lists);
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
//                    boolean hasChoose = (Boolean) cbImg.getTag(R.id.hasChoose);
//                    if (hasChoose) {
//                        cbImg.setTag(R.id.hasChoose, false);
//                        cbImg.setImageResource(R.mipmap.choose_icon);
//                        item.hasChoose = false;
//                        currentChooseCount--;
//                    } else {
//                        cbImg.setTag(R.id.hasChoose, true);
//                        cbImg.setImageResource(R.mipmap.surechoose_icon);
//                        item.hasChoose = true;
//                        currentChooseCount++;
//                    }
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

    private int getCurrentChooseItemCount() {
        int totalCount = 0;
        for (UploadImgItemBean itemBean : lists) {
            if (itemBean.hasChoose) {
                totalCount++;
            }
        }
        return totalCount;
    }

    ;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {

                ArrayList<String> files = data.getStringArrayListExtra("files");

//                if (files.size() > 0) {
//                    hasImg = true;
//                    onImgCountChange(hasImg);
//                }

                List<UploadImgItemBean> toAddList = new ArrayList<>();
                for (String file : files) {
                    UploadImgItemBean item = new UploadImgItemBean();
                    item.local_path = file;
                    item.role = Constants.PersonType.LENDER;
                    item.type = topItem.value;
                    toAddList.add(item);
                }
                lists.addAll(toAddList);
                adapter.notifyItemRangeInserted(adapter.getItemCount(), toAddList.size());
                Dialog dialog = LoadingUtils.createLoadingDialog(this);
                dialog.show();
                int account = 0;
                for (UploadImgItemBean imgItemBean : toAddList) {
                    account++;
                    int finalAccount = account;
                    OssUtil.uploadOss(this, false, imgItemBean.local_path, new OSSObjectKeyBean(Constants.PersonType.LENDER, topItem.value, ".png"), new OnItemDataCallBack<String>() {
                        @Override
                        public void onItemDataCallBack(String objectKey) {
                            imgItemBean.objectKey = objectKey;
                            if (finalAccount == files.size()) {
                                dialog.dismiss();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        uploadImgs(app_id, clt_id, toAddList);
                                    }
                                });
                            }
                        }
                    }, new OnItemDataCallBack<Throwable>() {
                        @Override
                        public void onItemDataCallBack(Throwable data) {
                            if (finalAccount == files.size()) {
                                dialog.dismiss();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        uploadImgs(app_id, clt_id, toAddList);
                                    }
                                });
                            }
                        }
                    });
                }


            }
        }
    }

    private Dialog mUploadFileDialog;

    public void uploadImgs(String app_id, String clt_id, List<UploadImgItemBean> lists) {
        List<UploadFilesUrlReq.FileUrlBean> uploadFileUrlBeanList = new ArrayList<>();
        for (UploadImgItemBean imgItemBean : lists) {
            UploadFilesUrlReq.FileUrlBean fileUrlBean = new UploadFilesUrlReq.FileUrlBean();
            fileUrlBean.app_id = app_id;
            fileUrlBean.clt_id = clt_id;
            fileUrlBean.file_id = imgItemBean.objectKey;
            fileUrlBean.label = imgItemBean.type;
            uploadFileUrlBeanList.add(fileUrlBean);
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
        UploadApi.uploadFileUrl(this, uploadFilesUrlReq, (code, msg) -> {
            mUploadFileDialog.dismiss();
            Toast.makeText(this, "上传照片成功", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    private void onBack() {
//        imgList = ((ArrayList<UploadImgItemBean>) mGetIntent.getSerializableExtra("imgList"));
//        setResult(RESULT_OK, mGetIntent);
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
                Dialog dialog = LoadingUtils.createLoadingDialog(mContext);
                dialog.show();
                if (!TextUtils.isEmpty(item.local_path)) {
                    Glide.with(mContext).load(new File(item.local_path)).listener(new GlideRequestListener(dialog)).into(holder.img);
                } else {
                    Glide.with(mContext).load(item.s_url).listener(new GlideRequestListener(dialog)).into(holder.img);
                }
                holder.itemView.setOnClickListener(mOnItemClick == null ? null : (View.OnClickListener) v -> mOnItemClick.onItemClick(v, item, holder.cbImg));
                if (isEditing) {
                    holder.cbImg.setVisibility(View.VISIBLE);
                    if (item.hasChoose) {
                        holder.cbImg.setImageResource(R.mipmap.surechoose_icon);
//                        holder.cbImg.setTag(R.id.hasChoose, true);
                    } else {
                        holder.cbImg.setImageResource(R.mipmap.choose_icon);
//                        holder.cbImg.setTag(R.id.hasChoose, false);
                    }
                } else {
                    holder.cbImg.setVisibility(View.GONE);
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

        private class GlideRequestListener implements RequestListener<Drawable> {
            private Dialog dialog;

            public GlideRequestListener(Dialog dialog) {
                this.dialog = dialog;
            }

            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                Toast.makeText(mContext, "图片加载失败", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                dialog.dismiss();
                return false;
            }
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

