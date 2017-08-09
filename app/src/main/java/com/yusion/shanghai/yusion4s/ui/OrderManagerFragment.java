package com.yusion.shanghai.yusion4s.ui;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.BaseFragment;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderManagerFragment extends BaseFragment {


    public OrderManagerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_manager, container, false);
    }

    public static OrderManagerFragment newInstance() {

        Bundle args = new Bundle();

        OrderManagerFragment fragment = new OrderManagerFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTitleBar(view, "申请");

//        ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);
//        ArrayList<Fragment> mFragments = new ArrayList<>();
//        String[] mTabTitle = {"全部", "待审核", "审核失败", "待签约", "签约失败", "放款中", "放款成功", "放款失败"};
//        String[] mStCode = {"0", "2", "3", "4", "5", "6", "7", "8"};
//        for (int i = 0; i < mTabTitle.length; i++) {
//            mFragments.add(OrderItemFragment.newInstance(mStCode[i]));
//        }
//        viewPager.setOffscreenPageLimit(3);
//        viewPager.setAdapter(new OrderFragmentPagerAdapter(getChildFragmentManager(), mFragments));
//        MagicIndicator mMagicIndicator = (MagicIndicator) view.findViewById(R.id.tab_layout);
//        CommonNavigator commonNavigator = new CommonNavigator(mContext);
//        commonNavigator.setAdjustMode(false);
//        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
//            @Override
//            public int getCount() {
//                return mTabTitle == null ? 0 : mTabTitle.length;
//            }
//
//            @Override
//            public IPagerTitleView getTitleView(Context context, final int index) {
//                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
//                colorTransitionPagerTitleView.setNormalColor(0xFF999999);
//                colorTransitionPagerTitleView.setSelectedColor(0xFF06B7A3);
//                colorTransitionPagerTitleView.setText(mTabTitle[index]);
//                colorTransitionPagerTitleView.setOnClickListener(view -> viewPager.setCurrentItem(index));
//                return colorTransitionPagerTitleView;
//            }
//
//            @Override
//            public IPagerIndicator getIndicator(Context context) {
//                LinePagerIndicator indicator = new LinePagerIndicator(context);
//                indicator.setColors(getResources().getColor(R.color.system_color));
//                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
//                return indicator;
//            }
//        });
//        mMagicIndicator.setNavigator(commonNavigator);
//        ViewPagerHelper.bind(mMagicIndicator, viewPager);
    }

//    private class OrderFragmentPagerAdapter extends FragmentPagerAdapter {
//
//        private final List<Fragment> mFragments;
//
//        OrderFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
//            super(fm);
//            mFragments = fragments;
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            return mFragments.get(position);
//        }
//
//        @Override
//        public int getCount() {
//            return mFragments == null ? 0 : mFragments.size();
//        }
//
//    }
}

