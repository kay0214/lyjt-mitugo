package co.yixiang.modules.bank.service;

import co.yixiang.modules.bank.entity.BankCode;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.bank.web.param.BankCodeQueryParam;
import co.yixiang.modules.bank.web.vo.BankCodeQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;

/**
 * <p>
 * 联行号表 服务类
 * </p>
 *
 * @author sss
 * @since 2020-09-17
 */
public interface BankCodeService extends BaseService<BankCode> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    BankCodeQueryVo getBankCodeById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param bankCodeQueryParam
     * @return
     */
    Paging<BankCodeQueryVo> getBankCodePageList(BankCodeQueryParam bankCodeQueryParam) throws Exception;

}
