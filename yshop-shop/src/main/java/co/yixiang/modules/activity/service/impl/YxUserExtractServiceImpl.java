/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 */
package co.yixiang.modules.activity.service.impl;

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
import co.yixiang.modules.shop.domain.User;
import co.yixiang.modules.shop.domain.YxExamineLog;
import co.yixiang.modules.shop.domain.YxUser;
import co.yixiang.modules.shop.domain.YxUserBill;
import co.yixiang.modules.shop.service.UserService;
import co.yixiang.modules.shop.service.YxExamineLogService;
import co.yixiang.modules.shop.service.YxUserBillService;
import co.yixiang.modules.shop.service.YxUserService;
import co.yixiang.utils.FileUtil;
import co.yixiang.utils.OrderUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
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

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxUserExtractQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YxUserExtract> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YxUserExtractDto.class));
        map.put("totalElements", page.getTotal());
        return map;
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
            Map<String, Object> map = new LinkedHashMap<>();
            map.put(" uid", yxUserExtract.getUid());
            map.put("名称", yxUserExtract.getRealName());
            map.put("bank = 银行卡 alipay = 支付宝wx=微信", yxUserExtract.getExtractType());
            map.put("银行卡", yxUserExtract.getBankCode());
            map.put("开户地址", yxUserExtract.getBankAddress());
            map.put("支付宝账号", yxUserExtract.getAlipayCode());
            map.put("提现金额", yxUserExtract.getExtractPrice());
            map.put(" mark", yxUserExtract.getMark());
            map.put(" balance", yxUserExtract.getBalance());
            map.put("无效原因", yxUserExtract.getFailMsg());
            map.put(" failTime", yxUserExtract.getFailTime());
            map.put("添加时间", yxUserExtract.getAddTime());
            map.put("-1 未通过 0 审核中 1 已提现", yxUserExtract.getStatus());
            map.put("微信号", yxUserExtract.getWechat());
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
        // 用户类型0:前台用户1后台用户
        if (0 == yxUserExtract.getUserType()) {
            yxUser = this.yxUserService.getOne(new QueryWrapper<YxUser>().lambda().eq(YxUser::getUid, yxUserExtract.getUid()));
            if (null == yxUser) {
                throw new BadRequestException("查询用户信息失败");
            }
            username = yxUser.getUsername();
        } else {
            user = this.userService.getById(resources.getUid());
            if (null == user) {
                throw new BadRequestException("查询用户信息失败");
            }
            username = user.getUsername();
        }

        // 梗库用
        YxUser updateYxUser = new YxUser();
        User updateUser = new User();
        YxUserExtract updateExtract = new YxUserExtract();
        // 审核驳回
        if (resources.getStatus() == -1) {
            examineStatus = 2;
            if (StrUtil.isEmpty(resources.getFailMsg())) {
                throw new BadRequestException("请填写失败原因");
            }
            mark = "提现失败,退回佣金" + resources.getExtractPrice() + "元";
            // 恢复用户余额
            if (0 == yxUserExtract.getUserType()) {
                updateYxUser.setNowMoney(yxUser.getNowMoney().add(yxUserExtract.getExtractPrice()));
                this.yxUserService.update(updateYxUser, new QueryWrapper<YxUser>().lambda().eq(YxUser::getUid, yxUserExtract.getUid()));
            } else {
                updateUser.setId(user.getId());
                updateUser.setWithdrawalAmount(user.getWithdrawalAmount().add(yxUserExtract.getExtractPrice()));
                this.userService.updateById(updateUser);
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
}
