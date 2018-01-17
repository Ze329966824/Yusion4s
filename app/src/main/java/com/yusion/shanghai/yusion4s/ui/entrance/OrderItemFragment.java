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
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.BaseFragment;
import com.yusion.shanghai.yusion4s.bean.auth.ReplaceSPReq;
import com.yusion.shanghai.yusion4s.bean.order.GetAppListResp;
import com.yusion.shanghai.yusion4s.bean.order.submit.ReSubmitReq;
import com.yusion.shanghai.yusion4s.retrofit.Api;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.ui.entrance.apply_financing.AlterCarInfoActivity;
import com.yusion.shanghai.yusion4s.ui.entrance.apply_financing.AlterOldCarInfoActivity;
import com.yusion.shanghai.yusion4s.ui.order.OrderDetailActivity;
import com.yusion.shanghai.yusion4s.ui.upload.SubmitMaterialActivity;
import com.yusion.shanghai.yusion4s.utils.ApiUtil;
import com.yusion.shanghai.yusion4s.utils.DensityUtil;
import com.yusion.shanghai.yusion4s.utils.PopupDialogUtil;
import com.yusion.shanghai.yusion4s.utils.ToastUtil;
import com.yusion.shanghai.yusion4s.widget.RecyclerViewDivider;

import java.util.ArrayList;
import java.util.List;

import static com.yusion.shanghai.yusion4s.R.id.order_list_item_check_tv;


public class OrderItemFragment extends BaseFragment {

    private Handler handler = new Handler();
    private List<GetAppListResp.DataBean> items;
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

    public void setVehicle_cond(String vehicle_cond) {
        this.vehicle_cond = vehicle_cond;
        if (myOrderListAdapter != null) {
            myOrderListAdapter.setVehicle_cond(vehicle_cond);
            myOrderListAdapter.notifyDataSetChanged();
        }
    }

    public static OrderItemFragment newInstance(String s) {

        Bundle args = new Bundle();
        args.putString("st", s);
        OrderItemFragment fragment = new OrderItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static OrderItemFragment newInstance(String s, String vehicle_cond) {

        Bundle args = new Bundle();
        args.putString("st", s);
        args.putString("vehicle_cond", vehicle_cond);
        OrderItemFragment fragment = new OrderItemFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_item, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        if (!EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().register(this);
//        }
        st = getArguments().getString("st");
        vehicle_cond = getArguments().getString("vehicle_cond");
        llyt = view.findViewById(R.id.my_order_llyt);
        rv = view.findViewById(R.id.my_order_rv);
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        rv.addItemDecoration(new RecyclerViewDivider(mContext, LinearLayoutManager.VERTICAL, DensityUtil.dip2px(getActivity(), 10), ContextCompat.getColor(getActivity(), R.color.main_bg)));
        items = new ArrayList<>();
        myOrderListAdapter = new MyOrderListAdapter(mContext, items);
        myOrderListAdapter.setVehicle_cond(vehicle_cond);
        adapter = new RecyclerAdapterWithHF(myOrderListAdapter);
        rv.setAdapter(adapter);
        ptr = view.findViewById(R.id.my_order_ptr);


//        RefreshHeader header = new RefreshHeader(mContext);
        ptr.autoRefresh(true);

//        ptr.setHeaderView(header);
//        ptr.addPtrUIHandler(header);
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
//        PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(mContext,"自定义的下拉刷14条信息");
//
//        ptr.setHeaderView(header);
//        ptr.addPtrUIHandler(header);


        ptr.setLoadMoreEnable(true);
        ptr.setOnLoadMoreListener(() -> {
            if (page < total_page) {
                ApiUtil.requestUrl4Data(mContext, Api.getOrderService().getAppList(st, vehicle_cond, ++page), (OnItemDataCallBack<GetAppListResp>) resp -> {
                    if (resp != null) {
                        if (resp.total_page == 0 || resp.total_page == 1) {
                            ptr.setLoadMoreEnable(false);
                            ptr.loadMoreComplete(false);
                        }
                        for (GetAppListResp.DataBean dataBean : resp.data) {
                            items.add(dataBean);
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
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    public void refresh() {
        page = 1;
        ApiUtil.requestUrl4Data(mContext, Api.getOrderService().getAppList(st, vehicle_cond, page), (OnItemDataCallBack<GetAppListResp>) resp -> {
            if (resp != null) {
                if (resp.total_page == 0 || resp.total_page == 1) {
                    ptr.setLoadMoreEnable(true);
                    ptr.loadMoreComplete(false);
                } else {
                    total_page = resp.total_page;
                }
                if (resp.data.size() > 0) {
                    ptr.setVisibility(View.VISIBLE);
                    rv.setVisibility(View.VISIBLE);
                    llyt.setVisibility(View.GONE);
                    items.clear();

                    items.addAll(resp.data);
                    Log.e("TAG", "refresh: items = " + items.size());
                    adapter.notifyDataSetChanged();
                    ptr.refreshComplete();
                    ptr.setLoadMoreEnable(true);
                } else {
                    ptr.refreshComplete();
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
        private List<GetAppListResp.DataBean> mItems;
        private String vehicle_cond;

        public void setVehicle_cond(String vehicle_cond) {
            this.vehicle_cond = vehicle_cond;
        }

        public MyOrderListAdapter(Context context, List<GetAppListResp.DataBean> items) {
            mContext = context;
            mLayoutInflater = LayoutInflater.from(mContext);
            mItems = items;
            Log.e("TAG", "Adapter: items = " + mItems.size());
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new VH(mLayoutInflater.inflate(R.layout.order_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Log.e("TAG", "onBindViewHolder: " + vehicle_cond);
            VH vh = (VH) holder;
            GetAppListResp.DataBean item = mItems.get(position);
            Log.e("TAG", "onBindViewHolder: items = " + mItems.size());
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, OrderDetailActivity.class);
                    intent.putExtra("come_from","orderitem");
                    intent.putExtra("app_id", item.app_id);
                    intent.putExtra("status_st", item.status_st);
                    if (item.can_switch_sp) {
                        intent.putExtra("spouse_clt_id", item.spouse_clt_id);
                    }
//                    intent.putExtra("modify_permission", item.modify_permission);
                    mContext.startActivity(intent);
                }
            });
            if (vehicle_cond.equals("二手车")) {
                vh.car_icon.setImageResource(R.mipmap.old_car_icon);
            } else {
                vh.car_icon.setImageResource(R.mipmap.new_car_icon);
            }
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
            if (item.status_st == 3) {                  //拒绝
                vh.st.setTextColor(Color.parseColor("#FFFF3F00"));
            } else if (item.status_st == 9) {           //已取消9
                vh.st.setTextColor(Color.parseColor("#FF666666"));
            } else if (item.status_st == 11) {           //已完成
                vh.st.setTextColor(Color.parseColor("#FF06B7A3"));
            } else {                                     //进行中
                vh.st.setTextColor(Color.parseColor("#FFFFA400"));
            }
            if (item.can_switch_sp) {
                vh.oneBtnlibn.setVisibility(View.VISIBLE);
                vh.twoBtnlibn.setVisibility(View.GONE);
            } else {


                vh.twoBtnlibn.setVisibility(View.VISIBLE);
                vh.oneBtnlibn.setVisibility(View.GONE);
                if (item.modify_permission) {
                    vh.change.setVisibility(View.VISIBLE);
                } else {
                    vh.change.setVisibility(View.GONE);
                    if (item.status_st == 9 || item.status_st == 11){
                        vh.upload.setText("查看资料");
                        vh.upload.setTextColor(Color.parseColor("#FF666666"));
                        vh.upload.setBackgroundResource(R.drawable.shape_text_gray);
                    }
                }
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
                    contentView.findViewById(R.id.btn1).setOnClickListener(v1 -> {
//                        UploadLabelListActivity.start(mContext, item.app_id);
                        popupWindow.dismiss();
                    });
                    /**
                     * 添加取消订单的功能，暂时用popupWindow.dismiss 替代
                     */
                    contentView.findViewById(R.id.btn2).setOnClickListener(v2 -> {
                        //popupWindow.dismiss();
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

            vh.change.setOnClickListener(v -> {
                if (item.vehicle_cond.equals("新车")) {
                    Intent i1 = new Intent(mContext, AlterCarInfoActivity.class);
                    i1.putExtra("app_id", item.app_id);
                    i1.putExtra("car_type", item.vehicle_cond);
                    Log.e("TAG", item.vehicle_cond);
                    mContext.startActivity(i1);
                } else {
                    Intent i1 = new Intent(mContext, AlterOldCarInfoActivity.class);
                    i1.putExtra("app_id", item.app_id);
                    i1.putExtra("car_type", item.vehicle_cond);
                    Log.e("TAG", item.vehicle_cond);
                    mContext.startActivity(i1);
                }

            });
            vh.upload.setOnClickListener(v -> {
                Intent i2 = new Intent(mContext, SubmitMaterialActivity.class);
                i2.putExtra("app_id", item.app_id);
                mContext.startActivity(i2);
            });

            vh.replace.setOnClickListener(v -> {
                PopupDialogUtil.showTwoButtonsDialog(mContext, R.layout.popup_dialog_two_hastitle_button, dialog -> {
                    dialog.dismiss();
                    checkAndReplace(item.app_id, item.spouse_clt_id, item.status_st);

                });
            });
            vh.check.setOnClickListener(v -> {
                Intent i3 = new Intent(mContext, SubmitMaterialActivity.class);
                i3.putExtra("app_id", item.app_id);
                mContext.startActivity(i3);
            });

        }

        private void checkAndReplace(String app_id, String spouse_clt_id, int status_st) {

            ReplaceSPReq replaceSPReq = new ReplaceSPReq();
            replaceSPReq.clt_id = spouse_clt_id;
//            Log.e("TAG", "spouse_clt_id = " + spouse_clt_id);
            //1.激活配偶登录
            ApiUtil.requestUrl4Data(mContext,Api.getAuthService().replaceSpToP(replaceSPReq),data1 -> {
//                AuthApi.replaceSpToP(mContext, replaceSPReq, data1 -> {
                if (data1 == null) {
                    return;
                }
                //2.检查配偶信息是否完善
                ApiUtil.requestUrl4Data(mContext,Api.getAuthService().CheckInfoComplete(spouse_clt_id),data22 ->{
//                    AuthApi.CheckInfoComplete(mContext, spouse_clt_id, data22 -> {
                        if (data22 == null) {
                            return;
                        }
                        //完善 - 提交成功
                        if (data22.info_completed) {
                            ReSubmitReq req = new ReSubmitReq();
                            req.clt_id = spouse_clt_id;
                            req.app_id = app_id;
                            //3：重新提报
                            ApiUtil.requestUrl4Data(mContext,Api.getOrderService().reSubmit(req),data2 -> {
//                          OrderApi.reSubmit(mContext, req, data2 -> {
                                if (data2 != null) {
                                    ToastUtil.showImageToast(mContext, "提交成功", R.mipmap.toast_success);
                                    Intent intent = new Intent(mContext, OrderDetailActivity.class);
                                    intent.putExtra("come_from","orderitem");
                                    intent.putExtra("app_id", data2.app_id);
                                    intent.putExtra("status_st", status_st);
                                    mContext.startActivity(intent);
                                }
                            });
                        }
                        //未完善
                        else {
                            PopupDialogUtil.showOneButtonDialog(mContext, R.layout.popup_dialog_one_hastitle_button, dialog1 -> {
//                                PackageManager packageManager = mContext.getPackageManager();  // 当前Activity获得packageManager对象
//                                Intent intent = new Intent();
//                                try {
//                                    intent = packageManager.getLaunchIntentForPackage("com.yusion.shanghai.yusion");
//                                } catch (Exception e) {
//                                }
//                                if (intent != null) {
//                                    mContext.startActivity(intent);
//                                }
                                dialog1.dismiss();
                            });
                        }
                    });
            });
        }

        @Override
        public int getItemCount() {
            int size = mItems.size();
            return mItems == null ? 0 : size;
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
            public TextView phone;
            public TextView change;
            public TextView upload;
            public TextView replace;
            public TextView check;
            public ImageView car_icon;
            public RelativeLayout oneBtnlibn;
            public RelativeLayout twoBtnlibn;

            public VH(View itemView) {
                super(itemView);
                btns = itemView.findViewById(R.id.order_list_item_btns_img);
                name = itemView.findViewById(R.id.order_list_item_name_tv);
                st = itemView.findViewById(R.id.order_list_item_st_tv);
                door = itemView.findViewById(R.id.order_list_item_door_tv);
                time = itemView.findViewById(R.id.order_list_item_time_tv);
                model = itemView.findViewById(R.id.order_list_item_model_tv);
                brand = itemView.findViewById(R.id.order_item_brand);
                trix = itemView.findViewById(R.id.order_list_item_trix_tv);
                loan = itemView.findViewById(R.id.order_list_item_total_loan_tv);
                periods = itemView.findViewById(R.id.order_list_item_periods_tv);
                phone = itemView.findViewById(R.id.order_list_item_phone_img);
                change = itemView.findViewById(R.id.order_list_item_change_tv);
                upload = itemView.findViewById(R.id.order_list_item_upload_tv);
                replace = itemView.findViewById(R.id.order_list_item_replace_tv);
                check = itemView.findViewById(order_list_item_check_tv);
                car_icon = itemView.findViewById(R.id.order_list_item_car_icon);
                oneBtnlibn = itemView.findViewById(R.id.order_list_item_one_btn_lin);
                twoBtnlibn = itemView.findViewById(R.id.order_list_item_two_btn_lin);
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
//        EventBus.getDefault().unregister(this);
    }

}
