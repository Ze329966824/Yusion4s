package com.yusion.shanghai.yusion4s.retrofit.api;

import android.app.Dialog;
import android.content.Context;

import com.yusion.shanghai.yusion4s.bean.order.SearchClientResp;
import com.yusion.shanghai.yusion4s.bean.order.GetAppListResp;
import com.yusion.shanghai.yusion4s.bean.order.OrderDetailBean;
import com.yusion.shanghai.yusion4s.bean.order.submit.SubmitOrderReq;
import com.yusion.shanghai.yusion4s.bean.order.submit.SubmitOrderResp;
import com.yusion.shanghai.yusion4s.retrofit.Api;
import com.yusion.shanghai.yusion4s.retrofit.callback.CustomCallBack;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.utils.LoadingUtils;

import java.util.List;

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

    public static void searchClientExist(final Context context, String key, final OnItemDataCallBack<List<SearchClientResp>> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
//        Api.getOrderService().searchClient(clt_nm).enqueue(new CustomCallBack<List<SearchClientResp>>()(context, dialog) {
//            @Override
//            public void onCustomResponse(SearchClientResp data) {
//                onItemDataCallBack.onItemDataCallBack(data);
//            }
//        });
        Api.getOrderService().searchClient(key).enqueue(new CustomCallBack<List<SearchClientResp>>(context, dialog) {
            @Override
            public void onCustomResponse(List<SearchClientResp> resp) {
                onItemDataCallBack.onItemDataCallBack(resp);
            }
        });
    }

    public static void getAppList(final Context context, String st, final OnItemDataCallBack<List<GetAppListResp>> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getOrderService().getAppList(st).enqueue(new CustomCallBack<List<GetAppListResp>>(context, dialog) {
            @Override
            public void onCustomResponse(List<GetAppListResp> data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });
    }

    public static void getAppDetails(final Context context, String app_id, final OnItemDataCallBack<OrderDetailBean> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getOrderService().getAppDetails(app_id).enqueue(new CustomCallBack<OrderDetailBean>(context, dialog) {
            @Override
            public void onCustomResponse(OrderDetailBean data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });
    }

}
