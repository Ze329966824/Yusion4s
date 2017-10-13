package com.yusion.shanghai.yusion4s.bean.order.submit;

import com.google.gson.Gson;

/**
 * Created by ice on 17/4/13.
 * ice is a big cow?
 */

public class SubmitOrderResp {
    public String app_id;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
