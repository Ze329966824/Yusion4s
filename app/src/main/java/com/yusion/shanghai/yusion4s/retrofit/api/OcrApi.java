package com.yusion.shanghai.yusion4s.retrofit.api;

import android.util.Log;

import com.yusion.shanghai.yusion4s.base.BaseResult;
import com.yusion.shanghai.yusion4s.bean.ocr.OcrReq;
import com.yusion.shanghai.yusion4s.bean.ocr.OcrResp;
import com.yusion.shanghai.yusion4s.retrofit.Api;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 类描述：
 * 伟大的创建人：ice
 * 不可相信的创建时间：17/4/19 下午7:07
 */

public class OcrApi {
    public static void requestOcr(OcrReq req, final OnItemDataCallBack<OcrResp> onSuccessCallBack, final OnItemDataCallBack<Throwable> onFailureCallBack) {
        Api.getOcrService().requestOcr(req).enqueue(new Callback<BaseResult<OcrResp>>() {
            @Override
            public void onResponse(Call<BaseResult<OcrResp>> call, Response<BaseResult<OcrResp>> response) {
                Log.e("API", "onResponse: " + response.body());
                onSuccessCallBack.onItemDataCallBack(response.body().data);
            }

            @Override
            public void onFailure(Call<BaseResult<OcrResp>> call, Throwable t) {
                onFailureCallBack.onItemDataCallBack(t);
            }
        });
    }
}
