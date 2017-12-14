package com.yusion.shanghai.yusion4s.bean.user;

import com.google.gson.Gson;

/**
 * Created by LX on 2017/11/21.
 */

public class GetClientInfoReq {
    public String id_no = "";
    public String clt_nm = "";
    public String mobile = "";

    public GetClientInfoReq() {
    }

    public GetClientInfoReq(String id_no, String clt_nm) {
        this.id_no = id_no;
        this.clt_nm = clt_nm;
    }

    public GetClientInfoReq(String id_no, String clt_nm, String mobile) {
        this.id_no = id_no;
        this.clt_nm = clt_nm;
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}