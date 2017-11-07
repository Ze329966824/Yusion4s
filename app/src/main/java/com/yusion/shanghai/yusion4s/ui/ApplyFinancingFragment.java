package com.yusion.shanghai.yusion4s.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.BaseFragment;
import com.yusion.shanghai.yusion4s.bean.order.submit.SubmitOrderReq;
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

        view.findViewById(R.id.apply_financing_cteate_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(mContext, OrderCreateActivity.class);
                startActivity(i1);
            }
        });

        view.findViewById(R.id.apply_financing_triangle_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(mContext, ChangeDLRActivity.class);
                startActivity(i2);
            }
        });

        view.findViewById(R.id.apply_financing_lin1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(2);
            }
        });

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
