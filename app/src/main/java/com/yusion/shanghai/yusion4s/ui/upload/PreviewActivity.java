package com.yusion.shanghai.yusion4s.ui.upload;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

import com.shizhefei.view.largeimage.LargeImageView;
import com.shizhefei.view.largeimage.factory.FileBitmapDecoderFactory;
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
    private LargeImageView imageBreviaryView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        Intent intent = getIntent();
        imageUrl = intent.getStringExtra("PreviewImg");
        isbBreviary = intent.getBooleanExtra("breviary", false);


        imageView = (StatusImageRel) findViewById(R.id.image_preview);
        imageBreviaryView = (LargeImageView) findViewById(R.id.image_preview_breviary);
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
                        imageBreviaryView.setImage(new FileBitmapDecoderFactory(imgFile.getPath()));
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
                            imageBreviaryView.setImage(new FileBitmapDecoderFactory(imgFile.getPath()));
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
