package com.yusion.shanghai.yusion4s.ui;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.ui.entrance.OrderManagerFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private MineFragment mMineFragment;//我的
    private Fragment mCurrentFragment;//当前的
    private FragmentManager mFragmentManager;
    private ApplyFinancingFragment mApplyFinancingFragment;//申请融资
    private OrderManagerFragment mOrderManagerFragment;//申请

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        WangDai4sApp.isBack2Home = false;
//        setContentView(R.layout.activity_main);
//
//        Log.e("TAG", "Token: " + WangDai4sApp.mToken);
//
//        if (getIntent().getBooleanExtra("toJPushDialogActivity", false)) {
//            Intent intent = getIntent();
//            intent.setClass(this, JPushDialogActivity.class);
//            startActivity(getIntent());
//        }

        mApplyFinancingFragment = ApplyFinancingFragment.newInstance();
        mMineFragment = MineFragment.newInstance();
//        mOrderListFragment = OrderListFragment.newInstance();
        mOrderManagerFragment = OrderManagerFragment.newInstance();
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction()
                .add(R.id.main_container, mApplyFinancingFragment)
                .add(R.id.main_container, mMineFragment)
                .add(R.id.main_container, mOrderManagerFragment)
                .hide(mMineFragment)
//                .hide(mApplyFinancingFragment)
                .hide(mOrderManagerFragment)
                .commit();
        mCurrentFragment = mApplyFinancingFragment;

    }
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        WangDai4sApp.isBack2Home = true;
//    }


    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        switch (v.getId()) {
//            case R.id.main_tab_order_apply:
//                transaction.hide(mCurrentFragment).show(mOrderApplyFragment);
//                mCurrentFragment = mOrderApplyFragment;
//                break;
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
        }
        transaction.commit();
    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        AuthApi.checkUserInfo(this, "正在获取用户资料...", new OnDataCallBack<CheckUserInfoResp>() {
//            @Override
//            public void callBack(CheckUserInfoResp resp) {
//                mMineFragment.refresh(resp);
//            }
//        });
//    }
//
//    @NeedsPermission({Manifest.permission.READ_CALENDAR})
//    void uploadUserData4Calendar() {
//        JSONArray calendar = MobileDataUtil.getUserData(this, "calendar");
//        if (calendar.length() != 0) {
//            CustomerApi.postMobileData(this, new MobileDataReq(WangDai4sApp.mMobile
//                    , new MobileDataReq.DeviceInfo(JPushInterface.getRegistrationID(this))
//                    , new MobileDataReq.Content(calendar, "calendar")), 5);
//        }
//
//        MainActivityPermissionsDispatcher.uploadUserData4PhotoWithCheck(this);
//    }
//
//    @OnPermissionDenied({Manifest.permission.READ_CALENDAR})
//    void OnPermissionDenied4Calendar() {
//        MainActivityPermissionsDispatcher.uploadUserData4PhotoWithCheck(this);
//    }
//
//    @OnNeverAskAgain({Manifest.permission.READ_CALENDAR})
//    void OnNeverAskAgain4Calendar() {
//        MainActivityPermissionsDispatcher.uploadUserData4PhotoWithCheck(this);
//    }
//
//    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE})
//    void uploadUserData4Photo() {
//        //小米手机读取存储卡不需要权限
//        CustomerApi.postMobileData(this, new MobileDataReq(WangDai4sApp.mMobile
//                , new MobileDataReq.DeviceInfo(JPushInterface.getRegistrationID(this))
//                , new MobileDataReq.Content(MobileDataUtil.getUserData(this, "photo"), "photo")), 5);
//        MainActivityPermissionsDispatcher.uploadUserData4CallLogWithCheck(this);
//    }
//
//    @OnPermissionDenied({Manifest.permission.READ_EXTERNAL_STORAGE})
//    void OnPermissionDenied4Photo() {
//        MainActivityPermissionsDispatcher.uploadUserData4CallLogWithCheck(this);
//    }
//
//    @OnNeverAskAgain({Manifest.permission.READ_EXTERNAL_STORAGE})
//    void OnNeverAskAgain4Photo() {
//        MainActivityPermissionsDispatcher.uploadUserData4CallLogWithCheck(this);
//    }
//
//    @NeedsPermission({Manifest.permission.READ_CALL_LOG})
//    void uploadUserData4CallLog() {
//        JSONArray call_log = MobileDataUtil.getUserData(this, "call_log");
//        if (call_log.length() != 0) {
//            CustomerApi.postMobileData(this, new MobileDataReq(WangDai4sApp.mMobile
//                    , new MobileDataReq.DeviceInfo(JPushInterface.getRegistrationID(this))
//                    , new MobileDataReq.Content(MobileDataUtil.getUserData(this, "call_log"), "call_log")), 5);
//        }
//        MainActivityPermissionsDispatcher.uploadUserData4ContactsWithCheck(this);
//    }
//
//    @OnPermissionDenied({Manifest.permission.READ_CALL_LOG})
//    void OnPermissionDenied4CallLog() {
//        MainActivityPermissionsDispatcher.uploadUserData4ContactsWithCheck(this);
//    }
//
//    @OnNeverAskAgain({Manifest.permission.READ_CALL_LOG})
//    void OnNeverAskAgain4CallLog() {
//        MainActivityPermissionsDispatcher.uploadUserData4ContactsWithCheck(this);
//    }
//
//    @NeedsPermission({Manifest.permission.READ_CONTACTS})
//    void uploadUserData4Contacts() {
//        JSONArray contactJson = MobileDataUtil.getUserData(this, "contact");
//        JSONArray sim = MobileDataUtil.getUserData(this, "sim");
//        for (int i = 0; i < sim.length(); i++) {
//            JSONObject jsonObject = null;
//            try {
//                jsonObject = sim.getJSONObject(i);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            contactJson.put(jsonObject);
//        }
//        CustomerApi.postMobileData(this, new MobileDataReq(WangDai4sApp.mMobile
//                , new MobileDataReq.DeviceInfo(JPushInterface.getRegistrationID(this))
//                , new MobileDataReq.Content(contactJson, "contact")), 5);
//    }
//
//    @OnPermissionDenied({Manifest.permission.READ_CONTACTS})
//    void OnPermissionDenied4Contacts() {
//    }
//
//    @OnNeverAskAgain({Manifest.permission.READ_CONTACTS})
//    void OnNeverAskAgain4Contacts() {
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }
}



