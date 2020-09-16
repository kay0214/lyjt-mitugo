/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.shop.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.constant.ShopConstants;
import co.yixiang.constant.SystemConfigConstants;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.enums.AppFromEnum;
import co.yixiang.exception.BadRequestException;
import co.yixiang.modules.mybatis.GeoPoint;
import co.yixiang.modules.shop.domain.*;
import co.yixiang.modules.shop.service.*;
import co.yixiang.modules.shop.service.dto.UserMoneyDto;
import co.yixiang.modules.shop.service.dto.YxMerchantsDetailDto;
import co.yixiang.modules.shop.service.dto.YxMerchantsDetailQueryCriteria;
import co.yixiang.modules.shop.service.mapper.YxMerchantsDetailMapper;
import co.yixiang.utils.FileUtil;
import co.yixiang.utils.OrderUtil;
import co.yixiang.utils.SecretUtil;
import co.yixiang.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
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
 * @author liusy
 * @date 2020-08-19
 */
@Slf4j
@Service
//@AllArgsConstructor
//@CacheConfig(cacheNames = "yxMerchantsDetail")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxMerchantsDetailServiceImpl extends BaseServiceImpl<YxMerchantsDetailMapper, YxMerchantsDetail> implements YxMerchantsDetailService {

    @Value("${file.path}")
    private String path;

    @Autowired
    private IGenerator generator;
    @Autowired
    private YxImageInfoService yxImageInfoService;
    @Autowired
    private YxExamineLogService yxExamineLogService;
    @Autowired
    private YxStoreInfoService yxStoreInfoService;
    @Autowired
    private UserService userService;
    @Autowired
    private YxUserBillService yxUserBillService;
    @Autowired
    private YxSystemConfigService systemConfigService;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxMerchantsDetailQueryCriteria criteria, Pageable pageable) {
        QueryWrapper<YxMerchantsDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByDesc(YxMerchantsDetail::getCreateTime);
        if (0 != criteria.getUserRole()) {
            if (null == criteria.getChildUser() || criteria.getChildUser().size() <= 0) {
                Map<String, Object> map = new LinkedHashMap<>(2);
                map.put("content", new ArrayList<>());
                map.put("totalElements", 0);
                return map;
            }
            queryWrapper.lambda().in(YxMerchantsDetail::getUid, criteria.getChildUser()).eq(YxMerchantsDetail::getDelFlag, 0);
        }
        IPage<YxMerchantsDetail> ipage = this.page(new Page<>(pageable.getPageNumber() + 1, pageable.getPageSize()), queryWrapper);

        List<YxMerchantsDetailDto> list = new ArrayList<>();
        for (YxMerchantsDetail vo : ipage.getRecords()) {
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
            QueryWrapper<YxExamineLog> examineWrapper = new QueryWrapper<>();
            examineWrapper.lambda().eq(YxExamineLog::getType, 2).eq(YxExamineLog::getTypeId, dto.getId()).orderByDesc(YxExamineLog::getCreateTime);
            List<YxExamineLog> yxExamineLogs = this.yxExamineLogService.list(examineWrapper);
            if (null != yxExamineLogs && yxExamineLogs.size() > 0) {
                dto.setExamineRemark(yxExamineLogs.get(0).getRemark());
            }
            User user = this.userService.getById(vo.getUid());
            if (null != user && StringUtils.isNotBlank(user.getUsername())) {
                dto.setUsername(user.getUsername());
                dto.setTotalAmount(user.getTotalAmount());
                dto.setWithdrawalAmount(user.getWithdrawalAmount());
                dto.setTotalScore(user.getTotalScore());
            }
            list.add(dto);
        }

        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", list);
        map.put("totalElements", ipage.getTotal());
        return map;
    }


    /**
     * 原有方法已废弃使用
     *
     * @param criteria 条件参数
     * @return
     */
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
            map.put("审批状态：0->初始,1->通过,2->提交审核", yxMerchantsDetail.getExamineStatus());
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
            map.put("商户名称", yxMerchantsDetail.getMerchantsName());
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
        YxMerchantsDetail yxMerchantsDetail = this.getOne(new QueryWrapper<YxMerchantsDetail>().eq("uid", uid));
        // 审核通过的直接返回不允许修改
        if (null != yxMerchantsDetail) {
            if (1 == yxMerchantsDetail.getExamineStatus()) {
                throw new BadRequestException("已认证通过不可修改数据");
            }
            isNew = false;
        }

        // 有数据的更新数据、没有数据的新插入数据
        yxMerchantsDetail = generator.convert(resources, YxMerchantsDetail.class);
        if (isNew) {
            yxMerchantsDetail.setCreateUserId(resources.getUpdateUserId());
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
            throw new BadRequestException("商铺信息查询失败");
        }
        if (1 == yxMerchantsDetail.getExamineStatus()) {
            throw new BadRequestException("当前商户已被审核通过");
        }
        YxExamineLog yxExamineLog = new YxExamineLog();
        // 审批类型 1:提现 2:商户信息
        yxExamineLog.setUid(yxMerchantsDetail.getUid());
        yxExamineLog.setUsername(yxMerchantsDetail.getContacts());
        yxExamineLog.setType(2);
        yxExamineLog.setTypeId(resources.getId());
        yxExamineLog.setStatus(resources.getExamineStatus());
        yxExamineLog.setRemark(resources.getExamineRemark());
        yxExamineLog.setDelFlag(0);
        yxExamineLogService.save(yxExamineLog);
        YxStoreInfo yxStoreInfo = this.yxStoreInfoService.getOne(new QueryWrapper<YxStoreInfo>().lambda().eq(YxStoreInfo::getMerId, yxMerchantsDetail.getUid()));
        // 审核通过生成一个默认店铺
        if (1 == resources.getExamineStatus() && null == yxStoreInfo) {
            User user = this.userService.getById(yxMerchantsDetail.getUid());
            yxStoreInfo = new YxStoreInfo();
            // 店铺编号
            yxStoreInfo.setStoreNid("S" + SecretUtil.createRandomStr(8) + resources.getId());
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
            yxStoreInfo.setPerCapita(BigDecimal.ZERO);
            yxStoreInfo.setDelFlag(0);
            yxStoreInfo.setCoordinate(new GeoPoint(BigDecimal.ZERO, BigDecimal.ZERO));
            yxStoreInfoService.save(yxStoreInfo);

            // 生成店铺分销用的二维码
            //判断用户是否小程序,注意小程序二维码生成路径要与H5不一样 不然会导致都跳转到小程序问题
            String siteUrl = systemConfigService.getData(SystemConfigConstants.SITE_URL);
            String apiUrl = systemConfigService.getData(SystemConfigConstants.API_URL);
            if (StringUtils.isNotBlank(siteUrl) && StringUtils.isNotBlank(apiUrl)) {
                //小程序地址
                siteUrl = siteUrl + "/shop/";
                // 二维码长宽
                QrConfig config = new QrConfig(180, 180);
                config.setMargin(0);
                String fileDir = path + "qrcode" + File.separator;
                String name = yxStoreInfo.getId() + "_" + yxMerchantsDetail.getUid() + "_mer_" + "_store_detail_wap.jpg";
//                File file = FileUtil.mkdir(new File(fileDir));
                //生成二维码
                QrCodeUtil.generate(siteUrl + "?productId=" + yxStoreInfo.getId() + "&spread=" + yxMerchantsDetail.getUid() + "&spreadType=mer&codeType=" + AppFromEnum.ROUNTINE.getValue(), config,
                        FileUtil.file(fileDir + name));

                String qrcodeUrl = apiUrl + "/file/qrcode/" + name;
                yxMerchantsDetail.setQrcode(qrcodeUrl);
            }
        }
        // 审核状态更新后放、审核通过的场景生成二维码
        yxMerchantsDetail.setExamineStatus(resources.getExamineStatus());
        this.updateById(yxMerchantsDetail);
        return true;
    }

    /**
     * 提交审核
     *
     * @param resources
     * @return
     */
    @Override
    public boolean updateMerDetail(YxMerchantsDetailDto resources) {
        if (null == resources.getId()) {
            throw new BadRequestException("缺少主键id");
        }
        YxMerchantsDetail yxMerchantsDetail = this.getById(resources.getId());
        if (null == yxMerchantsDetail) {
            throw new BadRequestException("数据查询失败");
        }
        if (1 == yxMerchantsDetail.getExamineStatus()) {
            throw new BadRequestException("已审核通过，不允许再次修改");
        }
        if (3 == yxMerchantsDetail.getExamineStatus()) {
            throw new BadRequestException("正在审核中");
        }

        YxMerchantsDetail update = generator.convert(resources, YxMerchantsDetail.class);
        update.setExamineStatus(3);
        boolean result = this.updateById(update);

        if (result) {
            // 图片处理id
            Integer typeId = yxMerchantsDetail.getId();
            // 处理图片入库
            result = insertOrUpdatePic(typeId, resources);
        }

        return result;
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
            personIdCard.setDelFlag(0);
            this.yxImageInfoService.saveOrUpdate(personIdCard);
            // 证件照人像面
            YxImageInfo personIdCardFace = new YxImageInfo();
            personIdCardFace.setTypeId(typeId);
            personIdCardFace.setImgType(ShopConstants.IMG_TYPE_MERCHANTS);
            personIdCardFace.setCreateUserId(resources.getUid());
            personIdCardFace.setImgCategory(ShopConstants.IMG_PERSON_IDCARD_FACE);
            personIdCardFace.setImgUrl(resources.getPersonIdCardFace());
            personIdCardFace.setDelFlag(0);
            this.yxImageInfoService.save(personIdCardFace);
            // 证件照国徽面
            YxImageInfo personIdCardBack = new YxImageInfo();
            personIdCardBack.setTypeId(typeId);
            personIdCardBack.setImgType(ShopConstants.IMG_TYPE_MERCHANTS);
            personIdCardBack.setCreateUserId(resources.getUid());
            personIdCardBack.setImgCategory(ShopConstants.IMG_PERSON_IDCARD_BACK);
            personIdCardBack.setImgUrl(resources.getPersonIdCardBack());
            personIdCardBack.setDelFlag(0);
            this.yxImageInfoService.save(personIdCardBack);
        } else {
            // 营业执照
            YxImageInfo businessLicenseImg = new YxImageInfo();
            businessLicenseImg.setTypeId(typeId);
            businessLicenseImg.setImgType(ShopConstants.IMG_TYPE_MERCHANTS);
            businessLicenseImg.setCreateUserId(resources.getUid());
            businessLicenseImg.setImgCategory(ShopConstants.IMG_BUSINESS_LICENSE);
            businessLicenseImg.setImgUrl(resources.getBusinessLicenseImg());
            businessLicenseImg.setDelFlag(0);
            this.yxImageInfoService.save(businessLicenseImg);
            // 银行开户证明
            YxImageInfo bankOpenProveImg = new YxImageInfo();
            bankOpenProveImg.setTypeId(typeId);
            bankOpenProveImg.setImgType(ShopConstants.IMG_TYPE_MERCHANTS);
            bankOpenProveImg.setCreateUserId(resources.getUid());
            bankOpenProveImg.setImgCategory(ShopConstants.IMG_BANK_OPEN_PROVE);
            bankOpenProveImg.setImgUrl(resources.getBankOpenProveImg());
            bankOpenProveImg.setDelFlag(0);
            this.yxImageInfoService.save(bankOpenProveImg);
            // 法人身份证头像面
            YxImageInfo legalIdCardFace = new YxImageInfo();
            legalIdCardFace.setTypeId(typeId);
            legalIdCardFace.setImgType(ShopConstants.IMG_TYPE_MERCHANTS);
            legalIdCardFace.setCreateUserId(resources.getUid());
            legalIdCardFace.setImgCategory(ShopConstants.IMG_LEGAL_IDCARD_FACE);
            legalIdCardFace.setImgUrl(resources.getLegalIdCardFace());
            legalIdCardFace.setDelFlag(0);
            this.yxImageInfoService.save(legalIdCardFace);
            // 法人身份证国徽面
            YxImageInfo legalIdCardBack = new YxImageInfo();
            legalIdCardBack.setTypeId(typeId);
            legalIdCardBack.setImgType(ShopConstants.IMG_TYPE_MERCHANTS);
            legalIdCardBack.setCreateUserId(resources.getUid());
            legalIdCardBack.setImgCategory(ShopConstants.IMG_LEGAL_IDCARD_BACK);
            legalIdCardBack.setImgUrl(resources.getLegalIdCardBack());
            legalIdCardBack.setDelFlag(0);
            this.yxImageInfoService.save(legalIdCardBack);
            // 门店照及经营场所
            YxImageInfo storeImg = new YxImageInfo();
            storeImg.setTypeId(typeId);
            storeImg.setImgType(ShopConstants.IMG_TYPE_MERCHANTS);
            storeImg.setCreateUserId(resources.getUid());
            storeImg.setImgCategory(ShopConstants.IMG_STORE);
            storeImg.setImgUrl(resources.getStoreImg());
            storeImg.setDelFlag(0);
            this.yxImageInfoService.save(storeImg);
            // 医疗机构许可证
            YxImageInfo licenceImg = new YxImageInfo();
            licenceImg.setTypeId(typeId);
            licenceImg.setImgType(ShopConstants.IMG_TYPE_MERCHANTS);
            licenceImg.setCreateUserId(resources.getUid());
            licenceImg.setImgCategory(ShopConstants.IMG_LICENCE);
            licenceImg.setImgUrl(resources.getLicenceImg());
            licenceImg.setDelFlag(0);
            this.yxImageInfoService.save(licenceImg);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserCommission(UserMoneyDto param) {
        User user = userService.getById(param.getUid());
        Double withDrawa = 0d;
        Double totleMoney = 0d;
        String mark = "";
        String type = "system_add";
        Integer pm = 1;
        String title = "增加可提现金额";
        if (param.getPtype() == 1) {
            mark = "系统增加了" + param.getMoney() + "可提现金额";
            withDrawa = NumberUtil.add(user.getWithdrawalAmount(), param.getMoney()).doubleValue();
            totleMoney = NumberUtil.add(user.getTotalAmount(), param.getMoney()).doubleValue();
            userService.updateAddAmount(user.getId(), new BigDecimal(param.getMoney()));
        } else {
            title = "减少可提现金额";
            mark = "系统扣除了" + param.getMoney() + "可提现金额";
            type = "system_sub";
            pm = 0;
            withDrawa = NumberUtil.sub(user.getWithdrawalAmount(), param.getMoney()).doubleValue();
            totleMoney = NumberUtil.sub(user.getTotalAmount(), param.getMoney()).doubleValue();

            if (withDrawa < 0 || totleMoney < 0) {
                throw new BadRequestException("商户余额不足");
            }
            userService.updateAmountSub(user.getId(), new BigDecimal(param.getMoney()));
        }
//        user.setWithdrawalAmount(BigDecimal.valueOf(withDrawa));
//        user.setTotalAmount(BigDecimal.valueOf(totleMoney));
//        userService.updateById(user);

        YxUserBill userBill = new YxUserBill();
        userBill.setUid(user.getId().intValue());
        userBill.setLinkId("0");
        userBill.setPm(pm);
        userBill.setTitle(title);
        //商户返钱？
        userBill.setCategory("now_money");
        userBill.setType(type);
        userBill.setNumber(BigDecimal.valueOf(param.getMoney()));
        userBill.setBalance(BigDecimal.valueOf(withDrawa));
        userBill.setMark(mark);
        userBill.setAddTime(OrderUtil.getSecondTimestampTwo());
        userBill.setStatus(1);
        //后台商户
        userBill.setUserType(1);
        userBill.setUsername(user.getNickName());
        yxUserBillService.save(userBill);
    }

}
