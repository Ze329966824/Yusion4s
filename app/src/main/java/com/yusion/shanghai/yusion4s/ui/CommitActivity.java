package com.yusion.shanghai.yusion4s.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.Yusion4sApp;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.bean.upload.ListDealerLabelsResp;
import com.yusion.shanghai.yusion4s.retrofit.api.UploadApi;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.ui.order.OrderCreateActivity;
import com.yusion.shanghai.yusion4s.ui.upload.UploadLabelListActivity;

import java.util.List;

public class CommitActivity extends BaseActivity {
    private Intent mGetIntent;
    private String clt_id;
    private String role;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commit);
        initTitleBar(this, "提交成功");

        switch (getIntent().getStringExtra("why_commit")) {

            case "old_car":
                fromOldCar();
                break;


            case "create_user":
                fromCreateUser();
                break;


            default:
                fromUnknown();
                break;
        }
    }

    private void fromUnknown() {
        ((TextView) findViewById(R.id.commit_title_tv)).setText("why come here?");
        findViewById(R.id.commit_continue_layout).setVisibility(View.GONE);
        findViewById(R.id.commit_return_layout).setVisibility(View.GONE);
    }

    private void fromCreateUser() {
        ((TextView) findViewById(R.id.commit_title_tv)).setText("客户创建成功");
        findViewById(R.id.commit_continue_layout).setVisibility(View.GONE);
        findViewById(R.id.commit_return_layout).setVisibility(View.VISIBLE);
        TextView returnBtn = (TextView) findViewById(R.id.commit_return_btn);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                intent.setClass(CommitActivity.this, OrderCreateActivity.class);
                startActivity(intent);
            }
        });

    }

    private void fromOldCar() {
        findViewById(R.id.commit_return_layout).setVisibility(View.GONE);
        findViewById(R.id.commit_continue_layout).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.commit_title_tv)).setText("还差最后一步!");

        TextView submitBtn = (TextView) findViewById(R.id.commit_continue_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadApi.listDealerLabels(CommitActivity.this, getIntent().getStringExtra("app_id"), new OnItemDataCallBack<List<ListDealerLabelsResp>>() {
                    @Override
                    public void onItemDataCallBack(List<ListDealerLabelsResp> data) {
                        ListDealerLabelsResp item = null;
                        for (ListDealerLabelsResp listDealerLabelsResp : data) {
                            if (listDealerLabelsResp.value.equals(((Yusion4sApp) getApplication()).getConfigResp().send_hand_base_material)) {
                                item = listDealerLabelsResp;
                                break;
                            }
                        }
                        if (item == null) {
                            Toast.makeText(myApp, "没有找到指定影像件标签,请稍后补充上传", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Intent intent = new Intent(CommitActivity.this, UploadLabelListActivity.class);
                        intent.putExtra("topItem", item);
                        intent.putExtra("app_id", getIntent().getStringExtra("app_id"));
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }
}

