package co.yixiang.modules.ship.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.manage.entity.SystemUser;
import co.yixiang.modules.ship.entity.YxShipInfo;
import co.yixiang.modules.ship.entity.YxShipOperation;
import co.yixiang.modules.ship.mapper.YxShipOperationMapper;
import co.yixiang.modules.ship.service.YxShipOperationService;
import co.yixiang.modules.ship.web.param.YxShipOperationQueryParam;
import co.yixiang.modules.ship.web.vo.YxShipOperationQueryVo;
import co.yixiang.utils.SnowflakeUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


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
public class YxShipOperationServiceImpl extends BaseServiceImpl<YxShipOperationMapper, YxShipOperation> implements YxShipOperationService
{
    @Value("${yshop.snowflake.datacenterId}")
    private Integer datacenterId;


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

    /**
     * 查看船只是否已经被分配了
     * @param shipId
     * @param shipUserId
     * @return
     */
    @Override
    public YxShipOperation getShipOperationBySidUid(Integer shipId, Integer shipUserId) {
        QueryWrapper<YxShipOperation> wrapper = new QueryWrapper<>();
        wrapper.eq("ship_id", shipId)
                .eq("captain_id", shipUserId)
                .eq("status", 0)
                .eq("delFlag", 0);
        List<YxShipOperation> list = yxShipOperationMapper.selectList(wrapper);
        return list == null || list.size() == 0 ? null : list.get(0);
    }

    /**
     * 插入船只运营记录
     * @param map
     * @return
     */
    @Override
    public YxShipOperation insertYxShipOperation(Map<String, Object> map) {
        YxShipOperation item = new YxShipOperation();
        // 生成订单号
        String uuid = SnowflakeUtil.getOrderId(datacenterId);
        item.setBatchNo(uuid);

        YxShipInfo shipInfo = (YxShipInfo)map.get("shipInfo");
        item.setShipId(shipInfo.getId());
        item.setShipName(shipInfo.getShipName());

        SystemUser captainUser = (SystemUser)map.get("captainUser");
        item.setCaptainId(captainUser.getId().intValue());
        item.setCaptainName(captainUser.getNickName());

        // 设置各种人数
        setShipPassenger(map,item);

        // 出港时间
        item.setLeaveTime(0);
        // 回港时间
        item.setLeaveTime(0);
        // 船只状态 0:待出港 1：出港 2：回港
        item.setStatus(0);
        // 是否删除（0：未删除，1：已删除）
        item.setDelFlag(0);

        SystemUser user = (SystemUser)map.get("user");
        item.setCreateUserId(user.getId().intValue());
        item.setUpdateUserId(user.getId().intValue());

        this.yxShipOperationMapper.insert(item);

        return item;
    }

    // 设置各种人数
    private void setShipPassenger(Map<String, Object> map, YxShipOperation item) {
        // 承载人数
        item.setTotalPassenger(0);
        // 老年人人数
        item.setOldPassenger(0);
        // 未成年人数
        item.setUnderagePassenger(0);
    }

}
