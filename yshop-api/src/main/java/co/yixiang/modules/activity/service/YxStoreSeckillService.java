/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.activity.service;

import co.yixiang.common.service.BaseService;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.activity.entity.YxStoreSeckill;
import co.yixiang.modules.activity.web.dto.StoreSeckillDTO;
import co.yixiang.modules.activity.web.param.YxStoreSeckillQueryParam;
import co.yixiang.modules.activity.web.vo.YxStoreSeckillQueryVo;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 商品秒杀产品表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2019-12-14
 */
public interface YxStoreSeckillService extends BaseService<YxStoreSeckill> {

    void incStockDecSales(int num,int seckillId);

    void decStockIncSales(int num,int seckillId);

    YxStoreSeckill getSeckill(int id);

    StoreSeckillDTO getDetail(int id) throws Exception;


    /**
     * 分页获取产品详情
     * @param page
     * @param limit
     * @return
     */
    List<YxStoreSeckillQueryVo> getList(int page, int limit, int time);
    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxStoreSeckillQueryVo getYxStoreSeckillById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxStoreSeckillQueryParam
     * @return
     */
    Paging<YxStoreSeckillQueryVo> getYxStoreSeckillPageList(YxStoreSeckillQueryParam yxStoreSeckillQueryParam) throws Exception;

}
