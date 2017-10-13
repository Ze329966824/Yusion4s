package com.yusion.shanghai.yusion4s.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by aa on 2017/9/20.
 */

public class MobileDataUtil {

    public static boolean hasRoot() {
        boolean root = false;
        try {
            root = !((!new File("/system/bin/su").exists()) && (!new File("/system/xbin/su").exists()));
        } catch (Exception ignored) {
        }
        return root;
    }

    /**
     * 获取用户手机数据
     *
     * @param context
     * @param dataType sms 获取短信数据 --- contact获取联系人(不包括sim卡) --- sim获取sim卡联系人(只能获取第一张)
     * @return
     */
    public static JSONArray getUserData(Context context, String dataType) {
        JSONArray dataJArray = new JSONArray();
        Uri uri = null;
        try {
            switch (dataType) {
                case "sms":
                    uri = Uri.parse("content://sms/");
                    break;
                case "photo":
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    break;
                case "calendar":
                    uri = CalendarContract.Events.CONTENT_URI;
                    break;
                case "call_log":
                    uri = CallLog.Calls.CONTENT_URI;
                    break;
                case "contact":
                    uri = ContactsContract.Data.CONTENT_URI;
                    break;
                case "sim":
                    uri = Uri.parse("content://icc/adn");
                    break;
                case "app":
                    List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
                    JSONObject dataJObject;
                    for (PackageInfo info : packages) {
                        if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                            dataJObject = new JSONObject();
                            dataJObject.put("firstInstallTime", info.firstInstallTime);
                            dataJObject.put("lastUpdateTime", info.lastUpdateTime);
                            dataJObject.put("name", info.applicationInfo.loadLabel(context.getPackageManager()).toString());
                            dataJObject.put("packageName", info.packageName);
                            dataJObject.put("versionCode", info.versionCode);
                            dataJObject.put("versionName", info.versionName);
                            dataJArray.put(dataJObject);
                        }
                    }
                    return dataJArray;
            }
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            JSONObject dataJObject;
            while (cursor != null && cursor.moveToNext()) {
                String[] strings = cursor.getColumnNames();
                dataJObject = new JSONObject();
                for (int i = 0, size = strings.length; i < size; i++) {
                    int type = cursor.getType(cursor.getColumnIndex(strings[i]));
                    if (Cursor.FIELD_TYPE_BLOB == type) {
                        dataJObject.put(strings[i], cursor.getBlob(cursor.getColumnIndex(strings[i])) == null ? "" : cursor.getBlob(cursor.getColumnIndex(strings[i])));
                    } else {
                        dataJObject.put(strings[i], cursor.getString(cursor.getColumnIndex(strings[i])) == null ? "" : cursor.getString(cursor.getColumnIndex(strings[i])));
                    }

                }
                dataJArray.put(dataJObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataJArray;
    }

    public static JSONObject getDeviceData(Context context) {
        JSONObject dataJson = new JSONObject();

        Build build = new Build();
        for (Field field : Build.class.getFields()) {
            try {
                dataJson.put(field.getName().toLowerCase(), field.get(build));
            } catch (IllegalAccessException | JSONException e) {
                e.printStackTrace();
            }
        }


        Build.VERSION version_build = new Build.VERSION();
        for (Field field : Build.VERSION.class.getFields()) {
            try {
                dataJson.put(field.getName().toLowerCase(), field.get(version_build));
            } catch (IllegalAccessException | JSONException e) {
                e.printStackTrace();
            }
        }

        SharedPrefsUtil.getInstance(context).putValue("release", Build.VERSION.RELEASE);
        SharedPrefsUtil.getInstance(context).putValue("manufacturer", Build.MANUFACTURER);
        SharedPrefsUtil.getInstance(context).putValue("model", Build.MODEL);
        SharedPrefsUtil.getInstance(context).putValue("brand", Build.BRAND);//品牌
        SharedPrefsUtil.getInstance(context).putValue("product", Build.PRODUCT);
        SharedPrefsUtil.getInstance(context).putValue("hardware", Build.HARDWARE);
        SharedPrefsUtil.getInstance(context).putValue("factory", Build.MANUFACTURER);

        return dataJson;

    }

    public static String getMac() {
        String str = "";
        String macSerial = "";
        try {
            Process pp = Runtime.getRuntime().exec(
                    "cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if ("".equals(macSerial)) {
            try {
                return loadFileAsString("/sys/class/net/eth0/address")
                        .toUpperCase().substring(0, 17);
            } catch (Exception e) {
                e.printStackTrace();

            }

        }
        return macSerial;
    }

    private static String loadFileAsString(String fileName) throws Exception {
        FileReader reader = new FileReader(fileName);
        String text = loadReaderAsString(reader);
        reader.close();
        return text;
    }

    private static String loadReaderAsString(Reader reader) throws Exception {
        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[4096];
        int readLength = reader.read(buffer);
        while (readLength >= 0) {
            builder.append(buffer, 0, readLength);
            readLength = reader.read(buffer);
        }
        return builder.toString();
    }

    /**
     * 上传gps photo等所有信息都必须附加以下json
     */
    public static JSONObject getAppendageJson(Context context) {
        JSONObject jsonObject = MobileDataUtil.getDeviceData(context);
        //SharedPrefsUtil.getInstance(context).putValue("mac", MobileDataUtil.getMac());

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            jsonObject.put("mac", MobileDataUtil.getMac());
            jsonObject.put("deviceId", tm.getDeviceId());
            jsonObject.put("deviceSoftwareVersion", tm.getDeviceSoftwareVersion());
            jsonObject.put("line1Number", tm.getLine1Number());
            jsonObject.put("networkCountryIso", tm.getNetworkCountryIso());
            jsonObject.put("networkOperator", tm.getNetworkOperator());
            jsonObject.put("networkOperatorName", tm.getNetworkOperatorName());
            jsonObject.put("simCountryIso", tm.getSimCountryIso());
            jsonObject.put("simOperator", tm.getSimOperator());
            jsonObject.put("simOperatorName", tm.getSimOperatorName());
            jsonObject.put("simSerialNumber", tm.getSimSerialNumber());
            jsonObject.put("subscriberId", tm.getSubscriberId());
            jsonObject.put("voiceMailAlphaTag", tm.getVoiceMailAlphaTag());
            jsonObject.put("voiceMailNumber", tm.getVoiceMailNumber());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //SharedPrefsUtil.getInstance(context).putValue("imei", tm.getDeviceId());
        //SharedPrefsUtil.getInstance(context).putValue("mac", MobileDataUtil.getMac());
        return jsonObject;


//        SharedPrefsUtil.getInstance(this).putValue("deviceId", tm.getDeviceId());
//        SharedPrefsUtil.getInstance(this).putValue("deviceSoftwareVersion", tm.getDeviceSoftwareVersion());
//        SharedPrefsUtil.getInstance(this).putValue("line1Number", tm.getLine1Number());
//        SharedPrefsUtil.getInstance(this).putValue("networkCountryIso", tm.getNetworkCountryIso());
//        SharedPrefsUtil.getInstance(this).putValue("networkOperator", tm.getNetworkOperator());
//        SharedPrefsUtil.getInstance(this).putValue("networkOperatorName", tm.getNetworkOperatorName());
//        SharedPrefsUtil.getInstance(this).putValue("simCountryIso", tm.getSimCountryIso());
//        SharedPrefsUtil.getInstance(this).putValue("simOperator", tm.getSimOperator());
//        SharedPrefsUtil.getInstance(this).putValue("simOperatorName", tm.getSimOperatorName());
//        SharedPrefsUtil.getInstance(this).putValue("simSerialNumber", tm.getSimSerialNumber());
//        SharedPrefsUtil.getInstance(this).putValue("subscriberId", tm.getSubscriberId());
//        SharedPrefsUtil.getInstance(this).putValue("voiceMailAlphaTag", tm.getVoiceMailAlphaTag());
//        SharedPrefsUtil.getInstance(this).putValue("voiceMailNumber", tm.getVoiceMailNumber());
    }

//    public static String getMacAddress() {
//        String result = "";
//        byte[] b;
//        try {
//            NetworkInterface NIC = NetworkInterface.getByName("eth0");
//            b = NIC.getHardwareAddress();
//            StringBuilder buffer = new StringBuilder();
//            for (byte aB : b) {
//                String str = Integer.toHexString(aB & 0xFF);
//                buffer.append(str.length() == 1 ? 0 + str : str);
//            }
//            result = buffer.toString().toUpperCase();
//        } catch (SocketException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }

}
