package com.yusion.shanghai.yusion4s.retrofit.api;

import com.yusion.shanghai.yusion4s.retrofit.Api;
import com.yusion.shanghai.yusion4s.retrofit.service.OssService;
import com.yusion.shanghai.yusion4s.settings.Settings;

import retrofit2.Retrofit;

/**
 * Description : 图片上传
 * Author : suijin
 * Date   : 17/04/10
 */
public class OssApi {
    public static Retrofit retrofit = Api.createRetrofit(Settings.OSS_SERVER_URL);
    public static OssService ossService = retrofit.create(OssService.class);
}
