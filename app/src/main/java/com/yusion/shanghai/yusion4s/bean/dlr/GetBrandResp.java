package com.yusion.shanghai.yusion4s.bean.dlr;

import com.google.gson.Gson;
import com.yusion.shanghai.yusion4s.car_select.IndexBar.bean.BaseIndexPinyinBean;

/**
 * Created by ice on 2017/7/28.
 */

public class GetBrandResp extends BaseIndexPinyinBean {

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
    public String brand_image_url;
    public String che_300_id;
    public boolean has_select_by_user;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    @Override
    public String getTarget() {
        return brand_name;
    }

    @Override
    public boolean isNeedToPinyin() {
        return true;
    }

    @Override
    public boolean isShowSuspension() {
        return true;
    }
}
