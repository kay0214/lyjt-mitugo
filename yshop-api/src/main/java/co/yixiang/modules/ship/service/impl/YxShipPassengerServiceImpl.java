package co.yixiang.modules.ship.service.impl;

import co.yixiang.modules.ship.entity.YxShipPassenger;
import co.yixiang.modules.ship.mapper.YxShipPassengerMapper;
import co.yixiang.modules.ship.service.YxShipPassengerService;
import co.yixiang.modules.ship.web.param.YxShipPassengerQueryParam;
import co.yixiang.modules.ship.web.vo.YxShipPassengerQueryVo;
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
 * 本地生活帆船订单乘客表 服务实现类
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxShipPassengerServiceImpl extends BaseServiceImpl<YxShipPassengerMapper, YxShipPassenger> implements YxShipPassengerService {

    @Autowired
    private YxShipPassengerMapper yxShipPassengerMapper;

    @Override
    public YxShipPassengerQueryVo getYxShipPassengerById(Serializable id) throws Exception{
        return yxShipPassengerMapper.getYxShipPassengerById(id);
    }

    @Override
    public Paging<YxShipPassengerQueryVo> getYxShipPassengerPageList(YxShipPassengerQueryParam yxShipPassengerQueryParam) throws Exception{
        Page page = setPageParam(yxShipPassengerQueryParam,OrderItem.desc("create_time"));
        IPage<YxShipPassengerQueryVo> iPage = yxShipPassengerMapper.getYxShipPassengerPageList(page,yxShipPassengerQueryParam);
        return new Paging(iPage);
    }

}
