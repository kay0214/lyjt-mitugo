package co.yixiang.modules.ship.service.impl;

import co.yixiang.modules.ship.entity.YxShipAppointDetail;
import co.yixiang.modules.ship.mapper.YxShipAppointDetailMapper;
import co.yixiang.modules.ship.service.YxShipAppointDetailService;
import co.yixiang.modules.ship.web.param.YxShipAppointDetailQueryParam;
import co.yixiang.modules.ship.web.vo.YxShipAppointDetailQueryVo;
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
 * 船只预约表详情 服务实现类
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxShipAppointDetailServiceImpl extends BaseServiceImpl<YxShipAppointDetailMapper, YxShipAppointDetail> implements YxShipAppointDetailService {

    @Autowired
    private YxShipAppointDetailMapper yxShipAppointDetailMapper;

    @Override
    public YxShipAppointDetailQueryVo getYxShipAppointDetailById(Serializable id) throws Exception{
        return yxShipAppointDetailMapper.getYxShipAppointDetailById(id);
    }

    @Override
    public Paging<YxShipAppointDetailQueryVo> getYxShipAppointDetailPageList(YxShipAppointDetailQueryParam yxShipAppointDetailQueryParam) throws Exception{
        Page page = setPageParam(yxShipAppointDetailQueryParam,OrderItem.desc("create_time"));
        IPage<YxShipAppointDetailQueryVo> iPage = yxShipAppointDetailMapper.getYxShipAppointDetailPageList(page,yxShipAppointDetailQueryParam);
        return new Paging(iPage);
    }

}
