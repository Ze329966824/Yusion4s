package com.yusion.shanghai.yusion4s.ui.upload;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.glide.StatusImageRel;
import com.yusion.shanghai.yusion4s.utils.FileUtil;
import com.yusion.shanghai.yusion4s.utils.GlideUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class PreviewActivity extends BaseActivity {
    private String imageUrl;
    private StatusImageRel imageView;
    private boolean isbBreviary;
    private SubsamplingScaleImageView imageBreviaryView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        Intent intent = getIntent();
        imageUrl = intent.getStringExtra("PreviewImg");
        isbBreviary = intent.getBooleanExtra("breviary", false);


        imageView = (StatusImageRel) findViewById(R.id.image_preview);
        imageBreviaryView = (SubsamplingScaleImageView) findViewById(R.id.image_preview_breviary);
        imageBreviaryView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
//        imageBreviaryView.setScaleType(IntensifyImage.ScaleType.CENTER);
//        imageBreviaryView.setImage("/storage/emulated/0/Pictures/%E9%95%BF%E5%9B%BE.png");
        if (isbBreviary) {
            imageView.setVisibility(View.GONE);
            imageBreviaryView.setVisibility(View.VISIBLE);
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("正在加载缩略图...");
            dialog.show();
            new Thread(() -> {
                String[] strings = imageUrl.split("/");
                int length = strings.length;
                String fileName = strings[length - 1];
                File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                boolean exist = false;
                for (File file : dir.listFiles()) {
                    if (file.getName().equals(fileName)) {
                        exist = true;
                        break;
                    }
                }

                File imgFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + fileName);

                if (exist) {
                    runOnUiThread(() -> {
                        dialog.dismiss();
                        String path = imgFile.getPath();
                        imageBreviaryView.setImage(ImageSource.uri(path));
//                        imageBreviaryView.setImage(path);
                    });
                } else {
                    URL url = null;
                    try {
                        url = new URL(imageUrl);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setConnectTimeout(5 * 1000);
                        InputStream inputStream = conn.getInputStream();
                        FileUtil.saveImg(inputStream, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + fileName, () -> runOnUiThread(() -> {
                            dialog.dismiss();
//                            imageBreviaryView.setImage(imgFile.getPath());
                            imageBreviaryView.setImage(ImageSource.uri(imgFile.getPath()));
                        }));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }).start();

        } else {
            imageBreviaryView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            imageView.getSourceImg().setScaleType(ImageView.ScaleType.FIT_CENTER);
            GlideUtil.loadImg(this, imageView, imageUrl);
            imageView.setOnClickListener(v -> finish());
        }
    }
}
