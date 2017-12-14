package com.yusion.shanghai.yusion4s.retrofit.service;

import com.yusion.shanghai.yusion4s.bean.amap.PoiResp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by LX on 2017/11/21.
 */

public interface AMapService {
    @GET("v3/place/text")
    Call<PoiResp> getPoiResp(@Query("key") String key, @Query("keywords") String keywords, @Query("city") String city);
}
