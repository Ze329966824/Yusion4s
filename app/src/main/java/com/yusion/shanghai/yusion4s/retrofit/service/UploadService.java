package com.yusion.shanghai.yusion4s.retrofit.service;


import com.yusion.shanghai.yusion4s.base.BaseResult;
import com.yusion.shanghai.yusion4s.bean.upload.DelImgsReq;
import com.yusion.shanghai.yusion4s.bean.upload.GetTemplateResp;
import com.yusion.shanghai.yusion4s.bean.upload.ListDealerLabelsResp;
import com.yusion.shanghai.yusion4s.bean.upload.ListImgsReq;
import com.yusion.shanghai.yusion4s.bean.upload.ListImgsResp;
import com.yusion.shanghai.yusion4s.bean.upload.ListLabelsErrorReq;
import com.yusion.shanghai.yusion4s.bean.upload.ListLabelsErrorResp;
import com.yusion.shanghai.yusion4s.bean.upload.UploadFilesUrlReq;
import com.yusion.shanghai.yusion4s.bean.upload.UploadLogReq;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 类描述：
 * 伟大的创建人：ice
 * 不可相信的创建时间：17/4/20 下午2:31
 */

public interface UploadService {
    @POST("/api/material/upload_yc_client_material/")
    Call<BaseResult> uploadFileUrl(@Body UploadFilesUrlReq req);

    @POST("/api/material/upload_yc_client_material/")
    Call<BaseResult<List<String>>> uploadFileUrlWithIdsResp(@Body UploadFilesUrlReq req);

    //列举图片
    @POST("/api/material/list_yc_client_material/")
    Call<BaseResult<ListImgsResp>> listImgs(@Body ListImgsReq req);

    @GET("/api/material/list_yc_dealer_material/")
    Call<BaseResult<List<ListDealerLabelsResp>>> listDealerLabels(@Query("app_id") String app_id);

    @POST("/api/material/list_yc_material_uw_error/")
    Call<BaseResult<ListLabelsErrorResp>> listLabelsError(@Body ListLabelsErrorReq req);

    @POST("/api/material/del_yc_client_material/")
    Call<BaseResult> delImgs(@Body DelImgsReq req);

    @POST("/api/material/upload_yc_client_material_log/")
    Call<BaseResult> uploadLog(@Body UploadLogReq req);


    @GET("/api/material/upload_yc_client_material_log/")
    Call<BaseResult> getLog();

//    http://api.alpha.yusiontech.com:8000/api/m/rest/image_checker_config/1/
    @GET("/api/material/list_yc_dealer_material_template/")
    Call<BaseResult<GetTemplateResp>> getTemplate(@Query("id") String id);

    @GET("/api/material/list_credit_material/")
    Call<BaseResult<GetTemplateResp>> getTemplateInSqs(@Query("bank_id") String bank_id,@Query("dlr_id") String dlr_id);
}
