package com.yusion.shanghai.yusion4s.bean.order.submit;

import com.google.gson.Gson;

/**
 * Created by LX on 2017/12/18.
 */

public class ReSubmitReq {

    /*
        "clt_id":"0ecc33eedb5711e788540242ac110002"
        "app_id":"YJCS0001"
    */

    public String clt_id;
    public String app_id;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
