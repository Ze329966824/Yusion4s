package com.yusion.shanghai.yusion4s.retrofit.service;

import com.yusion.shanghai.yusion4s.base.BaseResult;
import com.yusion.shanghai.yusion4s.bean.dlr.GetBrandResp;
import com.yusion.shanghai.yusion4s.bean.dlr.GetDlrListByTokenResp;
import com.yusion.shanghai.yusion4s.bean.dlr.GetLoanBankResp;
import com.yusion.shanghai.yusion4s.bean.dlr.GetModelResp;
import com.yusion.shanghai.yusion4s.bean.dlr.GetTrixResp;
import com.yusion.shanghai.yusion4s.bean.dlr.GetproductResp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by aa on 2017/8/9.
 */

public interface DlrService {

    @GET("/api/crm/dealer/get_dealer_list/")
    Call<BaseResult<List<GetDlrListByTokenResp>>> getDlrListByToken();


    @GET("/api/crm/dealer/get_vehicle_brand_list/")
    Call<BaseResult<List<GetBrandResp>>> getBrand(@Query("dlr_id") String dlr_id);

    //获取车系
    @GET("/api/crm/dealer/get_vehicle_trix_list/")
    Call<BaseResult<List<GetTrixResp>>> getTrix(@Query("brand_id") String brand);

    //贷款银行
    @GET("/api/crm/dealer/get_loan_bank_by_dlr_id/")
    Call<BaseResult<List<GetLoanBankResp>>> getLoanBank(@Query("dlr_id") String dlr_id);

    //获取产品类型
    @GET("/api/crm/dealer/get_product_type/")
    Call<BaseResult<GetproductResp>> getProductType(@Query("bank_id") String bank_id, @Query("dlr_id") String dlr_id);//bank_name 改bank_id

    //获取车型
    @GET("/api/crm/dealer/get_vehicle_model_list/")
    Call<BaseResult<List<GetModelResp>>> getModel(@Query("trix_id") String trix);
}
