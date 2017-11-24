package com.yusion.shanghai.yusion4s.ui.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.yusion.shanghai.yusion4s.ubt.UBT;
import com.yusion.shanghai.yusion4s.ubt.annotate.BindView;
import com.yusion.shanghai.yusion4s.utils.DensityUtil;
import com.yusion.shanghai.yusion4s.widget.RecyclerViewDivider;

import java.util.ArrayList;
import java.util.List;

public class SearchClientActivity extends BaseActivity {

    @BindView(id = R.id.et_search, widgetName = "et_search")
    private EditText et_search;

    private RecyclerView rv_client_info;
    private List<SearchClientResp> items;
    private RecyclerAdapterWithHF adapter;

    @BindView(id = R.id.search_info, widgetName = "search_info", onClick = "searchClient")
    private Button search_info;

    private TextView tv_notice;

    private LinearLayout search_warn_lly;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_client);
        UBT.bind(this);

        search_info.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    v.clearFocus();

                    String key = et_search.getText().toString();
                    if (key.equals("")) {
                        Toast.makeText(SearchClientActivity.this, "搜索内容不能为空", Toast.LENGTH_LONG).show();
                    } else {
                        OrderApi.searchClientExist(SearchClientActivity.this, key, new OnItemDataCallBack<List<SearchClientResp>>() {
                            @Override
                            public void onItemDataCallBack(List<SearchClientResp> data) {
                                if (data.size() != 0) {
                                    Log.e("TAG", " searchClientExist : "+data.size());
                                    items.clear();
                                    rv_client_info.setVisibility(View.VISIBLE);
                                    search_warn_lly.setVisibility(View.GONE);
                                    items.addAll(data);
                                    adapter.notifyDataSetChanged();
                                } else {
                                    search_warn_lly.setVisibility(View.VISIBLE);
                                    rv_client_info.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                }
            }
        });

        initTitleBar(this, "搜索客户");
        initView();

//        initEvent();
    }

    private void initView() {
        tv_notice = (TextView) findViewById(R.id.tv_notice);
        // search_info = (TextView) findViewById(R.id.search_info);
        // et_search = (EditText) findViewById(R.id.et_search);
        rv_client_info = (RecyclerView) findViewById(R.id.rv_client_info);
        rv_client_info.setLayoutManager(new LinearLayoutManager(SearchClientActivity.this));
        rv_client_info.addItemDecoration(new RecyclerViewDivider(SearchClientActivity.this, LinearLayoutManager.VERTICAL, DensityUtil.dip2px(SearchClientActivity.this, 5), ContextCompat.getColor(SearchClientActivity.this, R.color.main_bg)));
        items = new ArrayList<>();
        SearchClientAdapter searchClientAdapter = new SearchClientAdapter(SearchClientActivity.this, items);
        adapter = new RecyclerAdapterWithHF(searchClientAdapter);
        rv_client_info.setAdapter(adapter);
        search_warn_lly = (LinearLayout) findViewById(R.id.search_warn_lly);

    }

//    private void initEvent() {
//
//        search_info.setOnClickListener(new View.OnClickListener() {//点击搜索按钮
//            @Override
//            public void onClick(View v) {
//                searchClient();
//            }
//
//        });
//    }

    private void searchClient(View view) {
        search_info.setFocusable(true);
        search_info.setFocusableInTouchMode(true);
        search_info.requestFocus();
        search_info.requestFocusFromTouch();


//        String key = et_search.getText().toString();
//        if (key.equals("")) {
//            Toast.makeText(SearchClientActivity.this, "搜索内容不能为空", Toast.LENGTH_LONG).show();
//        } else {
//            OrderApi.searchClientExist(SearchClientActivity.this, key, new OnItemDataCallBack<List<SearchClientResp>>() {
//                @Override
//                public void onItemDataCallBack(List<SearchClientResp> data) {
//                    if (data.size() != 0) {
//                        items.clear();
//                        rv_client_info.setVisibility(View.VISIBLE);
//                        search_warn_lly.setVisibility(View.GONE);
//                        items.addAll(data);
//                        adapter.notifyDataSetChanged();
//                    } else {
//                        search_warn_lly.setVisibility(View.VISIBLE);
//                        rv_client_info.setVisibility(View.GONE);
//                    }
//                }
//            });
//        }
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

                    intent.putExtra("name", item.clt_nm);
                    intent.putExtra("sfz", item.id_no);
                    intent.putExtra("mobile", item.mobile);

                    checkAuthCreditExist(intent, item);

                    intent.putExtra("enable", true);
                    SearchClientActivity.this.setResult(RESULT_OK, intent);
                    SearchClientActivity.this.finish();
                }
            });

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

    private void checkAuthCreditExist(Intent intent, SearchClientResp item) {
        if (item.auth_credit.lender != null) {//如果不等于空
            intent.putExtra("isHasLender", "1");
            intent.putExtra("lender_clt_id", item.auth_credit.lender.clt_id);
            intent.putExtra("lender", item.auth_credit.lender.auth_credit_img_count);
        } else {
            intent.putExtra("isHasLender", "2");
        }
        if (item.auth_credit.lender_sp != null) {//如果不等于空
            intent.putExtra("isHasLender_sp", "1");
            intent.putExtra("lender_sp_clt_id", item.auth_credit.lender_sp.clt_id);
            intent.putExtra("lender_sp", item.auth_credit.lender_sp.auth_credit_img_count);
        } else {
            intent.putExtra("isHasLender_sp", "2");
        }

        if (item.auth_credit.guarantor != null) {//如果不等于空
            intent.putExtra("isGuarantor", "1");
            intent.putExtra("guarantor_clt_id", item.auth_credit.guarantor.clt_id);
            intent.putExtra("guarantor", item.auth_credit.guarantor.auth_credit_img_count);
        } else {
            intent.putExtra("isGuarantor", "2");
        }

        if (item.auth_credit.guarantor_sp != null) {//如果不等于空
            intent.putExtra("isGuarantor_sp", "1");
            intent.putExtra("guarantor_sp_clt_id", item.auth_credit.guarantor_sp.clt_id);
            intent.putExtra("guarantor_sp", item.auth_credit.guarantor_sp.auth_credit_img_count);

        } else {
            intent.putExtra("isGuarantor_sp", "2");
        }

    }

}

