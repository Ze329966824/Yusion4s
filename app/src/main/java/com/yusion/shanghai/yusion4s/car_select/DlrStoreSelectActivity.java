package com.yusion.shanghai.yusion4s.car_select;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.bean.dlr.GetDlrListByTokenResp;
import com.yusion.shanghai.yusion4s.bean.dlr.GetStoreList;
import com.yusion.shanghai.yusion4s.bean.dlr.GetTrixResp;
import com.yusion.shanghai.yusion4s.car_select.IndexBar.widget.IndexBar;
import com.yusion.shanghai.yusion4s.car_select.adapter.DlrAdapter;
import com.yusion.shanghai.yusion4s.car_select.adapter.DlrStoreAdapter;
import com.yusion.shanghai.yusion4s.car_select.suspension.SuspensionDecoration;
import com.yusion.shanghai.yusion4s.retrofit.api.DlrApi;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;

import java.util.ArrayList;
import java.util.List;

public class DlrStoreSelectActivity extends BaseActivity {
    private RecyclerView mBrandRv;
    private DlrAdapter mBrandAdapter;

    private List<GetDlrListByTokenResp> brandList = new ArrayList<>();

    private List<GetStoreList> trixList = new ArrayList<>();

    private IndexBar mIndexBar;
    private TextView mTvSideBarHint;
    private RecyclerView mTrixRv;
    private DlrStoreAdapter mDlrStoreAdapter;
    private SuspensionDecoration brandSuspensionDecoration;
    private BrandSelectDrawerLayout drawerLayout2;
    private BrandSelectDrawerLayout drawerLayout1;
    private SuspensionDecoration trixSuspensionDecoration;
    private LinearLayout mTrixPackUp;

    private GetTrixResp currentTrixResp;
    private GetDlrListByTokenResp currentBrandResp;
    private Class<?> toClass;
    private ArrayList<String> dlrItems;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        toClass = (Class<?>) intent.getExtras().get("class");
        if (intent.getBooleanExtra("should_reset", false)) {
            //drawerLayout2.closeDrawer(Gravity.RIGHT);
            drawerLayout1.closeDrawer(Gravity.RIGHT);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, toClass));
        overridePendingTransition(R.anim.pop_car_select_exit_anim,R.anim.stay);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_select);
        toClass = (Class<?>) getIntent().getExtras().get("class");
        initTitleBar(this, "选择经销商").setLeftClickListener(v -> {
            startActivity(new Intent(this, toClass));
            overridePendingTransition(R.anim.pop_car_select_exit_anim,R.anim.stay);
        });
        Log.e("TAG", "onCreate: ");

        drawerLayout1 = findViewById(R.id.car_select_drawlayout1);
        drawerLayout2 = findViewById(R.id.car_select_drawlayout2);

        //品牌
        mBrandRv = findViewById(R.id.brand_rv);
        LinearLayoutManager brandManager = new LinearLayoutManager(this);
        mBrandRv.setLayoutManager(brandManager);
        mBrandAdapter = new DlrAdapter(this, brandList);
        mBrandAdapter.setOnItemClickListener(new DlrAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, GetDlrListByTokenResp brandResp) {
                showTrixList(brandResp);
            }
        });
        mBrandRv.setAdapter(mBrandAdapter);
        brandSuspensionDecoration = new SuspensionDecoration(this, brandList);
        mBrandRv.addItemDecoration(brandSuspensionDecoration);
        mBrandRv.addItemDecoration(new DividerItemDecoration(DlrStoreSelectActivity.this, DividerItemDecoration.VERTICAL_LIST));
        mTvSideBarHint = findViewById(R.id.tvSideBarHint);
        mIndexBar = findViewById(R.id.indexBar);
        mIndexBar.setmPressedShowTextView(mTvSideBarHint).setmLayoutManager(brandManager);


        initDatas();

        //车系 第二层
        mTrixRv = findViewById(R.id.trix_rv);
        mTrixPackUp = findViewById(R.id.trix_pack_up);
        mTrixPackUp.setOnClickListener(v -> drawerLayout1.closeDrawer(Gravity.RIGHT));
        LinearLayoutManager manager2 = new LinearLayoutManager(this);
        mTrixRv.setLayoutManager(manager2);
        mDlrStoreAdapter = new DlrStoreAdapter(this, trixList);
        mTrixRv.setAdapter(mDlrStoreAdapter);
        trixSuspensionDecoration = new SuspensionDecoration(this, trixList);
        mTrixRv.addItemDecoration(trixSuspensionDecoration);
        mTrixRv.addItemDecoration(new DividerItemDecoration(DlrStoreSelectActivity.this, DividerItemDecoration.VERTICAL_LIST));
        mDlrStoreAdapter.setDatas(trixList);
        mDlrStoreAdapter.notifyDataSetChanged();
        mDlrStoreAdapter.setOnItemClickListener(new DlrStoreAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, GetStoreList trixResp) {
                selectModel(currentBrandResp, trixResp);
            }
        });
        // mDlrStoreAdapter.setOnItemClickListener((v, trixResp) -> showModelList(trixResp));

/*
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
        mModelRv.addItemDecoration(new DividerItemDecoration(DlrStoreSelectActivity.this, DividerItemDecoration.VERTICAL_LIST));
        mModelAdapter.setOnItemClickListener((v, modelResp) -> {
            selectModel(modelResp);
        });
        mEngGv = findViewById(R.id.eng_gv);
        mEngGv.setLayoutManager(new GridLayoutManager(this, 3));
        mEngGv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int childLayoutPosition = parent. etChildLayoutPosition(view);
                if (childLayoutPosition > -1 && childLayoutPosition < 3) {
                    outRect.top = DensityUtil.dip2px(DlrStoreSelectActivity.this, 9);
                }
                outRect.right = DensityUtil.dip2px(DlrStoreSelectActivity.this, 11);
                outRect.bottom = DensityUtil.dip2px(DlrStoreSelectActivity.this, 9);
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
*/
    }


    private void selectModel(GetDlrListByTokenResp brandResp, GetStoreList trixResp) {
        Intent intent2 = new Intent();
        intent2.putExtra("Dlr", brandResp);
        intent2.putExtra("DlrStore", trixResp);
        setResult(RESULT_OK, intent2);
        intent2.setClass(this, toClass);
        intent2.putExtra("why_come", "dlr_select");
        startActivity(intent2);
        overridePendingTransition(R.anim.pop_car_select_exit_anim,R.anim.stay);

    }

    private void showTrixList(GetDlrListByTokenResp brandResp) {
        currentBrandResp = brandResp;
        //drawerLayout2.closeDrawer(Gravity.RIGHT);
//        DlrApi.getTrix(this, brandResp.dlr_id, trixResp -> {
//            if (trixResp == null) {
//                Toast.makeText(myApp, "返回数据为空", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            trixList.clear();
//            trixList.addAll(trixResp);
//            drawerLayout1.openDrawer(Gravity.RIGHT);
//            trixSuspensionDecoration.setmDatas(trixList);
//            mDlrStoreAdapter.notifyDataSetChanged();
//        });
        DlrApi.getStore(this, brandResp.id, new OnItemDataCallBack<List<GetStoreList>>() {
            @Override
            public void onItemDataCallBack(List<GetStoreList> resp) {
                if (resp == null || resp.size() == 0) {
                    drawerLayout1.closeDrawer(Gravity.RIGHT);
                    selectModel(currentBrandResp, null);
                    return;
                }
                trixList.clear();
                trixList.addAll(resp);
                drawerLayout1.openDrawer(Gravity.RIGHT);
                trixSuspensionDecoration.setmDatas(trixList);
                mDlrStoreAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initDatas() {
//        String dlr_id = getIntent().getStringExtra("dlr_id");
//        if (TextUtils.isEmpty(dlr_id)) {
//            dlr_id = "YJCS0001";
//        }
//        DlrApi.getBrand(this, dlr_id, getBrandResp -> {
//            if (getBrandResp == null) {
//                Toast.makeText(myApp, "返回数据为空", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            brandList.clear();
//            // brandList.addAll(getBrandResp);
//
//            //数据源排序并让其支持悬停 如果不适用indexBar而想选填，则必须手动调用排序的几个方法
//            mIndexBar.setmSourceDatas(brandList).invalidate();
//            mBrandAdapter.notifyDataSetChanged();
//        });

        DlrApi.getDlrListByToken(this, new OnItemDataCallBack<List<GetDlrListByTokenResp>>() {
            @Override
            public void onItemDataCallBack(List<GetDlrListByTokenResp> resp) {
                if (resp == null || resp.size() == 0) {
                    return;
                }
                brandList.clear();
                brandList.addAll(resp);
                //数据源排序并让其支持悬停 如果不适用indexBar而想选填，则必须手动调用排序的几个方法
                mIndexBar.setmSourceDatas(brandList).invalidate();
                mBrandAdapter.notifyDataSetChanged();
            }
        });

    }

}
