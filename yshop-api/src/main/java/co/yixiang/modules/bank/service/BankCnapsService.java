package co.yixiang.modules.bank.service;

import co.yixiang.modules.bank.entity.BankCnaps;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.bank.web.param.BankCnapsQueryParam;
import co.yixiang.modules.bank.web.vo.BankCnapsQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 银行机构编码 服务类
 * </p>
 *
 * @author sss
 * @since 2020-09-17
 */
public interface BankCnapsService extends BaseService<BankCnaps> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    BankCnapsQueryVo getBankCnapsById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param bankCnapsQueryParam
     * @return
     */
    Paging<BankCnapsQueryVo> getBankCnapsPageList(BankCnapsQueryParam bankCnapsQueryParam) throws Exception;

    /**
     * 查询所有信息
     * @return
     */
    List<BankCnaps> getAllCnaps();
}
