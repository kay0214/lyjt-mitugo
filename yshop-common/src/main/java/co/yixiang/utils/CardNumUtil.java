/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package co.yixiang.utils;

/**
 * @author PC-sss
 */
public class CardNumUtil {
    // 手机号码前三后四脱敏
    public static String mobileEncrypt(String mobile) {
        if (StringUtils.isEmpty(mobile) || (mobile.length() != 11)) {
            return mobile;
        }
        return mobile.replaceAll("(\\d{4})\\d{3}(\\d{4})", "$1 ** $2");
    }

    //身份证前三后四脱敏
    public static String idEncrypt(String id) {
        if (StringUtils.isEmpty(id) || (id.length() < 8)) {
            return id;
        }// (?<=\w{3})\w(?=\w{4})
        return id.replaceAll("(\\d{4})\\d{10}(\\d{4})", "$1 ** $2");
    }

    public static void main(String[] args) {
        System.out.println(idEncrypt("370282199106045339"));
    }

    //护照前2后3位脱敏，护照一般为8或9位
    public static String idPassport(String id) {
        if (StringUtils.isEmpty(id) || (id.length() < 8)) {
            return id;
        }
        return id.substring(0, 2) + new String(new char[id.length() - 5]).replace("\0", "*") + id.substring(id.length() - 3);
    }

    // 姓名脱敏
    public static String nameEncrypt(String name){
        if(name==null || name.isEmpty()){
            return "";
        }
        String myName = null;
        char[] chars = name.toCharArray();
        if(chars.length==1){
            myName=name;
        }
        if(chars.length==2){
            myName=name.replaceFirst(name.substring(1), "*");
        }
        if(chars.length>2){
            myName=name.replaceAll(name.substring(1, chars.length-1), "*");
        }
        return myName;
    }
}
