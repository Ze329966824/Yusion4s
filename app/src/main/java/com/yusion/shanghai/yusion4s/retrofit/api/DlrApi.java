package com.yusion.shanghai.yusion4s.retrofit.api;

import android.app.Dialog;
import android.content.Context;
import android.icu.lang.UCharacter;
import android.widget.Toast;

import com.google.gson.internal.bind.SqlDateTypeAdapter;
import com.yusion.shanghai.yusion4s.bean.dlr.GetBrandResp;
import com.yusion.shanghai.yusion4s.bean.dlr.GetDlrListByTokenResp;
import com.yusion.shanghai.yusion4s.bean.dlr.GetLoanBankResp;
import com.yusion.shanghai.yusion4s.bean.dlr.GetModelResp;
import com.yusion.shanghai.yusion4s.bean.dlr.GetTrixResp;
import com.yusion.shanghai.yusion4s.retrofit.Api;
import com.yusion.shanghai.yusion4s.retrofit.callback.CustomCallBack;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.utils.LoadingUtils;

import java.util.List;

/**
 * Created by aa on 2017/8/9.
 */

public class DlrApi {
    public static void getDlrListByToken(final Context context, final OnItemDataCallBack<List<GetDlrListByTokenResp>> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getDlrService().getDlrListByToken().enqueue(new CustomCallBack<List<GetDlrListByTokenResp>>(context, dialog) {
            @Override
            public void onCustomResponse(List<GetDlrListByTokenResp> data) {
                if (data == null) {
                    Toast.makeText(context, "获取门店列表信息失败,请再试一次。", Toast.LENGTH_SHORT).show();
                } else {
                    // onItemDataCallBack.callBack(data);
                    onItemDataCallBack.onItemDataCallBack(data);
                }
            }
        });
    }

    public static void getLoanBank(final Context context, String dlr_id, final OnItemDataCallBack<List<GetLoanBankResp>> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getDlrService().getLoanBank(dlr_id).enqueue(new CustomCallBack<List<GetLoanBankResp>>(context, dialog) {
            @Override
            public void onCustomResponse(List<GetLoanBankResp> data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });
    }

    public static void getBrand(final Context context, String dlr_id, final OnItemDataCallBack<List<GetBrandResp>> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getDlrService().getBrand(dlr_id).enqueue(new CustomCallBack<List<GetBrandResp>>(context, dialog) {
            @Override
            public void onCustomResponse(List<GetBrandResp> data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });
    }

    public static void getTrix(final Context context, String brand, final OnItemDataCallBack<List<GetTrixResp>> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getDlrService().getTrix(brand).enqueue(new CustomCallBack<List<GetTrixResp>>(context, dialog) {
            @Override
            public void onCustomResponse(List<GetTrixResp> data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });
    }

    public static void getModel(final Context context, String trix, final OnItemDataCallBack<List<GetModelResp>> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getDlrService().getModel(trix).enqueue(new CustomCallBack<List<GetModelResp>>(context, dialog) {
            @Override
            public void onCustomResponse(List<GetModelResp> data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });
    }

    public static void getProductType(final Context context, String bankName, final OnItemDataCallBack<List<String>> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getDlrService().getProductType(bankName).enqueue(new CustomCallBack<List<String>>(context, dialog) {
            @Override
            public void onCustomResponse(List<String> data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });
    }

}
