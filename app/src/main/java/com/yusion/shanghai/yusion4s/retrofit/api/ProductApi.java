package com.yusion.shanghai.yusion4s.retrofit.api;

import android.app.Dialog;
import android.content.Context;

import com.yusion.shanghai.yusion4s.bean.auth.GetVCodeResp;
import com.yusion.shanghai.yusion4s.bean.product.GetAuthenticationVerifyCodeReq;
import com.yusion.shanghai.yusion4s.bean.product.RequestAuthenticationReq;
import com.yusion.shanghai.yusion4s.bean.user.ClientInfo;
import com.yusion.shanghai.yusion4s.bean.user.GetClientInfoReq;
import com.yusion.shanghai.yusion4s.retrofit.Api;
import com.yusion.shanghai.yusion4s.retrofit.callback.CustomCallBack;
import com.yusion.shanghai.yusion4s.retrofit.callback.CustomCodeAndMsgCallBack;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnCodeAndMsgCallBack;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.utils.LoadingUtils;

/**
 * Created by LX on 2017/11/20.
 */

public class ProductApi {
    public static void requestAuthentication(final Context context, RequestAuthenticationReq req, final OnCodeAndMsgCallBack onCodeAndMsgCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getProductService().requestAuthentication(req).enqueue(new CustomCodeAndMsgCallBack(context, dialog) {
            @Override
            public void onCustomResponse(int code, String msg) {
                onCodeAndMsgCallBack.callBack(code, msg);
            }
        });
    }

    public static void getAuthenticationVerifyCode(final Context context, GetAuthenticationVerifyCodeReq req, final OnItemDataCallBack<GetVCodeResp> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getProductService().getAuthenticationVerifyCode(req.mobile, req.oneself_clt_id, req.other_clt_id, req.relation_ship).enqueue(new CustomCallBack<GetVCodeResp>(context, dialog) {
            @Override
            public void onCustomResponse(GetVCodeResp data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });
    }

    public static void getClientInfo(final Context context, GetClientInfoReq req, String token, final OnItemDataCallBack<ClientInfo> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getProductService().getClientInfo(req.id_no, req.clt_nm, req.update,token).enqueue(
                new CustomCallBack<ClientInfo>(context, dialog) {
                    @Override
                    public void onCustomResponse(ClientInfo data) {
                        onItemDataCallBack.onItemDataCallBack(data);
                    }
                });
    }

    public static void updateClientInfo(final Context context, ClientInfo req, final OnItemDataCallBack<ClientInfo> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getProductService().updateClientInfo(req).enqueue(
                new CustomCallBack<ClientInfo>(context, dialog) {
                    @Override
                    public void onCustomResponse(ClientInfo data) {
                        onItemDataCallBack.onItemDataCallBack(data);
                    }
                });
    }
}
