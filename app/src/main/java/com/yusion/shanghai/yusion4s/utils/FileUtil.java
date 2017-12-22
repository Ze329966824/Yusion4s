package com.yusion.shanghai.yusion4s.utils;

import android.os.Environment;

import com.yusion.shanghai.yusion4s.retrofit.callback.OnVoidCallBack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ice on 2017/9/29.
 */

public class FileUtil {

    /**
     * @param callBack 下载完成后子线程回调
     */
    public static void saveImg(InputStream inputStream, String desPath, OnVoidCallBack callBack) throws IOException {
        File file = new File(desPath);
        FileOutputStream outputStream = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        inputStream.close();
        outputStream.close();
        if (callBack != null) {
            callBack.callBack();
        }
    }

    /**
     * 自动开启子线程记录log
     */
    public static void saveLog(String log) {
        new Thread(() -> {
            String fileName = new SimpleDateFormat("MM-dd").format(new Date()) + ".txt";
            File dir = new File(Environment.getExternalStorageDirectory(), "yusion");
            if (!dir.exists()) {
                dir.mkdir();
            }
            File desFile = new File(dir, fileName);
            try {
                FileOutputStream fos = new FileOutputStream(desFile, true);
                fos.write("\r\n".getBytes());
                fos.write(log.getBytes());
                fos.write("\r\n".getBytes());
                fos.flush();
                fos.close();
            } catch (Exception ignored) {
            }
        }).start();
    }

    /**
     * 判断文件类型 只支持本地图片
     */
    public static FileType getType(String filePath) throws IOException {
        // 获取文件头
        String fileHead = getFileHeader(filePath);

        if (fileHead != null && fileHead.length() > 0) {
            fileHead = fileHead.toUpperCase();
            FileType[] fileTypes = FileType.values();

            for (FileType type : fileTypes) {
                if (fileHead.startsWith(type.getValue())) {
                    return type;
                }
            }
        }
        return null;
    }

    /**
     * 读取文件头
     */
    private static String getFileHeader(String filePath) throws IOException {
        byte[] b = new byte[28];
        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(filePath);
            inputStream.read(b, 0, 28);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return bytesToHex(b);
    }

    /**
     * 将字节数组转换成16进制字符串
     */
    private static String bytesToHex(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public enum FileType {

        /**
         * JPEG
         */
        JPEG("FFD8FF"),

        /**
         * PNG
         */
        PNG("89504E47"),

        /**
         * GIF
         */
        GIF("47494638"),

        /**
         * TIFF
         */
        TIFF("49492A00"),

        /**
         * Windows bitmap
         */
        BMP("424D"),

        /**
         * CAD
         */
        DWG("41433130"),

        /**
         * Adobe photoshop
         */
        PSD("38425053"),

        /**
         * Rich Text Format
         */
        RTF("7B5C727466"),

        /**
         * XML
         */
        XML("3C3F786D6C"),

        /**
         * HTML
         */
        HTML("68746D6C3E"),

        /**
         * Outlook Express
         */
        DBX("CFAD12FEC5FD746F "),

        /**
         * Outlook
         */
        PST("2142444E"),

        /**
         * doc;xls;dot;ppt;xla;ppa;pps;pot;msi;sdw;db
         */
        OLE2("0xD0CF11E0A1B11AE1"),

        /**
         * Microsoft Word/Excel
         */
        XLS_DOC("D0CF11E0"),

        /**
         * Microsoft Access
         */
        MDB("5374616E64617264204A"),

        /**
         * Word Perfect
         */
        WPB("FF575043"),

        /**
         * Postscript
         */
        EPS_PS("252150532D41646F6265"),

        /**
         * Adobe Acrobat
         */
        PDF("255044462D312E"),

        /**
         * Windows Password
         */
        PWL("E3828596"),

        /**
         * ZIP Archive
         */
        ZIP("504B0304"),

        /**
         * ARAR Archive
         */
        RAR("52617221"),

        /**
         * WAVE
         */
        WAV("57415645"),

        /**
         * AVI
         */
        AVI("41564920"),

        /**
         * Real Audio
         */
        RAM("2E7261FD"),

        /**
         * Real Media
         */
        RM("2E524D46"),

        /**
         * Quicktime
         */
        MOV("6D6F6F76"),

        /**
         * Windows Media
         */
        ASF("3026B2758E66CF11"),

        /**
         * MIDI
         */
        MID("4D546864");

        private String value = "";

        private FileType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }

}
