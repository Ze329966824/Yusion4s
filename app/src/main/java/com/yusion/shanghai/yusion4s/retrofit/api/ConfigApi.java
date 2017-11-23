package com.yusion.shanghai.yusion4s.retrofit.api;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.yusion.shanghai.yusion4s.Yusion4sApp;
import com.yusion.shanghai.yusion4s.bean.config.ConfigResp;
import com.yusion.shanghai.yusion4s.retrofit.Api;
import com.yusion.shanghai.yusion4s.retrofit.callback.CustomResponseBodyCallBack;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnVoidCallBack;
import com.yusion.shanghai.yusion4s.ubt.UBT;
import com.yusion.shanghai.yusion4s.utils.SharedPrefsUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2017/8/9.
 */

public class ConfigApi {
    /**
     * 获取配置文件
     * 如果返回的responseBody为空则从缓存文件中取
     * 否则直接返回并存入缓存文件
     */
    public static void getConfigJson(final Context context, final OnVoidCallBack onVoidCallBack) {
        Api.getConfigService().getConfigJson().enqueue(new CustomResponseBodyCallBack(context) {
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
                    Yusion4sApp.CONFIG_RESP = parseJsonObject2ConfigResp(data);
                    if (onVoidCallBack != null)
                        onVoidCallBack.callBack();
                } catch (JSONException e) {
                    //两种可能 一种是用户第一次使用APP时未能成功拉取服务器配置文件 一种是parseJsonObject2ConfigResp解析出错
                    Toast.makeText(context, "静态文件获取失败，请重新打开APP。", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }


    public static ConfigResp parseJsonObject2ConfigResp(JSONObject jsonObject) throws JSONException {
        ConfigResp configResp = new ConfigResp();
        configResp.DELAY_MILLIS = jsonObject.optInt("lost_focus_delay");
        configResp.agreement_url = jsonObject.optString("agreement_url");
        configResp.send_hand_base_material = jsonObject.optString("send_hand_base_material");
        configResp.contract_list_url = jsonObject.optString("contract_list_url");
        String ubt_limit = jsonObject.optString("ubt_limit");
        if (!TextUtils.isEmpty(ubt_limit)) {
            UBT.LIMIT = Integer.valueOf(ubt_limit);
        }

        fullConfigResp(jsonObject, "", configResp.carInfo_alter_key, configResp.carInfo_alter_value);
        fullConfigResp(jsonObject, "vehicle_owner_lender_relationship_list", configResp.owner_applicant_relation_key, configResp.owner_applicant_relation_value);
        fullConfigResp(jsonObject, "application_modify_reason_list", configResp.carInfo_alter_key, configResp.carInfo_alter_value);
        fullConfigResp(jsonObject, "label_list", configResp.label_list_key, configResp.label_list_value);
        fullConfigResp(jsonObject, "app_display", configResp.order_type_key, configResp.order_type_value);

        JSONArray dealer_material = jsonObject.optJSONArray("dealer_material");
        configResp.dealer_material = dealer_material != null ? dealer_material.toString() : "";


        return configResp;
    }

    private static void fullConfigResp(JSONObject jsonObject, String arrayName, List<String> keyList, List<String> valueList) throws JSONException {
        JSONArray education_list = jsonObject.optJSONArray(arrayName);
        for (int i = 0; education_list != null && i < education_list.length(); i++) {
            JSONObject o = education_list.getJSONObject(i);
            String next = o.keys().next();
            keyList.add(next);
            valueList.add(o.getString(next));
        }
    }
}
