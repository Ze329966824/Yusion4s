package com.yusion.shanghai.yusion4s.bean.dlr;

import com.google.gson.Gson;
import com.yusion.shanghai.yusion4s.car_select.IndexBar.bean.BaseIndexPinyinBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by aa on 2017/8/9.
 */

public class GetDlrListByTokenResp extends BaseIndexPinyinBean implements Serializable {
    /**
     * dlr_id : TEST0001
     * dlr_nm : 弘高融资租赁有限公司一部江伟
     * dtype : D
     * management_fee : [0,"300.00"]
     * other_fee : 3000.00
     */
    public String id;
    public String dlr_id;
    public String dlr_nm;
    public String dtype;
    public int other_fee;
    public List<Integer> management_fee;
    public boolean has_select_by_user;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }


    @Override
    public String getTarget() {
        return dlr_nm;
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
