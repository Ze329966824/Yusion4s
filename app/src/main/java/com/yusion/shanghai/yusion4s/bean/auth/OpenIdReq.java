package com.yusion.shanghai.yusion4s.bean.auth;

import com.google.gson.Gson;

/**
 * Created by LX on 2017/9/26.
 */

public class OpenIdReq {
    public String source;
    public String open_id;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
