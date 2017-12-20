package com.yusion.shanghai.yusion4s.ubt.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.yusion.shanghai.yusion4s.ubt.UBT;

/**
 * Created by aa on 2017/12/20.
 */

public class UploadPersonInfoService extends IntentService {

    public UploadPersonInfoService() {
        super("UploadPersonInfoService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            UBT.uploadPersonAndDeviceInfo(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
