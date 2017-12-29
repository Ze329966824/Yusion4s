package com.yusion.shanghai.yusion4s.ui.upload;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.bean.upload.ListDealerLabelsResp;
import com.yusion.shanghai.yusion4s.bean.upload.UploadLogReq;
import com.yusion.shanghai.yusion4s.retrofit.api.UploadApi;
import com.yusion.shanghai.yusion4s.utils.PopupDialogUtil;

import java.util.List;

/**
 * 该页面有两种可能进入 1.提交二手车订单成功后提交影像件 2.提交资料页面点击label
 * 该页面和UploadListActivity联动
 */
public class UploadLabelListActivity extends BaseActivity {
    private ListDealerLabelsResp topItem;
    private RvAdapter adapter;
    private String app_id;
    private boolean shouldUploadLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_label_list);

        topItem = ((ListDealerLabelsResp) getIntent().getSerializableExtra("topItem"));
        app_id = getIntent().getStringExtra("app_id");


        initTitleBar(this, topItem.name).setRightText("提交").setRightClickListener(v -> {
            uploadAndFinish(false);

        });

        RecyclerView rv = findViewById(R.id.upload_label_list_rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UploadLabelListActivity.RvAdapter(this, topItem.label_list);
        rv.setAdapter(adapter);
        adapter.setOnItemClick((v, item, index) -> onLabelClick(item, index));
    }

    private void onLabelClick(ListDealerLabelsResp.LabelListBean item, int index) {
        Intent intent = new Intent();
        intent.setClass(UploadLabelListActivity.this, ExtraUploadListActivity.class);

        intent.putExtra("topItem", item);
        intent.putExtra("index", index);
        intent.putExtra("app_id", app_id);
        intent.putExtra("clt_id", item.clt_id);
        startActivityForResult(intent, 100);
    }

    @Override
    public void onBackPressed() {
        uploadAndFinish(true);
    }


    private void uploadAndFinish(boolean isShowDialog) {
        //检查是否需要向服务器打log
        shouldUploadLog = false;
        for (ListDealerLabelsResp.LabelListBean labelListBean : topItem.label_list) {
            if (labelListBean.has_change) {
                shouldUploadLog = true;
            }
        }
        if (shouldUploadLog) {
            if (isShowDialog) {
                //左上角或返回键
                PopupDialogUtil.showOneButtonDialog(this, "您已修改了影像件，请点击提交按钮", "知道了", dialog -> {
                    dialog.dismiss();
                });
            } else {
                //提交
                UploadLogReq req;
                req = new UploadLogReq();
                req.app_id = app_id;
                req.file_name = topItem.name;
                req.file_value = topItem.value;
                UploadApi.uploadLog(this, req, (code, msg) -> {
                });
                toSubmitMaterial();
            }
        }else {
            toSubmitMaterial();
        }


    }

    private void toSubmitMaterial() {
        Intent intent = new Intent(this, SubmitMaterialActivity.class);
        intent.putExtra("app_id", app_id);
        startActivity(intent);
        finish();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                ListDealerLabelsResp.LabelListBean item = (ListDealerLabelsResp.LabelListBean) data.getSerializableExtra("topItem");
                topItem.label_list.set(data.getIntExtra("index", -1), item);
                adapter.notifyDataSetChanged();
            }
        }
    }


    public static class RvAdapter extends RecyclerView.Adapter<RvAdapter.VH> {

        private LayoutInflater mLayoutInflater;
        private List<ListDealerLabelsResp.LabelListBean> mItems;
        private Context mContext;
        private OnItemClick mOnItemClick;

        public RvAdapter(Context context, List<ListDealerLabelsResp.LabelListBean> items) {
            mItems = items;
            mContext = context;
            mLayoutInflater = LayoutInflater.from(mContext);
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new VH(mLayoutInflater.inflate(R.layout.upload_label_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {

            ListDealerLabelsResp.LabelListBean item = mItems.get(position);
            holder.name.setText(item.name);
            setLabelStatus(holder, item);

            holder.itemView.setOnClickListener(mOnItemClick == null ? null : (View.OnClickListener) v -> mOnItemClick.onItemClick(v, item, position));

        }

        private void setLabelStatus(VH holder, ListDealerLabelsResp.LabelListBean item) {
            holder.status.setVisibility(View.VISIBLE);
            if (item.has_img > 0) {
                holder.status.setText("已上传");
                holder.status.setTextColor(mContext.getResources().getColor(R.color.system_color));
            } else {
                holder.status.setText("请上传");
                holder.status.setTextColor(mContext.getResources().getColor(R.color.please_upload_color));
            }
            holder.icon.setVisibility(View.GONE);
            if (item.has_error > 0) {
                holder.status.setVisibility(View.GONE);
                holder.icon.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            return mItems == null ? 0 : mItems.size();
        }

        public interface OnItemClick {
            void onItemClick(View v, ListDealerLabelsResp.LabelListBean item, int index);
        }

        public void setOnItemClick(OnItemClick mOnItemClick) {
            this.mOnItemClick = mOnItemClick;
        }

        public class VH extends RecyclerView.ViewHolder {

            public TextView name;
            public TextView status;
            public ImageView icon;

            public VH(View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.upload_label_list_item_name_tv);
                status = itemView.findViewById(R.id.upload_label_list_item_status_tv);
                icon = itemView.findViewById(R.id.upload_label_list_icon_img);
            }
        }
    }
}