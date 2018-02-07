package com.yusion.shanghai.yusion4s.bean.order.submit;

import com.google.gson.Gson;

/**
 * Created by aa on 2018/2/6.
 */

public class CheckAmountReq {


    public int vehicle_model_id;
    public String vehicle_price;
    public String vehicle_down_payment;
    public String vehicle_loan_amt;
    public String loan_amt;
    public String vehicle_cond;
    public String plate_reg_addr;

    public String send_hand_valuation;//"二手车评估价"

    public String bank_id;
    public int product_id;


    @Override
    public String toString() {

        return new Gson().toJson(this);
    }
}
