package co.yixiang.modules.ship.service.impl;

import co.yixiang.modules.ship.entity.YxShipSeries;
import co.yixiang.modules.ship.mapper.YxShipSeriesMapper;
import co.yixiang.modules.ship.service.YxShipSeriesService;
import co.yixiang.modules.ship.web.param.YxShipSeriesQueryParam;
import co.yixiang.modules.ship.web.vo.YxShipSeriesQueryVo;
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
 * 船只系列表 服务实现类
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxShipSeriesServiceImpl extends BaseServiceImpl<YxShipSeriesMapper, YxShipSeries> implements YxShipSeriesService {

    @Autowired
    private YxShipSeriesMapper yxShipSeriesMapper;

    @Override
    public YxShipSeriesQueryVo getYxShipSeriesById(Serializable id) throws Exception{
        return yxShipSeriesMapper.getYxShipSeriesById(id);
    }

    @Override
    public Paging<YxShipSeriesQueryVo> getYxShipSeriesPageList(YxShipSeriesQueryParam yxShipSeriesQueryParam) throws Exception{
        Page page = setPageParam(yxShipSeriesQueryParam,OrderItem.desc("create_time"));
        IPage<YxShipSeriesQueryVo> iPage = yxShipSeriesMapper.getYxShipSeriesPageList(page,yxShipSeriesQueryParam);
        return new Paging(iPage);
    }

}
