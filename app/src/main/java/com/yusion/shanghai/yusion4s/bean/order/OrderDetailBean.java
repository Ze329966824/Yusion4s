package com.yusion.shanghai.yusion4s.bean.order;


/**
 * Description :订单详情类
 * Author :suijin
 * Date   :17/03/30
 */

public class OrderDetailBean {

    /**
     * uw : true
     * brand : 别克
     * trix : 君威
     * model_name : 2002款 君威 2.5L 自动 豪华版
     * msrp : 195799.00
     * dlr_nm : 上海予见信息科技有限公司
     * clt_nm : 薄锐
     * mobile : 18642086398
     * status : 待签约
     * vehicle_price : 180000.00
     * vehicle_down_payment : 100000.00
     * loan_amt : 83300.00
     * nper : 36
     * vehicle_down_payment_percent : 0.5556
     * vehicle_loan_amt : 80000.00
     * management_fee : 300.00
     * other_fee : 3000.00
     * uw_detail : {"vehicle_price":"8888.00","vehicle_down_payment":"2222.00","loan_amt":"2222.00","nper":null,"vehicle_down_payment_percent":100,"vehicle_loan_amt":"2222.00","management_fee":"6000.00","other_fee":"500.00"}
     * dlr_sales_name : wjw
     * dlr_sales_mobile : 17621066549
     * dlr_dfim_name : null
     * dlr_dfim_mobile : null
     * app_id : 10000300
     */

    public boolean uw;
    public String brand;
    public String trix;
    public String model_name;
    public String msrp;
    public String dlr_nm;
    public String clt_nm;
    public String mobile;
    public String status;
    public String vehicle_price;
    public String vehicle_down_payment;
    public String loan_amt;
    public String nper;
    public float vehicle_down_payment_percent;
    public String vehicle_loan_amt;
    public String management_fee;
    public String other_fee;
    public UwDetailBean uw_detail;
    public String dlr_sales_name;
    public String dlr_sales_mobile;
    public Object dlr_dfim_name;
    public Object dlr_dfim_mobile;
    public String app_id;
    public String loan_bank;
    public String product_type;
    public String id_no;

    public static class UwDetailBean {
        /**
         * vehicle_price : 8888.00
         * vehicle_down_payment : 2222.00
         * loan_amt : 2222.00
         * nper : null
         * vehicle_down_payment_percent : 100
         * vehicle_loan_amt : 2222.00
         * management_fee : 6000.00
         * other_fee : 500.00
         */

        public String vehicle_price;
        public String vehicle_down_payment;
        public String loan_amt;
        public String nper;
        public float vehicle_down_payment_percent;
        public String vehicle_loan_amt;
        public String management_fee;
        public String other_fee;
    }
}