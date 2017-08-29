package com.yusion.shanghai.yusion4s.bean.order;

import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by ice on 2017/7/26.
 */

public class SearchClientResp {
    /*
        {
            "code": 0,
                "msg": "查询成功",
                "data": [
            {
                "clt_id": "ede5314687ad11e7b0c202f1f38b2f4a",
                    "id_no": "341***********6136",
                    "clt_nm": "叶磊",
                    "mobile": "176****4799",
                    "auth_credit": {
                "lender": {
                    "clt_id": "ede5314687ad11e7b0c202f1f38b2f4a",
                            "auth_credit_img_count": 0
                },
                "guarantor": {
                    "clt_id": "cbef40dc889711e7b05502f1f38b2f4a",
                            "auth_credit_img_count": 0
                }
            }
            }
        ]
        }
        */
    // public String id_no_r;
    public String clt_id;
    public String clt_nm;
    public String mobile;
    public String id_no;
    public auth_credit auth_credit;

    public static class auth_credit {

        public lender lender;
        public guarantor guarantor;
        public lender_sp lender_sp;
        public guarantor_sp guarantor_sp;

        @Override
        public String toString() {

            return new Gson().toJson(this);
        }

    }

    public class lender {
        public String clt_id;
        public String auth_credit_img_count;

        @Override
        public String toString() {

            return new Gson().toJson(this);
        }
    }

    public class lender_sp {
        public String clt_id;
        public String auth_credit_img_count;

        @Override
        public String toString() {

            return new Gson().toJson(this);
        }
    }

    public class guarantor {
        public String clt_id;
        public String auth_credit_img_count;

        @Override
        public String toString() {

            return new Gson().toJson(this);
        }
    }

    public class guarantor_sp {
        public String clt_id;
        public String auth_credit_img_count;

        @Override
        public String toString() {

            return new Gson().toJson(this);
        }
    }


    @Override
    public String toString() {

        return new Gson().toJson(this);
    }
}
