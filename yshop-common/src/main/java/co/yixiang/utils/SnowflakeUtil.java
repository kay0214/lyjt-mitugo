/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package co.yixiang.utils;

import cn.hutool.core.util.IdUtil;

/**
 * 订单号生成工具类
 * @author sss
 * @version SnowflakeUtil, v0.1 2020/9/6 15:13
 */
public class SnowflakeUtil {

    private static Integer workId = 0;

    public static String getOrderId(Integer datacenterId) {
        String result = IdUtil.getSnowflake(workId, datacenterId).nextIdStr();
        workId++;
        if(workId>=30){
            workId = 0;
        }
        return result;
    }
}
