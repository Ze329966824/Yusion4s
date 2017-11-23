package com.yusion.shanghai.yusion4s.bean.dlr;

import com.google.gson.Gson;

/**
 * Created by aa on 2017/10/23.
 */

public class GetRawCarInfoResp {

    //     * brand : 别克
//     * trix : 君威
//     * model_name : 2002款 君威 2.5L 自动 豪华版
//     * msrp : 195799.00
//            * dlr_nm : 上海予见信息科技有限公司
//     * clt_nm : 薄锐
//    * gps_fee : 500
//            * plate_reg_addr : 上海
//     * nper : 24
//            * loan_bank : 工商银行        bank_id 银行名字
//     * product_type : 予见I型     product_id 产品 名字
//     */
    public String id_no;
    public String vehicle_owner_lender_relation;
    public String gps_fee = "0";
    public String vehicle_cond;
    public String clt_id;
    public String client_id;
    public String app_id;
    public String msrp;
    public String dlr_id;
    public String dlr;
    public String dlr_nm;
    public String brand;
    public String trix;
    public String model_name;
    public String vehicle_color;
    public String guide_price;
    public String vehicle_price;//开票价
    public String loan_amt;//车辆总贷款额
    public String vehicle_loan_amt;//贷款额
    public String vehicle_down_payment;//车辆首付款
    public String nper;//还款期限
    public String management_fee;//管理费
    public String other_fee;//其他费用
    public String loan_bank; //贷款银行
    public String product_name;//产品类型
    public String plate_reg_addr;
    public String brand_id;
    public String bank_id;
    public int product_id;
    public int vehicle_model_id;
    public String reason;
    public String trix_id;

    public String origin_plate_reg_addr;// "二手车上牌地"
    public String send_hand_plate_time;// '二手车上牌时间'
    public String send_hand_mileage;//"里程数"
    public String send_hand_valuation;//"二手车评估价"
    public String max_reg_year;
    public String min_reg_year;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
