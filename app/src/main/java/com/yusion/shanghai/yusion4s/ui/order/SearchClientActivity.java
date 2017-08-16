package com.yusion.shanghai.yusion4s.ui.order;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.bean.order.SearchClientResp;
import com.yusion.shanghai.yusion4s.retrofit.api.OrderApi;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.ui.entrance.OrderItemFragment;
import com.yusion.shanghai.yusion4s.utils.DensityUtil;
import com.yusion.shanghai.yusion4s.widget.RecyclerViewDivider;

import java.util.ArrayList;
import java.util.List;

public class SearchClientActivity extends BaseActivity {
    private EditText et_search;
    private RecyclerView rv_client_info;
    private List<SearchClientResp> items;
    private RecyclerAdapterWithHF adapter;
    private TextView search_info;
    private TextView tv_notice;
    private LinearLayout search_warn_lly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_client);
        initTitleBar(this, "搜索客户");
        initView();
        initEvent();
    }

    private void initView() {
        tv_notice = (TextView) findViewById(R.id.tv_notice);
        search_info = (TextView) findViewById(R.id.search_info);
        et_search = (EditText) findViewById(R.id.et_search);
        rv_client_info = (RecyclerView) findViewById(R.id.rv_client_info);
        rv_client_info.setLayoutManager(new LinearLayoutManager(SearchClientActivity.this));
        rv_client_info.addItemDecoration(new RecyclerViewDivider(SearchClientActivity.this, LinearLayoutManager.VERTICAL, DensityUtil.dip2px(SearchClientActivity.this, 5), ContextCompat.getColor(SearchClientActivity.this, R.color.main_bg)));
        //rv_client_info.addItemDecoration(new RecyclerViewDivider(SearchClientActivity.this,LinearLayoutManager.VERTICAL,DensityUtil.dip2px()));
        items = new ArrayList<>();
        SearchClientAdapter searchClientAdapter = new SearchClientAdapter(SearchClientActivity.this, items);
        adapter = new RecyclerAdapterWithHF(searchClientAdapter);
        rv_client_info.setAdapter(adapter);
        search_warn_lly = (LinearLayout) findViewById(R.id.search_warn_lly);

    }

    private void initEvent() {
//        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == KeyEvent.KEYCODE_SEARCH) {
//                    searchClient();
//                }
//                return false;
//            }
//        });

        search_info.setOnClickListener(new View.OnClickListener() {//点击搜索按钮
            @Override
            public void onClick(View v) {
                searchClient();
            }

        });
//        et_search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                OrderApi.searchClientExist(SearchClientActivity.this, "李拓", new OnItemDataCallBack<List<SearchClientResp>>() {
//                    @Override
//                    public void onItemDataCallBack(List<SearchClientResp> data) {
//                        items.addAll(data);
//                        adapter.notifyDataSetChanged();
//                    }
//                });
//            }
//        });
    }

    private void searchClient() {
        String key = et_search.getText().toString();
        if (key.equals("")) {
            Toast.makeText(SearchClientActivity.this, "请输入用户信息", Toast.LENGTH_LONG).show();
        } else {
            OrderApi.searchClientExist(SearchClientActivity.this, key, new OnItemDataCallBack<List<SearchClientResp>>() {
                @Override
                public void onItemDataCallBack(List<SearchClientResp> data) {
                    if (data.size() != 0) {
                        rv_client_info.setVisibility(View.VISIBLE);
//                        tv_notice.setVisibility(View.GONE);
                        search_warn_lly.setVisibility(View.GONE);
                        items.addAll(data);
                        adapter.notifyDataSetChanged();
                    } else {
//                        tv_notice.setVisibility(View.VISIBLE);
                        search_warn_lly.setVisibility(View.VISIBLE);
                        rv_client_info.setVisibility(View.GONE);
                    }
                }
            });
        }
    }


    public class SearchClientAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private LayoutInflater mLayoutInflater;
        private List<SearchClientResp> mItems;
        private Context mContext;
        //private onItemClick mOnItemClick;

        public SearchClientAdapter(Context context, List<SearchClientResp> items) {
            mContext = context;
            mItems = items;
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new VH(mLayoutInflater.inflate(R.layout.client_info_card, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            VH vh = (VH) holder;
            SearchClientResp item = mItems.get(position);
            vh.client_name.setText(item.clt_nm);
            vh.client_phone.setText(item.mobile);
            vh.client_sfz.setText(item.id_no);
            vh.confirm_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("sfz", item.id_no);
                    intent.putExtra("image1", item.auth_credit.lender);
                    intent.putExtra("image2", item.auth_credit.lender_sp);
                    intent.putExtra("image3", item.auth_credit.guarantor);
                    intent.putExtra("enable", true);
                    SearchClientActivity.this.setResult(2000, intent);
                    SearchClientActivity.this.finish();
                }
            });

//            vh.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(mContext, "点击了整个的item", Toast.LENGTH_LONG).show();
//                }
//            });
        }

        @Override
        public int getItemCount() {
            return mItems == null ? 0 : mItems.size();
        }

        public class VH extends RecyclerView.ViewHolder {
            public TextView client_name;
            public TextView client_phone;
            public TextView client_sfz;
            public TextView confirm_info;

            public VH(View itemView) {
                super(itemView);
                client_name = (TextView) itemView.findViewById(R.id.client_name);
                client_phone = (TextView) itemView.findViewById(R.id.client_phone);
                client_sfz = (TextView) itemView.findViewById(R.id.client_sfz);
                confirm_info = (TextView) itemView.findViewById(R.id.confirm_info);
            }
        }

//        public interface onItemClick {
//            void onItemClick(View v);
//        }
//
//        public void setOnItemClick(onItemClick mOnItemClick) {
//
//            this.mOnItemClick = mOnItemClick;
//        }
    }
}

