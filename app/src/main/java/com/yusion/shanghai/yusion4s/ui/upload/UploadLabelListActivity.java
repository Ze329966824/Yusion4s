package com.yusion.shanghai.yusion4s.ui.upload;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.List;

/**
 * 该页面和UploadListActivity联动
 */
public class UploadLabelListActivity extends BaseActivity {
    private ListDealerLabelsResp topItem;
    private RvAdapter adapter;
    private String app_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_label_list);

        topItem = ((ListDealerLabelsResp) getIntent().getSerializableExtra("topItem"));
        app_id = getIntent().getStringExtra("app_id");

        initTitleBar(this, topItem.name).setRightText("提交").setLeftClickListener(v -> onBack()).setRightClickListener(v -> submitLog()).setRightTextColor(Color.WHITE);

        RecyclerView rv = findViewById(R.id.upload_label_list_rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UploadLabelListActivity.RvAdapter(this, topItem.label_list);
        rv.setAdapter(adapter);
        adapter.setOnItemClick((v, item, index) -> {
            Intent intent = new Intent();
            intent.setClass(UploadLabelListActivity.this, ExtraUploadListActivity.class);

            intent.putExtra("topItem", item);
            intent.putExtra("index", index);
            intent.putExtra("app_id", getIntent().getStringExtra("app_id"));
            intent.putExtra("clt_id", item.clt_id);
            startActivityForResult(intent, 100);
        });
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    private void onBack() {
        startActivity(new Intent(this,SubmitInformationActivity.class));
        finish();
    }

    private void submitLog() {
        UploadLogReq req;
        req = new UploadLogReq();
        req.app_id = app_id;
        req.file_name = topItem.name;
        req.file_value = topItem.value;
        UploadApi.uploadLog(this, req, (code, msg) -> finish());
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
            holder.itemView.setOnClickListener(mOnItemClick == null ? null : (View.OnClickListener) v -> mOnItemClick.onItemClick(v, item, position));

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
            public TextView mark;
            public ImageView icon;

            public VH(View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.upload_label_list_item_name_tv);
                status = itemView.findViewById(R.id.upload_label_list_item_status_tv);
                icon = itemView.findViewById(R.id.upload_label_list_icon_img);
                mark = itemView.findViewById(R.id.upload_label_list_item_mark_tv);
            }
        }
    }
}