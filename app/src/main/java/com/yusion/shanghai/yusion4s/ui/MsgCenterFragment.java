package com.yusion.shanghai.yusion4s.ui;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.BaseFragment;
import com.yusion.shanghai.yusion4s.bean.msg_center.GetMsgList;
import com.yusion.shanghai.yusion4s.retrofit.Api;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnCodeAndMsgCallBack;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.ui.order.OrderDetailActivity;
import com.yusion.shanghai.yusion4s.utils.ApiUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.yusion.shanghai.yusion4s.R.id.my_order_rv;

/**
 * A simple {@link Fragment} subclass.
 */
public class MsgCenterFragment extends BaseFragment {

    private List<GetMsgList.MsgListBean> items;
    private int page;
    private RecyclerAdapterWithHF adapter;
    private PtrClassicFrameLayout ptr;
    private LinearLayout llyt;
    private String st;
    private RecyclerView rv;
    private TextView order_list_item_update_tv;
    private String vehicle_cond = "新车";
    private MyOrderListAdapter myOrderListAdapter;
    private int current_page = 1;
    private int total_page;
    private TextView msg_count_tv;
    private boolean isFirstShow = true;

    public void setVehicle_cond(String vehicle_cond) {
        this.vehicle_cond = vehicle_cond;
        if (myOrderListAdapter != null) {
            myOrderListAdapter.setVehicle_cond(vehicle_cond);
            myOrderListAdapter.notifyDataSetChanged();
        }
    }

    public static MsgCenterFragment newInstance() {

        Bundle args = new Bundle();
        MsgCenterFragment fragment = new MsgCenterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 5:
                    ViewCompat.animate(msg_count_tv).translationY(120).setDuration(1000).start();
//                   msg_count_tv.setVisibility(View.GONE);

//                ViewCompat.animate(msg_count_tv).translationY(0).setDuration(4000).alpha(1).start();
                    // ViewCompat.animate(autonym_certify_warnning_lin).translationY(-autonym_certify_warnning_lin.getHeight() * 1.0f).setDuration(1000).start();
                    break;
                case 6:
                    // msg_count_tv.setVisibility(View.GONE);
                    ViewCompat.animate(msg_count_tv).translationY(0 - msg_count_tv.getHeight()).setDuration(1000).start();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_info_item2, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTitleBar(view, "订单消息");
        st = getArguments().getString("st");
        vehicle_cond = getArguments().getString("vehicle_cond");
        llyt = view.findViewById(R.id.my_order_llyt);
        rv = view.findViewById(my_order_rv);
        msg_count_tv = view.findViewById(R.id.msg_count_tv);
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        rv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = (int) getResources().getDimension(R.dimen.y75);
            }
        });
        // rv.addItemDecoration(new RecyclerViewDivider(mContext, LinearLayoutManager.VERTICAL, DensityUtil.dip2px(getActivity(), 10), ContextCompat.getColor(getActivity(), R.color.main_bg)));
        items = new ArrayList<>();
        myOrderListAdapter = new MyOrderListAdapter(mContext, items);
        myOrderListAdapter.setVehicle_cond(vehicle_cond);
        adapter = new RecyclerAdapterWithHF(myOrderListAdapter);
        rv.setAdapter(adapter);
        ptr = view.findViewById(R.id.my_order_ptr);
//        ptr.autoRefresh(true);

        ptr.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                refresh();
            }

            //改掉RecyclerView 吃 item 问题。
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, rv, header);
            }
        });
        //ptr.setLoadMoreEnable(true);
        ptr.setOnLoadMoreListener(() -> {
            if (page < total_page) {
                ApiUtil.requestUrl4Data(mContext, Api.getMsgCenterService().getMessageList(++page), (OnItemDataCallBack<GetMsgList>) resp -> {
                    //  ApiUtil.requestUrl4Data(mContext, Api.getOrderService().getAppList(st, vehicle_cond, ++page), (OnItemDataCallBack<GetAppListResp>) resp -> {
                    if (resp != null) {
                        if (resp.total_page == 0 || resp.total_page == 1) {
                            ptr.setLoadMoreEnable(false);
                        }
                        for (GetMsgList.MsgListBean msgListBean : resp.msg_list) {
                            items.add(msgListBean);
                            ptr.setVisibility(View.VISIBLE);
                            rv.setVisibility(View.VISIBLE);
                            llyt.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                            ptr.loadMoreComplete(true);
                        }
                    }
                });
            } else {
                ptr.loadMoreComplete(false);
                msg_count_tv.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        //// TODO: 2018/1/16  如果是true 则是当前影藏 如果是false 则当前显示
        super.onHiddenChanged(hidden);
        if (!hidden) {
            rv.scrollToPosition(0);
            refresh();
            ptr.autoRefresh(true);
        }
    }

    public void refresh() {
        page = 1;
        ApiUtil.requestUrl4Data(mContext, Api.getMsgCenterService().getMessageList(page), (OnItemDataCallBack<GetMsgList>) resp -> {
            //ApiUtil.requestUrl4Data(mContext, Api.getOrderService().getAppList(st, vehicle_cond, page), (OnItemDataCallBack<GetAppListResp>) resp -> {
            if (resp != null) {
                ApiUtil.requestUrl4CodeAndMsg(mContext, Api.getMsgCenterService().clearRedPoint(), true, new OnCodeAndMsgCallBack() {
                    @Override
                    public void callBack(int code, String msg) {
                        ((MainActivity) getActivity()).red_point.setVisibility(View.GONE);
                    }
                });
                if (resp.total_page == 0 || resp.total_page == 1) {
                    ptr.setLoadMoreEnable(false);
                } else {
                    total_page = resp.total_page;
                }
                if (resp.new_msg_count > 0) {
                    msg_count_tv.setVisibility(View.VISIBLE);
                    msg_count_tv.setText("为您更新了" + resp.new_msg_count + "条信息");
                    msg_count_tv.post(() -> {
                        if (handler.hasMessages(5)) {
                            handler.removeMessages(5);
                        }
                        if (handler.hasMessages(6)) {
                            handler.removeMessages(6);
                        }
                        handler.sendEmptyMessageDelayed(5, 0);
                        handler.sendEmptyMessageDelayed(6, 2500);
                    });
                }
                if (resp.msg_list.size() > 0) {
                    ptr.setVisibility(View.VISIBLE);
                    rv.setVisibility(View.VISIBLE);
                    llyt.setVisibility(View.GONE);
                    items.clear();
                    items.addAll(resp.msg_list);
                    Log.e("TAG", "refresh: items = " + items.size());
                    adapter.notifyDataSetChanged();
                    ptr.refreshComplete();
                    ptr.setLoadMoreEnable(true);
                } else {
                    ptr.refreshComplete();
                    msg_count_tv.setVisibility(View.GONE);
                    rv.setVisibility(View.GONE);
                    llyt.setVisibility(View.VISIBLE);
                    ptr.setVisibility(View.VISIBLE);
                }
            }

        });
    }

    static class MyOrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private LayoutInflater mLayoutInflater;
        private Context mContext;
        private OnItemClick mOnItemClick;
        private List<GetMsgList.MsgListBean> mItems;
        private String vehicle_cond;

        public void setVehicle_cond(String vehicle_cond) {
            this.vehicle_cond = vehicle_cond;
        }

        public MyOrderListAdapter(Context context, List<GetMsgList.MsgListBean> items) {
            mContext = context;
            mLayoutInflater = LayoutInflater.from(mContext);
            mItems = items;
            Log.e("TAG", "Adapter: items = " + mItems.size());
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new VH(mLayoutInflater.inflate(R.layout.info_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            VH vh = (VH) holder;
            GetMsgList.MsgListBean item = mItems.get(position);
//            GetMsgList.MsgListBean beforeitem = position == 0 ? null : mItems.get(position - 1);
//            GetMsgList.MsgListBean afteritem = position == mItems.size() - 1 ? null : mItems.get(position + 1);

            vh.look_detail_rel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, OrderDetailActivity.class);
                    intent.putExtra("app_id", item.app_id);
                    mContext.startActivity(intent);
                }
            });

            Date date = new Date(item.msg_ts);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            vh.ceceive_time.setText(format.format(date));
            vh.time.setText(item.app_up_ts);
            vh.state_tv.setText(item.summary);
            vh.user_info_tv.setText(item.detail.clt_info);
            vh.sell_person_tv.setText(item.detail.dealer);
            vh.model_tv.setText(item.detail.vehicle);
            vh.total_loan_tv.setText(item.detail.loan_amt);
            vh.state_tv.setTextColor(Color.parseColor(item.color));
        }

        @Override
        public int getItemCount() {
            int size = mItems.size();
            return mItems == null ? 0 : size;
        }

        protected class VH extends RecyclerView.ViewHolder {

            public TextView ceceive_time;
            public TextView state_tv;
            public TextView user_info_tv;
            public TextView sell_person_tv;
            public TextView second_sell_tv;
            public TextView model_tv;
            public TextView total_loan_tv;
            public RelativeLayout look_detail_rel;
            public TextView time;

            public VH(View itemView) {
                super(itemView);
                time = itemView.findViewById(R.id.time_tv);
                ceceive_time = itemView.findViewById(R.id.ceceive_time);
                state_tv = itemView.findViewById(R.id.state_tv);
                user_info_tv = itemView.findViewById(R.id.user_info_tv);
                sell_person_tv = itemView.findViewById(R.id.sell_person_tv);
                //second_sell_tv = itemView.findViewById(R.id.second_sell_tv);
                model_tv = itemView.findViewById(R.id.model_tv);
                total_loan_tv = itemView.findViewById(R.id.total_loan_tv);
                look_detail_rel = itemView.findViewById(R.id.look_detail_rel);
            }
        }

        public interface OnItemClick {
            void onItemClick(View v);
        }

        public void setOnItemClick(OnItemClick mOnItemClick) {
            this.mOnItemClick = mOnItemClick;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
