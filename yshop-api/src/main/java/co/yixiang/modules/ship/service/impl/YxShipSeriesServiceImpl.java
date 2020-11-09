package co.yixiang.modules.ship.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.couponUse.dto.AllShipsVO;
import co.yixiang.modules.couponUse.dto.ShipInfoVO;
import co.yixiang.modules.ship.entity.YxShipInfo;
import co.yixiang.modules.ship.entity.YxShipSeries;
import co.yixiang.modules.ship.mapper.YxShipInfoMapper;
import co.yixiang.modules.ship.mapper.YxShipSeriesMapper;
import co.yixiang.modules.ship.service.YxShipSeriesService;
import co.yixiang.modules.ship.web.param.YxShipSeriesQueryParam;
import co.yixiang.modules.ship.web.vo.YxShipSeriesQueryVo;
import co.yixiang.utils.CommonsUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


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

    @Autowired
    private YxShipInfoMapper yxShipInfoMapper;

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

    /**
     * 获取本商户所有船只系列和船只
     * @param storeId
     * @return
     */
    @Override
    public List<AllShipsVO> getAllShipByStoreId(int storeId) {
        QueryWrapper<YxShipSeries> wrapper = new QueryWrapper<>();
        wrapper.eq("del_flag",0).eq("status",0).eq("store_id",storeId);
        List<YxShipSeries> shipSeries = yxShipSeriesMapper.selectList(wrapper);
        List<AllShipsVO> result = new ArrayList<>();
        for (YxShipSeries item : shipSeries) {
            AllShipsVO ship = CommonsUtils.convertBean(item,AllShipsVO.class);
            QueryWrapper<YxShipInfo> wrapperInfo = new QueryWrapper<>();
            wrapperInfo.eq("del_flag",0).eq("status",0).eq("store_id",storeId).eq("series_id",item.getId());
            List<YxShipInfo> shipInfos = yxShipInfoMapper.selectList(wrapperInfo);
            ship.setShipInfos(CommonsUtils.convertBeanList(shipInfos, ShipInfoVO.class));
            result.add(ship);
        }
        return result;
    }

}
