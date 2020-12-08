/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.shipManage.service.impl;

import cn.hutool.core.date.DateTime;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.constant.ShopConstants;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.exception.BadRequestException;
import co.yixiang.modules.shipManage.domain.YxShipInfo;
import co.yixiang.modules.shipManage.domain.YxShipSeries;
import co.yixiang.modules.shipManage.param.YxShipInfoRequest;
import co.yixiang.modules.shipManage.service.YxShipInfoService;
import co.yixiang.modules.shipManage.service.dto.YxShipInfoDto;
import co.yixiang.modules.shipManage.service.dto.YxShipInfoQueryCriteria;
import co.yixiang.modules.shipManage.service.mapper.YxShipInfoMapper;
import co.yixiang.modules.shipManage.service.mapper.YxShipSeriesMapper;
import co.yixiang.modules.shop.domain.YxImageInfo;
import co.yixiang.modules.shop.domain.YxStoreInfo;
import co.yixiang.modules.shop.service.YxImageInfoService;
import co.yixiang.modules.shop.service.mapper.UserSysMapper;
import co.yixiang.modules.shop.service.mapper.YxStoreInfoMapper;
import co.yixiang.utils.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
//@CacheConfig(cacheNames = "yxShipInfo")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxShipInfoServiceImpl extends BaseServiceImpl<YxShipInfoMapper, YxShipInfo> implements YxShipInfoService {

    private final IGenerator generator;
    @Autowired
    private YxShipSeriesMapper shipSeriesMapper;
    @Autowired
    private UserSysMapper userSysMapper;
    @Autowired
    private YxStoreInfoMapper yxStoreInfoMapper;
    @Autowired
    private YxImageInfoService yxImageInfoService;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxShipInfoQueryCriteria criteria, Pageable pageable) {
        QueryWrapper<YxShipInfo> queryWrapper = new QueryWrapper<>();

        if (0 != criteria.getUserRole()) {
            if (null == criteria.getChildUser() || criteria.getChildUser().size() <= 0) {
                Map<String, Object> map = new LinkedHashMap<>(2);
                map.put("content", new ArrayList<>());
                map.put("totalElements", 0);
                return map;
            }
            queryWrapper.lambda().in(YxShipInfo::getMerId, criteria.getChildUser()).eq(YxShipInfo::getDelFlag, 0);
        }
        if (StringUtils.isNotBlank(criteria.getShipName())) {
            queryWrapper.lambda().like(YxShipInfo::getShipName, criteria.getShipName());
        }
        if (null!=criteria.getSeriesId()) {
            queryWrapper.lambda().eq(YxShipInfo::getSeriesId, criteria.getSeriesId());
        }

        if (null!=criteria.getCurrentStatus()) {
            queryWrapper.lambda().eq(YxShipInfo::getCurrentStatus, criteria.getCurrentStatus());
        }

        IPage<YxShipInfo> ipage = this.page(new Page<>(pageable.getPageNumber() + 1, pageable.getPageSize()), queryWrapper);
        List<YxShipInfo> yxShipInfoList = ipage.getRecords();
        List<YxShipInfoDto> yxShipInfoDtoList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(yxShipInfoList)){
            yxShipInfoDtoList = CommonsUtils.convertBeanList(yxShipInfoList,YxShipInfoDto.class);
            for(YxShipInfoDto yxShipInfoDto:yxShipInfoDtoList){
                QueryWrapper<YxImageInfo> queryWrapperImg= new QueryWrapper<YxImageInfo>();
                queryWrapperImg.lambda().eq(YxImageInfo::getTypeId, yxShipInfoDto.getId())
                        .eq(YxImageInfo::getImgType, ShopConstants.IMG_TYPE_SHIPINFO)
                        .eq(YxImageInfo::getDelFlag, 0);
                YxImageInfo imageInfo = yxImageInfoService.getOne(queryWrapperImg);
                if(null!=imageInfo){yxShipInfoDto.setImageUrl(imageInfo.getImgUrl());}
            }
        }
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", yxShipInfoDtoList);
        map.put("totalElements", ipage.getTotal());

        return map;
    }


    @Override
    //@Cacheable
    public List<YxShipInfo> queryAll(YxShipInfoQueryCriteria criteria) {
        QueryWrapper<YxShipInfo> queryWrapper = new QueryWrapper<>();

        if (0 != criteria.getUserRole()) {
            if (null == criteria.getChildUser() || criteria.getChildUser().size() <= 0) {
                return null;
            }
            queryWrapper.lambda().in(YxShipInfo::getMerId, criteria.getChildUser()).eq(YxShipInfo::getDelFlag, 0);
        }
        if (StringUtils.isNotBlank(criteria.getShipName())) {
            queryWrapper.lambda().like(YxShipInfo::getShipName, criteria.getShipName());
        }
        if (null!=criteria.getSeriesId()) {
            queryWrapper.lambda().eq(YxShipInfo::getSeriesId, criteria.getSeriesId());
        }

        if (null!=criteria.getCurrentStatus()) {
            queryWrapper.lambda().eq(YxShipInfo::getCurrentStatus, criteria.getCurrentStatus());
        }
        return baseMapper.selectList(queryWrapper);
//        return baseMapper.selectList(QueryHelpPlus.getPredicate(YxShipInfo.class, criteria));
    }


    @Override
    public void download(List<YxShipInfoDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxShipInfoDto yxShipInfo : all) {
            Map<String, Object> map = new LinkedHashMap<>();
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

    /**
     * 获取状态为启用的船只系列
     * @return
     */
    @Override
    public List<YxShipSeries> getShipSeriseList(List<Long> merId) {
        QueryWrapper<YxShipSeries> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(YxShipSeries::getStatus, 0).eq(YxShipSeries::getDelFlag, 0);
        if(CollectionUtils.isNotEmpty(merId)){
            queryWrapper.lambda().in(YxShipSeries::getMerId,merId);
        }
        return shipSeriesMapper.selectList(queryWrapper);
    }

    /**
     * 根据船只系列，以及商户id获取船只信息
     * @param seriseId
     * @param merId
     * @return
     */
    @Override
    public List<YxShipInfo> getShipInfoList(int seriseId, List<Long> merId) {
        QueryWrapper<YxShipInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(YxShipInfo::getSeriesId, seriseId).eq(YxShipInfo::getShipStatus, 0).eq(YxShipInfo::getDelFlag, 0);
        if(CollectionUtils.isNotEmpty(merId)){
            queryWrapper.lambda().in(YxShipInfo::getMerId, merId);
        }
        return this.list(queryWrapper);
    }

    /**
     * 保存船只信息
     * @param resources
     */
    @Override
    public boolean saveOrUpdShipInfoByParam(YxShipInfoRequest resources) {
        int loginUserId = SecurityUtils.getUserId().intValue();
        YxShipInfo yxShipInfo = new YxShipInfo();
        BeanUtils.copyProperties(resources,yxShipInfo);
        if(null==resources.getStoreId()){
            QueryWrapper<YxStoreInfo> queryWrapperStore = new QueryWrapper<>();
            queryWrapperStore.lambda().eq(YxStoreInfo::getMerId, loginUserId);
            YxStoreInfo yxStoreInfo = yxStoreInfoMapper.selectOne(queryWrapperStore);
            if (null == yxStoreInfo) {
                throw new BadRequestException("商户id：" + loginUserId + "，店铺查找失败！");
            }
            yxShipInfo.setStoreId( yxStoreInfo.getId());
        }
        if(null==resources.getMerId()){
            yxShipInfo.setMerId(loginUserId);
        }
//        yxShipInfo.setMerId(loginUserId);
        yxShipInfo.setUpdateUserId(loginUserId);
        yxShipInfo.setUpdateTime(DateTime.now().toTimestamp());
        boolean flg;
        if (null == resources.getId()) {
            //添加
            //默认在港
            yxShipInfo.setCurrentStatus(0);
            yxShipInfo.setDelFlag(0);
            yxShipInfo.setCreateUserId(loginUserId);
            yxShipInfo.setCreateTime(DateTime.now().toTimestamp());
            flg = this.save(yxShipInfo);
        } else {
            //修改
            flg =  this.updateById(yxShipInfo);
        }
        saveImgList(yxShipInfo.getId(),resources.getImageUrl());
        return flg;
    }


    private void saveImgList(int id,String imageUrl) {
        List<YxImageInfo> imageInfoList = yxImageInfoService.list(new QueryWrapper<YxImageInfo>().eq("type_id", id).eq("img_type", ShopConstants.IMG_TYPE_SHIPINFO).eq("del_flag", 0));

        if (CollectionUtils.isNotEmpty(imageInfoList)) {
            yxImageInfoService.remove(new QueryWrapper<YxImageInfo>().eq("type_id", id).eq("img_type", ShopConstants.IMG_TYPE_SHIPINFO).eq("del_flag", 0));
        }
        //图片
        YxImageInfo yxImageInfo = new YxImageInfo();
        yxImageInfo.setTypeId(id);
        yxImageInfo.setImgType(ShopConstants.IMG_TYPE_SHIPINFO);
        yxImageInfo.setImgCategory(ShopConstants.IMG_CATEGORY_PIC);
        yxImageInfo.setImgUrl(imageUrl);
        yxImageInfo.setDelFlag(0);
        yxImageInfoService.save(yxImageInfo);
    }
    /**
     * 根据id修改船只状态
     * @param id
     */
    @Override
    public void changeStatus(int id) {
        YxShipInfo yxShipInfo = this.getById(id);
        if (null == yxShipInfo) {
            throw new BadRequestException("根据船只id：" + id + " 获取数据错误！");
        }
        int statusShip = 0;
        if (yxShipInfo.getShipStatus() == 0) {
            statusShip = 1;
        }
        yxShipInfo.setShipStatus(statusShip);
        yxShipInfo.setUpdateUserId(SecurityUtils.getUserId().intValue());
        yxShipInfo.setUpdateTime(DateTime.now().toTimestamp());
        this.updateById(yxShipInfo);
    }
}
