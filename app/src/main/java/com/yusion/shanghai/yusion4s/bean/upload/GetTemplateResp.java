package com.yusion.shanghai.yusion4s.bean.upload;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ice on 2017/10/23.
 */

public class GetTemplateResp {
    /**
     * id : 1
     * role : lender
     * created_time : 2017-10-23T11:38:07.934371
     * update_time : 2017-10-23T11:38:07.934393
     * plan_item : {"id":1,"created_time":"2017-10-23T11:38:07.911982","update_time":"2017-10-23T11:38:07.912003","dealer":null,"bank":null,"plan_item":1,"who_can_approval":[]}
     * checker_item_ : {"id":1,"label":"auth_credit","name":"征信授权书","ftype":"image","business_belong":"both","description":"<h1>请注意上传事项<\/h1>","sample_url":"http://p4.gexing.com/G1/M00/21/17/rBACFFLAPsviekXAAAI2l3em1Pg505.jpg","is_base_info":true,"is_necessary":true,"check_comments":"<li>检查项目一<\/li>","created_time":"2017-10-23T11:38:07.851967","update_time":"2017-10-23T11:38:07.852011","who_can_approval":[]}
     */

    public int id;
    public String role;
    public String created_time;
    public String update_time;
    public CheckerItemBean checker_item_;


    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static class CheckerItemBean {
        /**
         * id : 1
         * label : auth_credit
         * name : 征信授权书
         * ftype : image
         * business_belong : both
         * description : <h1>请注意上传事项</h1>
         * sample_url : http://p4.gexing.com/G1/M00/21/17/rBACFFLAPsviekXAAAI2l3em1Pg505.jpg
         * is_base_info : true
         * is_necessary : true
         * check_comments : <li>检查项目一</li>
         * created_time : 2017-10-23T11:38:07.851967
         * update_time : 2017-10-23T11:38:07.852011
         * who_can_approval : []
         */

        public int id;
        public String label;
        public String name;
        public String ftype;
        public String business_belong;
        public String description;
        public String sample_url;
        public String detail_url;
        public ArrayList<String> url_list;
        public boolean is_base_info;
        public boolean is_necessary;
        public String check_comments;
        public String created_time;
        public String update_time;
        public List<?> who_can_approval;

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }
}
