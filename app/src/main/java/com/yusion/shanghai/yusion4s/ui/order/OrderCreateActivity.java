package com.yusion.shanghai.yusion4s.ui.order;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.bean.order.submit.SubmitOrderReq;
import com.yusion.shanghai.yusion4s.event.ApplyFinancingFragmentEvent;
import com.yusion.shanghai.yusion4s.event.MainActivityEvent;
import com.yusion.shanghai.yusion4s.ui.entrance.apply_financing.CarInfoFragment;
import com.yusion.shanghai.yusion4s.ui.entrance.apply_financing.CreditInfoFragment;

import org.greenrobot.eventbus.EventBus;

public class OrderCreateActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_create);

        initTitleBar(this, "申请融资");
    }




    @Subscribe
    public void changeFragment(ApplyFinancingFragmentEvent event) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
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
                mCarInfoFragment.changeCarInfo();
                break;
            case reset:
                req = new SubmitOrderReq();
                getChildFragmentManager()
                        .beginTransaction()
                        .remove(mCarInfoFragment)
                        .remove(mCreditInfoFragment)
                        .commit();
                mCarInfoFragment = CarInfoFragment.newInstance();
                mCreditInfoFragment = CreditInfoFragment.newInstance();
                getChildFragmentManager()
                        .beginTransaction()
                        .add(R.id.apply_financing_container, mCarInfoFragment)
                        .add(R.id.apply_financing_container, mCreditInfoFragment)
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
