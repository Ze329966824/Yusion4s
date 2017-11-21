package com.yusion.shanghai.yusion4s.ui.apply;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.ubt.UBT;
import com.yusion.shanghai.yusion4s.ui.MainActivity;



public class CommitActivity extends BaseActivity {
    private Intent mGetIntent;
    private String clt_id;
    private String role;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commit);
        initTitleBar(this, getResources().getString(R.string.commit));
        new Thread(() -> UBT.uploadPersonAndDeviceInfo(CommitActivity.this)).start();


        findViewById(R.id.return_list_info1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                intent.setClass(CommitActivity.this, MainActivity.class);

                startActivity(intent);


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
//            default:
//                break;
//        }


    }
}
