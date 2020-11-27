/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package co.yixiang.common.util;

import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.AlgorithmParameters;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信工具类
 *
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
     * 获取用户ACCESS_TOKEN
     *
     * @author zhangyk
     * @date 2020/8/18 14:34
     */
    public static String getAccessToken(String appId, String appSecret) {
        String requestUrl = ACCESS_TOKEN_URL.replace("{appId}", appId).replace("{appSecret}", appSecret);
        String res = HttpUtils.sendGet(requestUrl.substring(0,requestUrl.indexOf("?")), requestUrl.substring(requestUrl.indexOf("?")+1));
        log.info(">>>>>>微信api获取accessToken响应:{}<<<<<<", res);
        JSONObject resObject = JSONObject.parseObject(res);
        String accessToken=resObject.getString("access_token");
        return accessToken;
    }



    /**
     * 获取用户opneId和sessionKey等
     *
     * @param code 前端提供code
     * @author zhangyk
     * @date 2020/8/18 14:34
     */
    public static JSONObject getUserInfo(String code, String appId, String appSecret) {
        String requestUrl = CODE_2_SESSION_URL.replace("{appId}", appId).replace("{appSecret}", appSecret).replace("{code}", code);
        String res = HttpUtils.sendGet(requestUrl, null);
        log.info(">>>>>>微信api获取opneId响应:{}<<<<<<", res);
        JSONObject resObject = JSONObject.parseObject(res);
        return resObject;
    }


    /**
     * 解密手机号信息
     *
     * @param encryptedData 前端提供加密串
     * @param iv            前端提供的加密向量
     * @author zhangyk
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
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, "UTF-8");
                return JSONObject.parseObject(result);
            }
        } catch (Exception e) {
            log.error("解密手机号信息失败", e);
        }
        return null;
    }

    public static void getQrCode(String accessToken,String filePath,String scene) {
        RestTemplate rest = new RestTemplate();
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + accessToken;
            Map<String, Object> param = new HashMap<>();
            param.put("scene", scene);
            Map<String, Object> line_color = new HashMap<>();
            line_color.put("r", 0);
            line_color.put("g", 0);
            line_color.put("b", 0);
            param.put("line_color", line_color);
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            HttpEntity requestEntity = new HttpEntity(JSON.toJSONString(param), headers);
            ResponseEntity<byte[]> entity = rest.exchange(url, HttpMethod.POST, requestEntity, byte[].class, new Object[0]);
            byte[] result = entity.getBody();
            inputStream = new ByteArrayInputStream(result);

            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            outputStream = new FileOutputStream(file);
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = inputStream.read(buf, 0, 1024)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.flush();
        } catch (Exception e) {
            log.error("调用小程序生成微信永久小程序码URL接口异常", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
