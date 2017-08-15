package com.yusion.shanghai.yusion4s.retrofit.service;

import com.yusion.shanghai.yusion4s.base.BaseResult;
import com.yusion.shanghai.yusion4s.bean.auth.CheckUserInfoResp;
import com.yusion.shanghai.yusion4s.bean.login.LoginReq;
import com.yusion.shanghai.yusion4s.bean.login.LoginResp;
import com.yusion.shanghai.yusion4s.bean.token.CheckTokenResp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/8/9.
 */

public interface AuthService {

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("/api/auth/login/")
    Call<BaseResult<LoginResp>> login(@Body LoginReq req);

    @POST("/api/auth/check_token/")
    Call<BaseResult<CheckTokenResp>> checkToken();

    @GET("/api/client/check_user_info/")
    Call<BaseResult<CheckUserInfoResp>> checkUserInfo();
}
