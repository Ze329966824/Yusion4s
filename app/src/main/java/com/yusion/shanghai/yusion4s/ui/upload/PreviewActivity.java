package com.yusion.shanghai.yusion4s.ui.upload;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.glide.StatusImageRel;
import com.yusion.shanghai.yusion4s.utils.GlideUtil;

public class PreviewActivity extends BaseActivity {
    private String imageUrl;
    private StatusImageRel imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        imageView = (StatusImageRel) findViewById(R.id.image_preview);
        imageView.getSourceImg().setScaleType(ImageView.ScaleType.FIT_CENTER);
        Intent intent = getIntent();
        imageUrl = intent.getStringExtra("PreviewImg");
//        Glide.with(this).load(imageUrl).into(imageView);
        GlideUtil.loadImg(this, imageView, imageUrl);
        imageView.setOnClickListener(v -> finish());
    }
}
