/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.user.service;

import co.yixiang.common.service.BaseService;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.user.entity.YxUserRecharge;
import co.yixiang.modules.user.web.param.RechargeParam;
import co.yixiang.modules.user.web.param.YxUserRechargeQueryParam;
import co.yixiang.modules.user.web.vo.YxUserRechargeQueryVo;

import java.io.Serializable;

/**
 * <p>
 * 用户充值表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2019-12-08
 */
public interface YxUserRechargeService extends BaseService<YxUserRecharge> {

    void updateRecharge(YxUserRecharge userRecharge);

    YxUserRecharge getInfoByOrderId(String orderId);

    void addRecharge(RechargeParam param,int uid);


    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxUserRechargeQueryVo getYxUserRechargeById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxUserRechargeQueryParam
     * @return
     */
    Paging<YxUserRechargeQueryVo> getYxUserRechargePageList(YxUserRechargeQueryParam yxUserRechargeQueryParam) throws Exception;

}
