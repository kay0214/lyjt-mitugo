/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package co.yixiang.utils;

import org.springframework.util.DigestUtils;

/**
 * @author sss
 * @version PassWordUtil, v0.1 2020/9/7 12:01
 */
public class PassWordUtil {

    public static String getUserPassWord(String pass,int roles,String username){

        return DigestUtils.md5DigestAsHex((pass+roles+username).getBytes());

    }

}
