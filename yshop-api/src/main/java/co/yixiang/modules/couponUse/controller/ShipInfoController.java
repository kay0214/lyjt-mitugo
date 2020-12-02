package co.yixiang.modules.couponUse.controller;

import co.yixiang.annotation.AnonymousAccess;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.modules.couponUse.param.*;
import co.yixiang.modules.manage.entity.SystemUser;
import co.yixiang.modules.ship.service.YxShipInfoService;
import co.yixiang.modules.ship.web.param.YxShipOperationDetailQueryParam;
import co.yixiang.modules.ship.web.param.YxShipOperationQueryParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * <p>
 * 船只表 前端控制器
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@RestController
@RequestMapping("/shipInfo")
@Api(value = "核销端：船只信息", tags = "核销端：船只信息")
public class ShipInfoController extends BaseController {

    @Autowired
    private YxShipInfoService yxShipInfoService;

    @AnonymousAccess
    @PostMapping("/getShipInfoList")
    @ApiOperation(value = "船只列表",notes = "船只列表")
    public ResponseEntity<Object> getYxShipInfoPageList(@Valid @RequestBody(required = false) ShipInfoQueryParam shipInfoQueryParam,@RequestHeader(value = "token") String token) {
        SystemUser user = getRedisUser(token);
        Map<String, Object> map = yxShipInfoService.getShipInfoList(shipInfoQueryParam, user);
        return ResponseEntity.ok(map);

    }

    /**
     * 修改船只状态
     * @param shipInfoChangeParam
     * @param token
     * @return
     */
    @AnonymousAccess
    @PostMapping("/changeShipStatus")
    @ApiOperation(value = "船只（回港、维修中）",notes = "船只（回港、维修中）")
    public ResponseEntity<Object> changeShipStatus(@Valid @RequestBody ShipInfoChangeParam shipInfoChangeParam, @RequestHeader(value = "token") String token) {
        SystemUser user = getRedisUser(token);
        Map<String, Object> map = yxShipInfoService.changeStatus(shipInfoChangeParam,user.getId().intValue());
        return ResponseEntity.ok(map);
    }

    @AnonymousAccess
    @PostMapping("/captainOutPortList")
    @ApiOperation(value = "船长出港列表展示",notes = "船长出港列表展示")
    public ResponseEntity<Object> captainOutPortList(@RequestHeader(value = "token") String token,@RequestBody ShipOperationParam shipOperationParam) {
        SystemUser user = getRedisUser(token);
        YxShipOperationQueryParam yxShipOperationQueryParam = new YxShipOperationQueryParam();

        Map<String,Object> map = yxShipInfoService.getShipOperationList(yxShipOperationQueryParam, shipOperationParam, user.getId().intValue(),null);
        return ResponseEntity.ok(map);
    }

    @AnonymousAccess
    @PostMapping("/captainOutPort")
    @ApiOperation(value = "船长确认出港",notes = "船长确认出港")
    public ResponseEntity<Object> captainOutPort(@RequestHeader(value = "token") String token,@RequestBody ShipOutInParam mapParam) {
        SystemUser user = getRedisUser(token);
        Map<String,Object> map = yxShipInfoService.updateCaptainLeave(user.getId().intValue(),mapParam);
        return ResponseEntity.ok(map);
    }

    @AnonymousAccess
    @PostMapping("/getShipOperationList")
    @ApiOperation(value = "船只运营记录",notes = "船只运营记录")
    public ResponseEntity<Object> getShipOperationList(@RequestHeader(value = "token") String token, @RequestBody ShipOperationParam shipOperationParam) throws Exception {
        SystemUser user = getRedisUser(token);
        YxShipOperationQueryParam yxShipOperationQueryParam = new YxShipOperationQueryParam();
        Map<String,Object> map = yxShipInfoService.getShipOperationList(yxShipOperationQueryParam, shipOperationParam, null,user.getStoreId());
        return ResponseEntity.ok(map);
    }

    @AnonymousAccess
    @PostMapping("/getShipOperationDeatalList")
    @ApiOperation(value = "船只订单详情",notes = "船只订单详情")
    public ResponseEntity<Object> getShipOperationDeatalList(@RequestBody YxShipOperationDetailQueryParam param) throws Exception {
        Map<String,Object> map = yxShipInfoService.getShipOperationDeatalList(param);
        return ResponseEntity.ok(map);
    }

    @AnonymousAccess
    @PostMapping("/modifyCaptain")
    @ApiOperation(value = "修改船长",notes = "修改船长")
    public ResponseEntity<Object> modifyCaptain(@RequestHeader(value = "token") String token,@RequestBody ShipCaptainModifyParam param) throws Exception {
        SystemUser user = getRedisUser(token);
        Map<String,Object> map = yxShipInfoService.modifyCaptain(param,user.getId().intValue());
        return ResponseEntity.ok(map);
    }

    @AnonymousAccess
    @PostMapping("/getShipLeaveRecord")
    @ApiOperation(value = "今日出海记录",notes = "今日出海记录")
    public ResponseEntity<Object> getShipLeaveRecord(@RequestHeader(value = "token") String token, @RequestBody ShipOperationParam shipOperationParam) throws Exception {
        SystemUser user = getRedisUser(token);
        YxShipOperationQueryParam yxShipOperationQueryParam = new YxShipOperationQueryParam();
        yxShipOperationQueryParam.setStatus(3);
        Map<String,Object> map = yxShipInfoService.getShipOperationList(yxShipOperationQueryParam, shipOperationParam, user.getId().intValue(),user.getStoreId());
        return ResponseEntity.ok(map);
    }

    @AnonymousAccess
    @PostMapping("/sendEmail")
    @ApiOperation(value = "发送邮件",notes = "发送邮件")
    public ResponseEntity<Object> sendEmail(@RequestBody Map<String,String> mapParam, @RequestHeader(value = "token") String token) {
        SystemUser user = getRedisUser(token);
        String batchNo = mapParam.get("batchNo");
        Map<String, Object> map =yxShipInfoService.sendEmail(batchNo);
        return ResponseEntity.ok(map);
    }
}

