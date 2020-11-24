/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.coupon.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.exception.BadRequestException;
import co.yixiang.modules.coupon.domain.YxCoupons;
import co.yixiang.modules.coupon.domain.YxCouponsPriceConfig;
import co.yixiang.modules.coupon.domain.YxCouponsPriceConfigRequest;
import co.yixiang.modules.coupon.service.YxCouponsPriceConfigService;
import co.yixiang.modules.coupon.service.dto.YxCouponsPriceConfigDto;
import co.yixiang.modules.coupon.service.dto.YxCouponsPriceConfigQueryCriteria;
import co.yixiang.modules.coupon.service.mapper.YxCouponsMapper;
import co.yixiang.modules.coupon.service.mapper.YxCouponsPriceConfigMapper;
import co.yixiang.utils.BeanUtils;
import co.yixiang.utils.FileUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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
 * @date 2020-11-04
 */
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "yxCouponsPriceConfig")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxCouponsPriceConfigServiceImpl extends BaseServiceImpl<YxCouponsPriceConfigMapper, YxCouponsPriceConfig> implements YxCouponsPriceConfigService {

    private final IGenerator generator;
    @Autowired
    private YxCouponsPriceConfigMapper couponsPriceConfigMapper;
    @Autowired
    private YxCouponsMapper yxCouponsMapper;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxCouponsPriceConfigQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YxCouponsPriceConfig> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YxCouponsPriceConfigDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YxCouponsPriceConfig> queryAll(YxCouponsPriceConfigQueryCriteria criteria) {
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YxCouponsPriceConfig.class, criteria));
    }


    @Override
    public void download(List<YxCouponsPriceConfigDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxCouponsPriceConfigDto yxCouponsPriceConfig : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("卡券id", yxCouponsPriceConfig.getCouponId());
            map.put("开始日期(YYYYMMDD)", yxCouponsPriceConfig.getStartDate());
            map.put("结束日期(YYYYMMDD)", yxCouponsPriceConfig.getEndDate());
            map.put("销售价格", yxCouponsPriceConfig.getSellingPrice());
            map.put("佣金", yxCouponsPriceConfig.getCommission());
            map.put("景区推广价格", yxCouponsPriceConfig.getScenicPrice());
            map.put("旅行社价格", yxCouponsPriceConfig.getTravelPrice());
            map.put("是否删除（0：未删除，1：已删除）", yxCouponsPriceConfig.getDelFlag());
            map.put("创建人", yxCouponsPriceConfig.getCreateUserId());
            map.put("修改人", yxCouponsPriceConfig.getUpdateUserId());
            map.put("创建时间", yxCouponsPriceConfig.getCreateTime());
            map.put("更新时间", yxCouponsPriceConfig.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public void setPriceConfig(String priceJson, Integer currUserId) {
        JSONObject jsonObject = JSONUtil.parseObj(priceJson);
        String couponIdStr = jsonObject.get("couponId").toString();
        if (StringUtils.isEmpty(couponIdStr)) {
            throw new BadRequestException("卡券id不能为空 ！");
        }
        int couponId = Integer.parseInt(couponIdStr);
        //查询
        YxCoupons yxCoupons = yxCouponsMapper.selectById(couponId);
        if (null == yxCoupons) {
            throw new BadRequestException("根据卡券id：" + couponId + " 获取卡券信息失败！");
        }
        // 处理添加数据
        List<YxCouponsPriceConfigRequest> configList = JSON.parseArray(jsonObject.get("data").toString(), YxCouponsPriceConfigRequest.class);
        if (CollectionUtils.isEmpty(configList)) {
            throw new BadRequestException("添加失败！");
        }
        // 入库list
        List<YxCouponsPriceConfig> resultList = new ArrayList<>();
        // 时间段check用list
        List<String> timeList = new ArrayList<>();
        // 时间段check用下标
        Integer index = 0;
        for (YxCouponsPriceConfigRequest param : configList) {
            YxCouponsPriceConfig priceConfig = new YxCouponsPriceConfig();
            BeanUtils.copyProperties(param, priceConfig);
            Integer startDateInt = Integer.parseInt(param.getStartDateStr());
            Integer endDateInt = Integer.parseInt(param.getEndDateStr());
            if (startDateInt > endDateInt) {
                throw new BadRequestException("时间配置错误");
            }
            // 时间list处理
            timeList.add(index + "-" + startDateInt + "-" + endDateInt);
            // 时间下标+1
            index += 1;
            priceConfig.setStartDate(startDateInt);
            priceConfig.setEndDate(endDateInt);
            priceConfig.setCouponId(couponId);
            priceConfig.setCreateUserId(currUserId);
            priceConfig.setUpdateUserId(currUserId);
            priceConfig.setDelFlag(0);
            int intCommFlg = priceConfig.getSellingPrice().subtract(yxCoupons.getSettlementPrice()).compareTo(BigDecimal.ZERO);
            if (intCommFlg < 0) {
                throw new BadRequestException("佣金不正确!");
            }
            BigDecimal bigComm = priceConfig.getSellingPrice().subtract(yxCoupons.getSettlementPrice());
            priceConfig.setCommission(bigComm);
            resultList.add(priceConfig);
        }
        // 校验时间是否有重叠
        if (timeList.size() > 1 && !checkTimeList(timeList)) {
            throw new BadRequestException("时间设置不可重叠");
        }

        //先删除
        QueryWrapper<YxCouponsPriceConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(YxCouponsPriceConfig::getCouponId, couponId);
        couponsPriceConfigMapper.delete(queryWrapper);
        // 后添加
        this.saveBatch(resultList);
    }

    /**
     * 判断时间是否重叠
     *
     * @param timeList
     * @return
     */
    private boolean checkTimeList(List<String> timeList) {
        for (String itemOut : timeList) {
            // 判断时间有没有跟其他时间重复
            String[] itemOutStr = itemOut.split("-");
            // 获取下标、开始、结束时间
            Integer outIdx = Integer.parseInt(itemOutStr[0]);
            Integer outStart = Integer.parseInt(itemOutStr[1]);
            Integer outEnd = Integer.parseInt(itemOutStr[2]);
            // 再次遍历list判断当前时间段是否与列表其他时间段重复
            for (String itemIn : timeList) {
                String[] itemInStr = itemIn.split("-");
                Integer inIdx = Integer.parseInt(itemInStr[0]);
                Integer inStart = Integer.parseInt(itemInStr[1]);
                Integer inEnd = Integer.parseInt(itemInStr[2]);
                // 下标相同的不做处理
                if (inIdx.equals(outIdx)) {
                    continue;
                }
                // 1.判定时间在当前时间段之前
                boolean check1 = outEnd < inStart;
                // 2.判定时间在当前时间段之后
                boolean check2 = outStart > inEnd;
                if (!check2 && !check1) {
                    return false;
                }
            }
        }
        return true;
    }


}
