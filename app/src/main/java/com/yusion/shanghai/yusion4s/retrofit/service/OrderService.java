package com.yusion.shanghai.yusion4s.retrofit.service;

import com.yusion.shanghai.yusion4s.base.BaseResult;
import com.yusion.shanghai.yusion4s.bean.order.GetFinancePlanDetailResp;
import com.yusion.shanghai.yusion4s.bean.order.SearchClientResp;
import com.yusion.shanghai.yusion4s.bean.order.GetAppListResp;
import com.yusion.shanghai.yusion4s.bean.order.OrderDetailBean;
import com.yusion.shanghai.yusion4s.bean.order.submit.GetApplicateDetailResp;
import com.yusion.shanghai.yusion4s.bean.order.submit.SubmitOrderReq;
import com.yusion.shanghai.yusion4s.bean.order.submit.SubmitOrderResp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by aa on 2017/8/9.
 */

public interface OrderService {
    @POST("/api/application/submit_app/")
    Call<BaseResult<SubmitOrderResp>> submitOrder(@Body SubmitOrderReq req);

    @GET("/api/client/search_client/")
    Call<BaseResult<List<SearchClientResp>>> searchClient(@Query("key") String clt_nm);

    @GET("/api/client/structure_all_client/")
    Call<BaseResult<GetApplicateDetailResp>> getApplicateDetail(@Query("clt_id") String clt_id);

    //获取订单列表
    @GET("/api/application/get_app_list/")
    Call<BaseResult<List<GetAppListResp>>> getAppList(@Query("st") String st);

    //获取订单详情
    @GET("/api/application/get_app_details/")
    Call<BaseResult<OrderDetailBean>> getAppDetails(@Query("app_id") String app_id);

    //获取金融方案详情
    @GET("/api/application/get_confirm_financial_plan/")
    Call<BaseResult<GetFinancePlanDetailResp>> getFinancePlanDetail(@Query("app_id") String app_id);
}
