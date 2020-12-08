package co.yixiang.modules.user.service;

import co.yixiang.common.service.BaseService;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.coupons.web.param.YxCouponOrderPassengParam;
import co.yixiang.modules.user.entity.YxUsedContacts;
import co.yixiang.modules.user.web.dto.YxUsedContactsSaveDto;
import co.yixiang.modules.user.web.dto.YxUsedContactsUpdateDto;
import co.yixiang.modules.user.web.param.YxUsedContactsQueryParam;
import co.yixiang.modules.user.web.vo.YxUsedContactsQueryVo;

import java.io.Serializable;

/**
 * <p>
 * 常用联系人表 服务类
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
public interface YxUsedContactsService extends BaseService<YxUsedContacts> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxUsedContactsQueryVo getYxUsedContactsById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxUsedContactsQueryParam
     * @return
     */
    Paging<YxUsedContactsQueryVo> getYxUsedContactsPageList(YxUsedContactsQueryParam yxUsedContactsQueryParam) throws Exception;

    /**
     * 小程序端选择乘客
     * @param orderId
     * @param userCount
     * @return
     */
    Paging<YxUsedContactsQueryVo> getUsedContactsByUserId(YxCouponOrderPassengParam yxCouponOrderQueryParam);

    /**
     * 插入数据
     *
     * @param request
     * @return
     */
    boolean saveUsedContacts(YxUsedContactsSaveDto request);

    /**
     * 更新数据
     *
     * @param request
     * @return
     */
    boolean updateUsedContacts(YxUsedContactsUpdateDto request);
}
