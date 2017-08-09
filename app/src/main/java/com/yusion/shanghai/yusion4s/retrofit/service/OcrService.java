package com.yusion.shanghai.yusion4s.retrofit.service;


import com.yusion.shanghai.yusion4s.base.BaseResult;
import com.yusion.shanghai.yusion4s.bean.ocr.OcrReq;
import com.yusion.shanghai.yusion4s.bean.ocr.OcrResp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OcrService {
    @POST("/api/material/ocr/")
    Call<BaseResult<OcrResp>> requestOcr(@Body OcrReq req);
}
