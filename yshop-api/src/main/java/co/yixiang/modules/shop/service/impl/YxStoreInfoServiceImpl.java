package co.yixiang.modules.shop.service.impl;

import co.yixiang.modules.shop.entity.YxStoreInfo;
import co.yixiang.modules.shop.mapper.YxStoreInfoMapper;
import co.yixiang.modules.shop.service.YxStoreInfoService;
import co.yixiang.modules.shop.web.param.YxStoreInfoQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreInfoQueryVo;
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
 * 店铺表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxStoreInfoServiceImpl extends BaseServiceImpl<YxStoreInfoMapper, YxStoreInfo> implements YxStoreInfoService {

    @Autowired
    private YxStoreInfoMapper yxStoreInfoMapper;

    @Override
    public YxStoreInfoQueryVo getYxStoreInfoById(Serializable id) throws Exception{
        return yxStoreInfoMapper.getYxStoreInfoById(id);
    }

    @Override
    public Paging<YxStoreInfoQueryVo> getYxStoreInfoPageList(YxStoreInfoQueryParam yxStoreInfoQueryParam) throws Exception{
        Page page = setPageParam(yxStoreInfoQueryParam,OrderItem.desc("create_time"));
        IPage<YxStoreInfoQueryVo> iPage = yxStoreInfoMapper.getYxStoreInfoPageList(page,yxStoreInfoQueryParam);
        return new Paging(iPage);
    }

}
