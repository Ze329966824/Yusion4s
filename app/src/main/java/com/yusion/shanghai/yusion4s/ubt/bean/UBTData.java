package com.yusion.shanghai.yusion4s.ubt.bean;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.google.gson.Gson;
import com.yusion.shanghai.yusion4s.ubt.sql.UBTEvent;
import com.yusion.shanghai.yusion4s.utils.MobileDataUtil;
import com.yusion.shanghai.yusion4s.utils.SharedPrefsUtil;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by ice on 2017/9/15.
 */

public class UBTData {

    /**
     * imei :
     * imsi :
     * mac :
     * app : Yusion/Yusion4s
     * gps :
     * token :
     * mobile : 13892839048
     * fingerprint : 同盾设备指纹
     * system : 手机系统
     * factory : 厂家
     * model : 型号
     * brand : 手机品牌
     * os_version : 操作系统版本
     * contact : {"name":"姓名","mobile":"手机号","contact_list":[{"display_name":"老哥","data1":"13323432354"},{"display_name":"老马","data1":"13823432354"}]}
     */

    public String imei;
    public String imsi;
    public String mac;
    public String app;
    public String token;
    public String mobile;
    public String fingerprint;
    public String system;
    public String factory;
    public String model;
    public String brand;
    public String os_version;
    public boolean rooted;
    public Gps gps = new Gps();
    public List<DataBean> data = new ArrayList<>();
    //    public List<UBTEvent> data;

    public UBTData(Context context) {
        gps.latitude = SharedPrefsUtil.getInstance(context).getValue("latitude", "");
        gps.longitude = SharedPrefsUtil.getInstance(context).getValue("longitude", "");
        system = "android";
        MobileDataUtil.getDeviceData(context);
        brand = SharedPrefsUtil.getInstance(context).getValue("brand", "");
        os_version = SharedPrefsUtil.getInstance(context).getValue("release", "");
        factory = SharedPrefsUtil.getInstance(context).getValue("factory", "");
        model = SharedPrefsUtil.getInstance(context).getValue("model", "");
        rooted = MobileDataUtil.hasRoot();
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        imei = telephonyManager.getDeviceId();
        imsi = telephonyManager.getSubscriberId();
        app = "Yusion";
        token = SharedPrefsUtil.getInstance(context).getValue("token", null);
        mobile = SharedPrefsUtil.getInstance(context).getValue("mobile", null);
    }

    public static class Gps {
        public String longitude;
        public String latitude;

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static class DataBean {
        public String category;
        public String clt_nm;
        public String mobile;
        public List<UBTEvent> ubt_list = new ArrayList<>();
        public List<ContactBean> contact_list = new ArrayList<>();
        public List<SmsBean> sms_list = new ArrayList<>();
        //public List<String> raw_data = new ArrayList<>();

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }

        public static class ContactBean {
            /**
             * display_name : 老哥
             * data1 : 13323432354
             */

            public String display_name;
            public String data1;

            @Override
            public String toString() {
                return new Gson().toJson(this);
            }
        }

        public static class SmsBean {
            public String to;
            public String from;
            public String content;
            public String type;
            public String ts;

            @Override
            public String toString() {
                return new Gson().toJson(this);
            }
        }
    }

}
