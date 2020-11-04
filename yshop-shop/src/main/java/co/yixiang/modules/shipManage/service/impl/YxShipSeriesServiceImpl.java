/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.shipManage.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.modules.shipManage.domain.YxShipSeries;
import co.yixiang.modules.shipManage.service.YxShipSeriesService;
import co.yixiang.modules.shipManage.service.dto.YxShipSeriesDto;
import co.yixiang.modules.shipManage.service.dto.YxShipSeriesQueryCriteria;
import co.yixiang.modules.shipManage.service.mapper.YxShipSeriesMapper;
import co.yixiang.utils.FileUtil;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// 默认不使用缓存
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;

/**
* @author nxl
* @date 2020-11-04
*/
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "yxShipSeries")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxShipSeriesServiceImpl extends BaseServiceImpl<YxShipSeriesMapper, YxShipSeries> implements YxShipSeriesService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxShipSeriesQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YxShipSeries> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YxShipSeriesDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YxShipSeries> queryAll(YxShipSeriesQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YxShipSeries.class, criteria));
    }


    @Override
    public void download(List<YxShipSeriesDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxShipSeriesDto yxShipSeries : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("系列名称", yxShipSeries.getSeriesName());
            map.put("船只类别", yxShipSeries.getShipCategory());
            map.put("限乘人数", yxShipSeries.getRideLimit());
            map.put("尺寸", yxShipSeries.getShipSize());
            map.put("状态：0：启用，1：禁用", yxShipSeries.getStatus());
            map.put("乘船省市区", yxShipSeries.getShipProvince());
            map.put("乘船地址", yxShipSeries.getShipAddress());
//            map.put("地图坐标", yxShipSeries.getCoordinate());
            map.put("地图坐标经度", yxShipSeries.getCoordinateX());
            map.put("地图坐标纬度", yxShipSeries.getCoordinateY());
            map.put("是否删除（0：未删除，1：已删除）", yxShipSeries.getDelFlag());
            map.put("创建人", yxShipSeries.getCreateUserId());
            map.put("修改人", yxShipSeries.getUpdateUserId());
            map.put("创建时间", yxShipSeries.getCreateTime());
            map.put("更新时间", yxShipSeries.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
