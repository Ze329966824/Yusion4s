package com.yusion.shanghai.yusion4s.bean.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/9.
 */

public class ConfigResp {
    public String agreement_url = "";
    public List<String> loan_periods_key = new ArrayList<>();
    public List<String> loan_periods_value = new ArrayList<>();
    public List<String> label_list_key = new ArrayList<>();
    public List<String> label_list_value = new ArrayList<>();
    public String dealer_material = "";
}