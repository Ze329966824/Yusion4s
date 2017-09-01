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

import java.util.List;

public class UploadLabelListActivity extends BaseActivity {
    private ListDealerLabelsResp topItem;
    private RvAdapter adapter;

//    public static List<UploadFilesUrlReq.FileUrlBean> uploadFileUrlBeanList = new ArrayList<>();
//    private List<UploadLabelItemBean> mItems = new ArrayList<>();
//    private UploadLabelListAdapter adapter;
//
//
//    public static void start(Context context, String app_id) {
//        Intent intent = new Intent(context, UploadLabelListActivity.class);
//        intent.putExtra("app_id", app_id);
//        context.startActivity(intent);
//    }
//
//    private Dialog mUploadFileDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_label_list);
//        uploadFileUrlBeanList.clear();

        topItem = ((ListDealerLabelsResp) getIntent().getSerializableExtra("topItem"));
        initTitleBar(this, topItem.name).setRightText("提交").setRightClickListener(v -> onBack()).setRightTextColor(Color.WHITE).setLeftClickListener(v -> onBack());

        RecyclerView rv = (RecyclerView) findViewById(R.id.upload_label_list_rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UploadLabelListActivity.RvAdapter(this, topItem.label_list);
        rv.setAdapter(adapter);
//        try {
//            JSONArray jsonArray = new JSONArray(Yusion4sApp.CONFIG_RESP.dealer_material);
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
                startActivityForResult(intent,100);
            }
        });

//        initData();
    }

    private void initData() {
//        ListLabelsErrorReq req = new ListLabelsErrorReq();
//        req.app_id = getIntent().getStringExtra("app_id");
//        ArrayList<String> labelsList = new ArrayList<>();
//        for (UploadLabelItemBean itemBean : mItems) {
//            labelsList.add(itemBean.value);
//        }
//        req.label_list = labelsList;
//        UploadApi.listLabelsError(this, req, new OnItemDataCallBack<ListLabelsErrorResp>() {
//            @Override
//            public void onItemDataCallBack(ListLabelsErrorResp resp) {
//                for (UploadLabelItemBean item : mItems) {
//                    for (String errLabel : resp.lender.err_labels) {
//                        if (item.value.equals(errLabel)) {
//                            item.hasError = true;
//                        }
//                    }
//                }
//                adapter.notifyDataSetChanged();
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    private void onBack() {
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