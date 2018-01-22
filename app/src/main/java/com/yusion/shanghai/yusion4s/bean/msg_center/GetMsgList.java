package com.yusion.shanghai.yusion4s.bean.msg_center;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by ice on 2018/1/9.
 */

public class GetMsgList {
    /**
     * total_page : 1
     * new_msg_count : 1
     * msg_list : [{"app_id":"10000200","summary":"审批通过","detail":{"clt_info":"客户姓名 手机号","dealer":"经销商名称","vehicle":"车型信息","loan_amt":"234533.00"},"color":"#666666"}]
     */

    public int total_page;
    public int new_msg_count;
    public List<MsgListBean> msg_list;

    public static class MsgListBean {
        /**
         * app_id : 10000200
         * summary : 审批通过
         * detail : {"clt_info":"客户姓名 手机号","dealer":"经销商名称","vehicle":"车型信息","loan_amt":"234533.00"}
         * color : #666666
         */

        public String app_id;
        public String summary;
        public DetailBean detail;
        public String color;
        public String app_up_ts;
        public long msg_ts;


        public static class DetailBean {
            /**
             * clt_info : 客户姓名 手机号
             * dealer : 经销商名称
             * vehicle : 车型信息
             * loan_amt : 234533.00
             */

            public String clt_info;
            public String dealer;
            public String vehicle;
            public String loan_amt;
        }
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
