/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.wechat.web.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.hutool.core.util.StrUtil;
import co.yixiang.common.api.ApiResult;
import co.yixiang.common.util.WxUtils;
import co.yixiang.exception.ErrorRequestException;
import co.yixiang.modules.user.entity.YxUser;
import co.yixiang.modules.user.service.YxUserService;
import co.yixiang.modules.user.service.YxWechatUserService;
import co.yixiang.modules.user.web.vo.YxUserQueryVo;
import co.yixiang.modules.wechat.web.param.BindPhoneParam;
import co.yixiang.modules.wechat.web.param.WxPhoneParam;
import co.yixiang.mp.config.ShopKeyUtils;
import co.yixiang.utils.RedisUtil;
import co.yixiang.utils.RedisUtils;
import co.yixiang.utils.SecurityUtils;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author hupeng
 * @date 2020/02/07
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(value = "微信其他", tags = "微信:微信其他", description = "微信其他")
public class WxMaUserController {

    private final WxMaService wxMaService;
    private final YxWechatUserService wechatUserService;
    private final YxUserService userService;
    private final RedisUtils redisUtils;


    @PostMapping("/binding")
    @ApiOperation(value = "公众号绑定手机号", notes = "公众号绑定手机号")
    public ApiResult<String> verify(@Validated @RequestBody BindPhoneParam param) {

        Object codeObj = redisUtils.get("code_" + param.getPhone());
        if (codeObj == null) {
            return ApiResult.fail("请先获取验证码");
        }
        String code = codeObj.toString();


        if (!StrUtil.equals(code, param.getCaptcha())) {
            return ApiResult.fail("验证码错误");
        }

        int uid = SecurityUtils.getUserId().intValue();
        YxUserQueryVo userQueryVo = userService.getYxUserById(uid);
        if (StrUtil.isNotBlank(userQueryVo.getPhone())) {
            return ApiResult.fail("您的账号已经绑定过手机号码");
        }

        YxUser yxUser = new YxUser();
        yxUser.setPhone(param.getPhone());
        yxUser.setUid(uid);
        userService.updateById(yxUser);

        return ApiResult.ok("绑定成功");

    }


    @PostMapping("/wxapp/binding")
    @ApiOperation(value = "小程序绑定手机号", notes = "小程序绑定手机号")
    public ApiResult<Map<String, Object>> phone(@Validated @RequestBody WxPhoneParam param) {
        log.info("授权手机号" + JSONObject.toJSONString(param));
        int uid = SecurityUtils.getUserId().intValue();
        YxUserQueryVo userQueryVo = userService.getYxUserById(uid);
        if (StrUtil.isNotBlank(userQueryVo.getPhone())) {
            return ApiResult.fail("您的账号已经绑定过手机号码");
        }

        //读取redis配置
        String appId = RedisUtil.get(ShopKeyUtils.getWxAppAppId());
        String secret = RedisUtil.get(ShopKeyUtils.getWxAppSecret());
        if (StrUtil.isBlank(appId) || StrUtil.isBlank(secret)) {
            throw new ErrorRequestException("请先配置小程序");
        }
        String phone = "";
        try {
            JSONObject userJSONObject = WxUtils.getUserInfo(param.getCode(), appId, secret);
            if (userJSONObject != null) {
                JSONObject phoneJSONObject = WxUtils.decryptPhoneData(userJSONObject.getString("session_key"), param.getEncryptedData(), param.getIv());
                if (phoneJSONObject != null) {
                    phone = phoneJSONObject.getString("phoneNumber");
                    YxUser yxUser = new YxUser();
                    yxUser.setPhone(phone);
                    yxUser.setUid(uid);
                    userService.updateById(yxUser);
                }
            }
        } catch (Exception e) {
            return ApiResult.fail(e.getMessage());
        }
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("phone", phone);

        return ApiResult.ok(map, "绑定成功");
    }


}
