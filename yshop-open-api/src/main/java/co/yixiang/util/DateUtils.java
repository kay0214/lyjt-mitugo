package co.yixiang.util;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * jdk8 新时间API工具类
 * @author zhangyk
 * @date 2019/9/24 14:26
 */
@Slf4j
public class DateUtils {


    private static final String ALL_DATE_TIME = "yyyy-MM-dd HH:mm:ss";

    private static final String ALL_DATE = "yyyy-MM-dd";

    private static final String ALL_BANK_DATE_TIME = "yyyyMMddHHmmss";

    private static final String TL_QUERY_DATE = "MMdd";

    private static final String ALL_DATE_CN = "yyyy年MM月dd日";

    private static Pattern patternTime = Pattern.compile("\\d{2}:\\d{2}:\\d{2}");

    private static Pattern patternDate = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(ALL_DATE_TIME);

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(ALL_DATE);

    public static final DateTimeFormatter  BANK_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(ALL_BANK_DATE_TIME);

    public static final DateTimeFormatter  TL_QUERY_DATE_FORMATTER = DateTimeFormatter.ofPattern(TL_QUERY_DATE);

    public static final DateTimeFormatter DATE_FORMATTER_CN = DateTimeFormatter.ofPattern(ALL_DATE_CN);

    /**
     * localDateTime转字符串
     * @author zhangyk
     * @date 2019/9/23 16:59
     */
    public static String localDateTime2String(LocalDateTime localDateTime, DateTimeFormatter  format) {
        return localDateTime.format(format);
    }

    /**
     * string转localDateTime
     * @author zhangyk
     * @date 2019/9/23 17:01
     */
    public static LocalDateTime string2LocalDateTime(String time, DateTimeFormatter  format) {
        return LocalDateTime.parse(time, format);
    }

    /**
     * localDateTime转date
     * @author zhangyk
     * @date 2019/9/23 16:52
     */
    public static Date localDateTime2Date(LocalDateTime localDateTime){
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zoneId).toInstant();
        Date date = Date.from(instant);
        return date;
    }

    /**
     * date转localDateTime
     * @author zhangyk
     * @date 2019/9/23 17:28
     */
    public static LocalDateTime date2LocalDateTime(Date date){
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDateTime();
    }

    /**
     * localDateTime 转 localDate
     * @author zhangyk
     * @date 2019/9/24 14:49
     */
    public static LocalDate localDateTime2LocalDate(LocalDateTime localDateTime){
        return  localDateTime.toLocalDate();
    }

    /**
     * localDateTime转时间戳
     * @author zhangyk
     * @date 2019/9/23 17:08
     */
    public static Long localDateTime2Timestamp(LocalDateTime localDateTime){
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return instant.toEpochMilli();

    }

    /**
     * timestamp 转localDateTime
     * @author zhangyk
     * @date 2019/9/23 17:11
     */
    public static LocalDateTime timestamp2LocalDateTime(Long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    /**
     * 日期增加days天
     * @author zhangyk
     * @date 2019/9/23 18:36
     */
    public static LocalDate plusDays(LocalDate localDate, int days){
        return localDate.plusDays(days);
    }

    /**
     * 日期减少days天
     * @author zhangyk
     * @date 2019/9/23 18:36
     */
    public static LocalDate minusDays(LocalDate localDate, int days){
        return localDate.minusDays(days);
    }


    /**
     * 两个日期时间相差数
     * @author zhangyk
     * @date 2019/9/23 18:42
     */
    public static Long dayDuration(LocalDateTime begin, LocalDateTime end){
        return Duration.between(begin,end).toDays();
    }

    /**
     * 两个日期时间相差数
     * @param a
     * @param b
     * @return
     */
    public static Long between_days(String a, String b) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");// 自定义时间格式
        Calendar calendar_a = Calendar.getInstance();// 获取日历对象
        Calendar calendar_b = Calendar.getInstance();
        Date date_a = null;
        Date date_b = null;
        try {
            date_a = simpleDateFormat.parse(a);//字符串转Date
            date_b = simpleDateFormat.parse(b);
            calendar_a.setTime(date_a);// 设置日历
            calendar_b.setTime(date_b);
        } catch (ParseException e) {
            e.printStackTrace();//格式化异常
        }
        long time_a = calendar_a.getTimeInMillis();
        long time_b = calendar_b.getTimeInMillis();
        long between_days = (time_b - time_a) / (1000 * 3600 * 24);//计算相差天数
        return between_days;
    }

    /**
     * 两个日期时间相差数(如果小于0则返0)
     * @author wangjun
     * @date 2019/9/23 18:42
     */
    public static int dayDurationToZero(LocalDateTime begin, LocalDateTime end){
        int remaining = dayDuration(begin, end).intValue();
        return remaining < 0 ? 0 : remaining;
    }

    /**
     * 车位回购剩余天数(根据购买订单时记录的天数计算)
     * @author wangjun
     * @date 2020/04/02 18:42
     */
    public static int remainingDays(LocalDateTime firstTime, int days){
        int remaining = dayDuration(LocalDate.now().atStartOfDay(),firstTime.plusDays(days).toLocalDate().atStartOfDay()).intValue();
        return remaining<0?0:remaining;
    }

    /**
     * 过去的某一时间点与现在相差的天数
     * @author sunpeikai
     * @date 2020/04/09 17:19
     */
    public static int pastBetweenNow(LocalDateTime pastDateTime){
        int result = 0;
        if(pastDateTime.isBefore(LocalDateTime.now())){
            result = dayDuration(pastDateTime.toLocalDate().atStartOfDay(),LocalDateTime.now()).intValue();
        }
        return result;
    }

    /**
     * 两个日期时间相差小时
     * @author zhangyk
     * @date 2019/9/25 11:36
     */
    public static Long hourDuration(LocalDateTime begin, LocalDateTime end){
        return Duration.between(begin,end).toHours();
    }

    /**
     * 两个日期相差年数(计算年龄)
     * @author zhangyk
     * @date 2019/9/23 18:40
     */
    public static int yearDuration(LocalDate begin, LocalDate end){
        return begin.until(end).getYears();
    }

    /**
     * 某一天的0时0分0秒
     * @author zhangyk
     * @date 2019/9/23 19:02
     */
    public static LocalDateTime startOfDay(LocalDate localDate){
        return localDate.atStartOfDay();
    }

    // 比较日期时间早于和晚于 有 isAfter() 和 isBefore() 可以直接使用 不在封装

    /**
     * 适当校验日期格式(无法排除 0000-00-00)
     * @author zhangyk
     * @date 2019/9/27 11:39
     */
    public static Boolean isDate(String dateStr){
        return patternDate.matcher(dateStr).matches();
    }

    /**
     * 适当校验time格式(无法排除 00:00:00)
     * @author zhangyk
     * @date 2019/9/27 11:40
     */
    public static boolean isTime(String timeStr){
        return patternTime.matcher(timeStr).matches();
    }

    /**
     * LocalDateTime → yyyy-MM-dd HH:mm:ss
     * @param dateTime
     * @return String yyyy-MM-dd HH:mm:ss
     */
    public static String format(LocalDateTime dateTime){
        return dateTime.format(DATE_TIME_FORMATTER);
    }

    /**
     * LocalDateTime → yyyy-MM-dd
     * @param date
     * @return String yyyy-MM-dd
     */
    public static String format(LocalDate date){
        return date.format(DATE_FORMATTER);
    }

    /**
     * LocalDateTime → yyyy-MM-dd
     * @param date
     * @return String yyyy-MM-dd
     */
    public static String getTlQueryDate(LocalDateTime date){
        return date.format(TL_QUERY_DATE_FORMATTER);
    }

    public static String formatCN(LocalDate date){
        return date.format(DATE_FORMATTER_CN);
    }

    public static String formatCN(LocalDateTime date){
        return date.format(DATE_FORMATTER_CN);
    }

    /**
     * 返回一年后中文格式
     * @param date
     * @return
     */
    public static String formatCNYearLater(LocalDateTime date){
        LocalDateTime twoYear = date.plusYears(1).plusDays(-1);
        return twoYear.format(DATE_FORMATTER_CN);
    }

    /**
     * localDateTime转日期
     * @param localDateTime
     * @return
     */
    public static String localDateTime2Second(LocalDateTime localDateTime){
        return localDateTime.format(BANK_DATE_TIME_FORMATTER);
    }

    /**取上月第一天*/
    public static String lastDayOfMonth(){
        LocalDateTime date = LocalDateTime.now().minusMonths(1);
        LocalDateTime lastDay = date.with(TemporalAdjusters.lastDayOfMonth());
        return lastDay.format(DATE_FORMATTER);
    }

    /**取上月最后一天*/
    public static String firstdayOfMonth(){
        LocalDateTime date = LocalDateTime.now().minusMonths(1);
        LocalDateTime firstday = date.with(TemporalAdjusters.firstDayOfMonth());
        return firstday.format(DATE_FORMATTER);
    }

}
