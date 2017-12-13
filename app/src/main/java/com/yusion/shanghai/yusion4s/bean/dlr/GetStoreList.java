package com.yusion.shanghai.yusion4s.bean.dlr;

import com.google.gson.Gson;
import com.yusion.shanghai.yusion4s.car_select.suspension.ISuspensionInterface;

import java.io.Serializable;

/**
 * Created by aa on 2017/12/12.
 */

public class GetStoreList implements ISuspensionInterface, Serializable {

    public String dlr_id;
    public String dlr_nm;
    public String dtype;
    public int other_fee;
    public String id;
    public String group_name;
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
        return group_name;
    }
}
