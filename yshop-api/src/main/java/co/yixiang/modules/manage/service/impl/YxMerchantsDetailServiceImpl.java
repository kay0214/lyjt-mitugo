package co.yixiang.modules.manage.service.impl;

import co.yixiang.modules.manage.entity.YxMerchantsDetail;
import co.yixiang.modules.manage.mapper.YxMerchantsDetailMapper;
import co.yixiang.modules.manage.service.YxMerchantsDetailService;
import co.yixiang.modules.manage.web.param.YxMerchantsDetailQueryParam;
import co.yixiang.modules.manage.web.vo.YxMerchantsDetailQueryVo;
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
 * 商户详情 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxMerchantsDetailServiceImpl extends BaseServiceImpl<YxMerchantsDetailMapper, YxMerchantsDetail> implements YxMerchantsDetailService {

    @Autowired
    private YxMerchantsDetailMapper yxMerchantsDetailMapper;

    @Override
    public YxMerchantsDetailQueryVo getYxMerchantsDetailById(Serializable id) throws Exception{
        return yxMerchantsDetailMapper.getYxMerchantsDetailById(id);
    }

    @Override
    public Paging<YxMerchantsDetailQueryVo> getYxMerchantsDetailPageList(YxMerchantsDetailQueryParam yxMerchantsDetailQueryParam) throws Exception{
        Page page = setPageParam(yxMerchantsDetailQueryParam,OrderItem.desc("create_time"));
        IPage<YxMerchantsDetailQueryVo> iPage = yxMerchantsDetailMapper.getYxMerchantsDetailPageList(page,yxMerchantsDetailQueryParam);
        return new Paging(iPage);
    }

}
