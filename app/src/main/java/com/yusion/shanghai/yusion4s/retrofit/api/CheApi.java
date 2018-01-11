package com.yusion.shanghai.yusion4s.retrofit.api;

import android.app.Dialog;
import android.content.Context;

import com.yusion.shanghai.yusion4s.Yusion4sApp;
import com.yusion.shanghai.yusion4s.bean.order.submit.GetChePriceAndImageResp;
import com.yusion.shanghai.yusion4s.bean.order.submit.GetCheUrlResp;
import com.yusion.shanghai.yusion4s.retrofit.Api;
import com.yusion.shanghai.yusion4s.retrofit.callback.CustomCallBack;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.retrofit.service.CheInfoService;
import com.yusion.shanghai.yusion4s.utils.LoadingUtils;

import retrofit2.Retrofit;

/**
 * Created by aa on 2017/11/24.
 */

public class CheApi {
    public static Retrofit retrofit = Api.createRetrofit(Yusion4sApp.CONFIG_RESP.Che300Url);

    public static CheInfoService getCheInfoService() {
        return retrofit.create(CheInfoService.class);
    }

    public static void getCheUrl(final Context context, String province_id, String city_id, String brand_id, String trix_id, String model_id, String plate_year, String plate_month, String mile_age, OnItemDataCallBack<GetCheUrlResp> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        CheApi.getCheInfoService().getCheUrl(province_id, city_id, brand_id, trix_id, model_id, plate_year, plate_month, mile_age).enqueue(new CustomCallBack<GetCheUrlResp>(context, dialog) {
            @Override
            public void onCustomResponse(GetCheUrlResp data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });
    }

    public static void getChePriceAndImage(final Context context, String province_id, String city_id, String brand_id, String trix_id, String model_id, String plate_year, String plate_month, String mile_age, OnItemDataCallBack<GetChePriceAndImageResp> onItemDataCallBack) {
        CheApi.getCheInfoService().getChePriceAndImage(province_id, city_id, brand_id, trix_id, model_id, plate_year, plate_month, mile_age).enqueue(new CustomCallBack<GetChePriceAndImageResp>(context) {
            @Override
            public void onCustomResponse(GetChePriceAndImageResp data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });
    }
}
