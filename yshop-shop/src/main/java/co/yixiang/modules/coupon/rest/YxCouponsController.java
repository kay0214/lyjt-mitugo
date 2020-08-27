package co.yixiang.modules.coupon.rest;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import co.yixiang.constant.LocalLiveConstants;
import co.yixiang.constant.ShopConstants;
import co.yixiang.exception.BadRequestException;
import co.yixiang.logging.aop.log.Log;
import co.yixiang.modules.coupon.domain.CouponAddRequest;
import co.yixiang.modules.coupon.domain.CouponModifyRequest;
import co.yixiang.modules.coupon.domain.YxCoupons;
import co.yixiang.modules.coupon.domain.YxCouponsCategory;
import co.yixiang.modules.coupon.service.YxCouponsCategoryService;
import co.yixiang.modules.coupon.service.YxCouponsService;
import co.yixiang.modules.coupon.service.dto.YxCouponsDto;
import co.yixiang.modules.coupon.service.dto.YxCouponsQueryCriteria;
import co.yixiang.modules.shop.domain.User;
import co.yixiang.modules.shop.domain.YxImageInfo;
import co.yixiang.modules.shop.domain.YxStoreInfo;
import co.yixiang.modules.shop.service.UserService;
import co.yixiang.modules.shop.service.YxImageInfoService;
import co.yixiang.modules.shop.service.YxStoreInfoService;
import co.yixiang.utils.OrderUtil;
import co.yixiang.utils.SecurityUtils;
import co.yixiang.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private UserService userService;

    @Autowired
    private YxStoreInfoService yxStoreInfoService;

    @Autowired
    private YxImageInfoService yxImageInfoService;

    @GetMapping
    @Log("查询卡券表")
    @ApiOperation("查询卡券表")
    @PreAuthorize("@el.check('admin','yxCoupons:list')")
    public ResponseEntity<Object> getYxCouponss(YxCouponsQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(yxCouponsService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增卡券表")
    @ApiOperation("新增卡券表")
    @PreAuthorize("@el.check('admin','yxCoupons:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody CouponAddRequest request) {

        // 当前登录用户ID
        int loginUserId = SecurityUtils.getUserId().intValue();

        User getOneUser = userService.getOne(new QueryWrapper<User>().eq("id", loginUserId).eq("merchants_status", 0));
        if (getOneUser == null) {
            throw new BadRequestException("当前登录用户异常!");
        }
        if (getOneUser.getUserRole() != 2) {
            throw new BadRequestException("当前登录用户非商户, 不可操作!");
        }

        YxStoreInfo findStoreInfo = yxStoreInfoService.getOne(new QueryWrapper<YxStoreInfo>().eq("mer_id", getOneUser.getId()).eq("del_flag", 0));
        if (findStoreInfo == null) {
            throw new BadRequestException("当前商户未绑定商铺!");
        }

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

        QueryWrapper<YxCouponsCategory> couponsCategoryQueryWrapper = new QueryWrapper<>();
        couponsCategoryQueryWrapper.lambda()
                .and(obj1 -> obj1.eq(YxCouponsCategory::getId, request.getCouponCategory()))
                .and(obj2 -> obj2.eq(YxCouponsCategory::getDelFlag, 0));
        YxCouponsCategory yxCouponsCategory = yxCouponsCategoryService.getOne(couponsCategoryQueryWrapper);
        if (yxCouponsCategory == null) {
            throw new BadRequestException("当前选择的卡券分类不存在!");
        }

        String couponNum = OrderUtil.orderSn();

        QueryWrapper<YxCoupons> yxCouponsQueryWrapper = new QueryWrapper<>();
        yxCouponsQueryWrapper.lambda()
                .and(couponNum0bj -> couponNum0bj.eq(YxCoupons::getCouponNum, couponNum))
                .and(delFlag -> delFlag.eq(YxCoupons::getDelFlag, 0));
        int countCoupons = yxCouponsService.count(yxCouponsQueryWrapper);
        if (countCoupons > 0) {
            log.error("卡券核销码[" + couponNum + "]已存在!");
            throw new BadRequestException("卡券核销码已存在, 请联系开发人员");
        }

        YxCoupons yxCoupons = new YxCoupons();
        BeanUtil.copyProperties(request, yxCoupons);
        // 添加以后不可修改所属商铺
        yxCoupons.setBelong(findStoreInfo.getId());
        yxCoupons.setCouponNum(couponNum);
        yxCoupons.setIsShow(0);
        yxCoupons.setIsHot(0);
        yxCoupons.setDelFlag(0);
        yxCoupons.setCreateUserId(loginUserId);
        yxCoupons.setCreateTime(DateTime.now().toTimestamp());
        yxCoupons.setUpdateUserId(loginUserId);
        yxCoupons.setUpdateTime(DateTime.now().toTimestamp());
        boolean saveStatus = yxCouponsService.save(yxCoupons);
        if (saveStatus) {
            couponImg(yxCoupons.getId(), request.getImage(), request.getSliderImage(), loginUserId);
        }
        return new ResponseEntity<>(saveStatus, HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改卡券表")
    @ApiOperation("修改卡券表")
    @PreAuthorize("@el.check('admin','yxCoupons:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody CouponModifyRequest request) {

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

        QueryWrapper<YxCouponsCategory> couponsCategoryQueryWrapper = new QueryWrapper<>();
        couponsCategoryQueryWrapper.lambda()
                .and(obj1 -> obj1.eq(YxCouponsCategory::getId, request.getCouponCategory()))
                .and(obj2 -> obj2.eq(YxCouponsCategory::getDelFlag, 0));
        YxCouponsCategory yxCouponsCategory = yxCouponsCategoryService.getOne(couponsCategoryQueryWrapper);
        if (yxCouponsCategory == null) {
            throw new BadRequestException("当前选择的卡券分类不存在!");
        }

        // 当前登录用户ID
        int loginUserId = SecurityUtils.getUserId().intValue();
        YxCoupons yxCoupons = new YxCoupons();
        BeanUtil.copyProperties(request, yxCoupons);
        yxCoupons.setUpdateUserId(loginUserId);
        yxCoupons.setUpdateTime(DateTime.now().toTimestamp());
        boolean updateStatus = yxCouponsService.updateById(yxCoupons);
        if (updateStatus) {
            if (updateStatus) {
                couponImg(yxCoupons.getId(), request.getImage(), request.getSliderImage(), loginUserId);
            }
            return new ResponseEntity<>(updateStatus, HttpStatus.OK);
        } else {
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

    /**
     * 缩略图操作
     *
     * @param typeId
     * @param imgPath     缩略图
     * @param sliderPath  缩略图
     * @param loginUserId
     */
    private void couponImg(Integer typeId, String imgPath, String sliderPath, Integer loginUserId) {

        if (StringUtils.isNotBlank(imgPath)) {
            // 查询缩略图图片是否存在(已存在则删除)
            QueryWrapper<YxImageInfo> imageInfoQueryWrapper = new QueryWrapper<>();
            imageInfoQueryWrapper.lambda()
                    .and(type -> type.eq(YxImageInfo::getTypeId, typeId))
                    .and(imgCate -> imgCate.eq(YxImageInfo::getImgCategory, ShopConstants.IMG_CATEGORY_PIC))
                    .and(imgType -> imgType.eq(YxImageInfo::getImgType, LocalLiveConstants.IMG_TYPE_COUPONS))
                    .and(del -> del.eq(YxImageInfo::getDelFlag, false));

            List<YxImageInfo> imageInfoList = yxImageInfoService.list(imageInfoQueryWrapper);

            if (imageInfoList.size() > 0) {
                // 删除已存在的图片
                for (YxImageInfo imageInfo : imageInfoList) {
                    YxImageInfo delImageInfo = new YxImageInfo();
                    delImageInfo.setId(imageInfo.getId());
                    delImageInfo.setDelFlag(1);
                    delImageInfo.setUpdateUserId(loginUserId);
                    delImageInfo.setUpdateTime(DateTime.now().toTimestamp());
                    yxImageInfoService.updateById(delImageInfo);
                }
            }

            // 写入分类对应的图片关联表
            YxImageInfo imageInfo = new YxImageInfo();
            imageInfo.setTypeId(typeId);
            // 卡券分类 img_type 为 5
            imageInfo.setImgType(LocalLiveConstants.IMG_TYPE_COUPONS);
            imageInfo.setImgCategory(ShopConstants.IMG_CATEGORY_PIC);
            imageInfo.setImgUrl(imgPath);
            imageInfo.setDelFlag(0);
            imageInfo.setCreateUserId(loginUserId);
            imageInfo.setUpdateUserId(loginUserId);
            imageInfo.setCreateTime(DateTime.now().toTimestamp());
            imageInfo.setUpdateTime(DateTime.now().toTimestamp());
            yxImageInfoService.save(imageInfo);
        }

        if (StringUtils.isNotBlank(sliderPath)) {
            // 幻灯片
            QueryWrapper<YxImageInfo> sliderImageInfoQueryWrapper = new QueryWrapper<>();
            sliderImageInfoQueryWrapper.lambda()
                    .and(type -> type.eq(YxImageInfo::getTypeId, typeId))
                    .and(imgCate -> imgCate.eq(YxImageInfo::getImgCategory, ShopConstants.IMG_CATEGORY_ROTATION1))
                    .and(imgType -> imgType.eq(YxImageInfo::getImgType, LocalLiveConstants.IMG_TYPE_COUPONS))
                    .and(del -> del.eq(YxImageInfo::getDelFlag, false));

            List<YxImageInfo> sliderImageInfoList = yxImageInfoService.list(sliderImageInfoQueryWrapper);

            List<YxImageInfo> delSliderImageInfoList = new ArrayList<YxImageInfo>();
            if (sliderImageInfoList.size() > 0) {
                // 删除已存在的图片
                for (YxImageInfo imageInfo : sliderImageInfoList) {
                    YxImageInfo delImageInfo = new YxImageInfo();
                    delImageInfo.setId(imageInfo.getId());
                    delImageInfo.setDelFlag(1);
                    delImageInfo.setUpdateUserId(loginUserId);
                    delImageInfo.setUpdateTime(DateTime.now().toTimestamp());
                    delSliderImageInfoList.add(delImageInfo);
                }
                yxImageInfoService.updateBatchById(delSliderImageInfoList, delSliderImageInfoList.size());
            }

            List<YxImageInfo> yxImageInfoList = new ArrayList<YxImageInfo>();
            String[] images = sliderPath.split(",");
            if (images.length > 0) {
                for (int i = 0; i < images.length; i++) {
                    YxImageInfo yxImageInfos = new YxImageInfo();
                    yxImageInfos.setTypeId(typeId);
                    yxImageInfos.setImgType(LocalLiveConstants.IMG_TYPE_COUPONS);
                    yxImageInfos.setImgCategory(ShopConstants.IMG_CATEGORY_ROTATION1);
                    yxImageInfos.setImgUrl(images[i]);
                    yxImageInfos.setDelFlag(0);
                    yxImageInfos.setUpdateUserId(loginUserId);
                    yxImageInfos.setCreateUserId(loginUserId);
                    yxImageInfoList.add(yxImageInfos);
                }
                yxImageInfoService.saveBatch(yxImageInfoList, yxImageInfoList.size());
            }
        }
    }

    @Log("根据核销码查询卡券信息")
    @ApiOperation("B端：根据核销码查询卡券信息")
    @GetMapping(value = "/getCouponDetail/{verifyCode}")
    public ResponseEntity<Object> getCouponDetail(@PathVariable String verifyCode) {
        int uid = SecurityUtils.getUserId().intValue();
        YxCouponsDto yxCouponsDto = this.yxCouponsService.getCouponByVerifyCode(verifyCode, uid);
        if (null == yxCouponsDto) {
            yxCouponsDto = new YxCouponsDto();
            yxCouponsDto.setStatus(0);
        }
        return new ResponseEntity<>(yxCouponsDto, HttpStatus.OK);
    }
}
