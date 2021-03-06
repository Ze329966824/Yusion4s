package com.yusion.shanghai.yusion4s.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestOptions;
import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.glide.StatusImageRel;
import com.yusion.shanghai.yusion4s.glide.progress.OnGlideImageViewListener;
import com.yusion.shanghai.yusion4s.widget.DonutProgress;

import java.io.File;

/**
 * Created by ice on 2017/9/12.
 */

public class GlideUtil {

    public static void loadImg(Context context, StatusImageRel statusImageRel, String url) {
        RequestOptions requestOptions = statusImageRel.getSourceImg().requestOptions(R.mipmap.place_holder_img);
        statusImageRel.getSourceImg().load(url, requestOptions).listener(new OnGlideImageViewListener() {
            @Override
            public void onProgress(int percent, boolean isDone, GlideException exception) {
                if (exception != null && !TextUtils.isEmpty(exception.getMessage())) {
                    Toast.makeText(context, exception.getMessage(), Toast.LENGTH_LONG).show();
                }
                DonutProgress progressPro = statusImageRel.getProgressPro();
                if (!isDone) {
                    progressPro.setVisibility(View.VISIBLE);
                }else {
                    progressPro.setVisibility(View.GONE);
                }
                progressPro.setProgress(percent);
            }
        });
    }

    public static void loadLocalImg(Context context, StatusImageRel statusImageRel, File file) {
        loadLocalImg(context, statusImageRel, file.getAbsolutePath());
    }

    public static void loadLocalImg(Context context, StatusImageRel statusImageRel, String url) {
        statusImageRel.getSourceImg().loadLocalImage(url, R.mipmap.place_holder_img).listener(new OnGlideImageViewListener() {
            @Override
            public void onProgress(int percent, boolean isDone, GlideException exception) {
                if (exception != null && !TextUtils.isEmpty(exception.getMessage())) {
                    Toast.makeText(context, exception.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
