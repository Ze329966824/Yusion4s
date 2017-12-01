package com.yusion.shanghai.yusion4s.ui.upload;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.glide.StatusImageRel;
import com.yusion.shanghai.yusion4s.utils.GlideUtil;

public class ExtraPreviewActivity extends BaseActivity {
    private String imageUrl;
    private StatusImageRel imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.extra_activity_preview);
        Intent intent = getIntent();
        imageUrl = intent.getStringExtra("PreviewImg");
        imageView = findViewById(R.id.image_preview);
        imageView.getSourceImg().setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setOnClickListener(v -> onBackPressed());
        GlideUtil.loadImg(this, imageView, imageUrl);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, R.anim.center_zoom_out);
    }
}
