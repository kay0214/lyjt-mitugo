/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.shop.service;

import co.yixiang.common.service.BaseService;
import co.yixiang.modules.shop.entity.YxSystemConfig;

/**
 * <p>
 * 配置表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2019-10-19
 */
public interface YxSystemConfigService extends BaseService<YxSystemConfig> {

    String getData(String name);
}
