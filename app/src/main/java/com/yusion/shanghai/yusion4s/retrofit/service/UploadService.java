package com.yusion.shanghai.yusion4s.retrofit.service;


import com.yusion.shanghai.yusion4s.base.BaseResult;
import com.yusion.shanghai.yusion4s.bean.upload.ListImgsReq;
import com.yusion.shanghai.yusion4s.bean.upload.ListImgsResp;
import com.yusion.shanghai.yusion4s.bean.upload.ListLabelsErrorReq;
import com.yusion.shanghai.yusion4s.bean.upload.ListLabelsErrorResp;
import com.yusion.shanghai.yusion4s.bean.upload.UploadFilesUrlReq;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 类描述：
 * 伟大的创建人：ice
 * 不可相信的创建时间：17/4/20 下午2:31
 */

public interface UploadService {
    @POST("/api/material/upload_yc_client_material/")
    Call<BaseResult> uploadFileUrl(@Body UploadFilesUrlReq req);

    @POST("/api/material/list_yc_client_material/")
    Call<BaseResult<ListImgsResp>> listImgs(@Body ListImgsReq req);

    @POST("/api/material/list_yc_material_uw_error/")
    Call<BaseResult<ListLabelsErrorResp>> listLabelsError(@Body ListLabelsErrorReq req);
}
