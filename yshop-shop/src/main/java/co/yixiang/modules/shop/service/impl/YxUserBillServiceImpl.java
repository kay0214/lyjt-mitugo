/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 */
package co.yixiang.modules.shop.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.constant.SystemConfigConstants;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.enums.BillDetailEnum;
import co.yixiang.exception.BadRequestException;
import co.yixiang.modules.activity.service.dto.YxUserExtractSetDto;
import co.yixiang.modules.shop.domain.User;
import co.yixiang.modules.shop.domain.YxFundsAccount;
import co.yixiang.modules.shop.domain.YxUserBill;
import co.yixiang.modules.shop.service.UserService;
import co.yixiang.modules.shop.service.YxFundsAccountService;
import co.yixiang.modules.shop.service.YxSystemConfigService;
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
    @Autowired
    private YxSystemConfigService systemConfigService;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxUserBillQueryCriteria criteria, Pageable pageable) {
//        getPage(pageable);
//        PageInfo<YxUserBillDto> page = new PageInfo<>(queryAll(criteria));
        QueryWrapper<YxUserBill> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(YxUserBill::getCategory, BillDetailEnum.CATEGORY_1.getValue());
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
            // 状态处理
            String status = "";
            if (0 == yxUserBill.getStatus()) {
                status = "待确定";
            } else if (1 == yxUserBill.getStatus()) {
                status = "有效";
            } else if (2 == yxUserBill.getStatus()) {
                status = "无效";
            }
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("用户uid", yxUserBill.getUid());
//            map.put("关联id", yxUserBill.getLinkId());
            map.put("类型", yxUserBill.getPm() == 0 ? "支出" : "获得");
            map.put("账单标题", yxUserBill.getTitle());
            map.put("明细种类", BillDetailEnum.getDesc(yxUserBill.getCategory()));
            map.put("明细类型", BillDetailEnum.getDesc(yxUserBill.getType()));
            map.put("明细数字", yxUserBill.getNumber());
            map.put("剩余", yxUserBill.getBalance());
            map.put("备注", yxUserBill.getMark());
            map.put("添加时间", DateUtils.timestampToStr10(yxUserBill.getAddTime()));
            map.put("状态", status);
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
            queryWrapper.lambda().ge(YxUserBill::getAddTime, DateUtils.stringToTimestamp(criteria.getAddTimeStart() + " 00:00:00")).le(YxUserBill::getAddTime, DateUtils.stringToTimestamp(criteria.getAddTimeEnd() + " 23:59:59"));
        }
        // 用户昵称模糊查询
        if (StringUtils.isNotBlank(criteria.getUsername())) {
            queryWrapper.lambda().like(YxUserBill::getUsername, criteria.getUsername());
        }
        if (StringUtils.isNotBlank(criteria.getType())) {
            queryWrapper.lambda().eq(YxUserBill::getType, criteria.getType());
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
        queryWrapper.lambda().orderByDesc(YxUserBill::getAddTime);

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
        queryWrapper.lambda().orderByDesc(YxUserBill::getAddTime);

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
        queryWrapper.lambda().orderByDesc(YxUserBill::getAddTime);

        IPage<YxUserBill> ipage = this.page(new Page<>(pageable.getPageNumber() + 1, pageable.getPageSize()), queryWrapper);
        Map<String, Object> map = new LinkedHashMap<>(3);
        map.put("content", ipage.getRecords());
        map.put("totalElements", ipage.getTotal());
        map.put("totalPrice", totalPrice);
        return map;
    }

    /**
     * 修改提现配置
     *
     * @param request
     * @return
     */
    @Override
    public boolean updateExtractSet(YxUserExtractSetDto request) {
        // ----------------------商户相关-----------------------
        // 商户最低提现金额
        String storeMinPrice = request.getStoreExtractMinPrice();
        if (StringUtils.isBlank(request.getStoreExtractMinPrice())) {
            storeMinPrice = "0";
        }
        boolean result = this.systemConfigService.saveOrUpdateValue(SystemConfigConstants.STORE_EXTRACT_MIN_PRICE, storeMinPrice);
        if (!result) {
            throw new BadRequestException("商户最低提现金额设置失败");
        }
        // 商户提现费率 0-100
        BigDecimal storeRate = BigDecimal.ZERO;
        if (StringUtils.isNotBlank(request.getStoreExtractRate())) {
            storeRate = new BigDecimal(request.getStoreExtractRate());
        }
        if (storeRate.compareTo(BigDecimal.ZERO) < 0 || storeRate.compareTo(new BigDecimal(100)) > 0) {
            throw new BadRequestException("商户提现费率设置区间0-100");
        }
        result = this.systemConfigService.saveOrUpdateValue(SystemConfigConstants.STORE_EXTRACT_RATE, storeRate.toString());
        if (!result) {
            throw new BadRequestException("商户提现费率设置设置失败");
        }

        // ----------------------用户相关-----------------------
        // 用户最低提现金额
        String userMinPrice = request.getUserExtractMinPrice();
        if (StringUtils.isBlank(request.getUserExtractMinPrice())) {
            userMinPrice = "0";
        }
        result = this.systemConfigService.saveOrUpdateValue(SystemConfigConstants.USER_EXTRACT_MIN_PRICE, userMinPrice);
        if (!result) {
            throw new BadRequestException("商户最低提现金额设置失败");
        }
        // 用户提现费率 0-1
        BigDecimal userRate = BigDecimal.ZERO;
        if (StringUtils.isNotBlank(request.getUserExtractRate())) {
            userRate = new BigDecimal(request.getUserExtractRate());
        }
        if (userRate.compareTo(BigDecimal.ZERO) < 0 || userRate.compareTo(new BigDecimal(100)) > 0) {
            throw new BadRequestException("商户提现费率设置区间0-100");
        }
        result = this.systemConfigService.saveOrUpdateValue(SystemConfigConstants.USER_EXTRACT_RATE, userRate.toString());
        if (!result) {
            throw new BadRequestException("商户提现费率设置设置失败");
        }
        return result;
    }

    /**
     * 获取用户提现配置
     *
     * @return
     */
    @Override
    public YxUserExtractSetDto getExtractSet() {
        YxUserExtractSetDto result = new YxUserExtractSetDto();
        // 商户最低提现金额
        String storeMinPrice = systemConfigService.getData(SystemConfigConstants.STORE_EXTRACT_MIN_PRICE);
        if (StringUtils.isBlank(storeMinPrice)) {
            storeMinPrice = "0";
            systemConfigService.saveOrUpdateValue(SystemConfigConstants.STORE_EXTRACT_MIN_PRICE, storeMinPrice);
        }
        // 商户最低提现费率
        String storeRate = systemConfigService.getData(SystemConfigConstants.STORE_EXTRACT_RATE);
        if (StringUtils.isBlank(storeRate)) {
            storeRate = "10";
            systemConfigService.saveOrUpdateValue(SystemConfigConstants.STORE_EXTRACT_RATE, storeRate);
        }

        // 用户最低提现金额
        String userMinPrice = systemConfigService.getData(SystemConfigConstants.USER_EXTRACT_MIN_PRICE);
        if (StringUtils.isBlank(userMinPrice)) {
            userMinPrice = "0";
            systemConfigService.saveOrUpdateValue(SystemConfigConstants.USER_EXTRACT_MIN_PRICE, userMinPrice);
        }

        // 用户提现费率
        String userRate = systemConfigService.getData(SystemConfigConstants.USER_EXTRACT_RATE);
        if (StringUtils.isBlank(userRate)) {
            userRate = "0";
            systemConfigService.saveOrUpdateValue(SystemConfigConstants.USER_EXTRACT_RATE, userRate);
        }
        result.setStoreExtractMinPrice(storeMinPrice);
        result.setStoreExtractRate(storeRate);
        result.setUserExtractMinPrice(userMinPrice);
        result.setUserExtractRate(userRate);
        return result;
    }

    /**
     * 查询资金明细导出数据
     *
     * @param criteria
     * @return
     */
    @Override
    public List<YxUserBillDto> queryDownloadUserBill(YxUserBillQueryCriteria criteria) {
        QueryWrapper<YxUserBill> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(YxUserBill::getCategory, BillDetailEnum.CATEGORY_1.getValue());
        queryWrapper.lambda().orderByDesc(YxUserBill::getAddTime);
        if (1 == criteria.getUserRole()) {
            queryWrapper.lambda().eq(YxUserBill::getUid, criteria.getUid()).eq(YxUserBill::getUserType, 2);
        }
        if (2 == criteria.getUserRole()) {
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
        List<YxUserBill> list = this.list(queryWrapper);
        if (null == list || list.size() <= 0) {
            throw new BadRequestException("未查询到数据");
        }
        return generator.convert(list, YxUserBillDto.class);
    }

    /**
     * 查询平台资金明细导出数据
     *
     * @param criteria
     * @return
     */
    @Override
    public List<YxUserBillDto> queryUserBillAll(YxUserBillQueryCriteria criteria) {
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
        queryWrapper.lambda().orderByDesc(YxUserBill::getAddTime);
        List<YxUserBill> list = this.list(queryWrapper);
        if (null == list || list.size() <= 0) {
            throw new BadRequestException("未查询到数据");
        }
        return generator.convert(list, YxUserBillDto.class);
    }
}
