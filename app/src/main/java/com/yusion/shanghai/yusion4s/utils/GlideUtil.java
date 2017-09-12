package com.yusion.shanghai.yusion4s.utils;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.glide.ProgressModelLoader;
import com.yusion.shanghai.yusion4s.glide.StatusImageRel;

import java.io.File;

/**
 * Created by ice on 2017/9/12.
 */

public class GlideUtil {

    public static void loadImg(Context context, StatusImageRel statusImageRel, String url) {
        Glide.with(context).using(new ProgressModelLoader(statusImageRel))
                .load(url)
                .placeholder(R.mipmap.place_holder_img)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(statusImageRel.sourceImg);
    }

    public static void loadImg(Context context, StatusImageRel statusImageRel, File file) {
        Glide.with(context)
                .load(file)
                .placeholder(R.mipmap.place_holder_img)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(statusImageRel.sourceImg);
    }
}
