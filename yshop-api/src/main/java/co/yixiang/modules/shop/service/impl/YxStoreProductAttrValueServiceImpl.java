package co.yixiang.modules.shop.service.impl;

import co.yixiang.modules.shop.entity.YxStoreProductAttrValue;
import co.yixiang.modules.shop.mapper.YxStoreProductAttrValueMapper;
import co.yixiang.modules.shop.service.YxStoreProductAttrValueService;
import co.yixiang.modules.shop.web.param.YxStoreProductAttrValueQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreProductAttrValueQueryVo;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.io.Serializable;


/**
 * <p>
 * 商品属性值表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxStoreProductAttrValueServiceImpl extends BaseServiceImpl<YxStoreProductAttrValueMapper, YxStoreProductAttrValue> implements YxStoreProductAttrValueService {

    @Autowired
    private YxStoreProductAttrValueMapper yxStoreProductAttrValueMapper;

    @Override
    public YxStoreProductAttrValueQueryVo getYxStoreProductAttrValueById(Serializable id) throws Exception{
        return yxStoreProductAttrValueMapper.getYxStoreProductAttrValueById(id);
    }

    @Override
    public Paging<YxStoreProductAttrValueQueryVo> getYxStoreProductAttrValuePageList(YxStoreProductAttrValueQueryParam yxStoreProductAttrValueQueryParam) throws Exception{
        Page page = setPageParam(yxStoreProductAttrValueQueryParam,OrderItem.desc("create_time"));
        IPage<YxStoreProductAttrValueQueryVo> iPage = yxStoreProductAttrValueMapper.getYxStoreProductAttrValuePageList(page,yxStoreProductAttrValueQueryParam);
        return new Paging(iPage);
    }

}
