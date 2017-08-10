package com.yusion.shanghai.yusion4s.retrofit.api;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

import com.yusion.shanghai.yusion4s.base.BaseResult;
import com.yusion.shanghai.yusion4s.bean.order.CheckClientExistResp;
import com.yusion.shanghai.yusion4s.bean.order.submit.SubmitOrderReq;
import com.yusion.shanghai.yusion4s.bean.order.submit.SubmitOrderResp;
import com.yusion.shanghai.yusion4s.retrofit.Api;
import com.yusion.shanghai.yusion4s.retrofit.callback.CustomCallBack;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnDataCallBack;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.utils.LoadingUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by aa on 2017/8/9.
 */

public class OrderApi {
    public static void submitOrder(final Context context, SubmitOrderReq req, final OnItemDataCallBack<SubmitOrderResp> onItemDataCallBack) {

        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getOrderService().submitOrder(req).enqueue(new CustomCallBack<SubmitOrderResp>(context, dialog) {
            @Override
            public void onCustomResponse(SubmitOrderResp data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });
    }

    public static void checkClientExist(final Context context, String idNo, final OnItemDataCallBack<CheckClientExistResp> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getOrderService().checkClientExist(idNo).enqueue(new CustomCallBack<CheckClientExistResp>(context, dialog) {
            @Override
            public void onCustomResponse(CheckClientExistResp data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });
    }

}
