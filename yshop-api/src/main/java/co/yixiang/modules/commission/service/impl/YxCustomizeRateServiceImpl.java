package co.yixiang.modules.commission.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.commission.entity.YxCustomizeRate;
import co.yixiang.modules.commission.mapper.YxCustomizeRateMapper;
import co.yixiang.modules.commission.service.YxCustomizeRateService;
import co.yixiang.modules.commission.web.param.YxCustomizeRateQueryParam;
import co.yixiang.modules.commission.web.vo.YxCustomizeRateQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;


/**
 * <p>
 * 自定义分佣配置表 服务实现类
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxCustomizeRateServiceImpl extends BaseServiceImpl<YxCustomizeRateMapper, YxCustomizeRate> implements YxCustomizeRateService {

    @Autowired
    private YxCustomizeRateMapper yxCustomizeRateMapper;

    @Override
    public YxCustomizeRateQueryVo getYxCustomizeRateById(Serializable id) throws Exception{
        return yxCustomizeRateMapper.getYxCustomizeRateById(id);
    }

    @Override
    public Paging<YxCustomizeRateQueryVo> getYxCustomizeRatePageList(YxCustomizeRateQueryParam yxCustomizeRateQueryParam) throws Exception{
        Page page = setPageParam(yxCustomizeRateQueryParam,OrderItem.desc("create_time"));
        IPage<YxCustomizeRateQueryVo> iPage = yxCustomizeRateMapper.getYxCustomizeRatePageList(page,yxCustomizeRateQueryParam);
        return new Paging(iPage);
    }

    /**
     * 根据类型以及linkId获取自定义分佣配置
     * @param type
     * @param linkId
     * @return
     */
    @Override
    public YxCustomizeRate getCustomizeRateByParam(int type,int linkId){
        QueryWrapper<YxCustomizeRate> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(YxCustomizeRate::getRateType,type).eq(YxCustomizeRate::getLinkId,linkId).eq(YxCustomizeRate::getDelFlag,0);
        List<YxCustomizeRate> customizeRateList = yxCustomizeRateMapper.selectList(queryWrapper);
        if(CollectionUtils.isEmpty(customizeRateList)){
            return null;
        }
        return customizeRateList.get(0);
    }
}
