/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.shipManage.service.impl;

import co.yixiang.modules.shipManage.domain.YxShipOperationDetail;
import co.yixiang.common.service.impl.BaseServiceImpl;
import lombok.AllArgsConstructor;
import co.yixiang.dozer.service.IGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.utils.ValidationUtil;
import co.yixiang.utils.FileUtil;
import co.yixiang.modules.shipManage.service.YxShipOperationDetailService;
import co.yixiang.modules.shipManage.service.dto.YxShipOperationDetailDto;
import co.yixiang.modules.shipManage.service.dto.YxShipOperationDetailQueryCriteria;
import co.yixiang.modules.shipManage.service.mapper.YxShipOperationDetailMapper;
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
* @date 2020-11-05
*/
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "yxShipOperationDetail")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxShipOperationDetailServiceImpl extends BaseServiceImpl<YxShipOperationDetailMapper, YxShipOperationDetail> implements YxShipOperationDetailService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxShipOperationDetailQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YxShipOperationDetail> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YxShipOperationDetailDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YxShipOperationDetail> queryAll(YxShipOperationDetailQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YxShipOperationDetail.class, criteria));
    }


    @Override
    public void download(List<YxShipOperationDetailDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxShipOperationDetailDto yxShipOperationDetail : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("卡券订单id", yxShipOperationDetail.getCouponOrderId());
            map.put("船只id", yxShipOperationDetail.getShipId());
            map.put("船只出港批次号", yxShipOperationDetail.getBatchNo());
            map.put("船只名称", yxShipOperationDetail.getShipName());
            map.put("船长id", yxShipOperationDetail.getCaptainId());
            map.put("船长姓名", yxShipOperationDetail.getCaptainName());
            map.put("核销人id", yxShipOperationDetail.getUseId());
            map.put("核销人姓名", yxShipOperationDetail.getUseName());
            map.put("乘客身体状况", yxShipOperationDetail.getHealthStatus());
            map.put("承载人数", yxShipOperationDetail.getTotalPassenger());
            map.put("老年人人数", yxShipOperationDetail.getOldPassenger());
            map.put("未成年人数", yxShipOperationDetail.getUnderagePassenger());
            map.put("是否删除（0：未删除，1：已删除）", yxShipOperationDetail.getDelFlag());
            map.put("创建人", yxShipOperationDetail.getCreateUserId());
            map.put("修改人", yxShipOperationDetail.getUpdateUserId());
            map.put("创建时间", yxShipOperationDetail.getCreateTime());
            map.put("更新时间", yxShipOperationDetail.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
