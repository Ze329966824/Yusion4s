package com.yusion.shanghai.yusion4s.bean.dlr;

import com.yusion.shanghai.yusion4s.car_select.suspension.ISuspensionInterface;

import java.io.Serializable;

/**
 * Created by ice on 2017/7/26.
 */

public class GetModelResp implements ISuspensionInterface,Serializable {

    /**
     * model_name : 2009款 RAV4 2.0L 经典版 MT
     * trix : 308
     * msrp : 189800
     * eng_cap : 2.0
     * seat_number : 5
     * model_id : 5998
     */

    public String model_name;
    public int trix;
    public float msrp;
    public String eng_cap;
    public String seat_number;
    public int model_id;
    public String min_reg_year;
    public String max_reg_year;
    public String group_name;

    public String che_300_id;

    @Override
    public boolean isShowSuspension() {
        return true;
    }

    @Override
    public String getSuspensionTag() {
        return group_name;
    }
}
