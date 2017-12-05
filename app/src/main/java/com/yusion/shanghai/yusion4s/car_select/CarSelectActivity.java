package com.yusion.shanghai.yusion4s.car_select;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.bean.dlr.GetBrandResp;
import com.yusion.shanghai.yusion4s.bean.dlr.GetModelResp;
import com.yusion.shanghai.yusion4s.bean.dlr.GetTrixResp;
import com.yusion.shanghai.yusion4s.car_select.IndexBar.widget.IndexBar;
import com.yusion.shanghai.yusion4s.car_select.adapter.BrandAdapter;
import com.yusion.shanghai.yusion4s.car_select.adapter.EngGvAdapter;
import com.yusion.shanghai.yusion4s.car_select.adapter.ModelAdapter;
import com.yusion.shanghai.yusion4s.car_select.adapter.TrixAdapter;
import com.yusion.shanghai.yusion4s.car_select.model.CityBean;
import com.yusion.shanghai.yusion4s.car_select.suspension.SuspensionDecoration;
import com.yusion.shanghai.yusion4s.retrofit.api.DlrApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 进入该页面需要使用 startActivityForResult 并用intent携带 dlr_id 字符串
 * 选中品牌后会调用{@link CarSelectActivity#selectModel(GetModelResp)}将数据返回
 */
public class CarSelectActivity extends BaseActivity {

    private RecyclerView mBrandRv;
    private BrandAdapter mBrandAdapter;
    private List<CityBean> mDatas;
    private List<GetBrandResp> brandList = new ArrayList<>();
    private List<GetTrixResp> trixList = new ArrayList<>();
    private List<GetModelResp> modelList = new ArrayList<>();
    private IndexBar mIndexBar;
    private TextView mTvSideBarHint;
    private RecyclerView mTrixRv;
    private TrixAdapter mTrixAdapter;
    private SuspensionDecoration brandSuspensionDecoration;
    private BrandSelectDrawerLayout drawerLayout2;
    private BrandSelectDrawerLayout drawerLayout1;
    private SuspensionDecoration trixSuspensionDecoration;
    private SuspensionDecoration modelSuspensionDecoration;
    private LinearLayout mTrixPackUp;
    private RecyclerView mModelRv;
    private LinearLayout mModelPackUp;
    private ModelAdapter mModelAdapter;
    private List<EngCapBean> engCapBeanList = new ArrayList<>();
    private RecyclerView mEngGv;
    private EngGvAdapter engGvAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_select);
        initTitleBar(this, "选择车型");

        drawerLayout1 = findViewById(R.id.car_select_drawlayout1);
        drawerLayout2 = findViewById(R.id.car_select_drawlayout2);

        //品牌
        mBrandRv = findViewById(R.id.brand_rv);
        LinearLayoutManager brandManager = new LinearLayoutManager(this);
        mBrandRv.setLayoutManager(brandManager);
        mBrandAdapter = new BrandAdapter(this, brandList);
        mBrandAdapter.setOnItemClickListener((v, brandResp) -> showTrixList(brandResp));
        mBrandRv.setAdapter(mBrandAdapter);
        brandSuspensionDecoration = new SuspensionDecoration(this, brandList);
        mBrandRv.addItemDecoration(brandSuspensionDecoration);
        mBrandRv.addItemDecoration(new DividerItemDecoration(CarSelectActivity.this, DividerItemDecoration.VERTICAL_LIST));
        mTvSideBarHint = findViewById(R.id.tvSideBarHint);
        mIndexBar = findViewById(R.id.indexBar);
        mIndexBar.setmPressedShowTextView(mTvSideBarHint).setmLayoutManager(brandManager);


        initDatas();

        //车系
        mTrixRv = findViewById(R.id.trix_rv);
        mTrixPackUp = findViewById(R.id.trix_pack_up);
        mTrixPackUp.setOnClickListener(v -> drawerLayout1.closeDrawer(Gravity.RIGHT));
        LinearLayoutManager manager2 = new LinearLayoutManager(this);
        mTrixRv.setLayoutManager(manager2);
        mTrixAdapter = new TrixAdapter(this, trixList);
        mTrixRv.setAdapter(mTrixAdapter);
        trixSuspensionDecoration = new SuspensionDecoration(this, trixList);
        mTrixRv.addItemDecoration(trixSuspensionDecoration);
        mTrixRv.addItemDecoration(new DividerItemDecoration(CarSelectActivity.this, DividerItemDecoration.VERTICAL_LIST));
        mTrixAdapter.setDatas(trixList);
        mTrixAdapter.notifyDataSetChanged();
        mTrixAdapter.setOnItemClickListener((v, trixResp) -> showModelList(trixResp));
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

        //车型
        mModelRv = findViewById(R.id.model_rv);
        mModelPackUp = findViewById(R.id.model_pack_up);
        mModelPackUp.setOnClickListener(v -> drawerLayout2.closeDrawer(Gravity.RIGHT));
        LinearLayoutManager manager3 = new LinearLayoutManager(this);
        mModelRv.setLayoutManager(manager3);
        mModelAdapter = new ModelAdapter(this, modelList);
        mModelRv.setAdapter(mModelAdapter);
        modelSuspensionDecoration = new SuspensionDecoration(this, modelList);
        mModelRv.addItemDecoration(modelSuspensionDecoration);
        mModelRv.addItemDecoration(new DividerItemDecoration(CarSelectActivity.this, DividerItemDecoration.VERTICAL_LIST));
        mModelAdapter.setOnItemClickListener((v,modelResp) -> {
            selectModel(modelResp);
        });
        mEngGv = findViewById(R.id.eng_gv);
        mEngGv.setLayoutManager(new GridLayoutManager(this, 2));
        engGvAdapter = new EngGvAdapter(this, engCapBeanList);
        mEngGv.setAdapter(engGvAdapter);
    }

    private void selectModel(GetModelResp modelResp) {
        Intent intent = new Intent();
        intent.putExtra("modleResp", modelResp);
        setResult(RESULT_OK,intent);
        finish();
    }

    private void showModelList(GetTrixResp trixResp) {
        DlrApi.getModel(this, trixResp.trix_id, modelResp -> {
            if (modelResp == null) {
                Toast.makeText(myApp, "返回数据为空", Toast.LENGTH_SHORT).show();
                return;
            }
            drawerLayout2.openDrawer(Gravity.RIGHT);
            modelList.clear();
            modelList.addAll(modelResp);
            modelSuspensionDecoration.setmDatas(modelList);
            mModelAdapter.notifyDataSetChanged();

            getEngCapList(modelList);
        });
    }

    private void getEngCapList(List<GetModelResp> modelList) {
        engCapBeanList.clear();
        ArrayList<String> engList = new ArrayList<>();
        for (GetModelResp modelResp : modelList) {
            if (!engList.contains(modelResp.eng_cap)) {
                engList.add(modelResp.eng_cap);
            }
        }
        Collections.sort(engList, (o1, o2) -> o1.compareTo(o2));
        for (String engItem : engList) {
            EngCapBean engCapBean = new EngCapBean();
            engCapBean.eng_cap = engItem;
            engCapBeanList.add(engCapBean);
        }
        engGvAdapter.notifyDataSetChanged();
        Toast.makeText(this, engCapBeanList.toString(), Toast.LENGTH_SHORT).show();
    }

    private void showTrixList(GetBrandResp brandResp) {
        DlrApi.getTrix(this, brandResp.brand_id, trixResp -> {
            if (trixResp == null) {
                Toast.makeText(myApp, "返回数据为空", Toast.LENGTH_SHORT).show();
                return;
            }
            trixList.clear();
            trixList.addAll(trixResp);
            drawerLayout1.openDrawer(Gravity.RIGHT);
            trixSuspensionDecoration.setmDatas(trixList);
            mTrixAdapter.notifyDataSetChanged();
        });
    }

    private void initDatas() {
        String dlr_id = getIntent().getStringExtra("dlr_id");
        if (TextUtils.isEmpty(dlr_id)) {
            dlr_id = "YJCS0001";
        }
        DlrApi.getBrand(this, dlr_id, getBrandResp -> {
            if (getBrandResp == null) {
                Toast.makeText(myApp, "返回数据为空", Toast.LENGTH_SHORT).show();
                return;
            }
            brandList.clear();
            brandList.addAll(getBrandResp);

            //数据源排序并让其支持悬停 如果不适用indexBar而想选填，则必须手动调用排序的几个方法
            mIndexBar.setmSourceDatas(brandList).invalidate();
            mBrandAdapter.notifyDataSetChanged();
        });

    }

    public void updateDatas(View view) {
        for (int i = 0; i < 5; i++) {
            mDatas.add(new CityBean("东京"));
            mDatas.add(new CityBean("大阪"));
        }
        mIndexBar.setmSourceDatas(mDatas).invalidate();
        mBrandAdapter.notifyDataSetChanged();
    }
}
