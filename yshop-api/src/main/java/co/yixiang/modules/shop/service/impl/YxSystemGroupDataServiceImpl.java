/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.shop.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.modules.shop.entity.YxSystemGroupData;
import co.yixiang.modules.shop.mapper.YxSystemGroupDataMapper;
import co.yixiang.modules.shop.service.YxSystemGroupDataService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
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


    /**
     * 获取配置数据
     * @param name
     * @return
     */
    @Override
    public List<Map<String,Object>> getDatas(String name) {
        QueryWrapper<YxSystemGroupData> wrapper = new QueryWrapper<>();

        List<Map<String,Object>> list = new ArrayList<>();

        wrapper.eq("group_name",name).eq("status",1).orderByDesc("sort");
        List<YxSystemGroupData> systemGroupDatas = baseMapper.selectList(wrapper);

        for (YxSystemGroupData yxSystemGroupData : systemGroupDatas) {
            list.add(JSONObject.parseObject(yxSystemGroupData.getValue()));
        }

        return list;
    }

    /**
     * 获取单条数据
     * @param id
     * @return
     */
    @Override
    public YxSystemGroupData findData(Integer id) {
        return baseMapper.selectById(id);
    }
}
