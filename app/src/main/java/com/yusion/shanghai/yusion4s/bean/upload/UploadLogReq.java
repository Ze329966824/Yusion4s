package com.yusion.shanghai.yusion4s.bean.upload;

import com.google.gson.Gson;


/**
 * Created by aa on 2017/9/14.
 */

public class UploadLogReq {

    public String app_id;
    public String file_label;
    public String file_name;
    public String file_value;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
