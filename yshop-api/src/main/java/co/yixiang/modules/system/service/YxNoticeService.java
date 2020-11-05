package co.yixiang.modules.system.service;

import co.yixiang.modules.coupons.web.vo.NoticeVO;
import co.yixiang.modules.system.entity.YxNotice;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.system.web.param.YxNoticeQueryParam;
import co.yixiang.modules.system.web.vo.YxNoticeQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;

/**
 * <p>
 * 公告表 服务类
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
public interface YxNoticeService extends BaseService<YxNotice> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxNoticeQueryVo getYxNoticeById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxNoticeQueryParam
     * @return
     */
    Paging<YxNoticeQueryVo> getYxNoticePageList(YxNoticeQueryParam yxNoticeQueryParam) throws Exception;

    /**
     * 获得首页的通知公告
     * @return
     */
    NoticeVO getIndexNotice() ;
}
