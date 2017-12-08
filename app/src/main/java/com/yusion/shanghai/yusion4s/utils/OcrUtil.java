package com.yusion.shanghai.yusion4s.utils;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.yusion.shanghai.yusion4s.bean.ocr.OcrReq;
import com.yusion.shanghai.yusion4s.bean.ocr.OcrResp;
import com.yusion.shanghai.yusion4s.bean.oss.OSSObjectKeyBean;
import com.yusion.shanghai.yusion4s.retrofit.api.OcrApi;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnTwoCallBack;

import io.sentry.Sentry;

/**
 * Created by ice on 2017/8/4.
 */
public class OcrUtil {
    public static void requestOcr(final Context context, Dialog dialog, final String localPath, @NonNull OSSObjectKeyBean objectKeyBean, String type,
                                  @NonNull final OnOcrSuccessCallBack onSuccessCallBack, @NonNull final OnTwoCallBack<Throwable, String> onFailureCallBack) {

        OssUtil.uploadOss(context,dialog, localPath, objectKeyBean, objectKey -> {
            OcrReq ocrReq = new OcrReq();
            ocrReq.fid = objectKey;
            ocrReq.region = SharedPrefsUtil.getInstance(context).getValue("region", "");
            ocrReq.bucket = SharedPrefsUtil.getInstance(context).getValue("bucket", "");
            ocrReq.type = type;
            OcrApi.requestOcr(ocrReq,dialog, data -> onSuccessCallBack.OnOcrSuccess(data, objectKey), throwable -> {
                String errorInfo = "onFailure() called with:  throwable = [" + throwable + "]";
                Sentry.capture(errorInfo);
                Log.e("TAG", errorInfo);
                onFailureCallBack.onTwoDataCallBack(throwable, objectKey);
            });
        }, throwable -> onFailureCallBack.onTwoDataCallBack(throwable, null));
    }

    public interface OnOcrSuccessCallBack {
        void OnOcrSuccess(OcrResp ocrResp, String objectKey);
    }
}
