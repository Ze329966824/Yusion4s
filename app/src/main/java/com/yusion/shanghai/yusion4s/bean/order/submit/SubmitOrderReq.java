package com.yusion.shanghai.yusion4s.bean.order.submit;

import com.google.gson.Gson;

/**
 * Created by ice on 17/4/13.
 * ice is a big cow?
 */

public class SubmitOrderReq {

    /**
     * id_no : 445202199002108316
     * dlr_id : TEST0001
     * vehicle_model_id : 3
     * vehicle_color : 白色
     * vehicle_price : 150000
     * vehicle_down_payment : 80000
     * vehicle_loan_amt : 70000
     * vehicle_cond : 新车
     * management_fee : 5600
     * other_fee : 400
     * gps_fee : 500
     * plate_reg_addr : 上海
     * nper : 24
     * loan_bank : 工商银行        bank_id 银行名字
     * product_type : 予见I型     product_id 产品 名字
     */
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

    @Override
    public String toString() {

        return new Gson().toJson(this);
    }
}
