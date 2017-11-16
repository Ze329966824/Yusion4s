package com.yusion.shanghai.yusion4s.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.BaseFragment;
import com.yusion.shanghai.yusion4s.bean.order.submit.SubmitOrderReq;
import com.yusion.shanghai.yusion4s.event.MainActivityEvent;
import com.yusion.shanghai.yusion4s.ui.entrance.apply_financing.CarInfoFragment;
import com.yusion.shanghai.yusion4s.ui.entrance.apply_financing.CreditInfoFragment;
import com.yusion.shanghai.yusion4s.ui.order.ChangeDLRActivity;
import com.yusion.shanghai.yusion4s.ui.order.OrderCreateActivity;

import org.greenrobot.eventbus.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class ApplyFinancingFragment extends BaseFragment {

    private CarInfoFragment mCarInfoFragment;
    private CreditInfoFragment mCreditInfoFragment;
    private Fragment mCurrentFragment;
    public SubmitOrderReq req = new SubmitOrderReq();

    public static ApplyFinancingFragment newInstance() {
        Bundle args = new Bundle();
        ApplyFinancingFragment fragment = new ApplyFinancingFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_apply_financing, container, false);



    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        onclick(view);


        /*

//
//
//        initTitleBar(view, "申请融资");
//
//        //setBackHide();
//
//        mCarInfoFragment = CarInfoFragment.newInstance();
//        mCreditInfoFragment = CreditInfoFragment.newInstance();
//        /**
//         * 测试时改变了显示的fragment  将mCreditInfoFragment显示，隐藏mCarInfoFragment
//         *
//         * 后续记得改回来
//
//
//
//
//        getChildFragmentManager()
//                .beginTransaction()
//                .add(R.id.apply_financing_container, mCarInfoFragment)
//                .add(R.id.apply_financing_container, mCreditInfoFragment)
//                //.hide(mCarInfoFragment)
//                .hide(mCreditInfoFragment)
//                .commit();
//        mCurrentFragment = mCarInfoFragment;
//
//
//
//
//
//
//
//
//
//
//        //mCurrentFragment = mCreditInfoFragment;



        */

    }

    private void onclick(View view) {
        //新车
        view.findViewById(R.id.apply_financing_cteate_newcar_btn).setOnClickListener(v -> {
            Intent i1 = new Intent(mContext, OrderCreateActivity.class);
            i1.putExtra("car_type","新车");
            startActivity(i1);
        });
        //二手车
        view.findViewById(R.id.apply_financing_cteate_oldcar_btn).setOnClickListener(v ->{
            Intent i2 = new Intent(mContext,OrderCreateActivity.class);
            i2.putExtra("car_type","二手车");
            startActivity(i2);
        });

        view.findViewById(R.id.apply_financing_dlr_lin).setOnClickListener(v -> {
            Intent i3 = new Intent(mContext, ChangeDLRActivity.class);
            startActivity(i3);
        });

        view.findViewById(R.id.apply_financing_lin1).setOnClickListener(v -> {
            Log.e("TAG", "onClick: ");
            MainActivityEvent.showOrderManager.position = 8;
            EventBus.getDefault().post(MainActivityEvent.showOrderManager);
        });

        view.findViewById(R.id.apply_financing_lin2).setOnClickListener(v -> {
            MainActivityEvent.showOrderManager.position = 2;
            EventBus.getDefault().post(MainActivityEvent.showOrderManager);
        });

        view.findViewById(R.id.apply_financing_lin3).setOnClickListener(v -> {
            MainActivityEvent.showOrderManager.position = 3;
            EventBus.getDefault().post(MainActivityEvent.showOrderManager);
        });

        view.findViewById(R.id.apply_financing_lin4).setOnClickListener(v -> {
            MainActivityEvent.showOrderManager.position = 5;
            EventBus.getDefault().post(MainActivityEvent.showOrderManager);
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().unregister(this);
//        }
    }


//
//    @Subscribe
//    public void changeFragment(ApplyFinancingFragmentEvent event) {
//        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//        switch (event) {
//            case showCarInfo:
//                transaction.hide(mCurrentFragment).show(mCarInfoFragment);
//                mCurrentFragment = mCarInfoFragment;
//                break;
//            case showCreditInfo:
//                transaction.hide(mCurrentFragment).show(mCreditInfoFragment);
//                mCurrentFragment = mCreditInfoFragment;
//                break;
//            case changeCarInfo:
//                transaction.hide(mCurrentFragment).show(mCarInfoFragment);
//                mCurrentFragment = mCarInfoFragment;
//                mCarInfoFragment.changeCarInfo();
//                break;
//            case reset:
//                req = new SubmitOrderReq();
//                getChildFragmentManager()
//                        .beginTransaction()
//                        .remove(mCarInfoFragment)
//                        .remove(mCreditInfoFragment)
//                        .commit();
//                mCarInfoFragment = CarInfoFragment.newInstance();
//                mCreditInfoFragment = CreditInfoFragment.newInstance();
//                getChildFragmentManager()
//                        .beginTransaction()
//                        .add(R.id.apply_financing_container, mCarInfoFragment)
//                        .add(R.id.apply_financing_container, mCreditInfoFragment)
//                        .hide(mCreditInfoFragment)
////                        .hide(mCarInfoFragment)
//                        .commit();
//                mCurrentFragment = mCarInfoFragment;
//                EventBus.getDefault().post(MainActivityEvent.showOrderManager);
//                break;
//        }
//        transaction.commit();
//    }
}
