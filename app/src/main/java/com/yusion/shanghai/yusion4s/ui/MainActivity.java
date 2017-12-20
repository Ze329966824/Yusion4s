package com.yusion.shanghai.yusion4s.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioButton;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.Yusion4sApp;
import com.yusion.shanghai.yusion4s.base.ActivityManager;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.event.MainActivityEvent;
import com.yusion.shanghai.yusion4s.retrofit.api.AuthApi;
import com.yusion.shanghai.yusion4s.retrofit.api.ConfigApi;
import com.yusion.shanghai.yusion4s.ui.entrance.LoginActivity;
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
    public Boolean isFirstLogin = true;
    private SpringSystem springSystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Yusion4sApp.isLogin = true;
        springSystem = SpringSystem.create();
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
        animateViewDirection(v, 0.8f, 1f, 100, 1);
//        animateViewDirection(v, 0.8f, 1f, 20, 5);
    }

    /**
     * 弹簧动画
     *
     * @param v        动画View
     * @param from     开始参数
     * @param to       结束参数
     * @param tension  拉力系数
     * @param friction 摩擦力系数
     */
    private void animateViewDirection(final View v, float from, float to, int tension, int friction) {
        //从弹簧系统创建一个弹簧
        Spring spring = springSystem.createSpring();
        //设置弹簧的开始参数
        spring.setCurrentValue(from);
        //查看源码可知
        //public static SpringConfig defaultConfig = fromOrigamiTensionAndFriction(40.0D, 7.0D);弹簧的默认拉力是40，摩擦是7。
        spring.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(tension, friction));
        //给弹簧添加监听，动态设置控件的状态
        spring.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                //设置图片的X,Y的缩放
                //还可以设置setAlpha,setTranslationX...综合形成复杂的动画
                v.setScaleX((float) spring.getCurrentValue());//0.8-1
                v.setScaleY((float) spring.getCurrentValue());
            }
        });
        //设置结束时图片的参数
        spring.setEndValue(to);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ConfigApi.getConfigJson(MainActivity.this, null);
        AuthApi.checkUserInfo(this, data -> {
            if (data == null) {
                return;
            }
            mMineFragment.refresh(data);
        });
        mApplyFinancingFragment.firstLogin();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //
        if (intent.getBooleanExtra("from_commit", false)) {
            changeFragment(MainActivityEvent.showOrderManager);
        }
    }

    @Subscribe
    public void changeFragment(MainActivityEvent event) {
        switch (event) {
            case showOrderManager:
                orderListRb.performClick();
                if (event.position == -1) {
                    break;
                } else {
                    OrderManagerFragmentEvent.showFragment.position = event.position;
                    mApplyFinancingFragment.removeImg(event.position);
                    EventBus.getDefault().post(OrderManagerFragmentEvent.showFragment);
                    break;
                }
            default:
                break;
        }
    }
}



