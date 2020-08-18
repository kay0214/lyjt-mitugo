/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.shop.service;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.shop.domain.YxStoreInfo;
import co.yixiang.modules.shop.domain.YxStoreInfoRequest;
import co.yixiang.modules.shop.domain.YxStoreInfoResponse;
import co.yixiang.modules.shop.service.dto.YxStoreInfoDto;
import co.yixiang.modules.shop.service.dto.YxStoreInfoQueryCriteria;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author nxl
* @date 2020-08-14
*/
public interface YxStoreInfoService  extends BaseService<YxStoreInfo>{

/**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(YxStoreInfoQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<YxStoreInfoDto>
    */
    List<YxStoreInfo> queryAll(YxStoreInfoQueryCriteria criteria);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<YxStoreInfoDto> all, HttpServletResponse response) throws IOException;

    /**
     * 更新店铺信息
     * @param request
     * @return
     */
    boolean updateStoreInfo(YxStoreInfoRequest request);

    void onSale(Integer id, int status);

    YxStoreInfoResponse getStoreInfo(int storeId);

    void updateDelFlg(Integer id);
}
