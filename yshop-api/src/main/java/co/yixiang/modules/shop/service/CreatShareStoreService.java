/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package co.yixiang.modules.shop.service;

import java.awt.*;
import java.io.IOException;

/**
 * @author zhangqingqing
 * @version CreatShareStoreService, v0.1 2020/8/25 17:01
 */
public interface CreatShareStoreService {
    String creatProductPic(Integer id, String shareCode, String spreadPicName, String spreadPicPath, String apiUrl) throws IOException, FontFormatException;
}
