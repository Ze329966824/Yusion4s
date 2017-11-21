package com.yusion.shanghai.yusion4s.retrofit.service;

import com.yusion.shanghai.yusion4s.base.BaseResult;
import com.yusion.shanghai.yusion4s.bean.auth.GetVCodeResp;
import com.yusion.shanghai.yusion4s.bean.product.RequestAuthenticationReq;
import com.yusion.shanghai.yusion4s.bean.user.ClientInfo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by LX on 2017/11/20.
 */

public interface ProductService {
    @POST("api/client/auth_client/")
    Call<BaseResult> requestAuthentication(@Body RequestAuthenticationReq requestAuthenticationReq);

    @GET("api/client/auth_client/")
    Call<BaseResult<GetVCodeResp>> getAuthenticationVerifyCode(@Query("mobile") String mobile, @Query("oneself_clt_id") String oneself_clt_id,
                                                               @Query("other_clt_id") String other_clt_id, @Query("relation_ship") String relation_ship);


    //获取用户信息
    @GET("api/client/client_info/")
    Call<BaseResult<ClientInfo>> getClientInfo(@Query("id_no") String id_no, @Query("clt_nm") String clt_nm, @Query("update") String update);

    //更新用户资料
    @POST("api/client/client_info/")
    Call<BaseResult<ClientInfo>> updateClientInfo(@Body ClientInfo req);
}
