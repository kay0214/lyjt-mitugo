package co.yixiang.modules.user.service;

import co.yixiang.common.service.BaseService;
import co.yixiang.common.web.param.QueryParam;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.couponUse.param.UserAccountQueryParam;
import co.yixiang.modules.order.web.vo.YxStoreOrderQueryVo;
import co.yixiang.modules.user.entity.YxUserBill;
import co.yixiang.modules.user.web.dto.BillDTO;
import co.yixiang.modules.user.web.param.YxUserBillQueryParam;
import co.yixiang.modules.user.web.vo.YxUserBillQueryVo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户账单表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
public interface YxUserBillService extends BaseService<YxUserBill> {

    int cumulativeAttendance(int uid);

    Map<String, Object> spreadOrder(int uid, int page, int limit);

    List<BillDTO> getUserBillList(int page, int limit, int uid, int type);

    double getBrokerage(int uid);

    BigDecimal yesterdayCommissionSum(int uid);

    BigDecimal todayCommissionSum(int uid);

    /**
     * 计算累计收益
     *
     * @param uid
     * @return
     */
    BigDecimal totalCommissionSum(int uid);

    List<YxUserBillQueryVo> userBillList(int uid, int page, int limit, String category);

    /**
     * 根据ID获取查询对象
     *
     * @param id
     * @return
     */
    YxUserBillQueryVo getYxUserBillById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     *
     * @param yxUserBillQueryParam
     * @return
     */
    Paging<YxUserBillQueryVo> getYxUserBillPageList(YxUserBillQueryParam yxUserBillQueryParam) throws Exception;

    /**
     * 确认收货保存商户资金和可用资金
     *
     * @param order
     */
    void saveMerchantsBill(YxStoreOrderQueryVo order);

    /**
     * 查询商户的线下交易流水列表
     * @param
     * @return
     */
    Paging<YxUserBillQueryVo> getYxUserAccountPageList(UserAccountQueryParam param, Long userId);

    /**
     * 查询商户的线上交易流水列表
     * @param param
     * @param id
     * @return
     */
    Paging<YxUserBillQueryVo> getUserProductAccountList(UserAccountQueryParam param, Long id);
}
