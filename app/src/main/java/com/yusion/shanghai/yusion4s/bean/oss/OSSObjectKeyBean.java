package com.yusion.shanghai.yusion4s.bean.oss;


import com.yusion.shanghai.yusion4s.Yusion4sApp;

/**
 * Created by ice on 2017/8/4.
 */

public class OSSObjectKeyBean {
    public OSSObjectKeyBean(String role, String category, String suffix) {
        this.role = role;
        this.category = category;
        this.suffix = suffix;
    }

    public OSSObjectKeyBean() {
    }

    public String client;
    public String mobile = Yusion4sApp.ACCOUNT;
    public String role;
    public String category;
    public String suffix;
}
