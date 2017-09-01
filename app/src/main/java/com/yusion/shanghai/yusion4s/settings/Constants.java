package com.yusion.shanghai.yusion4s.settings;

/**
 * Created by ice on 2017/8/4.
 */

public class Constants {
    public static final int REQUEST_IDCARD_1_CAPTURE = 201;
    public static final int REQUEST_IDCARD_2_CAPTURE = 202;
    public static final int REQUEST_CONTACTS = 203;
    public static final int REQUEST_ADDRESS = 204;

    public static final int REQUEST_MULTI_DOCUMENT = 206;

    public static final String CONFIG_STRING = "configString";//配置文件信息


    public static class FileLabelType {
        public static final String ID_BACK = "id_card_back";
        public static final String ID_FRONT = "id_card_front";
        public static final String DRI_LIC = "driving_lic";
        public static final String DIVORCE = "divorce_proof";
        public static final String RES_BOOKLET = "res_booklet";


        public static final String POWER_OF_ATTORNEY_LENDER = "power of attorney_lender";
        public static final String POWER_OF_ATTORNEY_LENDER_SP = "power of attorney_lender_sp";
        public static final String POWER_OF_ATTORNEY_GUARANTOR = "power of attorney_guarantor";
        public static final String POWER_OF_ATTORNEY_GUARANTOR_SP = "power of attorney_guarantor_sp";


    }

    public static class PersonType {
        public static final String LENDER = "lender";
        public static final String LENDER_SP = "lender_sp";
        public static final String GUARANTOR = "guarantor";
        public static final String GUARANTOR_SP = "guarantor_sp";
    }

}
