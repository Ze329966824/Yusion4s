package com.yusion.shanghai.yusion4s.ui.yusion.apply;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.bean.ocr.OcrResp;
import com.yusion.shanghai.yusion4s.bean.user.ClientInfo;
import com.yusion.shanghai.yusion4s.event.ApplyActivityEvent;
import com.yusion.shanghai.yusion4s.ui.CommitActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import static com.yusion.shanghai.yusion4s.base.ActivityManager.finish;


public class ApplyActivity extends BaseActivity {
    private AutonymCertifyFragment mAutonymCertifyFragment;
    private PersonalInfoFragment mPersonalInfoFragment;
    private SpouseInfoFragment mSpouseInfoFragment;
    private Fragment mCurrentFragment;
    OcrResp mOcrRespByAutonymCertify = new OcrResp();

    public ClientInfo getMClientInfo() {
        return mClientInfo;
    }

    public void setMClientInfo(ClientInfo mClientInfo) {
        this.mClientInfo = mClientInfo;
    }

    public ClientInfo mClientInfo ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);

        initView();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onBackPressed() {}

    private void initView() {
        mClientInfo = new ClientInfo();
        initTitleBar(this, "创建客户").setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ApplyActivity.this).setMessage("您确定退出该页面返回首页?")
                        .setPositiveButton("确定退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        })
                        .setNegativeButton("取消退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();

            }
        });

        mAutonymCertifyFragment = new AutonymCertifyFragment();
        mPersonalInfoFragment = new PersonalInfoFragment();
        mSpouseInfoFragment = new SpouseInfoFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, mAutonymCertifyFragment)
                .add(R.id.container, mPersonalInfoFragment)
                .add(R.id.container, mSpouseInfoFragment)
//                .hide(mAutonymCertifyFragment)
                .hide(mPersonalInfoFragment)
                .hide(mSpouseInfoFragment)
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

            case showCommonRepaymentPeople:
                transaction.hide(mCurrentFragment).show(mSpouseInfoFragment);
                mCurrentFragment = mSpouseInfoFragment;
                break;
            default:
                break;
        }
        transaction.commit();
    }

}
