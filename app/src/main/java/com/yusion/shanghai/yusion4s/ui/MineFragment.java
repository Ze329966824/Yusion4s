package com.yusion.shanghai.yusion4s.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yusion.shanghai.yusion4s.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends Fragment {

//    private LinearLayout mFragmentCalculatorTvRelativeLayout;
//    private LinearLayout mFragmentSettingTvRelativeLayout;
//    private LinearLayout mFragmentEmployeeTvTextView;
//    private TextView mName;
//    private TextView mMobile;
//    private ImageView mRole;


    public MineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }
    public static MineFragment newInstance() {

        Bundle args = new Bundle();

        MineFragment fragment = new MineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        initView(view);
    }

//    private void initView(View view) {
//        mFragmentEmployeeTvTextView = (LinearLayout) view.findViewById(R.id.mine_fragment_employee_tv);
//        mFragmentEmployeeTvTextView.setOnClickListener(this);
//        mFragmentCalculatorTvRelativeLayout = (LinearLayout) view.findViewById(R.id.mine_fragment_calculator_tv);
//        mFragmentCalculatorTvRelativeLayout.setOnClickListener(this);
//        mFragmentSettingTvRelativeLayout = (LinearLayout) view.findViewById(R.id.mine_fragment_setting_tv);
//        mFragmentSettingTvRelativeLayout.setOnClickListener(this);
//        mName = ((TextView) view.findViewById(R.id.mine_name_tv));
//        mMobile = ((TextView) view.findViewById(R.id.mine_mobile_tv));
//        mRole = ((ImageView) view.findViewById(R.id.mine_role_img));
//    }
//
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.mine_fragment_employee_tv:
//                Intent intent = new Intent(getActivity(), StaffManageActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.mine_fragment_calculator_tv:
//                Intent intent2 = new Intent(getActivity(), WebViewActivity.class);
//                intent2.putExtra("title", getResources().getString(R.string.main_calculator));
//                intent2.putExtra("url", JsonUtils.getConfigValueFromKey("calc_url"));
//                startActivity(intent2);
//                break;
//            case R.id.mine_fragment_setting_tv:
//                Intent intent3 = new Intent(getActivity(), SettingActivity.class);
//                startActivity(intent3);
//                break;
//        }
//    }
//
//    public void refresh(CheckUserInfoResp resp) {
//        mName.setText(resp.name);
//        mMobile.setText(resp.mobile);
//        if (resp.role.equals("S")) {
//            mRole.setImageResource(R.mipmap.mine_xs);
//            mFragmentEmployeeTvTextView.setVisibility(View.GONE);
//        } else if (resp.role.equals("D")) {
//            mRole.setImageResource(R.mipmap.mine_jr);
////            mFragmentEmployeeTvTextView.setVisibility(View.VISIBLE);
//        }
//    }
}
