package com.yusion.shanghai.yusion4s.car_select;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
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
import com.yusion.shanghai.yusion4s.utils.DensityUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 进入该页面需要使用 startActivity 并用intent携带页面结束后需要跳转的 class 类对象
 * eg:  Intent intent = new Intent(this, CarSelectActivity.class);
 * intent.putExtra("class", LoginActivity.class);//选择完车型后跳转到LoginActivity
 * intent.putExtra("dlr_id", xxx);
 * intent.putExtra("vehicle_cond", xxx);
 * intent.putExtra("should_reset", true);//true表示重置该页面 默认false
 * startActivity(intent);
 * <p>
 * 该class类对象启动模式必须是 singleTask
 * 选中品牌后会调用{@link CarSelectActivity#selectModel(GetModelResp)}将数据返回
 * 返回到之前的activity时会回调其 onNewIntent 方法
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
    private List<GetModelResp> rawModelList;
    private GetTrixResp currentTrixResp;
    private GetBrandResp currentBrandResp;


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (getIntent().getBooleanExtra("should_reset", false)) {
            drawerLayout2.closeDrawer(Gravity.RIGHT);
            drawerLayout1.closeDrawer(Gravity.RIGHT);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_select);
        initTitleBar(this, "选择车型").setLeftClickListener(v -> startActivity(new Intent(this, (Class<?>) getIntent().getExtras().get("class"))));
        Log.e("TAG", "onCreate: ");

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

                //关闭的时候需要将被选中的块颜色去掉
                for (GetTrixResp trixResp : trixList) {
                    trixResp.has_select_by_user = false;
                }
                mTrixAdapter.notifyDataSetChanged();
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
        drawerLayout1.addDrawerListener(new BrandSelectDrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                //关闭的时候需要将被选中的块颜色去掉
                for (GetBrandResp brandResp : brandList) {
                    brandResp.has_select_by_user = false;
                }
                mBrandAdapter.notifyDataSetChanged();
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
        mModelAdapter.setOnItemClickListener((v, modelResp) -> {
            selectModel(modelResp);
        });
        mEngGv = findViewById(R.id.eng_gv);
        mEngGv.setLayoutManager(new GridLayoutManager(this, 3));
        mEngGv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int childLayoutPosition = parent.getChildLayoutPosition(view);
                if (childLayoutPosition > -1 && childLayoutPosition < 3) {
                    outRect.top = DensityUtil.dip2px(CarSelectActivity.this, 9);
                }
                outRect.right = DensityUtil.dip2px(CarSelectActivity.this, 11);
                outRect.bottom = DensityUtil.dip2px(CarSelectActivity.this, 9);
            }
        });
        engGvAdapter = new EngGvAdapter(this, engCapBeanList);
        engGvAdapter.setOnItemCheckedChangeListener(new EngGvAdapter.OnItemCheckedChangeListener() {
            @Override
            public void onItemCheckedChange(View v, boolean isChecked, EngCapBean engCapBean) {
                boolean hasItemSelected = false;
                List<EngCapBean> hasSelectedEngCap = new ArrayList<>();
                for (EngCapBean capBean : engCapBeanList) {
                    if (capBean.has_selected) {
                        hasItemSelected = true;
                        hasSelectedEngCap.add(capBean);
                    }
                }
                modelList.clear();
                if (hasItemSelected) {
                    List<String> hasSelectedEngCapString = new ArrayList<>();
                    for (EngCapBean bean : hasSelectedEngCap) {
                        hasSelectedEngCapString.add(bean.eng_cap);
                    }
                    for (GetModelResp modelResp : rawModelList) {
                        if (hasSelectedEngCapString.contains(modelResp.eng_cap)) {
                            modelList.add(modelResp);
                        }
                    }
                } else {
                    modelList.addAll(rawModelList);
                }
                mModelAdapter.notifyDataSetChanged();
            }
        });
        mEngGv.setAdapter(engGvAdapter);

    }


    private void selectModel(GetModelResp modelResp) {
        Intent intent = getIntent();
        Class toClass = (Class) intent.getExtras().get("class");
        Intent intent2 = new Intent();
        intent2.putExtra("modleResp", modelResp);
        setResult(RESULT_OK, intent2);
        intent2.setClass(this, toClass);
        intent2.putExtra("brand_che300_id", currentBrandResp.che_300_id);
        intent2.putExtra("trix_che300_id", currentTrixResp.che_300_id);
        intent2.putExtra("why_come", "car_select");
        startActivity(intent2);
//        finish();
    }

    private void showModelList(GetTrixResp trixResp) {
        currentTrixResp = trixResp;
        DlrApi.getModel(this, trixResp.trix_id, getIntent().getStringExtra("vehicle_cond"), modelResp -> {
            rawModelList = modelResp;
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
    }

    private void showTrixList(GetBrandResp brandResp) {
        currentBrandResp = brandResp;
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
