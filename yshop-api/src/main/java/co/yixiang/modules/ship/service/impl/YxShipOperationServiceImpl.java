package co.yixiang.modules.ship.service.impl;

import co.yixiang.modules.ship.entity.YxShipOperation;
import co.yixiang.modules.ship.mapper.YxShipOperationMapper;
import co.yixiang.modules.ship.service.YxShipOperationService;
import co.yixiang.modules.ship.web.param.YxShipOperationQueryParam;
import co.yixiang.modules.ship.web.vo.YxShipOperationQueryVo;
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
 * 船只运营记录 服务实现类
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxShipOperationServiceImpl extends BaseServiceImpl<YxShipOperationMapper, YxShipOperation> implements YxShipOperationService {

    @Autowired
    private YxShipOperationMapper yxShipOperationMapper;

    @Override
    public YxShipOperationQueryVo getYxShipOperationById(Serializable id) throws Exception{
        return yxShipOperationMapper.getYxShipOperationById(id);
    }

    @Override
    public Paging<YxShipOperationQueryVo> getYxShipOperationPageList(YxShipOperationQueryParam yxShipOperationQueryParam) throws Exception{
        Page page = setPageParam(yxShipOperationQueryParam,OrderItem.desc("create_time"));
        IPage<YxShipOperationQueryVo> iPage = yxShipOperationMapper.getYxShipOperationPageList(page,yxShipOperationQueryParam);
        return new Paging(iPage);
    }

}
