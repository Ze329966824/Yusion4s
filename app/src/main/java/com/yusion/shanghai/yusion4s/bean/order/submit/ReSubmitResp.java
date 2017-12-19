package com.yusion.shanghai.yusion4s.bean.order.submit;

import com.google.gson.Gson;

/**
 * Created by LX on 2017/12/18.
 */

public class ReSubmitResp {
    public String app_id;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
