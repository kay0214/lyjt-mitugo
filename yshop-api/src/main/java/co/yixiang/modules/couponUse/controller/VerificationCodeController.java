/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package co.yixiang.modules.couponUse.controller;

import co.yixiang.annotation.AnonymousAccess;
import co.yixiang.aspectj.annotation.NeedLogin;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.exception.BadRequestException;
import co.yixiang.logging.aop.log.Log;
import co.yixiang.modules.couponUse.dto.AllShipsVO;
import co.yixiang.modules.couponUse.dto.ShipUserVO;
import co.yixiang.modules.coupons.service.YxCouponOrderService;
import co.yixiang.modules.coupons.service.YxCouponsService;
import co.yixiang.modules.manage.entity.SystemUser;
import co.yixiang.modules.ship.service.YxShipSeriesService;
import co.yixiang.modules.user.service.YxUserService;
import co.yixiang.utils.Base64Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController(value = "ShipVerificationCodeController")
@RequestMapping("/work/ship")
@Api(value = "核销端船只相关操作")
public class VerificationCodeController extends BaseController {


    @Autowired
    private YxCouponOrderService yxCouponOrderService;

    @Autowired
    private YxUserService yxUserService;
    @Autowired
    private YxShipSeriesService yxShipSeriesService;
    @Autowired
    private YxCouponsService yxCouponsService;


    // shipId 船只id   shipUserId  船长id
    @NeedLogin
    @AnonymousAccess
    @Log("船票核销")
    @ApiOperation("B端：船票核销")
    @PostMapping(value = "/useCoupon")
    public ResponseEntity<Object> updateCouponOrder(@RequestHeader(value = "token") String token
            , @RequestParam(value = "verifyCode") String verifyCode
            , @RequestParam(value = "shipId") Integer shipId
            , @RequestParam(value = "shipUserId") Integer shipUserId) {
        // 获取登陆用户的id
        SystemUser user = getRedisUser(token);
        int uid = user.getId().intValue();
        String decodeVerifyCode = "";
        try {
            decodeVerifyCode = Base64Utils.decode(verifyCode);
        } catch (Exception e) {
            throw new BadRequestException("无效卡券");
        }
        Map<String, Object> result = this.yxCouponOrderService.updateShipCouponOrder(decodeVerifyCode, uid,shipId,shipUserId,user);
        return ResponseEntity.ok(result);
    }

    @NeedLogin
    @ApiOperation("B端：扫码获取船票订单信息")
    @AnonymousAccess
    @PostMapping(value = "/getShipCouponInfo")
    public ResponseEntity<Object> getShipCouponInfo(@RequestHeader(value = "token") String token
            , @RequestParam(value = "verifyCode") String verifyCode) {
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

        // TODO: 2020/11/8
        return ResponseEntity.ok(this.yxCouponsService.getShipCouponInfo(decodeVerifyCode, user));
    }


    // 获取商户所有的船长
    @NeedLogin
    @AnonymousAccess
    @ApiOperation("B端：获取本商户所有船长")
    @PostMapping(value = "/getAllShipUser")
    public ResponseEntity<Object> getAllShipUser(@RequestHeader(value = "token") String token) {
        // 获取登陆用户的id
        Map<String, String> map = new HashMap<>();
        SystemUser user = getRedisUser(token);

        int storeId = user.getStoreId();

        List<ShipUserVO> result = this.yxUserService.getAllShipUserByStoreId(storeId);
        return ResponseEntity.ok(result);
    }

    // 获取商户所有的船只系列和船只
    @NeedLogin
    @AnonymousAccess
    @ApiOperation("B端：获取本商户所有船只系列和船只")
    @PostMapping(value = "/getAllShip")
    public ResponseEntity<Object> getAllShip(@RequestHeader(value = "token") String token) {
        // 获取登陆用户的id
        Map<String, String> map = new HashMap<>();
        SystemUser user = getRedisUser(token);
        int storeId = user.getStoreId();
        List<AllShipsVO> list = yxShipSeriesService.getAllShipByStoreId(storeId);

        return ResponseEntity.ok(list);
    }
}
