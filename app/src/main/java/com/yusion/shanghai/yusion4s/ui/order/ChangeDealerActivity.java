package com.yusion.shanghai.yusion4s.ui.order;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.bean.dlr.GetDlrListByTokenResp;
import com.yusion.shanghai.yusion4s.retrofit.api.DlrApi;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;

import java.util.List;

public class ChangeDealerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_dealer);

        showDlr();
    }

    private void showDlr() {
        DlrApi.getDlrListByToken(this, new OnItemDataCallBack<List<GetDlrListByTokenResp>>() {
            @Override
            public void onItemDataCallBack(List<GetDlrListByTokenResp> data) {
                for (int i = 0; i < data.size(); i++) {
//                    data.get(i).dlr_nm
                }
            }
        });
    }
}
