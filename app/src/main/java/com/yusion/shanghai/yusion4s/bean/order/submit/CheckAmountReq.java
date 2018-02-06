package com.yusion.shanghai.yusion4s.bean.order.submit;

import com.google.gson.Gson;

/**
 * Created by aa on 2018/2/6.
 */

public class CheckAmountReq {

    public String imei;
    public String clt_id;
    public String id_no;
    public String dlr_id;
    public int vehicle_model_id;
    public String vehicle_color;
    public String vehicle_price;
    public String vehicle_down_payment;
    public String vehicle_loan_amt;
    public String loan_amt;
    public String vehicle_cond;
    public String management_fee;
    public String other_fee;
    public String gps_fee = "0";
    public String plate_reg_addr;
    public String nper;

    public String origin_plate_reg_addr;// "二手车上牌地"
    public String send_hand_plate_time;// '二手车上牌时间'
    public String send_hand_mileage;//"里程数"
    public String send_hand_valuation;//"二手车评估价"

    //public String loan_bank;

    public String bank_id;
    public int product_id;

    //public String product_type;
    public String vehicle_owner_lender_relation;
    public String aid_id;

    @Override
    public String toString() {

        return new Gson().toJson(this);
    }
}
