package com.yusion.shanghai.yusion4s.bean.upload;

import java.util.List;

/**
 * Created by ice on 2017/7/31.
 */

public class ListLabelsErrorReq {


    /**
     * clt_id : 62c6b748736611e78ead0242ac110004
     * label_list : ["id_card_back"]
     */

    public String clt_id;
    public String app_id;
    public List<String> label_list;

    @Override
    public String toString() {
        return "ListLabelsErrorReq{" +
                "clt_id='" + clt_id + '\'' +
                "app_id='" + app_id + '\'' +
                ", label_list=" + label_list +
                '}';
    }
}
