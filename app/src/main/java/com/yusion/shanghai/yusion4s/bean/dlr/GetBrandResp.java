package com.yusion.shanghai.yusion4s.bean.dlr;

import com.google.gson.Gson;

/**
 * Created by ice on 2017/7/28.
 */

public class GetBrandResp {

    /**
     * superior : null
     * brand_name : 丰田
     * brand_code : 6477
     * level : 0
     * brand_id : 307
     */

    public Object superior;
    public String brand_name;
    public String brand_code;
    public String level;
    public String brand_id;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
