/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co

 */
package co.yixiang.tools.utils;

import co.yixiang.utils.FileUtil;
import com.qiniu.storage.Region;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 七牛云存储工具类
 * @author hupeng
 * @date 2018-12-31
 */
public class QiNiuUtil {

    private static final String HUAD = "华东";

    private static final String HUAB = "华北";

    private static final String HUAN = "华南";

    private static final String BEIM = "北美";

    /**
     * 得到机房的对应关系
     * @param zone 机房名称
     * @return Region
     */
    public static Region getRegion(String zone){

        if(HUAD.equals(zone)){
            return Region.huadong();
        } else if(HUAB.equals(zone)){
            return Region.huabei();
        } else if(HUAN.equals(zone)){
            return Region.huanan();
        } else if (BEIM.equals(zone)){
            return Region.beimei();
            // 否则就是东南亚
        } else {
            return Region.qvmHuadong();
        }
    }

    /**
     * 默认不指定key的情况下，以文件内容的hash值作为文件名
     * @param file 文件名
     * @return String
     */
    public static String getKey(String file){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        return FileUtil.getFileNameNoEx(file) + "-" +
                sdf.format(date) +
                "." +
                FileUtil.getExtensionName(file);
    }



    /**
     * 上传文件重命名
     * @return 新的文件名
     */
    public static String fileRename() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String time = sdf.format(new Date());
        StringBuffer buf = new StringBuffer(time);
        Random r = new Random();
        //循环取得三个不大于10的随机整数
        for (int x = 0; x < 3; x++) {
            buf.append(r.nextInt(10));
        }
        return buf.toString();
    }

}
