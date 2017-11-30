package com.yusion.shanghai.yusion4s.bean.auth;

import com.google.gson.Gson;

/**
 * Created by LX on 2017/11/28.
 */

public class Check3ElementsResp {

    /**
     * match : 1
     */

    public String match;


    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
