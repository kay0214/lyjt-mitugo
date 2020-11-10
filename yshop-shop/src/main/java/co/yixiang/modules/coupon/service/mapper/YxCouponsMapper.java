/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.coupon.service.mapper;

import co.yixiang.common.mapper.CoreMapper;
import co.yixiang.modules.coupon.domain.YxCoupons;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @author huiy
 * @date 2020-08-14
 */
@Repository
@Mapper
public interface YxCouponsMapper extends CoreMapper<YxCoupons> {

    /**
     * 库存恢复
     *
     * @param couponId
     * @param totalNum
     * @return
     */
    @Update("update yx_coupons set `sales` = `sales` - #{totalNum},`inventory` = `inventory` + #{totalNum} where id = #{couponId}")
    int updateMinusSales(@Param("couponId") Integer couponId, @Param("totalNum") Integer totalNum);

    /**
     * 根据orderid获取合同模板id
     * @param orderId
     * @return
     */
    @Select("SELECT cou.temp_id FROM yx_coupons cou LEFT JOIN yx_coupon_order couord ON cou.id = couord.coupon_id " +
            "WHERE couord.id =  #{orderId}")
    int getTempIdByOrderId(@Param("orderId") Integer orderId);
}
