package com.yusion.shanghai.yusion4s.ui.order;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.bean.order.submit.SubmitOrderReq;
import com.yusion.shanghai.yusion4s.event.ApplyFinancingFragmentEvent;
import com.yusion.shanghai.yusion4s.event.MainActivityEvent;
import com.yusion.shanghai.yusion4s.ui.entrance.apply_financing.CarInfoFragment;
import com.yusion.shanghai.yusion4s.ui.entrance.apply_financing.CreditInfoFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class OrderCreateActivity extends BaseActivity {

    private CarInfoFragment mCarInfoFragment;
    private CreditInfoFragment mCreditInfoFragment;
    private Fragment mCurrentFragment;
    public SubmitOrderReq req = new SubmitOrderReq();
    public String cartype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_create);
        EventBus.getDefault().register(this);

        //initTitleBar(this, "申请融资");
        cartype = getIntent().getStringExtra("car_type");
//        if (cartype.equals("二手车")) {
//            initTitleBar(this, "二手车申请");
//        } else {
//            initTitleBar(this, "新车申请");
//        }

        mCarInfoFragment = CarInfoFragment.newInstance();
        mCreditInfoFragment = CreditInfoFragment.newInstance();

         /*   测试时改变了显示的fragment  将mCreditInfoFragment显示，隐藏mCarInfoFragment
         *
         * 后续记得改回来
         */


        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.order_create_container, mCarInfoFragment)
                .add(R.id.order_create_container, mCreditInfoFragment)
                //.hide(mCarInfoFragment)
                .hide(mCreditInfoFragment)
                .commit();
        mCurrentFragment = mCarInfoFragment;


        //mCurrentFragment = mCreditInfoFragment;


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe
    public void changeFragment(ApplyFinancingFragmentEvent event) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (event) {
            case showCarInfo:
                transaction.hide(mCurrentFragment).show(mCarInfoFragment);
                mCurrentFragment = mCarInfoFragment;
                break;
            case showCreditInfo:
                transaction.hide(mCurrentFragment).show(mCreditInfoFragment);
                mCurrentFragment = mCreditInfoFragment;
                break;
            case changeCarInfo:
                transaction.hide(mCurrentFragment).show(mCarInfoFragment);
                mCurrentFragment = mCarInfoFragment;
//                mCarInfoFragment.changeCarInfo();
                break;
            case reset:
                req = new SubmitOrderReq();
                getSupportFragmentManager()
                        .beginTransaction()
                        .remove(mCarInfoFragment)
                        .remove(mCreditInfoFragment)
                        .commit();
                mCarInfoFragment = CarInfoFragment.newInstance();
                mCreditInfoFragment = CreditInfoFragment.newInstance();
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.order_create_container, mCarInfoFragment)
                        .add(R.id.order_create_container, mCreditInfoFragment)
                        .hide(mCreditInfoFragment)
//                        .hide(mCarInfoFragment)
                        .commit();
                mCurrentFragment = mCarInfoFragment;
                EventBus.getDefault().post(MainActivityEvent.showOrderManager);
                break;
            default:
                break;
        }
        transaction.commit();
    }
}
