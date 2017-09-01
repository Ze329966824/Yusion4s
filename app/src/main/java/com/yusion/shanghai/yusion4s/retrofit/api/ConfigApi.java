package com.yusion.shanghai.yusion4s.retrofit.api;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.yusion.shanghai.yusion4s.Yusion4sApp;
import com.yusion.shanghai.yusion4s.bean.config.ConfigResp;
import com.yusion.shanghai.yusion4s.retrofit.Api;
import com.yusion.shanghai.yusion4s.retrofit.callback.CustomResponseBodyCallBack;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnDataCallBack;
import com.yusion.shanghai.yusion4s.settings.Constants;
import com.yusion.shanghai.yusion4s.utils.LoadingUtils;
import com.yusion.shanghai.yusion4s.utils.SharedPrefsUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/8/9.
 */

public class ConfigApi {
    /**
     * 获取配置文件
     * 如果返回的responseBody为空则从缓存文件中取
     * 否则直接返回并存入缓存文件
     */
    public static void getConfigJson(final Context context, final OnDataCallBack<ConfigResp> onDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getConfigService().getConfigJson().enqueue(new CustomResponseBodyCallBack(context, dialog) {
            @Override
            public void onCustomResponse(String body) {
                JSONObject data;
                try {
                    if (TextUtils.isEmpty(body)) {
                        //new JsonObject("")会throw new JSONException();
                        data = new JSONObject(SharedPrefsUtil.getInstance(context).getValue("config_json", ""));
                    } else {
                        data = new JSONObject(body).getJSONObject("data");
                        SharedPrefsUtil.getInstance(context).putValue("config_json", data.toString());
                    }
                    onDataCallBack.callBack(parseJsonObject2ConfigResp(context, data));
                } catch (JSONException e) {
                    //两种可能 一种是用户第一次使用APP时未能成功拉取服务器配置文件 一种是parseJsonObject2ConfigResp解析出错
                    Toast.makeText(context, "静态文件获取失败，请重新打开APP。", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }


    private static ConfigResp parseJsonObject2ConfigResp(Context context, JSONObject jsonObject) throws JSONException {
        ConfigResp configResp = new ConfigResp();

//        JSONArray employee_position_list = jsonObject.getJSONArray("employee_position");
//        for (int i = 0; i < employee_position_list.length(); i++) {
//            JSONObject o = employee_position_list.getJSONObject(i);
//            String next = o.keys().next();
//            configResp.regist_job_list_key.add(next);
//            configResp.regist_job_list_value.add(o.getString(next));
//        }
//
//        JSONArray loan_periods_list = jsonObject.getJSONArray("loan_periods");
//        for (int i = 0; i < loan_periods_list.length(); i++) {
//            configResp.loan_periods_list.add(loan_periods_list.getInt(i));
//        }

        String agreement_url = jsonObject.optString("agreement_url");
        configResp.agreement_url = agreement_url;

        JSONArray loan_periods = jsonObject.optJSONArray("loan_periods");
        for (int i = 0; loan_periods != null && i < loan_periods.length(); i++) {
            JSONObject item = loan_periods.getJSONObject(i);
            String key = item.keys().next();
            String value = item.getString(key);
            configResp.loan_periods_key.add(key);
            configResp.loan_periods_value.add(value);
        }

        JSONArray owner_lender_relationship_list = jsonObject.optJSONArray("vehicle_owner_lender_relationship_list");
        for (int i = 0; owner_lender_relationship_list != null && i < owner_lender_relationship_list.length(); i++) {
            JSONObject item = owner_lender_relationship_list.getJSONObject(i);
            String key = item.keys().next();
            String value = item.getString(key);
            configResp.owner_applicant_relation_key.add(key);
            configResp.owner_applicant_relation_value.add(value);
        }
        //vehicle_owner_lender_relationship_list


        JSONArray label_list = jsonObject.optJSONArray("label_list");
        for (int i = 0; label_list != null && i < label_list.length(); i++) {
            JSONObject item = label_list.getJSONObject(i);
            String key = item.keys().next();
            String value = item.getString(key);
            configResp.label_list_key.add(key);
            configResp.label_list_value.add(value);
        }

        JSONArray dealer_material = jsonObject.optJSONArray("dealer_material");
        configResp.dealer_material = dealer_material != null ? dealer_material.toString() : "";

        Yusion4sApp.CONFIG_RESP = configResp;

        return configResp;
    }
}
