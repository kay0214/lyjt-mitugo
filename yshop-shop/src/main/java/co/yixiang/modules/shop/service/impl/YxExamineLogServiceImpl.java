/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.shop.service.impl;

import cn.hutool.core.util.ObjectUtil;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.modules.activity.domain.YxUserExtract;
import co.yixiang.modules.activity.service.mapper.YxUserExtractMapper;
import co.yixiang.modules.shop.domain.User;
import co.yixiang.modules.shop.domain.YxExamineLog;
import co.yixiang.modules.shop.domain.YxMerchantsDetail;
import co.yixiang.modules.shop.domain.YxUser;
import co.yixiang.modules.shop.service.UserService;
import co.yixiang.modules.shop.service.YxExamineLogService;
import co.yixiang.modules.shop.service.YxUserService;
import co.yixiang.modules.shop.service.dto.YxExamineLogDto;
import co.yixiang.modules.shop.service.dto.YxExamineLogQueryCriteria;
import co.yixiang.modules.shop.service.mapper.YxExamineLogMapper;
import co.yixiang.modules.shop.service.mapper.YxMerchantsDetailMapper;
import co.yixiang.utils.FileUtil;
import co.yixiang.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
 * @author liusy
 * @date 2020-08-19
 */
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "yxExamineLog")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxExamineLogServiceImpl extends BaseServiceImpl<YxExamineLogMapper, YxExamineLog> implements YxExamineLogService {

    private final IGenerator generator;
    //    @Autowired
//    private YxMerchantsDetailService yxMerchantsDetailService;
    @Autowired
    private YxMerchantsDetailMapper yxMerchantsDetailMapper;
    @Autowired
    private YxUserExtractMapper yxUserExtractMapper;
    @Autowired
    private YxUserService yxUserService;
    @Autowired
    private UserService userService;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxExamineLogQueryCriteria criteria, Pageable pageable) {
//        getPage(pageable);
//        PageInfo<YxExamineLogDto> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        QueryWrapper<YxExamineLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        if (0 != criteria.getUserRole()) {
            if (null == criteria.getChildUser() || criteria.getChildUser().size() <= 0) {
                map.put("content", new ArrayList<>());
                map.put("totalElements", 0);
                return map;
            }
            queryWrapper.lambda().in(YxExamineLog::getUid, criteria.getChildUser()).eq(YxExamineLog::getDelFlag, 0);
        }
        if (null != criteria.getType()) {
            queryWrapper.lambda().eq(YxExamineLog::getType, criteria.getType());
        }
        if (StringUtils.isNotBlank(criteria.getUsername())) {
            queryWrapper.lambda().like(YxExamineLog::getUsername, criteria.getUsername());
        }
        if (StringUtils.isNotBlank(criteria.getMerchantsName())) {
            List<YxMerchantsDetail> list = this.yxMerchantsDetailMapper.selectList(new QueryWrapper<YxMerchantsDetail>().lambda().like(YxMerchantsDetail::getMerchantsName, criteria.getMerchantsName()));
            if (null == list || list.size() <= 0) {
                map.put("content", new ArrayList<>());
                map.put("totalElements", 0);
                return map;
            }
            List<Integer> ids = new ArrayList<>();
            for (YxMerchantsDetail item : list) {
                ids.add(item.getId());
            }
            queryWrapper.lambda().in(YxExamineLog::getTypeId, ids);
        }
        if (StringUtils.isNotBlank(criteria.getContactMobile())) {
            YxMerchantsDetail yxMerchantsDetail = this.yxMerchantsDetailMapper.selectOne(new QueryWrapper<YxMerchantsDetail>().lambda().eq(YxMerchantsDetail::getContactMobile, criteria.getContactMobile()));
            if (null == yxMerchantsDetail) {
                map.put("content", new ArrayList<>());
                map.put("totalElements", 0);
                return map;
            }
            queryWrapper.lambda().eq(YxExamineLog::getTypeId, yxMerchantsDetail.getId());
        }
        if (null != criteria.getStatus()) {
            queryWrapper.lambda().eq(YxExamineLog::getStatus, criteria.getStatus());
        }

        IPage<YxExamineLog> ipage = this.page(new Page<>(pageable.getPageNumber() + 1, pageable.getPageSize()), queryWrapper);
        if (ipage.getTotal() <= 0) {
            map.put("content", new ArrayList<YxExamineLogDto>());
            map.put("totalElements", 0);
            return map;
        }
        List<YxExamineLogDto> list = generator.convert(ipage.getRecords(), YxExamineLogDto.class);
        // 查询商户认证数据
        for (YxExamineLogDto dto : list) {
            YxMerchantsDetail yxMerchantsDetail = yxMerchantsDetailMapper.selectById(dto.getTypeId());
            if (null != yxMerchantsDetail) {
                dto.setContacts(yxMerchantsDetail.getContacts());
                dto.setContactMobile(yxMerchantsDetail.getContactMobile());
                dto.setMerchantsName(yxMerchantsDetail.getMerchantsName());
            }
        }

        map.put("content", list);
        map.put("totalElements", ipage.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public Map<String, Object> queryExtractAll(YxExamineLogQueryCriteria criteria, Pageable pageable) {
//        getPage(pageable);
//        PageInfo<YxExamineLogDto> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        QueryWrapper<YxExamineLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByDesc(YxExamineLog::getCreateTime);
        if (null != criteria.getType()) {
            queryWrapper.lambda().eq(YxExamineLog::getType, criteria.getType());
        }
        if (StringUtils.isNotBlank(criteria.getUsername())) {
            queryWrapper.lambda().like(YxExamineLog::getUsername, criteria.getUsername());
        }
        if (null != criteria.getStatus()) {
            queryWrapper.lambda().eq(YxExamineLog::getStatus, criteria.getStatus());
        }
//        if (StringUtils.isNotBlank(criteria.getPhone())) {
//            List<YxUserExtract> extracts = this.yxUserExtractMapper.selectList(new QueryWrapper<YxUserExtract>().lambda().eq(YxUserExtract::getBankMobile, criteria.getPhone()));
//            if (null == extracts || extracts.size() <= 0) {
//                map.put("content", new ArrayList<>());
//                map.put("totalElements", 0);
//                return map;
//            }
//            List<Integer> ids = new ArrayList<>();
//            for (YxUserExtract item : extracts) {
//                ids.add(item.getId());
//            }
//            queryWrapper.lambda().in(YxExamineLog::getTypeId, ids);
//        }
        if (StringUtils.isNotBlank(criteria.getUserTrueName())) {
            QueryWrapper<YxUserExtract> extractWrapper = new QueryWrapper<>();
            List<YxUser> yxUsers = this.yxUserService.list(new QueryWrapper<YxUser>().lambda().eq(YxUser::getRealName, criteria.getUserTrueName()));
            List<User> users = this.userService.list(new QueryWrapper<User>().lambda().eq(User::getMerchantsContact, criteria.getUserTrueName()));
            if (null != yxUsers && yxUsers.size() > 0 && null != users && users.size() > 0) {
                List<Integer> yxUserIds = new ArrayList<>();
                for (YxUser item : yxUsers) {
                    yxUserIds.add(item.getUid());
                }
                List<Integer> userIds = new ArrayList<>();
                for (User item : users) {
                    userIds.add(item.getId().intValue());
                }
                extractWrapper.lambda().and(orqw -> orqw.and(qw -> qw.in(YxUserExtract::getUid, yxUserIds).eq(YxUserExtract::getUserType, 3)).or(qw -> qw.in(YxUserExtract::getUid, userIds).eq(YxUserExtract::getUserType, 1)));
            } else if (null != yxUsers && yxUsers.size() > 0) {
                List<Integer> yxUserIds = new ArrayList<>();
                for (YxUser item : yxUsers) {
                    yxUserIds.add(item.getUid());
                }
                extractWrapper.lambda().in(YxUserExtract::getUid, yxUserIds).eq(YxUserExtract::getUserType, 3);
            } else if (null != users && users.size() > 0) {
                List<Integer> userIds = new ArrayList<>();
                for (User item : users) {
                    userIds.add(item.getId().intValue());
                }
                extractWrapper.lambda().in(YxUserExtract::getUid, userIds).eq(YxUserExtract::getUserType, 1);
            } else {
                map.put("content", new ArrayList<>());
                map.put("totalElements", 0);
                return map;
            }
            List<YxUserExtract> extracts = this.yxUserExtractMapper.selectList(extractWrapper);
            if (null == extracts || extracts.size() <= 0) {
                map.put("content", new ArrayList<>());
                map.put("totalElements", 0);
                return map;
            }
            List<Integer> ids = new ArrayList<>();
            for (YxUserExtract item : extracts) {
                ids.add(item.getId());
            }
            queryWrapper.lambda().in(YxExamineLog::getTypeId, ids);
        }
        if (StringUtils.isNotBlank(criteria.getSeqNo())) {
            List<YxUserExtract> extracts = this.yxUserExtractMapper.selectList(new QueryWrapper<YxUserExtract>().lambda().eq(YxUserExtract::getSeqNo, criteria.getSeqNo()));
            if (null == extracts || extracts.size() <= 0) {
                map.put("content", new ArrayList<>());
                map.put("totalElements", 0);
                return map;
            }
            List<Integer> ids = new ArrayList<>();
            for (YxUserExtract item : extracts) {
                ids.add(item.getId());
            }
            queryWrapper.lambda().in(YxExamineLog::getTypeId, ids);
        }
        if (null != criteria.getAddTime() && criteria.getAddTime().size() == 2) {
            queryWrapper.lambda().ge(YxExamineLog::getCreateTime, criteria.getAddTime().get(0).concat(" 00:00:00")).le(YxExamineLog::getCreateTime, criteria.getAddTime().get(1).concat(" 23:59:59"));
        }
        IPage<YxExamineLog> ipage = this.page(new Page<>(pageable.getPageNumber() + 1, pageable.getPageSize()), queryWrapper);
        List<YxExamineLogDto> list = generator.convert(ipage.getRecords(), YxExamineLogDto.class);
        // 查询提现数据
        for (YxExamineLogDto dto : list) {
            String realName = "";
            YxUserExtract yxUserExtract = yxUserExtractMapper.selectById(dto.getTypeId());
            //  0:预留 1商户;2合伙人;3用户
            if (3 == yxUserExtract.getUserType()) {
                YxUser user = yxUserService.getById(yxUserExtract.getUid());
                if (ObjectUtil.isNotEmpty(user)) {
                    realName = StringUtils.isNotBlank(user.getRealName()) ? user.getRealName() : "";
                }
            } else {
                User user = userService.getById(yxUserExtract.getUid());
                if (null != user) {
                    realName = StringUtils.isNotBlank(user.getMerchantsContact()) ? user.getMerchantsContact() : "";
                }
            }
            // 驳回信息
            dto.setFailMsg(yxUserExtract.getFailMsg());
            dto.setFailTime(yxUserExtract.getFailTime());
            // 放用户名
            dto.setWechat(yxUserExtract.getRealName());
            // 放真实姓名
            dto.setUsername(realName);
            dto.setUserType(yxUserExtract.getUserType());
            dto.setExtractPrice(yxUserExtract.getExtractPrice());
            switch (yxUserExtract.getExtractType()) {
                case "bank":
                    dto.setExtractType("银行卡");
                    break;
                case "alipay":
                    dto.setExtractType("支付宝");
                    break;
                case "wx":
                    dto.setExtractType("微信");
                    break;
            }
            dto.setSeqNo(yxUserExtract.getSeqNo());
        }

        map.put("content", list);
        map.put("totalElements", ipage.getTotal());
        return map;
    }

    @Override
    //@Cacheable
    public List<YxExamineLogDto> queryAll(YxExamineLogQueryCriteria criteria) {
        List<YxExamineLog> list = baseMapper.selectList(QueryHelpPlus.getPredicate(YxExamineLog.class, criteria));
        if (null == list || list.size() <= 0) {
            return new ArrayList<YxExamineLogDto>();
        }
        List<YxExamineLogDto> result = generator.convert(list, YxExamineLogDto.class);
        // 查询商户认证数据
        if (2 == criteria.getType()) {
            for (YxExamineLogDto dto : result) {
                YxMerchantsDetail yxMerchantsDetail = yxMerchantsDetailMapper.selectById(dto.getTypeId());
                dto.setContacts(yxMerchantsDetail.getContacts());
                dto.setContactMobile(yxMerchantsDetail.getContactMobile());
                dto.setMerchantsName(yxMerchantsDetail.getMerchantsName());
            }
        }
        return result;
    }


    @Override
    public void download(List<YxExamineLogDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxExamineLogDto yxExamineLog : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("审批类型 1:提现 2:商户信息", yxExamineLog.getType());
            map.put("审核数据关联id", yxExamineLog.getTypeId());
            map.put("审批状态：0->待审核,1->通过,2->驳回", yxExamineLog.getStatus());
            map.put("审核说明", yxExamineLog.getRemark());
            map.put("是否删除（0：未删除，1：已删除）", yxExamineLog.getDelFlag());
            map.put("创建人(审核人)", yxExamineLog.getCreateUserId());
            map.put("修改人", yxExamineLog.getUpdateUserId());
            map.put("创建时间", yxExamineLog.getCreateTime());
            map.put("更新时间", yxExamineLog.getUpdateTime());
            map.put("冗余字段：被审核人id", yxExamineLog.getUid());
            map.put("冗余字段：被审核人信息", yxExamineLog.getUsername());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
