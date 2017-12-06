package com.yusion.shanghai.yusion4s.ui.yusion.apply;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.bean.user.ClientInfo;
import com.yusion.shanghai.yusion4s.event.ApplyActivityEvent;
import com.yusion.shanghai.yusion4s.ui.CommitActivity;
import com.yusion.shanghai.yusion4s.utils.PopupDialogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class ApplyActivity extends BaseActivity {
    private AutonymCertifyFragment mAutonymCertifyFragment;       //征信信息
    private PersonalInfoFragment mPersonalInfoFragment;           //个人信息
    private SpouseInfoFragment mSpouseInfoFragment;               //配偶信息
    private Fragment mCurrentFragment;
    public ClientInfo mClientInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);
        //启动动画
        overridePendingTransition(R.anim.pop_enter_anim, R.anim.pop_exit_anim);
        initView();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onBackPressed() {
        if (mCurrentFragment == mAutonymCertifyFragment) {
            Toast.makeText(this, "已经是第一页了！", Toast.LENGTH_SHORT).show();
        } else if (mCurrentFragment == mPersonalInfoFragment) {
            changeFragment(ApplyActivityEvent.showAutonymCertifyFragment);
        } else {
            changeFragment(ApplyActivityEvent.showPersonalInfoFragment);
        }
    }


    private void initView() {
        mClientInfo = new ClientInfo();
        initTitleBar(this, "创建客户").setLeftImageResource(R.mipmap.create_finish_icon).setLeftClickListener(v ->
                PopupDialogUtil.showTwoButtonsDialog(ApplyActivity.this, "您确定退出该页面返回首页", "确定退出", "取消退出?", dialog -> {
                    dialog.dismiss();
                    finish();
                }));
        mAutonymCertifyFragment = new AutonymCertifyFragment();
        mPersonalInfoFragment = new PersonalInfoFragment();
        mSpouseInfoFragment = new SpouseInfoFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, mAutonymCertifyFragment)
                .add(R.id.container, mPersonalInfoFragment)
                .add(R.id.container, mSpouseInfoFragment)
                .hide(mAutonymCertifyFragment)
                .hide(mPersonalInfoFragment)
//                .hide(mSpouseInfoFragment)
                .commit();
        mCurrentFragment = mAutonymCertifyFragment;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void finish() {
        super.finish();
        //退出动画
        overridePendingTransition(R.anim.pop_enter_anim, R.anim.pop_exit_anim);
    }

    //填完所有信息二次确认后走这里
    public void requestSubmit() {
        Intent intent = getIntent();
        intent.setClass(ApplyActivity.this, CommitActivity.class);
        intent.putExtra("why_commit", "create_user");
        intent.putExtra("id_no", mClientInfo.id_no);
        startActivity(intent);
        finish();
    }

    @Subscribe
    public void changeFragment(ApplyActivityEvent event) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (event) {
            case showAutonymCertifyFragment:
                transaction.hide(mCurrentFragment).show(mAutonymCertifyFragment);
                mCurrentFragment = mAutonymCertifyFragment;
                break;
            case showPersonalInfoFragment:
                transaction.hide(mCurrentFragment).show(mPersonalInfoFragment);
                mCurrentFragment = mPersonalInfoFragment;
                break;
            case showSpouseInfoFragment:
                transaction.hide(mCurrentFragment).show(mSpouseInfoFragment);
                mCurrentFragment = mSpouseInfoFragment;
                break;
            default:
                break;
        }
        transaction.commit();
    }

}
