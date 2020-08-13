package co.yixiang.modules.funds.service;

import co.yixiang.modules.funds.entity.YxFundsAccount;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.funds.web.param.YxFundsAccountQueryParam;
import co.yixiang.modules.funds.web.vo.YxFundsAccountQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;

/**
 * <p>
 * 平台账户表 服务类
 * </p>
 *
 * @author zqq
 * @since 2020-08-13
 */
public interface YxFundsAccountService extends BaseService<YxFundsAccount> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxFundsAccountQueryVo getYxFundsAccountById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxFundsAccountQueryParam
     * @return
     */
    Paging<YxFundsAccountQueryVo> getYxFundsAccountPageList(YxFundsAccountQueryParam yxFundsAccountQueryParam) throws Exception;

}
