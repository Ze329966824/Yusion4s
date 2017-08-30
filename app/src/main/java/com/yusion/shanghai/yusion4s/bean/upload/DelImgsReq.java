package com.yusion.shanghai.yusion4s.bean.upload;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ice on 2017/7/19.
 */

public class DelImgsReq {
    public String clt_id;
    public String app_id;
    public List<String> id = new ArrayList<>();

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
