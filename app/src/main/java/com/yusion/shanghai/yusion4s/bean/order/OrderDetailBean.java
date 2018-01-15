package com.yusion.shanghai.yusion4s.bean.order;


import com.google.gson.Gson;

/**
 * Description :订单详情类
 * Author :suijin
 * Date   :17/03/30
 */

public class OrderDetailBean {


    /**
     * is_modify : true
     * old_app : {"brand":"奥迪","trix":"奥迪A4L","model_name":"2017款 奥迪A4L Plus 45 TFSI quattro 风尚型","msrp":"414000.00","dlr_nm":"测试经销商","id_no":"510108199411260323","vehicle_color":"黑色","plate_reg_addr":"河南省/郑州市/金水区","clt_nm":"欧阳沁欣","mobile":"15982172250","status":"APPLICATION_UW_PENDING","loan_bank":"中国工商银行股份有限公司台州路桥支行","product_type":"予见II型","vehicle_cond":"新车","vehicle_price":"400000.00","vehicle_down_payment":"250000.00","loan_amt":"155000.00","nper":24,"vehicle_down_payment_percent":0.625,"vehicle_loan_amt":"150000.00","monthly_payment":"7367.00","management_fee":"0.00","other_fee":"5000.00","gps_fee":"0.00","status_code":"待审核","status_st":2,"dlr_sales_name":"测试","dlr_sales_mobile":"13838384438","dlr_dfim_name":"测试","dlr_dfim_mobile":"13838384438","app_id":"11000105"}
     * new_app : {"brand":"奥迪","trix":"奥迪A4L","model_name":"2017款 奥迪A4L Plus 45 TFSI quattro 风尚型","msrp":"414000.00","dlr_nm":"测试经销商","id_no":"510108199411260323","vehicle_color":"黑色","plate_reg_addr":"河南省/郑州市/金水区","clt_nm":"欧阳沁欣","mobile":"15982172250","status":"APPLICATION_SUBMIT_CANCELED","loan_bank":"中国工商银行股份有限公司台州路桥支行","product_type":"予见II型","vehicle_cond":"新车","vehicle_price":"400000.00","vehicle_down_payment":"250000.00","loan_amt":"155000.00","nper":24,"vehicle_down_payment_percent":0.625,"vehicle_loan_amt":"150000.00","monthly_payment":"7367.00","management_fee":"0.00","other_fee":"5000.00","gps_fee":"0.00","status_code":"已取消","status_st":9,"dlr_sales_name":"欧阳沁欣","dlr_sales_mobile":"15982172250","dlr_dfim_name":"欧阳沁欣","dlr_dfim_mobile":"15982172250","app_id":"11000105"}
     * uw : true
     * uw_detail : {"vehicle_price":"400000.00","vehicle_down_payment":"250000.00","loan_amt":"155000.00","nper":24,"vehicle_down_payment_percent":0.625,"vehicle_loan_amt":"150000.00","monthly_payment":"7367.00","management_fee":"0.00","other_fee":"5000.00","uw_result_code":"P","comments":null,"uw_result":"审核通过","loan_bank":"中国工商银行股份有限公司台州路桥支行","product_type":"予见II型"}
     * origin_app : {"brand":"奥迪","trix":"奥迪A4L","model_name":"2017款 奥迪A4L Plus 45 TFSI quattro 风尚型","msrp":"414000.00","dlr_nm":"测试经销商","id_no":"510108199411260323","vehicle_color":"黑色","plate_reg_addr":"河南省/郑州市/金水区","clt_nm":"欧阳沁欣","mobile":"15982172250","status":"APPLICATION_SUBMIT_CANCELED","loan_bank":"中国工商银行股份有限公司台州路桥支行","product_type":"予见II型","vehicle_cond":"新车","vehicle_price":"400000.00","vehicle_down_payment":"250000.00","loan_amt":"155000.00","nper":24,"vehicle_down_payment_percent":0.625,"vehicle_loan_amt":"150000.00","monthly_payment":"7367.00","management_fee":"0.00","other_fee":"5000.00","gps_fee":"0.00","status_code":"已取消","status_st":9,"dlr_sales_name":"欧阳沁欣","dlr_sales_mobile":"15982172250","dlr_dfim_name":"欧阳沁欣","dlr_dfim_mobile":"15982172250","app_id":"11000105"}
     * brand : 奥迪
     * trix : 奥迪A4L
     * model_name : 2017款 奥迪A4L Plus 45 TFSI quattro 风尚型
     * msrp : 414000.00
     * dlr_nm : 测试经销商
     * id_no : 510108199411260323
     * vehicle_color : 黑色
     * plate_reg_addr : 河南省/郑州市/金水区
     * clt_nm : 欧阳沁欣
     * mobile : 15982172250
     * status : APPLICATION_SUBMIT_CANCELED
     * loan_bank : 中国工商银行股份有限公司台州路桥支行
     * product_type : 予见II型
     * vehicle_cond : 新车
     * vehicle_price : 400000.00
     * vehicle_down_payment : 250000.00
     * loan_amt : 155000.00
     * nper : 24
     * vehicle_down_payment_percent : 0.625
     * vehicle_loan_amt : 150000.00
     * monthly_payment : 7367.00
     * management_fee : 0.00
     * other_fee : 5000.00
     * gps_fee : 0.00
     * status_code : 已取消
     * status_st : 9
     * dlr_sales_name : 欧阳沁欣
     * dlr_sales_mobile : 15982172250
     * dlr_dfim_name : 欧阳沁欣
     * dlr_dfim_mobile : 15982172250
     * app_id : 11000105
     */
    public String aid_dlr_nm;
    public String client_status_code;
    public boolean is_modify;
    public OldAppBean old_app;
    public NewAppBean new_app;
    public boolean uw;
    public UwDetailBean uw_detail;
    public OriginAppBean origin_app;
    public String brand;
    public String trix;
    public String model_name;
    public String msrp;
    public String dlr_nm;
    public String id_no;
    public String vehicle_color;
    public String plate_reg_addr;
    public String clt_nm;
    public String mobile;
    public String status;
    public String loan_bank;
    public String product_type;
    public String vehicle_price;
    public String vehicle_down_payment;
    public String loan_amt;
    public String nper;
    public float vehicle_down_payment_percent;
    public String vehicle_loan_amt;
    public String monthly_payment;
    public String management_fee;
    public String other_fee;
    public String gps_fee;
    public String status_code;
    public int status_st;
    public String dlr_sales_name;
    public String dlr_sales_mobile;
    public String dlr_dfim_name;
    public String dlr_dfim_mobile;
    public String app_id;
    public Boolean modify_permission;
    public String vehicle_cond;//车辆类型
    public String origin_plate_reg_addr;// "二手车上牌地"
    public String send_hand_plate_time;// '二手车上牌时间'
    public String send_hand_mileage;//"里程数"
    public String send_hand_valuation;//"二手车评估价"

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static class OldAppBean {
        /**
         * brand : 奥迪
         * trix : 奥迪A4L
         * model_name : 2017款 奥迪A4L Plus 45 TFSI quattro 风尚型
         * msrp : 414000.00
         * dlr_nm : 测试经销商
         * id_no : 510108199411260323
         * vehicle_color : 黑色
         * plate_reg_addr : 河南省/郑州市/金水区
         * clt_nm : 欧阳沁欣
         * mobile : 15982172250
         * status : APPLICATION_UW_PENDING
         * loan_bank : 中国工商银行股份有限公司台州路桥支行
         * product_type : 予见II型
         * vehicle_cond : 新车
         * vehicle_price : 400000.00
         * vehicle_down_payment : 250000.00
         * loan_amt : 155000.00
         * nper : 24
         * vehicle_down_payment_percent : 0.625
         * vehicle_loan_amt : 150000.00
         * monthly_payment : 7367.00
         * management_fee : 0.00
         * other_fee : 5000.00
         * gps_fee : 0.00
         * status_code : 待审核
         * status_st : 2
         * dlr_sales_name : 测试
         * dlr_sales_mobile : 13838384438
         * dlr_dfim_name : 测试
         * dlr_dfim_mobile : 13838384438
         * app_id : 11000105
         */

        public String brand;
        public String trix;
        public String model_name;
        public String msrp;
        public String dlr_nm;
        public String id_no;
        public String vehicle_color;
        public String plate_reg_addr;
        public String clt_nm;
        public String mobile;
        public String status;
        public String loan_bank;
        public String product_type;
        public String vehicle_price;
        public String vehicle_down_payment;
        public String loan_amt;
        public String nper;
        public String vehicle_down_payment_percent;
        public String vehicle_loan_amt;
        public String monthly_payment;
        public String management_fee;
        public String other_fee;
        public String gps_fee;
        public String status_code;
        public String status_st;
        public String dlr_sales_name;
        public String dlr_sales_mobile;
        public String dlr_dfim_name;
        public String dlr_dfim_mobile;
        public String app_id;
        public String vehicle_cond;//车辆类型
        public String origin_plate_reg_addr;// "二手车上牌地"
        public String send_hand_plate_time;// '二手车上牌时间'
        public String send_hand_mileage;//"里程数"
        public String send_hand_valuation;//"二手车评估价"

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }

    public static class NewAppBean {
        /**
         * brand : 奥迪
         * trix : 奥迪A4L
         * model_name : 2017款 奥迪A4L Plus 45 TFSI quattro 风尚型
         * msrp : 414000.00
         * dlr_nm : 测试经销商
         * id_no : 510108199411260323
         * vehicle_color : 黑色
         * plate_reg_addr : 河南省/郑州市/金水区
         * clt_nm : 欧阳沁欣
         * mobile : 15982172250
         * status : APPLICATION_SUBMIT_CANCELED
         * loan_bank : 中国工商银行股份有限公司台州路桥支行
         * product_type : 予见II型
         * vehicle_cond : 新车
         * vehicle_price : 400000.00
         * vehicle_down_payment : 250000.00
         * loan_amt : 155000.00
         * nper : 24
         * vehicle_down_payment_percent : 0.625
         * vehicle_loan_amt : 150000.00
         * monthly_payment : 7367.00
         * management_fee : 0.00
         * other_fee : 5000.00
         * gps_fee : 0.00
         * status_code : 已取消
         * status_st : 9
         * dlr_sales_name : 欧阳沁欣
         * dlr_sales_mobile : 15982172250
         * dlr_dfim_name : 欧阳沁欣
         * dlr_dfim_mobile : 15982172250
         * app_id : 11000105
         */

        public String brand;
        public String trix;
        public String model_name;
        public String msrp;
        public String dlr_nm;
        public String id_no;
        public String vehicle_color;
        public String plate_reg_addr;
        public String clt_nm;
        public String mobile;
        public String status;
        public String loan_bank;
        public String product_type;
        public String vehicle_price;
        public String vehicle_down_payment;
        public String loan_amt;
        public String nper;
        public String vehicle_down_payment_percent;
        public String vehicle_loan_amt;
        public String monthly_payment;
        public String management_fee;
        public String other_fee;
        public String gps_fee;
        public String status_code;
        public String status_st;
        public String dlr_sales_name;
        public String dlr_sales_mobile;
        public String dlr_dfim_name;
        public String dlr_dfim_mobile;
        public String app_id;
        public String vehicle_cond;//车辆类型
        public String origin_plate_reg_addr;// "二手车上牌地"
        public String send_hand_plate_time;// '二手车上牌时间'
        public String send_hand_mileage;//"里程数"
        public String send_hand_valuation;//"二手车评估价"

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }

    public static class UwDetailBean {
        /**
         * vehicle_price : 400000.00
         * vehicle_down_payment : 250000.00
         * loan_amt : 155000.00
         * nper : 24
         * vehicle_down_payment_percent : 0.625
         * vehicle_loan_amt : 150000.00
         * monthly_payment : 7367.00
         * management_fee : 0.00
         * other_fee : 5000.00
         * uw_result_code : P
         * comments : null
         * uw_result : 审核通过
         * loan_bank : 中国工商银行股份有限公司台州路桥支行
         * product_type : 予见II型
         */

        public String vehicle_price;
        public String vehicle_down_payment;
        public String loan_amt;
        public String nper;
        public float vehicle_down_payment_percent;
        public String vehicle_loan_amt;
        public String monthly_payment;
        public String management_fee;
        public String other_fee;
        public String uw_result_code;
        public String comments;
        public String uw_result;
        public String loan_bank;
        public String product_type;
        public String vehicle_cond;//车辆类型
        public String origin_plate_reg_addr;// "二手车上牌地"
        public String send_hand_plate_time;// '二手车上牌时间'
        public String send_hand_mileage;//"里程数"
        public String send_hand_valuation;//"二手车评估价"

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }

    public static class OriginAppBean {
        /**
         * brand : 奥迪
         * trix : 奥迪A4L
         * model_name : 2017款 奥迪A4L Plus 45 TFSI quattro 风尚型
         * msrp : 414000.00
         * dlr_nm : 测试经销商
         * id_no : 510108199411260323
         * vehicle_color : 黑色
         * plate_reg_addr : 河南省/郑州市/金水区
         * clt_nm : 欧阳沁欣
         * mobile : 15982172250
         * status : APPLICATION_SUBMIT_CANCELED
         * loan_bank : 中国工商银行股份有限公司台州路桥支行
         * product_type : 予见II型
         * vehicle_cond : 新车
         * vehicle_price : 400000.00
         * vehicle_down_payment : 250000.00
         * loan_amt : 155000.00
         * nper : 24
         * vehicle_down_payment_percent : 0.625
         * vehicle_loan_amt : 150000.00
         * monthly_payment : 7367.00
         * management_fee : 0.00
         * other_fee : 5000.00
         * gps_fee : 0.00
         * status_code : 已取消
         * status_st : 9
         * dlr_sales_name : 欧阳沁欣
         * dlr_sales_mobile : 15982172250
         * dlr_dfim_name : 欧阳沁欣
         * dlr_dfim_mobile : 15982172250
         * app_id : 11000105
         */

        public String spouse_clt_id;
        public String brand;
        public String trix;
        public String model_name;
        public String msrp;
        public String dlr_nm;
        public String id_no;
        public String vehicle_color;
        public String plate_reg_addr;
        public String clt_nm;
        public String mobile;
        public String status;
        public String loan_bank;
        public String product_type;
        public String vehicle_price;
        public String vehicle_down_payment;
        public String loan_amt;
        public String nper;
        public String vehicle_down_payment_percent;
        public String vehicle_loan_amt;
        public String monthly_payment;
        public String management_fee;
        public String other_fee;
        public String gps_fee;
        public String status_code;
        public String status_st;
        public String dlr_sales_name;
        public String dlr_sales_mobile;
        public String dlr_dfim_name;
        public String dlr_dfim_mobile;
        public String app_id;
        public String vehicle_cond;//车辆类型
        public String origin_plate_reg_addr;// "二手车上牌地"
        public String send_hand_plate_time;// '二手车上牌时间'
        public String send_hand_mileage;//"里程数"
        public String send_hand_valuation;//"二手车评估价"

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }
}