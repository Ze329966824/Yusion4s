package com.yusion.shanghai.yusion4s.bean.login;

import com.google.gson.Gson;

/**
 * Created by Administrator on 2017/8/9.
 */

public class LoginReq {

    /**
     * username : yujian
     * password : yujian
     * dtype : 2
     */
    public String mobile;
    public String verify_code;
    public String username;
    public String password;
    public String dtype = "2";
    public String reg_id;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
