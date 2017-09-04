package com.yusion.shanghai.yusion4s.ui.entrance;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.BaseFragment;
import com.yusion.shanghai.yusion4s.bean.order.GetAppListResp;
import com.yusion.shanghai.yusion4s.event.OrderItemFragmentEvent;
import com.yusion.shanghai.yusion4s.retrofit.api.OrderApi;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.ui.order.OrderDetailActivity;
import com.yusion.shanghai.yusion4s.utils.DensityUtil;
import com.yusion.shanghai.yusion4s.widget.RecyclerViewDivider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderItemFragment extends BaseFragment {

    private Handler handler = new Handler();
    private List<GetAppListResp> items;
    private int page;
    private RecyclerAdapterWithHF adapter;
    private PtrClassicFrameLayout ptr;
    private LinearLayout llyt;
    private String st;

    public static OrderItemFragment newInstance(String s) {

        Bundle args = new Bundle();
        args.putString("st", s);
        OrderItemFragment fragment = new OrderItemFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_order_item, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        llyt = (LinearLayout) view.findViewById(R.id.my_order_llyt);
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.my_order_rv);
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        rv.addItemDecoration(new RecyclerViewDivider(mContext, LinearLayoutManager.VERTICAL, DensityUtil.dip2px(getActivity(), 10), ContextCompat.getColor(getActivity(), R.color.main_bg)));
        items = new ArrayList<>();
        MyOrderListAdapter myOrderListAdapter = new MyOrderListAdapter(mContext, items, getArguments().getString("st"));
        adapter = new RecyclerAdapterWithHF(myOrderListAdapter);
        rv.setAdapter(adapter);

        ptr = (PtrClassicFrameLayout) view.findViewById(R.id.my_order_ptr);
        ptr.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                items.clear();
                initData(OrderItemFragmentEvent.refresh);
            }
        });
        // st=getArguments().getString("st");
        initData(OrderItemFragmentEvent.refresh);

//        ptr.setLoadMoreEnable(true);
//        ptr.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void loadMore() {
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        OrderItemFragment.this.adapter.notifyDataSetChanged();
//                        ptr.loadMoreComplete(true);
//                        page++;
//                    }
//                }, 1000);
//            }
//        });
    }

    @Subscribe
    public void initData(OrderItemFragmentEvent event) {
        switch (event) {
            case refresh:
//                OrderApi.getAppList(mContext,  getArguments().getString("st"), new OnDataCallBack<List<GetAppListResp>>() {
//                    @Override
//                    public void callBack(List<GetAppListResp> resp) {
//                        //if (resp.size() != 0) {
//                        ptr.setVisibility(View.VISIBLE);
//                        //llyt.setVisibility(View.GONE);
//                        items.addAll(resp);
//                        adapter.notifyDataSetChanged();
//                        ptr.refreshComplete();
//                        //   }
////                        else {//添加空的view
////                            // llyt.setVisibility(View.VISIBLE);
////                            //ptr.setVisibility(View.GONE);
////                        }
//                    }
//                });
                OrderApi.getAppList(mContext, getArguments().getString("st"), new OnItemDataCallBack<List<GetAppListResp>>() {
                    @Override
                    public void onItemDataCallBack(List<GetAppListResp> resp) {
                        if (resp != null && resp.size() > 0) {
                            ptr.setVisibility(View.VISIBLE);
                            items.addAll(resp);
                            adapter.notifyDataSetChanged();
                            ptr.refreshComplete();
                        }
                    }
                });
                break;
        }
        /*OrderApi.getAppList(mContext, "正在获取订单列表", getArguments().getString("st"), new OnDataCallBack<List<GetAppListResp>>() {
            @Override
            public void callBack(List<GetAppListResp> resp) {
                if (resp.size() == 0) {
                    // llyt.setVisibility(View.VISIBLE);
                    // ptr.setVisibility(View.GONE);
                } else {
                    ptr.setVisibility(View.VISIBLE);
                    // llyt.setVisibility(View.GONE);
                    items.addAll(resp);
                    adapter.notifyDataSetChanged();
                    ptr.refreshComplete();
                }
            }
        });*/
    }

    static class MyOrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private LayoutInflater mLayoutInflater;
        private Context mContext;
        private OnItemClick mOnItemClick;
        private List<GetAppListResp> mItems;
        private String st;

        public MyOrderListAdapter(Context context, List<GetAppListResp> items, String st1) {
            mContext = context;
            mLayoutInflater = LayoutInflater.from(mContext);
            mItems = items;
            st = st1;
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new VH(mLayoutInflater.inflate(R.layout.order_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            VH vh = (VH) holder;
            GetAppListResp item = mItems.get(position);
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, OrderDetailActivity.class);
                    intent.putExtra("app_id", item.app_id);
                    mContext.startActivity(intent);
                }
            });
            vh.name.setText(item.clt_nm);
            vh.door.setText(item.dlr_nm);
            vh.brand.setText(item.brand);
            vh.model.setText(item.model_name);
            vh.trix.setText(item.trix);
            vh.time.setText(item.app_ts);
            vh.phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + item.mobile));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            });
            if (item.status_st == 2) {//待审核
                vh.st.setTextColor(Color.parseColor("#FFA400"));
            } else if (item.status_st == 3) {//审核失败
                vh.st.setTextColor(Color.parseColor("#FF3F00"));
            } else if (item.status_st == 4) {//待确认金融方案
                vh.st.setTextColor(Color.parseColor("#FFA400"));
            } else if (item.status_st == 6) {//放款中
                vh.st.setTextColor(Color.parseColor("#06B7A3"));
            } else if (item.status_st == 9) {//已取消
                vh.st.setTextColor(Color.parseColor("#666666"));
            }
            vh.st.setText(item.status_code);
            vh.periods.setText(item.nper);
            vh.loan.setText(item.loan_amt);
            vh.btns.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Window window = ((Activity) mContext).getWindow();
                    WindowManager.LayoutParams lp = window.getAttributes();
                    lp.alpha = 0.5f;
                    window.setAttributes(lp);

                    PopupWindow popupWindow = new PopupWindow(mContext);
                    popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                    popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                    View contentView = LayoutInflater.from(mContext).inflate(R.layout.btns_group, null);
                    ((Button) contentView.findViewById(R.id.btn1)).setOnClickListener(v1 -> {
//                        UploadLabelListActivity.start(mContext, item.app_id);
                        popupWindow.dismiss();
                    });
                    /**
                     * 添加取消订单的功能，暂时用popupWindow.dismiss 替代
                     */
                    contentView.findViewById(R.id.btn2).setOnClickListener(v2 -> {
                        //popupWindow.dismiss();
//                        OrderApi.getCancelInfo(mContext, "取消订单", new CancelOrderReq(item.app_id), new OnDataCallBack<CancleInfo>() {
//                            @Override
//                            public void callBack(CancleInfo resp) {
//                                Log.e("----msg---",resp.toString());
//                                //String msg = resp.getMsg();
//                                //Log.e("---msg---", msg);
//                            }
//                        });
//                        OrderApi.getCancelInfo(mContext, "取消订单", new CancelOrderReq(item.app_id), new OnDataCallBack() {
//                            @Override
//                            public void callBack(Object resp) {
//                                //eventbus 发送请求
//                                EventBus.getDefault().post(OrderItemFragmentEvent.refresh);
//                            }
//                        });
                        popupWindow.dismiss();

                    });
                    popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            lp.alpha = 1f;
                            window.setAttributes(lp);
                        }
                    });
                    popupWindow.setContentView(contentView);
                    popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
                    popupWindow.setOutsideTouchable(true);
                    popupWindow.setFocusable(true);
                    popupWindow.showAsDropDown(v);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mItems == null ? 0 : mItems.size();
        }

        protected class VH extends RecyclerView.ViewHolder {

            public ImageView btns;
            public TextView name;
            public TextView st;
            public TextView door;
            public TextView time;
            public TextView model;
            public TextView brand;
            public TextView trix;
            public TextView loan;
            public TextView periods;
            public ImageView phone;

            public VH(View itemView) {
                super(itemView);
                btns = ((ImageView) itemView.findViewById(R.id.order_list_item_btns_img));
                name = ((TextView) itemView.findViewById(R.id.order_list_item_name_tv));
                st = ((TextView) itemView.findViewById(R.id.order_list_item_st_tv));
                door = ((TextView) itemView.findViewById(R.id.order_list_item_door_tv));
                time = ((TextView) itemView.findViewById(R.id.order_list_item_time_tv));
                model = ((TextView) itemView.findViewById(R.id.order_list_item_model_tv));
                brand = ((TextView) itemView.findViewById(R.id.order_item_brand));
                trix = ((TextView) itemView.findViewById(R.id.order_list_item_trix_tv));
                loan = ((TextView) itemView.findViewById(R.id.order_list_item_total_loan_tv));
                periods = ((TextView) itemView.findViewById(R.id.order_list_item_periods_tv));
                phone = ((ImageView) itemView.findViewById(R.id.order_list_item_phone_img));
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
        EventBus.getDefault().unregister(this);
    }

}
