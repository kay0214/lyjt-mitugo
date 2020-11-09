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
import co.yixiang.modules.shipManage.domain.YxShipOperation;
import co.yixiang.modules.shipManage.domain.YxShipPassenger;
import co.yixiang.modules.shipManage.param.YxShipOperationResponse;
import co.yixiang.modules.shipManage.param.YxShipPassengerResponse;
import co.yixiang.modules.shipManage.service.YxShipOperationService;
import co.yixiang.modules.shipManage.service.dto.YxShipOperationDto;
import co.yixiang.modules.shipManage.service.dto.YxShipOperationQueryCriteria;
import co.yixiang.modules.shipManage.service.mapper.YxShipOperationMapper;
import co.yixiang.modules.shipManage.service.mapper.YxShipPassengerMapper;
import co.yixiang.utils.CommonsUtils;
import co.yixiang.utils.DateUtils;
import co.yixiang.utils.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
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
* @date 2020-11-05
*/
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "yxShipOperation")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxShipOperationServiceImpl extends BaseServiceImpl<YxShipOperationMapper, YxShipOperation> implements YxShipOperationService {

    private final IGenerator generator;
    @Autowired
    private YxShipOperationMapper yxShipOperationMapper;
    @Autowired
    private YxShipPassengerMapper yxShipPassengerMapper;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxShipOperationQueryCriteria criteria, Pageable pageable) {
       /* getPage(pageable);
        PageInfo<YxShipOperation> page = new PageInfo<>(queryAll(criteria));*/
        QueryWrapper<YxShipOperation> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(YxShipOperation::getDelFlag ,0);

        IPage<YxShipOperation> ipage = this.page(new Page<>(pageable.getPageNumber() + 1, pageable.getPageSize()), queryWrapper);
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(ipage.getRecords(), YxShipOperationDto.class));
        map.put("totalElements", ipage.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YxShipOperation> queryAll(YxShipOperationQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YxShipOperation.class, criteria));
    }


    @Override
    public void download(List<YxShipOperationDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxShipOperationDto yxShipOperation : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("船只出港批次号", yxShipOperation.getBatchNo());
            map.put("船只id", yxShipOperation.getShipId());
            map.put("船只名称", yxShipOperation.getShipName());
            map.put("船长id", yxShipOperation.getCaptainId());
            map.put("船长姓名", yxShipOperation.getCaptainName());
            map.put("承载人数", yxShipOperation.getTotalPassenger());
            map.put("老年人人数", yxShipOperation.getOldPassenger());
            map.put("未成年人数", yxShipOperation.getUnderagePassenger());
            map.put("出港时间", yxShipOperation.getLeaveTime());
            map.put("回港时间", yxShipOperation.getReturnTime());
            map.put("船只状态 0:待出港 1：出港 2：回港", yxShipOperation.getStatus());
            map.put("是否删除（0：未删除，1：已删除）", yxShipOperation.getDelFlag());
            map.put("创建人", yxShipOperation.getCreateUserId());
            map.put("修改人", yxShipOperation.getUpdateUserId());
            map.put("创建时间", yxShipOperation.getCreateTime());
            map.put("更新时间", yxShipOperation.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * 海岸支队大屏（船只出海记录列表）
     * @return
     */
    @Override
    public List<YxShipOperationResponse> findOperationList(YxShipOperationQueryCriteria criteria,Pageable pageable){
        //
        List<YxShipOperationResponse> shipOperationResponseList = new ArrayList<>();
        //查询运营记录信息
        getPage(pageable);
        PageInfo<YxShipOperation> page = new PageInfo<>(queryAll(criteria));
        List<YxShipOperation> shipOperationList = page.getList();
        if(CollectionUtils.isEmpty(shipOperationList)){
            return shipOperationResponseList;
        }
        shipOperationResponseList = CommonsUtils.convertBeanList(shipOperationList,YxShipOperationResponse.class);
        for(YxShipOperationResponse response:shipOperationResponseList){
            //价格
            BigDecimal totlePrice = yxShipOperationMapper.getBatchTotlePrice(response.getBatchNo());
            response.setTotlePricet(totlePrice);
            //船只状态
            String strStatus ="";
            switch (response.getStatus()){
                case 0:
                    strStatus = "待出港";break;
                case 1:
                    strStatus="出港"; break;
                case 2:
                    strStatus="回港";break;
            }
            response.setStatusValue(strStatus);
            //离港时间
            response.setLeaveForTime(DateUtils.timestampToStr10(response.getLeaveTime()));
            //返港时间
            response.setReturnForTime(DateUtils.timestampToStr10(response.getReturnTime()));
            //乘客信息
            QueryWrapper<YxShipPassenger> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(YxShipPassenger::getDelFlag, 0).eq(YxShipPassenger::getBatchNo,response.getBatchNo());
            List<YxShipPassenger> shipPassengerList = yxShipPassengerMapper.selectList(queryWrapper);
            if(!CollectionUtils.isEmpty(shipPassengerList)){
                List<YxShipPassengerResponse> passengerResponseList = CommonsUtils.convertBeanList(shipPassengerList,YxShipPassengerResponse.class);
                response.setListPassenger(passengerResponseList);
            }
        }

        return  shipOperationResponseList;
    }
}
