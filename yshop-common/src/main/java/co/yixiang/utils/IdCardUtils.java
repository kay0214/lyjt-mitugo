/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package co.yixiang.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author lsy
 * @version IdentityCardUtils, v0.1 2019/4/24 10:27
 * @description
 */
@Slf4j
public class IdCardUtils {

    /**
     * 省、直辖市代码表：
     * 11 : 北京  12 : 天津  13 : 河北   14 : 山西  15 : 内蒙古
     * 21 : 辽宁  22 : 吉林  23 : 黑龙江 31 : 上海  32 : 江苏
     * 33 : 浙江  34 : 安徽  35 : 福建   36 : 江西  37 : 山东
     * 41 : 河南  42 : 湖北  43 : 湖南   44 : 广东  45 : 广西  46 : 海南
     * 50 : 重庆  51 : 四川  52 : 贵州   53 : 云南  54 : 西藏
     * 61 : 陕西  62 : 甘肃  63 : 青海   64 : 宁夏  65 : 新疆
     * 71 : 台湾
     * 81 : 香港  82 : 澳门
     * 91 : 国外
     */
    final static String CITY_CODE[] = {"11", "12", "13", "14", "15", "21", "22", "23", "31", "32", "33", "34", "35", "36", "37", "41", "42", "43", "44", "45", "46", "50", "51", "52", "53", "54", "61", "62", "63", "64", "65", "71", "81", "82", "91"};

    /**
     * 效验码
     */
    final static char[] VERIFYCODE = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};

    /**
     * 加权因子
     * Math.pow(2,  i - 1) % 11
     */
    final static int[] WI = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

    /**
     * 根据15位的身份证号码获得18位身份证号码
     *
     * @param fifteenIDCard 15位的身份证号码
     * @return 升级后的18位身份证号码
     * @throws Exception 如果不是15位的身份证号码，则抛出异常
     */
    public static String getEighteenIDCard(String fifteenIDCard) throws Exception {
        if (fifteenIDCard != null && fifteenIDCard.length() == 15) {
            StringBuilder sb = new StringBuilder();
            sb.append(fifteenIDCard.substring(0, 6))
                    .append("19")
                    .append(fifteenIDCard.substring(6));
            sb.append(getVerifyCode(sb.toString()));
            return sb.toString();
        } else {
            throw new Exception("不是15位的身份证");
        }
    }

    /**
     * 获取校验码
     *
     * @param idCardNumber 不带校验位的身份证号码（17位）
     * @return 校验码
     * @throws Exception 如果身份证没有加上19，则抛出异常
     */
    private static char getVerifyCode(String idCardNumber) throws Exception {
        if (idCardNumber == null || idCardNumber.length() < 17) {
            throw new Exception("不合法的身份证号码");
        }
        char[] Ai = idCardNumber.toCharArray();
        int S = 0;
        int Y;
        for (int i = 0; i < WI.length; i++) {
            S += (Ai[i] - '0') * WI[i];
        }
        Y = S % 11;
        return VERIFYCODE[Y];
    }

    /**
     * 校验18位的身份证号码的校验位是否正确
     *
     * @param idCardNumber 18位的身份证号码
     * @return
     * @throws Exception
     */
    public static boolean verify(String idCardNumber) throws Exception {
        if (idCardNumber == null || idCardNumber.length() != 18) {
            throw new Exception("不是18位的身份证号码");
        }
        return getVerifyCode(idCardNumber) == idCardNumber.charAt(idCardNumber.length() - 1);
    }

    /**
     * 方法描述: 校验身份证号码是否有效
     *
     * @param
     * @return
     */
    public final static boolean isValid(String id) {
        if (id == null) {
            return false;
        }

        int len = id.length();
        if (len != 15 && len != 18) {
            return false;
        }

        //校验区位码
        if (!validCityCode(id.substring(0, 2))) {
            return false;
        }
        //校验生日
        if (!validDate(id)) {
            return false;
        }
        if (len == 15) {
            return true;
        }
        //校验位数
        return validParityBit(id);
    }

    private static boolean validParityBit(String id) {
        char[] cs = id.toUpperCase().toCharArray();
        int power = 0;
        for (int i = 0; i < cs.length; i++) {
            //最后一位可以是X
            if (i == cs.length - 1 && cs[i] == 'X') {
                break;
            }
            // 非数字
            if (cs[i] < '0' || cs[i] > '9') {
                return false;
            }
            // 加权求和
            if (i < cs.length - 1) {
                power += (cs[i] - '0') * WI[i];
            }
        }
        return VERIFYCODE[power % 11] == cs[cs.length - 1];
    }

    private static boolean validDate(String id) {
        try {
            String birth = id.length() == 15 ? "19" + id.substring(6, 12) : id.substring(6, 14);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date birthDate = sdf.parse(birth);
            if (!birth.equals(sdf.format(birthDate))) {
                return false;
            }
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    private static boolean validCityCode(String cityCode) {
        for (String code : CITY_CODE) {
            if (code.equals(cityCode)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param
     * @return
     * @description 根据身份证号获取性别 0女 1男
     * @auth lsy
     */
    public static Integer getSexByNo(String cardNo) {
        try {
            String no = "";
            if (cardNo.length() < 18) {
                no = getEighteenIDCard(cardNo);
            } else {
                no = cardNo;
            }
            Integer sex = Integer.valueOf(no.charAt(16));
            return (sex % 2 == 0) ? 0 : 1;
        } catch (Exception e) {
            log.error("身份证号输入有误,card:[{}]", cardNo);
            return -1;
        }
    }

    /**
     * 通过身份证号获取出生日期(LocalDate)
     *
     * @param idCard
     * @return
     */
    public static LocalDate getBirthByIdCard(String idCard) {
        if (idCard.length() != 18) {
            throw new RuntimeException("不是有效长度的身份证号码");
        }

        int year = Integer.parseInt(idCard.substring(6, 10));// 截取年
        int month = Integer.parseInt(idCard.substring(10, 12));// 截取月份
        int day = Integer.parseInt(idCard.substring(12, 14));// 截取天
        // 出生日期
        return LocalDate.of(year, month, day);
    }
//    /**
//     * 通过身份证号获取出生日期(yyyy-MM-dd格式)
//     *
//     * @param idCard
//     * @return
//     */
//    public static String getBirthStrByIdCard(String idCard) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateUtils.YYYY_MM_DD);//test?
//        return getBirthByIdCard(idCard).format(formatter);
//    }
//
//    /**
//     * 通过身份证号获取年龄
//     *
//     * @param idCard
//     * @return
//     */
//    public static int getAgeByIdCard(String idCard) {
//        return DateUtils.getAge(getBirthByIdCard(idCard));
//    }
//
//    /**
//     * 通过身份证号获取出生天数
//     *
//     * @param idCard
//     * @return
//     */
//    public static int getBirthDaysByIdCard(String idCard) {
//        return DateUtils.getBirthDays(getBirthByIdCard(idCard));
//    }
}
