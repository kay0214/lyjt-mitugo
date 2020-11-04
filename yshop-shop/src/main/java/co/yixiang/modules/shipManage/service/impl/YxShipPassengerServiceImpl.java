/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.shipManage.service.impl;

import co.yixiang.modules.shipManage.domain.YxShipPassenger;
import co.yixiang.common.service.impl.BaseServiceImpl;
import lombok.AllArgsConstructor;
import co.yixiang.dozer.service.IGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.utils.ValidationUtil;
import co.yixiang.utils.FileUtil;
import co.yixiang.modules.shipManage.service.YxShipPassengerService;
import co.yixiang.modules.shipManage.service.dto.YxShipPassengerDto;
import co.yixiang.modules.shipManage.service.dto.YxShipPassengerQueryCriteria;
import co.yixiang.modules.shipManage.service.mapper.YxShipPassengerMapper;
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
//@CacheConfig(cacheNames = "yxShipPassenger")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxShipPassengerServiceImpl extends BaseServiceImpl<YxShipPassengerMapper, YxShipPassenger> implements YxShipPassengerService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxShipPassengerQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YxShipPassenger> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YxShipPassengerDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YxShipPassenger> queryAll(YxShipPassengerQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YxShipPassenger.class, criteria));
    }


    @Override
    public void download(List<YxShipPassengerDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxShipPassengerDto yxShipPassenger : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("卡券订单id", yxShipPassenger.getCouponOrderId());
            map.put("船只出港批次号", yxShipPassenger.getBatchNo());
            map.put("船只id", yxShipPassenger.getShipId());
            map.put("乘客姓名", yxShipPassenger.getPassengerName());
            map.put("乘客身份证", yxShipPassenger.getIdCard());
            map.put("乘客电话", yxShipPassenger.getPhone());
            map.put("0:未成年 1:成年人 2：老年人", yxShipPassenger.getIsAdult());
            map.put("是否删除（0：未删除，1：已删除）", yxShipPassenger.getDelFlag());
            map.put("创建人", yxShipPassenger.getCreateUserId());
            map.put("修改人", yxShipPassenger.getUpdateUserId());
            map.put("创建时间", yxShipPassenger.getCreateTime());
            map.put("更新时间", yxShipPassenger.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
