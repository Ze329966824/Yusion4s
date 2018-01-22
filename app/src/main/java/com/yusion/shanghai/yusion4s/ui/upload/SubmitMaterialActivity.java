package com.yusion.shanghai.yusion4s.ui.upload;

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
import com.yusion.shanghai.yusion4s.retrofit.Api;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.utils.ApiUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 提交资料页面
 */
public class SubmitMaterialActivity extends BaseActivity {

    private RvAdapter mAdapter;
    private List<ListDealerLabelsResp> lists = new ArrayList<>();
    private String app_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_information);
        initView();
        initData();
    }

    private void initData() {
        initTitleBar(this, "提交资料");
        app_id = getIntent().getStringExtra("app_id");
    }

    private void initView() {
        RecyclerView rv = findViewById(R.id.submit_info_rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RvAdapter(this, lists);
        rv.setAdapter(mAdapter);

        mAdapter.setOnItemClick((v, item) -> onLabelClick(item));
    }

    private void onLabelClick(ListDealerLabelsResp item) {
        Intent intent = new Intent();
        intent.setClass(SubmitMaterialActivity.this, UploadLabelListActivity.class);
        intent.putExtra("topItem", item);
        intent.putExtra("app_id", app_id);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //每次回到该页面都要获取最新数据
        ApiUtil.requestUrl4Data(this, Api.getUploadService().listDealerLabels(app_id),
                (OnItemDataCallBack<List<ListDealerLabelsResp>>) listDealerLabelsResp -> {
                    lists.clear();
                    lists.addAll(listDealerLabelsResp);
                    mAdapter.notifyDataSetChanged();
                });
    }

    public static class RvAdapter extends RecyclerView.Adapter<RvAdapter.VH> {

        private LayoutInflater mLayoutInflater;
        private List<ListDealerLabelsResp> mItems;
        private Context mContext;
        private OnItemClick mOnItemClick;

        public RvAdapter(Context context, List<ListDealerLabelsResp> items) {
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
            ListDealerLabelsResp item = mItems.get(position);
            holder.name.setText(item.name);
            holder.icon.setVisibility(View.GONE);
            holder.status.setVisibility(View.VISIBLE);
            setLabelStatus(holder, item);
            holder.itemView.setOnClickListener(mOnItemClick == null ? null : (View.OnClickListener) v -> mOnItemClick.onItemClick(v, item));
        }

        /**
         * 设置label的状态 是否有错误,待完善,请上传,已上传
         */
        private void setLabelStatus(VH holder, ListDealerLabelsResp item) {
            boolean hasImgForOneItem = false;
            boolean hasEmptyImgForOneItem = false;
            for (ListDealerLabelsResp.LabelListBean labelListBean : item.label_list) {
                if (labelListBean.has_img > 0) {
                    hasImgForOneItem = true;
                } else {
                    hasEmptyImgForOneItem = true;
                }
                if (labelListBean.has_error > 0) {
                    holder.icon.setVisibility(View.VISIBLE);
                    holder.status.setVisibility(View.GONE);
                }
            }
            holder.status.setText("");
            if (hasEmptyImgForOneItem && hasImgForOneItem) {
                holder.status.setText("待完善");
                holder.status.setTextColor(Color.parseColor("#ffa400"));
            }
            if (!hasImgForOneItem) {
                holder.status.setText("请上传");
                holder.status.setTextColor(mContext.getResources().getColor(R.color.please_upload_color));
            }
            if (!hasEmptyImgForOneItem) {
                holder.status.setText("已上传");
                holder.status.setTextColor(mContext.getResources().getColor(R.color.system_color));
            }
        }

        @Override
        public int getItemCount() {
            return mItems == null ? 0 : mItems.size();
        }

        public interface OnItemClick {
            void onItemClick(View v, ListDealerLabelsResp item);
        }

        public void setOnItemClick(OnItemClick mOnItemClick) {
            this.mOnItemClick = mOnItemClick;
        }

        public class VH extends RecyclerView.ViewHolder {

            public TextView name;
            public ImageView icon;
            public TextView status;

            public VH(View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.upload_label_list_item_name_tv);
                icon = itemView.findViewById(R.id.upload_label_list_icon_img);
                status = itemView.findViewById(R.id.upload_label_list_item_status_tv);
            }
        }
    }
}
