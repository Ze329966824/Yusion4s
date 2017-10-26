package com.yusion.shanghai.yusion4s.bean.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/9.
 */

public class ConfigResp {
    public String agreement_url = "";
    public String contract_list_url = "";
    public int DELAY_MILLIS = 0;
    //    public List<String> loan_periods_key = new ArrayList<>();
//    public List<String> loan_periods_value = new ArrayList<>();
    public List<String> label_list_key = new ArrayList<>();
    public List<String> label_list_value = new ArrayList<>();

    public List<String> owner_applicant_relation_key = new ArrayList<>();
    public List<String> owner_applicant_relation_value = new ArrayList<>();
    public List<String> carInfo_alter_key = new ArrayList<>();
    public List<String> carInfo_alter_value = new ArrayList<>();

    public String dealer_material = "";
}
