package com.yusion.shanghai.yusion4s.retrofit.service;

import com.yusion.shanghai.yusion4s.base.BaseResult;
import com.yusion.shanghai.yusion4s.bean.order.CheckClientExistResp;
import com.yusion.shanghai.yusion4s.bean.order.submit.SubmitOrderReq;
import com.yusion.shanghai.yusion4s.bean.order.submit.SubmitOrderResp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by aa on 2017/8/9.
 */

public interface OrderService {
    @POST("/api/application/submit_app/")
    Call<BaseResult<SubmitOrderResp>> submitOrder(@Body SubmitOrderReq req);

    @GET("/api/client/check_client/")
    Call<BaseResult<CheckClientExistResp>> checkClientExist(@Query("id_no") String idNo);

}
