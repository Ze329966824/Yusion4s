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

        //UBT.uploadPersonAndDeviceInfo(this);


        TextView submit = (TextView) findViewById(R.id.commit_submit);
        submit.setOnClickListener(new View.OnClickListener() {
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
//        mGetIntent = getIntent();
//        switch (mGetIntent.getStringExtra("commit_state")) {
//            case "return":
//                findViewById(R.id.commit_return_layout).setVisibility(View.VISIBLE);
//                findViewById(R.id.commit_continue_layout).setVisibility(View.GONE);
//
//                findViewById(R.id.return_list_info1).setOnClickListener(v -> UserApi.getListCurrentTpye(CommitActivity.this, data -> {
//                    if (data.guarantor_commited) {
//                        Intent intent = new Intent(CommitActivity.this, InfoListActivity.class);
//                        intent.putExtra("ishaveGuarantee", true);
//                        startActivity(intent);
//                        finish();
//                    } else {
//                        Intent intent = new Intent(CommitActivity.this, InfoListActivity.class);
//                        intent.putExtra("ishaveGuarantee", false);
//                        startActivity(intent);
//                        finish();
//                    }
//                }));
//
//                break;
//
//            case "continue":
//                findViewById(R.id.commit_continue_layout).setVisibility(View.VISIBLE);
//                findViewById(R.id.commit_return_layout).setVisibility(View.GONE);
//
//                //返回资料列表
//                findViewById(R.id.continue_imgs_info).setOnClickListener(v -> UserApi.getListCurrentTpye(CommitActivity.this, data -> {
//                    if (data.guarantor_commited) {
//                        Intent intent = new Intent(CommitActivity.this, InfoListActivity.class);
//                        intent.putExtra("ishaveGuarantee", true);
//                        startActivity(intent);
//                        finish();
//                    } else {
//                        Intent intent = new Intent(CommitActivity.this, InfoListActivity.class);
//                        intent.putExtra("ishaveGuarantee", false);
//                        startActivity(intent);
//                        finish();
//                    }
//                }));
//
//                //继续上传影像件
//                findViewById(R.id.return_list_info).setOnClickListener(v -> {
//                    clt_id = mGetIntent.getStringExtra("clt_id");
//                    role = mGetIntent.getStringExtra("role");
//                    title = mGetIntent.getStringExtra("title");
//                    Intent intent = new Intent(this, UploadLabelListActivity.class);
//                    intent.putExtra("clt_id", clt_id);
//                    intent.putExtra("role", role);
//                    intent.putExtra("title", title);
//                    startActivity(intent);
//                    finish();
//                });
//
//                break;
//        }


    }
}

