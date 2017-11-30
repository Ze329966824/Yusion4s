package com.yusion.shanghai.yusion4s.ui.entrance;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.shizhefei.view.largeimage.LargeImageView;
import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.bean.upload.ListImgsReq;
import com.yusion.shanghai.yusion4s.retrofit.api.UploadApi;
import com.yusion.shanghai.yusion4s.utils.Base64Util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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

        initTitleBar(this, "车300估值报告").setLeftClickListener(v -> finish());
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
        if (baseStr != null) {
            Bitmap bitmap = Base64Util.stringtoBitmap(baseStr);
            appraisal_value_img.setImage(bitmap);
            baseStr = "";
        } else {
            ListImgsReq req = new ListImgsReq();
            req.app_id = getIntent().getStringExtra("app_id");
            req.clt_id = getIntent().getStringExtra("clt_id");
            req.role = getIntent().getStringExtra("role");
            req.label = getIntent().getStringExtra("label");
            Log.e("TAG", req.clt_id + req.app_id + req.role + req.label);

            UploadApi.listImgs(this, req, data -> {
                if (data.list.isEmpty() || data.list.size() < 0) {
                    return;
                }
                URL url = null;
                try {
                    url = new URL(data.list.get(0).s_url);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5 * 1000);
                    InputStream inputStream = conn.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    appraisal_value_img.setImage(bitmap);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }


        appraisal_download_tv.setOnClickListener(v -> {
            Date date = new Date();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String time = format.format(date);
            String name = time + ".png";

            Base64Util.saveBaseImage(baseStr, Environment.getExternalStorageDirectory().getPath() + "/yusion/", name);
            Toast.makeText(this,"截图已保存到"+Environment.getExternalStorageDirectory().getPath()+ "/yusion/"+name,Toast.LENGTH_SHORT).show();
            Log.e("TAG", "path : " + Environment.getExternalStorageDirectory().getPath());

        });


    }
}


