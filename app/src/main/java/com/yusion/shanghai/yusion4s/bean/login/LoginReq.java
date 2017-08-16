package com.yusion.shanghai.yusion4s.bean.login;

/**
 * Created by Administrator on 2017/8/9.
 */

public class LoginReq {

    /**
     * username : yujian
     * password : yujian
     * dtype : 2
     */

    public String username;
    public String password;
    public String dtype = "2";
    public String reg_id;

    @Override
    public String toString() {
        return "LoginReq{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", dtype='" + dtype + '\'' +
                ", reg_id='" + reg_id + '\'' +
                '}';
    }
}
