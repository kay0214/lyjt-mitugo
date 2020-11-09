package co.yixiang.modules.coupon.rest;

import cn.hutool.core.date.DateTime;
import co.yixiang.constant.ShopConstants;
import co.yixiang.exception.BadRequestException;
import co.yixiang.logging.aop.log.Log;
import co.yixiang.modules.coupon.domain.CouponAddRequest;
import co.yixiang.modules.coupon.domain.CouponModifyRateRequest;
import co.yixiang.modules.coupon.domain.CouponModifyRequest;
import co.yixiang.modules.coupon.domain.YxCoupons;
import co.yixiang.modules.coupon.service.YxCouponsCategoryService;
import co.yixiang.modules.coupon.service.YxCouponsService;
import co.yixiang.modules.coupon.service.dto.YxCouponsQueryCriteria;
import co.yixiang.modules.shop.domain.User;
import co.yixiang.modules.shop.domain.YxStoreInfo;
import co.yixiang.modules.shop.service.UserService;
import co.yixiang.modules.shop.service.YxStoreInfoService;
import co.yixiang.utils.Base64Utils;
import co.yixiang.utils.CurrUser;
import co.yixiang.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * @author liusy
 * @date 2020-08-31
 */
@Slf4j
@AllArgsConstructor
@Api(tags = "卡券表管理")
@RestController
@RequestMapping("/api/yxCoupons")
public class YxCouponsController {

    private final YxCouponsService yxCouponsService;

    private final YxCouponsCategoryService yxCouponsCategoryService;
    @Autowired
    private UserService userService;
    @Autowired
    private YxStoreInfoService yxStoreInfoService;

    @GetMapping
    @ApiOperation("查询卡券表")
    @PreAuthorize("@el.check('admin','yxCoupons:list')")
    public ResponseEntity<Object> getYxCoupons(YxCouponsQueryCriteria criteria, Pageable pageable) {
        CurrUser currUser = SecurityUtils.getCurrUser();
        criteria.setUserRole(currUser.getUserRole());
        if (null != currUser.getChildUser()) {
            criteria.setChildUser(currUser.getChildUser());
            criteria.setChildStoreId(this.yxStoreInfoService.getStoreIdByMerId(currUser.getChildUser()));
        }
        return new ResponseEntity<>(yxCouponsService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增卡券表")
    @ApiOperation("新增卡券表")
    @PreAuthorize("@el.check('admin','yxCoupons:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody CouponAddRequest request) {

        // 当前登录用户ID
        int loginUserId = SecurityUtils.getUserId().intValue();
        request.setCreateUser(loginUserId);

        User getOneUser = userService.getOne(new QueryWrapper<User>().eq("id", loginUserId).eq("merchants_status", 0));
        if (getOneUser == null) {
            throw new BadRequestException("当前登录用户异常!");
        }
//        if (getOneUser.getUserRole() != 2) {
//            throw new BadRequestException("当前登录用户非商户, 不可操作!");
//        }

        YxStoreInfo findStoreInfo = yxStoreInfoService.getOne(new QueryWrapper<YxStoreInfo>().eq("mer_id", getOneUser.getId()).eq("del_flag", 0));
        if (findStoreInfo == null) {
            throw new BadRequestException("当前商户未绑定商铺!");
        }
        request.setStoreId(findStoreInfo.getId());

        if (request.getCouponName().length() > 42) {
            throw new BadRequestException("代金券名称长度不正确!");
        }

        if (request.getCouponType() == 1 && request.getDenomination() == null) {
            throw new BadRequestException("代金券面额不可为空!");
        }
        if (request.getCouponType() == 2 && request.getDiscount() == null) {
            throw new BadRequestException("请输入折扣券折扣率!");
        }
        if (request.getCouponType() == 3) {
            if (null == request.getThreshold()) {
                throw new BadRequestException("请输入折扣券的使用门槛!");
            }
            if (null == request.getDiscountAmount()) {
                throw new BadRequestException("请输入满减金额!");
            }
        }

        // 校验佣金是否正确, 防止出现负值
        int commission = request.getSellingPrice().subtract(request.getSettlementPrice()).compareTo(BigDecimal.ZERO);
        if (commission < 0) {
            throw new BadRequestException("佣金不正确!");
        }

        boolean saveStatus = this.yxCouponsService.createCoupons(request);

        return new ResponseEntity<>(saveStatus, HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改卡券表")
    @ApiOperation("修改卡券表")
    @PreAuthorize("@el.check('admin','yxCoupons:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody CouponModifyRequest request) {

        // 当前登录用户ID
        int loginUserId = SecurityUtils.getUserId().intValue();
        request.setCreateUser(loginUserId);

        if (request.getCouponName().length() > 42) {
            throw new BadRequestException("代金券名称长度不正确!");
        }

        if (request.getCouponType() == 1 && request.getDenomination() == null) {
            throw new BadRequestException("代金券面额不可为空!");
        }
        if (request.getCouponType() == 2 && request.getDiscount() == null) {
            throw new BadRequestException("请输入折扣券折扣率!");
        }
        if (request.getCouponType() == 3) {
            if (null == request.getThreshold()) {
                throw new BadRequestException("请输入折扣券的使用门槛!");
            }
            if (null == request.getDiscountAmount()) {
                throw new BadRequestException("请输入满减金额!");
            }
        }

        // 校验佣金是否正确, 防止出现负值
        int commission = request.getSellingPrice().subtract(request.getSettlementPrice()).compareTo(BigDecimal.ZERO);
        if (commission < 0) {
            throw new BadRequestException("佣金不正确!");
        }
        boolean updateStatus = this.yxCouponsService.updateCoupons(request);
        return new ResponseEntity<>(updateStatus, HttpStatus.OK);
    }

    @Log("修改分佣比例")
    @ApiOperation(value = "修改分佣比例")
    @PostMapping(value = "/updateRate")
    @PreAuthorize("@el.check('admin','yxCoupons:rate')")
    public ResponseEntity updateRate(@Validated @RequestBody CouponModifyRateRequest request) {
        int sysUserId = SecurityUtils.getUserId().intValue();
        if (null == request.getId()) {
            throw new BadRequestException("主键不能为空");
        }
        if (null == request.getCustomizeType()) {
            throw new BadRequestException("请选择分佣类型");
        }
        // 自定义分佣比例校验总分成是否是100
        if (2 == request.getCustomizeType()) {
            BigDecimal fundsRate = request.getYxCustomizeRate().getFundsRate();
            BigDecimal shareRate = request.getYxCustomizeRate().getShareRate();
            BigDecimal shareParentRate = request.getYxCustomizeRate().getShareParentRate();
            BigDecimal parentRate = request.getYxCustomizeRate().getParentRate();
            BigDecimal partnerRate = request.getYxCustomizeRate().getPartnerRate();
            BigDecimal referenceRate = request.getYxCustomizeRate().getReferenceRate();
            BigDecimal merRate = request.getYxCustomizeRate().getMerRate();
            // 分佣总和应等于100
            BigDecimal count = fundsRate.add(shareRate).add(shareParentRate).add(parentRate).add(partnerRate).add(referenceRate).add(merRate);
            // 分佣比例总和应等于100
            if (count.compareTo(new BigDecimal("100")) != 0) {
                throw new BadRequestException("分佣比例配置不正确!");
            }
        }
        request.setCreateUser(sysUserId);
        this.yxCouponsService.updateRate(request);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("删除卡券表")
    @ApiOperation("删除卡券表")
    @PreAuthorize("@el.check('admin','yxCoupons:del')")
    @DeleteMapping(value = "/delCoupon/{ids}")
    public ResponseEntity<Object> deleteAll(@PathVariable String ids) {
        String[] idsArr = ids.split(",");
        boolean delStatus = false;
        for (String id : idsArr) {
            YxCoupons yxCoupons = new YxCoupons();
            yxCoupons.setId(Integer.valueOf(id));
            yxCoupons.setDelFlag(1);
            yxCoupons.setUpdateUserId(SecurityUtils.getUserId().intValue());
            yxCoupons.setUpdateTime(DateTime.now().toTimestamp());
            delStatus = yxCouponsService.removeById(id);
        }
        return new ResponseEntity<>(delStatus, HttpStatus.OK);
    }

    @Log("上架下架操作")
    @ApiOperation("卡券上下架操作")
    @PostMapping(value = "/pufOff/{id}")
    @PreAuthorize("@el.check('admin','yxCoupons:putoff')")
    public ResponseEntity<Object> putOff(@PathVariable Integer id) {

        QueryWrapper<YxCoupons> yxCouponsQueryWrapper = new QueryWrapper<>();
        yxCouponsQueryWrapper.lambda()
                .and(obj -> obj.eq(YxCoupons::getId, id))
                .and(obj2 -> obj2.eq(YxCoupons::getDelFlag, 0));
        YxCoupons yxCoupons = yxCouponsService.getOne(yxCouponsQueryWrapper);
        if (yxCoupons == null) {
            throw new BadRequestException("未找到相关数据, 请确认!");
        }

        // 更新上下架状态
        YxCoupons updateCoupon = new YxCoupons();
        updateCoupon.setId(id);
        updateCoupon.setIsShow(yxCoupons.getIsShow() == 1 ? 0 : 1);
        boolean updateStatus = yxCouponsService.updateById(updateCoupon);
        return new ResponseEntity<>(updateStatus, HttpStatus.OK);
    }

    @Log("设为热销操作")
    @ApiOperation("设为热销操作")
    @PostMapping(value = "/popular/{id}")
    @PreAuthorize("@el.check('admin','yxCoupons:popular')")
    public ResponseEntity<Object> popular(@PathVariable Integer id) {

        QueryWrapper<YxCoupons> yxCouponsQueryWrapper = new QueryWrapper<>();
        yxCouponsQueryWrapper.lambda()
                .and(obj -> obj.eq(YxCoupons::getId, id))
                .and(obj2 -> obj2.eq(YxCoupons::getDelFlag, 0));
        YxCoupons yxCoupons = yxCouponsService.getOne(yxCouponsQueryWrapper);
        if (yxCoupons == null) {
            throw new BadRequestException("未找到相关数据, 请确认!");
        }

        // 更新上下架状态
        YxCoupons updateCoupon = new YxCoupons();
        updateCoupon.setId(id);
        updateCoupon.setIsHot(yxCoupons.getIsHot() == 1 ? 0 : 1);
        boolean updateStatus = yxCouponsService.updateById(updateCoupon);
        return new ResponseEntity<>(updateStatus, HttpStatus.OK);
    }

    @ApiOperation("B端：根据核销码查询卡券信息")
    @GetMapping(value = "/getCouponDetail/{verifyCode}")
    public ResponseEntity<Object> getCouponDetail(@PathVariable String verifyCode) {
        int uid = SecurityUtils.getUserId().intValue();
        return new ResponseEntity<>(this.yxCouponsService.getCouponByVerifyCode(Base64Utils.decode(verifyCode), uid), HttpStatus.OK);
    }
}
