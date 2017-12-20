package com.yusion.shanghai.yusion4s.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
/**
 * Created by ice on 2017/11/27.
 */

public class Base64Util {

    /**
     * 将一串base64转化为png图片
     * @param base64
     * @param path 存放的地址
     * @param imgName 存放的名称 eg:xxx.png
     */
    public static void saveBaseImage(String base64, String path, String imgName) {
        if (TextUtils.isEmpty(base64)) {
            throw new IllegalArgumentException("base64没有读取到！！！");
        }
        try {
            FileOutputStream write = new FileOutputStream(new File(path + imgName));
            byte[] decoderBytes = Base64.decode(base64, Base64.DEFAULT);
            write.write(decoderBytes);
            write.close();
        } catch (IOException e) {
            Log.e("TAG", "saveBaseImage: "+e);
            e.printStackTrace();
        }
    }


    /**
     * 将base64转换为bitmap返回
     * @param string
     * @return
     */
    public static Bitmap stringtoBitmap(String string){
        //将字符串转换成Bitmap类型
        Bitmap bitmap=null;
        try {
            byte[]bitmapArray;
            bitmapArray= Base64.decode(string, Base64.DEFAULT);
            bitmap= BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

}
