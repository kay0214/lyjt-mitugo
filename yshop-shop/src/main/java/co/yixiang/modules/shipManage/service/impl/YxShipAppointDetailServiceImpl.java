/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.shipManage.service.impl;

import co.yixiang.modules.shipManage.domain.YxShipAppointDetail;
import co.yixiang.common.service.impl.BaseServiceImpl;
import lombok.AllArgsConstructor;
import co.yixiang.dozer.service.IGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.utils.ValidationUtil;
import co.yixiang.utils.FileUtil;
import co.yixiang.modules.shipManage.service.YxShipAppointDetailService;
import co.yixiang.modules.shipManage.service.dto.YxShipAppointDetailDto;
import co.yixiang.modules.shipManage.service.dto.YxShipAppointDetailQueryCriteria;
import co.yixiang.modules.shipManage.service.mapper.YxShipAppointDetailMapper;
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
//@CacheConfig(cacheNames = "yxShipAppointDetail")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxShipAppointDetailServiceImpl extends BaseServiceImpl<YxShipAppointDetailMapper, YxShipAppointDetail> implements YxShipAppointDetailService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxShipAppointDetailQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YxShipAppointDetail> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YxShipAppointDetailDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YxShipAppointDetail> queryAll(YxShipAppointDetailQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YxShipAppointDetail.class, criteria));
    }


    @Override
    public void download(List<YxShipAppointDetailDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxShipAppointDetailDto yxShipAppointDetail : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("船只预约表id", yxShipAppointDetail.getAppointId());
            map.put("船只系列id", yxShipAppointDetail.getSeriesId());
            map.put("所选船只id", yxShipAppointDetail.getShipId());
            map.put("是否删除（0：未删除，1：已删除）", yxShipAppointDetail.getDelFlag());
            map.put("创建人", yxShipAppointDetail.getCreateUserId());
            map.put("修改人", yxShipAppointDetail.getUpdateUserId());
            map.put("创建时间", yxShipAppointDetail.getCreateTime());
            map.put("更新时间", yxShipAppointDetail.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
