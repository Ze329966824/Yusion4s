package com.yusion.shanghai.yusion4s.bean.upload;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ice on 2017/8/31.
 */

public class ListDealerLabelsResp implements Serializable{

    /**
     * name : 征信授权书上传
     * value :
     * img_list : []
     * label_list : [{"role":"client","name":"申请人征信授权书","clt_id":"65733460861e11e7bf250242ac110006","label_list":["auth_credit"],"has_img":1,"has_error":0}]
     */

    public String name;
    public String value;
    public List<LabelListBean> label_list;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static class LabelListBean implements Serializable{
        /**
         * role : client
         * name : 申请人征信授权书
         * clt_id : 65733460861e11e7bf250242ac110006
         * label_list : ["auth_credit"]
         * has_img : 1
         * has_error : 0
         */

        public String role;
        public String id;
        public String name;
        public String ftype;
        public String value;
        public String clt_id;
        public int has_img;
        public int has_error;
        public List<String> label_list;
        public boolean has_change;

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }
}
