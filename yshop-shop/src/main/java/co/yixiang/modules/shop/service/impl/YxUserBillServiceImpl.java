/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 */
package co.yixiang.modules.shop.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.enums.BillDetailEnum;
import co.yixiang.modules.shop.domain.User;
import co.yixiang.modules.shop.domain.YxFundsAccount;
import co.yixiang.modules.shop.domain.YxUserBill;
import co.yixiang.modules.shop.service.UserService;
import co.yixiang.modules.shop.service.YxFundsAccountService;
import co.yixiang.modules.shop.service.YxUserBillService;
import co.yixiang.modules.shop.service.dto.WithdrawReviewQueryCriteria;
import co.yixiang.modules.shop.service.dto.YxUserBillDto;
import co.yixiang.modules.shop.service.dto.YxUserBillQueryCriteria;
import co.yixiang.modules.shop.service.mapper.UserBillMapper;
import co.yixiang.utils.DateUtils;
import co.yixiang.utils.FileUtil;
import co.yixiang.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
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

/**
 * @author hupeng
 * @date 2020-05-12
 */
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "yxUserBill")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxUserBillServiceImpl extends BaseServiceImpl<UserBillMapper, YxUserBill> implements YxUserBillService {

    private final IGenerator generator;
    @Autowired
    private UserService userService;
    @Autowired
    private UserBillMapper userBillMapper;
    @Autowired
    private YxFundsAccountService fundsAccountService;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxUserBillQueryCriteria criteria, Pageable pageable) {
//        getPage(pageable);
//        PageInfo<YxUserBillDto> page = new PageInfo<>(queryAll(criteria));
        QueryWrapper<YxUserBill> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByDesc(YxUserBill::getAddTime);
        int userType = 0;
        if (1 == criteria.getUserRole()) {
            userType = 2;
            queryWrapper.lambda().eq(YxUserBill::getUid, criteria.getUid()).eq(YxUserBill::getUserType, 2);
        }
        if (2 == criteria.getUserRole()) {
            userType = 1;
            queryWrapper.lambda().eq(YxUserBill::getUid, criteria.getUid()).eq(YxUserBill::getUserType, 1);
        }
        if (StringUtils.isNotBlank(criteria.getUsername())) {
            queryWrapper.lambda().like(YxUserBill::getUsername, criteria.getUsername());
        }
        //收支类型
        if (null != criteria.getPm()) {
            queryWrapper.lambda().eq(YxUserBill::getPm, criteria.getPm());
        }
        if (StringUtils.isNotBlank(criteria.getTitle())) {
            queryWrapper.lambda().like(YxUserBill::getTitle, criteria.getTitle());
        }
        if (StringUtils.isNotBlank(criteria.getAddTimeStart()) && StringUtils.isNotBlank(criteria.getAddTimeEnd())) {
            queryWrapper.lambda().ge(YxUserBill::getAddTime, DateUtils.stringToTimestamp(criteria.getAddTimeStart() + " 00:00:00")).le(YxUserBill::getAddTime, DateUtils.stringToTimestamp(criteria.getAddTimeEnd() + " 23:59:59"));
        }
        //明细种类
        if (StringUtils.isNotBlank(criteria.getCategory())) {
            queryWrapper.lambda().eq(YxUserBill::getCategory, criteria.getCategory());
        }

        //明细类型
        if (StringUtils.isNotBlank(criteria.getType())) {
            queryWrapper.lambda().eq(YxUserBill::getType, criteria.getType());
        }
        User user = this.userService.getById(criteria.getUid());

        IPage<YxUserBill> ipage = this.page(new Page<>(pageable.getPageNumber() + 1, pageable.getPageSize()), queryWrapper);

        Map<String, Object> map = new LinkedHashMap<>(4);
        map.put("content", ipage.getRecords());
        map.put("totalElements", ipage.getTotal());
        map.put("totalPrice", user.getWithdrawalAmount());
        BigDecimal bigOut = userBillMapper.getSumPrice(criteria.getUid(), 0, userType);
        BigDecimal bigIn = userBillMapper.getSumPrice(criteria.getUid(), 1, userType);
//        map.put("remainPrice", user.getWithdrawalAmount());
        map.put("remainPrice", bigIn);
        map.put("expenditurePrice", bigOut);
        return map;
    }

    @Override
//    @Cacheable
    public List<YxUserBillDto> queryAll(YxUserBillQueryCriteria criteria) {
        Integer startTime = null;
        Integer endTime = null;
        if (StringUtils.isNotBlank(criteria.getAddTimeStart())) {
            startTime = DateUtils.stringToTimestamp(criteria.getAddTimeStart() + " 00:00:00");
            endTime = DateUtils.stringToTimestamp(criteria.getAddTimeEnd() + " 23:59:59");
        }
        return baseMapper.findAllByQueryCriteria(criteria.getUsername(), criteria.getTitle(), criteria.getPm(), startTime, endTime);
    }

    @Override
    public Map<String, Object> queryAll(WithdrawReviewQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YxUserBillDto> page = new PageInfo<>(queryAll2(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", page.getList());
        map.put("totalElements", page.getTotal());
        return map;
    }

    private List<YxUserBillDto> queryAll2(WithdrawReviewQueryCriteria criteria) {
        return baseMapper.withdrawReviewLog(criteria.getLinkId());
    }

    @Override
    public void download(List<YxUserBillDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxUserBillDto yxUserBill : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("用户uid", yxUserBill.getUid());
            map.put("关联id", yxUserBill.getLinkId());
            map.put("0 = 支出 1 = 获得", yxUserBill.getPm());
            map.put("账单标题", yxUserBill.getTitle());
            map.put("明细种类", yxUserBill.getCategory());
            map.put("明细类型", yxUserBill.getType());
            map.put("明细数字", yxUserBill.getNumber());
            map.put("剩余", yxUserBill.getBalance());
            map.put("备注", yxUserBill.getMark());
            map.put("添加时间", yxUserBill.getAddTime());
            map.put("0 = 带确定 1 = 有效 -1 = 无效", yxUserBill.getStatus());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * 获取用户积分明细
     *
     * @param criteria
     * @param pageable
     * @return
     */
    @Override
    public Map<String, Object> getPointDetail(YxUserBillQueryCriteria criteria, Pageable pageable) {
        BigDecimal totalPoint = BigDecimal.ZERO;
        QueryWrapper<YxUserBill> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(YxUserBill::getCategory, BillDetailEnum.CATEGORY_2.getValue()).eq(YxUserBill::getStatus, 1);
        // 检索条件
        if (StringUtils.isNotBlank(criteria.getAddTimeStart()) && StringUtils.isNotBlank(criteria.getAddTimeStart())) {
            queryWrapper.lambda().ge(YxUserBill::getAddTime, DateUtils.stringToTimestamp(criteria.getAddTimeStart())).le(YxUserBill::getAddTime, DateUtils.stringToTimestamp(criteria.getAddTimeEnd()));
        }
        if (0 != criteria.getUserRole()) {
            // 非管理员的情况、获取登陆用户的数据
            queryWrapper.lambda().eq(YxUserBill::getUid, criteria.getUid());
            User user = this.userService.getById(criteria.getUid());
            totalPoint = user.getTotalScore();
        } else {
            YxFundsAccount yxFundsAccount = fundsAccountService.getOne(new QueryWrapper<YxFundsAccount>().lambda().eq(YxFundsAccount::getDelFlag, 0));
            if (null != yxFundsAccount) {
                totalPoint = yxFundsAccount.getBonusPoint().add(yxFundsAccount.getReferencePoint());
            }
        }
        queryWrapper.lambda().orderByDesc(YxUserBill::getAddTime);
        IPage<YxUserBill> ipage = this.page(new Page<>(pageable.getPageNumber() + 1, pageable.getPageSize()), queryWrapper);

        Map<String, Object> map = new LinkedHashMap<>(3);
        map.put("content", ipage.getRecords());
        map.put("totalElements", ipage.getTotal());
        map.put("totalPoint", totalPoint);
        return map;
    }

    /**
     * 获取分红池数据
     *
     * @param criteria
     * @param pageable
     * @return
     */
    @Override
    public Map<String, Object> getShareDividendPoint(YxUserBillQueryCriteria criteria, Pageable pageable) {
        BigDecimal totalPoint = BigDecimal.ZERO;
        YxFundsAccount yxFundsAccount = fundsAccountService.getOne(new QueryWrapper<YxFundsAccount>().lambda().eq(YxFundsAccount::getDelFlag, 0));
        if (null != yxFundsAccount) {
            totalPoint = yxFundsAccount.getBonusPoint();
        }
        QueryWrapper<YxUserBill> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(YxUserBill::getCategory, BillDetailEnum.CATEGORY_2.getValue()).eq(YxUserBill::getType, BillDetailEnum.TYPE_11.getValue()).eq(YxUserBill::getStatus, 1);
        if (StringUtils.isNotBlank(criteria.getAddTimeStart()) && StringUtils.isNotBlank(criteria.getAddTimeStart())) {
            queryWrapper.lambda().ge(YxUserBill::getAddTime, DateUtils.stringToTimestamp(criteria.getAddTimeStart() + " 00:00:00")).le(YxUserBill::getAddTime, DateUtils.stringToTimestamp(criteria.getAddTimeEnd() + " 23:59:59"));
        }
        if (StringUtils.isNotBlank(criteria.getUsername())) {
            queryWrapper.lambda().like(YxUserBill::getUsername, criteria.getUsername());
        }

        IPage<YxUserBill> ipage = this.page(new Page<>(pageable.getPageNumber() + 1, pageable.getPageSize()), queryWrapper);
        Map<String, Object> map = new LinkedHashMap<>(3);
        map.put("content", ipage.getRecords());
        map.put("totalElements", ipage.getTotal());
        map.put("totalPoint", totalPoint);
        return map;
    }

    /**
     * 获取拉新池数据
     *
     * @param criteria
     * @param pageable
     * @return
     */
    @Override
    public Map<String, Object> getPullNewPoint(YxUserBillQueryCriteria criteria, Pageable pageable) {

        BigDecimal totalPoint = BigDecimal.ZERO;
        YxFundsAccount yxFundsAccount = fundsAccountService.getOne(new QueryWrapper<YxFundsAccount>().lambda().eq(YxFundsAccount::getDelFlag, 0));
        if (null != yxFundsAccount) {
            totalPoint = yxFundsAccount.getReferencePoint();
        }
        QueryWrapper<YxUserBill> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(YxUserBill::getCategory, BillDetailEnum.CATEGORY_2.getValue()).eq(YxUserBill::getType, BillDetailEnum.TYPE_12.getValue()).eq(YxUserBill::getStatus, 1);
        if (StringUtils.isNotBlank(criteria.getAddTimeStart()) && StringUtils.isNotBlank(criteria.getAddTimeStart())) {
            queryWrapper.lambda().ge(YxUserBill::getAddTime, DateUtils.stringToTimestamp(criteria.getAddTimeStart() + " 00:00:00")).le(YxUserBill::getAddTime, DateUtils.stringToTimestamp(criteria.getAddTimeEnd() + " 23:59:59"));
        }

        IPage<YxUserBill> ipage = this.page(new Page<>(pageable.getPageNumber() + 1, pageable.getPageSize()), queryWrapper);
        Map<String, Object> map = new LinkedHashMap<>(3);
        map.put("content", ipage.getRecords());
        map.put("totalElements", ipage.getTotal());
        map.put("totalPoint", totalPoint);
        return map;
    }

    /**
     * 平台资金明细
     *
     * @param criteria
     * @param pageable
     * @return
     */
    @Override
    public Map<String, Object> queryAllNew(YxUserBillQueryCriteria criteria, Pageable pageable) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        YxFundsAccount yxFundsAccount = fundsAccountService.getOne(new QueryWrapper<YxFundsAccount>().lambda().eq(YxFundsAccount::getDelFlag, 0));
        if (null != yxFundsAccount) {
            // 平台分佣总金额
            totalPrice = yxFundsAccount.getPrice();
        }
        QueryWrapper<YxUserBill> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(YxUserBill::getCategory, BillDetailEnum.CATEGORY_1.getValue()).eq(YxUserBill::getStatus, 1);
        if (StringUtils.isNotBlank(criteria.getAddTimeStart()) && StringUtils.isNotBlank(criteria.getAddTimeStart())) {
            queryWrapper.lambda().ge(YxUserBill::getAddTime, DateUtils.stringToTimestamp(criteria.getAddTimeStart() + " 00:00:00")).le(YxUserBill::getAddTime, DateUtils.stringToTimestamp(criteria.getAddTimeEnd() + " 23:59:59"));
        }
        //收支类型
        if (null != criteria.getPm()) {
            queryWrapper.lambda().eq(YxUserBill::getPm, criteria.getPm());
        }
        if (StringUtils.isNotBlank(criteria.getTitle())) {
            queryWrapper.lambda().like(YxUserBill::getTitle, criteria.getTitle());
        }
        //明细类型
        if (StringUtils.isNotBlank(criteria.getType())) {
            queryWrapper.lambda().eq(YxUserBill::getType, criteria.getType());
        }
        // 用户昵称
        if (StringUtils.isNotBlank(criteria.getUsername())) {
            queryWrapper.lambda().like(YxUserBill::getUsername, criteria.getUsername());
        }
        if (null != criteria.getUserType()) {
            queryWrapper.lambda().eq(YxUserBill::getUserType, criteria.getUserType());
        }

        IPage<YxUserBill> ipage = this.page(new Page<>(pageable.getPageNumber() + 1, pageable.getPageSize()), queryWrapper);
        Map<String, Object> map = new LinkedHashMap<>(3);
        map.put("content", ipage.getRecords());
        map.put("totalElements", ipage.getTotal());
        map.put("totalPrice", totalPrice);
        return map;
    }
}
