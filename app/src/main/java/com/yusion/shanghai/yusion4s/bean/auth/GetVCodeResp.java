package com.yusion.shanghai.yusion4s.bean.auth;

import com.google.gson.Gson;

/**
 * Created by ice on 2017/8/3.
 */

public class GetVCodeResp {
    public String verify_code;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
