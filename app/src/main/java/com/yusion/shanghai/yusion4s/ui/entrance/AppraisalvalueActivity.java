package com.yusion.shanghai.yusion4s.ui.entrance;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.shizhefei.view.largeimage.LargeImageView;
import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.utils.Base64Util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AppraisalvalueActivity extends BaseActivity {
    private LargeImageView appraisal_value_img;
    private TextView appraisal_download_tv;



    private String baseStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appraisalvalue);

        initTitleBar(this, "车300估值报告").setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initView();
    }

    private void initView() {
        appraisal_value_img = (LargeImageView) findViewById(R.id.appraisal_value_img);
        appraisal_download_tv = (TextView) findViewById(R.id.appraisal_download_tv);

//        CheApi.getChePriceAndImage(this, new OnItemDataCallBack<GetChePriceAndImageResp>() {
//            @Override
//            public void onItemDataCallBack(GetChePriceAndImageResp data) {
//                baseStr = data.result.img;
//                Bitmap bitmap = Base64Util.stringtoBitmap(baseStr);
//                appraisal_value_img.setImage(bitmap);
//            }
//        });
        baseStr = getIntent().getStringExtra("guess_img");
        Bitmap bitmap = Base64Util.stringtoBitmap(baseStr);
        appraisal_value_img.setImage(bitmap);



        appraisal_download_tv.setOnClickListener(v -> {
            Date date=new Date();
            DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
            String time=format.format(date);
            String name = time +".png";

            Base64Util.saveBaseImage(baseStr,Environment.getExternalStorageDirectory().getPath()+"/yusion/",name);
            Log.e("TAG", "path : "+Environment.getExternalStorageDirectory().getPath());

        });


    }
}


