package com.yusion.shanghai.yusion4s.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckMobileUtil {
    public static boolean checkMobile(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }

        Pattern p;
        Matcher m;
        boolean b;
        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");
        m = p.matcher(str);
        b = m.matches();
        return b;
    }
}
