package com.yusion.shanghai.yusion4s.bean.order.submit;

import com.google.gson.Gson;

/**
 * Created by aa on 2017/11/24.
 */

public class GetCheUrlResp {
    public String url;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
