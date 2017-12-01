package com.yusion.shanghai.yusion4s.utils;

import android.app.Activity;

import com.awen.photo.photopick.controller.PhotoPagerConfig;

import java.util.ArrayList;

/**
 * Created by ice on 2017/12/1.
 */

public class PreviewImgUtil {
    public static void showImg(Activity context, ArrayList<String> showImgUrls) {
        ArrayList<String> relShowImgUrls = new ArrayList<>();
        for (String imgUrl : showImgUrls) {
            if (imgUrl.contains("/storage/")) {
                relShowImgUrls.add("file://" + imgUrl);
            } else {
                relShowImgUrls.add(imgUrl);
            }
        }
        new PhotoPagerConfig.Builder(context)
//                .setLowImageUrls(relShowImgUrls)
                .setBigImageUrls(relShowImgUrls)
                .build();
    }
}
