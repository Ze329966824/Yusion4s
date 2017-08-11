package com.yusion.shanghai.yusion4s.bean.order.submit;

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
     * loan_bank : 工商银行
     * product_type : 予见I型
     */

    public String id_no;
    public String dlr_id;
    public int vehicle_model_id;
    public String vehicle_color;
    public String vehicle_price;
    public String vehicle_down_payment;
    public String vehicle_loan_amt;
    public String loan_amt;
    public String vehicle_cond = "新车";
    public String management_fee;
    public String other_fee;
    public String gps_fee = "0";
    public String plate_reg_addr;
    public int nper;
    public String loan_bank;
    public String product_type;

    @Override
    public String toString() {
        return "SubmitOrderReq{" +
                "id_no='" + id_no + '\'' +
                ", dlr_id='" + dlr_id + '\'' +
                ", vehicle_model_id=" + vehicle_model_id +
                ", vehicle_color='" + vehicle_color + '\'' +
                ", vehicle_price='" + vehicle_price + '\'' +
                ", vehicle_down_payment='" + vehicle_down_payment + '\'' +
                ", vehicle_loan_amt='" + vehicle_loan_amt + '\'' +
                ", vehicle_cond='" + vehicle_cond + '\'' +
                ", management_fee='" + management_fee + '\'' +
                ", loan_amt='" + loan_amt + '\'' +
                ", other_fee='" + other_fee + '\'' +
                ", gps_fee='" + gps_fee + '\'' +
                ", plate_reg_addr='" + plate_reg_addr + '\'' +
                ", nper=" + nper +
                ", loan_bank='" + loan_bank + '\'' +
                ", product_type='" + product_type + '\'' +
                '}';
    }
}
