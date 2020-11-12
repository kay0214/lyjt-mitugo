/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.shop.service;

import co.yixiang.common.service.BaseService;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.coupons.web.param.IndexTabQueryParam;
import co.yixiang.modules.coupons.web.vo.LocalLifeSliderVo;
import co.yixiang.modules.coupons.web.vo.LocalLiveIndexVo;
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

    /**
     * 设置首页的文字
     * @param localLiveIndexVo
     * @return
     */
    LocalLiveIndexVo setIndexTitle(LocalLiveIndexVo localLiveIndexVo);

    /**
     * 查询首页数据
     * @param seachStr
     * @return
     */
    List<LocalLifeSliderVo> getDataByGroupName(String seachStr);

    /**
     * 查询首页Tab数据
     * @param indexTabQueryParam
     * @return
     */
    Paging<LocalLifeSliderVo> getDataByGroupNamePage(IndexTabQueryParam indexTabQueryParam);
}
