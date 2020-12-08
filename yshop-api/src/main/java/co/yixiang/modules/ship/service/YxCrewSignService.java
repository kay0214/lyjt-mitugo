package co.yixiang.modules.ship.service;

import co.yixiang.modules.ship.entity.YxCrewSign;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.ship.web.param.YxCrewSignQueryParam;
import co.yixiang.modules.ship.web.vo.YxCrewSignQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;

/**
 * <p>
 * 船员签到表 服务类
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
public interface YxCrewSignService extends BaseService<YxCrewSign> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxCrewSignQueryVo getYxCrewSignById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxCrewSignQueryParam
     * @return
     */
    Paging<YxCrewSignQueryVo> getYxCrewSignPageList(YxCrewSignQueryParam yxCrewSignQueryParam) throws Exception;

}
