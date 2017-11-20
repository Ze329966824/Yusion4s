package com.yusion.shanghai.yusion4s.bean.order;

import com.google.gson.Gson;

/**
 * Created by LX on 2017/11/17.
 */

public class DlrNumResp {

    /**
     * reject_count : 0
     * dealing_count : 5
     * to_be_confirm_count : 2
     * to_be_upload_count : 0
     * to_loan_count : 1
     * today_count : 0
     * all_count : 6
     */

  public String reject_count;
  public String dealing_count;
  public String to_be_confirm_count;
  public String to_be_upload_count;
  public String to_loan_count;
  public String today_count;
  public String all_count;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
