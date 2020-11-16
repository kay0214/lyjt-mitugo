/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package co.yixiang.modules.couponUse.controller;

import co.yixiang.annotation.AnonymousAccess;
import co.yixiang.aspectj.annotation.NeedLogin;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.exception.BadRequestException;
import co.yixiang.modules.couponUse.dto.UserBillVo;
import co.yixiang.modules.couponUse.param.UserAccountQueryParam;
import co.yixiang.modules.couponUse.param.UserUpdateParam;
import co.yixiang.modules.manage.entity.SystemUser;
import co.yixiang.modules.manage.entity.YxMerchantsDetail;
import co.yixiang.modules.manage.service.YxMerchantsDetailService;
import co.yixiang.modules.user.service.YxUserBillService;
import co.yixiang.modules.user.service.YxUserService;
import co.yixiang.utils.AESUtils;
import co.yixiang.utils.PassWordUtil;
import co.yixiang.utils.RedisUtils;
import co.yixiang.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 交易流水
 */
@Slf4j
@RestController
@RequestMapping("/userAccount")
@Api(value = "核销端 交易流水")
public class UserAccountController extends BaseController {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private YxUserBillService billService;

    @Autowired
    private YxMerchantsDetailService yxMerchantsDetailService;
    @Value("${aes.localKey}")
    private String aesKey;
    @Value("${aes.iv}")
    private String iv;
    @Value("${aes.mode}")
    private String aesMode;
    @Autowired
    private YxUserService yxUserService;


    private final IGenerator generator;

    public UserAccountController(IGenerator generator) {
        this.generator = generator;
    }


    @NeedLogin
    @AnonymousAccess
    @ApiOperation("B端：线下交易流水列表")
    @GetMapping(value = "/getUserAccountList")
    public ResponseEntity<Object> getUserAccountList(@RequestHeader(value = "token") String token, UserAccountQueryParam param) {
        Map<String, Object> map = new HashMap<>();
        SystemUser user = getRedisUser(token);
        YxMerchantsDetail detail = yxMerchantsDetailService.getById(user.getStoreId());
        Paging<UserBillVo> result = billService.getYxUserAccountPageList(param,detail.getUid().longValue());
        return ResponseEntity.ok(result);
    }

    @NeedLogin
    @AnonymousAccess
    @ApiOperation("B端：线上交易流水列表")
    @GetMapping(value = "/getUserProductAccountList")
    public ResponseEntity<Object> getUserProductAccountList(@RequestHeader(value = "token") String token, UserAccountQueryParam param) {
        // 获取登陆用户的id
        SystemUser user = getRedisUser(token);
        YxMerchantsDetail detail = yxMerchantsDetailService.getById(user.getStoreId());
        Paging<UserBillVo> result = billService.getUserProductAccountList(param, detail.getUid().longValue());
        return ResponseEntity.ok(result);
    }

    @NeedLogin
    @AnonymousAccess
    @ApiOperation("B端：修改密码")
    @PostMapping(value = "/updateUserPwd")
    public ResponseEntity<Object> updateUserPwd(@RequestHeader(value = "token") String token,@RequestBody UserUpdateParam param) {
        // 获取登陆用户的id
        SystemUser user = getRedisUser(token);
        // 密码解密
        String oldPass = AESUtils.AES_Decrypt(param.getOldPass(), aesKey, aesMode, iv);

        String newPass = AESUtils.AES_Decrypt(param.getNewPass(), aesKey, aesMode, iv);

        String okNewPass = AESUtils.AES_Decrypt(param.getOkNewPass(), aesKey, aesMode, iv);
        if (StringUtils.isBlank(oldPass) || StringUtils.isBlank(newPass) || StringUtils.isBlank(okNewPass) ) {
            throw new BadRequestException("密码错误");
        }
        if (!newPass.equals(okNewPass) ) {
            throw new BadRequestException("两次输入的密码不一致");
        }

        SystemUser db_user = yxUserService.getSystemUserByUserNameNew(user.getUsername());
        if (user == null || user.getId() == null) {
            throw new BadRequestException("用户名或密码错误");
        }

        String pass = db_user.getUserpassword();
        if (!PassWordUtil.getUserPassWord(oldPass, db_user.getUserRole(), db_user.getUsername()).equals(pass)) {
            throw new BadRequestException("密码错误");
        }

        String userPass = PassWordUtil.getUserPassWord(newPass, user.getUserRole(), user.getUsername());
        yxUserService.updatePass(user.getUsername(), newPass, userPass);


        // 返回
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", "1");
        map.put("statusDesc", "操作成功");

        return ResponseEntity.ok(map);
    }
}
