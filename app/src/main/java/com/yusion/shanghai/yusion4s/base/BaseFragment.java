package com.yusion.shanghai.yusion4s.base;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.umeng.analytics.MobclickAgent;
import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.widget.TitleBar;


public abstract class BaseFragment extends Fragment {

    public Context mContext;
    public FragmentActivity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        activity = getActivity();
    }

    public TitleBar initTitleBar(final View view, String title) {
        TitleBar titleBar = (TitleBar) view.findViewById(R.id.title_bar);
        titleBar.setTitle(title);
        titleBar.setLeftTextColor(Color.BLACK);
        titleBar.setRightTextColor(Color.BLACK);
        titleBar.setBackgroundResource(R.color.white);
        titleBar.setTitleColor(activity.getResources().getColor(R.color.black));
        titleBar.setDividerColor(activity.getResources().getColor(R.color.separate_line_color));
        return titleBar;
    }

    @Override
    public void onResume() {
        super.onResume();
//        UBT.addPageEvent(getContext(), "page_show", "fragment", getClass().getSimpleName());
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
//        UBT.addPageEvent(getContext(), "page_hidden", "fragment", getClass().getSimpleName());
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }


}
