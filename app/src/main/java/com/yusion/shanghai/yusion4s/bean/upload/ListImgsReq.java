package com.yusion.shanghai.yusion4s.bean.upload;

/**
 * Created by ice on 2017/7/19.
 */

public class ListImgsReq {
    public String clt_id;
    public String app_id;
    public String role;
    public String label;

    @Override
    public String toString() {
        return "ListImgsReq{" +
                "clt_id='" + clt_id + '\'' +
                ", app_id='" + app_id + '\'' +
                ", role='" + role + '\'' +
                ", label='" + label + '\'' +
                '}';
    }
}
