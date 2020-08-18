/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.manage.service;

import co.yixiang.common.service.BaseService;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.manage.entity.YxExpress;
import co.yixiang.modules.manage.web.param.YxExpressQueryParam;
import co.yixiang.modules.manage.web.vo.YxExpressQueryVo;

import java.io.Serializable;

/**
 * <p>
 * 快递公司表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2019-12-13
 */
public interface YxExpressService extends BaseService<YxExpress> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxExpressQueryVo getYxExpressById(Serializable id);

    /**
     * 获取分页对象
     * @param yxExpressQueryParam
     * @return
     */
    Paging<YxExpressQueryVo> getYxExpressPageList(YxExpressQueryParam yxExpressQueryParam) throws Exception;

}
