package com.yusion.shanghai.yusion4s.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import io.sentry.Sentry;

/**
 * Created by ice on 2017/11/27.
 */

public class Base64Util {

    /**
     * 将一串base64转化为png图片
     *
     * @param dirPath 存放的地址
     * @param imgName 存放的名称 eg:xxx.png
     */
    public static void saveBaseImage(String base64, String dirPath, String imgName) {
        if (TextUtils.isEmpty(base64)) {
            throw new IllegalArgumentException("base64没有读取到！！！");
        }
        try {
            FileOutputStream write = new FileOutputStream(new File(dirPath + imgName));
            byte[] decoderBytes = Base64.decode(base64, Base64.DEFAULT);
            write.write(decoderBytes);
            write.close();
        } catch (IOException e) {
            Sentry.capture("将Base64存储为图片失败🙄: " + e);
            Log.e("TAG", "将Base64存储为图片失败🙄: " + e);
        }
    }


    /**
     * 将base64转换为bitmap返回
     */
    public static Bitmap string2Bitmap(String base64) {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(base64, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            Sentry.capture("将Base64转换为Bitmap失败🙄: " + e);
            Log.e("TAG", "将Base64转换为Bitmap失败🙄: " + e);
        }
        return bitmap;
    }

}
