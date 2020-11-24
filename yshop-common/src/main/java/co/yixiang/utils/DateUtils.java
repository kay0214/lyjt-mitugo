/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 */
package co.yixiang.utils;

import cn.hutool.core.date.DateTime;
import co.yixiang.exception.ErrorRequestException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String MMDD = "MMdd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";


    public static String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate() {
        return dateTimeNow(YYYY_MM_DD);
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDateMMDD() {
        return dateTimeNow(MMDD);
    }

    public static final String getTime() {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow() {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format) {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date) {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts) {
        try {
            return new SimpleDateFormat(format).parse(ts);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * String(yyyy-MM-dd HH:mm:ss)转10位时间戳
     * @param dateStr
     * @return
     */
    public static int stringToTimestamp(String dateStr) {
        int times = 0;
        try {
            times = (int) ((Timestamp.valueOf(dateStr).getTime()) / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times;
    }

    /**
     * 获得当前时间的时间戳  10位
     *
     * @return
     */
    public static Integer getNowTime() {
        Long time = System.currentTimeMillis() / 1000;
        return time.intValue();
    }

    /**
     * Date转LocalDateTime
     * @param date
     * @return
     */
    public static LocalDateTime dateToLocalDate(Date date) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());

    }

    /**
     * 10位时间戳转字符串   默认为yyyy-MM-dd HH:mm:ss
     *
     * @param timestamp
     * @param format
     * @return
     */
    public static String timestampToStr10(Integer timestamp, String format) {
        if (StringUtils.isEmpty(format)) {
            return timestampToStr10(timestamp);
        }
        if (timestamp == null || timestamp == 0) {
            return "";
        }
        Long time = Long.valueOf(timestamp) * 1000;
        DateTime now = new DateTime(time);
        return now.toString(format);
    }

    /**
     * 10位时间戳转字符串yyyy-MM-dd HH:mm:ss
     *
     * @param timestamp
     * @return
     */
    public static String timestampToStr10(Integer timestamp) {
        if (timestamp == null || timestamp == 0) {
            return "";
        }
        Long time = Long.valueOf(timestamp) * 1000;
        DateTime now = new DateTime(time);
        return now.toString(YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 日期增加Minutes分钟
     * @author liusy
     * @date 2019/9/23 18:36
     */
    public static LocalDateTime plusMinutes(LocalDateTime localDate, Long minutes) {
        return localDate.plusMinutes(minutes);
    }

    /**
     * 日期减少Minutes分钟
     * @author liusy
     * @date 2019/9/23 18:36
     */
    public static LocalDateTime minusMinutes(LocalDateTime localDate, Long minutes) {
        return localDate.minusMinutes(minutes);
    }

    /**
     * 日期增加Seconds秒
     * @author liusy
     * @date 2019/9/23 18:36
     */
    public static LocalDateTime plusSeconds(LocalDateTime localDate, Long seconds) {
        return localDate.plusSeconds(seconds);
    }

    /**
     * 日期增加Seconds秒
     * @author liusy
     * @date 2019/9/23 18:36
     */
    public static LocalDateTime minusSeconds(LocalDateTime localDate, Long seconds) {
        return localDate.minusSeconds(seconds);
    }

    /**
     * 日期增加days天
     * @author zhangyk
     * @date 2019/9/23 18:36
     */
    public static LocalDate plusDays(LocalDate localDate, int days) {
        return localDate.plusDays(days);
    }

    /**
     * 日期减少days天
     * @author zhangyk
     * @date 2019/9/23 18:36
     */
    public static LocalDate minusDays(LocalDate localDate, int days) {
        return localDate.minusDays(days);
    }

    /**
     * 日期增加mouths月
     * @author liusy
     * @date 2020/5/27 11:20
     */
    public static LocalDate plusMouths(LocalDate localDate, int mouths) {
        return localDate.plusMonths(mouths);
    }

    /**
     * 日期增加mouths月
     * @author liusy
     * @date 2020/5/27 11:20
     */
    public static LocalDate minusMouths(LocalDate localDate, int mouths) {
        return localDate.minusMonths(mouths);
    }

    /**
     * 获取当天开始时间
     *
     * @param timestamp
     * @return
     */
    public static Timestamp getDayStart(Timestamp timestamp) {
        return Timestamp.valueOf(timestamp.toLocalDateTime().toLocalDate().toString() + " 00:00:00");
    }

    /**
     * 获取当天结束时间
     *
     * @param timestamp
     * @return
     */
    public static Timestamp getDayEnd(Timestamp timestamp) {
        return Timestamp.valueOf(timestamp.toLocalDateTime().toLocalDate().toString() + " 23:59:59");
    }

    /**
     * 根据身份证号计算年龄
     * @param idCardNo
     * @return
     */
    public static Integer IdCardNoToAge(String idCardNo) {
        Integer age = 0;
        if (co.yixiang.utils.StringUtils.isBlank(idCardNo)) {
            throw new ErrorRequestException("身份证号为空！");
        }

        if (idCardNo.length() != 15 && idCardNo.length() != 18) {
            throw new ErrorRequestException("身份证号长度错误！");
        }
        Calendar cal = Calendar.getInstance();
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayNow = cal.get(Calendar.DATE);

        int year = Integer.valueOf(idCardNo.substring(6, 10));
        int month = Integer.valueOf(idCardNo.substring(10, 12));
        int day = Integer.valueOf(idCardNo.substring(12, 14));

        if ((month < monthNow) || (month == monthNow && day <= dayNow)) {
            age = yearNow - year;
        } else {
            age = yearNow - year - 1;
        }
        return age;
    }

    /**
     * 获取某个日期的时间戳（yyyy-MM-dd）
     * @param strDate
     * @return
     */
    public static Integer stringToTimestampDate(String strDate){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Long dateTimeLong = 0L;
        try {
            dateTimeLong = sdf.parse(strDate).getTime()/ 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTimeLong.intValue();
    }
}
