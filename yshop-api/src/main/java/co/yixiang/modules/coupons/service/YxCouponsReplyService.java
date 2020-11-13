package co.yixiang.modules.coupons.service;

import co.yixiang.modules.coupons.entity.YxCouponsReply;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.coupons.web.param.YxCouponsReplyQueryParam;
import co.yixiang.modules.coupons.web.vo.YxCouponsReplyQueryVo;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.coupons.web.vo.couponReply.addReply.YxCouponsAddReplyRequest;
import co.yixiang.modules.coupons.web.vo.couponReply.queryReply.YxCouponsReplyDetailVO;
import co.yixiang.modules.coupons.web.vo.couponReply.queryReply.YxCouponsReplyVO;

import java.io.Serializable;

/**
 * <p>
 * 本地生活评论表 服务类
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
public interface YxCouponsReplyService extends BaseService<YxCouponsReply> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxCouponsReplyQueryVo getYxCouponsReplyById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxCouponsReplyQueryParam
     * @return
     */
    Paging<YxCouponsReplyQueryVo> getYxCouponsReplyPageList(YxCouponsReplyQueryParam yxCouponsReplyQueryParam) throws Exception;

    /**
     * 提交卡券评价
     *
     * @param request
     * @return
     */
    boolean createReply(YxCouponsAddReplyRequest request);

    /**
     * 处理评价数据
     *
     * @param yxCouponsReply
     * @return
     */
    YxCouponsReplyVO convertCouponsReply(YxCouponsReply yxCouponsReply);

    YxCouponsReplyDetailVO getReplyDetailList(YxCouponsReplyQueryParam param);
}
