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
import co.yixiang.modules.shop.domain.YxImageInfo;
import co.yixiang.modules.shop.service.YxImageInfoService;
import co.yixiang.modules.shop.service.dto.YxImageInfoDto;
import co.yixiang.modules.shop.service.dto.YxImageInfoQueryCriteria;
import co.yixiang.modules.shop.service.mapper.YxImageInfoMapper;
import co.yixiang.utils.FileUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
 * @date 2020-08-14
 */
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "yxImageInfo")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxImageInfoServiceImpl extends BaseServiceImpl<YxImageInfoMapper, YxImageInfo> implements YxImageInfoService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxImageInfoQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YxImageInfo> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YxImageInfoDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YxImageInfo> queryAll(YxImageInfoQueryCriteria criteria) {
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YxImageInfo.class, criteria));
    }


    @Override
    public void download(List<YxImageInfoDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxImageInfoDto yxImageInfo : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("类型id（关联id）", yxImageInfo.getTypeId());
            map.put("图片类型（1：卡券，2：店铺，3：商品，4：商户相关）", yxImageInfo.getImgType());
            map.put("图片类别 1：缩略图/图片，2：轮播图。以下选项针对商户 1：个人手持身份证,2：个人证件照人像面，3：个人证件照国徽面，4：营业执照，5：银行开户证明，6：法人身份证头像面，7：法人身份证头像面国徽面，8：门店照及经营场所，9： 医疗机构许可证", yxImageInfo.getImgCategory());
            map.put("图片地址", yxImageInfo.getImgUrl());
            map.put("是否删除（0：未删除，1：已删除）", yxImageInfo.getDelFlag());
            map.put("创建人", yxImageInfo.getCreateUserId());
            map.put("修改人", yxImageInfo.getUpdateUserId());
            map.put("创建时间", yxImageInfo.getCreateTime());
            map.put("更新时间", yxImageInfo.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * 查询图片
     *
     * @param typeId
     * @param imgType
     * @param cateTypeId
     * @return
     */
    @Override
    public String selectImgByParam(int typeId, Integer imgType, Integer cateTypeId) {
        YxImageInfo imageInfoParam = new YxImageInfo();
        imageInfoParam.setTypeId(typeId);
        imageInfoParam.setImgType(imgType);
        imageInfoParam.setImgCategory(cateTypeId);
        String strImg = "";
        List<YxImageInfo> imageInfoList = list(Wrappers.query(imageInfoParam));
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(imageInfoList)) {
            for (YxImageInfo imageInfo : imageInfoList) {
                strImg = strImg.concat(imageInfo.getImgUrl()).concat(",");
            }
            strImg = strImg.substring(0, strImg.length() - 1);
        }
        return strImg;
    }
}
