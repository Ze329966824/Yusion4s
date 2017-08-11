package com.yusion.shanghai.yusion4s.retrofit.service;

import com.yusion.shanghai.yusion4s.base.BaseResult;
import com.yusion.shanghai.yusion4s.bean.order.CheckClientExistResp;
import com.yusion.shanghai.yusion4s.bean.order.GetAppListResp;
import com.yusion.shanghai.yusion4s.bean.order.OrderDetailBean;
import com.yusion.shanghai.yusion4s.bean.order.submit.SubmitOrderReq;
import com.yusion.shanghai.yusion4s.bean.order.submit.SubmitOrderResp;

import java.util.List;

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

    //获取订单列表
    @GET("/api/application/get_app_list/")
    Call<BaseResult<List<GetAppListResp>>> getAppList(@Query("st") String st);

    //获取订单详情
    @GET("/api/application/get_app_details/")
    Call<BaseResult<OrderDetailBean>> getAppDetails(@Query("app_id") String app_id);
}
