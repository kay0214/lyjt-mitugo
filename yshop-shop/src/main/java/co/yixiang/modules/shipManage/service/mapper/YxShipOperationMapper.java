/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.shipManage.service.mapper;

import co.yixiang.common.mapper.CoreMapper;
import co.yixiang.modules.shipManage.domain.YxShipOperation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * @author nxl
 * @date 2020-11-05
 */
@Repository
@Mapper
public interface YxShipOperationMapper extends CoreMapper<YxShipOperation> {
    @Select("<script> SELECT" +
            " SUM(yco.total_price) AS totlePricet" +
            " FROM " +
            " yx_ship_operation_detail ysod" +
            " LEFT JOIN yx_coupon_order yco ON ysod.coupon_order_id = yco.id" +
            " WHERE " +
            " ysod.batch_no =#{batchNo}; " +
            "</script>")
    BigDecimal getBatchTotlePrice( @Param("batchNo") String batchNo);
}
