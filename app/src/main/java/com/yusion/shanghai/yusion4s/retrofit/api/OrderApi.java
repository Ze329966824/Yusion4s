package com.yusion.shanghai.yusion4s.retrofit.api;

import android.app.Dialog;
import android.content.Context;

import com.yusion.shanghai.yusion4s.bean.dlr.GetRawCarInfoResp;
import com.yusion.shanghai.yusion4s.bean.order.GetAppListResp;
import com.yusion.shanghai.yusion4s.bean.order.GetFinancePlanDetailResp;
import com.yusion.shanghai.yusion4s.bean.order.OrderDetailBean;
import com.yusion.shanghai.yusion4s.bean.order.RefreshAppList;
import com.yusion.shanghai.yusion4s.bean.order.SearchClientResp;
import com.yusion.shanghai.yusion4s.bean.order.submit.GetApplicateDetailResp;
import com.yusion.shanghai.yusion4s.bean.order.submit.ReSubmitReq;
import com.yusion.shanghai.yusion4s.bean.order.submit.ReSubmitResp;
import com.yusion.shanghai.yusion4s.bean.order.submit.SubmitOrderReq;
import com.yusion.shanghai.yusion4s.bean.order.submit.SubmitOrderResp;
import com.yusion.shanghai.yusion4s.retrofit.Api;
import com.yusion.shanghai.yusion4s.retrofit.callback.CustomCallBack;
import com.yusion.shanghai.yusion4s.retrofit.callback.CustomCodeAndMsgCallBack;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnCodeAndMsgCallBack;
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
        Api.getOrderService().searchClient(key).enqueue(new CustomCallBack<List<SearchClientResp>>(context, dialog) {
            @Override
            public void onCustomResponse(List<SearchClientResp> resp) {
                onItemDataCallBack.onItemDataCallBack(resp);
            }
        });
    }

    public static void getApplicateDetail(final Context context, String clt_id, final OnItemDataCallBack<GetApplicateDetailResp> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getOrderService().getApplicateDetail(clt_id).enqueue(new CustomCallBack<GetApplicateDetailResp>(context, dialog) {
            @Override
            public void onCustomResponse(GetApplicateDetailResp data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });
    }


//    public static void getAppList(final Context context, String st, final OnItemDataCallBack<List<GetAppListResp>> onItemDataCallBack) {
//        Dialog dialog = LoadingUtils.createLoadingDialog(context);
//        Api.getOrderService().getAppList(st).enqueue(new CustomCallBack<List<GetAppListResp>>(context, dialog) {
//            @Override
//            public void onCustomResponse(List<GetAppListResp> data) {
//                onItemDataCallBack.onItemDataCallBack(data);
//            }
//
//        });
//
//    }

    public static void getAppList(final Context context, String st, String vehicle_cond, int page, final OnItemDataCallBack<GetAppListResp> onItemDataCallBack) {
//        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getOrderService().getAppList(st,vehicle_cond, page).enqueue(new CustomCallBack<GetAppListResp>(context) {
            @Override
            public void onCustomResponse(GetAppListResp data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });

    }

//    public static void getAppList(final Context context, String st, String vehicle_cond, final OnItemDataCallBack<List<GetAppListResp>> onItemDataCallBack) {
////        Dialog dialog = LoadingUtils.createLoadingDialog(context);
//        Api.getOrderService().getAppList(st, vehicle_cond).enqueue(new CustomCallBack<List<GetAppListResp>>(context) {
//            @Override
//            public void onCustomResponse(List<GetAppListResp> data) {
//                onItemDataCallBack.onItemDataCallBack(data);
//            }
//        });
//
//    }

    public static void getAppDetails(final Context context, String app_id, final OnItemDataCallBack<OrderDetailBean> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getOrderService().getAppDetails2(app_id).enqueue(new CustomCallBack<OrderDetailBean>(context, dialog) {
            @Override
            public void onCustomResponse(OrderDetailBean data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });
    }

    public static void getFinancePlanDetail(final Context context, String plan_id, final OnItemDataCallBack<GetFinancePlanDetailResp> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getOrderService().getFinancePlanDetail(plan_id).enqueue(new CustomCallBack<GetFinancePlanDetailResp>(context, dialog) {
            @Override
            public void onCustomResponse(GetFinancePlanDetailResp resp) {
                onItemDataCallBack.onItemDataCallBack(resp);
            }
        });
    }

    public static void getRawCarInfo(final Context context, String app_id, final OnItemDataCallBack<GetRawCarInfoResp> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getOrderService().getRawCarInfo(app_id).enqueue(new CustomCallBack<GetRawCarInfoResp>(context, dialog) {
            @Override
            public void onCustomResponse(GetRawCarInfoResp resp) {
                onItemDataCallBack.onItemDataCallBack(resp);
            }
        });
    }

    public static void submitAlterInfo(final Context context, GetRawCarInfoResp req, final OnCodeAndMsgCallBack onCodeAndMsgCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getOrderService().submitAlterInfo(req).enqueue(new CustomCodeAndMsgCallBack(context, dialog) {
            @Override
            public void onCustomResponse(int code, String msg) {
                onCodeAndMsgCallBack.callBack(code, msg);
            }
        });
    }

    public static void reSubmit(final Context context, ReSubmitReq req, final OnItemDataCallBack<ReSubmitResp> onItemDataCallBack) {
        Api.getOrderService().reSubmit(req).enqueue(new CustomCallBack<ReSubmitResp>(context) {
            @Override
            public void onCustomResponse(ReSubmitResp data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });
    }


}
