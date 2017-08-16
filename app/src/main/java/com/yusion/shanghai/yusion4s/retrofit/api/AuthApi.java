package com.yusion.shanghai.yusion4s.retrofit.api;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;

import com.yusion.shanghai.yusion4s.Yusion4sApp;
import com.yusion.shanghai.yusion4s.bean.auth.CheckUserInfoResp;
import com.yusion.shanghai.yusion4s.bean.login.LoginReq;
import com.yusion.shanghai.yusion4s.bean.login.LoginResp;
import com.yusion.shanghai.yusion4s.bean.token.CheckTokenResp;
import com.yusion.shanghai.yusion4s.retrofit.Api;
import com.yusion.shanghai.yusion4s.retrofit.callback.CustomCallBack;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.utils.LoadingUtils;

/**
 * Created by Administrator on 2017/8/9.
 */

public class AuthApi {
    public static void login(final Context context, LoginReq req, final OnItemDataCallBack<LoginResp> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        req.reg_id = Yusion4sApp.reg_id;
        Log.e("reg_id",req.reg_id);
        Api.getAuthService().login(req).enqueue(new CustomCallBack<LoginResp>(context,dialog) {
            @Override
            public void onCustomResponse(LoginResp data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });
    }
    public static void checkToken(final Context context, final OnItemDataCallBack<CheckTokenResp> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getAuthService().checkToken().enqueue(new CustomCallBack<CheckTokenResp>(context, dialog) {
            @Override
            public void onCustomResponse(CheckTokenResp data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });
    }
    public static void checkUserInfo(Context context, final OnItemDataCallBack<CheckUserInfoResp> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getAuthService().checkUserInfo().enqueue(new CustomCallBack<CheckUserInfoResp>(context, dialog) {
            @Override
            public void onCustomResponse(CheckUserInfoResp data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });
    }


}
