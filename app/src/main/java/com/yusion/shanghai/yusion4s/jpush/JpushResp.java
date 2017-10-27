package com.yusion.shanghai.yusion4s.jpush;

import com.google.gson.Gson;

/**
 * Created by LX on 2017/10/13.
 */

public class JpushResp {


    public String mobilel;
    public String title;
    public String content;
    public String app_st;
    public String app_id;
    public String category;


    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
