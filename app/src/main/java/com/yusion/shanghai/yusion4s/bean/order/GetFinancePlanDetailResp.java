package com.yusion.shanghai.yusion4s.bean.order;

/**
 * Created by ice on 2017/7/28.
 */

public class GetFinancePlanDetailResp {
    /**
     * plan_id : 1
     * uw : {"vehicle_price":"232342.00","loan_amt":"23423.00","vehicle_loan_amt":"23423.00","vehicle_down_payment":"234234.00","vehicle_down_payment_percent":0.34,"monthly_payment":"4444.00","nper":12,"management_fee":"23422.00","other_fee":"444.00","loan_bank":"测试"}
     * app : {"vehicle_price":"80000.00","loan_amt":"13300.00","vehicle_loan_amt":"10000.00","vehicle_down_payment":"70000.00","vehicle_down_payment_percent":0.875,"monthly_payment":"557.00","nper":24,"management_fee":"300.00","other_fee":"3000.00","loan_bank":"测试"}
     */

    private int plan_id;
    private UwBean uw;
    private AppBean app;

    public int getPlan_id() {
        return plan_id;
    }

    public void setPlan_id(int plan_id) {
        this.plan_id = plan_id;
    }

    public UwBean getUw() {
        return uw;
    }

    public void setUw(UwBean uw) {
        this.uw = uw;
    }

    public AppBean getApp() {
        return app;
    }

    public void setApp(AppBean app) {
        this.app = app;
    }

    public static class UwBean {
        /**
         * vehicle_price : 232342.00
         * loan_amt : 23423.00
         * vehicle_loan_amt : 23423.00
         * vehicle_down_payment : 234234.00
         * vehicle_down_payment_percent : 0.34
         * monthly_payment : 4444.00
         * nper : 12
         * management_fee : 23422.00
         * other_fee : 444.00
         * loan_bank : 测试
         */

        private String vehicle_price;//开票价
        private String loan_amt;//车辆总贷款额
        private String vehicle_loan_amt;//贷款额
        private String vehicle_down_payment;//车辆首付款
        private double vehicle_down_payment_percent; //首付比例
        private String monthly_payment;
        private int nper;//还款期限
        private String management_fee;//管理费
        private String other_fee;//其他费用
        private String loan_bank; //贷款银行
        private String product_type;//产品类型

        public String getProduct_type() {
            return product_type;
        }

        public void setProduct_type(String product_type) {
            this.product_type = product_type;
        }

        public String getVehicle_price() {


            return vehicle_price;
        }

        public void setVehicle_price(String vehicle_price) {
            this.vehicle_price = vehicle_price;
        }

        public String getLoan_amt() {
            return loan_amt;
        }

        public void setLoan_amt(String loan_amt) {
            this.loan_amt = loan_amt;
        }

        public String getVehicle_loan_amt() {
            return vehicle_loan_amt;
        }

        public void setVehicle_loan_amt(String vehicle_loan_amt) {
            this.vehicle_loan_amt = vehicle_loan_amt;
        }

        public String getVehicle_down_payment() {
            return vehicle_down_payment;
        }

        public void setVehicle_down_payment(String vehicle_down_payment) {
            this.vehicle_down_payment = vehicle_down_payment;
        }

        public double getVehicle_down_payment_percent() {
            return vehicle_down_payment_percent;
        }

        public void setVehicle_down_payment_percent(double vehicle_down_payment_percent) {
            this.vehicle_down_payment_percent = vehicle_down_payment_percent;
        }

        public String getMonthly_payment() {
            return monthly_payment;
        }

        public void setMonthly_payment(String monthly_payment) {
            this.monthly_payment = monthly_payment;
        }

        public int getNper() {
            return nper;
        }

        public void setNper(int nper) {
            this.nper = nper;
        }

        public String getManagement_fee() {
            return management_fee;
        }

        public void setManagement_fee(String management_fee) {
            this.management_fee = management_fee;
        }

        public String getOther_fee() {
            return other_fee;
        }

        public void setOther_fee(String other_fee) {
            this.other_fee = other_fee;
        }

        public String getLoan_bank() {
            return loan_bank;
        }

        public void setLoan_bank(String loan_bank) {
            this.loan_bank = loan_bank;
        }
    }

    public static class AppBean {
        /**
         * vehicle_price : 80000.00
         * loan_amt : 13300.00
         * vehicle_loan_amt : 10000.00
         * vehicle_down_payment : 70000.00
         * vehicle_down_payment_percent : 0.875
         * monthly_payment : 557.00
         * nper : 24
         * management_fee : 300.00
         * other_fee : 3000.00
         * loan_bank : 测试
         */

        private String vehicle_price;
        private String loan_amt;
        private String vehicle_loan_amt;
        private String vehicle_down_payment;
        private double vehicle_down_payment_percent;
        private String monthly_payment;
        private int nper;
        private String management_fee;
        private String other_fee;
        private String loan_bank;
        private String product_type;

        public String getProduct_type() {
            return product_type;
        }

        public void setProduct_type(String product_type) {
            this.product_type = product_type;
        }

        public String getVehicle_price() {
            return vehicle_price;
        }

        public void setVehicle_price(String vehicle_price) {
            this.vehicle_price = vehicle_price;
        }

        public String getLoan_amt() {
            return loan_amt;
        }

        public void setLoan_amt(String loan_amt) {
            this.loan_amt = loan_amt;
        }

        public String getVehicle_loan_amt() {
            return vehicle_loan_amt;
        }

        public void setVehicle_loan_amt(String vehicle_loan_amt) {
            this.vehicle_loan_amt = vehicle_loan_amt;
        }

        public String getVehicle_down_payment() {
            return vehicle_down_payment;
        }

        public void setVehicle_down_payment(String vehicle_down_payment) {
            this.vehicle_down_payment = vehicle_down_payment;
        }

        public double getVehicle_down_payment_percent() {
            return vehicle_down_payment_percent;
        }

        public void setVehicle_down_payment_percent(double vehicle_down_payment_percent) {
            this.vehicle_down_payment_percent = vehicle_down_payment_percent;
        }

        public String getMonthly_payment() {
            return monthly_payment;
        }

        public void setMonthly_payment(String monthly_payment) {
            this.monthly_payment = monthly_payment;
        }

        public int getNper() {
            return nper;
        }

        public void setNper(int nper) {
            this.nper = nper;
        }

        public String getManagement_fee() {
            return management_fee;
        }

        public void setManagement_fee(String management_fee) {
            this.management_fee = management_fee;
        }

        public String getOther_fee() {
            return other_fee;
        }

        public void setOther_fee(String other_fee) {
            this.other_fee = other_fee;
        }

        public String getLoan_bank() {
            return loan_bank;
        }

        public void setLoan_bank(String loan_bank) {
            this.loan_bank = loan_bank;
        }
    }

    /**
     * vehicle_price : 88888.00
     * loan_amt : 8888.00
     * vehicle_loan_amt : 888.00
     * vehicle_down_payment : 88.00
     * vehicle_down_payment_percent : 8
     * monthly_payment : null
     * nper : null
     * management_fee : 222.00
     * other_fee : 333.00
     * plan_id : 10000300
     * loan_bank : 台州工行
     */
    /*
    "uw": { 批复
        "vehicle_price": "232342.00",
                "loan_amt": "23423.00",
                "vehicle_loan_amt": "23423.00",
                "vehicle_down_payment": "234234.00",
                "vehicle_down_payment_percent": 0.34,
                "monthly_payment": "4444.00",
                "nper": 12,
                "management_fee": "23422.00",
                "other_fee": "444.00",
                "loan_bank": "测试"
    },
            "app": {  申请
        "vehicle_price": "80000.00",
                "loan_amt": "13300.00",
                "vehicle_loan_amt": "10000.00",
                "vehicle_down_payment": "70000.00",
                "vehicle_down_payment_percent": 0.875,
                "monthly_payment": "557.00",
                "nper": 24,
                "management_fee": "300.00",
                "other_fee": "3000.00",
                "loan_bank": "测试"
    }
    */

//    public String vehicle_price;
//    public String loan_amt;
//    public String vehicle_loan_amt;
//    public String vehicle_down_payment;
//    public float vehicle_down_payment_percent;
//    public Object monthly_payment;
//    public String nper;
//    public String management_fee;
//    public String other_fee;
//    public int plan_id;
//    public String loan_bank;


}
