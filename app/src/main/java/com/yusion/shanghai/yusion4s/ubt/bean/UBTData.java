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
     * account : 19999999999
     * app : Yusion4s
     * brand : samsung
     * data : [{"category":"ubt","clt_nm":null,"contact_list":[],"mobile":"19999999999","sms_list":[],"ubt_list":[{"action":"app_awake","action_value":null,"object":"","page":"","page_cn":"","ts":1513840875198,"widget":null,"widget_cn":null},{"action":"page_show","action_value":null,"object":"activity","page":"launch","page_cn":"启动页面","ts":1513840875210,"widget":null,"widget_cn":null},{"action":"app_pause","action_value":null,"object":"","page":"","page_cn":"","ts":1513840875635,"widget":null,"widget_cn":null},{"action":"page_hidden","action_value":null,"object":"activity","page":"launch","page_cn":"启动页面","ts":1513840875641,"widget":null,"widget_cn":null},{"action":"page_show","action_value":null,"object":"activity","page":"home_order_mine","page_cn":"首页-订单-我的页面","ts":1513840875947,"widget":null,"widget_cn":null},{"action":"app_pause","action_value":null,"object":"","page":"","page_cn":"","ts":1513840877200,"widget":null,"widget_cn":null},{"action":"page_hidden","action_value":null,"object":"activity","page":"home_order_mine","page_cn":"首页-订单-我的页面","ts":1513840877228,"widget":null,"widget_cn":null},{"action":"app_pause","action_value":null,"object":"","page":"","page_cn":"","ts":1513840877844,"widget":null,"widget_cn":null},{"action":"app_awake","action_value":null,"object":"","page":"","page_cn":"","ts":1513840892335,"widget":null,"widget_cn":null}]}]
     * factory : samsung
     * fingerprint : null
     * gps : {"latitude":"31.211603","longitude":"121.62609"}
     * imei : 990004798048900
     * imsi : 460028217607025
     * mac : null
     * mobile : 19999999999
     * model : SM-G9250
     * os_version : 6.0.1
     * rooted : true
     * system : android
     * token : 11f88cfb37c9cef11633111500000000
     */

    public String imei;
    public String imsi;
    public String mac;
    public String app;
    public String token;
    public String mobile;
    public String account;
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
        app = "Yusion4s";
        token = SharedPrefsUtil.getInstance(context).getValue("token", null);
        account = SharedPrefsUtil.getInstance(context).getValue("account", null);
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
