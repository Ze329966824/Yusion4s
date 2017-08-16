package com.yusion.shanghai.yusion4s.bean.upload;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by ice on 2017/7/31.
 */

public class ListLabelsErrorResp {


    /**
     * lender_sp : {"err_labels":[],"err_num":0}
     * counter_guarantor : {"err_labels":[],"err_num":0}
     * guarantor : {"err_labels":[],"err_num":0}
     * guarantor_sp : {"err_labels":[],"err_num":0}
     * lender : {"err_labels":["id_card_front"],"err_num":1}
     * counter_guarantor_sp : {"err_labels":[],"err_num":0}
     */

    public LenderSpBean lender_sp;
    public CounterGuarantorBean counter_guarantor;
    public GuarantorBean guarantor;
    public GuarantorSpBean guarantor_sp;
    public LenderBean lender;
    public CounterGuarantorSpBean counter_guarantor_sp;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static class LenderSpBean {
        /**
         * err_labels : []
         * err_num : 0
         */

        public int err_num;
        public List<String> err_labels;

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }

    public static class CounterGuarantorBean {
        /**
         * err_labels : []
         * err_num : 0
         */

        public int err_num;
        public List<String> err_labels;

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }

    public static class GuarantorBean {
        /**
         * err_labels : []
         * err_num : 0
         */

        public int err_num;
        public List<String> err_labels;

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }

    public static class GuarantorSpBean {
        /**
         * err_labels : []
         * err_num : 0
         */

        public int err_num;
        public List<String> err_labels;

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }

    public static class LenderBean {
        /**
         * err_labels : ["id_card_front"]
         * err_num : 1
         */

        public int err_num;
        public List<String> err_labels;

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }

    public static class CounterGuarantorSpBean {
        /**
         * err_labels : []
         * err_num : 0
         */

        public int err_num;
        public List<String> err_labels;

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }
}
