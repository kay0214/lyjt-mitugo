/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.shop.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.modules.shop.entity.YxSystemConfig;
import co.yixiang.modules.shop.mapper.YxSystemConfigMapper;
import co.yixiang.modules.shop.service.YxSystemConfigService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>
 * 配置表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2019-10-19
 */
@Slf4j
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class YxSystemConfigServiceImpl extends BaseServiceImpl<YxSystemConfigMapper, YxSystemConfig> implements YxSystemConfigService {

    private final YxSystemConfigMapper yxSystemConfigMapper;

    @Override
    public String getData(String name) {
        QueryWrapper<YxSystemConfig> wrapper = new QueryWrapper<>();
        wrapper.eq("menu_name",name);
        YxSystemConfig systemConfig = yxSystemConfigMapper.selectOne(wrapper);
        if(systemConfig == null) return "";
        return systemConfig.getValue();
    }
}
