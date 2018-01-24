package com.yusion.shanghai.yusion4s.bean.auth;

import com.google.gson.Gson;

public class AccessTokenResp {

    /**
     * access_token : ACCESS_TOKEN
     * expires_in : 7200
     * refresh_token : REFRESH_TOKEN
     * openid : OPENID
     * scope : SCOPE
     */

    public String access_token;
    public int expires_in;
    public String refresh_token;
    public String openid;
    public String scope;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
