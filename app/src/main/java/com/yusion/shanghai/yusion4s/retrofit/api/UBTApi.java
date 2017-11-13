package com.yusion.shanghai.yusion4s.retrofit.api;


import com.yusion.shanghai.yusion4s.retrofit.Api;
import com.yusion.shanghai.yusion4s.retrofit.service.UBTService;
import com.yusion.shanghai.yusion4s.settings.Settings;

import retrofit2.Retrofit;

public class UBTApi {

    public static Retrofit retrofit = Api.createRetrofit(Settings.UBT_SERVER_URL);
    public static UBTService getUBTService() {
        return retrofit.create(UBTService.class);
    }
}
