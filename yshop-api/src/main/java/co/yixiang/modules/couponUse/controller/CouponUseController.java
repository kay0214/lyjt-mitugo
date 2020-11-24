/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package co.yixiang.modules.couponUse.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import co.yixiang.annotation.AnonymousAccess;
import co.yixiang.aspectj.annotation.NeedLogin;
import co.yixiang.common.constant.CommonConstant;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.constant.ShopConstants;
import co.yixiang.constant.SystemConfigConstants;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.exception.BadRequestException;
import co.yixiang.logging.aop.log.Log;
import co.yixiang.modules.couponUse.criteria.YxCouponOrderUseQueryCriteria;
import co.yixiang.modules.couponUse.dto.YxCouponsDto;
import co.yixiang.modules.couponUse.dto.YxStoreInfoDto;
import co.yixiang.modules.coupons.service.YxCouponOrderService;
import co.yixiang.modules.coupons.service.YxCouponOrderUseService;
import co.yixiang.modules.coupons.service.YxCouponsService;
import co.yixiang.modules.image.service.YxImageInfoService;
import co.yixiang.modules.manage.entity.SystemUser;
import co.yixiang.modules.manage.entity.UserAvatar;
import co.yixiang.modules.manage.entity.UsersRoles;
import co.yixiang.modules.manage.entity.YxMerchantsDetail;
import co.yixiang.modules.manage.service.SystemUserService;
import co.yixiang.modules.manage.service.UserAvatarService;
import co.yixiang.modules.manage.service.YxMerchantsDetailService;
import co.yixiang.modules.manage.web.vo.SystemUserParamVo;
import co.yixiang.modules.security.security.vo.AuthUser;
import co.yixiang.modules.shop.entity.YxStoreInfo;
import co.yixiang.modules.shop.service.YxStoreInfoService;
import co.yixiang.modules.shop.service.YxSystemConfigService;
import co.yixiang.modules.user.service.YxSystemAttachmentService;
import co.yixiang.modules.user.service.YxUserService;
import co.yixiang.tools.domain.QiniuContent;
import co.yixiang.tools.service.QiNiuService;
import co.yixiang.utils.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wf.captcha.ArithmeticCaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author PC-LIUSHOUYI
 * @version CouponUseController, v0.1 2020/9/5 13:42
 */
@Slf4j
@RestController
@RequestMapping("/yxCouponsUse")
@Api(value = "本地生活, 卡券核销", tags = "本地生活, 卡券核销", description = "订单")
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
    @Value("${file.localUrl}")
    private String localUrl;
    @Value("${file.path}")
    private String path;
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
    @Autowired
    private YxUserService yxUserService;
    @Autowired
    private SystemUserService systemUserService;
    @Autowired
    QiNiuService qiNiuService;
    @Autowired
    private YxSystemAttachmentService systemAttachmentService;
    @Autowired
    private YxSystemConfigService systemConfigService;
    @Autowired
    private UserAvatarService userAvatarService;

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
    public ResponseEntity login(@Validated @RequestBody AuthUser authUser,
                                HttpServletRequest request) {
        // 密码解密
        String password = AESUtils.AES_Decrypt(authUser.getPassword(), aesKey, aesMode, iv);
        if (StringUtils.isBlank(password)) {
            throw new BadRequestException("密码错误");
        }

        SystemUser user = yxUserService.getSystemUserByUserNameNew(authUser.getUsername());
        if (user == null || user.getId() == null) {
            throw new BadRequestException("用户名或密码错误");
        }
        /*if (user.getUserRole().intValue() != 2) {
            throw new BadRequestException("暂无权限");
        }*/
        String pass = user.getUserpassword();
        if (!PassWordUtil.getUserPassWord(password, user.getUserRole(), user.getUsername()).equals(pass)) {
            throw new BadRequestException("密码错误");
        }
        // 判断当前登陆用户是否是商户
        /*YxStoreInfo yxStoreInfo = this.yxStoreInfoService.getOne(new QueryWrapper<YxStoreInfo>().eq("mer_id", user.getId()));
        if (null == yxStoreInfo) {
            throw new BadRequestException("无可用门店，请先到蜜兔管理平台创建门店");
        }*/
        YxStoreInfo yxStoreInfo = this.yxStoreInfoService.getById(user.getStoreId());
        if (null == yxStoreInfo) {
            throw new BadRequestException("无可用门店，请先到蜜兔管理平台创建门店");
        }

        UsersRoles usersRoles = yxUserService.getUserRolesByUserId(user.getId().intValue());
        if (null == usersRoles) {
            throw new BadRequestException("此用户未配置角色，请先到平台分配角色");
        }
        int intRoleId = usersRoles.getRoleId().intValue();
        List<Integer> listIds = new ArrayList<>();
        //7	核销人员	只能登录核销端   只有核销功能
        listIds.add(SystemConfigConstants.ROLE_VERIFICATION);
        //8	船只核销人员	只能登录核销端
        listIds.add(SystemConfigConstants.ROLE_SHIPVER);
        //9	船长	只能登录核销端
        listIds.add(SystemConfigConstants.ROLE_CAPTAIN);
        //10	景区推广	只能登录核销端
        listIds.add(SystemConfigConstants.ROLE_SPREAD);
        if (!listIds.contains(intRoleId)) {
            throw new BadRequestException("暂无权限");
        }

        SystemUserParamVo systemUserParamVo = new SystemUserParamVo();
        systemUserParamVo.setUsername(user.getUsername());
        systemUserParamVo.setUserRole(intRoleId);
        // 默认头像
        String defaultAvatar = this.systemConfigService.getData(SystemConfigConstants.ADMIN_DEFAULT_AVATAR);
        if (StringUtils.isNotBlank(defaultAvatar)) {
            systemUserParamVo.setAvatar(defaultAvatar);
        }
        systemUserParamVo.setAvatar("https://mtcdn.metoogo.cn/admin_tx.png");
        // 处理用户头像
        if (null != user.getAvatarId()) {
            UserAvatar userAvatar = this.userAvatarService.getById(user.getAvatarId());
            if (null != userAvatar) {
                // 获取数据库配置的api接口地址
                String apiUrl = systemConfigService.getData(SystemConfigConstants.API_URL);
                if (StringUtils.isNotBlank(apiUrl)) {
                    systemUserParamVo.setAvatar(apiUrl + "/" + "avatar/" + userAvatar.getRealName());
                }
            }
        }
        // 生成一个token给前端 56
        String token = CommonConstant.COUPON_USE_LOGIN_TOKEN + SecretUtil.createRandomStr(10) + UUID.randomUUID();
        // 设置30天失效
        redisUtils.set(token, user, 30, TimeUnit.DAYS);

        // 返回 token 与 用户信息
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("token", token);
        map.put("userInfo", systemUserParamVo);
        map.put("status", "1");
        map.put("statusDesc", "成功");
        return ResponseEntity.ok(map);
    }

    @NeedLogin
    @GetMapping(value = "/getMerchantsDetailByUid")
    @AnonymousAccess
    @ApiOperation("B端：获取商户及门店信息")
    public ResponseEntity<Object> getMerchantsDetailByUid(@RequestHeader(value = "token") String token) {

        Map<String, Object> map = new HashMap<>();
        // 获取登陆用户的id
        SystemUser user = getRedisUser(token);

        int uid = user.getId().intValue();
        // 判断当前登陆用户是否是商户
        YxStoreInfo yxStoreInfo = this.yxStoreInfoService.getOne(new QueryWrapper<YxStoreInfo>().eq("mer_id", uid));
        if (null == yxStoreInfo) {
            map.put("status", "99");
            map.put("statusDesc", "无可用门店，请先到蜜兔管理平台创建门店");
            return ResponseEntity.ok(map);
        }
        YxMerchantsDetail yxMerchantsDetail = this.yxMerchantsDetailService.getOne(new QueryWrapper<YxMerchantsDetail>().eq("uid", uid));
        if (null == yxMerchantsDetail) {
            map.put("status", "99");
            map.put("statusDesc", "无可用商户认证信息，请先到蜜兔管理平台提交审核");
            return ResponseEntity.ok(map);
        } else if (1 != yxMerchantsDetail.getExamineStatus()) {
            map.put("status", "99");
            map.put("statusDesc", "商户认证信息未审批或审批未通过，请先到蜜兔管理平台核实");
            return ResponseEntity.ok(map);
        }
        YxStoreInfoDto yxStoreInfoDto = generator.convert(yxStoreInfo, YxStoreInfoDto.class);        //店铺缩略图
        yxStoreInfoDto.setStoreImage(yxImageInfoService.selectImgByParam(yxStoreInfoDto.getId(), ShopConstants.IMG_TYPE_STORE, ShopConstants.IMG_CATEGORY_PIC));
        //轮播图
        yxStoreInfoDto.setStoreRotationImages(yxImageInfoService.selectImgByParamList(yxStoreInfoDto.getId(), ShopConstants.IMG_TYPE_STORE, ShopConstants.IMG_CATEGORY_ROTATION1));

        map.put("status", "1");
        map.put("statusDesc", "成功");
        map.put("data", yxStoreInfoDto);
        return ResponseEntity.ok(map);
    }

    @NeedLogin
    @Log("根据核销码查询卡券信息")
    @AnonymousAccess
    @ApiOperation("B端：根据核销码查询卡券信息")
    @GetMapping(value = "/getCouponDetail")
    public ResponseEntity<Object> getCouponDetail(@RequestHeader(value = "token") String token, @RequestParam(value = "verifyCode") String verifyCode) {
        // 获取登陆用户的id
        Map<String, String> map = new HashMap<>();
        SystemUser user = getRedisUser(token);

        int uid = user.getId().intValue();
        String decodeVerifyCode = "";
        try {
            decodeVerifyCode = Base64Utils.decode(verifyCode);
        } catch (Exception e) {
            throw new BadRequestException("无效卡券");
        }
        return ResponseEntity.ok(this.yxCouponsService.getCouponByVerifyCode(decodeVerifyCode, user));
    }


    @NeedLogin
    @ApiOperation("B端：查询核销记录")
    @AnonymousAccess
    @PostMapping("/getOrderUseList")
    public ResponseEntity<Object> getOrderUseList(@RequestHeader(value = "token") String token, @RequestBody YxCouponOrderUseQueryCriteria criteria) {
        // 获取登陆用户的id
        Map<String, String> map = new HashMap<>();
        SystemUser user = getRedisUser(token);

        int uid = user.getId().intValue();
        criteria.setStoreId(user.getStoreId());
        return ResponseEntity.ok(yxCouponOrderUseService.queryAll(criteria));
    }

    @NeedLogin
    @Log("扫码核销卡券")
    @AnonymousAccess
    @ApiOperation("B端：扫码核销卡券")
    @GetMapping(value = "/useCoupon")
    public ResponseEntity<Object> updateCouponOrder(@RequestHeader(value = "token") String token,
                                                    @RequestParam(value = "verifyCode") String verifyCode,
                                                    @RequestParam(value = "isAll", defaultValue = "false") Boolean isAll) {
        // 获取登陆用户的id
        Map<String, String> map = new HashMap<>();
        SystemUser user = getRedisUser(token);

        int uid = user.getId().intValue();
        String decodeVerifyCode = "";
        try {
            decodeVerifyCode = Base64Utils.decode(verifyCode);
        } catch (Exception e) {
            throw new BadRequestException("无效卡券");
        }
        // 增加打印日志
        if (isAll) {
            log.info("------------打印日志----------------" + decodeVerifyCode);
        }
        log.info("增加打印日志看下传参：" + isAll.toString());

        Map<String, Object> result = this.yxCouponOrderService.updateCouponOrder(decodeVerifyCode, uid, isAll);
        return ResponseEntity.ok(result);
    }

    @NeedLogin
    @Log("手动核销卡券")
    @AnonymousAccess
    @ApiOperation("B端：手动核销卡券（废）")
    @GetMapping(value = "/useCouponInput")
    public ResponseEntity<Object> updateCouponOrderInput(@RequestHeader(value = "token") String token, @RequestParam(value = "orderId") String orderId) {
        // 获取登陆用户的id
        Map<String, String> map = new HashMap<>();
        SystemUser user = getRedisUser(token);

        int uid = user.getId().intValue();
        boolean result = this.yxCouponOrderService.updateCouponOrderInput(orderId, uid);
        if (result) {
            map.put("status", "1");
            map.put("statusDesc", "核销成功");
        } else {
            map.put("status", "99");
            map.put("statusDesc", "核销失败");
        }
        return ResponseEntity.ok(map);
    }

    @NeedLogin
    @ApiOperation("B端：手动核销查询卡券")
    @AnonymousAccess
    @GetMapping(value = "/getUseCouponInput")
    public ResponseEntity<Object> getUseCouponInput(@RequestHeader(value = "token") String token, @RequestParam(value = "orderId") String orderId) {
        // 获取登陆用户的id
        Map<String, Object> map = new HashMap<>();
        SystemUser user = getRedisUser(token);

        int uid = user.getId().intValue();
        YxCouponsDto yxCouponsDto = this.yxCouponsService.getCouponByOrderId(orderId, user);
        if (1 == yxCouponsDto.getStatus()) {
            map.put("data", yxCouponsDto);
        }
        map.put("status", yxCouponsDto.getStatus());
        map.put("statusDesc", yxCouponsDto.getStatusDesc());
        return ResponseEntity.ok(map);
    }


    @NeedLogin
    @ApiOperation("B端：生成线下支付码")
    @AnonymousAccess
    @GetMapping(value = "/getPayOfflineCode")
    public ResponseEntity<Object> getPayOfflineCode(@RequestHeader(value = "token") String token) {
        SystemUser user = getRedisUser(token);
        // 获取用户所属店铺
        if (null == user.getStoreId()) {
            user = systemUserService.getById(user.getId());
        }
        // 获取店铺信息
        YxStoreInfo yxStoreInfo = this.yxStoreInfoService.getById(user.getStoreId());
        if (null == yxStoreInfo) {
            throw new BadRequestException("获取店铺信息失败");
        }
        // 获取数据库配置的api接口地址
        String apiUrl = systemConfigService.getData(SystemConfigConstants.API_URL);
        // 组装线下收款地址
        if (!apiUrl.endsWith("/")) {
            apiUrl = apiUrl.concat("/");
        }
        String urlAddress = apiUrl.concat("shop/?productId=").concat(yxStoreInfo.getStoreNid()).concat("&codeType=offline");

        // 二维码路径+地址
        String fileDir = path + "qrcode" + File.separator;
        File path = new File(fileDir);
        if (!path.exists()) {
            path.mkdirs();
        }

        // 二维码名称：商铺nid+商铺id+offline+时间戳
        String name = yxStoreInfo.getStoreNid() + "_" + yxStoreInfo.getId() + "_offline_" + DateUtils.getNowTime() + ".jpg";

        // 生成二维码
        QrConfig config = new QrConfig(300, 300);
        config.setMargin(0);
        File file = new File(fileDir + name);
        QrCodeUtil.generate(urlAddress, config, file);

        // 返回二维码的url地址
        String qrCodeUrl = "";
        if (StrUtil.isEmpty(localUrl)) {
            QiniuContent qiniuContent = qiNiuService.uploadPic(file, qiNiuService.find());
            systemAttachmentService.attachmentAdd(name, String.valueOf(qiniuContent.getSize()),
                    qiniuContent.getUrl(), qiniuContent.getUrl(), 2);
            qrCodeUrl = qiniuContent.getUrl();
        } else {
            systemAttachmentService.attachmentAdd(name, String.valueOf(FileUtil.size(file)),
                    fileDir + name, "qrcode/" + name);
            qrCodeUrl = apiUrl + "/file/qrcode/" + name;
        }

        Map<String, String> map = new HashMap<>();
        map.put("status", "1");
        map.put("statusDesc", "成功");
        map.put("data", qrCodeUrl);
        return ResponseEntity.ok(map);
    }
}
