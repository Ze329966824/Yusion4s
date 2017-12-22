package com.yusion.shanghai.yusion4s.ui.entrance;

import android.content.Intent;
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
import com.yusion.shanghai.yusion4s.bean.upload.ListImgsResp;
import com.yusion.shanghai.yusion4s.retrofit.api.UploadApi;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.utils.Base64Util;

import java.io.File;
import java.io.FileOutputStream;
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
    private Bitmap bitmap;
    private URL finalUrl;
    private URL url;

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
        Intent intent = getIntent();
        baseStr = intent.getStringExtra("guess_img");
        Log.e("TAG", "initView: " + baseStr);

//        appraisal_download_tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.e("TAG", "onClick: " + intent.getStringExtra("guess_img"));
//                Log.e("TAG", "onClick: " + baseStr);
//            }
//        });
        appraisal_download_tv.setOnClickListener(v -> {
            Date date = new Date();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String time = format.format(date);
            String name = time + ".png";
            if (intent.getStringExtra("guess_img") != null) {
                Base64Util.saveBaseImage(intent.getStringExtra("guess_img"), Environment.getExternalStorageDirectory().getPath() + "/yusion/", name);
                Log.e("TAG", "path : " + Environment.getExternalStorageDirectory().getPath());
            }else {
                saveImage(name);
            }
            Toast.makeText(this, "截图已保存到" + Environment.getExternalStorageDirectory().getPath() + "/yusion/" + name, Toast.LENGTH_SHORT).show();

        });

        if (baseStr != null) {
            Bitmap bitmap = Base64Util.string2Bitmap(baseStr);
            appraisal_value_img.setImage(bitmap);
            baseStr = "";
        } else {
            ListImgsReq req = new ListImgsReq();
            req.app_id = getIntent().getStringExtra("app_id");
//            req.clt_id = getIntent().getStringExtra("clt_id");
            req.role = getIntent().getStringExtra("role");
            req.label = getIntent().getStringExtra("label");
            Log.e("TAG", req.clt_id + req.app_id + req.role + req.label);


            UploadApi.listImgs(this, req, new OnItemDataCallBack<ListImgsResp>() {
                @Override
                public void onItemDataCallBack(ListImgsResp data) {
                    if (data.list.isEmpty() || data.list.size() < 0) {
                        Toast.makeText(AppraisalvalueActivity.this, "并没有图片", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    try {
                        url = new URL(data.list.get(0).raw_url);
                        finalUrl = url;
                        setUrl();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void setUrl() {
        new Thread(() -> {
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) finalUrl.openConnection();
                conn.setConnectTimeout(5 * 1000);
                InputStream inputStream = null;
                inputStream = conn.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        appraisal_value_img.setImage(bitmap);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void saveImage(String strFileName) {
        String strPath = Environment.getExternalStorageDirectory().getPath() + "/yusion";

        try {
            File destDir = new File(strPath);
            if (!destDir.exists()) {
                Log.d("MagicMirror", "Dir not exist create it " + strPath);
                destDir.mkdirs();
                Log.d("MagicMirror", "Make dir success: " + strPath);
            }

            File imageFile = new File(strPath + "/" + strFileName);
            imageFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


