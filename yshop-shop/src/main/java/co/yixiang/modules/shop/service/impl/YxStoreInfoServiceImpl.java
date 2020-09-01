/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.shop.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.constant.ShopConstants;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.modules.mybatis.GeoPoint;
import co.yixiang.modules.shop.domain.*;
import co.yixiang.modules.shop.service.YxStoreInfoService;
import co.yixiang.modules.shop.service.YxSystemAttachmentService;
import co.yixiang.modules.shop.service.dto.YxStoreInfoDto;
import co.yixiang.modules.shop.service.dto.YxStoreInfoQueryCriteria;
import co.yixiang.modules.shop.service.mapper.YxStoreInfoMapper;
import co.yixiang.utils.BeanUtils;
import co.yixiang.utils.FileUtil;
import co.yixiang.utils.SecurityUtils;
import co.yixiang.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

// 默认不使用缓存
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;

/**
 * @author nxl
 * @date 2020-08-14
 */
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "yxStoreInfo")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxStoreInfoServiceImpl extends BaseServiceImpl<YxStoreInfoMapper, YxStoreInfo> implements YxStoreInfoService {

    private final IGenerator generator;

    @Autowired
    private YxStoreInfoMapper yxStoreInfoMapper;
    @Autowired
    private YxImageInfoServiceImpl yxImageInfoService;
    @Autowired
    private YxStoreAttributeServiceImpl yxStoreAttributeService;
    @Autowired
    private YxSystemAttachmentService yxSystemAttachmentService;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxStoreInfoQueryCriteria criteria, Pageable pageable) {
//        getPage(pageable);
//        PageInfo<YxStoreInfo> page = new PageInfo<>(queryAll(criteria));
        QueryWrapper<YxStoreInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        if (0 != criteria.getUserRole()) {
            if (null == criteria.getChildUser() || criteria.getChildUser().size() <= 0) {
                Map<String, Object> map = new LinkedHashMap<>(2);
                map.put("content", new ArrayList<>());
                map.put("totalElements", 0);
                return map;
            }
            queryWrapper.lambda().in(YxStoreInfo::getMerId, criteria.getChildUser()).eq(YxStoreInfo::getDelFlag, 0);
        }
        if (StringUtils.isNotBlank(criteria.getStoreName())) {
            queryWrapper.lambda().like(YxStoreInfo::getStoreName, criteria.getStoreName());
        }
        IPage<YxStoreInfo> ipage = this.page(new Page<>(pageable.getPageNumber() + 1, pageable.getPageSize()), queryWrapper);

        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(ipage.getRecords(), YxStoreInfoDto.class));
        map.put("totalElements", ipage.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YxStoreInfo> queryAll(YxStoreInfoQueryCriteria criteria) {
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YxStoreInfo.class, criteria));
    }

    /**
     * 更新店铺信息
     *
     * @param request
     * @return
     */
    @Transactional
    @Override
    public boolean updateStoreInfo(YxStoreInfoRequest request) {
        YxStoreInfo yxStoreInfo = new YxStoreInfo();
        BeanUtils.copyProperties(request, yxStoreInfo);
        //
        GeoPoint geoPoint = new GeoPoint(new BigDecimal(request.getCoordinateX()), new BigDecimal(request.getCoordinateX()));
        yxStoreInfo.setCoordinate(geoPoint);

        yxStoreInfo.setUpdateUserId(SecurityUtils.getUserId().intValue());
        yxStoreInfo.setDelFlag(0);
//        boolean isUpd = this.update(yxStoreInfo,new QueryWrapper<YxStoreInfo>().eq("id",request.getId()));
        boolean isUpd = yxStoreInfoMapper.updateByPrimaryKey(yxStoreInfo) > 0 ? true : false;
        List<YxImageInfo> imageInfoList = yxImageInfoService.list(new QueryWrapper<YxImageInfo>().eq("type_id", yxStoreInfo.getId()).eq("img_type", ShopConstants.IMG_TYPE_STORE).eq("del_flag", 0));

        if (CollectionUtils.isNotEmpty(imageInfoList)) {
            yxImageInfoService.remove(new QueryWrapper<YxImageInfo>().eq("type_id", yxStoreInfo.getId()).eq("img_type", ShopConstants.IMG_TYPE_STORE).eq("del_flag", 0));
        }
        //批量保存图片信息
        List<YxImageInfo> yxImageInfoList = new ArrayList<YxImageInfo>();
        //图片
        YxImageInfo yxImageInfo = new YxImageInfo();
        yxImageInfo.setTypeId(yxStoreInfo.getId());
        yxImageInfo.setImgType(ShopConstants.IMG_TYPE_STORE);
        yxImageInfo.setImgCategory(ShopConstants.IMG_CATEGORY_PIC);
        yxImageInfo.setImgUrl(request.getImageArr());
        yxImageInfo.setDelFlag(0);
        yxImageInfoList.add(yxImageInfo);
        if (StringUtils.isNotBlank(request.getSliderImageArr())) {
            String[] images = request.getSliderImageArr().split(",");
            if (images.length > 0) {
                for (int i = 0; i < images.length; i++) {
                    YxImageInfo yxImageInfos = new YxImageInfo();
                    yxImageInfos.setTypeId(yxStoreInfo.getId());
                    yxImageInfos.setImgType(ShopConstants.IMG_TYPE_STORE);
                    yxImageInfos.setImgCategory(ShopConstants.IMG_CATEGORY_ROTATION1);
                    yxImageInfos.setImgUrl(images[i]);
                    yxImageInfos.setDelFlag(0);
                    yxImageInfos.setUpdateUserId(SecurityUtils.getUserId().intValue());
                    yxImageInfos.setCreateUserId(SecurityUtils.getUserId().intValue());
                    yxImageInfoList.add(yxImageInfos);
                }
            }
            yxImageInfoService.saveBatch(yxImageInfoList, yxImageInfoList.size());
        }


        //保存属性信息
        //店铺服务
        List<YxStoreAttribute> serviceAttribute = yxStoreAttributeService.list(new QueryWrapper<YxStoreAttribute>().eq("store_id", yxStoreInfo.getId()).eq("attribute_type", 1).eq("del_flag", 0));
        if (CollectionUtils.isNotEmpty(serviceAttribute)) {
            //删除之前的
            yxStoreAttributeService.remove(new QueryWrapper<YxStoreAttribute>().eq("store_id", yxStoreInfo.getId()).eq("attribute_type", 1));
        }
        List<YxStoreAttribute> storeAttributeList = new ArrayList<YxStoreAttribute>();

        if (CollectionUtils.isNotEmpty(request.getStoreService())) {
            for (String service : request.getStoreService()) {
                YxStoreAttribute yxStoreattribute = new YxStoreAttribute();
                //0：运营时间，1：店铺服务
                yxStoreattribute.setAttributeType(1);
                yxStoreattribute.setStoreId(yxStoreInfo.getId());
                yxStoreattribute.setAttributeValue1(service);
                yxStoreattribute.setUpdateUserId(SecurityUtils.getUserId().intValue());
                yxStoreattribute.setCreateUserId(SecurityUtils.getUserId().intValue());
                yxStoreattribute.setDelFlag(0);
                storeAttributeList.add(yxStoreattribute);
            }
        }
        List<YxStoreAttribute> openAttribute = yxStoreAttributeService.list(new QueryWrapper<YxStoreAttribute>().eq("store_id", yxStoreInfo.getId()).eq("attribute_type", 0).eq("del_flag", 0));

        if (CollectionUtils.isNotEmpty(openAttribute)) {
            yxStoreAttributeService.remove(new QueryWrapper<YxStoreAttribute>().eq("store_id", yxStoreInfo.getId()).eq("attribute_type", 0).eq("del_flag", 0));
        }
        if (CollectionUtils.isNotEmpty(request.getOpenDays())) {
            //
            List<Map<String, String>> valueList = request.getOpenDays();
            if (CollectionUtils.isNotEmpty(valueList)) {
                for (int i = 0; i < valueList.size(); i++) {
                    Map<String, String> mapParam = valueList.get(i);
                    YxStoreAttribute yxStoreattribute = new YxStoreAttribute();
                    yxStoreattribute.setAttributeType(0);
                    yxStoreattribute.setStoreId(yxStoreInfo.getId());
                    yxStoreattribute.setAttributeValue1(mapParam.get("openDay"));
                    yxStoreattribute.setAttributeValue2(mapParam.get("openTime"));
                    yxStoreattribute.setUpdateUserId(SecurityUtils.getUserId().intValue());
                    yxStoreattribute.setCreateUserId(SecurityUtils.getUserId().intValue());
                    yxStoreattribute.setDelFlag(0);
                    storeAttributeList.add(yxStoreattribute);
                }
            }
        }
        //删除详情图片
        QueryWrapper<YxSystemAttachment> queryWrapperAtt = new QueryWrapper();
        queryWrapperAtt.like("name",""+request.getId()+"_%").like("name","%store%");
        yxSystemAttachmentService.remove(queryWrapperAtt);

        //批量保存数据
        yxStoreAttributeService.saveBatch(storeAttributeList);

        if (!isUpd) {
            throw new RuntimeException("店铺修改信息，更新失败！");
        }
        return isUpd;
    }

    @Override
    public void download(List<YxStoreInfoDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxStoreInfoDto yxStoreInfo : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("店铺编号", yxStoreInfo.getStoreNid());
            map.put("店铺名称", yxStoreInfo.getStoreName());
            map.put("管理人用户名", yxStoreInfo.getManageUserName());
          /*  map.put("商户id", yxStoreInfo.getMerId());
            map.put("合伙人id", yxStoreInfo.getPartnerId());*/
            map.put("管理人电话", yxStoreInfo.getManageMobile());
            map.put("店铺电话", yxStoreInfo.getStoreMobile());
            map.put("状态:", yxStoreInfo.getStatus() == 0 ? "上架" : "下架");
            map.put("人均消费", yxStoreInfo.getPerCapita());
            map.put("行业类别", yxStoreInfo.getIndustryCategory());
            map.put("店铺省市区", yxStoreInfo.getStoreProvince());
            map.put("店铺详细地址", yxStoreInfo.getStoreAddress());
            /*map.put("创建人", yxStoreInfo.getCreateUserId());
            map.put("修改人", yxStoreInfo.getUpdateUserId());*/
            map.put("创建时间", yxStoreInfo.getCreateTime());
//            map.put("更新时间", yxStoreInfo.getUpdateTime());
            map.put("店铺介绍", yxStoreInfo.getIntroduction());
           /* map.put("地图坐标经度", yxStoreInfo.getCoordinateX());
            map.put("地图坐标纬度", yxStoreInfo.getCoordinateY());*/
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public void onSale(Integer id, int status) {
        if (status == 1) {
            status = 0;
        } else {
            status = 1;
        }
        yxStoreInfoMapper.updateOnsale(status, id);
    }

    /**
     * 获取店铺编辑信息
     *
     * @param storeId
     * @return
     */
    @Override
    public YxStoreInfoResponse getStoreInfo(int storeId) {
        YxStoreInfoResponse response = new YxStoreInfoResponse();
        YxStoreInfo storeInfo = this.getById(storeId);
        BeanUtils.copyProperties(storeInfo, response);
        // 图片
        response.setImageArr(yxImageInfoService.selectImgByParam(storeId, ShopConstants.IMG_TYPE_STORE, ShopConstants.IMG_CATEGORY_PIC));
        //轮播图
        response.setSliderImageArr(yxImageInfoService.selectImgByParam(storeId, ShopConstants.IMG_TYPE_STORE, ShopConstants.IMG_CATEGORY_ROTATION1));
        //店铺服务
        List<String> serviceLists = new ArrayList<String>();
        if (StringUtils.isNotBlank(selectAttributeByParam((storeId)))) {
            String[] serviceArr = selectAttributeByParam((storeId)).split(",");
            serviceLists = new ArrayList<String>(Arrays.asList(serviceArr));
        }
        response.setStoreService(serviceLists);
        //营业时间
        response.setOpenTime(selectAttributeListByParam(storeId));

        return response;
    }

    /**
     * 店铺服务
     *
     * @param storeId
     * @return
     */
    private String selectAttributeByParam(int storeId) {
        YxStoreAttribute yxStoreAttributeParam = new YxStoreAttribute();
        yxStoreAttributeParam.setStoreId(storeId);
        yxStoreAttributeParam.setAttributeType(1);
        String strAttribute = "";
        List<YxStoreAttribute> attributeList = yxStoreAttributeService.list(Wrappers.query(yxStoreAttributeParam));
        if (CollectionUtils.isNotEmpty(attributeList)) {
            for (YxStoreAttribute attribute : attributeList) {
                strAttribute = strAttribute + attribute.getAttributeValue1() + ",";
            }
        }
        return strAttribute;
    }

    /**
     * 营业时间
     *
     * @param storeId
     * @return
     */
    private List<Map<String, Object>> selectAttributeListByParam(int storeId) {
        YxStoreAttribute yxStoreAttributeParam = new YxStoreAttribute();
        yxStoreAttributeParam.setStoreId(storeId);
        yxStoreAttributeParam.setAttributeType(0);
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        List<YxStoreAttribute> attributeList = yxStoreAttributeService.list(Wrappers.query(yxStoreAttributeParam));
        if (CollectionUtils.isNotEmpty(attributeList)) {
            for (YxStoreAttribute attribute : attributeList) {
                Map<String, Object> mapValue = new HashMap<String, Object>();
                mapValue.put("openDay", attribute.getAttributeValue1());
                mapValue.put("openTime", attribute.getAttributeValue2());
                mapList.add(mapValue);
            }
        }
        return mapList;
    }

    @Override
    public void updateDelFlg(Integer id) {
        yxStoreInfoMapper.updateDelFlg(id);
    }

    /**
     * 根据商户uid获取所有商户的storeId
     *
     * @param childUser
     * @return
     */
    @Override
    public List<Long> getStoreIdByMerId(List<Long> childUser) {
        if (null == childUser || childUser.size() <= 0) {
            return new ArrayList<>();
        }
        List<YxStoreInfo> list = this.list(new QueryWrapper<YxStoreInfo>().lambda().in(YxStoreInfo::getMerId, childUser));
        if (null == list || list.size() <= 0) {
            return new ArrayList<>();
        }
        List<Long> result = new ArrayList<>();
        for (YxStoreInfo item : list) {
            result.add(item.getId().longValue());
        }

        return result;
    }
}
