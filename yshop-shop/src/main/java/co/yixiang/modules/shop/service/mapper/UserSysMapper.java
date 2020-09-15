/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.shop.service.mapper;

import co.yixiang.common.mapper.CoreMapper;
import co.yixiang.modules.shop.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * @author zhangyk
 * @date 2020-08-15
 */
@Repository
@Mapper
public interface UserSysMapper extends CoreMapper<User> {

    /**
     * 更新商户可提现金额
     *
     * @param id
     * @param withdrawalAmount
     */
    @Update("update `user` set withdrawal_amount = withdrawal_amount + #{withdrawalAmount} where id = #{id}")
    void updateWithdrawalAmount(@Param("id") Integer id, @Param("withdrawalAmount") BigDecimal withdrawalAmount);

    /**
     * 更新商户可提现金额
     * @param id
     * @param extractPrice
     */
    @Update("update `user` set withdrawal_amount = withdrawal_amount - #{extractPrice} where id = #{id}")
    void updateWithdrawalAmountSub(@Param("id")Long id, @Param("extractPrice")BigDecimal extractPrice);
}
