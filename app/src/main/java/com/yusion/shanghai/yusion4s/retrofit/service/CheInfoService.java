package com.yusion.shanghai.yusion4s.retrofit.service;

import com.yusion.shanghai.yusion4s.base.BaseResult;
import com.yusion.shanghai.yusion4s.bean.order.submit.GetChePriceAndImageResp;
import com.yusion.shanghai.yusion4s.bean.order.submit.GetCheUrlResp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by aa on 2017/11/24.
 */

public interface CheInfoService {

    //获取二手车估值价
    @GET("/che300/vuluation/url/")
    Call<BaseResult<GetCheUrlResp>> getCheUrl(@Query("province_id") String province_id, @Query("city_id") String city_id, @Query("brand_id") String brand_id, @Query("trix_id") String trix_id, @Query("model_id") String model_id, @Query("plate_year") String plate_year, @Query("plate_month") String plate_month, @Query("mile_age") String mile_age);

    //获取截图和价格
    @GET("/che300/vuluation/price/")
    Call<BaseResult<GetChePriceAndImageResp>> getChePriceAndImage(@Query("province_id") String province_id, @Query("city_id") String city_id, @Query("brand_id") String brand_id, @Query("trix_id") String trix_id, @Query("model_id") String model_id, @Query("plate_year") String plate_year, @Query("plate_month") String plate_month, @Query("mile_age") String mile_age);


}
