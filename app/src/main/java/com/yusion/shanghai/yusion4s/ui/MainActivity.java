package com.yusion.shanghai.yusion4s.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.Yusion4sApp;
import com.yusion.shanghai.yusion4s.base.ActivityManager;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.bean.auth.CheckUserInfoResp;
import com.yusion.shanghai.yusion4s.event.MainActivityEvent;
import com.yusion.shanghai.yusion4s.retrofit.api.AuthApi;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.ui.entrance.OrderManagerFragment;
import com.yusion.shanghai.yusion4s.ui.entrance.OrderManagerFragmentEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private ApplyFinancingFragment mApplyFinancingFragment; //申请融资
    private OrderManagerFragment mOrderManagerFragment;     //申请
    private MineFragment mMineFragment;                     //我的
    private Fragment mCurrentFragment;                      //当前的
    private FragmentManager mFragmentManager;
    private RadioButton applyOrderRb;
    private RadioButton orderListRb;
    private RadioButton mineRb;
    public Boolean isfirstLogin = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Yusion4sApp.isLogin = true;
        init();
    }

    private void init() {
        applyOrderRb = findViewById(R.id.main_tab_order_apply);
        orderListRb = findViewById(R.id.main_tab_order);
        mineRb = findViewById(R.id.main_tab_mine);
        mApplyFinancingFragment = ApplyFinancingFragment.newInstance();
        mMineFragment = MineFragment.newInstance();
        mOrderManagerFragment = OrderManagerFragment.newInstance();
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction()
                .add(R.id.main_container, mApplyFinancingFragment)
                .add(R.id.main_container, mMineFragment)
                .add(R.id.main_container, mOrderManagerFragment)
                .hide(mMineFragment)
                .hide(mOrderManagerFragment)
                .commit();
        mCurrentFragment = mApplyFinancingFragment;
        EventBus.getDefault().register(this);
        mApplyFinancingFragment.removeDrl();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onBackPressed() {
        ActivityManager.exit();
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        switch (v.getId()) {
            case R.id.main_tab_order_apply:
                transaction.hide(mCurrentFragment).show(mApplyFinancingFragment);
                mCurrentFragment = mApplyFinancingFragment;
                break;
            case R.id.main_tab_order:
                transaction.hide(mCurrentFragment).show(mOrderManagerFragment);
                mCurrentFragment = mOrderManagerFragment;
                break;
            case R.id.main_tab_mine:
                transaction.hide(mCurrentFragment).show(mMineFragment);
                mCurrentFragment = mMineFragment;
                break;
            default:
                break;
        }
        transaction.commit();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String cond = intent.getStringExtra("cond");
        if (!TextUtils.isEmpty(cond)) {
            if (cond.equals("二手车")) {
                Intent intent1 = new Intent(this, CommitActivity.class);
                intent1.putExtra("app_id", intent.getStringExtra("app_id"));
                intent1.putExtra("why_commit", "old_car");
                startActivity(intent1);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AuthApi.checkUserInfo(this, data -> {
            if (data == null) {
                return;
            }
            mMineFragment.refresh(data);
        });
        mApplyFinancingFragment.firstLogin();
    }

    @Subscribe
    public void changeFragment(MainActivityEvent event) {
        switch (event) {
            case showOrderManager:
                orderListRb.performClick();
                if (event.position == -1) {
                    break;
                }else {
                    OrderManagerFragmentEvent.showFragment.position =event.position;
                    mApplyFinancingFragment.removeImg(event.position);
                    EventBus.getDefault().post( OrderManagerFragmentEvent.showFragment);
                    break;
                }
            default:
                break;
        }
    }
}



