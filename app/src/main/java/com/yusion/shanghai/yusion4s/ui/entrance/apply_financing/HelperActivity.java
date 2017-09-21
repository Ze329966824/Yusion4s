package com.yusion.shanghai.yusion4s.ui.entrance.apply_financing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.ui.entrance.WebViewActivity;

public class HelperActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper);
        initTitleBar(this,"小助手");

        findViewById(R.id.helper_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HelperActivity.this, WebViewActivity.class);
                intent.putExtra("type", "contract");
                startActivity(intent);
            }
        });
    }
}
