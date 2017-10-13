package com.yusion.shanghai.yusion4s.retrofit.api;


import com.yusion.shanghai.yusion4s.retrofit.Api;
import com.yusion.shanghai.yusion4s.retrofit.service.PersonInfoService;
import com.yusion.shanghai.yusion4s.settings.Settings;
import com.yusion.shanghai.yusion4s.ubt.bean.UBTData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

/**
 * Created by aa on 2017/9/18.
 */

public class PersonApi {
    public static Retrofit retrofit = Api.createRetrofit(Settings.PERSON_SERVER_URL);

    private static PersonInfoService getPersonInfoService() {
        return retrofit.create(PersonInfoService.class);
    }

    public static void uploadPersonAndDeviceInfo(UBTData req) {
        uploadPersonAndDeviceInfo(req, null);
    }
    public static void uploadPersonAndDeviceInfo(UBTData req, Callback callback) {
        PersonApi.getPersonInfoService().uploadPersonAndDeviceInfo(req).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                if (callback != null) {
                    callback.onResponse(call, response);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if (callback != null) {
                    callback.onFailure(call, t);
                }
            }
        });
    }
}
