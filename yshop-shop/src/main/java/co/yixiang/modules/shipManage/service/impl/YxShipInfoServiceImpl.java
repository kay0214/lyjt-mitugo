/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.shipManage.service.impl;

import co.yixiang.modules.shipManage.domain.YxShipInfo;
import co.yixiang.common.service.impl.BaseServiceImpl;
import lombok.AllArgsConstructor;
import co.yixiang.dozer.service.IGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.utils.ValidationUtil;
import co.yixiang.utils.FileUtil;
import co.yixiang.modules.shipManage.service.YxShipInfoService;
import co.yixiang.modules.shipManage.service.dto.YxShipInfoDto;
import co.yixiang.modules.shipManage.service.dto.YxShipInfoQueryCriteria;
import co.yixiang.modules.shipManage.service.mapper.YxShipInfoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
// 默认不使用缓存
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @author nxl
* @date 2020-11-04
*/
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "yxShipInfo")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxShipInfoServiceImpl extends BaseServiceImpl<YxShipInfoMapper, YxShipInfo> implements YxShipInfoService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxShipInfoQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YxShipInfo> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YxShipInfoDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YxShipInfo> queryAll(YxShipInfoQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YxShipInfo.class, criteria));
    }


    @Override
    public void download(List<YxShipInfoDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxShipInfoDto yxShipInfo : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("船只名称", yxShipInfo.getShipName());
            map.put("船只系列id", yxShipInfo.getSeriesId());
            map.put("商户id", yxShipInfo.getMerId());
            map.put("所属商铺", yxShipInfo.getStoreId());
            map.put("帆船所属商户名", yxShipInfo.getMerName());
            map.put("帆船负责人", yxShipInfo.getManagerName());
            map.put("负责人电话", yxShipInfo.getManagerPhone());
            map.put("船只状态：0：启用，1：禁用", yxShipInfo.getShipStatus());
            map.put("船只当前状态：0：在港，1：离港。2：维修中", yxShipInfo.getCurrentStatus());
            map.put("最近一次出港时间", yxShipInfo.getLastLeaveTime());
            map.put("最近一次返港时间", yxShipInfo.getLastReturnTime());
            map.put("是否删除（0：未删除，1：已删除）", yxShipInfo.getDelFlag());
            map.put("创建人", yxShipInfo.getCreateUserId());
            map.put("修改人", yxShipInfo.getUpdateUserId());
            map.put("创建时间", yxShipInfo.getCreateTime());
            map.put("更新时间", yxShipInfo.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
