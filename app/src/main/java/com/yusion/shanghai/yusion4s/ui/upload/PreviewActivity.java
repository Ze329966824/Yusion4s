package com.yusion.shanghai.yusion4s.ui.upload;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.BaseActivity;

public class PreviewActivity extends BaseActivity {
    private String imageUrl;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        imageView = (ImageView) findViewById(R.id.image_preview);
        Intent intent = getIntent();
        imageUrl = intent.getStringExtra("PreviewImg");
        Glide.with(this).load(imageUrl).into(imageView);
        imageView.setOnClickListener(v -> finish());
    }
}
