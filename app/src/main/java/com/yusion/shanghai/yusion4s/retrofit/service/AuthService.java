package com.yusion.shanghai.yusion4s.retrofit.service;

import com.yusion.shanghai.yusion4s.base.BaseResult;
import com.yusion.shanghai.yusion4s.bean.auth.CheckInfoCompletedResp;
import com.yusion.shanghai.yusion4s.bean.auth.CheckUserInfoResp;
import com.yusion.shanghai.yusion4s.bean.auth.GetVCodeResp;
import com.yusion.shanghai.yusion4s.bean.auth.ReplaceSPReq;
import com.yusion.shanghai.yusion4s.bean.auth.UpdateResp;
import com.yusion.shanghai.yusion4s.bean.login.LoginReq;
import com.yusion.shanghai.yusion4s.bean.login.LoginResp;
import com.yusion.shanghai.yusion4s.bean.token.CheckTokenResp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/8/9.
 */

public interface AuthService {

    @GET("/api/auth/user_login/")
    Call<BaseResult<GetVCodeResp>> getVCode(@Query("mobile") String mobile);

    @POST("/api/auth/login/")
    Call<BaseResult<LoginResp>> login(@Body LoginReq req);

    @POST("/api/auth/user_login/")
    Call<BaseResult<LoginResp>> yusionLogin(@Body LoginReq req);

    @POST("/api/auth/check_token/")
    Call<BaseResult<CheckTokenResp>> checkToken();

    @GET("/api/client/check_user_info/")
    Call<BaseResult<CheckUserInfoResp>> checkUserInfo();

    @GET("/api/check_new_app/")
    Call<BaseResult<UpdateResp>> update(@Query("frontend") String frontend);

    //检查用户信息是否完整
    @GET("/api/client/check_client_info_completed/")
    Call<BaseResult<CheckInfoCompletedResp>> CheckInfoComplete(@Query("clt_id") String clt_id);

    //转换配偶为主带人
    @POST("/api/client/enable_spouse_auth_account/")
    Call<BaseResult<LoginResp>> replaceSpToP(@Body ReplaceSPReq replaceSPReq);

}
