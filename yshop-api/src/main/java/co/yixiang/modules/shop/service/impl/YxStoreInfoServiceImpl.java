package co.yixiang.modules.shop.service.impl;

import co.yixiang.common.constant.CommonConstant;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.enums.CommonEnum;
import co.yixiang.modules.manage.entity.DictDetail;
import co.yixiang.modules.manage.service.DictDetailService;
import co.yixiang.modules.shop.entity.YxStoreInfo;
import co.yixiang.modules.shop.mapper.YxStoreInfoMapper;
import co.yixiang.modules.shop.mapping.YxStoreInfoMap;
import co.yixiang.modules.shop.service.YxStoreInfoService;
import co.yixiang.modules.shop.web.param.YxStoreInfoQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreInfoQueryVo;
import co.yixiang.utils.StringUtils;
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
 * 店铺表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2020-08-14
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxStoreInfoServiceImpl extends BaseServiceImpl<YxStoreInfoMapper, YxStoreInfo> implements YxStoreInfoService {

    @Autowired
    private YxStoreInfoMapper yxStoreInfoMapper;
    @Autowired
    private YxStoreInfoMap yxStoreInfoMap;
    @Autowired
    private DictDetailService dictDetailService;

    @Override
    public YxStoreInfoQueryVo getYxStoreInfoById(Serializable id){
        return yxStoreInfoMapper.getYxStoreInfoById(id);
    }

    @Override
    public Paging<YxStoreInfoQueryVo> getYxStoreInfoPageList(YxStoreInfoQueryParam yxStoreInfoQueryParam) throws Exception{
        Page page = setPageParam(yxStoreInfoQueryParam,OrderItem.desc("create_time"));
        IPage<YxStoreInfoQueryVo> iPage = yxStoreInfoMapper.getYxStoreInfoPageList(page,yxStoreInfoQueryParam);
        return new Paging(iPage);
    }

    /**
     * 根据参数获取店铺信息
     * @param yxStoreInfoQueryParam
     * @return
     */
    @Override
    public List<YxStoreInfoQueryVo> getStoreInfoList(YxStoreInfoQueryParam yxStoreInfoQueryParam){
        QueryWrapper<YxStoreInfo> wrapper = new QueryWrapper<YxStoreInfo>();
        wrapper.eq("del_flag", CommonEnum.DEL_STATUS_0.getValue()).eq("status",0);
        if(StringUtils.isNotBlank(yxStoreInfoQueryParam.getStoreName())){
            wrapper.likeRight("store_name", yxStoreInfoQueryParam.getStoreName());
        }
        wrapper.orderByDesc("create_time");
        Page<YxStoreInfo> pageModel = new Page<YxStoreInfo>(yxStoreInfoQueryParam.getPage(),
                yxStoreInfoQueryParam.getLimit());
        IPage<YxStoreInfo> pageList = yxStoreInfoMapper.selectPage(pageModel,wrapper);
        List<YxStoreInfoQueryVo> list = yxStoreInfoMap.toDto(pageList.getRecords());
        if(!CollectionUtils.isEmpty(list)){
            for(YxStoreInfoQueryVo yxStoreInfoQueryVo:list){
                DictDetail dictDetail = dictDetailService.getDictDetailValueByType(CommonConstant.DICT_TYPE_INDUSTRY_CATEGORY,yxStoreInfoQueryVo.getIndustryCategory());
                yxStoreInfoQueryVo.setIndustryCategoryInfo(dictDetail.getLabel());
            }
        }
        return list;
    }
}
