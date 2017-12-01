package com.yusion.shanghai.yusion4s.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.BaseFragment;
import com.yusion.shanghai.yusion4s.bean.auth.CheckUserInfoResp;
import com.yusion.shanghai.yusion4s.ui.entrance.WebViewActivity;
import com.yusion.shanghai.yusion4s.ui.entrance.apply_financing.HelperActivity;
import com.yusion.shanghai.yusion4s.ui.main.SettingsActivity;

public class MineFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout mFragmentSettingTvRelativeLayout;
    private LinearLayout mine_fragment_helper_tv;
    private LinearLayout mine_fragment_about_tv;
    private TextView mName;
    private TextView mMobile;
    private ImageView mRole;
    public MineFragment() {}

    public static MineFragment newInstance() {
        Bundle args = new Bundle();
        MineFragment fragment = new MineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_fragment_setting_tv:
                Intent intent3 = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent3);
                break;
            case R.id.mine_fragment_helper_tv:
                Intent intent1 = new Intent(getActivity(), HelperActivity.class);
                startActivity(intent1);
                break;

            case R.id.mine_fragment_about_tv:
                Intent intent2 = new Intent(getActivity(), WebViewActivity.class);
                intent2.putExtra("type", "homepage");
                startActivity(intent2);
                break;
            default:
                break;
        }
    }

    private void initView(View view) {
        mFragmentSettingTvRelativeLayout = view.findViewById(R.id.mine_fragment_setting_tv);
        mFragmentSettingTvRelativeLayout.setOnClickListener(this);
        mName = view.findViewById(R.id.mine_name_tv);
        mMobile = view.findViewById(R.id.mine_mobile_tv);
        mRole = view.findViewById(R.id.mine_role_img);
        mine_fragment_about_tv = view.findViewById(R.id.mine_fragment_about_tv);
        mine_fragment_about_tv.setOnClickListener(this);
        mine_fragment_helper_tv = view.findViewById(R.id.mine_fragment_helper_tv);
        mine_fragment_helper_tv.setOnClickListener(this);
    }

    public void refresh(CheckUserInfoResp resp) {
        if (TextUtils.isEmpty(resp.mobile)) {
            Toast.makeText(mContext, "错误:手机号为空！！！(UBT)", Toast.LENGTH_SHORT).show();
        }
        mName.setText(resp.name);
        mMobile.setText(resp.mobile);
        if (resp.role.equals("S")) {
            mRole.setImageResource(R.mipmap.mine_xs);
        } else if (resp.role.equals("D")) {
            mRole.setImageResource(R.mipmap.mine_jr);
        }
    }
}
