/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package co.yixiang.common.util;

import cn.hutool.core.codec.Base64;
import co.yixiang.enums.RedisKeyEnum;
import co.yixiang.utils.RedisUtils;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.framework.common.util.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.Arrays;

/**
 *  微信工具类
 * @author zhangyk
 * @date 2020/9/1 14:27
 */
@Slf4j
@Component
public class WxUtils {
    private static String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={appId}&secret={appSecret}";

    private static String CODE_2_SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session?appid={appId}&secret={appSecret}&js_code={code}&grant_type=authorization_code";

    private static final String ACCESS_TOKEN = "access_token";

    public static final String SESSION_KEY = "session_key";

    public static final String OPEN_ID = "openid";


    /**
     * 获取用户opneId和sessionKey等
     * @author zhangyk
     * @param code  前端提供code
     * @date 2020/8/18 14:34
     */
    public static JSONObject getUserInfo(String code,String appId,String appSecret) {
        String requestUrl = CODE_2_SESSION_URL.replace("{appId}",appId).replace("{appSecret}", appSecret).replace("{code}", code);
        String res = HttpUtils.sendGet(requestUrl, null);
        log.info(">>>>>>微信api获取opneId响应:{}<<<<<<", res);
        JSONObject resObject = JSONObject.parseObject(res);
        return resObject;
    }


    /**
     *  解密手机号信息
     * @author zhangyk
     * @param encryptedData  前端提供加密串
     * @param iv  前端提供的加密向量
     * @date 2020/8/18 14:41
     */
    public static JSONObject decryptPhoneData(String sessionKey, String encryptedData, String iv) {
        // 被加密的数据
        byte[] dataByte = Base64.decode(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.decode(sessionKey);
        // 偏移量
        byte[] ivByte = Base64.decode(iv);
        try {
            // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            byte[] resultByte;
            try {
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
                AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
                parameters.init(new IvParameterSpec(ivByte));
                cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
                resultByte = cipher.doFinal(dataByte);
            }catch (Exception e) {
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
                SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
                AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
                parameters.init(new IvParameterSpec(ivByte));
                cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
                resultByte = cipher.doFinal(dataByte);
            }
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, "UTF-8");
                return JSONObject.parseObject(result);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

}
