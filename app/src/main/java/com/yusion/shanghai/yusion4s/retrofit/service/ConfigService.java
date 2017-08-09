package com.yusion.shanghai.yusion4s.retrofit.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Administrator on 2017/8/9.
 */

public interface ConfigService {
    @GET("/api/material/global_config/")
    Call<ResponseBody> getConfigJson();
}
