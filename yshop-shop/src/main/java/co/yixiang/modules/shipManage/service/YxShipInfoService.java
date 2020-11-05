/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.shipManage.service;

import co.yixiang.common.service.BaseService;
import co.yixiang.modules.shipManage.domain.YxShipInfo;
import co.yixiang.modules.shipManage.domain.YxShipInfoRequest;
import co.yixiang.modules.shipManage.domain.YxShipSeries;
import co.yixiang.modules.shipManage.service.dto.YxShipInfoDto;
import co.yixiang.modules.shipManage.service.dto.YxShipInfoQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
* @author nxl
* @date 2020-11-04
*/
public interface YxShipInfoService  extends BaseService<YxShipInfo>{

/**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(YxShipInfoQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<YxShipInfoDto>
    */
    List<YxShipInfo> queryAll(YxShipInfoQueryCriteria criteria);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<YxShipInfoDto> all, HttpServletResponse response) throws IOException;

    /**
     * 获取状态为启用的船只系列
     * @return
     */
    public List<YxShipSeries> getShipSeriseList();

    /**
     * 根据船只系列，以及商户id获取船只信息
     * @param seriseId
     * @param merId
     * @return
     */
    public List<YxShipInfo> getShipInfoList(int seriseId,int merId);

    /**
     * 保存船只信息
     * @param resources
     */
    boolean saveOrUpdShipInfoByParam(YxShipInfoRequest resources);
}
