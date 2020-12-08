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
import co.yixiang.modules.shipManage.domain.YxShipAppoint;
import co.yixiang.modules.shipManage.service.dto.YxShipAppointDto;
import co.yixiang.modules.shipManage.service.dto.YxShipAppointQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author nxl
* @date 2020-11-04
*/
public interface YxShipAppointService  extends BaseService<YxShipAppoint>{

/**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(YxShipAppointQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<YxShipAppointDto>
    */
    List<YxShipAppoint> queryAll(YxShipAppointQueryCriteria criteria);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<YxShipAppointDto> all, HttpServletResponse response) throws IOException;
}
