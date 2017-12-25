package com.yusion.shanghai.yusion4s.ui.order;

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
import com.yusion.shanghai.yusion4s.ui.yusion.apply.CreateUserActivity;
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
        // TODO: 2017/12/22  
        String why_come = intent.getStringExtra("why_come");
        if (why_come != null) {
            if ("car_select".equals(why_come)) {
                if (cartype.equals("新车")) {
                    mCarInfoFragment.getCarInfo(intent);
                } else {
                    mOldCarInfoFragment.getCarInfo(intent);
                }
            } else if ("create_user".equals(why_come)) {
                mCreditInfoFragment.relevance(intent);
            } else if ("dlr_select".equals(why_come)) {
                if (cartype.equals("新车")) {
                    mCarInfoFragment.getDlrInfo(intent);
                } else {
                    mOldCarInfoFragment.getDlrInfo(intent);
                }
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
            mCurrentFragment = mCarInfoFragment;
        } else {
            transaction.add(R.id.order_create_container, mOldCarInfoFragment)
                    .hide(mCreditInfoFragment);
            mCurrentFragment = mOldCarInfoFragment;
        }
        transaction.commit();
    }


    private void setTitle() {

        // TODO: 2017/12/22  
        if (cartype.equals("二手车")) {
            initTitleBar(this, "二手车申请").setLeftClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    back();
                }
            });
            initTitleBar(this, "sss").setLeftClickListener(this);
        } else {
//            initTitleBar(this, "新车申请").setLeftClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    back();
//                }
//            });
            // initTitleBar(this, "sss").setLeftClickListener(view -> onBackPressed());
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
        //super.onBackPressed();
        back();
    }

    private void back() {
        PopupDialogUtil.showTwoButtonsDialog(this, "是否放弃本次编辑？", "放弃", "取消", dialog -> {
            dialog.dismiss();
            finish();
        });
    }

    // TODO: 2017/12/22  
    public void anima() {
        startActivity(new Intent(this, CreateUserActivity.class));
        overridePendingTransition(R.anim.activity_enter, R.anim.stay);
    }
}
