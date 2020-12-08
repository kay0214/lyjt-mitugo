package co.yixiang.modules.system.service;

import co.yixiang.modules.system.entity.YxHotConfig;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.system.web.param.YxHotConfigQueryParam;
import co.yixiang.modules.system.web.vo.YxHotConfigQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;

/**
 * <p>
 * HOT配置表 服务类
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
public interface YxHotConfigService extends BaseService<YxHotConfig> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxHotConfigQueryVo getYxHotConfigById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxHotConfigQueryParam
     * @return
     */
    Paging<YxHotConfigQueryVo> getYxHotConfigPageList(YxHotConfigQueryParam yxHotConfigQueryParam) throws Exception;

}
