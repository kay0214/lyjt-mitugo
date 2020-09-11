/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package co.yixiang.modules.couponUse.controller;

import cn.hutool.core.util.IdUtil;
import co.yixiang.annotation.AnonymousAccess;
import co.yixiang.common.api.ApiResult;
import co.yixiang.common.constant.CommonConstant;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.common.web.param.QueryParam;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.constant.ShopConstants;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.exception.BadRequestException;
import co.yixiang.logging.aop.log.Log;
import co.yixiang.modules.couponUse.criteria.YxCouponOrderUseQueryCriteria;
import co.yixiang.modules.couponUse.dto.UserBillVo;
import co.yixiang.modules.couponUse.dto.YxStoreInfoDto;
import co.yixiang.modules.couponUse.param.UserAccountQueryParam;
import co.yixiang.modules.coupons.service.YxCouponOrderService;
import co.yixiang.modules.coupons.service.YxCouponOrderUseService;
import co.yixiang.modules.coupons.service.YxCouponsService;
import co.yixiang.modules.coupons.web.param.YxCouponsQueryParam;
import co.yixiang.modules.coupons.web.vo.YxCouponsQueryVo;
import co.yixiang.modules.image.service.YxImageInfoService;
import co.yixiang.modules.manage.entity.SystemUser;
import co.yixiang.modules.manage.entity.YxMerchantsDetail;
import co.yixiang.modules.manage.service.YxMerchantsDetailService;
import co.yixiang.modules.security.security.vo.AuthUser;
import co.yixiang.modules.shop.entity.YxStoreInfo;
import co.yixiang.modules.shop.service.YxStoreInfoService;
import co.yixiang.modules.user.entity.YxUserBill;
import co.yixiang.modules.user.service.YxUserBillService;
import co.yixiang.modules.user.service.YxUserService;
import co.yixiang.modules.user.web.vo.YxUserBillQueryVo;
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
import javax.validation.Valid;
import java.util.*;
import java.util.concurrent.TimeUnit;

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


    private final IGenerator generator;

    public UserAccountController(IGenerator generator) {
        this.generator = generator;
    }



    @AnonymousAccess
    @ApiOperation("B端：线下交易流水列表")
    @GetMapping(value = "/getUserAccountList")
    public ResponseEntity<Object> getUserAccountList (@RequestHeader(value = "token") String token,
                                                              UserAccountQueryParam param) {

        // 获取登陆用户的id
        Map<String, Object> map = new HashMap<>();
        SystemUser user = getRedisUser(token);
        if (null == user) {
            map.put("status", "999");
            map.put("statusDesc", "请先登录");
            return ResponseEntity.ok(map);
        }

        Paging<YxUserBillQueryVo> result = billService.getYxUserAccountPageList(param,user.getId());


        return ResponseEntity.ok(getResultList(result));
    }

    @AnonymousAccess
    @ApiOperation("B端：线上交易流水列表")
    @GetMapping(value = "/getUserProductAccountList")
    public ResponseEntity<Object> getUserProductAccountList (@RequestHeader(value = "token") String token,
                                                      UserAccountQueryParam param) {

        // 获取登陆用户的id
        Map<String, Object> map = new HashMap<>();
        SystemUser user = getRedisUser(token);
        if (null == user) {
            map.put("status", "999");
            map.put("statusDesc", "请先登录");
            return ResponseEntity.ok(map);
        }

        Paging<YxUserBillQueryVo> result = billService.getUserProductAccountList(param,user.getId());


        return ResponseEntity.ok(getResultList(result));
    }

    private Paging<UserBillVo> getResultList(Paging<YxUserBillQueryVo> result) {
        Paging<UserBillVo> resultStr = new Paging<UserBillVo>();
        resultStr.setSum(result.getSum());
        resultStr.setTotal(result.getTotal());
        if(result.getRecords()!=null){
            List<UserBillVo> list = new ArrayList<>();
            for (YxUserBillQueryVo item :result.getRecords()) {
                UserBillVo userBillVo = CommonsUtils.convertBean(item,UserBillVo.class);
                userBillVo.setAddTimeStr(DateUtils.timestampToStr10(item.getAddTime()));
                list.add(userBillVo);
            }
            resultStr.setRecords(list);
        }
        return  resultStr;
    }
    /**
     * 从redis里面获取用户
     *
     * @param token
     * @return
     */
    private SystemUser getRedisUser(String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        if (redisUtils.hasKey(token)) {
            return (SystemUser) redisUtils.get(token);
        }
        return null;
    }

}
