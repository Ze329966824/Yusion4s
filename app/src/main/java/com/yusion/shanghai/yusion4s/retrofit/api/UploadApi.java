package com.yusion.shanghai.yusion4s.retrofit.api;

import android.app.Dialog;
import android.content.Context;

import com.yusion.shanghai.yusion4s.bean.upload.DelImgsReq;
import com.yusion.shanghai.yusion4s.bean.upload.ListDealerLabelsResp;
import com.yusion.shanghai.yusion4s.bean.upload.ListImgsReq;
import com.yusion.shanghai.yusion4s.bean.upload.ListImgsResp;
import com.yusion.shanghai.yusion4s.bean.upload.ListLabelsErrorReq;
import com.yusion.shanghai.yusion4s.bean.upload.ListLabelsErrorResp;
import com.yusion.shanghai.yusion4s.bean.upload.UploadFilesUrlReq;
import com.yusion.shanghai.yusion4s.retrofit.Api;
import com.yusion.shanghai.yusion4s.retrofit.callback.CustomCallBack;
import com.yusion.shanghai.yusion4s.retrofit.callback.CustomCodeAndMsgCallBack;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnCodeAndMsgCallBack;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.utils.LoadingUtils;

import java.util.List;


/**
 * 类描述：
 * 伟大的创建人：ice
 * 不可相信的创建时间：17/4/20 下午2:30
 */

public class UploadApi {

    public static void uploadFileUrl(final Context context, UploadFilesUrlReq req, final OnCodeAndMsgCallBack onCodeAndMsgCallBack) {
//        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getUploadService().uploadFileUrl(req).enqueue(new CustomCodeAndMsgCallBack(context) {
            @Override
            public void onCustomResponse(int code, String msg) {
                onCodeAndMsgCallBack.callBack(code, msg);
            }
        });
    }

    public static void uploadFileUrl(final Context context, UploadFilesUrlReq req, final OnItemDataCallBack<List<String>> onItemDataCallBack) {
//        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getUploadService().uploadFileUrlWithIdsResp(req).enqueue(new CustomCallBack<List<String>>(context) {
            @Override
            public void onCustomResponse(List<String> data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });
    }

    public static void listImgs(final Context context, ListImgsReq req, OnItemDataCallBack<ListImgsResp> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getUploadService().listImgs(req).enqueue(new CustomCallBack<ListImgsResp>(context, dialog) {
            @Override
            public void onCustomResponse(ListImgsResp data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });
    }

    public static void listLabelsError(final Context context, ListLabelsErrorReq req, OnItemDataCallBack<ListLabelsErrorResp> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getUploadService().listLabelsError(req).enqueue(new CustomCallBack<ListLabelsErrorResp>(context, dialog) {
            @Override
            public void onCustomResponse(ListLabelsErrorResp data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });
    }

    public static void listDealerLabels(final Context context, String app_id, OnItemDataCallBack<List<ListDealerLabelsResp>> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getUploadService().listDealerLabels(app_id).enqueue(new CustomCallBack<List<ListDealerLabelsResp>>(context, dialog) {
            @Override
            public void onCustomResponse(List<ListDealerLabelsResp> data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });
    }

    public static void delImgs(final Context context, DelImgsReq req, OnCodeAndMsgCallBack onCodeAndMsgCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getUploadService().delImgs(req).enqueue(new CustomCodeAndMsgCallBack(context, dialog) {
            @Override
            public void onCustomResponse(int code, String msg) {
                onCodeAndMsgCallBack.callBack(code, msg);
            }
        });
    }
}
