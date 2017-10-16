package com.yusion.shanghai.yusion4s.bean.login;

import com.google.gson.Gson;

/**
 * Created by Administrator on 2017/8/9.
 */

public class LoginResp {
    public String token;
//    public String mobile;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
