/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package co.yixiang.modules.couponUse.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import co.yixiang.annotation.AnonymousAccess;
import co.yixiang.common.api.ApiResult;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.constant.ShopConstants;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.exception.BadRequestException;
import co.yixiang.logging.aop.log.Log;
import co.yixiang.modules.couponUse.criteria.YxCouponOrderUseQueryCriteria;
import co.yixiang.modules.couponUse.dto.YxStoreInfoDto;
import co.yixiang.modules.coupons.service.YxCouponOrderService;
import co.yixiang.modules.coupons.service.YxCouponOrderUseService;
import co.yixiang.modules.coupons.service.YxCouponsService;
import co.yixiang.modules.image.service.YxImageInfoService;
import co.yixiang.modules.manage.entity.YxMerchantsDetail;
import co.yixiang.modules.manage.service.YxMerchantsDetailService;
import co.yixiang.modules.security.security.TokenProvider;
import co.yixiang.modules.security.security.vo.AuthUser;
import co.yixiang.modules.security.service.OnlineUserService;
import co.yixiang.modules.shop.entity.YxStoreInfo;
import co.yixiang.modules.shop.service.YxStoreInfoService;
import co.yixiang.utils.*;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wf.captcha.ArithmeticCaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author PC-LIUSHOUYI
 * @version CouponUseController, v0.1 2020/9/5 13:42
 */
@Slf4j
@RestController
@RequestMapping("/yxCouponsUse")
@Api(value = "本地生活, 卡券核销")
public class CouponUseController extends BaseController {
    @Value("${loginCode.expiration}")
    private Long expiration;
    @Value("${single.login}")
    private Boolean singleLogin;
    @Value("${aes.localKey}")
    private String aesKey;
    @Value("${aes.iv}")
    private String iv;
    @Value("${aes.mode}")
    private String aesMode;
    @Autowired
    private OnlineUserService onlineUserService;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private YxStoreInfoService yxStoreInfoService;
    @Autowired
    private YxMerchantsDetailService yxMerchantsDetailService;
    @Autowired
    private YxImageInfoService yxImageInfoService;
    @Autowired
    private YxCouponsService yxCouponsService;
    @Autowired
    private YxCouponOrderUseService yxCouponOrderUseService;
    @Autowired
    private YxCouponOrderService yxCouponOrderService;

    private final IGenerator generator;

    public CouponUseController(IGenerator generator) {
        this.generator = generator;
    }

    @AnonymousAccess
    @ApiOperation("获取验证码")
    @GetMapping(value = "/code")
    public ResponseEntity<Object> getCode() {
        // 算术类型 https://gitee.com/whvse/EasyCaptcha
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(111, 36);
        // 几位数运算，默认是两位
        captcha.setLen(2);
        // 获取运算的结果
        String result = "";
        try {
            result = new Double(Double.parseDouble(captcha.text())).intValue() + "";
        } catch (Exception e) {
            result = captcha.text();
        }
        String uuid = IdUtil.simpleUUID();
        // 保存
        redisUtils.set(uuid, result, expiration, TimeUnit.MINUTES);
        // 验证码信息
        Map<String, Object> imgResult = new HashMap<String, Object>(2) {{
            put("img", captcha.toBase64());
            put("uuid", uuid);
        }};
        return ResponseEntity.ok(imgResult);
    }

    @Log("B端核销登陆")
    @ApiOperation("B端核销登陆")
    @AnonymousAccess
    @PostMapping(value = "/login")
    public ApiResult<Map<String, Object>> login(@Validated @RequestBody AuthUser authUser,
                                                HttpServletRequest request) {
        // 密码解密
        String password = AESUtils.AES_Decrypt(authUser.getPassword(), aesKey, aesMode, iv);
        if (StringUtils.isBlank(password)) {
            throw new BadRequestException("用户名或密码错误");
        }
        // 查询验证码
        String code = (String) redisUtils.get(authUser.getUuid());
        // 清除验证码
        redisUtils.del(authUser.getUuid());
        if (StringUtils.isBlank(code)) {
            throw new BadRequestException("验证码不存在或已过期");
        }
        if (StringUtils.isBlank(authUser.getCode()) || !authUser.getCode().equalsIgnoreCase(code)) {
            throw new BadRequestException("验证码错误");
        }

        // TODO 验证用户名密码并存到redis的token中
        // 生成一个token给前端 56
        String token = SecretUtil.createRandomStr(10) + UUID.randomUUID();

        Date expiresTime = tokenProvider.getExpirationDateFromToken(token);
        String expiresTimeStr = DateUtil.formatDateTime(expiresTime);
        // 返回 token 与 用户信息
        Map<String, Object> authInfo = new HashMap<String, Object>(2) {{
            put("token", token);
            put("expires_time", expiresTimeStr);
        }};
        if (singleLogin) {
            //踢掉之前已经登录的token
            onlineUserService.checkLoginOnUser(authUser.getUsername(), token);
        }
        // 返回 token
        return ApiResult.ok(authInfo);
    }

    @GetMapping(value = "/getMerchantsDetailByUid")
    @Log("获取商户及门店信息")
    @ApiOperation("B端：获取商户及门店信息")
    public ResponseEntity<Object> getMerchantsDetailByUid() {

        // 获取登陆用户的id
//        int uid = SecurityUtils.getUserId().intValue();
        // 测试写死
        int uid = 56;
        Map<String, String> map = new HashMap<>();
        // 判断当前登陆用户是否是商户
        YxStoreInfo yxStoreInfo = this.yxStoreInfoService.getOne(new QueryWrapper<YxStoreInfo>().eq("mer_id", uid));
        if (null == yxStoreInfo) {
            map.put("status", "1");
            map.put("statusDesc", "无可用门店，请先到蜜兔管理平台创建门店");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        YxMerchantsDetail yxMerchantsDetail = this.yxMerchantsDetailService.getOne(new QueryWrapper<YxMerchantsDetail>().eq("uid", uid));
        if (null == yxMerchantsDetail) {
            map.put("status", "2");
            map.put("statusDesc", "无可用商户认证信息，请先到蜜兔管理平台提交审核");
            return new ResponseEntity<>(map, HttpStatus.OK);
        } else if (1 != yxMerchantsDetail.getExamineStatus()) {
            map.put("status", "3");
            map.put("statusDesc", "商户认证信息未审批或审批未通过，请先到蜜兔管理平台核实");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        YxStoreInfoDto yxStoreInfoDto = generator.convert(yxStoreInfo, YxStoreInfoDto.class);        //店铺缩略图
        yxStoreInfoDto.setStoreImage(yxImageInfoService.selectImgByParam(yxStoreInfoDto.getId(), ShopConstants.IMG_TYPE_STORE, ShopConstants.IMG_CATEGORY_PIC));
        //轮播图
        yxStoreInfoDto.setStoreRotationImages(yxImageInfoService.selectImgByParamList(yxStoreInfoDto.getId(), ShopConstants.IMG_TYPE_STORE, ShopConstants.IMG_CATEGORY_ROTATION1));

        map.put("status", "0");
        map.put("statusDesc", "成功");
        map.put("data", JSON.toJSONString(yxStoreInfoDto));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @Log("根据核销码查询卡券信息")
    @ApiOperation("B端：根据核销码查询卡券信息")
    @GetMapping(value = "/getCouponDetail/{verifyCode}")
    public ResponseEntity<Object> getCouponDetail(@PathVariable String verifyCode) {
        // 获取登陆用户的id
//        int uid = SecurityUtils.getUserId().intValue();
        // 测试写死
        int uid = 56;
        return new ResponseEntity<>(this.yxCouponsService.getCouponByVerifyCode(Base64Utils.decode(verifyCode), uid), HttpStatus.OK);
    }


    @Log("查询核销记录")
    @ApiOperation("B端：查询核销记录")
    @PostMapping("/getOrderUseList")
    public ResponseEntity<Object> getOrderUseList(YxCouponOrderUseQueryCriteria criteria, Pageable pageable) {
        // 获取登陆用户的id
//        int uid = SecurityUtils.getUserId().intValue();
        // 测试写死
        int uid = 56;
        criteria.setCreateUserId(uid);
        return new ResponseEntity<>(yxCouponOrderUseService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @Log("扫码核销卡券")
    @ApiOperation("B端：扫码核销卡券")
    @GetMapping(value = "/useCoupon/{verifyCode}")
    public ResponseEntity<Object> updateCouponOrder(@PathVariable String verifyCode) {
        // 获取登陆用户的id
//        int uid = SecurityUtils.getUserId().intValue();
        // 测试写死
        int uid = 56;
        boolean result = this.yxCouponOrderService.updateCouponOrder(Base64Utils.decode(verifyCode), uid);
        Map<String, String> map = new HashMap<>();
        if (result) {
            map.put("status", "0");
            map.put("statusDesc", "核销成功");
        } else {
            map.put("status", "1");
            map.put("statusDesc", "核销失败");
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @Log("手动核销卡券")
    @ApiOperation("B端：手动核销卡券")
    @GetMapping(value = "/useCouponInput/{orderId}")
    public ResponseEntity<Object> updateCouponOrderInput(@PathVariable String orderId) {
        // 获取登陆用户的id
//        int uid = SecurityUtils.getUserId().intValue();
        // 测试写死
        int uid = 56;
        boolean result = this.yxCouponOrderService.updateCouponOrderInput(orderId, uid);
        Map<String, String> map = new HashMap<>();
        if (result) {
            map.put("status", "0");
            map.put("statusDesc", "核销成功");
        } else {
            map.put("status", "1");
            map.put("statusDesc", "核销失败");
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

}