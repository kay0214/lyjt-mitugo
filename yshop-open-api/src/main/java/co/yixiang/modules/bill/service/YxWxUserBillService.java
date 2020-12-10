package co.yixiang.modules.bill.service;

import co.yixiang.modules.bill.entity.YxWxUserBill;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.bill.web.param.YxWxUserBillQueryParam;
import co.yixiang.modules.bill.web.vo.YxWxUserBillQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;

/**
 * <p>
 * 用户账单明细表 服务类
 * </p>
 *
 * @author zqq
 * @since 2020-12-10
 */
public interface YxWxUserBillService extends BaseService<YxWxUserBill> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxWxUserBillQueryVo getYxWxUserBillById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxWxUserBillQueryParam
     * @return
     */
    Paging<YxWxUserBillQueryVo> getYxWxUserBillPageList(YxWxUserBillQueryParam yxWxUserBillQueryParam) throws Exception;

}
