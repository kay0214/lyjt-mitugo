/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.shop.service.impl;

import co.yixiang.constant.ShopConstants;
import co.yixiang.modules.shop.domain.*;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.modules.shop.service.mapper.YxImageInfoMapper;
import co.yixiang.modules.shop.service.mapper.YxStoreAttributeMapper;
import co.yixiang.utils.BeanUtils;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import co.yixiang.dozer.service.IGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.utils.ValidationUtil;
import co.yixiang.utils.FileUtil;
import co.yixiang.modules.shop.service.YxStoreInfoService;
import co.yixiang.modules.shop.service.dto.YxStoreInfoDto;
import co.yixiang.modules.shop.service.dto.YxStoreInfoQueryCriteria;
import co.yixiang.modules.shop.service.mapper.YxStoreInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
// 默认不使用缓存
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

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
    private YxImageInfoMapper yxImageInfoMapper;
    @Autowired
    private YxStoreAttributeMapper yxStoreAttributeMapper;
    @Autowired
    private YxImageInfoServiceImpl yxImageInfoService;
    @Autowired
    private YxStoreAttributeServiceImpl yxStoreAttributeService;


    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxStoreInfoQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YxStoreInfo> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YxStoreInfoDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YxStoreInfo> queryAll(YxStoreInfoQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YxStoreInfo.class, criteria));
    }

    /**
     * 更新店铺信息
     * @param request
     * @return
     */
    @Transactional
    @Override
    public boolean updateStoreInfo(YxStoreInfoRequest request){
        YxStoreInfo yxStoreInfo = new YxStoreInfo();
        BeanUtils.copyProperties(request,yxStoreInfo);
        //
       boolean isUpd = this.updateById(yxStoreInfo);
        List<YxImageInfo> imageInfoList = yxImageInfoService.list(new QueryWrapper<YxImageInfo>().eq("type_id",yxStoreInfo.getId()).eq("img_type",ShopConstants.IMG_TYPE_STORE));

        if(CollectionUtils.isNotEmpty(imageInfoList)){
            yxImageInfoService.remove(new QueryWrapper<YxImageInfo>().eq("type_id",yxStoreInfo.getId()).eq("img_type",ShopConstants.IMG_TYPE_STORE));
        }
        //批量保存图片信息
        List<YxImageInfo> yxImageInfoList = new ArrayList<YxImageInfo>();
        //图片
        YxImageInfo yxImageInfo = new YxImageInfo();
        yxImageInfo.setTypeId(yxStoreInfo.getId());
        yxImageInfo.setImgType(ShopConstants.IMG_TYPE_STORE);
        yxImageInfo.setImgCategory(ShopConstants.IMG_CATEGORY_PIC);
        yxImageInfo.setImgUrl(request.getImg());
        yxImageInfo.setDelFlag(false);
        yxImageInfoList.add(yxImageInfo);
        String [] images = request.getOpenTime().split(",");
        if(images.length>0){
            for(int i=0;i<images.length;i++){
                YxImageInfo yxImageInfos = new YxImageInfo();
                yxImageInfos.setTypeId(yxStoreInfo.getId());
                yxImageInfos.setImgType(ShopConstants.IMG_TYPE_STORE);
                yxImageInfos.setImgCategory(ShopConstants.IMG_CATEGORY_ROTATION1);
                yxImageInfos.setImgUrl(images[i]);
                yxImageInfos.setDelFlag(false);
                yxImageInfoList.add(yxImageInfos);
            }
        }
        yxImageInfoService.saveBatch(yxImageInfoList,yxImageInfoList.size());

        //保存属性信息
        //店铺服务
        List<YxStoreAttribute> storeAttributeList = new ArrayList<YxStoreAttribute>();
        String[] service = request.getStoreService().split(",");
        if(service.length>0){
            for(int i=0;i<service.length;i++){
                YxStoreAttribute yxStoreattribute = new YxStoreAttribute();
                //0：运营时间，1：店铺服务
                yxStoreattribute.setAttributeType(1);
                yxStoreattribute.setStoreId(yxStoreInfo.getId());
                yxStoreattribute.setAttributeValue1(service[i]);
                storeAttributeList.add(yxStoreattribute);
            }
            yxStoreAttributeService.saveBatch(storeAttributeList);
        }
        if(!isUpd){
            throw new RuntimeException("店铺修改信息，更新失败！");
        }
        return isUpd;
    }
    @Override
    public void download(List<YxStoreInfoDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxStoreInfoDto yxStoreInfo : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("店铺编号", yxStoreInfo.getStoreNid());
            map.put("店铺名称", yxStoreInfo.getStoreName());
            map.put("管理人用户名", yxStoreInfo.getManageUserName());
            map.put("商户id", yxStoreInfo.getMerId());
            map.put("合伙人id", yxStoreInfo.getPartnerId());
            map.put("管理人电话", yxStoreInfo.getManageMobile());
            map.put("店铺电话", yxStoreInfo.getStoreMobile());
            map.put("状态:", yxStoreInfo.getStatus()==0?"上架":"下架");
            map.put("人均消费", yxStoreInfo.getPerCapita());
            map.put("行业类别", yxStoreInfo.getIndustryCategory());
            map.put("店铺省市区", yxStoreInfo.getStoreProvince());
            map.put("店铺详细地址", yxStoreInfo.getStoreAddress());
            map.put("创建人", yxStoreInfo.getCreateUserId());
            map.put("修改人", yxStoreInfo.getUpdateUserId());
            map.put("创建时间", yxStoreInfo.getCreateTime());
            map.put("更新时间", yxStoreInfo.getUpdateTime());
            map.put("店铺介绍", yxStoreInfo.getIntroduction());
            map.put("地图坐标经度", yxStoreInfo.getCoordinateX());
            map.put("地图坐标纬度", yxStoreInfo.getCoordinateY());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
