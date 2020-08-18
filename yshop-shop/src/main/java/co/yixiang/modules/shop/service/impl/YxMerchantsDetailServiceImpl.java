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
import co.yixiang.modules.shop.domain.*;
import co.yixiang.modules.shop.service.*;
import co.yixiang.modules.shop.service.dto.YxMerchantsDetailDto;
import co.yixiang.modules.shop.service.dto.YxMerchantsDetailQueryCriteria;
import co.yixiang.modules.shop.service.mapper.YxMerchantsDetailMapper;
import co.yixiang.utils.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

// 默认不使用缓存
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;

/**
 * @author liusy
 * @date 2020-08-15
 */
@Slf4j
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "yxMerchantsDetail")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxMerchantsDetailServiceImpl extends BaseServiceImpl<YxMerchantsDetailMapper, YxMerchantsDetail> implements YxMerchantsDetailService {

    private final IGenerator generator;
    @Autowired
    private YxImageInfoService yxImageInfoService;
    @Autowired
    private YxExamineLogService yxExamineLogService;
    @Autowired
    private YxStoreInfoService yxStoreInfoService;
    @Autowired
    private UserService userService;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxMerchantsDetailQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YxMerchantsDetailDto> page = new PageInfo<>(queryAll(criteria));

        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YxMerchantsDetailDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YxMerchantsDetailDto> queryAll(YxMerchantsDetailQueryCriteria criteria) {
        List<YxMerchantsDetail> list = baseMapper.selectList(QueryHelpPlus.getPredicate(YxMerchantsDetail.class, criteria));
        if (null == list || list.size() <= 0) {
            return new ArrayList<YxMerchantsDetailDto>();
        }
        List<YxMerchantsDetailDto> result = new ArrayList<>();
        for (YxMerchantsDetail vo : list) {
            YxMerchantsDetailDto dto = generator.convert(vo, YxMerchantsDetailDto.class);
            // 认证类型：0->个人,1->企业,2->个体商户
            if (0 == dto.getMerchantsType()) {
                // 手持证件照
                dto.setPersonIdCard(yxImageInfoService.selectImgByParam(dto.getId(), ShopConstants.IMG_TYPE_MERCHANTS, ShopConstants.IMG_PERSON_IDCARD));
                // 证件照人像面
                dto.setPersonIdCardFace(yxImageInfoService.selectImgByParam(dto.getId(), ShopConstants.IMG_TYPE_MERCHANTS, ShopConstants.IMG_PERSON_IDCARD_FACE));
                // 证件照国徽面
                dto.setPersonIdCardBack(yxImageInfoService.selectImgByParam(dto.getId(), ShopConstants.IMG_TYPE_MERCHANTS, ShopConstants.IMG_PERSON_IDCARD_BACK));
            } else {
                // 营业执照
                dto.setBusinessLicenseImg(yxImageInfoService.selectImgByParam(dto.getId(), ShopConstants.IMG_TYPE_MERCHANTS, ShopConstants.IMG_BUSINESS_LICENSE));
                // 银行开户证明
                dto.setBankOpenProveImg(yxImageInfoService.selectImgByParam(dto.getId(), ShopConstants.IMG_TYPE_MERCHANTS, ShopConstants.IMG_BANK_OPEN_PROVE));
                // 法人身份证头像面
                dto.setLegalIdCardFace(yxImageInfoService.selectImgByParam(dto.getId(), ShopConstants.IMG_TYPE_MERCHANTS, ShopConstants.IMG_LEGAL_IDCARD_FACE));
                // 法人身份证国徽面
                dto.setLegalIdCardBack(yxImageInfoService.selectImgByParam(dto.getId(), ShopConstants.IMG_TYPE_MERCHANTS, ShopConstants.IMG_LEGAL_IDCARD_BACK));
                // 门店照及经营场所
                dto.setStoreImg(yxImageInfoService.selectImgByParam(dto.getId(), ShopConstants.IMG_TYPE_MERCHANTS, ShopConstants.IMG_STORE));
                // 医疗机构许可证
                dto.setLicenceImg(yxImageInfoService.selectImgByParam(dto.getId(), ShopConstants.IMG_TYPE_MERCHANTS, ShopConstants.IMG_LICENCE));
            }
            // 获取审核意见
            QueryWrapper<YxExamineLog> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("type", 2).eq("type_id", dto.getId()).orderByDesc("create_time");
            List<YxExamineLog> yxExamineLogs = this.yxExamineLogService.list(queryWrapper);
            if (null != yxExamineLogs && yxExamineLogs.size() > 0) {
                dto.setExamineRemark(yxExamineLogs.get(0).getRemark());
            }
            result.add(dto);
        }
        return result;
    }

    @Override
    public void download(List<YxMerchantsDetailDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxMerchantsDetailDto yxMerchantsDetail : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("用户id", yxMerchantsDetail.getUid());
            map.put("审批状态：0->待审核,1->通过,2->驳回", yxMerchantsDetail.getExamineStatus());
            map.put("商户地址", yxMerchantsDetail.getAddress());
            map.put("联系人", yxMerchantsDetail.getContacts());
            map.put("联系电话", yxMerchantsDetail.getContactMobile());
            map.put("邮箱", yxMerchantsDetail.getMailbox());
            map.put("认证类型：0->个人,1->企业,2->个体商户", yxMerchantsDetail.getMerchantsType());
            map.put("银行账号", yxMerchantsDetail.getBankNo());
            map.put("开户省市", yxMerchantsDetail.getOpenAccountProvince());
            map.put("银行卡信息：0->对私账号,1->对公账号", yxMerchantsDetail.getBankType());
            map.put("开户名称", yxMerchantsDetail.getOpenAccountName());
            map.put("开户行", yxMerchantsDetail.getOpenAccountBank());
            map.put("开户支行", yxMerchantsDetail.getOpenAccountSubbranch());
            map.put("企业所在省市区", yxMerchantsDetail.getCompanyProvince());
            map.put("企业所在详细地址", yxMerchantsDetail.getCompanyAddress());
            map.put("公司名称", yxMerchantsDetail.getCompanyName());
            map.put("法定代表人", yxMerchantsDetail.getCompanyLegalPerson());
            map.put("公司电话", yxMerchantsDetail.getCompanyPhone());
            map.put("经营类目", yxMerchantsDetail.getBusinessCategory());
            map.put("主体资质类型", yxMerchantsDetail.getQualificationsType());
            map.put("是否删除（0：未删除，1：已删除）", yxMerchantsDetail.getDelFlag());
            map.put("创建人", yxMerchantsDetail.getCreateUserId());
            map.put("修改人", yxMerchantsDetail.getUpdateUserId());
            map.put("创建时间", yxMerchantsDetail.getCreateTime());
            map.put("更新时间", yxMerchantsDetail.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * 根据id查询商铺详情
     *
     * @param id
     * @return
     */
    @Override
    public YxMerchantsDetailDto queryById(Integer id) {
        YxMerchantsDetail yxMerchantsDetail = this.getById(id);
        if (null == yxMerchantsDetail) {
            return null;
        }
        YxMerchantsDetailDto result = generator.convert(yxMerchantsDetail, YxMerchantsDetailDto.class);
        // 认证类型：0->个人,1->企业,2->个体商户
        if (0 == result.getMerchantsType()) {
            // 手持证件照
            result.setPersonIdCard(yxImageInfoService.selectImgByParam(id, ShopConstants.IMG_TYPE_MERCHANTS, ShopConstants.IMG_PERSON_IDCARD));
            // 证件照人像面
            result.setPersonIdCardFace(yxImageInfoService.selectImgByParam(id, ShopConstants.IMG_TYPE_MERCHANTS, ShopConstants.IMG_PERSON_IDCARD_FACE));
            // 证件照国徽面
            result.setPersonIdCardBack(yxImageInfoService.selectImgByParam(id, ShopConstants.IMG_TYPE_MERCHANTS, ShopConstants.IMG_PERSON_IDCARD_BACK));
        } else {
            // 营业执照
            result.setBusinessLicenseImg(yxImageInfoService.selectImgByParam(id, ShopConstants.IMG_TYPE_MERCHANTS, ShopConstants.IMG_BUSINESS_LICENSE));
            // 银行开户证明
            result.setBankOpenProveImg(yxImageInfoService.selectImgByParam(id, ShopConstants.IMG_TYPE_MERCHANTS, ShopConstants.IMG_BANK_OPEN_PROVE));
            // 法人身份证头像面
            result.setLegalIdCardFace(yxImageInfoService.selectImgByParam(id, ShopConstants.IMG_TYPE_MERCHANTS, ShopConstants.IMG_LEGAL_IDCARD_FACE));
            // 法人身份证国徽面
            result.setLegalIdCardBack(yxImageInfoService.selectImgByParam(id, ShopConstants.IMG_TYPE_MERCHANTS, ShopConstants.IMG_LEGAL_IDCARD_BACK));
            // 门店照及经营场所
            result.setStoreImg(yxImageInfoService.selectImgByParam(id, ShopConstants.IMG_TYPE_MERCHANTS, ShopConstants.IMG_STORE));
            // 医疗机构许可证
            result.setLicenceImg(yxImageInfoService.selectImgByParam(id, ShopConstants.IMG_TYPE_MERCHANTS, ShopConstants.IMG_LICENCE));
        }
        // 获取审核意见
        QueryWrapper<YxExamineLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", 2).eq("type_id", id).orderByDesc("create_time");
        List<YxExamineLog> yxExamineLogs = this.yxExamineLogService.list(queryWrapper);
        if (null != yxExamineLogs && yxExamineLogs.size() > 0) {
            result.setExamineRemark(yxExamineLogs.get(0).getRemark());
        }
        return result;
    }

    /**
     * @param resources
     * @return
     */
    @Override
    public boolean createOrUpdate(YxMerchantsDetailDto resources) {
        // 判断下uid是否已经被创建、一个商户只允许有一条数据、创建过的判断是否已审核通过
        // 记录下商家id
        Integer uid = resources.getUid();
        // 是否新增数据
        boolean isNew = true;
        // 处理结果
        boolean result = true;
        // 记录下
        YxMerchantsDetail yxMerchantsDetail = this.getOne(query().eq("uid", uid));
        // 审核通过的直接返回不允许修改
        if (null != yxMerchantsDetail) {
            if (1 == yxMerchantsDetail.getExamineStatus()) {
                log.info("用户：" + resources.getUid() + "已认证通过，不允许再次修改数据!");
                return false;
            }
            isNew = false;
        }

        // 有数据的更新数据、没有数据的新插入数据
        yxMerchantsDetail = generator.convert(resources, YxMerchantsDetail.class);
        if (isNew) {
            result = this.save(yxMerchantsDetail);
        } else {
            result = this.updateById(yxMerchantsDetail);
        }

        // 入库成功更新图片
        if (result) {
            // 图片处理id
            Integer typeId = yxMerchantsDetail.getId();
            // 处理图片入库
            result = insertOrUpdatePic(typeId, resources);
        }
        return result;
    }

    /**
     * 更新审核状态
     *
     * @param resources
     * @return
     */
    @Override
    public boolean updateExamineStatus(YxMerchantsDetailDto resources) {
        YxMerchantsDetail yxMerchantsDetail = getById(resources.getId());
        if (null == yxMerchantsDetail) {
            log.info("审核获取商铺认证id：" + resources.getId() + "详细信息失败！");
            return false;
        }
        yxMerchantsDetail.setExamineStatus(resources.getExamineStatus());
        this.updateById(yxMerchantsDetail);
        YxExamineLog yxExamineLog = new YxExamineLog();
        // 审批类型 1:提现 2:商户信息
        yxExamineLog.setType(1);
        yxExamineLog.setTypeId(resources.getId());
        yxExamineLog.setStatus(resources.getExamineStatus());
        yxExamineLog.setRemark(resources.getExamineRemark());
        yxExamineLogService.save(yxExamineLog);
        // 审核通过生成一个默认店铺
        if (1 == resources.getExamineStatus()) {
            User user = this.userService.getById(yxMerchantsDetail.getUid());
            YxStoreInfo yxStoreInfo = new YxStoreInfo();
            // 店铺编号
            yxStoreInfo.setStoreNid("S" + UUID.randomUUID());
            yxStoreInfo.setStoreName("未命名店铺");
            yxStoreInfo.setManageUserName(yxMerchantsDetail.getContacts());
            yxStoreInfo.setMerId(yxMerchantsDetail.getUid());
            // 重新获取下
            yxStoreInfo.setPartnerId(user.getParentId());
            yxStoreInfo.setManageMobile(yxMerchantsDetail.getContactMobile());
            yxStoreInfo.setStoreMobile(yxMerchantsDetail.getContactMobile());
            // 状态：0：上架，1：下架
            yxStoreInfo.setStatus(1);
            yxStoreInfo.setStoreProvince("");
            yxStoreInfo.setStoreAddress("");
            yxStoreInfoService.save(yxStoreInfo);
        }
        return true;
    }

    /**
     * 图片入库参数组装
     *
     * @param typeId
     * @param resources
     * @return
     */
    private boolean insertOrUpdatePic(Integer typeId, YxMerchantsDetailDto resources) {
        // 插入之前清理图片表中所有该id的数据
        QueryWrapper<YxImageInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type_id", typeId).eq("img_type", ShopConstants.IMG_TYPE_MERCHANTS).eq("del_flag", 0);
        this.yxImageInfoService.remove(queryWrapper);
        // 根据用户类型插入图片
        if (0 == resources.getMerchantsType()) {
            // 手持证件照
            YxImageInfo personIdCard = new YxImageInfo();
            personIdCard.setTypeId(typeId);
            personIdCard.setImgType(ShopConstants.IMG_TYPE_MERCHANTS);
            personIdCard.setCreateUserId(resources.getUid());
            personIdCard.setImgCategory(ShopConstants.IMG_PERSON_IDCARD);
            personIdCard.setImgUrl(resources.getPersonIdCard());
            this.yxImageInfoService.saveOrUpdate(personIdCard);
            // 证件照人像面
            YxImageInfo personIdCardFace = new YxImageInfo();
            personIdCardFace.setTypeId(typeId);
            personIdCardFace.setImgType(ShopConstants.IMG_TYPE_MERCHANTS);
            personIdCardFace.setCreateUserId(resources.getUid());
            personIdCardFace.setImgCategory(ShopConstants.IMG_PERSON_IDCARD_FACE);
            personIdCardFace.setImgUrl(resources.getPersonIdCardFace());
            this.yxImageInfoService.save(personIdCardFace);
            // 证件照国徽面
            YxImageInfo personIdCardBack = new YxImageInfo();
            personIdCardBack.setTypeId(typeId);
            personIdCardBack.setImgType(ShopConstants.IMG_TYPE_MERCHANTS);
            personIdCardBack.setCreateUserId(resources.getUid());
            personIdCardBack.setImgCategory(ShopConstants.IMG_PERSON_IDCARD_BACK);
            personIdCardBack.setImgUrl(resources.getPersonIdCardBack());
            this.yxImageInfoService.save(personIdCardBack);
        } else {
            // 营业执照
            YxImageInfo businessLicenseImg = new YxImageInfo();
            businessLicenseImg.setTypeId(typeId);
            businessLicenseImg.setImgType(ShopConstants.IMG_TYPE_MERCHANTS);
            businessLicenseImg.setCreateUserId(resources.getUid());
            businessLicenseImg.setImgCategory(ShopConstants.IMG_BUSINESS_LICENSE);
            businessLicenseImg.setImgUrl(resources.getBusinessLicenseImg());
            this.yxImageInfoService.save(businessLicenseImg);
            // 银行开户证明
            YxImageInfo bankOpenProveImg = new YxImageInfo();
            bankOpenProveImg.setTypeId(typeId);
            bankOpenProveImg.setImgType(ShopConstants.IMG_TYPE_MERCHANTS);
            bankOpenProveImg.setCreateUserId(resources.getUid());
            bankOpenProveImg.setImgCategory(ShopConstants.IMG_BANK_OPEN_PROVE);
            bankOpenProveImg.setImgUrl(resources.getBankOpenProveImg());
            this.yxImageInfoService.save(bankOpenProveImg);
            // 法人身份证头像面
            YxImageInfo legalIdCardFace = new YxImageInfo();
            legalIdCardFace.setTypeId(typeId);
            legalIdCardFace.setImgType(ShopConstants.IMG_TYPE_MERCHANTS);
            legalIdCardFace.setCreateUserId(resources.getUid());
            legalIdCardFace.setImgCategory(ShopConstants.IMG_LEGAL_IDCARD_FACE);
            legalIdCardFace.setImgUrl(resources.getLegalIdCardFace());
            this.yxImageInfoService.save(legalIdCardFace);
            // 法人身份证国徽面
            YxImageInfo legalIdCardBack = new YxImageInfo();
            legalIdCardBack.setTypeId(typeId);
            legalIdCardBack.setImgType(ShopConstants.IMG_TYPE_MERCHANTS);
            legalIdCardBack.setCreateUserId(resources.getUid());
            legalIdCardBack.setImgCategory(ShopConstants.IMG_LEGAL_IDCARD_BACK);
            legalIdCardBack.setImgUrl(resources.getLegalIdCardBack());
            this.yxImageInfoService.save(legalIdCardBack);
            // 门店照及经营场所
            YxImageInfo storeImg = new YxImageInfo();
            storeImg.setTypeId(typeId);
            storeImg.setImgType(ShopConstants.IMG_TYPE_MERCHANTS);
            storeImg.setCreateUserId(resources.getUid());
            storeImg.setImgCategory(ShopConstants.IMG_STORE);
            storeImg.setImgUrl(resources.getStoreImg());
            this.yxImageInfoService.save(storeImg);
            // 医疗机构许可证
            YxImageInfo licenceImg = new YxImageInfo();
            licenceImg.setTypeId(typeId);
            licenceImg.setImgType(ShopConstants.IMG_TYPE_MERCHANTS);
            licenceImg.setCreateUserId(resources.getUid());
            licenceImg.setImgCategory(ShopConstants.IMG_LICENCE);
            licenceImg.setImgUrl(resources.getLicenceImg());
            this.yxImageInfoService.save(licenceImg);
        }
        return true;
    }
}
