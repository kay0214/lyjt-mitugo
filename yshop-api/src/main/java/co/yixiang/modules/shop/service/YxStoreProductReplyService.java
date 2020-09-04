package co.yixiang.modules.shop.service;

import co.yixiang.common.service.BaseService;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.shop.entity.YxStoreProductReply;
import co.yixiang.modules.shop.web.dto.ReplyCountDTO;
import co.yixiang.modules.shop.web.param.YxStoreProductReplyQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreProductReplyQueryVo;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 评论表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
public interface YxStoreProductReplyService extends BaseService<YxStoreProductReply> {

    ReplyCountDTO getReplyCount(int productId);

    YxStoreProductReplyQueryVo handleReply(YxStoreProductReplyQueryVo replyQueryVo);

    YxStoreProductReplyQueryVo getReply(int productId);

    List<YxStoreProductReplyQueryVo> getReplyList(int productId, int type, int page, int limit);

    int getInfoCount(Integer oid, String unique);

    int replyCount(String unique);

    int productReplyCount(int productId);

    String doReply(int productId, int count);

    /**
     * 根据ID获取查询对象
     *
     * @param id
     * @return
     */
    YxStoreProductReplyQueryVo getYxStoreProductReplyById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     *
     * @param yxStoreProductReplyQueryParam
     * @return
     */
    Paging<YxStoreProductReplyQueryVo> getYxStoreProductReplyPageList(YxStoreProductReplyQueryParam yxStoreProductReplyQueryParam) throws Exception;

    String doReplyStar(int productId, int count);
}
