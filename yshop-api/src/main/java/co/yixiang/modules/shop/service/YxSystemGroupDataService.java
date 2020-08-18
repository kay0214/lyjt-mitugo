/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.shop.service;

import co.yixiang.common.service.BaseService;
import co.yixiang.modules.shop.entity.YxSystemGroupData;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 组合数据详情表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2019-10-19
 */
public interface YxSystemGroupDataService extends BaseService<YxSystemGroupData> {

    List<Map<String,Object>> getDatas(String name);

    YxSystemGroupData findData(Integer id);

}
