/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 */
package co.yixiang.modules.activity.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.enums.BillDetailEnum;
import co.yixiang.exception.BadRequestException;
import co.yixiang.modules.activity.domain.YxUserExtract;
import co.yixiang.modules.activity.service.YxUserExtractService;
import co.yixiang.modules.activity.service.dto.YxUserExtractDto;
import co.yixiang.modules.activity.service.dto.YxUserExtractQueryCriteria;
import co.yixiang.modules.activity.service.mapper.YxUserExtractMapper;
import co.yixiang.modules.bank.domain.BankCode;
import co.yixiang.modules.bank.service.BankCodeService;
import co.yixiang.modules.shop.domain.User;
import co.yixiang.modules.shop.domain.YxExamineLog;
import co.yixiang.modules.shop.domain.YxUser;
import co.yixiang.modules.shop.domain.YxUserBill;
import co.yixiang.modules.shop.service.UserService;
import co.yixiang.modules.shop.service.YxExamineLogService;
import co.yixiang.modules.shop.service.YxUserBillService;
import co.yixiang.modules.shop.service.YxUserService;
import co.yixiang.utils.DateUtils;
import co.yixiang.utils.FileUtil;
import co.yixiang.utils.OrderUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
 * @author hupeng
 * @date 2020-05-13
 */
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "yxUserExtract")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxUserExtractServiceImpl extends BaseServiceImpl<YxUserExtractMapper, YxUserExtract> implements YxUserExtractService {

    private final IGenerator generator;
    @Autowired
    private YxUserService yxUserService;
    @Autowired
    private UserService userService;
    @Autowired
    private YxUserBillService yxUserBillService;
    @Autowired
    private YxExamineLogService yxExamineLogService;
    @Autowired
    private BankCodeService bankCodeService;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxUserExtractQueryCriteria criteria, Pageable pageable) {
//        getPage(pageable);
//        PageInfo<YxUserExtract> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        // 组装查询条件
        QueryWrapper<YxUserExtract> queryWrapper = createQueryWrapper(criteria);
        IPage<YxUserExtract> iPage = this.page(new Page<>(pageable.getPageNumber() + 1, pageable.getPageSize()), queryWrapper);
        if (0 == iPage.getTotal()) {
            map.put("content", new ArrayList<>());
            map.put("totalElements", 0);
            return map;
        }
        List<YxUserExtractDto> extractDtoList = generator.convert(iPage.getRecords(), YxUserExtractDto.class);
        for (YxUserExtractDto extractDto : extractDtoList) {
            // 用户类型 1商户;2合伙人;3用户
            if (3 == extractDto.getUserType()) {
                YxUser user = yxUserService.getById(extractDto.getUid());
                if (ObjectUtil.isNotEmpty(user)) {
                    extractDto.setUserTrueName(StringUtils.isNotBlank(user.getRealName()) ? user.getRealName() : "");
                }
            } else {
                User user = userService.getById(extractDto.getUid());
                if (null != user) {
                    extractDto.setUserTrueName(StringUtils.isNotBlank(user.getMerchantsContact()) ? user.getMerchantsContact() : "");
                }
            }
        }
        map.put("content", extractDtoList);
        map.put("totalElements", iPage.getTotal());
        return map;
    }

    /**
     * 组装查询条件
     *
     * @param criteria
     * @return
     */
    private QueryWrapper<YxUserExtract> createQueryWrapper(YxUserExtractQueryCriteria criteria) {
        QueryWrapper<YxUserExtract> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByDesc(YxUserExtract::getId);
        if (StringUtils.isNotBlank(criteria.getSeqNo())) {
            queryWrapper.lambda().eq(YxUserExtract::getSeqNo, criteria.getSeqNo());
        }
        if (StringUtils.isNotBlank(criteria.getRealName())) {
            queryWrapper.lambda().eq(YxUserExtract::getRealName, criteria.getRealName());
        }
        if (StringUtils.isNotBlank(criteria.getUserTrueName())) {
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
                queryWrapper.lambda().and(orqw -> orqw.and(qw -> qw.in(YxUserExtract::getUid, yxUserIds).eq(YxUserExtract::getUserType, 3)).or(qw -> qw.in(YxUserExtract::getUid, userIds).eq(YxUserExtract::getUserType, 1)));
            } else if (null != yxUsers && yxUsers.size() > 0) {
                List<Integer> yxUserIds = new ArrayList<>();
                for (YxUser item : yxUsers) {
                    yxUserIds.add(item.getUid());
                }
                queryWrapper.lambda().in(YxUserExtract::getUid, yxUserIds).eq(YxUserExtract::getUserType, 3);
            } else if (null != users && users.size() > 0) {
                List<Integer> userIds = new ArrayList<>();
                for (User item : users) {
                    userIds.add(item.getId().intValue());
                }
                queryWrapper.lambda().in(YxUserExtract::getUid, userIds).eq(YxUserExtract::getUserType, 1);
            } else {
                // 查询必为空的条件
                queryWrapper.lambda().eq(YxUserExtract::getId, 0);
            }
        }
        if (StringUtils.isNotBlank(criteria.getBankCode())) {
            queryWrapper.lambda().eq(YxUserExtract::getBankCode, criteria.getBankCode());
        }
        if (StringUtils.isNotBlank(criteria.getPhone())) {
            queryWrapper.lambda().eq(YxUserExtract::getBankMobile, criteria.getPhone());
        }
        if (StringUtils.isNotBlank(criteria.getBankCode())) {
            queryWrapper.lambda().eq(YxUserExtract::getBankCode, criteria.getBankCode());
        }
        if (null != criteria.getUserType()) {
            queryWrapper.lambda().eq(YxUserExtract::getUserType, criteria.getUserType());
        }
        if (null != criteria.getStatus()) {
            queryWrapper.lambda().eq(YxUserExtract::getStatus, criteria.getStatus());
        }
        if (null != criteria.getAddTime() && criteria.getAddTime().size() == 2) {
            Integer addTimeStart = DateUtils.stringToTimestamp(criteria.getAddTime().get(0).concat(" 00:00:00"));
            Integer addTimeEnd = DateUtils.stringToTimestamp(criteria.getAddTime().get(1).concat(" 23:59:59"));
            queryWrapper.lambda().ge(YxUserExtract::getAddTime, addTimeStart).le(YxUserExtract::getAddTime, addTimeEnd);
        }
        return queryWrapper;
    }


    @Override
    //@Cacheable
    public List<YxUserExtract> queryAll(YxUserExtractQueryCriteria criteria) {
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YxUserExtract.class, criteria));
    }


    @Override
    public void download(List<YxUserExtractDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxUserExtractDto yxUserExtract : all) {
            String statusStr = "";
            switch (yxUserExtract.getStatus()) {
                case -1:
                    statusStr = "未通过";
                    break;
                case 0:
                    statusStr = "审核中";
                    break;
                case 1:
                    statusStr = "已提现";
                    break;
                case 2:
                    statusStr = "处理中";
                    break;
                case 3:
                    statusStr = "提现失败";
                    break;
                default:
                    statusStr = "未知状态" + yxUserExtract.getStatus();
                    break;
            }
            Map<String, Object> map = new LinkedHashMap<>();
            // 申请id  用户名  真实姓名  银行卡号  所属银行  支行   联行号   联系电话   提现金额  订单号  用户类型  时间   状态    驳回原因  驳回时间
            map.put("申请id", yxUserExtract.getId());
            map.put("用户名", yxUserExtract.getRealName());
            map.put("真实姓名", yxUserExtract.getUserTrueName());
//            map.put("提现类型", yxUserExtract.getExtractType().equals("bank") ? "银行卡" : "其他");
            map.put("银行卡号", yxUserExtract.getBankCode());
            // 所属银行
            map.put("所属银行", yxUserExtract.getBankAddress());
            // 支行
            map.put("支行", yxUserExtract.getBankAdd());
            // 联行号
            map.put("联行号", yxUserExtract.getCnapsCode());
            // 联系电话
            map.put("联系电话", yxUserExtract.getBankMobile());
            map.put("提现金额", yxUserExtract.getExtractPrice());
            // 订单号
            map.put("订单号", yxUserExtract.getSeqNo());
            map.put("用户类型", yxUserExtract.getUserType() == 3 ? "用户" : "商户");
            map.put("添加时间", DateUtils.timestampToStr10(yxUserExtract.getAddTime()));
            map.put("状态", statusStr);
            map.put("驳回原因", yxUserExtract.getFailMsg());
            map.put("驳回时间", DateUtils.timestampToStr10(yxUserExtract.getFailTime()));
//
//            map.put("开户地址", yxUserExtract.getBankAddress());
//            map.put("支付宝账号", yxUserExtract.getAlipayCode());
//            map.put("提现后余额", yxUserExtract.getBalance());
//            map.put("备注", yxUserExtract.getMark());
//            map.put("微信号", yxUserExtract.getWechat());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * 更新提现是审批结果
     *
     * @param resources
     * @return
     */
    @Override
    public boolean updateExtractStatus(YxUserExtract resources) {// 获取提现申请详情
        YxUserExtract yxUserExtract = this.getById(resources.getId());
        if (null == yxUserExtract) {
            throw new BadRequestException("查询提现申请记录失败");
        }
        if (0 != yxUserExtract.getStatus()) {
            throw new BadRequestException("当前提现申请已审批");
        }

        YxUser yxUser = new YxUser();
        User user = new User();
        // 获取用户昵称
        String username = "";
        // 审核记录里的status
        Integer examineStatus = 0;
        String mark = "";
        //  0:预留 1商户;2合伙人;3用户
        if (3 == yxUserExtract.getUserType()) {
            yxUser = this.yxUserService.getOne(new QueryWrapper<YxUser>().lambda().eq(YxUser::getUid, yxUserExtract.getUid()));
            if (null == yxUser) {
                throw new BadRequestException("查询用户信息失败");
            }
            username = yxUser.getNickname();
        } else {
            user = this.userService.getById(resources.getUid());
            if (null == user) {
                throw new BadRequestException("查询用户信息失败");
            }
            username = user.getNickName();
        }

        // 梗库用
        YxUserExtract updateExtract = new YxUserExtract();
        // 审核驳回
        if (resources.getStatus() == -1) {
            examineStatus = 2;
            if (StrUtil.isEmpty(resources.getFailMsg())) {
                throw new BadRequestException("请填写失败原因");
            }
            mark = "提现失败,退回佣金" + resources.getExtractPrice() + "元";
            // 恢复用户余额
            if (3 == yxUserExtract.getUserType()) {
                this.yxUserService.updateAddMoney(yxUserExtract.getUid(), yxUserExtract.getExtractPrice());
            } else {
                this.userService.updateWithdrawalAmount(user.getId().intValue(), yxUserExtract.getExtractPrice());
            }

            // 更新审核记录
            updateExtract.setId(yxUserExtract.getId());
            updateExtract.setStatus(-1);
            updateExtract.setFailMsg(resources.getFailMsg());
            updateExtract.setMark(mark);
            updateExtract.setFailTime(OrderUtil.getSecondTimestampTwo());
        }
        // 发起申请时已扣减用户余额、审核通过后记录bill和审核记录
        if (resources.getStatus() == 1) {
            examineStatus = 1;
            mark = StringUtils.isNotBlank(resources.getMark()) ? resources.getMark() : "";
            YxUserBill yxUserBill = new YxUserBill();
            yxUserBill.setUid(yxUserExtract.getUid());
            yxUserBill.setUsername(username);
            yxUserBill.setLinkId(yxUserExtract.getId() + "");
            //0 = 支出 1 = 获得
            yxUserBill.setPm(0);
            yxUserBill.setTitle("用户提现");
            yxUserBill.setCategory(BillDetailEnum.CATEGORY_1.getValue());
            yxUserBill.setType(BillDetailEnum.TYPE_4.getValue());
            yxUserBill.setNumber(yxUserExtract.getExtractPrice());
            yxUserBill.setBalance(BigDecimal.ZERO);
            yxUserBill.setMark(mark);
            yxUserBill.setAddTime(OrderUtil.getSecondTimestampTwo());
            yxUserBill.setStatus(1);
            yxUserBill.setUserType(yxUserExtract.getUserType());
            this.yxUserBillService.save(yxUserBill);

            // 更新审核记录
            updateExtract.setId(yxUserExtract.getId());
            updateExtract.setStatus(1);
            updateExtract.setMark(mark);
        }
        this.updateById(updateExtract);
        // 记录提现审核记录
        YxExamineLog yxExamineLog = new YxExamineLog();
        // 审批类型 1:提现 2:商户信息
        yxExamineLog.setUid(resources.getUid());
        yxExamineLog.setUsername(username);
        yxExamineLog.setType(1);
        yxExamineLog.setTypeId(resources.getId());
        yxExamineLog.setStatus(examineStatus);
        yxExamineLog.setRemark(mark);
        yxExamineLog.setDelFlag(0);
        yxExamineLogService.save(yxExamineLog);
        return true;
    }

    /**
     * 查询导出数据
     *
     * @param criteria
     * @return
     */
    @Override
    public List<YxUserExtractDto> queryDownload(YxUserExtractQueryCriteria criteria) {
        QueryWrapper<YxUserExtract> queryWrapper = createQueryWrapper(criteria);
        List<YxUserExtract> yxUserExtracts = this.list(queryWrapper);
        if (null == yxUserExtracts || yxUserExtracts.size() <= 0) {
            throw new BadRequestException("未查询到数据");
        }
        List<YxUserExtractDto> list = generator.convert(yxUserExtracts, YxUserExtractDto.class);
        for (YxUserExtractDto extractDto : list) {
            // 用户类型 1商户;2合伙人;3用户
            if (3 == extractDto.getUserType()) {
                YxUser user = yxUserService.getById(extractDto.getUid());
                if (ObjectUtil.isNotEmpty(user)) {
                    extractDto.setUserTrueName(user.getRealName());
                }
            } else {
                User user = userService.getById(extractDto.getUid());
                if (null != user) {
                    extractDto.setUserTrueName(user.getMerchantsContact());
                }
            }
            // 根据联行号查询支行
            BankCode bankCode = this.bankCodeService.getOne(new QueryWrapper<BankCode>().lambda().eq(BankCode::getBankCode, extractDto.getCnapsCode()));
            if (null != bankCode && StringUtils.isNotBlank(bankCode.getBankAdd())) {
                extractDto.setBankAdd(bankCode.getBankAdd());
                if (StringUtils.isBlank(extractDto.getBankAddress())) {
                    extractDto.setBankAddress(bankCode.getBankName());
                }
            }
        }

        return list;
    }
}
