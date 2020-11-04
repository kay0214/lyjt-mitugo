package co.yixiang.modules.ship.service.impl;

import co.yixiang.modules.ship.entity.YxShipInfo;
import co.yixiang.modules.ship.mapper.YxShipInfoMapper;
import co.yixiang.modules.ship.service.YxShipInfoService;
import co.yixiang.modules.ship.web.param.YxShipInfoQueryParam;
import co.yixiang.modules.ship.web.vo.YxShipInfoQueryVo;
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
 * 船只表 服务实现类
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxShipInfoServiceImpl extends BaseServiceImpl<YxShipInfoMapper, YxShipInfo> implements YxShipInfoService {

    @Autowired
    private YxShipInfoMapper yxShipInfoMapper;

    @Override
    public YxShipInfoQueryVo getYxShipInfoById(Serializable id) throws Exception{
        return yxShipInfoMapper.getYxShipInfoById(id);
    }

    @Override
    public Paging<YxShipInfoQueryVo> getYxShipInfoPageList(YxShipInfoQueryParam yxShipInfoQueryParam) throws Exception{
        Page page = setPageParam(yxShipInfoQueryParam,OrderItem.desc("create_time"));
        IPage<YxShipInfoQueryVo> iPage = yxShipInfoMapper.getYxShipInfoPageList(page,yxShipInfoQueryParam);
        return new Paging(iPage);
    }

}
