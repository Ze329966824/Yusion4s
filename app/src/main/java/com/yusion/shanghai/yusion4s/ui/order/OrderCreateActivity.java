package com.yusion.shanghai.yusion4s.ui.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

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
    private String why_come;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        why_come = intent.getStringExtra("why_come");
        if (!TextUtils.isEmpty(why_come)) {
            switch (cartype) {
                case "新车":
                    switch (why_come) {
                        case "car_select":
                            mCarInfoFragment.getCarInfo(intent);
                            break;
                        case "dlr_select":
                            mCarInfoFragment.getDlrInfo(intent);
                            break;
                        case "create_user":
                            mCreditInfoFragment.relevance(intent);
                            break;

                        default:
                            break;
                    }
                    break;
                case "二手车":
                    switch (why_come) {
                        case "car_select":
                            mOldCarInfoFragment.getCarInfo(intent);
                            break;
                        case "dlr_select":
                            mOldCarInfoFragment.getDlrInfo(intent);
                            break;
                        case "create_user":
                            mCreditInfoFragment.relevance(intent);
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_create);
        EventBus.getDefault().register(this);
        cartype = getIntent().getStringExtra("car_type");
        setTitle();

        //initTitleBar(this, "申请融资");

        initView();
    }

    private void initView() {
        if (cartype.equals("新车")) {
            mCarInfoFragment = CarInfoFragment.newInstance();
        } else if (cartype.equals("二手车")) {
            mOldCarInfoFragment = OldCarInfoFragment.newInstance();
        }
        mCreditInfoFragment = CreditInfoFragment.newInstance();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.add(R.id.order_create_container, mCreditInfoFragment);

        if (cartype.equals("新车")) {
            transaction.add(R.id.order_create_container, mCarInfoFragment)
                    .hide(mCreditInfoFragment);
//                    .hide(mCarInfoFragment);
            mCurrentFragment = mCarInfoFragment;
//            mCurrentFragment = mCreditInfoFragment;

        } else {
            transaction.add(R.id.order_create_container, mOldCarInfoFragment)
                    .hide(mCreditInfoFragment);
            mCurrentFragment = mOldCarInfoFragment;
        }
        transaction.commit();
    }


    private void setTitle() {
        if (cartype.equals("二手车")) {
                initTitleBar(this, "二手车申请");
        } else {
            initTitleBar(this, "新车申请");
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
        PopupDialogUtil.showTwoButtonsDialog(this, "是否放弃此次编辑？", "放弃", "取消", dialog -> {
            dialog.dismiss();
            finish();
        });
    }
}
