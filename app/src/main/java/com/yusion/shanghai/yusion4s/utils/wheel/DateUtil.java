package com.yusion.shanghai.yusion4s.utils.wheel;

/**
 * Created by aa on 2018/1/11.
 */

public class DateUtil {

    public static long getDateMinute(long nowtime, long afterTime) {
        return (nowtime - afterTime) / 1000 / 60;

    }

    public static long getDateHour(long nowTime, long afterTime) {
        return getDateMinute(nowTime, afterTime) / 60;
    }

    public static long getDateDay(long nowTime, long afterTime) {
        return getDateHour(nowTime, afterTime) / 24;
    }
//
//    public static long getDateYear(long nowTime, long afterTime) {
//
//        return;
//    }


}
