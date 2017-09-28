package com.fss.util;

import java.util.Date;

public class DateCalculateUtil {
    public static String calculateDelayTime(Date begin, Date end) {
        try {
            long diff = end.getTime() - begin.getTime();
            long months = diff / (1000 * 60 * 60 * 24 * 30);
            if (months > 0) return months + "月前";
            long days = diff / (1000 * 60 * 60 * 24);
            if (days > 0) return days + "天前";
            long hours = diff / (1000 * 60 * 60);
            if (hours > 0) return hours + "小时前";
            long minutes = diff / (1000 * 60);
            if (minutes > 0) return minutes + "分钟前";
            long seconds = diff / 1000;
            if (seconds > 0) return seconds + "秒前";
            else return "";
        } catch (Exception e) {
            return "时间转换出错";
        }
    }
}
