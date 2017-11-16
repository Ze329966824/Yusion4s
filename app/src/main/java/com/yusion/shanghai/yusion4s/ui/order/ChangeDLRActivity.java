package com.yusion.shanghai.yusion4s.ui.order;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.bean.dlr.GetDlrListByTokenResp;
import com.yusion.shanghai.yusion4s.retrofit.api.DlrApi;

import java.util.ArrayList;

public class ChangeDLRActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_dealer);

        showDlr();
    }

    private void showDlr() {
        DlrApi.getDlrListByToken(this, resp -> {
            if (resp != null && !resp.isEmpty()) {
                ArrayList<String> dlrItems = new ArrayList<String>();
                for (GetDlrListByTokenResp item : resp) {
                    dlrItems.add(item.dlr_nm);
                }
            }
        });
    }
}