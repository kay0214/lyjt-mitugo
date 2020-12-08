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
import co.yixiang.modules.shop.domain.User;
import co.yixiang.modules.shop.service.dto.UserDto;
import co.yixiang.modules.shop.service.dto.UserQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author zhangyk
 * @date 2020-08-15
 */
public interface UserService extends BaseService<User> {

    /**
     * 查询数据分页
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String , Object>
     */
    Map<String, Object> queryAll(UserQueryCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     * @param criteria 条件参数
     * @return List<UserDto>
     */
    List<User> queryAll(UserQueryCriteria criteria);

    /**
     * 导出数据
     * @param all 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<UserDto> all, HttpServletResponse response) throws IOException;

    /**
     * 用户提现
     *
     * @param uid
     * @param userType
     * @param extractPrice
     * @return
     */
    Integer updateUserWithdraw(Integer uid, Integer userType, BigDecimal extractPrice);

    /**
     * 更新商户提现金额
     *
     * @param id
     * @param withdrawalAmount
     */
    void updateWithdrawalAmount(Integer id, BigDecimal withdrawalAmount);

    /**
     * 增加商户金额
     * @param id
     * @param money
     */
    void updateAddAmount(Long id, BigDecimal money);

    /**
     * 减少商户总金额
     * @param id
     * @param money
     */
    void updateAmountSub(Long id, BigDecimal money);

    /**
     * 减少商户的可提现金额
     *
     * @param id
     * @param withdrawalAmount
     */
    void updateWithdrawalAmountSub(int id, BigDecimal withdrawalAmount);

    Integer getRoleIdByUserId(int userId);
}
