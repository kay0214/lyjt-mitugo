package co.yixiang.modules.bank.service;

import co.yixiang.modules.bank.entity.BankCodeReg;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.bank.web.param.BankCodeRegQueryParam;
import co.yixiang.modules.bank.web.vo.BankCodeRegQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 联行号地区代码 服务类
 * </p>
 *
 * @author sss
 * @since 2020-09-17
 */
public interface BankCodeRegService extends BaseService<BankCodeReg> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    BankCodeRegQueryVo getBankCodeRegById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param bankCodeRegQueryParam
     * @return
     */
    Paging<BankCodeRegQueryVo> getBankCodeRegPageList(BankCodeRegQueryParam bankCodeRegQueryParam) throws Exception;

    /**
     * 查询所有的省份
     * @return
     */
    List<String> getAllProvinces();

    /**
     * 查询所有市
     * @param name
     * @return
     */
    List<String> getAllCitys(String name);
}
