/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.shop.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.shop.entity.YxStoreProductAttrResult;
import co.yixiang.modules.shop.mapper.YxStoreProductAttrResultMapper;
import co.yixiang.modules.shop.service.YxStoreProductAttrResultService;
import co.yixiang.modules.shop.web.param.YxStoreProductAttrResultQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreProductAttrResultQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;


/**
 * <p>
 * 商品属性详情表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2019-10-23
 */
@Slf4j
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class YxStoreProductAttrResultServiceImpl extends BaseServiceImpl<YxStoreProductAttrResultMapper, YxStoreProductAttrResult> implements YxStoreProductAttrResultService {

    private final YxStoreProductAttrResultMapper yxStoreProductAttrResultMapper;

    @Override
    public YxStoreProductAttrResultQueryVo getYxStoreProductAttrResultById(Serializable id) throws Exception{
        return yxStoreProductAttrResultMapper.getYxStoreProductAttrResultById(id);
    }

    @Override
    public Paging<YxStoreProductAttrResultQueryVo> getYxStoreProductAttrResultPageList(YxStoreProductAttrResultQueryParam yxStoreProductAttrResultQueryParam) throws Exception{
        Page page = setPageParam(yxStoreProductAttrResultQueryParam,OrderItem.desc("create_time"));
        IPage<YxStoreProductAttrResultQueryVo> iPage = yxStoreProductAttrResultMapper.getYxStoreProductAttrResultPageList(page,yxStoreProductAttrResultQueryParam);
        return new Paging(iPage);
    }

}
