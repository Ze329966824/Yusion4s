package com.yusion.shanghai.yusion4s.ubt.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.bean.auth.CheckUserInfoResp;
import com.yusion.shanghai.yusion4s.retrofit.Api;
import com.yusion.shanghai.yusion4s.retrofit.api.PersonApi;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.ubt.bean.UBTData;
import com.yusion.shanghai.yusion4s.utils.ApiUtil;
import com.yusion.shanghai.yusion4s.utils.MobileDataUtil;
import com.yusion.shanghai.yusion4s.utils.SharedPrefsUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 用于上传用户信息和手机设备信息的服务
 * Created by aa on 2017/12/20.
 */

public class UploadPersonInfoService extends IntentService {

    public UploadPersonInfoService() {
        super("UploadPersonInfoService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            uploadPersonAndDeviceInfo(this);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static void uploadPersonAndDeviceInfo(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        UBTData req = new UBTData(context);

        JSONArray contactJsonArray = MobileDataUtil.getUserData(context, "contact");
        List<UBTData.DataBean.ContactBean> contactBeenList = new ArrayList<>();
        for (int i = 0; i < contactJsonArray.length(); i++) {
            JSONObject jsonObject = null;
            try {
                jsonObject = contactJsonArray.getJSONObject(i);
                UBTData.DataBean.ContactBean contactListBean = new UBTData.DataBean.ContactBean();

                contactListBean.data1 = jsonObject.optString("data1");
                contactListBean.display_name = jsonObject.optString("display_name");

                contactBeenList.add(contactListBean);
                //raw_list.add(jsonObject.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        UBTData.DataBean contactBean = new UBTData.DataBean();
        contactBean.category = "contact";
        req.data.add(contactBean);
        if (contactBeenList.size() > 0 && !contactBeenList.isEmpty()) {
            contactBean.contact_list = contactBeenList;
        }

        JSONArray smsJsonArray = MobileDataUtil.getUserData(context, "sms");
        List<UBTData.DataBean.SmsBean> smsList = new ArrayList<>();
        for (int i = 0; i < smsJsonArray.length(); i++) {
            JSONObject jsonObject = null;
            try {
                jsonObject = smsJsonArray.getJSONObject(i);
                UBTData.DataBean.SmsBean smsListBean = new UBTData.DataBean.SmsBean();
                String type = jsonObject.optString("type");
                if (type.equals("1")) {
                    smsListBean.from = jsonObject.optString("address");
                    smsListBean.content = jsonObject.optString("body");
                    smsListBean.type = "recv";
                    smsListBean.ts = jsonObject.optString("date");
                } else if (type.equals("2")) {
                    smsListBean.to = jsonObject.optString("address");
                    smsListBean.content = jsonObject.optString("body");
                    smsListBean.type = "snd";
                    smsListBean.ts = jsonObject.optString("date");//date
                }
                smsList.add(smsListBean);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        UBTData.DataBean simBean = new UBTData.DataBean();
        simBean.category = "sms";
        req.data.add(simBean);
        if (smsList.size() > 0 && !smsList.isEmpty()) {
            simBean.sms_list = smsList;
        }
        ApiUtil.requestUrl4Data(context, Api.getAuthService().checkUserInfo(), true, (OnItemDataCallBack<CheckUserInfoResp>) data -> {
            if (data == null) {
                return;
            }
            contactBean.clt_nm = data.name;
            contactBean.mobile = data.mobile;
            simBean.clt_nm = data.name;
            simBean.mobile = data.mobile;
            req.imei = telephonyManager.getDeviceId();
            req.imsi = telephonyManager.getSubscriberId();
            req.app = context.getResources().getString(R.string.app_name);
            req.token = SharedPrefsUtil.getInstance(context).getValue("token", null);
            req.mobile = SharedPrefsUtil.getInstance(context).getValue("mobile", null);
            PersonApi.uploadPersonAndDeviceInfo(req, new Callback() {
                @Override
                public void onResponse(Call call, Response response) {

                }

                @Override
                public void onFailure(Call call, Throwable t) {

                }
            });
        });
    }

}
