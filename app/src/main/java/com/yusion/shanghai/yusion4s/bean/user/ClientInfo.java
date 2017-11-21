package com.yusion.shanghai.yusion4s.bean.user;

import com.google.gson.Gson;

/**
 * Created by ice on 2017/8/22.
 */

public class ClientInfo {

//
//    {
//        "clt_id":"5be9490a871f11e7b7f902f1f38b2f4a",
//            "clt_nm":"xx",
//            "commited":"1",
//            "created_time":"2017-08-24 15:24:57",
//            "current_addr":{
//        "address1":"玄武湖(东北门)",
//                "address2":"kkk",
//                "city":"南京市",
//                "created_time":"2017-08-23T16:27:23.810",
//                "district":"玄武区",
//                "province":"江苏省",
//                "update_time":"2017-08-23T16:27:23.810"
//    },
//        "drv_lic_relationship":"本人",
//            "edu":"高中以下",
//            "extra_company_addr":{
//        "address1":"玄武湖(东北门)",
//                "address2":"jjj",
//                "city":"南京市",
//                "created_time":"2017-08-23T16:27:23.877",
//                "district":"玄武区",
//                "province":"江苏省",
//                "update_time":"2017-08-23T16:27:23.877"
//    },
//        "extra_company_name":"nnn",
//            "extra_income":"2225",
//            "extra_income_type":"工资",
//            "extra_work_phone_num":"jjjj",
//            "extra_work_position":"高管",
//            "gender":"男",
//            "house_area":"9666",
//            "house_owner_name":"mmjj",
//            "house_owner_relation":"自己",
//            "house_type":"商品房",
//            "id_no":"xx",
//            "major_company_addr":{
//        "city":"",
//                "created_time":"2017-08-23T16:27:23.843",
//                "province":"",
//                "update_time":"2017-08-23T16:27:23.843"
//    },
//        "major_income":"555",
//            "major_income_type":"其他",
//            "major_remark":"nnn",
//            "marriage":"未婚",
//            "mobile":"13888888888",
//            "reg_addr":{
//        "city":"南京市",
//                "created_time":"2017-08-23T16:27:23.772",
//                "district":"玄武区",
//                "province":"江苏省",
//                "update_time":"2017-08-23T16:27:23.772"
//    },
//        "reg_addr_details":"",
//            "spouse":{
//        "clt_id":"54c2327a889d11e79bc602f1f38b2f4a",
//                "clt_nm":"",
//                "commited":"0",
//                "current_addr":{
//            "city":"",
//                    "province":""
//        },
//        "extra_company_addr":{
//            "city":"",
//                    "province":""
//        },
//        "extra_income":"0",
//                "house_area":"0",
//                "id_no":"",
//                "major_company_addr":{
//            "city":"",
//                    "province":""
//        },
//        "major_income":"0",
//                "mobile":"",
//                "reg_addr":{
//            "city":"",
//                    "province":""
//        },
//        "status":"N"
//    },
//        "status":"N",
//            "update_time":"2017-08-22 17:50:41",
//            "urg_contact1":"mmm",
//            "urg_contact2":"旺资",
//            "urg_mobile1":"666",
//            "urg_mobile2":"123",
//            "urg_relation1":"父母",
//            "urg_relation2":"朋友"
//    }


    /**
     * major_income_type : null
     * major_income : 0
     * major_busi_type : null
     * major_company_name : null
     * major_company_addr : {"province":"","city":"","district":null,"zip_code":null,"address1":null,"address2":null,"longitude":null,"latitude":null,"len_of_residence":null,"created_time":"2017-08-22T17:09:33.306","update_time":"2017-08-22T17:09:33.306"}
     * major_work_position : null
     * major_work_phone_num : null
     * major_remark : null
     * extra_income_type : null
     * extra_income : 0
     * extra_busi_type : null
     * extra_company_name : null
     * extra_company_addr : {"province":"","city":"","district":null,"zip_code":null,"address1":null,"address2":null,"longitude":null,"latitude":null,"len_of_residence":null,"created_time":"2017-08-22T17:09:33.324","update_time":"2017-08-22T17:09:33.324"}
     * extra_work_position : null
     * extra_work_phone_num : null
     * extra_remark : null
     * clt_id : 9c77d6cc871911e7999802f1f38b2f4a
     * clt_nm : 担保人2
     * id_no : 0987654321
     * gender : null
     * birthday : null
     * age : null
     * edu : null
     * marriage : 已婚
     * mobile : 13333333333
     * reg_addr : {"province":"","city":"","district":null,"zip_code":null,"address1":null,"address2":null,"longitude":null,"latitude":null,"len_of_residence":null,"created_time":"2017-08-22T17:09:33.266","update_time":"2017-08-22T17:09:33.266"}
     * reg_addr_details : null
     * current_addr : {"province":"","city":"","district":null,"zip_code":null,"address1":null,"address2":null,"longitude":null,"latitude":null,"len_of_residence":null,"created_time":"2017-08-22T17:09:33.288","update_time":"2017-08-22T17:09:33.288"}
     * house_owner_name : null
     * house_type : null
     * house_area : 0
     * house_owner_relation : null
     * urg_contact1 : null
     * urg_mobile1 : null
     * urg_relation1 : null
     * urg_contact2 : null
     * urg_mobile2 : null
     * urg_relation2 : null
     * commited : 1
     * status : N
     * created_time : 2017-08-22 17:09:33
     * update_time : 2017-08-22 17:09:32
     * spouse : {"major_income_type":null,"major_income":0,"major_busi_type":null,"major_company_name":null,"major_company_addr":{"province":"","city":"","district":null,"zip_code":null,"address1":null,"address2":null,"longitude":null,"latitude":null,"len_of_residence":null,"created_time":"2017-08-22T17:09:33.630","update_time":"2017-08-22T17:09:33.630"},"major_work_position":null,"major_work_phone_num":null,"major_remark":null,"extra_income_type":null,"extra_income":0,"extra_busi_type":null,"extra_company_name":null,"extra_company_addr":{"province":"","city":"","district":null,"zip_code":null,"address1":null,"address2":null,"longitude":null,"latitude":null,"len_of_residence":null,"created_time":"2017-08-22T17:09:33.698","update_time":"2017-08-22T17:09:33.698"},"extra_work_position":null,"extra_work_phone_num":null,"extra_remark":null,"clt_id":"9c7df05c871911e7999802f1f38b2f4a","clt_nm":"我老婆","id_no":"1234567","gender":null,"birthday":null,"age":null,"edu":null,"marriage":null,"mobile":"","reg_addr":{"province":"","city":"","district":null,"zip_code":null,"address1":null,"address2":null,"longitude":null,"latitude":null,"len_of_residence":null,"created_time":"2017-08-22T17:09:33.497","update_time":"2017-08-22T17:09:33.498"},"reg_addr_details":null,"current_addr":{"province":"","city":"","district":null,"zip_code":null,"address1":null,"address2":null,"longitude":null,"latitude":null,"len_of_residence":null,"created_time":"2017-08-22T17:09:33.564","update_time":"2017-08-22T17:09:33.564"},"house_owner_name":null,"house_type":null,"house_area":0,"house_owner_relation":null,"urg_contact1":null,"urg_mobile1":null,"urg_relation1":null,"urg_contact2":null,"urg_mobile2":null,"urg_relation2":null,"commited":0,"status":"N","created_time":"2017-08-22 17:09:33","update_time":"2017-08-22 17:09:33"}
     */

    public String major_income_type;
    public String major_income;
    public String major_busi_type = "";
    public String major_company_name;
    public MajorCompanyAddrBean major_company_addr;
    public String major_work_position;
    public String major_work_phone_num;
    public String major_remark;
    public String extra_income_type;
    public String extra_income;
    public String extra_busi_type = "";
    public String extra_company_name;
    public ExtraCompanyAddrBean extra_company_addr;
    public String extra_work_position;
    public String extra_work_phone_num;
    public String extra_remark;
    public String clt_id;
    public String clt_nm;
    public String id_no;
    public String gender;
    public String birthday;
    public String age;
    public String edu;
    public String marriage;
    public String mobile;
    public RegAddrBean reg_addr;
    public String reg_addr_details;
    public CurrentAddrBean current_addr;
    public String house_owner_name;
    public String house_type;
    public String house_area;
    public String house_owner_relation;
    public String urg_contact1;
    public String urg_mobile1;
    public String urg_relation1;
    public String urg_contact2;
    public String urg_mobile2;
    public String urg_relation2;
    public String commited;
    public String status;
    public String created_time;
    public String drv_lic_relationship;
    public String update_time;
    public SpouseBean spouse;
    public String is_live_with_parent;
    public String child_num;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static class MajorCompanyAddrBean {
        /**
         * province :
         * city :
         * district : null
         * zip_code : null
         * address1 : null
         * address2 : null
         * longitude : null
         * latitude : null
         * len_of_residence : null
         * created_time : 2017-08-22T17:09:33.306
         * update_time : 2017-08-22T17:09:33.306
         */

        public String province;
        public String city;
        public String district;
        public String zip_code;
        public String address1;
        public String address2;
        public String longitude;
        public String latitude;
        public String len_of_residence;
        public String created_time;
        public String update_time;

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }

    public static class ExtraCompanyAddrBean {
        /**
         * province :
         * city :
         * district : null
         * zip_code : null
         * address1 : null
         * address2 : null
         * longitude : null
         * latitude : null
         * len_of_residence : null
         * created_time : 2017-08-22T17:09:33.324
         * update_time : 2017-08-22T17:09:33.324
         */

        public String province;
        public String city;
        public String district;
        public String zip_code;
        public String address1;
        public String address2;
        public String longitude;
        public String latitude;
        public String len_of_residence;
        public String created_time;
        public String update_time;

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }

    public static class RegAddrBean {
        /**
         * province :
         * city :
         * district : null
         * zip_code : null
         * address1 : null
         * address2 : null
         * longitude : null
         * latitude : null
         * len_of_residence : null
         * created_time : 2017-08-22T17:09:33.266
         * update_time : 2017-08-22T17:09:33.266
         */

        public String province;
        public String city;
        public String district;
        public String zip_code;
        public String address1;
        public String address2;
        public String longitude;
        public String latitude;
        public String len_of_residence;
        public String created_time;
        public String update_time;

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }

    public static class CurrentAddrBean {
        /**
         * province :
         * city :
         * district : null
         * zip_code : null
         * address1 : null
         * address2 : null
         * longitude : null
         * latitude : null
         * len_of_residence : null
         * created_time : 2017-08-22T17:09:33.288
         * update_time : 2017-08-22T17:09:33.288
         */

        public String province;
        public String city;
        public String district;
        public String zip_code;
        public String address1;
        public String address2;
        public String longitude;
        public String latitude;
        public String len_of_residence;
        public String created_time;
        public String update_time;

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }

    public static class SpouseBean {
        /**
         * major_income_type : null
         * major_income : 0
         * major_busi_type : null
         * major_company_name : null
         * major_company_addr : {"province":"","city":"","district":null,"zip_code":null,"address1":null,"address2":null,"longitude":null,"latitude":null,"len_of_residence":null,"created_time":"2017-08-22T17:09:33.630","update_time":"2017-08-22T17:09:33.630"}
         * major_work_position : null
         * major_work_phone_num : null
         * major_remark : null
         * extra_income_type : null
         * extra_income : 0
         * extra_busi_type : null
         * extra_company_name : null
         * extra_company_addr : {"province":"","city":"","district":null,"zip_code":null,"address1":null,"address2":null,"longitude":null,"latitude":null,"len_of_residence":null,"created_time":"2017-08-22T17:09:33.698","update_time":"2017-08-22T17:09:33.698"}
         * extra_work_position : null
         * extra_work_phone_num : null
         * extra_remark : null
         * clt_id : 9c7df05c871911e7999802f1f38b2f4a
         * clt_nm : 我老婆
         * id_no : 1234567
         * gender : null
         * birthday : null
         * age : null
         * edu : null
         * marriage : null
         * mobile :
         * reg_addr : {"province":"","city":"","district":null,"zip_code":null,"address1":null,"address2":null,"longitude":null,"latitude":null,"len_of_residence":null,"created_time":"2017-08-22T17:09:33.497","update_time":"2017-08-22T17:09:33.498"}
         * reg_addr_details : null
         * current_addr : {"province":"","city":"","district":null,"zip_code":null,"address1":null,"address2":null,"longitude":null,"latitude":null,"len_of_residence":null,"created_time":"2017-08-22T17:09:33.564","update_time":"2017-08-22T17:09:33.564"}
         * house_owner_name : null
         * house_type : null
         * house_area : 0
         * house_owner_relation : null
         * urg_contact1 : null
         * urg_mobile1 : null
         * urg_relation1 : null
         * urg_contact2 : null
         * urg_mobile2 : null
         * urg_relation2 : null
         * commited : 0
         * status : N
         * created_time : 2017-08-22 17:09:33
         * update_time : 2017-08-22 17:09:33
         */

        public String major_income_type;
        public String major_income;
        public String major_busi_type;
        public String major_company_name;
        public MajorCompanyAddrBeanX major_company_addr;
        public String major_work_position;
        public String major_work_phone_num;
        public String major_remark;
        public String extra_income_type;
        public String extra_income;
        public String extra_busi_type;
        public String extra_company_name;
        public ExtraCompanyAddrBeanX extra_company_addr;
        public String extra_work_position;
        public String extra_work_phone_num;
        public String extra_remark;
        public String clt_id;
        public String clt_nm;
        public String id_no;
        public String gender;
        public String birthday;
        public String age;
        public String edu;
        public String marriage;
        public String mobile;
        public RegAddrBeanX reg_addr;
        public String reg_addr_details;
        public CurrentAddrBeanX current_addr;
        public String house_owner_name;
        public String house_type;
        public String house_area;
        public String house_owner_relation;
        public String urg_contact1;
        public String urg_mobile1;
        public String urg_relation1;
        public String urg_contact2;
        public String urg_mobile2;
        public String urg_relation2;
        public String commited;
        public String status;
        public String created_time;
        public String update_time;
        public String drv_lic_relationship;

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }

        public static class MajorCompanyAddrBeanX {
            /**
             * province :
             * city :
             * district : null
             * zip_code : null
             * address1 : null
             * address2 : null
             * longitude : null
             * latitude : null
             * len_of_residence : null
             * created_time : 2017-08-22T17:09:33.630
             * update_time : 2017-08-22T17:09:33.630
             */

            public String province;
            public String city;
            public String district;
            public String zip_code;
            public String address1;
            public String address2;
            public String longitude;
            public String latitude;
            public String len_of_residence;
            public String created_time;
            public String update_time;

            @Override
            public String toString() {
                return new Gson().toJson(this);
            }
        }

        public static class ExtraCompanyAddrBeanX {
            /**
             * province :
             * city :
             * district : null
             * zip_code : null
             * address1 : null
             * address2 : null
             * longitude : null
             * latitude : null
             * len_of_residence : null
             * created_time : 2017-08-22T17:09:33.698
             * update_time : 2017-08-22T17:09:33.698
             */

            public String province;
            public String city;
            public String district;
            public String zip_code;
            public String address1;
            public String address2;
            public String longitude;
            public String latitude;
            public String len_of_residence;
            public String created_time;
            public String update_time;

            @Override
            public String toString() {
                return new Gson().toJson(this);
            }
        }

        public static class RegAddrBeanX {
            /**
             * province :
             * city :
             * district : null
             * zip_code : null
             * address1 : null
             * address2 : null
             * longitude : null
             * latitude : null
             * len_of_residence : null
             * created_time : 2017-08-22T17:09:33.497
             * update_time : 2017-08-22T17:09:33.498
             */

            public String province;
            public String city;
            public String district;
            public String zip_code;
            public String address1;
            public String address2;
            public String longitude;
            public String latitude;
            public String len_of_residence;
            public String created_time;
            public String update_time;

            @Override
            public String toString() {
                return new Gson().toJson(this);
            }
        }

        public static class CurrentAddrBeanX {
            /**
             * province :
             * city :
             * district : null
             * zip_code : null
             * address1 : null
             * address2 : null
             * longitude : null
             * latitude : null
             * len_of_residence : null
             * created_time : 2017-08-22T17:09:33.564
             * update_time : 2017-08-22T17:09:33.564
             */

            public String province;
            public String city;
            public String district;
            public String zip_code;
            public String address1;
            public String address2;
            public String longitude;
            public String latitude;
            public String len_of_residence;
            public String created_time;
            public String update_time;

            @Override
            public String toString() {
                return new Gson().toJson(this);
            }
        }
    }
}
