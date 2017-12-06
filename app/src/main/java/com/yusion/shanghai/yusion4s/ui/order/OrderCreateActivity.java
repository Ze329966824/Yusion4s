package com.yusion.shanghai.yusion4s.ui.order;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.bean.order.submit.SubmitOrderReq;
import com.yusion.shanghai.yusion4s.event.ApplyFinancingFragmentEvent;
import com.yusion.shanghai.yusion4s.event.MainActivityEvent;
import com.yusion.shanghai.yusion4s.ui.entrance.apply_financing.CarInfoFragment;
import com.yusion.shanghai.yusion4s.ui.entrance.apply_financing.CreditInfoFragment;
import com.yusion.shanghai.yusion4s.ui.entrance.apply_financing.OldCarInfoFragment;
import com.yusion.shanghai.yusion4s.utils.PopupDialogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class OrderCreateActivity extends BaseActivity {

    private CarInfoFragment mCarInfoFragment;
    private CreditInfoFragment mCreditInfoFragment;
    private OldCarInfoFragment mOldCarInfoFragment;
    private Fragment mCurrentFragment;
    public SubmitOrderReq req = new SubmitOrderReq();
    public String cartype;
    public String file_id;
    public String label;
    public String region;
    public String bucket;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
//        setTitle();

        mCreditInfoFragment.relevance(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_create);
        EventBus.getDefault().register(this);

        setTitle();

        //initTitleBar(this, "申请融资");


        mCarInfoFragment = CarInfoFragment.newInstance();
        mCreditInfoFragment = CreditInfoFragment.newInstance();
        mOldCarInfoFragment = OldCarInfoFragment.newInstance();

         /*   测试时改变了显示的fragment  将mCreditInfoFragment显示，隐藏mCarInfoFragment
         *
         * 后续记得改回来
         */

//        getSupportFragmentManager()
//                .beginTransaction()
//                .add(R.id.order_create_container, mCarInfoFragment)
//                .add(R.id.order_create_container, mCreditInfoFragment)
//                .add(R.id.order_create_container, mOldCarInfoFragment)
//                //.hide(mCarInfoFragment)
//                .hide(mCreditInfoFragment)
//                .commit();
//        mCurrentFragment = mCarInfoFragment;
        getSupportFragmentManager().beginTransaction()
                .add(R.id.order_create_container, mCarInfoFragment)
                .add(R.id.order_create_container, mCreditInfoFragment)
                .add(R.id.order_create_container, mOldCarInfoFragment)
                .commit();
        if (cartype.equals("新车")) {
            getSupportFragmentManager().beginTransaction()
                    .hide(mCreditInfoFragment)
//                    .hide(mCarInfoFragment)
                    .hide(mOldCarInfoFragment)
                    .commit();
            mCurrentFragment = mCarInfoFragment;
        } else {
            getSupportFragmentManager().beginTransaction()
                    .hide(mCreditInfoFragment)
                    .hide(mCarInfoFragment)
                    .commit();
            mCurrentFragment = mOldCarInfoFragment;
        }


        //mCurrentFragment = mCreditInfoFragment;


    }

    private void setTitle() {
        cartype = getIntent().getStringExtra("car_type");
        if (cartype.equals("二手车")) {
            initTitleBar(this, "二手车申请").setLeftClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    back();
                }
            });
        } else {
            initTitleBar(this, "新车申请").setLeftClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    back();
                }
            });
        }
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
                if (cartype.equals("新车")) {
                    transaction.hide(mCurrentFragment).show(mCarInfoFragment);
                    mCurrentFragment = mCarInfoFragment;
                } else {
                    transaction.hide(mCurrentFragment).show(mOldCarInfoFragment);
                    mCurrentFragment = mOldCarInfoFragment;
                }
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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        back();
    }

    private void back() {
        PopupDialogUtil.showTwoButtonsDialog(this, "是否放弃本次编辑？", "放弃", "取消", new PopupDialogUtil.OnOkClickListener() {
            @Override
            public void onOkClick(Dialog dialog) {
                dialog.dismiss();
                finish();
            }
        });
    }
}
