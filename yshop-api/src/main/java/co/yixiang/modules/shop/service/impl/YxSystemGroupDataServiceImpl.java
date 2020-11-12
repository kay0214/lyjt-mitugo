/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.shop.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.constant.ShopConstants;
import co.yixiang.modules.coupons.web.param.IndexTabQueryParam;
import co.yixiang.modules.coupons.web.vo.IndexTitleVO;
import co.yixiang.modules.coupons.web.vo.LocalLifeSliderVo;
import co.yixiang.modules.coupons.web.vo.LocalLiveIndexVo;
import co.yixiang.modules.shop.entity.SystemGroupDataValue;
import co.yixiang.modules.shop.entity.YxSystemGroupData;
import co.yixiang.modules.shop.mapper.YxSystemGroupDataMapper;
import co.yixiang.modules.shop.service.YxSystemGroupDataService;
import co.yixiang.utils.RedisUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 组合数据详情表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2019-10-19
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxSystemGroupDataServiceImpl extends BaseServiceImpl<YxSystemGroupDataMapper, YxSystemGroupData> implements YxSystemGroupDataService {
    @Autowired
    private RedisUtils redisUtils;

    /**
     * 获取配置数据
     *
     * @param name
     * @return
     */
    @Override
    public List<Map<String, Object>> getDatas(String name) {
        QueryWrapper<YxSystemGroupData> wrapper = new QueryWrapper<>();

        List<Map<String, Object>> list = new ArrayList<>();

//        wrapper.eq("group_name",name).eq("status",1).orderByDesc("sort").orderByDesc("add_time");
        wrapper.lambda().eq(YxSystemGroupData::getGroupName, name).eq(YxSystemGroupData::getStatus, 1).orderByAsc(YxSystemGroupData::getSort).orderByDesc(YxSystemGroupData::getAddTime);
        List<YxSystemGroupData> systemGroupDatas = baseMapper.selectList(wrapper);

        for (YxSystemGroupData yxSystemGroupData : systemGroupDatas) {
            list.add(JSONObject.parseObject(yxSystemGroupData.getValue()));
        }

        return list;
    }

    /**
     * 获取单条数据
     *
     * @param id
     * @return
     */
    @Override
    public YxSystemGroupData findData(Integer id) {
        return baseMapper.selectById(id);
    }


    /**
     * 设置首页的文字
     * @param localLiveIndexVo
     * @return
     */
    @Override
    public LocalLiveIndexVo setIndexTitle(LocalLiveIndexVo localLiveIndexVo) {
        localLiveIndexVo.setTitle_1((IndexTitleVO)redisUtils.get(ShopConstants.PAGE_INDEX_TITLE_1));
        localLiveIndexVo.setTitle_2((IndexTitleVO)redisUtils.get(ShopConstants.PAGE_INDEX_TITLE_2));
        localLiveIndexVo.setTitle_3_1((IndexTitleVO)redisUtils.get(ShopConstants.PAGE_INDEX_TITLE_3_1));
        localLiveIndexVo.setTitle_3_2((IndexTitleVO)redisUtils.get(ShopConstants.PAGE_INDEX_TITLE_3_2));
        localLiveIndexVo.setTitle_3_3((IndexTitleVO)redisUtils.get(ShopConstants.PAGE_INDEX_TITLE_3_3));
        return localLiveIndexVo;
    }

    /**
     * 查询首页数据
     * @param seachStr
     * @return
     */
    @Override
    public List<LocalLifeSliderVo> getDataByGroupName(String seachStr) {
        List<LocalLifeSliderVo> result = new ArrayList<>();
        List<YxSystemGroupData> localLiveMenu = this.list(new QueryWrapper<YxSystemGroupData>()
                .eq("group_name", seachStr).eq("status", 1).orderByAsc("sort").orderByDesc("add_time"));
        if (localLiveMenu != null){
            for (YxSystemGroupData menu : localLiveMenu){
                LocalLifeSliderVo liveMenuVo = new LocalLifeSliderVo();
                String jsonString = menu.getValue();

                SystemGroupDataValue sliderSystemData = JSON.parseObject(jsonString, SystemGroupDataValue.class);
                BeanUtils.copyProperties(sliderSystemData, liveMenuVo);
                result.add(liveMenuVo);
            }
        }
        return result;
    }

    /**
     * 查询首页Tab数据
     * @param indexTabQueryParam
     * @return
     */
    @Override
    public Paging<LocalLifeSliderVo> getDataByGroupNamePage(IndexTabQueryParam indexTabQueryParam) {
        Page page = setPageParam(indexTabQueryParam, OrderItem.asc("sort"));
        String group_name="";
        if(indexTabQueryParam.getType().intValue()==1){
            group_name = "local_live_module3";
        }else if(indexTabQueryParam.getType().intValue()==2){
            group_name = "local_live_module4";
        }else if(indexTabQueryParam.getType().intValue()==3){
            group_name = "local_live_module5";
        }
        QueryWrapper<YxSystemGroupData> query = new QueryWrapper<YxSystemGroupData>()
                .eq("group_name", group_name).eq("status", 1).orderByAsc("sort").orderByDesc("add_time");
        IPage<YxSystemGroupData> iPage = getBaseMapper().selectPage(page, query);
        List<LocalLifeSliderVo> result = new ArrayList<>();
        IPage<LocalLifeSliderVo> iPageResult = new Page<>();
        iPageResult.setCurrent(iPage.getCurrent());
        iPageResult.setPages(iPage.getPages());
        iPageResult.setSize(iPage.getSize());
        iPageResult.setTotal(iPage.getTotal());

        if (iPage.getRecords() != null){
            for (YxSystemGroupData menu : iPage.getRecords()){
                LocalLifeSliderVo liveMenuVo = new LocalLifeSliderVo();
                String jsonString = menu.getValue();

                SystemGroupDataValue sliderSystemData = JSON.parseObject(jsonString, SystemGroupDataValue.class);
                BeanUtils.copyProperties(sliderSystemData, liveMenuVo);
                result.add(liveMenuVo);
            }
        }
        iPageResult.setRecords(result);
        return new Paging(iPageResult);
    }
}
