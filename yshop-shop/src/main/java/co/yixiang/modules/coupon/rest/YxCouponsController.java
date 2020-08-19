package co.yixiang.modules.coupon.rest;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import co.yixiang.exception.BadRequestException;
import co.yixiang.logging.aop.log.Log;
import co.yixiang.modules.coupon.domain.CouponAddRequest;
import co.yixiang.modules.coupon.domain.CouponModifyRequest;
import co.yixiang.modules.coupon.domain.YxCoupons;
import co.yixiang.modules.coupon.domain.YxCouponsCategory;
import co.yixiang.modules.coupon.service.YxCouponsCategoryService;
import co.yixiang.modules.coupon.service.YxCouponsService;
import co.yixiang.modules.coupon.service.dto.YxCouponsQueryCriteria;
import co.yixiang.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
* @author huiy
* @date 2020-08-14
*/
@Slf4j
@AllArgsConstructor
@Api(tags = "卡券表管理")
@RestController
@RequestMapping("/api/yxCoupons")
public class YxCouponsController {

    private final YxCouponsService yxCouponsService;

    private final YxCouponsCategoryService yxCouponsCategoryService;

    @GetMapping
    @Log("查询卡券表")
    @ApiOperation("查询卡券表")
    @PreAuthorize("@el.check('admin','yxCoupons:list')")
    public ResponseEntity<Object> getYxCouponss(YxCouponsQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yxCouponsService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增卡券表")
    @ApiOperation("新增卡券表")
    @PreAuthorize("@el.check('admin','yxCoupons:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody CouponAddRequest request){
        if (request.getCouponType() == 1 && request.getDenomination() == null){
            throw new BadRequestException("代金券面额不可为空!");
        }
        if (request.getCouponType() == 2 && request.getDiscount() == null){
            throw new BadRequestException("请输入折扣券折扣率!");
        }
        if (request.getCouponType() == 3){
            if (null == request.getThreshold()){
                throw new BadRequestException("请输入折扣券的使用门槛!");
            }
            if (null == request.getDiscountAmount()){
                throw new BadRequestException("请输入满减金额!");
            }
        }

        // 校验佣金是否正确, 防止出现负值
        int commission = request.getSellingPrice().subtract(request.getSettlementPrice()).compareTo(BigDecimal.ZERO);
        if (commission < 0){
            throw new BadRequestException("佣金不正确!");
        }

        QueryWrapper<YxCouponsCategory> couponsCategoryQueryWrapper = new QueryWrapper<>();
        couponsCategoryQueryWrapper.lambda()
                .and(obj1->obj1.eq(YxCouponsCategory::getId, request.getCouponCategory()))
                .and(obj2 -> obj2.eq(YxCouponsCategory::getDelFlag, 0));
        YxCouponsCategory yxCouponsCategory = yxCouponsCategoryService.getOne(couponsCategoryQueryWrapper);
        if (yxCouponsCategory == null){
            throw new BadRequestException("当前选择的卡券分类不存在!");
        }

        //TODO:: 商品码生成未实现
        String couponNum = "123333135b";

        QueryWrapper<YxCoupons> yxCouponsQueryWrapper = new QueryWrapper<>();
        yxCouponsQueryWrapper.lambda()
                .and(couponNum0bj -> couponNum0bj.eq(YxCoupons::getCouponNum, couponNum))
                .and(delFlag -> delFlag.eq(YxCoupons::getDelFlag, 0));
        int countCoupons = yxCouponsService.count(yxCouponsQueryWrapper);
        if (countCoupons > 0){
            log.error("卡券核销码[" + couponNum +"]已存在!");
            throw new BadRequestException("卡券核销码已存在, 请联系开发人员");
        }

        // 当前登录用户ID
        int loginUserId = SecurityUtils.getUserId().intValue();

        YxCoupons yxCoupons = new YxCoupons();
        BeanUtil.copyProperties(request, yxCoupons);
        yxCoupons.setCouponNum(couponNum);
        yxCoupons.setIsShow(0);
        yxCoupons.setIsHot(0);
        yxCoupons.setDelFlag(0);
        yxCoupons.setCreateUserId(loginUserId);
        yxCoupons.setCreateTime(DateTime.now().toTimestamp());
        yxCoupons.setUpdateUserId(loginUserId);
        yxCoupons.setUpdateTime(DateTime.now().toTimestamp());
        return new ResponseEntity<>(yxCouponsService.save(yxCoupons),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改卡券表")
    @ApiOperation("修改卡券表")
    @PreAuthorize("@el.check('admin','yxCoupons:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody CouponModifyRequest request){

        if (request.getCouponType() == 1 && request.getDenomination() == null){
            throw new BadRequestException("代金券面额不可为空!");
        }
        if (request.getCouponType() == 2 && request.getDiscount() == null){
            throw new BadRequestException("请输入折扣券折扣率!");
        }
        if (request.getCouponType() == 3){
            if (null == request.getThreshold()){
                throw new BadRequestException("请输入折扣券的使用门槛!");
            }
            if (null == request.getDiscountAmount()){
                throw new BadRequestException("请输入满减金额!");
            }
        }

        // 校验佣金是否正确, 防止出现负值
        int commission = request.getSellingPrice().subtract(request.getSettlementPrice()).compareTo(BigDecimal.ZERO);
        if (commission < 0){
            throw new BadRequestException("佣金不正确!");
        }

        QueryWrapper<YxCouponsCategory> couponsCategoryQueryWrapper = new QueryWrapper<>();
        couponsCategoryQueryWrapper.lambda()
                .and(obj1->obj1.eq(YxCouponsCategory::getId, request.getCouponCategory()))
                .and(obj2 -> obj2.eq(YxCouponsCategory::getDelFlag, 0));
        YxCouponsCategory yxCouponsCategory = yxCouponsCategoryService.getOne(couponsCategoryQueryWrapper);
        if (yxCouponsCategory == null){
            throw new BadRequestException("当前选择的卡券分类不存在!");
        }

        //TODO:: 商品码生成未实现
        String couponNum = "123333135b";

        QueryWrapper<YxCoupons> yxCouponsQueryWrapper = new QueryWrapper<>();
        yxCouponsQueryWrapper.lambda()
                .and(couponNum0bj -> couponNum0bj.eq(YxCoupons::getCouponNum, couponNum))
                .and(delFlag -> delFlag.eq(YxCoupons::getDelFlag, 0));
        int countCoupons = yxCouponsService.count(yxCouponsQueryWrapper);
        if (countCoupons > 0){
            log.error("卡券核销码[" + couponNum +"]已存在!");
            throw new BadRequestException("卡券核销码已存在, 请联系开发人员");
        }

        // 当前登录用户ID
        int loginUserId = SecurityUtils.getUserId().intValue();
        YxCoupons yxCoupons = new YxCoupons();
        BeanUtil.copyProperties(request, yxCoupons);
        yxCoupons.setUpdateUserId(loginUserId);
        yxCoupons.setUpdateTime(DateTime.now().toTimestamp());
        boolean updateStatus = yxCouponsService.updateById(yxCoupons);
        if (updateStatus){
            return new ResponseEntity<>(updateStatus, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(updateStatus, HttpStatus.NO_CONTENT);
        }
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
    public ResponseEntity<Object> putOff(@PathVariable Integer id){

        QueryWrapper<YxCoupons> yxCouponsQueryWrapper = new QueryWrapper<>();
        yxCouponsQueryWrapper.lambda()
                .and(obj -> obj.eq(YxCoupons::getId, id))
                .and(obj2 -> obj2.eq(YxCoupons::getDelFlag, 0));
        YxCoupons yxCoupons = yxCouponsService.getOne(yxCouponsQueryWrapper);
        if (yxCoupons == null){
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
    public ResponseEntity<Object> popular(@PathVariable Integer id){

        QueryWrapper<YxCoupons> yxCouponsQueryWrapper = new QueryWrapper<>();
        yxCouponsQueryWrapper.lambda()
                .and(obj -> obj.eq(YxCoupons::getId, id))
                .and(obj2 -> obj2.eq(YxCoupons::getDelFlag, 0));
        YxCoupons yxCoupons = yxCouponsService.getOne(yxCouponsQueryWrapper);
        if (yxCoupons == null){
            throw new BadRequestException("未找到相关数据, 请确认!");
        }

        // 更新上下架状态
        YxCoupons updateCoupon = new YxCoupons();
        updateCoupon.setId(id);
        updateCoupon.setIsHot(yxCoupons.getIsHot() == 1 ? 0 : 1);
        boolean updateStatus = yxCouponsService.updateById(updateCoupon);
        return new ResponseEntity<>(updateStatus, HttpStatus.OK);
    }
}
