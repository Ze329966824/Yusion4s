package com.yusion.shanghai.yusion4s.retrofit.api;

import android.app.Dialog;
import android.content.Context;
import android.widget.Toast;

import com.yusion.shanghai.yusion4s.bean.auth.AccessTokenResp;
import com.yusion.shanghai.yusion4s.bean.auth.BindingReq;
import com.yusion.shanghai.yusion4s.bean.auth.BindingResp;
import com.yusion.shanghai.yusion4s.bean.auth.CheckInfoCompletedResp;
import com.yusion.shanghai.yusion4s.bean.auth.CheckUserInfoResp;
import com.yusion.shanghai.yusion4s.bean.auth.GetVCodeResp;
import com.yusion.shanghai.yusion4s.bean.auth.OpenIdReq;
import com.yusion.shanghai.yusion4s.bean.auth.OpenIdResp;
import com.yusion.shanghai.yusion4s.bean.auth.ReplaceSPReq;
import com.yusion.shanghai.yusion4s.bean.auth.UpdateResp;
import com.yusion.shanghai.yusion4s.bean.auth.WXUserInfoResp;
import com.yusion.shanghai.yusion4s.bean.login.LoginReq;
import com.yusion.shanghai.yusion4s.bean.login.LoginResp;
import com.yusion.shanghai.yusion4s.bean.token.CheckTokenResp;
import com.yusion.shanghai.yusion4s.retrofit.Api;
import com.yusion.shanghai.yusion4s.retrofit.callback.CustomCallBack;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.settings.Settings;
import com.yusion.shanghai.yusion4s.utils.LoadingUtils;

import java.net.UnknownHostException;

import io.sentry.Sentry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/8/9.
 */

public class AuthApi {
    public static void getVCode(Context context, String mobile, final OnItemDataCallBack<GetVCodeResp> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getAuthService().getVCode(mobile).enqueue(new CustomCallBack<GetVCodeResp>(context, dialog) {
            @Override
            public void onCustomResponse(GetVCodeResp data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });
    }

    public static void login(final Context context, LoginReq req, final OnItemDataCallBack<LoginResp> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getAuthService().login(req).enqueue(new CustomCallBack<LoginResp>(context, dialog) {
            @Override
            public void onCustomResponse(LoginResp data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });
    }

    public static void yusionLogin(final Context context, LoginReq req, final OnItemDataCallBack<LoginResp> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getAuthService().yusionLogin(req).enqueue(new CustomCallBack<LoginResp>(context, dialog) {
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
        //Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getAuthService().checkUserInfo().enqueue(new CustomCallBack<CheckUserInfoResp>(context) {
            @Override
            public void onCustomResponse(CheckUserInfoResp data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });
    }

    public static void update(Context context, String frontend, final OnItemDataCallBack<UpdateResp> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getAuthService().update(frontend).enqueue(new CustomCallBack<UpdateResp>(context, dialog) {
            @Override
            public void onCustomResponse(UpdateResp data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });
    }

    public static void CheckInfoComplete(Context context, String clt_id, final OnItemDataCallBack<CheckInfoCompletedResp> onItemDataCallBack) {
        Api.getAuthService().CheckInfoComplete(clt_id).enqueue(new CustomCallBack<CheckInfoCompletedResp>(context) {
            @Override
            public void onCustomResponse(CheckInfoCompletedResp data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });
    }

    public static void replaceSpToP(Context context, ReplaceSPReq replaceSPReq, final OnItemDataCallBack<LoginResp> onItemDataCallBack) {
        Api.getAuthService().replaceSpToP(replaceSPReq).enqueue(new CustomCallBack<LoginResp>(context) {
            @Override
            public void onCustomResponse(LoginResp data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });
    }

    public static void thirdLogin(Context context, OpenIdReq req, final OnItemDataCallBack<OpenIdResp> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getAuthService().openId(req).enqueue(new CustomCallBack<OpenIdResp>(context, dialog) {
            @Override
            public void onCustomResponse(OpenIdResp data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });
    }

    public static void binding(Context context, BindingReq req, final OnItemDataCallBack<BindingResp> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getAuthService().binding(req).enqueue(new CustomCallBack<BindingResp>(context, dialog) {
            @Override
            public void onCustomResponse(BindingResp data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });
    }

    public static void checkOpenID(Context context, String mobile, String source,String dtype, final OnItemDataCallBack<Integer>onItemDataCallBack){
        Api.getAuthService().checkOpenID(mobile,source,dtype).enqueue(new CustomCallBack<Integer>(context) {
            @Override
            public void onCustomResponse(Integer data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });
    }

    public static void getWXUserInfo(Context context, String access_token,String openid, final OnItemDataCallBack<WXUserInfoResp> onItemDataCallBack) {
        Api.getWXService().getWXUserInfo(access_token,openid).enqueue(new Callback<WXUserInfoResp>() {
            @Override
            public void onResponse(Call<WXUserInfoResp> call, Response<WXUserInfoResp> data) {
                onItemDataCallBack.onItemDataCallBack(data.body());
            }
            @Override
            public void onFailure(Call<WXUserInfoResp> call, Throwable t) {
                if (t instanceof UnknownHostException) {
                    Toast.makeText(context, "网络繁忙,请检查网络", Toast.LENGTH_SHORT).show();
                } else if (Settings.isOnline) {
                    Toast.makeText(context, "接口调用失败,请稍后再试...", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show();
                }
                Sentry.capture(t);
            }
        });
    }

    public static void getAccessToken(Context context, String appid, String secret, String code, String grant_type, final OnItemDataCallBack<AccessTokenResp> onItemDataCallBack){
        Api.getWXService().getAccessToken(appid,secret,code,grant_type).enqueue(new Callback<AccessTokenResp>() {
            @Override
            public void onResponse(Call<AccessTokenResp> call, Response<AccessTokenResp> data) {
                onItemDataCallBack.onItemDataCallBack(data.body());
            }
            @Override
            public void onFailure(Call<AccessTokenResp> call, Throwable t) {
                if (t instanceof UnknownHostException) {
                    Toast.makeText(context, "网络繁忙,请检查网络", Toast.LENGTH_SHORT).show();
                } else if (Settings.isOnline) {
                    Toast.makeText(context, "接口调用失败,请稍后再试...", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show();
                }
                Sentry.capture(t);
            }
        });
    }
}
