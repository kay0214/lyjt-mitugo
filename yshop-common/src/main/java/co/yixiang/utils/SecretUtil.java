package co.yixiang.utils;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Base64;
import java.util.Random;
import java.util.UUID;

/**
 * 随机字符串工具类
 *
 * @author zhangyk
 * @date 2020/7/15 16:44
 */
public class SecretUtil {

    private static String RANDOM_SEED = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static String RANDOM_INT_SEED = "0123456789";


    /**
     * 指定位数的随机字符串
     *
     * @param length 密码位数，不足8位则置为8位
     */
    public static String createRandomStr(Integer length) {
        if (length == null || length <= 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int f = (int) (Math.random() * RANDOM_SEED.length());
            sb.append(RANDOM_SEED.charAt(f));
        }
        return sb.toString();
    }

    /**
     * 生成唯一标识符
     * 生成规则： uuid + 六位随机串
     */
    public static String createSign() {
        StringBuilder result = new StringBuilder(getRandomUUID());
        Random random = new Random();
        result.append(createRandomStr(6));
        return result.toString();
    }

    /**
     * 获取不包含中划线的uuid
     *
     * @author zhangyk
     * @date 2020/7/15 16:54
     */
    public static String getRandomUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }


}
