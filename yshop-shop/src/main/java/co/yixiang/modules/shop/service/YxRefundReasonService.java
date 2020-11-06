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
import co.yixiang.modules.shop.domain.YxRefundReason;
import co.yixiang.modules.shop.service.dto.YxRefundReasonDto;
import co.yixiang.modules.shop.service.dto.YxRefundReasonQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author nxl
* @date 2020-11-04
*/
public interface YxRefundReasonService  extends BaseService<YxRefundReason>{

/**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(YxRefundReasonQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<YxRefundReasonDto>
    */
    List<YxRefundReason> queryAll(YxRefundReasonQueryCriteria criteria);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<YxRefundReasonDto> all, HttpServletResponse response) throws IOException;

    /**
     * 根据主键查询未删除的数据
     *
     * @param id
     * @return
     */
    YxRefundReason selectById(Integer id);
}
