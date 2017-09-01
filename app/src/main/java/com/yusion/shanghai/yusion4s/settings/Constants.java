package com.yusion.shanghai.yusion4s.settings;

/**
 * Created by ice on 2017/8/4.
 */

public class Constants {
    public static final int REQUEST_IDCARD_1_CAPTURE = 201;
    public static final int REQUEST_IDCARD_2_CAPTURE = 202;
    public static final int REQUEST_CONTACTS = 203;
    public static final int REQUEST_ADDRESS = 204;
    public static final int REQUEST_DOCUMENT = 205;
    public static final int REQUEST_MULTI_DOCUMENT = 206;

    public static class FileLabelType {
        public static final String ID_BACK = "id_card_back";
        public static final String ID_FRONT = "id_card_front";
        public static final String DRI_LIC = "driving_lic";
        public static final String DIVORCE = "divorce_proof";
        public static final String RES_BOOKLET = "res_booklet";
        public static final String MARRIAGE_PROOF = "marriage_proof";
    }

    public static class PersonType {
        public static final String LENDER = "lender";
        public static final String LENDER_SP = "lender_sp";
        public static final String GUARANTOR = "guarantor";
        public static final String GUARANTOR_SP = "guarantor_sp";
    }

}

