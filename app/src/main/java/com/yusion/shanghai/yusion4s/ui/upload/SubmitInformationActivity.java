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
import com.yusion.shanghai.yusion4s.retrofit.api.UploadApi;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.ui.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class SubmitInformationActivity extends BaseActivity {

    private RvAdapter mAdapter;
    private List<ListDealerLabelsResp> lists = new ArrayList<>();
    private String app_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_information);

        initTitleBar(this, "提交资料");
        app_id = getIntent().getStringExtra("app_id");

        RecyclerView rv = (RecyclerView) findViewById(R.id.submit_info_rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RvAdapter(this, lists);
        rv.setAdapter(mAdapter);

        mAdapter.setOnItemClick(new RvAdapter.OnItemClick() {
            @Override
            public void onItemClick(View v, ListDealerLabelsResp item) {
                Intent intent = new Intent();
                intent.setClass(SubmitInformationActivity.this, UploadLabelListActivity.class);
                intent.putExtra("topItem", item);
                intent.putExtra("app_id", app_id);
//                //clt_id取图片
//                intent.putExtra("clt_id", ((UpdateUserInfoActivity) getActivity()).getUserInfoBean().clt_id);
//                intent.putExtra("index", index);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        UploadApi.listDealerLabels(this, app_id, new OnItemDataCallBack<List<ListDealerLabelsResp>>() {
            @Override
            public void onItemDataCallBack(List<ListDealerLabelsResp> data) {
                lists.clear();
                lists.addAll(data);
                mAdapter.notifyDataSetChanged();
            }
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
            boolean hasImgForOneItem = false;
            boolean hasEmptyImgForOneItem = false;
            //ffa400
            for (ListDealerLabelsResp.LabelListBean labelListBean : item.label_list) {
                if (labelListBean.has_img>0) {
                    hasImgForOneItem = true;
                }else {
                    hasEmptyImgForOneItem = true;
                }
                if (labelListBean.has_error > 0) {
                    holder.icon.setVisibility(View.VISIBLE);
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
            if (holder.icon.getVisibility() == View.VISIBLE) {
                holder.status.setVisibility(View.GONE);
            }

            holder.itemView.setOnClickListener(mOnItemClick == null ? null : new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClick.onItemClick(v, item);
                }
            });


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
                name = ((TextView) itemView.findViewById(R.id.upload_label_list_item_name_tv));
                icon = ((ImageView) itemView.findViewById(R.id.upload_label_list_icon_img));
                status = ((TextView) itemView.findViewById(R.id.upload_label_list_item_status_tv));
            }
        }
    }
}
