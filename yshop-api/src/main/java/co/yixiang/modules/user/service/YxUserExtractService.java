/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.user.service;

import co.yixiang.common.service.BaseService;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.pay.param.PaySeachParam;
import co.yixiang.modules.user.entity.YxUserExtract;
import co.yixiang.modules.user.web.param.UserExtParam;
import co.yixiang.modules.user.web.param.YxUserExtractQueryParam;
import co.yixiang.modules.user.web.vo.YxUserExtractQueryVo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 用户提现表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2019-11-11
 */
public interface YxUserExtractService extends BaseService<YxUserExtract> {

    void userExtract(int uid, UserExtParam param);

    BigDecimal extractSum(int uid);

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxUserExtractQueryVo getYxUserExtractById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxUserExtractQueryParam
     * @return
     */
    Paging<YxUserExtractQueryVo> getYxUserExtractPageList(YxUserExtractQueryParam yxUserExtractQueryParam) throws Exception;

    /**
     * 确认提现信息
     * @param param
     */
    YxUserExtract getConfirmOrder(PaySeachParam param);
}
