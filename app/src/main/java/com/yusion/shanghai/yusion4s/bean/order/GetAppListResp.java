package com.yusion.shanghai.yusion4s.bean.order;

/**
 * Created by ice on 2017/7/27.
 */

public class GetAppListResp {

    /**
     * vehicle_price : 150000.00
     * loan_amt : 76000.00
     * vehicle_loan_amt : 70000.00
     * vehicle_down_payment : 80000.00
     * nper : 24
     * management_fee : 5600.00
     * other_fee : 400.00
     * clt_nm : 黄耿嘉
     * mobile : 5555
     * dlr_nm : 弘高融资租赁有限公司一部江伟
     * brand : 奥迪
     * trix : 奥迪A3
     * model_name : 2015款 奥迪A3 Sportback 35 TFSI 手动进取型
     * msrp : 18.49
     * app_ts : 2017-07-27T03:48:28.371Z
     * dlr_sales_nm : 魏佳伟
     * dlr_sales_mobile : 17621066549
     * dlr_dfim_nm : null
     * dlr_dfim_mobile : null
     */

    public String vehicle_price;
    public String loan_amt;
    public String vehicle_loan_amt;
    public String vehicle_down_payment;
    public String nper;
    public String management_fee;
    public String other_fee;
    public String clt_nm;
    public String mobile;
    public String dlr_nm;
    public String brand;
    public String trix;
    public String model_name;
    public String msrp;
    public String status;
    public String app_ts;
    public String dlr_sales_nm;
    public String dlr_sales_mobile;
    public Object dlr_dfim_nm;
    public Object dlr_dfim_mobile;
    public String app_id;

    public String status_code;
    public int status_st;

    public Boolean modify_permission = true;

    public Boolean modify_permission;

    @Override
    public String toString() {
        return "GetAppListResp{" +
                "vehicle_price='" + vehicle_price + '\'' +
                ", loan_amt='" + loan_amt + '\'' +
                ", vehicle_loan_amt='" + vehicle_loan_amt + '\'' +
                ", vehicle_down_payment='" + vehicle_down_payment + '\'' +
                ", nper='" + nper + '\'' +
                ", management_fee='" + management_fee + '\'' +
                ", other_fee='" + other_fee + '\'' +
                ", clt_nm='" + clt_nm + '\'' +
                ", mobile='" + mobile + '\'' +
                ", dlr_nm='" + dlr_nm + '\'' +
                ", brand='" + brand + '\'' +
                ", trix='" + trix + '\'' +
                ", model_name='" + model_name + '\'' +
                ", msrp='" + msrp + '\'' +
                ", status='" + status + '\'' +
                ", app_ts='" + app_ts + '\'' +
                ", dlr_sales_nm='" + dlr_sales_nm + '\'' +
                ", dlr_sales_mobile='" + dlr_sales_mobile + '\'' +
                ", dlr_dfim_nm=" + dlr_dfim_nm +
                ", dlr_dfim_mobile=" + dlr_dfim_mobile +
                ", app_id='" + app_id + '\'' +
                ", status_code='" + status_code + '\'' +
                ", status_st=" + status_st +
                '}';
    }
}
