package com.yusion.shanghai.yusion4s.bean.auth;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by LX on 2017/12/25.
 */

public class WXUserInfoResp {

    /**
     * openid : og6yE1VJ-mZGXCkQ2Gjfg2R1Jv0Q
     * nickname : 凤年
     * sex : 0
     * language : zh_CN
     * city :
     * province :
     * country :
     * headimgurl :
     * privilege : []
     * unionid : oi4FbwhfMUO1Svuqplq7T_ViEr_I
     */

    public String openid;
    public String nickname;
    public int sex;
    public String language;
    public String city;
    public String province;
    public String country;
    public String headimgurl;
    public String unionid;
    public List<?> privilege;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
