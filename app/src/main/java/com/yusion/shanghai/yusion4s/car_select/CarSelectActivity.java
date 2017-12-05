package com.yusion.shanghai.yusion4s.car_select;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.bean.dlr.GetBrandResp;
import com.yusion.shanghai.yusion4s.bean.dlr.GetTrixResp;
import com.yusion.shanghai.yusion4s.car_select.IndexBar.helper.IndexBarDataHelperImpl;
import com.yusion.shanghai.yusion4s.car_select.IndexBar.widget.IndexBar;
import com.yusion.shanghai.yusion4s.car_select.adapter.BrandAdapter;
import com.yusion.shanghai.yusion4s.car_select.adapter.TrixAdapter;
import com.yusion.shanghai.yusion4s.car_select.model.CityBean;
import com.yusion.shanghai.yusion4s.car_select.suspension.SuspensionDecoration;
import com.yusion.shanghai.yusion4s.retrofit.api.DlrApi;

import java.util.ArrayList;
import java.util.List;

public class CarSelectActivity extends BaseActivity {

    private RecyclerView mBrandRv;
    private BrandAdapter mBrandAdapter;
    private List<CityBean> mDatas;
    private List<GetBrandResp> brandList = new ArrayList<>();
    private List<GetTrixResp> trixList = new ArrayList<>();
    private IndexBar mIndexBar;
    private TextView mTvSideBarHint;
    private RecyclerView mTrixRv;
    private TrixAdapter mTrixAdapter;
    private SuspensionDecoration decoration;
    private BrandSelectDrawerLayout drawerLayout2;
    private BrandSelectDrawerLayout drawerLayout1;
    private SuspensionDecoration trixSuspensionDecoration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_select);

        drawerLayout1 = findViewById(R.id.car_select_drawlayout1);
        drawerLayout2 = findViewById(R.id.car_select_drawlayout2);
//        drawerLayout1.setDrawerShadow(new ColorDrawable(getResources().getColor(R.color.system_color)),Gravity.RIGHT);
        drawerLayout1.setDrawerShadow(R.drawable.step_gradient_line_style1, Gravity.RIGHT & Gravity.LEFT);

        //品牌
        mBrandRv = findViewById(R.id.brand_rv);
        LinearLayoutManager brandManager = new LinearLayoutManager(this);
        mBrandRv.setLayoutManager(brandManager);
        mBrandAdapter = new BrandAdapter(this, brandList);
        mBrandAdapter.setOnItemClickListener((v, brandResp) -> showTrixList(brandResp));
        mBrandRv.setAdapter(mBrandAdapter);
        decoration = new SuspensionDecoration(this, brandList);
        mBrandRv.addItemDecoration(decoration);
        mBrandRv.addItemDecoration(new DividerItemDecoration(CarSelectActivity.this, DividerItemDecoration.VERTICAL_LIST));
        mTvSideBarHint = findViewById(R.id.tvSideBarHint);
        mIndexBar = findViewById(R.id.indexBar);
        mIndexBar.setmPressedShowTextView(mTvSideBarHint).setmLayoutManager(brandManager);


        initDatas();

        //车系
        mTrixRv = findViewById(R.id.trix_rv);
        LinearLayoutManager manager2 = new LinearLayoutManager(this);
        mTrixRv.setLayoutManager(manager2);
        mTrixAdapter = new TrixAdapter(this, trixList);
        mTrixRv.setAdapter(mTrixAdapter);
        trixSuspensionDecoration = new SuspensionDecoration(this, trixList);
        mTrixRv.addItemDecoration(trixSuspensionDecoration);
        mTrixRv.addItemDecoration(new DividerItemDecoration(CarSelectActivity.this, DividerItemDecoration.VERTICAL_LIST));
        mTrixAdapter.setDatas(trixList);
        mTrixAdapter.notifyDataSetChanged();
        mTrixAdapter.setOnItemClickListener(v -> drawerLayout2.openDrawer(Gravity.RIGHT));
        drawerLayout2.addDrawerListener(new BrandSelectDrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                drawerLayout1.setDrawerLockMode(BrandSelectDrawerLayout.LOCK_MODE_LOCKED_OPEN);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                drawerLayout1.setDrawerLockMode(BrandSelectDrawerLayout.LOCK_MODE_UNLOCKED);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
    }

    private void showTrixList(GetBrandResp brandResp) {
        DlrApi.getTrix(this, brandResp.brand_id, trixResp -> {
            trixList.clear();
            trixList.addAll(trixResp);
            drawerLayout1.openDrawer(Gravity.RIGHT);
            IndexBarDataHelperImpl indexBarDataHelper = new IndexBarDataHelperImpl();
            indexBarDataHelper.sortSourceDatas(trixList);
            indexBarDataHelper.convert(trixList);
            indexBarDataHelper.fillInexTag(trixList);
            mTrixAdapter.setDatas(trixList);
            mTrixAdapter.notifyDataSetChanged();
            trixSuspensionDecoration.setmDatas(trixList);
        });
    }

    private void initDatas() {
        DlrApi.getBrand(this, "YJCS0001", getBrandResp -> {
            brandList.clear();
            brandList.addAll(getBrandResp);

            //会排序
            mIndexBar.setmSourceDatas(brandList).invalidate();

            mBrandAdapter.setDatas(brandList);
            mBrandAdapter.notifyDataSetChanged();
            decoration.setmDatas(brandList);
        });

    }

    public void updateDatas(View view) {
        for (int i = 0; i < 5; i++) {
            mDatas.add(new CityBean("东京"));
            mDatas.add(new CityBean("大阪"));
        }
        mIndexBar.setmSourceDatas(mDatas)
                .invalidate();
        mBrandAdapter.notifyDataSetChanged();
    }
}
