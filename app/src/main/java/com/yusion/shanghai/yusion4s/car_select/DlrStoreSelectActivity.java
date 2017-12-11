package com.yusion.shanghai.yusion4s.car_select;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.BaseActivity;

public class DlrStoreSelectActivity extends BaseActivity {
    private RecyclerView mDlrStoreRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dlr_store_select);
    }
}
