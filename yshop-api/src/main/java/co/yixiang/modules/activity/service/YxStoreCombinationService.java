/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.activity.service;

import co.yixiang.common.service.BaseService;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.activity.entity.YxStoreCombination;
import co.yixiang.modules.activity.web.dto.StoreCombinationDTO;
import co.yixiang.modules.activity.web.param.YxStoreCombinationQueryParam;
import co.yixiang.modules.activity.web.vo.YxStoreCombinationQueryVo;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 拼团产品表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2019-11-19
 */
public interface YxStoreCombinationService extends BaseService<YxStoreCombination> {

    YxStoreCombinationQueryVo getCombinationT(int id);
    void decStockIncSales(int num,int combinationId);

    void incStockDecSales(int num,int combinationId);

    YxStoreCombination getCombination(int id);

    boolean judgeCombinationStock(int combinationId,int cartNum);

    List<YxStoreCombinationQueryVo> getList(int page, int limit);

    StoreCombinationDTO getDetail(int id,int uid);


    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxStoreCombinationQueryVo getYxStoreCombinationById(Serializable id);

    /**
     * 获取分页对象
     * @param yxStoreCombinationQueryParam
     * @return
     */
    Paging<YxStoreCombinationQueryVo> getYxStoreCombinationPageList(YxStoreCombinationQueryParam yxStoreCombinationQueryParam) throws Exception;

}
