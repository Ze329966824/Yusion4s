package com.yusion.shanghai.yusion4s.retrofit.service;
/*
 * 类描述：
 * 伟大的创建人：ice   
 * 不可相信的创建时间：17/6/14 下午5:19 
 * 爱信不信的修改时间：17/6/14 下午5:19 
 * @version
 */


import com.yusion.shanghai.yusion4s.ubt.bean.UBTData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UBTService {

    @POST("/ubt/")
    Call<Void> postUBTData(@Body UBTData req);

}
