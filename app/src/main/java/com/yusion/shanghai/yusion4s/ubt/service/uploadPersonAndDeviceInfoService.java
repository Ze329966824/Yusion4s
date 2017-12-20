package com.yusion.shanghai.yusion4s.ubt.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.yusion.shanghai.yusion4s.ubt.UBT;

/**
 * Created by aa on 2017/12/20.
 */

public class uploadPersonAndDeviceInfoService extends IntentService {
    private static final String ACTION_UPLOAD_IMG = "com.zhy.blogcodes.intentservice.action.UPLOAD_IMAGE";
    public static final String EXTRA_IMG_PATH = "com.zhy.blogcodes.intentservice.extra.IMG_PATH";

    public static void startUploadImg(Context context, String path) {
        Intent intent = new Intent(context, uploadPersonAndDeviceInfoService.class);
        intent.setAction(ACTION_UPLOAD_IMG);
        intent.putExtra(EXTRA_IMG_PATH, path);
        context.startService(intent);
    }

    public uploadPersonAndDeviceInfoService() {
        super("uploadPersonAndDeviceInfoService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPLOAD_IMG.equals(action)) {
                final String path = intent.getStringExtra(EXTRA_IMG_PATH);
                handleUploadImg(path);
            }
        }
    }

    private void handleUploadImg(String path) {
        try {
            //模拟上传耗时

            UBT.uploadPersonAndDeviceInfo(this);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
