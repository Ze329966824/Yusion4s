package com.yusion.shanghai.yusion4s.ubt.sql;

import com.google.gson.Gson;

/**
 * Created by ice on 2017/9/15.
 */
public class UBTEvent {
    //eg:AppCompatTextView
    public String object;
    //eg:onClick onResume
    public String action;
    public long ts;
    public String page; //eg:LoginActivity
    public String page_cn;
    public String action_value;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
