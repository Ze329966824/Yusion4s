package com.yusion.shanghai.yusion4s.bean.order;

import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by ice on 2017/7/26.
 */

public class SearchClientResp {

    /**
     * "clt_id": "2ce3b4ee7be711e7a7560242ac110003",
     * "clt_nm": "李拓",
     * "mobile": "176****6549",
     * "id_no": "412***********6013"
     */
    //public boolean exsits
    public String id_no_r;
    public String clt_id;
    public String clt_nm;
    public String mobile;
    public String id_no;
    public auth_credit auth_credit;

    public static class auth_credit {
        public String lender_sp;
        public String lender;
        public String guarantor;

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }

    @Override
    public String toString() {
//        return "SearchClientResp{" +
//                "clt_id='" + clt_id + '\'' +
//                ", clt_nm='" + clt_nm + '\'' +
//                ", mobile=" + mobile +
//                ", id_no=" + id_no +
//                ", auth_credit=" + auth_credit +
//                '}';
        return new Gson().toJson(this);
    }
}
