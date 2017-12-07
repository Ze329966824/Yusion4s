package com.yusion.shanghai.yusion4s.bean.dlr;

import com.google.gson.Gson;
import com.yusion.shanghai.yusion4s.car_select.suspension.ISuspensionInterface;

/**
 * Created by ice on 2017/7/28.
 */

public class GetTrixResp implements ISuspensionInterface {
//public class GetTrixResp {
    /**
     * superior : 307
     * level : 1
     * trix_name : 卡罗拉
     * trix_code : null
     * trix_id : 309
     */

    public int superior;
    public String level;
    public String trix_name;
    public Object trix_code;
    public String trix_id;
    public String che_300_id;
    public boolean has_header;
    public String header_name;
    public String series_group_name;
    public boolean has_select_by_user;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    @Override
    public boolean isShowSuspension() {
        return true;
    }

    @Override
    public String getSuspensionTag() {
        return series_group_name;
    }
}
