package co.yixiang.modules.ship.service.impl;

import co.yixiang.modules.ship.entity.YxShipOperationDetail;
import co.yixiang.modules.ship.mapper.YxShipOperationDetailMapper;
import co.yixiang.modules.ship.service.YxShipOperationDetailService;
import co.yixiang.modules.ship.web.param.YxShipOperationDetailQueryParam;
import co.yixiang.modules.ship.web.vo.YxShipOperationDetailQueryVo;
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
 * 船只运营记录详情 服务实现类
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxShipOperationDetailServiceImpl extends BaseServiceImpl<YxShipOperationDetailMapper, YxShipOperationDetail> implements YxShipOperationDetailService {

    @Autowired
    private YxShipOperationDetailMapper yxShipOperationDetailMapper;

    @Override
    public YxShipOperationDetailQueryVo getYxShipOperationDetailById(Serializable id) throws Exception{
        return yxShipOperationDetailMapper.getYxShipOperationDetailById(id);
    }

    @Override
    public Paging<YxShipOperationDetailQueryVo> getYxShipOperationDetailPageList(YxShipOperationDetailQueryParam yxShipOperationDetailQueryParam) throws Exception{
        Page page = setPageParam(yxShipOperationDetailQueryParam,OrderItem.desc("create_time"));
        IPage<YxShipOperationDetailQueryVo> iPage = yxShipOperationDetailMapper.getYxShipOperationDetailPageList(page,yxShipOperationDetailQueryParam);
        return new Paging(iPage);
    }

}
