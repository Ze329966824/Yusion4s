package com.yusion.shanghai.yusion4s.retrofit.service;


import com.yusion.shanghai.yusion4s.ubt.bean.UBTData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by aa on 2017/9/18.
 */

public interface PersonInfoService {
    @POST("contact/")
    Call<Void> uploadPersonAndDeviceInfo(@Body UBTData req);
}
