package com.yusion.shanghai.yusion4s.bean.auth;

import com.google.gson.Gson;

/**
 * Created by LX on 2017/12/15.
 */

public class CheckInfoCompletedResp {

    public Boolean info_completed;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
