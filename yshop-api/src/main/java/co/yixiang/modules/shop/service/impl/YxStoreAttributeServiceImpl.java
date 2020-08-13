package co.yixiang.modules.shop.service.impl;

import co.yixiang.modules.shop.entity.YxStoreAttribute;
import co.yixiang.modules.shop.mapper.YxStoreAttributeMapper;
import co.yixiang.modules.shop.service.YxStoreAttributeService;
import co.yixiang.modules.shop.web.param.YxStoreAttributeQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreAttributeQueryVo;
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
 * 店铺属性表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxStoreAttributeServiceImpl extends BaseServiceImpl<YxStoreAttributeMapper, YxStoreAttribute> implements YxStoreAttributeService {

    @Autowired
    private YxStoreAttributeMapper yxStoreAttributeMapper;

    @Override
    public YxStoreAttributeQueryVo getYxStoreAttributeById(Serializable id) throws Exception{
        return yxStoreAttributeMapper.getYxStoreAttributeById(id);
    }

    @Override
    public Paging<YxStoreAttributeQueryVo> getYxStoreAttributePageList(YxStoreAttributeQueryParam yxStoreAttributeQueryParam) throws Exception{
        Page page = setPageParam(yxStoreAttributeQueryParam,OrderItem.desc("create_time"));
        IPage<YxStoreAttributeQueryVo> iPage = yxStoreAttributeMapper.getYxStoreAttributePageList(page,yxStoreAttributeQueryParam);
        return new Paging(iPage);
    }

}
