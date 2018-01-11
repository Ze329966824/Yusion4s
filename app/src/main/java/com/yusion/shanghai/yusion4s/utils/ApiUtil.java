package com.yusion.shanghai.yusion4s.utils;

import android.app.Dialog;
import android.content.Context;

import com.yusion.shanghai.yusion4s.base.BaseResult;
import com.yusion.shanghai.yusion4s.retrofit.callback.CustomCallBack;
import com.yusion.shanghai.yusion4s.retrofit.callback.CustomCodeAndMsgCallBack;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnCodeAndMsgCallBack;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;

import retrofit2.Call;

/**
 * Created by ice on 2017/12/20.
 */

public class ApiUtil {
    public static <T> void requestUrl4Data(Context context, Call<BaseResult<T>> call, final OnItemDataCallBack<T>... onItemDataCallBacks) {
        requestUrl4Data(context, call, false, onItemDataCallBacks);
    }

    public static void requestUrl4CodeAndMsg(Context context, Call<BaseResult> call, final OnCodeAndMsgCallBack onCodeAndMsgCallBack) {
        requestUrl4CodeAndMsg(context, call, false, onCodeAndMsgCallBack);
    }

    /**
     * @param isSilence           false表示会自动创建dialog并管理生命周期
     * @param onItemDataCallBacks 可变参数列表 第一个callback在调用接口有数据后回调 第二个callback在调用接口返回null时回调
     */
    public static <T> void requestUrl4Data(Context context, Call<BaseResult<T>> call, boolean isSilence, final OnItemDataCallBack<T>... onItemDataCallBacks) {
        if (call == null) {
            throw new IllegalArgumentException("必要数据缺失");
        }
        Dialog dialog;
        if (isSilence) {
            dialog = null;
        } else {
            dialog = LoadingUtils.createLoadingDialog(context);
            dialog.setOnCancelListener(loadingDialog -> call.cancel());
        }
        call.enqueue(new CustomCallBack<T>(context, dialog) {
            @Override
            public void onCustomResponse(T data) {
                if (onItemDataCallBacks != null && onItemDataCallBacks.length > 0) {
                    onItemDataCallBacks[0].onItemDataCallBack(data);
                }
            }

            @Override
            protected void onEmptyDataResponse() {
                if (onItemDataCallBacks != null && onItemDataCallBacks.length > 1) {
                    onItemDataCallBacks[1].onItemDataCallBack(null);
                }
            }
        });
    }

    public static void requestUrl4CodeAndMsg(Context context, Call<BaseResult> call, boolean isSilence, final OnCodeAndMsgCallBack onCodeAndMsgCallBack) {
        if (call == null) {
            throw new IllegalArgumentException("必要数据缺失");
        }
        Dialog dialog;
        if (isSilence) {
            dialog = null;
        } else {
            dialog = LoadingUtils.createLoadingDialog(context);
            dialog.setOnCancelListener(loadingDialog -> call.cancel());
        }
        call.enqueue(new CustomCodeAndMsgCallBack(context, dialog) {
            @Override
            public void onCustomResponse(int code, String msg) {
                if (onCodeAndMsgCallBack != null) {
                    onCodeAndMsgCallBack.callBack(code, msg);
                }
            }
        });
    }
}
