package com.yusion.shanghai.yusion4s.ui.entrance;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.Yusion4sApp;
import com.yusion.shanghai.yusion4s.base.BaseFragment;
import com.yusion.shanghai.yusion4s.ui.entrance.apply_financing.CarInfoFragment;
import com.yusion.shanghai.yusion4s.ui.entrance.apply_financing.CreditInfoFragment;
import com.yusion.shanghai.yusion4s.widget.SwitchButton;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderManagerFragment extends BaseFragment {

    private CarInfoFragment mCarInfoFragment;
    private CreditInfoFragment mCreditInfoFragment;
    private Fragment mCurrentFragment;
    private ViewPager viewPager;
    private ArrayList<OrderItemFragment> mFragments;

    public static OrderManagerFragment newInstance() {

        Bundle args = new Bundle();

        OrderManagerFragment fragment = new OrderManagerFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_manager, container, false);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        initTitleBar(view, "申请");
        //setBackHide();

        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mFragments = new ArrayList<>();
        //网络请求，映射的应该是key value
        //WheelViewUtil.showWheelView(((Yusion4sApp) getActivity().getApplication()).getConfigResp().owner_applicant_relation_key,
        //Yusion4sApp.getConfigResp().order_type_value;
        List<String> mTabTitle = ((Yusion4sApp) getActivity().getApplication()).getConfigResp().order_type_value;
        List<String> mStCode = ((Yusion4sApp) getActivity().getApplication()).getConfigResp().order_type_key;
        Log.e("TAG", "mTabTitle: "+mTabTitle.toString());
        Log.e("TAG", "mStCode: "+mStCode.toString());
        //String[] mTabTitle = {"全部", "待审核", "审核失败", "待确认金融方案", "放款中", "放款成功", "已取消"};
        //String[] mStCode = {"0", "2", "3", "4", "6", "7", "9"};
//        for (int i = 0; i < mTabTitle.length; i++) {
//            mFragments.add(OrderItemFragment.newInstance(mStCode[i]));
//        }
        for (int i = 0; i < mTabTitle.size(); i++) {
            OrderItemFragment fragment = OrderItemFragment.newInstance(mStCode.get(i),"新车");
//            fragment.setVehicle_cond("新车");
            mFragments.add(fragment);
        }
        viewPager.setAdapter(new OrderFragmentPagerAdapter(getChildFragmentManager(), mFragments));
       // viewPager.setCurrentItem(1);
        MagicIndicator mMagicIndicator = (MagicIndicator) view.findViewById(R.id.tab_layout);
        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigator.setAdjustMode(false);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mTabTitle.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setNormalColor(0xFF999999);
                colorTransitionPagerTitleView.setSelectedColor(0xFF06B7A3);
                //colorTransitionPagerTitleView.setText(mTabTitle[index]);
                colorTransitionPagerTitleView.setText(mTabTitle.get(index));
                colorTransitionPagerTitleView.setOnClickListener(view -> viewPager.setCurrentItem(index));
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setColors(getResources().getColor(R.color.system_color));
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                return indicator;
            }
        });
        mMagicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mMagicIndicator, viewPager);


        SwitchButton sb = (SwitchButton) view.findViewById(R.id.order_manager_sb);
        final TextView newCar = (TextView) view.findViewById(R.id.order_manager_new_car);
        final TextView oldCar = (TextView) view.findViewById(R.id.order_manager_old_car);
        sb.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    newCar.setTextColor(Color.parseColor("#666666"));
                    oldCar.setTextColor(Color.parseColor("#ffffff"));
                    onSelectOldCar();
                } else {
                    newCar.setTextColor(Color.parseColor("#ffffff"));
                    oldCar.setTextColor(Color.parseColor("#666666"));
                    onSelectNewCar();
                }
            }
        });
        {
            newCar.setTextColor(Color.parseColor("#ffffff"));
            oldCar.setTextColor(Color.parseColor("#666666"));
        }
    }

    private void onSelectOldCar() {
        for (OrderItemFragment fragment : mFragments) {
            fragment.setVehicle_cond("二手车");
            if (fragment.isResumed()) {
                fragment.refresh();
            }
        }
    }

    private void onSelectNewCar() {
        for (OrderItemFragment fragment : mFragments) {
            fragment.setVehicle_cond("新车");
            if (fragment.isResumed()) {
                fragment.refresh();
            }
        }
    }

    private class OrderFragmentPagerAdapter extends FragmentPagerAdapter {

        private final List<OrderItemFragment> mFragments;

        OrderFragmentPagerAdapter(FragmentManager fm, List<OrderItemFragment> fragments) {
            super(fm);
            mFragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments == null ? 0 : mFragments.size();
        }

    }
    @Subscribe
    public void changeFragment(OrderManagerFragmentEvent event){
        Log.e("TAG", "changeFragment: "+event);
        switch (event){
            case showFragment:
                viewPager.setCurrentItem(event.position);
                break;
            default:
                break;
        }
    }
}
