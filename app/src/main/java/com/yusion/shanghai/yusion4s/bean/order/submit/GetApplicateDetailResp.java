package com.yusion.shanghai.yusion4s.bean.order.submit;

import com.google.gson.Gson;

/**
 * Created by aa on 2017/8/24.
 * {
 * "code": 0,
 * "msg": "获取成功",
 * "data": {
 * "lender": {
 * "clt_id": "ede5314687ad11e7b0c202f1f38b2f4a",
 * "clt_nm": "叶磊",
 * "gender": "女",
 * "id_no": "341***********6136",
 * "mobile": "176****4799"
 * },
 * "guarantor": {
 * "clt_id": "cbef40dc889711e7b05502f1f38b2f4a",
 * "clt_nm": "just Test",
 * "gender": "女",
 * "id_no": "150***********",
 * "mobile": "****请****选****择****"
 * }
 * }
 * }
 */

public class GetApplicateDetailResp {

    public Lender lender;
    public Lender_sp lender_sp;
    public Guarantor guarantor;
    public Guarantor_sp guarantor_sp;

    @Override
    public String toString() {

        return new Gson().toJson(this);
    }


    public static class Lender {
        public String clt_id;
        public String clt_nm;
        public String gender;
        public String id_no;
        public String mobile;

        @Override
        public String toString() {

            return new Gson().toJson(this);
        }
    }

    public static class Lender_sp {
        public String clt_id;
        public String clt_nm;
        public String gender;
        public String id_no;
        public String mobile;

        @Override
        public String toString() {

            return new Gson().toJson(this);
        }
    }

    public static class Guarantor {
        public String clt_id;
        public String clt_nm;
        public String gender;
        public String id_no;
        public String mobile;

        @Override
        public String toString() {

            return new Gson().toJson(this);
        }
    }

    public class Guarantor_sp {
        public String clt_id;
        public String clt_nm;
        public String gender;
        public String id_no;
        public String mobile;

        @Override
        public String toString() {

            return new Gson().toJson(this);
        }
    }

}
