package com.yusion.shanghai.yusion4s;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Looper;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.awen.photo.FrescoImageLoader;
import com.instabug.library.Instabug;
import com.instabug.library.invocation.InstabugInvocationEvent;
import com.pgyersdk.crash.PgyCrashManager;
import com.umeng.analytics.MobclickAgent;
import com.yusion.shanghai.yusion4s.bean.config.ConfigResp;
import com.yusion.shanghai.yusion4s.retrofit.api.ConfigApi;
import com.yusion.shanghai.yusion4s.settings.Settings;
import com.yusion.shanghai.yusion4s.ubt.sql.SqlLiteUtil;
import com.yusion.shanghai.yusion4s.utils.SharedPrefsUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Locale;

import cn.jpush.android.api.JPushInterface;
import io.sentry.Sentry;
import io.sentry.android.AndroidSentryClientFactory;

/**
 * Created by ice on 2017/8/9.
 */
public class Yusion4sApp extends MultiDexApplication {

    private static Yusion4sApp myApplication = null;
    public static String TOKEN;
    public static String ACCOUNT;
    public static ConfigResp CONFIG_RESP;

    public static boolean isForeground;

    public static boolean isLogin;
    private static String reg_id;

    //定位服务类
    @SuppressLint("StaticFieldLeak")
    public static AMapLocationClient aMapLocationClient;

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.setCustomCrashHanler(this);

        initData();

        initSentry();

        PgyCrashManager.register(this);

        initJpush();

        initUmeng();

        initInstabug();

        initAMap();

        //初始化photoLibrary
        FrescoImageLoader.init(this);
    }

    private void initSentry() {
        if (BuildConfig.isOnline) {
            Sentry.init("http://99c65c10b5564f8280e1d8230cb97880:18d30de1e6c64542837a7d82bbd33e9c@116.62.161.180:9002/6", new AndroidSentryClientFactory(this));
        } else {
            Sentry.init("http://a38b78ed9d104631998185e97f1465ff:d7046f67331d4f8d860d922b0e02bc55@116.62.161.180:9002/8", new AndroidSentryClientFactory(this));
        }
    }

    private void initData() {
        SqlLiteUtil.init(this);
        myApplication = this;
        TOKEN = SharedPrefsUtil.getInstance(this).getValue("token", "");
        ACCOUNT = SharedPrefsUtil.getInstance(this).getValue("account", "");
        String cacheUrl = SharedPrefsUtil.getInstance(this).getValue("SERVER_URL", "");
        if (!TextUtils.isEmpty(cacheUrl)) {
            Settings.SERVER_URL = cacheUrl;
        }
    }

    private void initInstabug() {
        if (Settings.forAppium) {
            //appium不支持instabug
            return;
        }
        new Instabug.Builder(this, "fac6ff642eec6bf5599d893a0a1224e3").setInvocationEvent(InstabugInvocationEvent.SHAKE).build();
    }

    private void initUmeng() {
        //禁止默认的页面统计方式
        MobclickAgent.openActivityDurationTrack(false);
        //捕获程序崩溃日志
        MobclickAgent.setCatchUncaughtExceptions(true);
    }

    public void clearUserData() {
        TOKEN = "";
        ACCOUNT = "";
        SharedPrefsUtil.getInstance(this).putValue("token", TOKEN);
        SharedPrefsUtil.getInstance(this).putValue("account", ACCOUNT);
    }

    public static Yusion4sApp getInstance() {
        return myApplication;
    }

    private void initJpush() {
        if (Settings.isOnline) {
            JPushInterface.setDebugMode(false);
        } else {
            JPushInterface.setDebugMode(true);
        }

        JPushInterface.init(this);
        new Thread(() -> {
            long time = new Date().getTime();
            while (TextUtils.isEmpty(reg_id) || (new Date().getTime() - time) / 1000 > 10) {
                reg_id = JPushInterface.getRegistrationID(Yusion4sApp.this);
            }
            if (TextUtils.isEmpty(reg_id)) {
                reg_id = SharedPrefsUtil.getInstance(Yusion4sApp.this).getValue("reg_id", "");
            }
            SharedPrefsUtil.getInstance(Yusion4sApp.this).putValue("reg_id", reg_id);
            Log.e("TAG", "reg_id: " + reg_id);
        }).start();

    }

    private void initAMap() {
        aMapLocationClient = new AMapLocationClient(this);
        //定位参数设置
        AMapLocationClientOption aMapLocationClientOption;
        aMapLocationClientOption = new AMapLocationClientOption();
        aMapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        aMapLocationClientOption.setOnceLocation(true);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        aMapLocationClientOption.setOnceLocationLatest(true);
        //设置是否返回地址信息（默认返回地址信息）
        aMapLocationClientOption.setNeedAddress(true);
        //设置是否强制刷新WIFI，默认为true，强制刷新。
        aMapLocationClientOption.setWifiScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        aMapLocationClientOption.setMockEnable(false);
        //设置定位请求超时时间,默认是30000毫秒，建议不低于8000毫秒
        aMapLocationClientOption.setHttpTimeOut(10000);
        //关闭缓存机制
        aMapLocationClientOption.setLocationCacheEnable(false);
        aMapLocationClient.setLocationOption(aMapLocationClientOption);
        requestLocation(null);
    }

    public void requestLocation(AMapLocationListener listener) {
        if (Settings.forAppium) {
            //appium不支持gps定位
            return;
        }
        aMapLocationClient.setLocationListener(aMapLocation -> {
            if (aMapLocation.getErrorCode() == 0) {
                String longitude = String.valueOf(aMapLocation.getLongitude());
                String latitude = String.valueOf(aMapLocation.getLatitude());
                Log.e("GPS", String.format(Locale.CHINA, "location Success:{\"latitude\":\"%s\",\"longitude\":\"%s\"}", latitude, longitude));
                SharedPrefsUtil.getInstance(Yusion4sApp.this).putValue("longitude", longitude);
                SharedPrefsUtil.getInstance(Yusion4sApp.this).putValue("latitude", latitude);
            } else {
                String errorInfo = String.format(Locale.CHINA, "location Error, ErrCode:%d, errInfo:%s", aMapLocation.getErrorCode(), aMapLocation.getErrorInfo());
                Log.e("GPS", errorInfo);
                Sentry.capture("GPS:" + errorInfo);
            }
            if (listener != null) {
                listener.onLocationChanged(aMapLocation);
            }
        });
        aMapLocationClient.startLocation();
    }

    public ConfigResp getConfigResp() {
        if (CONFIG_RESP == null) {
            try {
                CONFIG_RESP = ConfigApi.parseJsonObject2ConfigResp(new JSONObject(SharedPrefsUtil.getInstance(this).getValue("config_json", "")));
            } catch (JSONException e) {
                Sentry.capture(e);
            }
        }
        return CONFIG_RESP;
    }

    static class CrashHandler implements Thread.UncaughtExceptionHandler {

        @SuppressLint("StaticFieldLeak")
        private static CrashHandler instance = new CrashHandler();
        private Context mContext;

        private CrashHandler() {
        }

        public static CrashHandler getInstance() {
            return instance;
        }

        void setCustomCrashHanler(Context context) {
            mContext = context;
            //崩溃时将catch住异常
            Thread.setDefaultUncaughtExceptionHandler(this);
        }

        //崩溃时触发
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            //使用Toast进行提示
            Log.e("TAG", "uncaughtException: " + ex);
            showToast(mContext, "很抱歉，程序异常即将退出！");
            //延时退出
            try {
                Thread.sleep(2000);
                System.exit(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //线程中展示Toast
        private void showToast(final Context context, final String msg) {
            new Thread(() -> {
                Looper.prepare();
                Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                Looper.loop();
            }).start();
        }
    }

}
