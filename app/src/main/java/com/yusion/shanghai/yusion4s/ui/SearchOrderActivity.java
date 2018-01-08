package com.yusion.shanghai.yusion4s.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
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
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.bean.auth.CheckInfoCompletedResp;
import com.yusion.shanghai.yusion4s.bean.auth.ReplaceSPReq;
import com.yusion.shanghai.yusion4s.bean.order.GetAppListResp;
import com.yusion.shanghai.yusion4s.bean.order.submit.ReSubmitReq;
import com.yusion.shanghai.yusion4s.car_select.adapter.HistoryAdapter;
import com.yusion.shanghai.yusion4s.retrofit.Api;
import com.yusion.shanghai.yusion4s.retrofit.api.AuthApi;
import com.yusion.shanghai.yusion4s.retrofit.api.OrderApi;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.ui.entrance.apply_financing.AlterCarInfoActivity;
import com.yusion.shanghai.yusion4s.ui.entrance.apply_financing.AlterOldCarInfoActivity;
import com.yusion.shanghai.yusion4s.ui.order.OrderDetailActivity;
import com.yusion.shanghai.yusion4s.ui.upload.SubmitMaterialActivity;
import com.yusion.shanghai.yusion4s.utils.ApiUtil;
import com.yusion.shanghai.yusion4s.utils.InputMethodUtil;
import com.yusion.shanghai.yusion4s.utils.PopupDialogUtil;
import com.yusion.shanghai.yusion4s.utils.SharedPrefsUtil;
import com.yusion.shanghai.yusion4s.utils.ToastUtil;
import com.yusion.shanghai.yusion4s.widget.NoEmptyEditText;

import java.util.ArrayList;
import java.util.List;

import static com.yusion.shanghai.yusion4s.base.ActivityManager.finish;


public class SearchOrderActivity extends BaseActivity {
    private PtrClassicFrameLayout my_search_order_ptr;
    private RecyclerView my_order_rv;
    private LinearLayout my_search_order_llyt;
    private NoEmptyEditText search_et;
    private ImageView poi_delete_img;
    private Button search_btn;
    private List<GetAppListResp.DataBean> items;
    private RecyclerAdapterWithHF adapter;
    private MyOrderListAdapter myOrderListAdapter;
    private RecyclerView hisRecyclerView;
    private String history = "";
    private ImageView back_img;
    private ImageView lajitong;//垃圾桶

    //用于展示历史记录
    private List<String> mDates;
    private HistoryAdapter historyAdapter;
    private RecyclerAdapterWithHF hisAdapter;
    private LinearLayout history_lin;

    private int page = 1;
    private int total_page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_order);
        initView();
    }

    private void initView() {
        lajitong = findViewById(R.id.lajitong);
        back_img = findViewById(R.id.back_img);
        history_lin = findViewById(R.id.history_lin);
        hisRecyclerView = findViewById(R.id.recyclerView);
        my_search_order_ptr = findViewById(R.id.my_search_order_ptr);
        my_order_rv = findViewById(R.id.my_order_rv);
        my_search_order_llyt = findViewById(R.id.my_search_order_llyt);
        search_et = findViewById(R.id.search_et);
        poi_delete_img = findViewById(R.id.poi_delete);
        search_btn = findViewById(R.id.search_btn);
        my_order_rv.setLayoutManager(new LinearLayoutManager(this));
//        my_order_rv.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.VERTICAL, DensityUtil.dip2px(this, 10), ContextCompat.getColor(this, R.color.main_bg)));
        my_order_rv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = 10;
            }
        });
        items = new ArrayList<>();
        myOrderListAdapter = new MyOrderListAdapter(this, items);
        //myOrderListAdapter.setVehicle_cond(vehicle_cond);
        adapter = new RecyclerAdapterWithHF(myOrderListAdapter);
        my_order_rv.setAdapter(adapter);

        mDates = new ArrayList<>();
        Log.e("TAG", mDates.size() + "");
        hisRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        historyAdapter = new HistoryAdapter(this, mDates, search_et);
        historyAdapter.setOnItemClickListener(new HistoryAdapter.onItemCLickListener() {
            @Override
            public void onItemClick(View v, List<String> mdates) {
                if (mdates.size() < 0 || mdates.isEmpty()) {
                    //隐藏
                    history_lin.setVisibility(View.GONE);
                }
            }
        });
        historyAdapter.setOnItemChooseClickListener(new HistoryAdapter.onItemChooseCLickListener() {
            @Override
            public void onChooseClick(View v, String etText) {
                search_et.setText(etText);
                search_et.setSelection(etText.length());
                refresh();
            }
        });

//        String sss = SharedPrefsUtil.getInstance(this).getValue("history", "");

        hisRecyclerView.setAdapter(historyAdapter);
        Log.e("TAG", mDates.size() + "");
        history = SharedPrefsUtil.getInstance(this).getValue("history", "");

        String[] ssss = history.split("#");

        for (int j = 0; j < ssss.length; j++) {
            if (!TextUtils.isEmpty(ssss[j])) {
                mDates.add(ssss[j]);
            }
        }
        historyAdapter.notifyDataSetChanged();


        my_search_order_ptr.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return false;
            }
        });
        lajitong.setOnClickListener(v -> {
            SharedPrefsUtil.getInstance(this).putValue("history", "");
//            mDates = new ArrayList<String>();
            mDates.clear();
            Log.e("TAG", "initView: " + mDates);
            historyAdapter.notifyDataSetChanged();
            history_lin.setVisibility(View.GONE);
        });
        my_search_order_ptr.setOnLoadMoreListener(() -> {
            if (page < total_page) {
                ApiUtil.requestUrl4Data(this, Api.getOrderService().getSearchAppList("0", search_et.getText().toString(), ++page), (OnItemDataCallBack<GetAppListResp>) resp -> {
                    if (resp != null) {
                        if (resp.total_page == 0 || resp.total_page == 1) {
                            my_search_order_ptr.setLoadMoreEnable(false);
                            my_search_order_ptr.loadMoreComplete(false);
                        }
                        for (GetAppListResp.DataBean dataBean : resp.data) {
                            items.add(dataBean);
                            my_search_order_ptr.setVisibility(View.VISIBLE);
                            my_order_rv.setVisibility(View.VISIBLE);
                            my_search_order_llyt.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                            my_search_order_ptr.loadMoreComplete(true);
                        }
                    }
                });
            } else {
                my_search_order_ptr.loadMoreComplete(false);
            }
        });

        back_img.setOnClickListener(v -> {
            onBackPressed();
        });
        search_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    refresh();
                }
                return false;
            }
        });
        search_et.setOnClickListener(v -> search_et.setCursorVisible(true));
        search_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                search_et.setCursorVisible(true);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    poi_delete_img.setVisibility(View.VISIBLE);
                } else if (s.equals("")) {
                    poi_delete_img.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    poi_delete_img.setVisibility(View.GONE);
                    my_search_order_llyt.setVisibility(View.GONE);
                    my_search_order_ptr.setVisibility(View.GONE);
                    my_order_rv.setVisibility(View.GONE);
                    if (mDates.size() > 0) {
                        history_lin.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        poi_delete_img.setOnClickListener(v -> {
            InputMethodUtil.showInputMethod(this);
            search_et.setText("");
            search_et.setCursorVisible(true);
            my_search_order_llyt.setVisibility(View.GONE);
            my_search_order_ptr.setVisibility(View.GONE);
            my_order_rv.setVisibility(View.GONE);
            if (mDates.size() > 0) {
                history_lin.setVisibility(View.VISIBLE);
            }
        });

        search_btn.setOnClickListener(v -> {
            refresh();
//            if (TextUtils.isEmpty(search_et.getText())) {
//                ToastUtil.showLong(this, "sss");
//            } else {
//                //  history = search_et.getText().toString() + "#" + history;
//                saveSearchHistory();
//            }
        });


    }

    private void saveSearchHistory() {
        history_lin.setVisibility(View.GONE);
        String ss[] = history.split("#");
        if (ss.length > 0 && ss[0] != null) {
            int k = 0;
            for (int i = 0; i < ss.length; i++) {
                if (ss[i].equals(search_et.getText().toString())) {
                    break;
                } else {
                    k++;
                    continue;
                }
            }
            if (k == ss.length) {
                history = search_et.getText().toString() + "#" + history;
                mDates.add(search_et.getText().toString());
                Log.e("TAG7777", "initView: " + mDates.get(0));
                historyAdapter.notifyDataSetChanged();
            }
        } else {
            history = search_et.getText().toString() + "#";
            mDates.add(search_et.getText().toString());
            Log.e("TAG6666", "initView: " + mDates.get(0));
            historyAdapter.notifyDataSetChanged();
        }
        Log.e("TAG", "initView: " + history);
        SharedPrefsUtil.getInstance(this).putValue("history", history);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // my_search_order_ptr.setEnabled(false);
        //显示搜索历史
        if (mDates.size() > 0) {
            showSearchHistory();
        }
    }

    public void showSearchHistory() {
        history_lin.setVisibility(View.VISIBLE);//搜索历史记录
        my_search_order_ptr.setVisibility(View.GONE);
        my_order_rv.setVisibility(View.GONE);
        my_search_order_llyt.setVisibility(View.GONE);
    }

    public void refresh() {

        if (TextUtils.isEmpty(search_et.getText())) {
            ToastUtil.showLong(this, "请输入用户姓名");
        } else {
            page = 1;
            ApiUtil.requestUrl4Data(this, Api.getOrderService().getSearchAppList("0", search_et.getText().toString(), page), (OnItemDataCallBack<GetAppListResp>) resp -> {
                if (resp != null && resp.data.size() > 0 && resp.data != null) {
                    search_et.setCursorVisible(false);
                    if (resp.total_page == 0 || resp.total_page == 1) {
                        my_search_order_ptr.setLoadMoreEnable(true);
                        my_search_order_ptr.loadMoreComplete(false);
                    } else {
                        total_page = resp.total_page;
                    }
                    saveSearchHistory();
                    my_search_order_ptr.setVisibility(View.VISIBLE);
                    my_order_rv.setVisibility(View.VISIBLE);
                    my_search_order_llyt.setVisibility(View.GONE);
                    items.clear();
                    items.addAll(resp.data);
                    adapter.notifyDataSetChanged();
                    my_search_order_ptr.refreshComplete();

                    my_search_order_ptr.setLoadMoreEnable(true);


                } else {
                    my_search_order_ptr.refreshComplete();
                    history_lin.setVisibility(View.GONE);
                    my_order_rv.setVisibility(View.GONE);
                    my_search_order_llyt.setVisibility(View.VISIBLE);
                    my_search_order_ptr.setVisibility(View.VISIBLE);
                }
            });
        }

    }

}

class MyOrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private OnItemClick mOnItemClick;
    private List<GetAppListResp.DataBean> mItems;
    private String vehicle_cond;

    public void setVehicle_cond(String vehicle_cond) {
        this.vehicle_cond = vehicle_cond;
    }

    public MyOrderListAdapter(Context context, List<GetAppListResp.DataBean> Items) {
        mContext = context;
        mItems = Items;
        mLayoutInflater = LayoutInflater.from(mContext);
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

        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OrderDetailActivity.class);
                intent.putExtra("app_id", item.app_id);
                intent.putExtra("status_st", item.status_st);
                if (item.can_switch_sp) {
                    intent.putExtra("spouse_clt_id", item.spouse_clt_id);
                }
                mContext.startActivity(intent);
            }
        });
        if (item.vehicle_cond.equals("二手车")) {
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
        if (item.status_st == 1 || item.status_st == 2 || item.status_st == 0) {//待审核2
            vh.st.setTextColor(Color.parseColor("#FFA400"));
        } else if (item.status_st == 5 || item.status_st == 6 || item.status_st == 7 || item.status_st == 8) {//4审核失败
            vh.st.setTextColor(Color.parseColor("#FF3F00"));
        } else if (item.status_st == 3 || item.status_st == 4) {//放款中
            vh.st.setTextColor(Color.parseColor("#06B7A3"));
        } else if (item.status_st == 9) {//已取消9
            vh.st.setTextColor(Color.parseColor("#666666"));
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
                ((Button) contentView.findViewById(R.id.btn1)).setOnClickListener(v1 -> {
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

//                    Toast.makeText(mContext,"修改资料按钮",Toast.LENGTH_SHORT).show();
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

    }

    private void checkAndReplace(String app_id, String spouse_clt_id, int status_st) {

        ReplaceSPReq replaceSPReq = new ReplaceSPReq();
        replaceSPReq.clt_id = spouse_clt_id;
        Log.e("TAG", "spouse_clt_id = " + spouse_clt_id);
        //1.激活配偶登录
        AuthApi.replaceSpToP(mContext, replaceSPReq, data1 -> {
            if (data1 == null) {
                return;
            }
            //2.检查配偶信息是否完善
            AuthApi.CheckInfoComplete(mContext, spouse_clt_id, new OnItemDataCallBack<CheckInfoCompletedResp>() {
                @Override
                public void onItemDataCallBack(CheckInfoCompletedResp data) {
                    if (data == null) {
                        return;
                    }
                    //完善 - 提交成功
                    if (data.info_completed) {
                        ReSubmitReq req = new ReSubmitReq();
                        req.clt_id = spouse_clt_id;
                        req.app_id = app_id;
                        //3：重新提报
                        OrderApi.reSubmit(mContext, req, data2 -> {
                            if (data2 != null) {
                                ToastUtil.showImageToast(mContext, "提交成功", R.mipmap.toast_success);
                                Intent intent = new Intent(mContext, OrderDetailActivity.class);
                                intent.putExtra("app_id", data2.app_id);
                                intent.putExtra("status_st", status_st);
                                mContext.startActivity(intent);
                                finish();
                            }
                        });
                    }
                    //未完善
                    else {
                        PopupDialogUtil.showOneButtonDialog(mContext, R.layout.popup_dialog_one_hastitle_button, dialog1 -> {
                            dialog1.dismiss();
                        });
                    }
                }
            });
        });
    }


    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    private class VH extends RecyclerView.ViewHolder {
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
        public ImageView car_icon;
        public RelativeLayout oneBtnlibn;
        public RelativeLayout twoBtnlibn;

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
            phone = ((TextView) itemView.findViewById(R.id.order_list_item_phone_img));
            change = (TextView) itemView.findViewById(R.id.order_list_item_change_tv);
            upload = (TextView) itemView.findViewById(R.id.order_list_item_upload_tv);
            replace = (TextView) itemView.findViewById(R.id.order_list_item_replace_tv);
            car_icon = (ImageView) itemView.findViewById(R.id.order_list_item_car_icon);
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

