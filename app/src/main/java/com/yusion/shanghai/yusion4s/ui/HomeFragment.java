package com.yusion.shanghai.yusion4s.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.BaseFragment;
import com.yusion.shanghai.yusion4s.bean.order.submit.SubmitOrderReq;
import com.yusion.shanghai.yusion4s.event.MainActivityEvent;
import com.yusion.shanghai.yusion4s.retrofit.Api;
import com.yusion.shanghai.yusion4s.retrofit.api.DlrApi;
import com.yusion.shanghai.yusion4s.settings.Constants;
import com.yusion.shanghai.yusion4s.ui.entrance.apply_financing.AlterCarInfoActivity;
import com.yusion.shanghai.yusion4s.ui.order.ChangeDlrActivity;
import com.yusion.shanghai.yusion4s.ui.order.OrderCreateActivity;
import com.yusion.shanghai.yusion4s.utils.ApiUtil;
import com.yusion.shanghai.yusion4s.utils.SharedPrefsUtil;

import org.greenrobot.eventbus.EventBus;

public class HomeFragment extends BaseFragment {

    private PtrClassicFrameLayout ptr;
    private TextView top_dlr;                  //顶部经销商
    private TextView all_count;                //累计完成
    private TextView today_count;              //今日已申请
    private TextView dealing_count;            //进行中

    private TextView reject_count;             //审核拒绝
    private ImageView reject_img;
    private TextView to_be_confirm_count;      //待确认金融方案
    private ImageView to_be_confirm_img;
    private TextView to_loan_count;            //放款中
    private ImageView to_loan_img;
    private TextView to_be_upload_count;       //待提贷后资料
    private ImageView to_be_upload_img;

    public SubmitOrderReq req = new SubmitOrderReq();
    public String dlr;
    public String dlr_id;
    private MainActivity activity;

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        activity = (MainActivity) getActivity();
        ptr.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                refresh(dlr_id);
            }
        });
    }

    void firstLogin() {
        if (activity.isFirstLogin) {
            ApiUtil.requestUrl4Data(mContext, Api.getDlrService().getDlrListByToken(), resp ->{
//            DlrApi.getDlrListByToken(mContext, resp -> {
                activity.isFirstLogin = false;
                if (resp != null && !resp.isEmpty()) {
                    top_dlr.setText(resp.get(0).dlr_nm);
                    dlr_id = resp.get(0).id;
                    refresh(dlr_id);
                }
            });
        }
    }

    //刷新首页订单数量
    void refresh(String id) {
        ApiUtil.requestUrl4Data(mContext, Api.getDlrNum().getDlrNum(id), dlrNumResp -> {
            ptr.refreshComplete();
            if (dlrNumResp != null) {

                //取出存在sp的订单状态 eg： 1-2-5-1
                String values = SharedPrefsUtil.getInstance(mContext).getValue(id, null);
                //之前存过
                if (values != null) {
                    Log.e("TAG", "refresh: "+values);
                    String[] value = values.split("-");
                    if (value.length == 4) {
                        if ((reject_count.getText().toString().compareTo(value[0]) == 1)) {
                            reject_img.setVisibility(View.VISIBLE);
                        } else {
                            reject_img.setVisibility(View.GONE);
                        }
                        if ((to_be_confirm_count.getText().toString().compareTo(value[1]) == 1)) {
                            to_be_confirm_img.setVisibility(View.VISIBLE);
                        } else {
                            to_be_confirm_img.setVisibility(View.GONE);
                        }
                        if ((to_loan_count.getText().toString().compareTo(value[2]) == 1)) {
                            to_loan_img.setVisibility(View.VISIBLE);
                        } else {
                            to_loan_img.setVisibility(View.GONE);
                        }
                        if ((to_be_upload_count.getText().toString().compareTo(value[3]) == 1)) {
                            to_be_upload_img.setVisibility(View.VISIBLE);
                        } else {
                            to_be_upload_img.setVisibility(View.GONE);
                        }
                    }
                }
                //之前没有存过
                else {
                    if (dlrNumResp.reject_count.equals("0")) {
                        reject_img.setVisibility(View.VISIBLE);
                    }
                    if (dlrNumResp.to_be_confirm_count.equals("0")) {
                        to_be_confirm_img.setVisibility(View.VISIBLE);
                    }
                    if (dlrNumResp.to_loan_count.equals("0")) {
                        to_loan_img.setVisibility(View.VISIBLE);
                    }
                    if (dlrNumResp.to_be_upload_count.equals("0")) {
                        to_be_upload_img.setVisibility(View.VISIBLE);
                    }
                }
                all_count.setText(dlrNumResp.all_count);
                today_count.setText(dlrNumResp.today_count);
                dealing_count.setText(dlrNumResp.dealing_count);

                reject_count.setText(dlrNumResp.to_be_confirm_count);   //1
                to_be_confirm_count.setText(dlrNumResp.to_loan_count);  //2
                to_loan_count.setText(dlrNumResp.to_be_upload_count);   //3
                to_be_upload_count.setText(dlrNumResp.reject_count);    //4

                SharedPrefsUtil.getInstance(mContext).putValue("dlr_nums", dlr_id + "/");
                SharedPrefsUtil.getInstance(mContext).putValue
                        (id, dlrNumResp.to_be_confirm_count + "-" + dlrNumResp.to_loan_count + "-" + dlrNumResp.to_be_upload_count + "-" + dlrNumResp.reject_count);
                dlr = null;
            }
        });
    }

    private void initView(View view) {
        top_dlr = view.findViewById(R.id.apply_financing_dlr_tv);
        all_count = view.findViewById(R.id.all_count);
        today_count = view.findViewById(R.id.today_count);
        dealing_count = view.findViewById(R.id.dealing_count);

        reject_count = view.findViewById(R.id.reject_count);
        to_be_confirm_count = view.findViewById(R.id.to_be_confirm_count);
        to_loan_count = view.findViewById(R.id.to_loan_count);
        to_be_upload_count = view.findViewById(R.id.to_be_upload_count);

        ptr = view.findViewById(R.id.dlr_num_ptr);
        reject_img = view.findViewById(R.id.reject_img);
        to_be_confirm_img = view.findViewById(R.id.to_be_confirm_img);
        to_loan_img = view.findViewById(R.id.to_loan_img);
        to_be_upload_img = view.findViewById(R.id.to_be_upload_img);

        //新车
        view.findViewById(R.id.apply_financing_cteate_newcar_btn).setOnClickListener(v -> {
            Intent i1 = new Intent(mContext, OrderCreateActivity.class);
            i1.putExtra("car_type", "新车");
            startActivity(i1);
        });
        //二手车
        view.findViewById(R.id.apply_financing_cteate_oldcar_btn).setOnClickListener(v -> {
            Intent i2 = new Intent(mContext, OrderCreateActivity.class);
            i2.putExtra("car_type", "二手车");
            startActivity(i2);
        });
        //选择经销商
        view.findViewById(R.id.apply_financing_dlr_lin).setOnClickListener(v -> {
            Intent i3 = new Intent(mContext, ChangeDlrActivity.class);
            startActivityForResult(i3, Constants.REQUEST_CHANGE_DLR);
            activity.overridePendingTransition(R.anim.pop_enter_anim, R.anim.stay);
        });
        //放款审核
        view.findViewById(R.id.apply_financing_lin1).setOnClickListener(v -> {
            MainActivityEvent.showOrderManager.position = 2;
            EventBus.getDefault().post(MainActivityEvent.showOrderManager);
        });
        //放款
        view.findViewById(R.id.apply_financing_lin2).setOnClickListener(v -> {
            MainActivityEvent.showOrderManager.position = 3;
            EventBus.getDefault().post(MainActivityEvent.showOrderManager);
        });
        //贷后追踪
        view.findViewById(R.id.apply_financing_lin3).setOnClickListener(v -> {
            MainActivityEvent.showOrderManager.position = 4;
            EventBus.getDefault().post(MainActivityEvent.showOrderManager);
        });
        //已拒绝
        view.findViewById(R.id.apply_financing_lin4).setOnClickListener(v -> {
            MainActivityEvent.showOrderManager.position = 8;
            EventBus.getDefault().post(MainActivityEvent.showOrderManager);
        });
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CHANGE_DLR) {
            if (resultCode == Activity.RESULT_OK) {
                dlr = data.getStringExtra("dlr");
                dlr_id = data.getStringExtra("dlr_id");
                if (!TextUtils.isEmpty(dlr)) {
                    top_dlr.setText(dlr);
                }
                refresh(dlr_id);
            }
        }
    }

    //移除new小图标
    public void removeImg(int position) {
        switch (position) {
            case 2:
                reject_img.setVisibility(View.GONE);
                break;
            case 3:
                to_be_confirm_img.setVisibility(View.GONE);
                break;
            case 4:
                to_loan_img.setVisibility(View.GONE);
                break;
            case 8:
                to_be_upload_img.setVisibility(View.GONE);
                break;
            default:
                break;
        }

    }
}
