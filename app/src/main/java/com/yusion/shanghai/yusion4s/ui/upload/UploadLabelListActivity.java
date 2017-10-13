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
import com.yusion.shanghai.yusion4s.base.BaseResult;
import com.yusion.shanghai.yusion4s.bean.upload.ListDealerLabelsResp;
import com.yusion.shanghai.yusion4s.bean.upload.UploadLogReq;
import com.yusion.shanghai.yusion4s.retrofit.Api;
import com.yusion.shanghai.yusion4s.retrofit.api.UploadApi;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnCodeAndMsgCallBack;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadLabelListActivity extends BaseActivity {
    private ListDealerLabelsResp topItem;
    private RvAdapter adapter;
    private UploadLogReq req;
    private String app_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_label_list);

        topItem = ((ListDealerLabelsResp) getIntent().getSerializableExtra("topItem"));
        initTitleBar(this, topItem.name).setRightText("提交").setRightClickListener(v -> onBack()).setRightTextColor(Color.WHITE).setLeftClickListener(v -> onBack());
        app_id = getIntent().getStringExtra("app_id");

        RecyclerView rv = (RecyclerView) findViewById(R.id.upload_label_list_rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UploadLabelListActivity.RvAdapter(this, topItem.label_list);
        rv.setAdapter(adapter);
//        try {
//            JSONArray jsonArray = new JSONArray(Yusion4sApp.getConfigResp().dealer_material);
//            mItems.clear();
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                UploadLabelItemBean uploadLabelItemBean = new Gson().fromJson(jsonObject.toString(), UploadLabelItemBean.class);
//                mItems.add(uploadLabelItemBean);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
        adapter.setOnItemClick(new RvAdapter.OnItemClick() {
            @Override
            public void onItemClick(View v, ListDealerLabelsResp.LabelListBean item, int index) {
                Intent intent = new Intent();
                intent.setClass(UploadLabelListActivity.this, UploadListActivity.class);
                intent.putExtra("topItem", item);
                intent.putExtra("index", index);
                intent.putExtra("app_id", getIntent().getStringExtra("app_id"));
                intent.putExtra("clt_id", item.clt_id);
                startActivityForResult(intent, 100);
            }
        });
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    private void onBack() {
        req = new UploadLogReq();
        req.app_id = app_id;
        if (topItem.name.equals("征信授权书上传")) {
            req.file_label = "FILE_CREDIT_CHOICE";
        } else if (topItem.name.equals("基本资料上传")) {
            req.file_label = "FILE_BASE_LABEL";
        } else if (topItem.name.equals("提车资料上传")) {
            req.file_label = "FILE_PICKUP_CAR_CHOICE";
        } else if (topItem.name.equals("抵押资料上传")) {
            req.file_label = "FILE_MORTGAGE_CHOICE";
        }
        UploadApi.uploadLog(this, req, new OnCodeAndMsgCallBack() {
            @Override
            public void callBack(int code, String msg) {

            }
        });

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
            holder.itemView.setOnClickListener(mOnItemClick == null ? null : new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClick.onItemClick(v, item, position);
                }
            });
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
                name = ((TextView) itemView.findViewById(R.id.upload_label_list_item_name_tv));
                status = ((TextView) itemView.findViewById(R.id.upload_label_list_item_status_tv));
                icon = ((ImageView) itemView.findViewById(R.id.upload_label_list_icon_img));
                mark = ((TextView) itemView.findViewById(R.id.upload_label_list_item_mark_tv));
            }
        }
    }
}