package co.yixiang.modules.ship.service.impl;

import co.yixiang.modules.ship.entity.YxShipAppoint;
import co.yixiang.modules.ship.mapper.YxShipAppointMapper;
import co.yixiang.modules.ship.service.YxShipAppointService;
import co.yixiang.modules.ship.web.param.YxShipAppointQueryParam;
import co.yixiang.modules.ship.web.vo.YxShipAppointQueryVo;
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
 * 船只预约表 服务实现类
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxShipAppointServiceImpl extends BaseServiceImpl<YxShipAppointMapper, YxShipAppoint> implements YxShipAppointService {

    @Autowired
    private YxShipAppointMapper yxShipAppointMapper;

    @Override
    public YxShipAppointQueryVo getYxShipAppointById(Serializable id) throws Exception{
        return yxShipAppointMapper.getYxShipAppointById(id);
    }

    @Override
    public Paging<YxShipAppointQueryVo> getYxShipAppointPageList(YxShipAppointQueryParam yxShipAppointQueryParam) throws Exception{
        Page page = setPageParam(yxShipAppointQueryParam,OrderItem.desc("create_time"));
        IPage<YxShipAppointQueryVo> iPage = yxShipAppointMapper.getYxShipAppointPageList(page,yxShipAppointQueryParam);
        return new Paging(iPage);
    }

}
