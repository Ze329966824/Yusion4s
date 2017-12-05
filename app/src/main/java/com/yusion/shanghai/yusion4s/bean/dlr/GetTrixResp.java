package com.yusion.shanghai.yusion4s.bean.dlr;

import com.yusion.shanghai.yusion4s.car_select.IndexBar.bean.BaseIndexPinyinBean;

/**
 * Created by ice on 2017/7/28.
 */

public class GetTrixResp extends BaseIndexPinyinBean {


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

    @Override
    public String getTarget() {
        return trix_name;
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
