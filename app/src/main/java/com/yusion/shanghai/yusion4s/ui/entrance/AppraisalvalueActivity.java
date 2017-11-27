package com.yusion.shanghai.yusion4s.ui.entrance;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.shizhefei.view.largeimage.LargeImageView;
import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.bean.order.submit.GetChePriceAndImageResp;
import com.yusion.shanghai.yusion4s.retrofit.api.CheApi;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.utils.Base64Util;

public class AppraisalvalueActivity extends AppCompatActivity {
    private LargeImageView appraisal_value_img;
    private TextView appraisal_download_tv;



    private String baseStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appraisalvalue);
        initView();
    }

    private void initView() {
        appraisal_value_img = (LargeImageView) findViewById(R.id.appraisal_value_img);
        appraisal_download_tv = (TextView) findViewById(R.id.appraisal_download_tv);

        CheApi.getChePriceAndImage(this, new OnItemDataCallBack<GetChePriceAndImageResp>() {
            @Override
            public void onItemDataCallBack(GetChePriceAndImageResp data) {
                baseStr = data.result.img;
                Bitmap bitmap = Base64Util.stringtoBitmap(baseStr);
                appraisal_value_img.setImage(bitmap);
            }
        });
//        String baseStr = getIntent().getStringExtra("guess_img");
//        Bitmap bitmap = Base64Util.stringtoBitmap(baseStr);
//        appraisal_value_img.setImageBitmap(bitmap);



        appraisal_download_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Base64Util.saveBaseImage(baseStr,getPackageResourcePath(),"appraisalvalue.png");
            }
        });


    }
}


