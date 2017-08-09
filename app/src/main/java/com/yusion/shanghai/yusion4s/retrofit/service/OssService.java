package com.yusion.shanghai.yusion4s.retrofit.service;


import com.yusion.shanghai.yusion4s.bean.oss.GetOssTokenBean;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OssService {
    @POST("/token/oss")
    Call<GetOssTokenBean> getOSSToken(@Body Map<String, String> body);
}
